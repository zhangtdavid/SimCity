package city.roles;

import java.util.*;

import city.Role;
import city.buildings.RestaurantChungBuilding;
import city.interfaces.RestaurantChungCustomer;
import city.interfaces.RestaurantChungHost;
import city.interfaces.RestaurantChungWaiterBase;
import city.roles.RestaurantChungCashierRole.WorkingState;
/**
 * Restaurant Host Agent
 */
//A Host is the manager of a restaurant who sees that all is proceeded as he wishes.
public class RestaurantChungHostRole extends Role implements RestaurantChungHost {	
	RestaurantChungBuilding restaurant;
	
	private int nTables = 4;
	private int numWaitingCustomers = 0; // Used to keep track of customers' positions in line
	
	public enum WorkingState
	{Working, GoingOffShift, NotWorking};
	WorkingState workingState = WorkingState.Working;
	
//	Waiters
//	=====================================================================	
	private List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	private class MyWaiter {
		RestaurantChungWaiterBase w;
		WaiterState s;
		int numCustomers;
		
		public MyWaiter(RestaurantChungWaiterBase w2) {
			w = w2;
			s = WaiterState.Working;
			numCustomers = 0;
		}
	}
	private enum WaiterState
	{Working, WantBreak, Rejected, OnBreak};
	
//	Customers
//	=====================================================================
	private List<HCustomer> customers = Collections.synchronizedList(new ArrayList<HCustomer>());
	private class HCustomer {
		RestaurantChungCustomer c;
		CustomerState s;
		int positionInLine;
		int debt;
		
		public HCustomer(RestaurantChungCustomer customer, CustomerState state, int pos) {
			c = customer;
			s = state;
			positionInLine = pos;
			debt = 0;
		}
	}
	
	private enum CustomerState
	{InRestaurant, WaitingInLine, WaitingToBeSeated, GettingSeated, DecidingToLeave, Seated, Done};
	
//	Tables
//	=====================================================================
	private Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented	
	private class Table {
		RestaurantChungCustomer c;
		int tableNumber;
		
		public Table(int table) {
			c = null;
			tableNumber = table;
		}
		public void setOccupant(RestaurantChungCustomer customer) {
			c = customer;
		}
		public void setUnoccupied() {
			c = null;
		}
		public String toString() {
			return "table " + tableNumber;
		}
	}

//	Constructor
//	====================================================================
	public RestaurantChungHostRole(RestaurantChungBuilding b, int t1, int t2) {
		super();
		restaurant = b;
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(RestaurantChungBuilding.getWorkerSalary());
		
		// make some tables
		tables = new Vector<Table>(nTables);
		for (int i = 1; i < nTables+1; i++) {
			tables.add(new Table(i));
		}
	}

	public void setActive(){
		this.setActivityBegun();
	}
	
	public void setInActive(){
		workingState = WorkingState.GoingOffShift;
	}
	
//  Messages
//	=====================================================================
//	Customer
//	---------------------------------------------------------------
	public void msgIWantToEat(RestaurantChungCustomer c) {
		print("Host received msgIWantToEat");
		if (workingState != WorkingState.NotWorking) {
			for (HCustomer customer : customers) {
				if (customer.c == c) {
					customer.s = CustomerState.InRestaurant;
					customer.positionInLine = numWaitingCustomers++;
					stateChanged();
					return;
				}
			}
			
			customers.add(new HCustomer(c, CustomerState.InRestaurant, numWaitingCustomers++));
			stateChanged();
		}
		// TODO inform sender of inactivity
	}
	
	public void msgDecidedToStay(RestaurantChungCustomer c) {
		print("Host received msgDecidedToStay");
		HCustomer hc = findCustomer(c);
		hc.s = CustomerState.WaitingInLine;
		stateChanged();
	}
	
	public void msgLeaving(RestaurantChungCustomer c) {
		print("Host received msgLeaving");
		HCustomer hc = findCustomer(c);
		hc.s = CustomerState.Done;
		stateChanged();
	}
	
//	Waiter
//	---------------------------------------------------------------
	public void msgTakingCustomerToTable(RestaurantChungCustomer c) {
		print("Host received msgTakingCustomerToTable");
		HCustomer hc = findCustomer(c);
		hc.s = CustomerState.GettingSeated;
		stateChanged();
	}
	
