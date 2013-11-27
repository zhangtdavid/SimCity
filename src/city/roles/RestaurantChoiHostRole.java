package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import trace.AlertLog;
import trace.AlertTag;
import utilities.RestaurantChoiTable;
import city.Role;
import city.animations.interfaces.RestaurantChoiAnimatedHost;
import city.buildings.RestaurantChoiBuilding;
import city.interfaces.RestaurantChoiCustomer;
import city.interfaces.RestaurantChoiHost;
import city.interfaces.RestaurantChoiWaiterAbs;

public class RestaurantChoiHostRole extends Role implements RestaurantChoiHost{

	/**
	 * Restaurant Host Role
	 */
	public static final int NTABLES = 4; // a global for the number of tables.
	static int xstart = 50;
	static int ystart = 50;
	public List<RestaurantChoiTable> tables;
	List<RestaurantChoiCustomer> waitingCustomers = Collections.synchronizedList(new ArrayList<RestaurantChoiCustomer>());
	public List<RestaurantChoiWaiterAbs> waiters = Collections.synchronizedList(new ArrayList<RestaurantChoiWaiterAbs>());
	public ConcurrentHashMap<RestaurantChoiWaiterAbs, Integer> waiterBalance = new ConcurrentHashMap<RestaurantChoiWaiterAbs, Integer>();
	int waitersOnBreak;
	private RestaurantChoiWaiterAbs leastActiveWaiter;
	//private int leastActiveWaiterIndex; //TODO Bad impact on functionality? lines 167, 176
	//public WaiterGUI hostGui = null;
	RestaurantChoiAnimatedHost animation;
	RestaurantChoiBuilding building;
	private boolean wantsToLeave;

