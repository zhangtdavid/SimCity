package city.interfaces;
import java.util.*;
import java.util.concurrent.Semaphore;

import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangRevolvingStand;
import utilities.RestaurantZhangTable;
import city.Building;
import city.Role;
import city.animations.interfaces.RestaurantZhangAnimatedWaiter;

public abstract class RestaurantZhangWaiterBase extends Role implements RestaurantZhangWaiter {
	// Customers
	
	public List<MyCustomer> myCustomerList = new ArrayList<MyCustomer>();
	public int numberCustomersServed = 0; // Host uses this to decide which waiter to choose
	// Other employees
	public RestaurantZhangCook myCook;
	public RestaurantZhangHost myHost;
	public RestaurantZhangCashier myCashier;
	//Menu
	public RestaurantZhangMenu waiterMenu;
	// Revolving stand
	public RestaurantZhangRevolvingStand myOrderStand;
	// GUI
	public RestaurantZhangAnimatedWaiter thisGui;
	
	public List<RestaurantZhangCheck> checkList = new ArrayList<RestaurantZhangCheck>();
	
	protected String name;
	
	public enum breakStatus {notOnBreak, wantToBreak, goingOnBreak, onBreak};
	public breakStatus wBreakStatus = breakStatus.notOnBreak;
	
	Timer timer = new Timer(); // Timer for waiting actions
	Semaphore atTable = new Semaphore(0, false);
	
	static final int BREAKX = 600;
	static final int BREAKY = 600;
	static final int BREAKTIME = 10000;
	private static final int RESTAURANTZHANGCASHIERSALARY = 100;
	
	public RestaurantZhangWaiterBase(Building restaurantToWorkAt, int shiftStart_, int shiftEnd_) {
		super();
		this.setShift(shiftStart_, shiftEnd_);
		this.setWorkplace(restaurantToWorkAt);
		this.setSalary(RESTAURANTZHANGCASHIERSALARY);
	}

	public String getName() {
		return super.getPerson().getName();
	}

	// Messages

	public void msgSeatCustomer(RestaurantZhangTable t, RestaurantZhangCustomer c) {
		numberCustomersServed++;
		myCustomerList.add(new MyCustomer(c, t));
		stateChanged();
	}
	
	public void msgReadyToOrder(RestaurantZhangCustomer c) {
		for(MyCustomer mc : myCustomerList) {
			if(mc.customer.equals(c)) {
				mc.state = mcState.readyToOrder;
				break;
			}
		}
		stateChanged();
	}
	
	public void msgHereIsMyChoice(RestaurantZhangCustomer c, String choice) {
		for(MyCustomer mc : myCustomerList) {
			if(mc.customer.equals(c)) {
				mc.state = mcState.ordered;
				mc.choice = choice;
				break;
			}
		}
		stateChanged();
	}
	
	public void msgOutOfFood(RestaurantZhangTable t) {
		for(MyCustomer mc : myCustomerList) {
			if(mc.table.equals(t)) {
				mc.state = mcState.reOrder;
				break;
			}
		}
		stateChanged();
	}
	
	public void msgOrderIsReady(String choice, RestaurantZhangTable t) {
		for(MyCustomer mc : myCustomerList) {
			if(mc.table.equals(t)) {
				mc.state = mcState.orderReady;
				mc.choice = choice;
				break;
			}
		}
		stateChanged();
	}
	
	public void msgHereWasMyOrder(RestaurantZhangCustomer c, String choice) {
		for(MyCustomer mc : myCustomerList) {
			if(mc.customer.equals(c)) {
				mc.state = mcState.doneEating;
				break;
			}
		}
		stateChanged();
	}
	
	public void msgHereIsWaiterCheck(RestaurantZhangCheck c) {
		checkList.add(c);
		stateChanged();
	}
	
	public void msgLeavingTable(RestaurantZhangCustomer c) {
		for(MyCustomer mc : myCustomerList) {
			if(mc.customer.equals(c)) {
				mc.state = mcState.leaving;
				break;
			}
		}
		stateChanged();
	}
	
	public void msgGoOnBreak(boolean canGoOnBreak) {
		if(canGoOnBreak) {
			wBreakStatus = breakStatus.goingOnBreak;
		} else {
			wBreakStatus = breakStatus.notOnBreak;
		}
		stateChanged();
	}

