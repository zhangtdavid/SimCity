package utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import city.animations.RestaurantChungWaiterAnimation;
import city.bases.JobRole;
import city.buildings.interfaces.RestaurantChung;
import city.roles.interfaces.RestaurantChungCustomer;
import city.roles.interfaces.RestaurantChungWaiter;

/**
 * Restaurant Waiter Agent
 */
public abstract class RestaurantChungWaiterBase extends JobRole implements RestaurantChungWaiter {
//  Data
//	=====================================================================
	public EventLog log = new EventLog();
	protected Semaphore atEntrance = new Semaphore(0, true);
	protected Semaphore atWaiterHome = new Semaphore(0, true);
	protected Semaphore atLine = new Semaphore(0, true);
	protected Semaphore atTable = new Semaphore(0, true);
	protected Semaphore atCook = new Semaphore(0, true);
	protected Semaphore atCashier = new Semaphore(0, true);
	
	protected RestaurantChung restaurant;

	protected RestaurantChungMenu menu = new RestaurantChungMenu();

	protected List<WCustomer> customers = new ArrayList<WCustomer>();
	
	protected BreakState state = BreakState.Working;
	protected WorkingState workingState = WorkingState.Working;
	
//  Constructor
//	=====================================================================
	public RestaurantChungWaiterBase() {
		super();
	}

//	Activity
//	====================================================================
	@Override
	public void setInactive() {
		workingState = WorkingState.GoingOffShift;
	}
	
//  Messages
//	=====================================================================
	//	Break
	//	---------------------------------------------------------------
	@Override
	public void msgAnimationAskedForBreak() {
		print("Waiter received gotGoOnBreak");
		state = BreakState.WantBreak;
		stateChanged();
	}
	
	@Override
	public void msgApprovedForBreak() {
		print("Waiter received msgApprovedForBreak");
		state = BreakState.ApprovedForBreak;
		stateChanged();
	}
	
	@Override
	public void msgRejectedForBreak() {
		print("Waiter received msgRejectedForBreak");
		state = BreakState.RejectedForBreak;
		stateChanged();
	}
	
	@Override
	public void msgAnimationBreakOver() {
		print("Waiter received msgAnimationBreakOver");
		state = BreakState.ReturningToWork;
		stateChanged();
	}
	
	//	Customer
	//	---------------------------------------------------------------
	@Override
	public void msgSitAtTable(RestaurantChungCustomer c, int table) {
		if (workingState != WorkingState.NotWorking) {
			print("Waiter received msgSitAtTable");
			customers.add(new WCustomer(c, table, WCustomer.CustomerState.Waiting));
			stateChanged();
		}
		// TODO inform sender of inactivity
	}
	
	@Override
	public void msgReadyToOrder(RestaurantChungCustomer c) {
		print("Waiter received msgReadyToOrder");
		WCustomer wc = findCustomer(c);
		wc.s = WCustomer.CustomerState.ReadyToOrder;
		print("customer is ready to order");
		stateChanged();
	}
	
	@Override
	public void msgHereIsMyOrder(RestaurantChungCustomer c, String choice) {
		print("Waiter received msgHereIsMyOrder");
		WCustomer wc = findCustomer(c);
		wc.o.choice = choice;
		wc.o.os = WCustomer.OrderStatus.Ordered;
		print("customer ordered " + choice);
		stateChanged();
	}
	
	@Override
	public void msgOutOfItem(String choice, int table) {
		print("Waiter received msgOutOfItem " + choice);
		WCustomer wc = findTable(table);
		wc.o.os = WCustomer.OrderStatus.Cancelled;
		menu.findItem(wc.o.choice).available = false;
		stateChanged();
	}
	
	@Override
	public void msgOrderIsReady(String choice, int table) {
		print("Waiter received msgOrderIsReady");
		WCustomer wc = findTable(table);
		wc.o.os = WCustomer.OrderStatus.DoneCooking;
		print("customer's order of " + choice + " is ready");		
		stateChanged();
	}
	
