package city.roles;

import java.util.*;
import java.util.concurrent.Semaphore;

import city.interfaces.RestaurantChungCustomer;
import city.interfaces.RestaurantChungHost;

/**
 * Restaurant Waiter Agent
 */
//A Waiter tends to the host and customers' requests
public class RestaurantChungWaiterRevolvingStandRole extends RestaurantChungWaiterBaseRole {
	
//	public enum WaiterState
//	{Working, WantBreak, AskedForBreak, ApprovedForBreak, RejectedForBreak, OnBreak, ReturningToWork};
//	private WaiterState state = WaiterState.Working;
//	
	
//	Customers
//	=====================================================================	
//	public List<WCustomer> customers = new ArrayList<WCustomer>();
//	private class WCustomer {
//		Customer c;
//		CustomerState s;
//		CheckState cs;
//		int table;
//		Order o;
//		class Order {
//			String choice;
//			OrderStatus os;
//		}
//		int bill;
//		
//		public WCustomer(Customer customer, int table, CustomerState state) {
//			c = customer;
//			s = state;
//			cs = CheckState.None;
//			this.table = table;
//			o = new Order();
//			o.os = OrderStatus.None;
//		}
//	}
//	
//	private enum CustomerState
//	{Waiting, Seated, ReadyToOrder, Asked, Eating, WaitingForCheck, Leaving};
//	private enum CheckState
//	{None, AskedForBill, ReceivedBill, DeliveredBill};
//	private enum OrderStatus
//	{None, Ordered, Cooking, Cancelled, DoneCooking, PickedUp, Delivered};
		
	public RestaurantChungWaiterRevolvingStandRole(String name, RestaurantChungHost host, RestaurantChungCookRole cook, RestaurantChungCashierRole cashier) {
		super();
		
		this.name = name;
		this.host = host;
		this.cook = cook;
		this.cashier = cashier;

//		host.msgWaiterAvailable(this); // TODO make msg in host
	}
	
//  Messages
//	=====================================================================
//	Break
//	---------------------------------------------------------------
	public void msgAnimationAskedForBreak() {
		print("Waiter received gotGoOnBreak");
		state = WaiterState.WantBreak;
		stateChanged();
	}
	
	public void msgApprovedForBreak() {
		print("Waiter received msgApprovedForBreak");
		state = WaiterState.ApprovedForBreak;
		stateChanged();
	}
	
	public void msgRejectedForBreak() {
		print("Waiter received msgRejectedForBreak");
		state = WaiterState.RejectedForBreak;
		stateChanged();
	}
	
	public void msgAnimationBreakOver() {
		print("Waiter received msgAnimationBreakOver");
		state = WaiterState.ReturningToWork;
		stateChanged();
	}
	
//	Customer
//	---------------------------------------------------------------
	public void msgSitAtTable(RestaurantChungCustomer c, int table) {
		print("Waiter received msgSitAtTable");
		customers.add(new WCustomer(c, table, CustomerState.Waiting));
		stateChanged();
	}
	
	public void msgReadyToOrder(RestaurantChungCustomer c) {
		print("Waiter received msgReadyToOrder");
		WCustomer wc = findCustomer(c);
		wc.s = CustomerState.ReadyToOrder;
		print("customer is ready to order");
		stateChanged();
	}
	
	public void msgHereIsMyOrder(RestaurantChungCustomer c, String choice) {
		print("Waiter received msgHereIsMyOrder");
		WCustomer wc = findCustomer(c);
		wc.o.choice = choice;
		wc.o.os = OrderStatus.Ordered;
		print("customer ordered " + choice);
		stateChanged();
	}
	
	public void msgOutOfItem(String choice, int table) {
		print("Waiter received msgOutOfItem " + choice);
		WCustomer wc = findTable(table);
		wc.o.os = OrderStatus.Cancelled;
		menu.findItem(wc.o.choice).available = false;
		stateChanged();
	}
	
	public void msgOrderIsReady(String choice, int table) {
		print("Waiter received msgOrderIsReady");
		WCustomer wc = findTable(table);
		wc.o.os = OrderStatus.DoneCooking;
		print("customer's order of " + choice + " is ready");		
		stateChanged();
	}
	