	public void msgWaiterAvailable(RestaurantChungWaiterBase w) {
		print("Host received msgWaiterAvailable");
		waiters.add(new MyWaiter(w));
		stateChanged();
	}
	
	public void msgIWantToGoOnBreak(RestaurantChungWaiterBase w) {
		print("Host received msgIWantToGoOnBreak");
		MyWaiter wa = findWaiter(w);
		wa.s = WaiterState.WantBreak;
		stateChanged();		
	}
	
	public void msgIAmReturningToWork(RestaurantChungWaiterBase w) {
		print("Host received msgIAmReturningToWork");
		MyWaiter wa = findWaiter(w);
		wa.s = WaiterState.Working;
		stateChanged();
	}
	
	public void msgTableIsFree(RestaurantChungWaiterBase waiter, int t, RestaurantChungCustomer c) {
		print("Host received msgTableIsFree");
		HCustomer hc = findCustomer(c);
		hc.s = CustomerState.Done;
		Table table = findTable(t);
		table.setUnoccupied();
		print("Table " + t + " is available");
		stateChanged();
	}

//	Cashier
//	---------------------------------------------------------------
	public void msgFlakeAlert(RestaurantChungCustomer c, int d) {
		print("Host received msgFlakeAlert");
		HCustomer hc = findCustomer(c);
		hc.debt = d;
//		System.out.println(hc.debt);
	}
	
//  Scheduler
//	=====================================================================
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean runScheduler() {
		if (workingState == WorkingState.GoingOffShift) {
			if (restaurant.host != this)
				workingState = WorkingState.NotWorking;
		}
		
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		
		synchronized(customers) {
			for (HCustomer customer : customers) {
				if (customer.s == CustomerState.Done && customer.positionInLine != -1) { // If the customer decided to leave
					print("CUSTOMER LEFT, UPDATE LINE");
					updateCustomersInLineAfterLeaving(customer);
					return true;
				}		
			}
		}
		
		synchronized(customers) {
			for (HCustomer customer : customers) {
				if (customer.s == CustomerState.GettingSeated) {
					updateCustomersInLineAfterSeating(customer);
					return true;
				}		
			}
		}
		
		synchronized(customers) {
			for (HCustomer customer : customers) {
				if (customer.s == CustomerState.InRestaurant) {
					standCustomerInLine(customer);
					return true;
				}	
			}
			if (workingState == WorkingState.NotWorking)
				setInactive();
		}
		
		synchronized(waiters) {
			for (MyWaiter waiter : waiters) {
				if (waiter.s == WaiterState.WantBreak) {
					DecideWaiterBreak(waiter);
					return true;
				}		
			}
		}		

		synchronized(customers) {
			for (HCustomer customer : customers) {	
				if (((customer.s == CustomerState.WaitingInLine && waiters.size() > 0))) {
					if (customer.debt > 0) {
//						System.out.println("IN KICKING OUT");
						customer.c.msgKickingYouOutAfterPaying(customer.debt);
						customer.s = CustomerState.Done;
						return true;
					}
					for (Table t : tables) {
						if (t.c == null) {
							int fewestCustomers = -1;
							MyWaiter waiterA = null;
							for (MyWaiter waiter : waiters) {
								if (waiter.s != WaiterState.OnBreak) {
									if (fewestCustomers == -1) fewestCustomers = waiter.numCustomers; // Sets the initial value of numCustomers to the numCustomers of the first available waiter
									if (waiterA == null) waiterA = waiter; // Sets the initial value of waiterA to the first available waiter
									if (waiter.numCustomers < fewestCustomers) {
										waiterA = waiter;
										fewestCustomers = waiter.numCustomers;
									}
								}
							}
							t.setOccupant(customer.c);
							
							seatCustomer(customer, t, waiterA.w);//the action
//							updateCustomersInLine();
							waiterA.numCustomers++;
							return true; //return true to the abstract agent to reinvoke the scheduler.
						}
					}
					if (waiters.size() > 0 && customer.s == CustomerState.WaitingInLine) {
						informCustomerOfNoTables(customer);
						customer.s = CustomerState.DecidingToLeave;
					}
					return true;
				}
			}
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	//  Actions
//	=====================================================================
//	Waiter
//	---------------------------------------------------------------
	private void DecideWaiterBreak(MyWaiter w) {
		int numWorkingWaiters = 0;
		for (MyWaiter waiter : waiters) {
			if (waiter.s == WaiterState.Working) {
				numWorkingWaiters++;
			}
		}
		
		if (numWorkingWaiters >= 1) {
			print("Host Approving Waiter's break");
			w.s = WaiterState.OnBreak;
			w.w.msgApprovedForBreak();
			return;
		}
		
		print("Host Rejecting Waiter's break");
		w.s = WaiterState.Rejected;
		w.w.msgRejectedForBreak();
	}

//	Customer
//	---------------------------------------------------------------
	private void standCustomerInLine(HCustomer customer) {
		print("Host telling customer to stand in position " + customer.positionInLine);
		customer.s = CustomerState.WaitingInLine;
		customer.c.msgGetInLinePosition(customer.positionInLine);
	}
	
	private void moveCustomerInLine(HCustomer customer) {
		print("Host telling customer to move to position " + customer.positionInLine);
		customer.c.msgGetInLinePosition(customer.positionInLine);
	}
	
	private void updateCustomersInLineAfterSeating(HCustomer c) {
		c.positionInLine = -1; // Makes the position in line variable invalid
		c.s = CustomerState.Seated;
		numWaitingCustomers--;
		for (HCustomer customer : customers) {
			if (customer.s == CustomerState.WaitingInLine || customer.s == CustomerState.WaitingToBeSeated || customer.s == CustomerState.DecidingToLeave) {
				customer.positionInLine--;
				moveCustomerInLine(customer);
			}		
		}
	}
	
	private void updateCustomersInLineAfterLeaving(HCustomer c) {
		numWaitingCustomers--;
		print("POSITION CUST LEFT " + c.positionInLine);
		for (int i = c.positionInLine+1; i < customers.size(); i++) {
			print("POSITION CUST BEHIND LEFT " + customers.get(i).positionInLine);
			if (customers.get(i).s == CustomerState.WaitingInLine && customers.get(i).positionInLine > c.positionInLine || customers.get(i).s == CustomerState.WaitingToBeSeated && customers.get(i).positionInLine > c.positionInLine || customers.get(i).s == CustomerState.DecidingToLeave && customers.get(i).positionInLine > c.positionInLine) {
				customers.get(i).positionInLine--;
				moveCustomerInLine(customers.get(i));
			}		
		}
		c.positionInLine = -1; // Makes the position in line variable invalid
	}
	
	private void seatCustomer(HCustomer customer, Table table, RestaurantChungWaiterBase w) {
		print("Host telling Waiter to seat customer");
		w.msgSitAtTable(customer.c, table.tableNumber);
		customer.s = CustomerState.WaitingToBeSeated;
		table.setOccupant(customer.c);
	}
	
	private void informCustomerOfNoTables(HCustomer customer) {
		customer.c.msgNoTablesAvailable();
	}
//	private void removeCustomer(HCustomer customer) {
//		removeCustomerFromList(customer);
//	}
	

//  Utilities
//	=====================================================================
//	public String getName() {
//		return name;
//	}

	public List<HCustomer> getCustomers() {
		return customers;
	}

	public Collection<Table> getTables() {
		return tables;
	}
	
//	public int getNumWaiters(){
//		return waiters.size();
//	}
	
	public void addTable(){
		tables.add(new Table(++nTables));//how you add to a collections
	}
	
	public int getNumTables(){
		return nTables;
	}
	
	public Table findTable(int t) {
		for (Table table : tables) {
			if (table.tableNumber == t) {
				return table;
			}
		}
		return null;
	}
	
	public HCustomer findCustomer(RestaurantChungCustomer ca) {
		for(HCustomer customer : customers ){
			if(customer.c == ca) {
				return customer;		
			}
		}
		return null;
	}
	
//	public void removeCustomerFromList(HCustomer c) {
//		for(int i = 0; i < customers.size(); i ++) {
//			if(customers.get(i) == c) {
//				customers.remove(c);
//			}
//		}
//	}
	
	public MyWaiter findWaiter(RestaurantChungWaiterBase w) {
		for(MyWaiter waiter : waiters ){
			if(waiter.w == w) {
				return waiter;
			}
		}
		return null;
	}
}

