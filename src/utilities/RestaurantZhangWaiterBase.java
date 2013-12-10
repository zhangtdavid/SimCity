package utilities;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import city.animations.interfaces.RestaurantZhangAnimatedWaiter;
import city.bases.Building;
import city.bases.JobRole;
import city.roles.RestaurantZhangHostRole;
import city.roles.interfaces.RestaurantZhangCashier;
import city.roles.interfaces.RestaurantZhangCook;
import city.roles.interfaces.RestaurantZhangCustomer;
import city.roles.interfaces.RestaurantZhangHost;
import city.roles.interfaces.RestaurantZhangWaiter;

public abstract class RestaurantZhangWaiterBase extends JobRole implements RestaurantZhangWaiter {
	
	// Data
	
	protected Timer timer = new Timer();
	protected Semaphore atTable = new Semaphore(0, false);
	protected boolean restaurantClosing = false;
	protected List<MyCustomer> myCustomerList = new ArrayList<MyCustomer>();
	protected int numberCustomersServed = 0; // Host uses this to decide which waiter to choose
	protected RestaurantZhangCook myCook;
	protected RestaurantZhangHost myHost;
	protected RestaurantZhangCashier myCashier;
	protected RestaurantZhangMenu waiterMenu;
	protected RestaurantZhangRevolvingStand myOrderStand;
	protected RestaurantZhangAnimatedWaiter thisGui;
	protected List<RestaurantZhangCheck> checkList = new ArrayList<RestaurantZhangCheck>();
	protected breakStatus wBreakStatus = breakStatus.notOnBreak;
	
	// Constructor
	
	public RestaurantZhangWaiterBase(Building restaurantToWorkAt, int shiftStart_, int shiftEnd_) {
		super();
		this.setShift(shiftStart_, shiftEnd_);
		this.setWorkplace(restaurantToWorkAt);
		this.setSalary(RESTAURANTZHANGCASHIERSALARY);
	}

	// Messages

	@Override
	public void msgSeatCustomer(RestaurantZhangTable t, RestaurantZhangCustomer c) {
		numberCustomersServed++;
		myCustomerList.add(new MyCustomer(c, t));
		stateChanged();
	}
	
	@Override
	public void msgReadyToOrder(RestaurantZhangCustomer c) {
		for(MyCustomer mc : myCustomerList) {
			if(mc.customer.equals(c)) {
				mc.state = MyCustomer.STATE.readyToOrder;
				break;
			}
		}
		stateChanged();
	}
	
	@Override
	public void msgHereIsMyChoice(RestaurantZhangCustomer c, String choice) {
		for(MyCustomer mc : myCustomerList) {
			if(mc.customer.equals(c)) {
				mc.state = MyCustomer.STATE.ordered;
				mc.choice = choice;
				break;
			}
		}
		stateChanged();
	}
	
	@Override
	public void msgOutOfFood(RestaurantZhangTable t) {
		for(MyCustomer mc : myCustomerList) {
			if(mc.table.equals(t)) {
				mc.state = MyCustomer.STATE.reOrder;
				break;
			}
		}
		stateChanged();
	}
	
	@Override
	public void msgOrderIsReady(String choice, RestaurantZhangTable t) {
		for(MyCustomer mc : myCustomerList) {
			if(mc.table.equals(t)) {
				mc.state = MyCustomer.STATE.orderReady;
				mc.choice = choice;
				break;
			}
		}
		stateChanged();
	}
	
	@Override
	public void msgHereWasMyOrder(RestaurantZhangCustomer c, String choice) {
		for(MyCustomer mc : myCustomerList) {
			if(mc.customer.equals(c)) {
				mc.state = MyCustomer.STATE.doneEating;
				break;
			}
		}
		stateChanged();
	}
	
	@Override
	public void msgHereIsWaiterCheck(RestaurantZhangCheck c) {
		checkList.add(c);
		stateChanged();
	}
	
	@Override
	public void msgLeavingTable(RestaurantZhangCustomer c) {
		for(MyCustomer mc : myCustomerList) {
			if(mc.customer.equals(c)) {
				mc.state = MyCustomer.STATE.leaving;
				break;
			}
		}
		stateChanged();
	}
	
	@Override
	public void msgGoOnBreak(boolean canGoOnBreak) {
		if(canGoOnBreak) {
			wBreakStatus = breakStatus.goingOnBreak;
		} else {
			wBreakStatus = breakStatus.notOnBreak;
		}
		stateChanged();
	}

	@Override
	public void msgAtDestination() { //from animation
		atTable.release();
		stateChanged();
	}