	@Override
	public void msgGetCheck(RestaurantChungCustomer c) {
		print("Waiter received msgGetCheck");
		WCustomer wc = findCustomer(c);
		wc.s = WCustomer.CustomerState.WaitingForCheck;
		stateChanged();
	}
	
	@Override
	public void msgLeaving(RestaurantChungCustomer c) {
		print("Waiter received msgLeaving");
		WCustomer wc = findCustomer(c);
		wc.s = WCustomer.CustomerState.Leaving;
		print("customer is leaving");
		stateChanged();
	}

	//	Cashier
	//	---------------------------------------------------------------
	@Override
	public void msgHereIsBill(RestaurantChungCustomer c, int price) {
		print("Waiter received msgHereIsBill");
		WCustomer wc = findCustomer(c);
		wc.bill = price;
		wc.cs = WCustomer.CheckState.ReceivedBill;	
		stateChanged();
	}
	
	//	Animation
	//	---------------------------------------------------------------
	@Override
	public void msgAnimationAtWaiterHome() {
		print("Waiter at Waiter Home");
		atWaiterHome.release();
		stateChanged();
	}
	
	@Override
	public void msgAnimationAtEntrance() {
		print("Waiter at Entrance");
		atEntrance.release();
		stateChanged();
	}
	
	@Override
	public void msgAnimationAtLine() {
		print("Waiter at Line");
		atLine.release();
		stateChanged();
	}
	
	@Override
	public void msgAnimationAtTable() {
		print("Waiter at Table");
		atTable.release();
		stateChanged();
	}
	
	@Override
	public void msgAnimationAtCook() {
		print("Waiter at Cook");
		atCook.release();
		stateChanged();
	}
	
	@Override
	public void msgAnimationAtCashier() {
		print("Waiter at Cashier");
		atCashier.release();
		stateChanged();
	}

//  Scheduler
//	=====================================================================
	@Override
	public boolean runScheduler() {	
		if (workingState == WorkingState.GoingOffShift) {
			if (customers.size() == 0)
				workingState = WorkingState.NotWorking;
		}
		
		if (state == BreakState.ReturningToWork) {
			returnToWork();
			return true;
		}
		
		if (state == BreakState.RejectedForBreak) {
			rejectForBreak();
			return true;
		}
		
		if (state == BreakState.WantBreak) {
			askForBreak();
			return true;
		}

		for (WCustomer customer : customers) {
			if (customer.o.os == WCustomer.OrderStatus.Cancelled) {
				try {
					informCustomerOfCancellation(customer);
				} catch (InterruptedException e) {}
				return true;
			}
		}
		
		for (WCustomer customer : customers) {
			if (customer.s == WCustomer.CustomerState.Waiting) {
				try {
					seatCustomer(customer);
				} catch (InterruptedException e) {}
				return true;
			}
		}
		
		for (WCustomer customer : customers) {
			if (customer.s == WCustomer.CustomerState.ReadyToOrder) {
				try {
					takeOrder(customer);
				} catch (InterruptedException e) {}
				return true;
			}
		}
		
		for (WCustomer customer : customers) {
			if (customer.o.os == WCustomer.OrderStatus.Ordered) {
				tellCookOrder(customer, customer.o.choice, customer.table);
				return true;
			}
		}
		
		for (WCustomer customer : customers) {
			if (customer.o.os == WCustomer.OrderStatus.DoneCooking) {
				try {
					pickUpOrder(customer);
				} catch (InterruptedException e) {}
				return true;
			}
		}
		
		for (WCustomer customer : customers) {
			if (customer.o.os == WCustomer.OrderStatus.PickedUp) {
				try {
					deliverOrder(customer);
				} catch (InterruptedException e) {}
				return true;
			}
		}
		
		for (WCustomer customer : customers) {
			if (customer.s == WCustomer.CustomerState.WaitingForCheck && customer.cs == WCustomer.CheckState.None) {
				try {
					getCheck(customer);
				} catch (InterruptedException e) {}
				return true;
			}
		}
		
		for (WCustomer customer : customers) {
			if (customer.cs == WCustomer.CheckState.ReceivedBill) {
				try {
					giveCheck(customer);
				} catch (InterruptedException e) {}
				return true;
			}
		}
		
		for (WCustomer customer : customers) {
			if (customer.cs == WCustomer.CheckState.DeliveredBill || customer.s == WCustomer.CustomerState.Leaving) { // Non-normative scenario when customer can't afford anything
				removeCustomer(customer);
				return true;
			}
		}

		if (workingState == WorkingState.NotWorking)
			super.setInactive();
		
		if (customers.size() == 0 && state == BreakState.ApprovedForBreak) {
			goOnBreak();
			return true;
		}
		
		return false;
		// we have tried all our rules and found nothing to do. So return false to main loop of abstract agent and wait.
	}

//  Actions
//	=====================================================================
	//	Break
	//	---------------------------------------------------------------
	private void askForBreak() {
		print("Waiter asking for break");
		state = BreakState.AskedForBreak;
		restaurant.getRestaurantChungHost().msgIWantToGoOnBreak(this);
	}	

