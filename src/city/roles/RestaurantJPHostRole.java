package city.roles;

import utilities.RestaurantJPTableClass;

import java.util.*;

import city.Role;

public class RestaurantJPHostRole extends Role {
	static final int NTABLES = 3;
	public List<RestaurantJPCustomerRole> waitingCustomers = Collections.synchronizedList(new ArrayList<RestaurantJPCustomerRole>());
	public Collection<RestaurantJPTableClass> tables;
	int WAITERCOUNT = 0;
	public List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	class MyWaiter{
		RestaurantJPWaiterRole w;
		state s;
		String name;
		boolean isAvailable() {
			return s == state.available;
		}	
	}
	public enum state{available, wantsBreak, onBreak};
	private String name;
//	public HostGui hostGui = null;

	public RestaurantJPHostRole(String name) {
		super();
		this.name = name;
		// make some tables
		tables = new ArrayList<RestaurantJPTableClass>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new RestaurantJPTableClass(ix));//how you add to a collections
		}
	}

	public void addWaiter(RestaurantJPWaiterRole w, String name)
	{
		MyWaiter myW = new MyWaiter();
		myW.w = w;      							
		myW.name = name;
	    myW.s = state.available;
	    synchronized(waiters){
		waiters.add(myW);
	    }
		stateChanged();
	}
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	/*public List getWaitingCustomers() {
		return waitingCustomers;
	}

	public Collection getTables() {
		return tables;
	}*/
	// ----------------------------------------------------------Messages

	public void msgIWantToEat(RestaurantJPCustomerRole cust) {
		print("IWantFood message received");
		synchronized(waitingCustomers){
		waitingCustomers.add(cust);
		}
		stateChanged();
	}

	public void msgWantToGoOnBreak(RestaurantJPWaiterRole waiter){
		print("Received break request");
		synchronized(waiters){
		for(MyWaiter w : waiters){
			if(w.w == waiter)
				w.s = state.wantsBreak;
		}
		}
		stateChanged();
	}
	
	public void msgTableIsFree(RestaurantJPTableClass table) {
		print("TableFree message received from Waiter");
		for (RestaurantJPTableClass t : tables) {
			if(t == table)
				table.setUnoccupied();
		}
		stateChanged();
	}
	
	public void msgOffBreak(RestaurantJPWaiterRole waiter){
		print("Off break meassage received");
		synchronized(waiters){
		for(MyWaiter w : waiters){
			if(w.w == waiter)
				w.s = state.available;
		}
		}
		stateChanged();
	}
	
	public void msgLeaving(RestaurantJPCustomerRole customer){
		print("Leaving restaurant message recieved");
		synchronized(waitingCustomers){
		for(RestaurantJPCustomerRole c : waitingCustomers){
			if(c == customer){
				synchronized(waitingCustomers){
				waitingCustomers.remove(c);
				}
				break;
			}
		}
		}
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean runScheduler() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		for (RestaurantJPTableClass table : tables) {
			if (!table.isOccupied() && waiters.size() > 0) {
					if(waiters.get(WAITERCOUNT).isAvailable() || waiters.get(WAITERCOUNT).s == state.wantsBreak)
					{					
						if (!waitingCustomers.isEmpty()) {//the action
							AddressCustomer(waitingCustomers.get(0), waiters.get(WAITERCOUNT), table);
							if(WAITERCOUNT == waiters.size()-1)
								WAITERCOUNT = 0;
							else
								WAITERCOUNT++;
							return true;
						} 
					}
					else{
						if(WAITERCOUNT == waiters.size()-1)
							WAITERCOUNT = 0;
						else
							WAITERCOUNT++;
						return true;
					}
			}
		}
		
		boolean full = true;
		
		for (RestaurantJPTableClass table : tables){
			if(!table.isOccupied() && !waitingCustomers.isEmpty() && !waiters.isEmpty())
				full = false;
		}
		
		if(full == true)
			if(!waitingCustomers.isEmpty() && !waiters.isEmpty())
				waitingCustomers.get(0).msgRestaurantFull();
		
		synchronized(waiters){
		for(MyWaiter w : waiters) {
			if(waiters.size() <= 1 && w.s == state.wantsBreak)
			{
				//Do("Cannot put waiter on break if there is only one waiter working");
				w.s = state.available;
				w.w.msgBreakGranted(false);
			}
			if(w.s == state.wantsBreak && waiters.size() > 1){
				//Do("Putting " + w.name + " on break");
				w.w.msgBreakGranted(true);
				w.s = state.onBreak;
				//ReassignCustomers(w);
				return true;
			}
		}
		}
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void AddressCustomer(RestaurantJPCustomerRole c, MyWaiter w, RestaurantJPTableClass t){
		t.occupiedBy = c;
		synchronized(waitingCustomers){
		waitingCustomers.remove(c);
		}
		//Do("Telling " + w.name + " to seat customer");
		w.w.msgSitAtTable(c, t);														//Fix w.w
	}
	
/*	private void ReassignCustomers(MyWaiter myW){
		Do("Reassigning " + myW.name + "'s Customers");
		for(MyWaiter w : waiters){
			if(w.s == state.available)
				w.w.msgPickUpHisCustomers(myW.w);
		}
	}*/
}