	public void msgAtDestination() { //from animation
		atTable.release();
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean runScheduler() {
		try {
			for(MyCustomer mc : myCustomerList) {
				if(mc.state == mcState.waiting) {
					seatCustomer(mc);
					return true;
				}
			}
			for(MyCustomer mc : myCustomerList) {
				if(mc.state == mcState.ordering) {
					waitForOrder(mc);
					return true;
				}
			}
			for(MyCustomer mc : myCustomerList) {
				if(mc.state == mcState.ordered) {
					sendOrderToCook(mc, mc.choice, mc.table);
					return true;
				}
			}
			for(MyCustomer mc : myCustomerList) {
				if(mc.state == mcState.readyToOrder) {
					takeOrderFromCustomer(mc);
					return true;
				}
			}
			for(MyCustomer mc : myCustomerList) {
				if(mc.state == mcState.reOrder) {
					tellCustomerDecideAgain(mc);
					return true;
				}
			}
			for(MyCustomer mc : myCustomerList) {
				if(mc.state == mcState.orderReady) {
					serveCustomer(mc);
					return true;
				}
			}
			for(MyCustomer mc : myCustomerList) {
				if(mc.state == mcState.doneEating) {
					GetCheckForCustomer(mc);
					return true;
				}
			}
			for(RestaurantZhangCheck c : checkList) {
				giveCheckToCust(c);
				return true;
			}
			for(MyCustomer mc : myCustomerList) {
				if(mc.state == mcState.leaving) {
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
	void seatCustomer(MyCustomer mc) {
		print("Going to entrance");
		DoGoToEntrance(mc.customer);
		WaitForAnimation();
		mc.customer.msgFollowMe(this, waiterMenu, mc.table);
		print("Seating " + mc.customer + " at " + mc.table);
		DoGoToTable(mc.table);
		mc.state = mcState.deciding;
		WaitForAnimation();
		stateChanged();
	}
	
	void takeOrderFromCustomer(MyCustomer mc) {
		print("Going to customer " + mc.customer.getName() + " to take order");
		DoGoToTable(mc.table);
		WaitForAnimation();
		mc.state = mcState.ordering;
		mc.customer.msgWhatWouldYouLike();
	}
	
	void waitForOrder(MyCustomer mc) {
		print("Waiting for customer " + mc.customer.getName() + " to take order");
		stateChanged();
	}
	
	public abstract void sendOrderToCook(MyCustomer mc, String choice, RestaurantZhangTable t);
	
	void tellCustomerDecideAgain(MyCustomer mc) {
		print("Telling customer to reorder");
		DoGoToTable(mc.table);
		WaitForAnimation();
		mc.state = mcState.deciding;
		mc.customer.msgOrderAgain();
	}
	
	void serveCustomer(MyCustomer mc) {
		print("Going to cook to get food for customer " + mc.customer.getName());
		DoGoToCook();
		WaitForAnimation();
		myCook.msgGotCompletedOrder(mc.table);
		print("Going to customer "+ mc.customer.getName() + " to serve");
		thisGui.setFoodLabel(mc.choice, true);
		DoGoToTable(mc.table);
		WaitForAnimation();
		mc.customer.msgHereIsYourFood(mc.choice);
		thisGui.setFoodLabel("", true);
		mc.state = mcState.eating;
	}
	
	void GetCheckForCustomer(MyCustomer mc) {
		print("Getting check for customer " + mc.customer.getName());
		myCashier.msgComputeBill(this, mc.customer, mc.choice);
		mc.state = mcState.waitingForCheck;
	}
	
	void giveCheckToCust(RestaurantZhangCheck c) {
		print("Giving check to customer " + ((RestaurantZhangCustomer) c.cust).getName());
		checkList.remove(c);
		c.cust.msgHereIsCustCheck(c);
	}
	
	void notifyHostCustomerLeaving(MyCustomer mc) {
		print("Notifying host that customer " + mc.customer.getName() + " has left");
		myCustomerList.remove(mc);
		myHost.msgTableOpen(mc.table);
	}
	
	void DoingNothing() {
		if(DoReturnToBase()) {
			WaitForAnimation();
		}
	}
	
	void GoOnBreak() {
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
	void DoGoToEntrance(RestaurantZhangCustomer c) {
		thisGui.GoToCustomer(c.getPos());
	}
	
	void DoGoToTable(RestaurantZhangTable table) {
		thisGui.GoToTable(table);
	}
	
	public void DoGoToCook() {
		thisGui.GoToDestination(myCook.getX(), myCook.getY());
	}
	
	boolean DoReturnToBase() {
		return thisGui.ReturnToBase();
	}

	public void WaitForAnimation() {
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//utilities

	public void setAnimation(RestaurantZhangAnimatedWaiter gui) {
		thisGui = gui;
	}

	public RestaurantZhangAnimatedWaiter getAnimation() {
		return thisGui;
	}
	
	public void setCook(RestaurantZhangCook c) {
		myCook = c;
	}
	
	public void setHost(RestaurantZhangHost h) {
		myHost = h;
	}
	
	public void setCashier(RestaurantZhangCashier c) {
		myCashier = c;
	}
	
	public void setMenu(RestaurantZhangMenu m) {
		waiterMenu = m;
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
	
	public void setRevolvingStand(RestaurantZhangRevolvingStand rs) {
		myOrderStand = rs;
	}

	public enum mcState {waiting, seating, deciding, readyToOrder, ordering, ordered, 
			reOrder, orderCooking, orderReady, eating, doneEating, waitingForCheck, leaving};
	public class MyCustomer {
		public RestaurantZhangCustomer customer;
		public RestaurantZhangTable table;
		public String choice = null;
		public mcState state;
		
		MyCustomer(RestaurantZhangCustomer c, RestaurantZhangTable t) {
			customer = c;
			table = t;
			state = mcState.waiting;
		}
	}
}

