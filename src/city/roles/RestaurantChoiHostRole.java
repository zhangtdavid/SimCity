package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import trace.AlertLog;
import trace.AlertTag;
import utilities.RestaurantChoiTable;
import utilities.RestaurantChoiWaiterBase;
import city.animations.interfaces.RestaurantChoiAnimatedHost;
import city.bases.JobRole;
import city.buildings.RestaurantChoiBuilding;
import city.roles.interfaces.RestaurantChoiCustomer;
import city.roles.interfaces.RestaurantChoiHost;

public class RestaurantChoiHostRole extends JobRole implements RestaurantChoiHost{

	// Data

	private List<RestaurantChoiCustomer> waitingCustomers = Collections.synchronizedList(new ArrayList<RestaurantChoiCustomer>());
	private int waitersOnBreak;
	private RestaurantChoiWaiterBase leastActiveWaiter;
	// private int leastActiveWaiterIndex; //TODO Bad impact on functionality? lines 167, 176
	private RestaurantChoiAnimatedHost animation;
	private RestaurantChoiBuilding building;
	private boolean wantsToLeave;
	
	// TODO Change these to private and add getters/setters
	public List<RestaurantChoiTable> tables;
	public List<RestaurantChoiWaiterBase> waiters = Collections.synchronizedList(new ArrayList<RestaurantChoiWaiterBase>());
	public ConcurrentHashMap<RestaurantChoiWaiterBase, Integer> waiterBalance = new ConcurrentHashMap<RestaurantChoiWaiterBase, Integer>();
	// public WaiterGUI hostGui = null;
	public static final int NTABLES = 4; // a global for the number of tables.
	
	// Constructor
	
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
		}
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(RestaurantChoiBuilding.getWorkerSalary());
	}

	// Messages

	@Override
	public void msgImHungry(RestaurantChoiCustomer c) {
		synchronized(waitingCustomers){
			print("received msgimhungry;added to queue");
			waitingCustomers.add(c);
		}
		stateChanged();
	}
	
	@Override
	public void msgImBack(RestaurantChoiWaiterBase w){
		synchronized(waiters){
			waitersOnBreak--;
			addWaiter(w);
		}
		// always add or remove workload when adding or removing waiters.
		stateChanged();
	}
	
	@Override
	public void msgIWantABreak(RestaurantChoiWaiterBase w){
		stateChanged();
	}

	@Override
	public void msgNotWaiting(RestaurantChoiCustomer c){
		synchronized(waitingCustomers){
			waitingCustomers.remove(c); //simple as that! no need to state change.
		}
	}

	@Override
	public void msgTablesClear(RestaurantChoiWaiterBase w, RestaurantChoiTable table) {
		//set the table to null.
		synchronized(tables){
			table.setOccupant(null);
			print("Cleared table " + table.getTableNumber());
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
	
	@Override
	public void msgSetUnavailable(RestaurantChoiWaiterBase waiter){
		for(RestaurantChoiWaiterBase w : waiters) {
			if(w == waiter)waiters.remove(w); // waiter wants to leave? remove him from queue just like you do for waiters on break.
		}
	}

	// Scheduler
	
	@Override
	public boolean runScheduler() {
		if(wantsToLeave && building.getHost() != this && waitingCustomers.isEmpty()){
			wantsToLeave = false;
			super.setInactive();
		}
		//see if waiter can be on break
		synchronized(waiters){
			for(int i = 0; i < waiters.size(); i++){
				if(waiters.get(i).askedForBreak()){
					if(waiters.size() == 1){
						//if only one waiter, outright reject the break request.
						print("Rejected break request, only 1 waiter");
						waiters.get(i).msgBreakOK(false);
					}
					else if(waitersOnBreak == waiters.size()-1){
						print("Rejected break request; accepting would mean 0 waiters on duty");
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

	private void findLeastActiveWaiter(){
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

	private void updateTable(RestaurantChoiTable t, RestaurantChoiCustomer c){
		synchronized(tables){
			t.setOccupant(c);
		}
	}

	private void waiterBreak(int i){
		synchronized(waiters){
			waitersOnBreak++;
			waiters.get(i).msgBreakOK(true);
			waiterBalance.remove(waiters.get(i));
			waiters.remove(i); // now remove him from the list of OK waiters
		}
		//to maintain injunctive relationship
		print("Removed waiter; on break. Uncheck his checkbox to return from break");
	}

	private void assignTable(int i){
		print("Host told "+leastActiveWaiter.getPerson().getName()
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

	// Getters
	
	@Override
	public RestaurantChoiAnimatedHost getAnimation(){
		return animation;
	}

	// Setters
	
	@Override
	public void setAnimation(RestaurantChoiAnimatedHost in){
		animation = in;
	}
	
	@Override
	public void setInactive(){
		if(building.getHost() != this && waitingCustomers.isEmpty()){
			super.setInactive();
		}else{
			wantsToLeave = true;
		}
	}
	
	// Utility
	
	@Override
	public void addWaiter(RestaurantChoiWaiterBase w) {
		synchronized(waiters){
			waiters.add(w); 
		}
		waiterBalance.put(w, 0); // add w, set workload to 0
		stateChanged(); // in case we have customers waiting, then a waiter comes in}
	}
	
	private void plus1Workload(RestaurantChoiWaiterBase w){
		waiterBalance.put(w, waiterBalance.get(w)+1); // replace old with new
	}

	private void minus1Workload(RestaurantChoiWaiterBase w){
		waiterBalance.put(w, waiterBalance.get(w)-1); // replace old with new
	}
	
	@Override
	public void print(String msg) {
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTCHOI, "RestaurantChoiHostRole " + this.getPerson().getName(), msg);
    }
}
