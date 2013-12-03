package city.roles;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangRevolvingStand;
import utilities.RestaurantZhangTable;
import city.Building;
import city.Role;
import city.animations.interfaces.RestaurantZhangAnimatedWaiter;
import city.interfaces.RestaurantZhangCashier;
import city.interfaces.RestaurantZhangCook;
import city.interfaces.RestaurantZhangCustomer;
import city.interfaces.RestaurantZhangHost;
import city.interfaces.RestaurantZhangWaiter;

public abstract class RestaurantZhangWaiterBase extends Role implements RestaurantZhangWaiter {
	
	// Data
	
	private Timer timer = new Timer();
	private Semaphore atTable = new Semaphore(0, false);
	private boolean restaurantClosing = false;
	
	// TODO Change these to private and add getters/setters
	public List<MyCustomer> myCustomerList = new ArrayList<MyCustomer>();
	public int numberCustomersServed = 0; // Host uses this to decide which waiter to choose
	public RestaurantZhangCook myCook;
	public RestaurantZhangHost myHost;
	public RestaurantZhangCashier myCashier;
	public RestaurantZhangMenu waiterMenu;
	public RestaurantZhangRevolvingStand myOrderStand;
	public RestaurantZhangAnimatedWaiter thisGui;
	public List<RestaurantZhangCheck> checkList = new ArrayList<RestaurantZhangCheck>();
	public enum breakStatus {notOnBreak, wantToBreak, goingOnBreak, onBreak};
	public breakStatus wBreakStatus = breakStatus.notOnBreak;
	
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
					GetCheckForCustomer(mc);
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
				GoOnBreak();
				return false;
			} else if (wBreakStatus != breakStatus.onBreak) {
				DoingNothing(); // If waiter has no more tasks
			}
			return false;
		} catch(ConcurrentModificationException e) {
			print("CME error caught, returning true");
			return true;
		}
	}

	// Actions
	
	private void seatCustomer(MyCustomer mc) {
		print("Going to entrance");
		DoGoToEntrance(mc.customer);
		WaitForAnimation();
		mc.customer.msgFollowMe(this, waiterMenu, mc.table);
		print("Seating " + mc.customer + " at " + mc.table);
		DoGoToTable(mc.table);
		mc.state = MyCustomer.STATE.deciding;
		WaitForAnimation();
		stateChanged();
	}
	
	private void takeOrderFromCustomer(MyCustomer mc) {
		print("Going to customer " + mc.customer.getPerson().getName() + " to take order");
		DoGoToTable(mc.table);
		WaitForAnimation();
		mc.state = MyCustomer.STATE.ordering;
		mc.customer.msgWhatWouldYouLike();
	}
	
	private void waitForOrder(MyCustomer mc) {
		print("Waiting for customer " + mc.customer.getPerson().getName() + " to take order");
		stateChanged();
	}
	
	protected abstract void sendOrderToCook(MyCustomer mc, String choice, RestaurantZhangTable t);
	
	private void tellCustomerDecideAgain(MyCustomer mc) {
		print("Telling customer to reorder");
		DoGoToTable(mc.table);
		WaitForAnimation();
		mc.state = MyCustomer.STATE.deciding;
		mc.customer.msgOrderAgain();
	}
	
	private void serveCustomer(MyCustomer mc) {
		print("Going to cook to get food for customer " + mc.customer.getPerson().getName());
		DoGoToCook();
		WaitForAnimation();
		myCook.msgGotCompletedOrder(mc.table);
		print("Going to customer "+ mc.customer.getPerson().getName() + " to serve");
		thisGui.setFoodLabel(mc.choice, true);
		DoGoToTable(mc.table);
		WaitForAnimation();
		mc.customer.msgHereIsYourFood(mc.choice);
		thisGui.setFoodLabel("", true);
		mc.state = MyCustomer.STATE.eating;
	}
	
	private void GetCheckForCustomer(MyCustomer mc) {
		print("Getting check for customer " + mc.customer.getPerson().getName());
		myCashier.msgComputeBill(this, mc.customer, mc.choice);
		mc.state = MyCustomer.STATE.waitingForCheck;
	}
	
	private void giveCheckToCust(RestaurantZhangCheck c) {
		print("Giving check to customer " + ((RestaurantZhangCustomer) c.cust).getPerson().getName());
		checkList.remove(c);
		c.cust.msgHereIsCustCheck(c);
	}
	
	private void notifyHostCustomerLeaving(MyCustomer mc) {
		print("Notifying host that customer " + mc.customer.getPerson().getName() + " has left");
		myCustomerList.remove(mc);
		myHost.msgTableOpen(mc.table);
		if(restaurantClosing) {
			if(myCustomerList.isEmpty()) {
				super.setInactive();
				restaurantClosing = false;
			}
		}
	}
	
	private void DoingNothing() {
		if(DoReturnToBase()) {
			WaitForAnimation();
		}
	}
	
	private void GoOnBreak() {
		print("Going on break");
		wBreakStatus = breakStatus.onBreak;
		thisGui.GoToDestination(BREAKX, BREAKY);
		WaitForAnimation();
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
	private void DoGoToEntrance(RestaurantZhangCustomer c) {
		thisGui.GoToCustomer(c.getPos());
	}
	
	private void DoGoToTable(RestaurantZhangTable table) {
		thisGui.GoToTable(table);
	}
	
	protected void DoGoToCook() {
		thisGui.GoToDestination(RestaurantZhangCook.COOKX, RestaurantZhangCook.COOKY);
	}
	
	private boolean DoReturnToBase() {
		return thisGui.ReturnToBase();
	}

	protected void WaitForAnimation() {
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// Getters
	
	public RestaurantZhangAnimatedWaiter getAnimation() {
		return thisGui;
	}
	
	public int getNumberCustomers() {
		return myCustomerList.size();
	}
	
	public int getNumberCustomersServed() {
		return numberCustomersServed;
	}
	
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
		super.setInactive();
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