	private void rejectForBreak() {
		this.getAnimation(RestaurantChungWaiterAnimation.class).setOffBreak();
		restaurant.getRestaurantChungHost().msgIAmReturningToWork(this);
		state = BreakState.Working;
	}
	
	private void goOnBreak() {
		print("Waiter going on break");
		state = BreakState.OnBreak;
		this.getAnimation(RestaurantChungWaiterAnimation.class).DoGoOnBreak();
	}
	
	private void returnToWork() {
		print("Waiter returning to work");
		state = BreakState.Working;
		restaurant.getRestaurantChungHost().msgIAmReturningToWork(this);
		this.getAnimation(RestaurantChungWaiterAnimation.class).DoReturnToWaiterHome();		
	}
	
	//	Customer
	//	---------------------------------------------------------------
	private void seatCustomer(WCustomer customer) throws InterruptedException {	
		print("Waiter seating " + customer.c + " at " + customer.table);
		this.getAnimation(RestaurantChungWaiterAnimation.class).DoGoToCustomerLine();
		// atEntrance.drainPermits(); // Have to drain because of multiple calls of DoReturnToEntrance() without atEntrance.acquires();
		atLine.acquire();
		restaurant.getRestaurantChungHost().msgTakingCustomerToTable(customer.c);
		this.getAnimation(RestaurantChungWaiterAnimation.class).DoBringToTable(customer.c, customer.table-1);
		customer.c.msgFollowMeToTable(this, menu);
		atTable.acquire();
		customer.s = WCustomer.CustomerState.Seated;
		this.getAnimation(RestaurantChungWaiterAnimation.class).DoReturnToWaiterHome();
	}
	
	private void informCustomerOfCancellation(WCustomer customer) throws InterruptedException {
		this.getAnimation(RestaurantChungWaiterAnimation.class).DoGoToTable(customer.table-1);
		atTable.acquire();
		customer.c.msgOutOfItem(customer.o.choice, menu);
		// Resets all menu items to available after informing a single customer
		for (int i = 0; i < menu.items.size(); i++) {
			menu.items.get(i).available = true;
		}
		customer.s = WCustomer.CustomerState.Seated;
		customer.o.os = WCustomer.OrderStatus.None;		

		this.getAnimation(RestaurantChungWaiterAnimation.class).DoReturnToWaiterHome();
	}
	
	private void takeOrder(WCustomer customer) throws InterruptedException {
		this.getAnimation(RestaurantChungWaiterAnimation.class).DoGoToTable(customer.table-1);
		atTable.acquire();
		print("taking order from " + customer.c);		
		customer.c.msgWhatWouldYouLike();
		customer.s = WCustomer.CustomerState.Asked;
		
		this.getAnimation(RestaurantChungWaiterAnimation.class).DoReturnToWaiterHome();
	}
	