	public void msgGetCheck(RestaurantChungCustomer c) {
		print("Waiter received msgGetCheck");
		WCustomer wc = findCustomer(c);
		wc.s = CustomerState.WaitingForCheck;
		stateChanged();
	}
	
	public void msgLeaving(RestaurantChungCustomer c) {
		print("Waiter received msgLeaving");
		WCustomer wc = findCustomer(c);
		wc.s = CustomerState.Leaving;
		print("customer is leaving");
		stateChanged();
	}

//	Cashier
//	---------------------------------------------------------------
	public void msgHereIsBill(RestaurantChungCustomer c, int price) {
		print("Waiter received msgHereIsBill");
		WCustomer wc = findCustomer(c);
		wc.bill = price;
		wc.cs = CheckState.ReceivedBill;	
		stateChanged();
	}
	
//	Animation
//	---------------------------------------------------------------
	public void msgAnimationAtWaiterHome() {
		print("Waiter at Waiter Home");
		atWaiterHome.release();
		stateChanged();
	}
	
	public void msgAnimationAtEntrance() {
		print("Waiter at Entrance");
		atEntrance.release();
		stateChanged();
	}
	
	public void msgAnimationAtLine() {
		print("Waiter at Line");
		atLine.release();
		stateChanged();
	}
	
	public void msgAnimationAtTable() {
		print("Waiter at Table");
		atTable.release();
		stateChanged();
	}
	
	public void msgAnimationAtCook() {
		print("Waiter at Cook");
		atCook.release();
		stateChanged();
	}
	
	public void msgAnimationAtCashier() {
		print("Waiter at Cashier");
		atCashier.release();
		stateChanged();
	}

//  Scheduler
//	=====================================================================
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean runScheduler() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
//		print("in waiter scheduler");
//		print(state.toString());
		
//		catch (ConcurrentModificationError c) { // TODO
//			return false();
//		}
		
		if (state == WaiterState.ReturningToWork) {
			returnToWork();
			return true;
		}
		
		if (state == WaiterState.RejectedForBreak) {
			rejectForBreak();
			return true;
		}
		
		if (state == WaiterState.WantBreak) {
			askForBreak();
			return true;
		}

		for (WCustomer customer : customers) {
			if (customer.o.os == OrderStatus.Cancelled) {
				informCustomerOfCancellation(customer);
				return true;
			}
		}
		
		for (WCustomer customer : customers) {
			if (customer.s == CustomerState.Waiting) {
				seatCustomer(customer);
				return true;
			}
		}
		
		for (WCustomer customer : customers) {
			if (customer.s == CustomerState.ReadyToOrder) {
				takeOrder(customer);
				return true;
			}
		}
		
		for (WCustomer customer : customers) {
			if (customer.o.os == OrderStatus.Ordered) {
				tellCookOrder(customer, customer.o.choice, customer.table);
				return true;
			}
		}
		
		for (WCustomer customer : customers) {
			if (customer.o.os == OrderStatus.DoneCooking) {
				pickUpOrder(customer);
				return true;
			}
		}
		
		for (WCustomer customer : customers) {
			if (customer.o.os == OrderStatus.PickedUp) {
				deliverOrder(customer);
				return true;
			}
		}
		
		for (WCustomer customer : customers) {
			if (customer.s == CustomerState.WaitingForCheck && customer.cs == CheckState.None) {
				getCheck(customer);
				return true;
			}
		}
		
		for (WCustomer customer : customers) {
			if (customer.cs == CheckState.ReceivedBill) {
				giveCheck(customer);
				return true;
			}
		}
		
		for (WCustomer customer : customers) {
			if (customer.cs == CheckState.DeliveredBill || customer.s == CustomerState.Leaving) { // Non-normative scenario when customer can't afford anything
				removeCustomer(customer);
				return true;
			}
		}