	// Scheduler
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean runScheduler() {
		try {
			for(MyCustomer mc : myCustomerList) {
				if(mc.state == MyCustomer.STATE.waiting) {
					seatCustomer(mc);
					return true;
				}
			}
			for(MyCustomer mc : myCustomerList) {
				if(mc.state == MyCustomer.STATE.ordering) {
					waitForOrder(mc);
					return true;
				}
			}
			for(MyCustomer mc : myCustomerList) {
				if(mc.state == MyCustomer.STATE.ordered) {
					sendOrderToCook(mc, mc.choice, mc.table);
					return true;
				}
			}
			for(MyCustomer mc : myCustomerList) {
				if(mc.state == MyCustomer.STATE.readyToOrder) {
					takeOrderFromCustomer(mc);
					return true;
				}
			}
			for(MyCustomer mc : myCustomerList) {
				if(mc.state == MyCustomer.STATE.reOrder) {
					tellCustomerDecideAgain(mc);
					return true;
				}
			}
			for(MyCustomer mc : myCustomerList) {
				if(mc.state == MyCustomer.STATE.orderReady) {
					serveCustomer(mc);
					return true;
				}
			}
			for(MyCustomer mc : myCustomerList) {
				if(mc.state == MyCustomer.STATE.doneEating) {
					getCheckForCustomer(mc);
					return true;
				}
			}
			for(RestaurantZhangCheck c : checkList) {
				giveCheckToCust(c);
				return true;
			}
			for(MyCustomer mc : myCustomerList) {
				if(mc.state == MyCustomer.STATE.leaving) {
					notifyHostCustomerLeaving(mc);
					return true;
				}
			}
			if(myCustomerList.isEmpty() && wBreakStatus == breakStatus.goingOnBreak) {
				goOnBreak();
				return false;
			} else if (wBreakStatus != breakStatus.onBreak) {
				doingNothing(); // If waiter has no more tasks
			}
			return false;
		} catch(ConcurrentModificationException e) {
			print("CME error caught, returning true");
			return true;
		}
	}

	// Actions
	
	protected void seatCustomer(MyCustomer mc) {
		print("Going to entrance");
		doGoToEntrance(mc.customer);
		waitForAnimation();
		mc.customer.msgFollowMe(this, waiterMenu, mc.table);
		print("Seating " + mc.customer + " at " + mc.table);
		doGoToTable(mc.table);
		mc.state = MyCustomer.STATE.deciding;
		waitForAnimation();
		stateChanged();
	}
	
	protected void takeOrderFromCustomer(MyCustomer mc) {
		print("Going to customer " + mc.customer.getPerson().getName() + " to take order");
		doGoToTable(mc.table);
		waitForAnimation();
		mc.state = MyCustomer.STATE.ordering;
		mc.customer.msgWhatWouldYouLike();
	}
	
	protected void waitForOrder(MyCustomer mc) {
		print("Waiting for customer " + mc.customer.getPerson().getName() + " to take order");
		stateChanged();
	}
	
	protected abstract void sendOrderToCook(MyCustomer mc, String choice, RestaurantZhangTable t);
	
	protected void tellCustomerDecideAgain(MyCustomer mc) {
		print("Telling customer to reorder");
		doGoToTable(mc.table);
		waitForAnimation();
		mc.state = MyCustomer.STATE.deciding;
		mc.customer.msgOrderAgain();
	}
	
	protected void serveCustomer(MyCustomer mc) {
		print("Going to cook to get food for customer " + mc.customer.getPerson().getName());
		doGoToCook();
		waitForAnimation();
		myCook.msgGotCompletedOrder(mc.table);
		print("Going to customer "+ mc.customer.getPerson().getName() + " to serve");
		thisGui.setFoodLabel(mc.choice, true);
		doGoToTable(mc.table);
		waitForAnimation();
		mc.customer.msgHereIsYourFood(mc.choice);
		thisGui.setFoodLabel("", true);
		mc.state = MyCustomer.STATE.eating;
	}
	
	protected void getCheckForCustomer(MyCustomer mc) {
		print("Getting check for customer " + mc.customer.getPerson().getName());
		myCashier.msgComputeBill(this, mc.customer, mc.choice);
		mc.state = MyCustomer.STATE.waitingForCheck;
	}
	
	protected void giveCheckToCust(RestaurantZhangCheck c) {
		print("Giving check to customer " + ((RestaurantZhangCustomer) c.cust).getPerson().getName());
		checkList.remove(c);
		c.cust.msgHereIsCustCheck(c);
	}
	
