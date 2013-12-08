package city.roles;

import trace.AlertLog;
import trace.AlertTag;
import utilities.EventLog;
import city.bases.JobRole;
import city.buildings.RestaurantChungBuilding;
import city.buildings.interfaces.RestaurantChung;
import city.buildings.interfaces.RestaurantChung.MyCustomer;
import city.buildings.interfaces.RestaurantChung.MyCustomer.HostCustomerState;
import city.buildings.interfaces.RestaurantChung.MyWaiter;
import city.buildings.interfaces.RestaurantChung.Table;
import city.roles.interfaces.RestaurantChungCustomer;
import city.roles.interfaces.RestaurantChungHost;
import city.roles.interfaces.RestaurantChungWaiter;

/**
 * Restaurant Host Agent
 */
//A Host is the manager of a restaurant who sees that all is proceeded as he wishes.
public class RestaurantChungHostRole extends JobRole implements RestaurantChungHost {	
	public EventLog log = new EventLog();

	RestaurantChung restaurant;
	
	WorkingState workingState = WorkingState.Working;
	// TODO host needs to know if waiters are going off shift

//	Constructor
//	====================================================================
	public RestaurantChungHostRole(RestaurantChung b, int t1, int t2) {
		super();
		restaurant = b;
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(RestaurantChungBuilding.WORKER_SALARY);
	}

//	Activity
//	====================================================================
	@Override
	public void setInactive(){
		workingState = WorkingState.GoingOffShift;
	}
	
//  Messages
//	=====================================================================	
//	Customer
//	---------------------------------------------------------------
	@Override
	public void msgIWantToEat(RestaurantChungCustomer c) {
		print("Host received msgIWantToEat");
		if (workingState != WorkingState.NotWorking) {
			restaurant.incrementNumWaitingCustomers();
			restaurant.getCustomers().add(new MyCustomer(c, restaurant.getNumWaitingCustomers()));
			stateChanged();
		}
	}
	
	@Override
	public void msgDecidedToStay(RestaurantChungCustomer c) {
		print("Host received msgDecidedToStay");
		MyCustomer hc = restaurant.findCustomer(c);
		hc.setHostCustomerState(HostCustomerState.WaitingInLine);
		stateChanged();
	}
	
	@Override
	public void msgLeaving(RestaurantChungCustomer c) {
		print("Host received msgLeaving");
		MyCustomer hc = restaurant.findCustomer(c);
		hc.setHostCustomerState(HostCustomerState.Done);
		stateChanged();
	}
	
//	Waiter
//	---------------------------------------------------------------
	@Override
	public void msgTakingCustomerToTable(RestaurantChungCustomer c) {
		print("Host received msgTakingCustomerToTable");
		MyCustomer hc = restaurant.findCustomer(c);
		hc.setHostCustomerState(HostCustomerState.GettingSeated);
		stateChanged();
	}
	
	@Override
	public void msgIWantToGoOnBreak(RestaurantChungWaiter w) {
		print("Host received msgIWantToGoOnBreak");
		MyWaiter wa = restaurant.findWaiter(w);
		wa.setWaiterState(WaiterState.WantBreak);
		stateChanged();		
	}
	
	@Override
	public void msgIAmReturningToWork(RestaurantChungWaiter w) {
		print("Host received msgIAmReturningToWork");
		MyWaiter wa = restaurant.findWaiter(w);
		wa.setWaiterState(WaiterState.Working);
		stateChanged();
	}
	