	/**
	 * Initializes Host for RestaurantChoi
	 * @param b : for RestaurantChoiBuilding
	 * @param t1 : Start of shift
	 * @param t2 : End of shift
	 */
	public RestaurantChoiHostRole(RestaurantChoiBuilding b, int t1, int t2) {
		super();
		building = b;
		// make some tables
		tables = Collections.synchronizedList(new ArrayList<RestaurantChoiTable>(NTABLES));
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new RestaurantChoiTable(ix));// how you add to a collection
			System.out.println("made a table");
		}
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(RestaurantChoiBuilding.getWorkerSalary());
	}

	/**
	 * msgs
	 */

	public void msgImHungry(RestaurantChoiCustomer c) {
		synchronized(waitingCustomers){
			System.out.println("received msgimhungry;added to queue");
			waitingCustomers.add(c);
		}
		stateChanged();
	}
	
	public void msgImBack(RestaurantChoiWaiterAbs w){
		synchronized(waiters){
			waitersOnBreak--;
			addWaiter(w);
		}
		// always add or remove workload when adding or removing waiters.
		stateChanged();
	}
	
	public void msgIWantABreak(RestaurantChoiWaiterAbs w){
		stateChanged();
	}

	public void msgNotWaiting(RestaurantChoiCustomer c){
		synchronized(waitingCustomers){
			waitingCustomers.remove(c); //simple as that! no need to state change.
		}
	}

	public void msgTablesClear(RestaurantChoiWaiterAbs w, RestaurantChoiTable table) {
		//set the table to null.
		synchronized(tables){
			table.setOccupant(null);
			System.out.println("Cleared table " + table.getTableNumber());
		}
		//find the waiter and de-increment his customer count.
		synchronized(waiters){
			for(int i = 0; i < waiters.size(); i++){
				if(w.equals(waiters.get(i))) minus1Workload(w);
			}
		}
		building.seatedCustomers--;
		stateChanged(); // look for waiting customers now
	}
	public void msgSetUnavailable(RestaurantChoiWaiterAbs waiter){
		     for(RestaurantChoiWaiterAbs w : waiters){
		       if(w == waiter)
		    	   waiters.remove(w); // waiter wants to leave? remove him from queue just like you do for waiters on break.
		     }
		   }

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	public boolean runScheduler() {
		if(wantsToLeave && building.host != this && waitingCustomers.isEmpty()){
			wantsToLeave = false;
			super.setInactive();
		}
		//see if waiter can be on break
		synchronized(waiters){
			for(int i = 0; i < waiters.size(); i++){
				if(waiters.get(i).askedForBreak()){
					if(waiters.size() == 1){
						//if only one waiter, outright reject the break request.
						System.out.println("Rejected break request, only 1 waiter");
						waiters.get(i).msgBreakOK(false);
					}
					else if(waitersOnBreak == waiters.size()-1){
						System.out.println("Rejected break request; accepting would mean 0 waiters on duty");
						waiters.get(i).msgBreakOK(false);
					}
					//he can only go on break after he finishes his work.
					else if(waiterBalance.get(waiters.get(i)) == 0 && waiters.size() > 1){
						waiterBreak(i);
					}
				}
			}
		}

		//check if you can seat a customer now
		synchronized(waitingCustomers){
			if (!waitingCustomers.isEmpty()) {// if we have waiting customers
				findLeastActiveWaiter();
				for (int i = 0; i < tables.size(); i++) {//for all tables
					if (!tables.get(i).isOccupied()) {//if there are tables open
						if(!waiters.isEmpty()){ // if there's >0 waiters
							//assign to the last active waiter. leastActiveWaiter being a pointer 
							//to a RestaurantChoiWaiterRole in waiters, of course.
							assignTable(i);
							return true;
						}
					}
				}//this means that if there are no tables free, host doesn't assign yet.
			}
		}
		//if we couldn't do anything above, tell remaining customers they have to wait
		synchronized(waitingCustomers){
			for(int i = 0; i < waitingCustomers.size(); i++){
				waitingCustomers.get(i).msgNotifyFull(waitingCustomers.size()-1);
			}
		}
		return false;
	}

	// Actions

	public void findLeastActiveWaiter(){
		synchronized(waiters){
			leastActiveWaiter = null;
			//leastActiveWaiterIndex = -1;
			int min = 99999; // come on, we're not going to have 99999 customers.
			for(int i = 0 ; i < waiters.size(); i++){
				if(waiters.get(i).isOnBreak() || (waiters.get(i).askedForBreak() && waiters.size() > 1)) i++; 
				// if the person's on break or approved, we skip.
				//workload to waiter relation is injunctive
				if(i<waiters.size()){ // no out of bounds exception!
					if(min > waiterBalance.get(waiters.get(i))){ // find the minimum workload
						leastActiveWaiter = waiters.get(i); //if found min, set waiter to least active
						//leastActiveWaiterIndex = i;
						min = waiterBalance.get(waiters.get(i));
					}
				}
			}
		}
	}

	public void addWaiter(RestaurantChoiWaiterAbs w) {
		synchronized(waiters){
			waiters.add(w); 
		}
		waiterBalance.put(w, 0); // add w, set workload to 0
		stateChanged(); // in case we have customers waiting, then a waiter comes in}
	}

	private void updateTable(RestaurantChoiTable t, RestaurantChoiCustomer c){
		synchronized(tables){
			t.setOccupant(c);
		}
	}

	public void waiterBreak(int i){
		synchronized(waiters){
			waitersOnBreak++;
			waiters.get(i).msgBreakOK(true);
			waiterBalance.remove(waiters.get(i));
			waiters.remove(i); // now remove him from the list of OK waiters
		}
		//to maintain injunctive relationship
		System.out.println("Removed waiter; on break. Uncheck his checkbox to return from break");
	}

	public void assignTable(int i){
		System.out.println("Host told "+leastActiveWaiter.getName()
				+" to seat customer " + waitingCustomers.get(0).getPerson().getName() 
				+  " at table " + tables.get(i).getTableNumber());
		synchronized(tables){
			leastActiveWaiter.msgSeatCustomer(
					waitingCustomers.get(0),
					tables.get(i));
			plus1Workload(leastActiveWaiter);
			updateTable(tables.get(i),waitingCustomers.get(0));
			building.seatedCustomers++;
		}
		// remove customer
		synchronized(waitingCustomers){
			waitingCustomers.remove(waitingCustomers.get(0));
		}
	}

	//Getters
	public RestaurantChoiAnimatedHost getAnimation(){
		return animation;
	}

	//Setters
	public void setAnimation(RestaurantChoiAnimatedHost in){
		animation = in;
	}
	public void setInactive(){
		if(building.host != this && waitingCustomers.isEmpty()){
			super.setInactive();
		}else{
			wantsToLeave = true;
		}
	}
	
	//Utility
	public void plus1Workload(RestaurantChoiWaiterAbs w){
		waiterBalance.put(w, waiterBalance.get(w)+1); // replace old with new
	}

	public void minus1Workload(RestaurantChoiWaiterAbs w){
		waiterBalance.put(w, waiterBalance.get(w)-1); // replace old with new
	}
	
	@Override
	public void print(String msg) {
        super.print(msg);
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTCHOI, "RestaurantChoiHostRole " + this.getPerson().getName(), msg);
    }
}