	protected void notifyHostCustomerLeaving(MyCustomer mc) {
		print("Notifying host that customer " + mc.customer.getPerson().getName() + " has left");
		myCustomerList.remove(mc);
		myHost.msgTableOpen(mc.table);
		if(restaurantClosing) {
			if(myCustomerList.isEmpty()) {
				setInactive();
				restaurantClosing = false;
			}
		}
	}
	
	protected void doingNothing() {
		if(doReturnToBase()) {
			waitForAnimation();
		}
	}
	
	protected void goOnBreak() {
		print("Going on break");
		wBreakStatus = breakStatus.onBreak;
		thisGui.GoToDestination(BREAKX, BREAKY);
		waitForAnimation();
		timer.schedule(new TimerTask() {
			public void run() {
				print("Returned from break");
				myHost.msgOffBreak(RestaurantZhangWaiterBase.this);
				wBreakStatus = breakStatus.notOnBreak;
				stateChanged();
			}
		},
		BREAKTIME);
	}
	
	// The animation DoXYZ() routines
	protected void doGoToEntrance(RestaurantZhangCustomer c) {
		thisGui.GoToCustomer(c.getWaitingPosition());
	}
	
	protected void doGoToTable(RestaurantZhangTable table) {
		thisGui.GoToTable(table);
	}
	
	protected void doGoToCook() {
		thisGui.GoToDestination(RestaurantZhangCook.COOKX, RestaurantZhangCook.COOKY);
	}
	
	protected boolean doReturnToBase() {
		return thisGui.ReturnToBase();
	}

	protected void waitForAnimation() {
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// Getters
	
	@Override
	public List<MyCustomer> getCustomerList() {
		return myCustomerList;
	}
	
	@Override
	public int getNumCustomersServed() {
		return numberCustomersServed;
	}
	
	@Override
	public RestaurantZhangCook getCook() {
		return myCook;
	}
	
	@Override
	public RestaurantZhangHost getHost() {
		return myHost;
	}
	
	@Override
	public RestaurantZhangCashier getCashier() {
		return myCashier;
	}
	
	@Override
	public RestaurantZhangMenu getMenu() {
		return waiterMenu;
	}
	
	@Override
	public RestaurantZhangRevolvingStand getOrderStand() {
		return myOrderStand;
	}
	
	@Override
	public List<RestaurantZhangCheck> getCheckList() {
		return checkList;
	}
	
	@Override
	public breakStatus getBreakStatus() {
		return wBreakStatus;
	}
	
	@Override
	public RestaurantZhangAnimatedWaiter getAnimation() {
		return thisGui;
	}
	
	@Override
	public int getNumberCustomers() {
		return myCustomerList.size();
	}
	
	@Override
	public boolean getOnBreak() {
		if(wBreakStatus == breakStatus.notOnBreak || wBreakStatus == breakStatus.wantToBreak) {
			return false;
		} else {
			return true;
		}
	}
	
	// Setters

	@Override
	public void setAnimation(RestaurantZhangAnimatedWaiter gui) {
		thisGui = gui;
	}
	
	@Override
	public void setCook(RestaurantZhangCook c) {
		myCook = c;
	}
	
	@Override
	public void setHost(RestaurantZhangHost h) {
		myHost = h;
	}
	
	@Override
	public void setCashier(RestaurantZhangCashier c) {
		myCashier = c;
	}
	
	@Override
	public void setMenu(RestaurantZhangMenu m) {
		waiterMenu = m;
	}
	
	@Override
	public void setRevolvingStand(RestaurantZhangRevolvingStand rs) {
		myOrderStand = rs;
	}
	
	@Override
	public void setInactive() {
		if(myHost != null) {
			if(((RestaurantZhangHostRole)myHost).getNumberOfCustomersInRestaurant() !=0) {
				restaurantClosing = true;
				return;
			}
		}
		thisGui.GoToDestination(-20, -20);
		waitForAnimation();
		thisGui.setVisible(false);
		super.setInactive();
	}
	
	@Override
	public void setActive() {
		super.setActive();
		thisGui.setVisible(true);
		thisGui.GoToDestination(thisGui.getBaseX(), thisGui.getBaseY());
		waitForAnimation();
		runScheduler();
	}

	// Utilities
	
	// Classes
	
	public static class MyCustomer {
		public enum STATE {waiting, seating, deciding, readyToOrder, ordering, ordered, reOrder, orderCooking, orderReady, eating, doneEating, waitingForCheck, leaving};
		public RestaurantZhangCustomer customer;
		public RestaurantZhangTable table;
		public String choice = null;
		public STATE state;
		
		MyCustomer(RestaurantZhangCustomer c, RestaurantZhangTable t) {
			customer = c;
			table = t;
			state = STATE.waiting;
		}
	}
}