	@Override
	public void msgTableIsFree(RestaurantChungWaiter waiter, int t, RestaurantChungCustomer c) {
		print("Host received msgTableIsFree");
		MyCustomer hc = restaurant.findCustomer(c);
		hc.setHostCustomerState(HostCustomerState.Done);
		Table table = restaurant.findTable(t);
		table.setUnoccupied();
		print("Table " + t + " is available");
		stateChanged();
	}

//	Cashier
//	---------------------------------------------------------------
	@Override
	public void msgFlakeAlert(RestaurantChungCustomer c, int d) {
		print("Host received msgFlakeAlert");
		MyCustomer hc = restaurant.findCustomer(c);
		hc.setDebt(d);
	}
	
//  Scheduler
//	=====================================================================
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	@Override
	public boolean runScheduler() {
		if (workingState == WorkingState.GoingOffShift) {
			if (restaurant.getRestaurantChungHost() != this)
				workingState = WorkingState.NotWorking;
		}
		
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		
		synchronized(restaurant.getCustomers()) {
			for (MyCustomer customer : restaurant.getCustomers()) {
				if (customer.getHostCustomerState() == HostCustomerState.Done && customer.getPositionInLine() != -1) { // If the customer decided to leave
					print("CUSTOMER LEFT, UPDATE LINE");
					updateCustomersInLineAfterLeaving(customer);
					return true;
				}		
			}
		}
		
		synchronized(restaurant.getCustomers()) {
			for (MyCustomer customer : restaurant.getCustomers()) {
				if (customer.getHostCustomerState() == HostCustomerState.GettingSeated) {
					updateCustomersInLineAfterSeating(customer);
					return true;
				}		
			}
		}
		
		synchronized(restaurant.getCustomers()) {
			for (MyCustomer customer : restaurant.getCustomers()) {
				if (customer.getHostCustomerState() == HostCustomerState.InRestaurant) {
					standCustomerInLine(customer);
					return true;
				}	
			}
		}
		
		synchronized(restaurant.getWaiters()) {
			for (MyWaiter waiter : restaurant.getWaiters()) {
				if (waiter.getWaiterState() == WaiterState.WantBreak) {
					DecideWaiterBreak(waiter);
					return true;
				}		
			}
		}		

		synchronized(restaurant.getCustomers()) {
			for (MyCustomer customer : restaurant.getCustomers()) {	
				if (((customer.getHostCustomerState() == HostCustomerState.WaitingInLine && restaurant.getWaiters().size() > 0))) {
					if (customer.getDebt() > 0) {
//						System.out.println("IN KICKING OUT");
						customer.getRestaurantChungCustomer().msgKickingYouOutAfterPaying(customer.getDebt());
						customer.setHostCustomerState(HostCustomerState.Done);
						return true;
					}
					for (Table t : restaurant.getTables()) {
						if (t.getOccupant() == null) {
							int fewestCustomers = -1;
							MyWaiter waiterA = null;
							for (MyWaiter waiter : restaurant.getWaiters()) {
								if (waiter.getWaiterState() != WaiterState.OnBreak && waiter.getRestaurantChungWaiter().getWorkingState() == RestaurantChungWaiter.WorkingState.Working) {
									if (fewestCustomers == -1) fewestCustomers = waiter.getNumCustomers(); // Sets the initial value of numCustomers to the numCustomers of the first available waiter
									if (waiterA == null) waiterA = waiter; // Sets the initial value of waiterA to the first available waiter
									if (waiter.getNumCustomers() < fewestCustomers) {
										waiterA = waiter;
										fewestCustomers = waiter.getNumCustomers();
									}
								}
							}
							t.setOccupant(customer.getRestaurantChungCustomer());
							
							seatCustomer(customer, t, waiterA.getRestaurantChungWaiter()); //the action
//							updateCustomersInLine();
							waiterA.incrementNumCustomers();
							return true; //return true to the abstract agent to reinvoke the scheduler.
						}
					}
					if (restaurant.getWaiters().size() > 0 && customer.getHostCustomerState() == HostCustomerState.WaitingInLine) {
						informCustomerOfNoTables(customer);
						customer.setHostCustomerState(HostCustomerState.DecidingToLeave);
					}
					return true;
				}
			}
		}

		if (workingState == WorkingState.NotWorking)
			setInactive();
		
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
		for (MyWaiter waiter : restaurant.getWaiters()) {
			if (waiter.getWaiterState() == WaiterState.Working) {
				numWorkingWaiters++;
			}
		}
		
		if (numWorkingWaiters >= 1) {
			print("Host Approving Waiter's break");
			w.setWaiterState(WaiterState.OnBreak);
			w.getRestaurantChungWaiter().msgApprovedForBreak();
			return;
		}
		
		print("Host Rejecting Waiter's break");
		w.setWaiterState(WaiterState.Rejected);
		w.getRestaurantChungWaiter().msgRejectedForBreak();
	}

//	Customer
//	---------------------------------------------------------------
	private void standCustomerInLine(MyCustomer customer) {
		print("Host telling customer to stand in position " + customer.getPositionInLine());
		customer.setHostCustomerState(HostCustomerState.WaitingInLine);
		customer.getRestaurantChungCustomer().msgGetInLinePosition(customer.getPositionInLine());
	}
	