		if (customers.size() == 0 && state == WaiterState.ApprovedForBreak) {
			goOnBreak();
			return true;
		}
		
//		print("return false in waiter scheduler");
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

//  Actions
//	=====================================================================
//	Break
//	---------------------------------------------------------------
	private void askForBreak() {
		print("Waiter asking for break");
		state = WaiterState.AskedForBreak;
//		host.msgIWantToGoOnBreak(this);
	}	

	private void rejectForBreak() {
		waiterGui.setOffBreak();
//		host.msgIAmReturningToWork(this);
		state = WaiterState.Working;
	}
	
	private void goOnBreak() {
		print("Waiter going on break");
		state = WaiterState.OnBreak;
		waiterGui.DoGoOnBreak();
	}
	
	private void returnToWork() {
		print("Waiter returning to work");
		state = WaiterState.Working;
//		host.msgIAmReturningToWork(this);
		waiterGui.DoReturnToWaiterHome();		
	}
	
//	Customer
//	---------------------------------------------------------------
	private void seatCustomer(WCustomer customer) {	
		print("Waiter seating " + customer.c + " at " + customer.table);
		
		waiterGui.DoGoToCustomerLine();
		try {
//			atEntrance.drainPermits(); // Have to drain because of multiple calls of DoReturnToEntrance() without atEntrance.acquires();
			atLine.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		host.msgTakingCustomerToTable(customer.c);
		waiterGui.DoBringToTable(customer.c, customer.table-1);
//		customer.c.msgFollowMeToTable(this, menu);
		
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		customer.s = CustomerState.Seated;

		waiterGui.DoReturnToWaiterHome();
	}
	
	private void informCustomerOfCancellation(WCustomer customer) {
		// TODO Auto-generated method stub
		waiterGui.DoGoToTable(customer.table-1);
		
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		customer.c.msgOutOfItem(customer.o.choice, menu);
		// Resets all menu items to available after informing a single customer
		for (int i = 0; i < menu.items.size(); i++) {
			menu.items.get(i).available = true;
		}
		customer.s = CustomerState.Seated;
		customer.o.os = OrderStatus.None;		

		waiterGui.DoReturnToWaiterHome();
	}
	
	private void takeOrder(WCustomer customer) {
		waiterGui.DoGoToTable(customer.table-1);
		
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		print("taking order from " + customer.c);		
		customer.c.msgWhatWouldYouLike();
		customer.s = CustomerState.Asked;
		
		waiterGui.DoReturnToWaiterHome();
	}
	
	private void tellCookOrder(WCustomer customer, String choice, int table) {
		waiterGui.DoGoToCook();

		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		print("telling cook order " + choice + " for " + customer.c);
//		cook.msgHereIsAnOrder(this, choice, table);
		customer.o.os = OrderStatus.Cooking;
		
		waiterGui.DoReturnToWaiterHome();
	}
	
	private void pickUpOrder(WCustomer customer) {
		print("picking up order from cook");
		waiterGui.DoGoToCook();
		
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		customer.o.os = OrderStatus.PickedUp;
	}
	
	private void deliverOrder(WCustomer customer) {
		waiterGui.DoDeliverFood(customer.table-1, customer.o.choice);
		
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

		customer.o.os = OrderStatus.Delivered;
		customer.c.msgHereIsYourFood();
		customer.s = CustomerState.Eating;
		
		waiterGui.DoReturnToWaiterHome();
	}
	
	private void getCheck(WCustomer customer) {
		waiterGui.DoGoToCashier();

		try {
			atCashier.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		customer.cs = CheckState.AskedForBill;
//		cashier.msgComputeBill(this, customer.c, customer.o.choice);
	}
	
	private void giveCheck(WCustomer customer) {
		waiterGui.DoGoToTable(customer.table-1);

		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		customer.cs = CheckState.DeliveredBill;
		customer.c.msgHereIsCheck(customer.bill);
		
		waiterGui.DoReturnToWaiterHome();
	}

	private void removeCustomer(WCustomer customer) {
//		host.msgTableIsFree(this, customer.table, customer.c);
		removeCustomerFromList(customer);
	}
	
//  Utilities
//	=====================================================================
}