	protected abstract void tellCookOrder(WCustomer customer, String choice, int table);
	
	private void pickUpOrder(WCustomer customer) throws InterruptedException {
		print("picking up order from cook");
		this.getAnimation(RestaurantChungWaiterAnimation.class).DoGoToCook();
		atCook.acquire();
		customer.o.os = WCustomer.OrderStatus.PickedUp;
	}
	
	private void deliverOrder(WCustomer customer) throws InterruptedException {
		this.getAnimation(RestaurantChungWaiterAnimation.class).DoDeliverFood(customer.table-1, customer.o.choice);
		atTable.acquire();	
		customer.o.os = WCustomer.OrderStatus.Delivered;
		customer.c.msgHereIsYourFood();
		customer.s = WCustomer.CustomerState.Eating;
		this.getAnimation(RestaurantChungWaiterAnimation.class).DoReturnToWaiterHome();
	}
	
	private void getCheck(WCustomer customer) throws InterruptedException {
		this.getAnimation(RestaurantChungWaiterAnimation.class).DoGoToCashier();
		atCashier.acquire();
		customer.cs = WCustomer.CheckState.AskedForBill;
		restaurant.getRestaurantChungCashier().msgComputeBill(this, customer.c, customer.o.choice);
	}
	
	private void giveCheck(WCustomer customer) throws InterruptedException {
		this.getAnimation(RestaurantChungWaiterAnimation.class).DoGoToTable(customer.table-1);
		atTable.acquire();	
		customer.cs = WCustomer.CheckState.DeliveredBill;
		customer.c.msgHereIsCheck(customer.bill);
		this.getAnimation(RestaurantChungWaiterAnimation.class).DoReturnToWaiterHome();
	}

	private void removeCustomer(WCustomer customer) {
		restaurant.getRestaurantChungHost().msgTableIsFree(this, customer.table, customer.c);
		removeCustomerFromList(customer);
	}
	
//  Getters and Setters
//  ====================================================================
	
	
//  Utilities
//	=====================================================================	

	@Override
	public WCustomer findCustomer(RestaurantChungCustomer ca) {
		for(WCustomer customer : customers ){
			if(customer.c == ca) {
				return customer;		
			}
		}
		return null;
	}
	
	@Override
	public WCustomer findTable(int table) {
		for(WCustomer customer : customers){
			if(customer.table == table) {
				return customer;				
			}
		}
		return null;
	}
	
	@Override
	public void removeCustomerFromList(WCustomer c) {
		for(int i = 0; i < customers.size(); i ++) {
			if(customers.get(i) == c) {
				customers.remove(c);
			}
		}
	}
	
	@Override
	public void print(String msg) {
        super.print(msg);
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTCHUNG, "RestaurantChungWaiterBaseRole " + this.getPerson().getName(), msg);
    }
	
//	Classes
//	=====================================================================	
	public static class WCustomer {
		public enum CustomerState {Waiting, Seated, ReadyToOrder, Asked, Eating, WaitingForCheck, Leaving};
		public enum CheckState {None, AskedForBill, ReceivedBill, DeliveredBill};
		public enum OrderStatus {None, Ordered, Cooking, Cancelled, DoneCooking, PickedUp, Delivered};
		public RestaurantChungCustomer c;
		public CustomerState s;
		public CheckState cs;
		public int table;
		public Order o;
		public int bill;
		
		public class Order {
			public String choice;
			public OrderStatus os;
		}
		
		public WCustomer(RestaurantChungCustomer customer, int table, CustomerState state) {
			c = customer;
			s = state;
			cs = CheckState.None;
			this.table = table;
			o = new Order();
			o.os = OrderStatus.None;
		}
	}
}