	private void moveCustomerInLine(MyCustomer customer) {
		print("Host telling customer to move to position " + customer.getPositionInLine());
		customer.getRestaurantChungCustomer().msgGetInLinePosition(customer.getPositionInLine());
	}
	
	private void updateCustomersInLineAfterSeating(MyCustomer c) {
		c.setPositionInLine(-1); // Makes the position in line variable invalid
		c.setHostCustomerState(HostCustomerState.Seated);
		restaurant.decrementNumWaitingCustomers();
		for (MyCustomer customer : restaurant.getCustomers()) {
			if (customer.getHostCustomerState() == HostCustomerState.WaitingInLine || customer.getHostCustomerState() == HostCustomerState.WaitingToBeSeated || customer.getHostCustomerState() == HostCustomerState.DecidingToLeave) {
				customer.decrementPositionInLine();
				moveCustomerInLine(customer);
			}		
		}
	}
	
	private void updateCustomersInLineAfterLeaving(MyCustomer c) {
		restaurant.decrementNumWaitingCustomers();
		print("POSITION CUST LEFT " + c.getPositionInLine());
		for (int i = c.getPositionInLine()+1; i < restaurant.getCustomers().size(); i++) {
			print("POSITION CUST BEHIND LEFT " + restaurant.getCustomers().get(i).getPositionInLine());
			if (restaurant.getCustomers().get(i).getHostCustomerState() == HostCustomerState.WaitingInLine && restaurant.getCustomers().get(i).getPositionInLine() > c.getPositionInLine() || restaurant.getCustomers().get(i).getHostCustomerState() == HostCustomerState.WaitingToBeSeated && restaurant.getCustomers().get(i).getPositionInLine() > c.getPositionInLine() || restaurant.getCustomers().get(i).getHostCustomerState() == HostCustomerState.DecidingToLeave && restaurant.getCustomers().get(i).getPositionInLine() > c.getPositionInLine()) {
				restaurant.getCustomers().get(i).decrementPositionInLine();
				moveCustomerInLine(restaurant.getCustomers().get(i));
			}		
		}
		c.setPositionInLine(-1); // Makes the position in line variable invalid
	}
	
	private void seatCustomer(MyCustomer customer, Table table, RestaurantChungWaiter w) {
		print("Host telling Waiter to seat customer");
		w.msgSitAtTable(customer.getRestaurantChungCustomer(), table.getTableNumber());
		customer.setHostCustomerState(HostCustomerState.WaitingToBeSeated);
		table.setOccupant(customer.getRestaurantChungCustomer());
	}
	
	private void informCustomerOfNoTables(MyCustomer customer) {
		customer.getRestaurantChungCustomer().msgNoTablesAvailable();
	}

	
//  Utilities
//	=====================================================================	
	@Override
	public void print(String msg) {
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTCHUNG, "RestaurantChungHostRole " + this.getPerson().getName(), msg);
    }
}

