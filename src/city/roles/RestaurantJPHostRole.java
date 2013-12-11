package city.roles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import trace.AlertLog;
import trace.AlertTag;
import utilities.RestaurantJPTableClass;
import utilities.RestaurantJPWaiterBase;
import city.bases.JobRole;
import city.buildings.RestaurantJPBuilding;
import city.buildings.interfaces.RestaurantJP;

public class RestaurantJPHostRole extends JobRole {
	//Data
	static final int NTABLES = 3;
	RestaurantJPBuilding building;
	public List<RestaurantJPCustomerRole> waitingCustomers = Collections.synchronizedList(new ArrayList<RestaurantJPCustomerRole>());
	public Collection<RestaurantJPTableClass> tables;
	int WAITERCOUNT = 0;
	public List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	private boolean wantsInactive = false;
	public enum state{available, wantsBreak, onBreak, unavailable};
	private String name;

	//Constructor
	
	public RestaurantJPHostRole(RestaurantJPBuilding b, int shiftStart, int shiftEnd) {
		super();
		building = b;
		b.setHost(this);
		this.setWorkplace(b);
		this.setSalary(RestaurantJP.WORKER_SALARY);
		this.setShift(shiftStart, shiftEnd);
		// make some tables
		tables = new ArrayList<RestaurantJPTableClass>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new RestaurantJPTableClass(ix));//how you add to a collections
		}
	}

	//Messages

	public void msgSetUnavailable(RestaurantJPWaiterBase waiter){
		for(MyWaiter myW : waiters){
			if(myW.w == waiter)
				myW.s = state.unavailable;
		}
	}
	public void msgIWantToEat(RestaurantJPCustomerRole cust) {
		print("IWantFood message received");
		synchronized(waitingCustomers){
		waitingCustomers.add(cust);
		}
		stateChanged();
	}

	public void msgWantToGoOnBreak(RestaurantJPWaiterBase restaurantJPWaiterBase){
		print("Received break request");
		synchronized(waiters){
		for(MyWaiter w : waiters){
			if(w.w == restaurantJPWaiterBase)
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
	
	public void msgOffBreak(RestaurantJPWaiterBase restaurantJPWaiterBase){
		print("Off break meassage received");
		synchronized(waiters){
		for(MyWaiter w : waiters){
			if(w.w == restaurantJPWaiterBase)
				w.s = state.available;
		}
		}
		stateChanged();
	}
	
	public void msgLeaving(RestaurantJPCustomerRole customer){
		print("Leaving restaurant message recieved");
		/*synchronized(waitingCustomers){
		for(RestaurantJPCustomerRole c : waitingCustomers){
			if(c == customer){
				synchronized(waitingCustomers){
				waitingCustomers.remove(c);
				}
				break;
			}
		}
		}*/			//Don't know why this was ever necessary
		building.seatedCustomers -= 1;
		stateChanged();
	}

	//Scheduler
	
	public boolean runScheduler() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		if(wantsInactive && building.host != this && waitingCustomers.size() == 0){
			super.setInactive();
			this.getPerson().setCash(this.getPerson().getCash() + RestaurantJP.WORKER_SALARY);
			wantsInactive = false;
		}
		
		for (RestaurantJPTableClass table : tables) {
			if (!table.isOccupied() && waiters.size() > 0) {
					if(waiters.get(WAITERCOUNT).isAvailable() || waiters.get(WAITERCOUNT).s == state.wantsBreak)
					{					
						if (!waitingCustomers.isEmpty()) {//the action
							AddressCustomer(waitingCustomers.get(0), waiters.get(WAITERCOUNT), table);
							building.seatedCustomers += 1;
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
	}

	// Actions

	private void AddressCustomer(RestaurantJPCustomerRole c, MyWaiter w, RestaurantJPTableClass t){
		t.occupiedBy = c;
		synchronized(waitingCustomers){
		waitingCustomers.remove(c);
		}
		w.w.msgSitAtTable(c, t);														
	}
	
	//Getters
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	//Setters
	
	public void setInactive(){
		if(building.host != this && waitingCustomers.size() == 0){
			super.setInactive();
			this.getPerson().setCash(this.getPerson().getCash() + RestaurantJP.WORKER_SALARY);
		}
		else
			wantsInactive = true;
	}
	
	//Utilities
	public void addWaiter(RestaurantJPWaiterBase restaurantJPWaiterBase, String name)
	{
		MyWaiter myW = new MyWaiter();
		myW.w = restaurantJPWaiterBase;      							
		myW.name = name;
	    myW.s = state.available;
	    synchronized(waiters){
		waiters.add(myW);
	    }
		stateChanged();
	}
	
	@Override
	public void print(String msg) {
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTJP, "RestaurantJPHostRole " + this.getPerson().getName(), msg);
    }

	//Classes
	class MyWaiter{
		RestaurantJPWaiterBase w;
		state s;
		String name;
		boolean isAvailable() {
			return s == state.available;
		}	
	}
}

