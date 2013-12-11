package utilities;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.Semaphore;

import city.animations.RestaurantJPWaiterAnimation;
import city.bases.JobRole;
import city.buildings.RestaurantJPBuilding;
import city.buildings.interfaces.RestaurantJP;
import city.roles.interfaces.RestaurantJPCashier;
import city.roles.interfaces.RestaurantJPCustomer;
import city.roles.interfaces.RestaurantJPWaiter;

public abstract class RestaurantJPWaiterBase extends JobRole implements RestaurantJPWaiter {
	
	// Data
	
	public RestaurantJPBuilding building;
	private List<MyCustomer> myCustomers = new ArrayList<MyCustomer>();
	private RestaurantJPMenuClass menu = new RestaurantJPMenuClass();
	public String name;
	private boolean onBreak = false;
	private boolean hasRequestedBreak = false;
	private boolean comingBackFromBreak = false;
	public RestaurantJPRevolvingStand revolvingStand; 
	public enum state{waiting, seated, readyToOrder, decidingOrder, ordered, reOrder, waitingForFood, foodReady, served, checkReady, checkReceived, done};
	protected Semaphore atDestination = new Semaphore(0,true);
	public RestaurantJPWaiterAnimation waiterGui;
	private boolean wantsInactive = false;
	
	// Constructor
	
	public RestaurantJPWaiterBase(RestaurantJPBuilding b, int shiftStart, int shiftEnd) {
		super();
		building = b;
		this.setWorkplace(b);
		this.setSalary(RestaurantJP.WORKER_SALARY);
		this.setShift(shiftStart, shiftEnd);
		if(b.host != null)
			b.host.addWaiter(this, null);
	}

	// Messages
	
	public void msgSitAtTable(RestaurantJPCustomer cust, RestaurantJPTableClass t) {
		print("SeatCustomer message received from Host");
		MyCustomer myC = new MyCustomer();
	    myC.customer = cust;
	    myC.s = state.waiting;
	    myC.table = t;
		myCustomers.add(myC);
		stateChanged();
	}
	
	public void msgImReadyToOrder(RestaurantJPCustomer cust) {
		print("ReadyToOrder message received");
		for(MyCustomer myC : myCustomers){
			if(myC.customer == cust){//Changed this line from comparing current table
				myC.s = state.readyToOrder;
			}
		}
		stateChanged();
	}
	
	public void msgAtDestination() {//from animation
		atDestination.release();// = true;
		stateChanged();
	}
	
	public void msgHereIsMyChoice(String choice, RestaurantJPCustomer cust){	
		//Do("HereIsMyChoice message received from " + cust.toString());
		for(MyCustomer myC : myCustomers){
			if(myC.customer == cust){
				myC.choice = choice;
				myC.s =state.ordered;
			}
		}
		stateChanged();
	}
	
	public void msgOutOf(String choice, RestaurantJPTableClass table){
		//Do("Out of " + choice + " message received from Cook");
		menu.remove(choice);
		for(MyCustomer myC : myCustomers){
			if(myC.table.equals(table))
				myC.s = state.reOrder;
		}
		stateChanged();
	}
	
	public void msgOrderIsReady(String choice, RestaurantJPTableClass t){
		for(MyCustomer myC : myCustomers){
			if(myC.choice.equals(choice)){
					myC.s = state.foodReady;
					stateChanged();
					return;
			}
		}
	}
	
	public void msgHereIsCheck(int check, RestaurantJPCashier csh, RestaurantJPCustomer c){
		//Do("Check received from cashier");
		for(MyCustomer myC : myCustomers){
			if(myC.customer == c){
				myC.check = check;
				myC.s = state.checkReady;
			}
		}
		stateChanged();
	}
	
	public void msgDoneEatingAndLeaving(RestaurantJPCustomer cust){
		//Do("DoneAndLeaving message received from " + cust.toString());
		for(MyCustomer myC : myCustomers){
			if(myC.customer == cust)
				myC.s =state.done;
		}
		stateChanged();
	}

	public void msgAskForBreak(){
		//Do("Received message GoOnBreak");
		building.host.msgWantToGoOnBreak(this);
		hasRequestedBreak = true;
		stateChanged();
	}
	
	public void msgBreakGranted(boolean answer){
		//Do("Heard back from Waiter");
		if(answer == true)
			onBreak = true;
		else 
			hasRequestedBreak = false;
	}
	
	public void msgComeBackFromBreak(){
		//Do("received message ComeBackFromBreak");
		comingBackFromBreak = true;
		stateChanged();
	}
	
	// Scheduler
	
	public boolean runScheduler() {
		if(wantsInactive && myCustomers.size() == 0) {
			super.setInactive();
			building.host.msgSetUnavailable(this);
			this.getPerson().setCash(this.getPerson().getCash() + RestaurantJP.WORKER_SALARY);
			wantsInactive = false;
		} 
		try {	
			for(MyCustomer myC : myCustomers) {
				if(myC.s == state.waiting && onBreak==false){
					BringCustomerToTable(myC);
					//myC.s = state.orderReady;
					return true;
				}
			}
			for(MyCustomer myC : myCustomers) {
				if(myC.s == state.readyToOrder){
					TakeOrder(myC);
					return true;
				}
			}
			for(MyCustomer myC : myCustomers) {
				if(myC.s == state.ordered){
					GiveOrderToCook(myC);
					return true;
				}
			}
			for(MyCustomer myC : myCustomers) {
				if(myC.s == state.reOrder){
					RetakeOrder(myC);
					return true;
				}
			}
			for(MyCustomer myC : myCustomers) {
				if(myC.s == state.foodReady){
					DeliverFood(myC);
					return true;
				}
			}
			for(MyCustomer myC : myCustomers) {
				if(myC.s == state.checkReady){
					DeliverCheck(myC);
					return true;
				}
			}
			for(MyCustomer myC : myCustomers) {
				if(myC.s == state.done){
					LeaveCustomer(myC);
					return true;
				}
			}
			if(comingBackFromBreak){
				TellHostYoureBack();
				return true;
			}
			return false;
		} catch(ConcurrentModificationException e) {
			return false;
		}
	}
	
	// Actions

	private void BringCustomerToTable(MyCustomer myC) {
		//Do("Waiter is Seating " + myC.customer + " at " + myC.table);
		waiterGui.DoPickUpCustomer();
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {}
		
		myC.customer.msgFollowMeToTable(menu, myC.table.tableNumber, this);
		waiterGui.DoGoToTable(myC.table.tableNumber);
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {}
		
		myC.table.setOccupant(myC.customer);
		//Do("Setting seated");
		myC.s = state.seated;			
	}

	private void TakeOrder(MyCustomer myC) {
		waiterGui.DoGoToTable(myC.table.tableNumber);
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {}
		
		//waiterGui.DoTakeOrder(myC.table);
		myC.s = state.decidingOrder;
		myC.customer.msgWhatWouldYouLike();
	}
	
	public abstract void GiveOrderToCook (MyCustomer myC);
	
	private void RetakeOrder(MyCustomer myC){
		waiterGui.DoGoToTable(myC.table.tableNumber);
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {}
		myC.s = state.decidingOrder;
		myC.customer.msgOutOfChoice(menu);
	}
	
	private void DeliverFood(MyCustomer myC){
		waiterGui.DoGoToCook();
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {}
		building.cook.msgFoodRetrieved(myC.choice);
		waiterGui.DoDeliverFood(myC.choice);
		waiterGui.DoGoToTable(myC.table.tableNumber);
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {}
		//myC.s = state.served;
		waiterGui.setDelivering(false);
		myC.s = state.served;
		myC.customer.msgHereIsYourFood();
		building.cashier.msgComputeBill(this, myC.customer, myC.choice);
	}
	
	private void DeliverCheck(MyCustomer myC){
		myC.customer.msgHereIsCheck(myC.check, building.cashier);
		myC.s = state.checkReceived;
	}
	
	private void LeaveCustomer(MyCustomer myC){
		//Do("Waiter is Leaving " + myC.customer.getName());
		waiterGui.DoLeaveCustomer();
		building.host.msgTableIsFree(myC.table);
		myCustomers.remove(myC);
	}
	
	private void TellHostYoureBack(){
		building.host.msgOffBreak(this);							
		onBreak = false;
		comingBackFromBreak = false;
	}
	
	
	// Getters
	
	public RestaurantJPWaiterAnimation getGui(){
		return waiterGui;
	}
	
	public boolean onBreak(){
		return onBreak;
	}
	
	public boolean hasRequestedBreak(){
		return hasRequestedBreak;
	}
	
	public int getSalary(){
		return RestaurantJPBuilding.WORKER_SALARY;
	}
	
	public List<MyCustomer> getCustomers(){
		return myCustomers;
	}
	
	
	// Setters
	
	@Override
	public void setActive() {
		super.setActive();
		waiterGui.DoGoToOrigin();
	}
	
	@Override
	public void setInactive() {
		if(myCustomers.size() == 0){
			super.setInactive();
			this.getPerson().setCash(this.getPerson().getCash() + RestaurantJP.WORKER_SALARY);
		}
		else
			wantsInactive = true;
		building.host.msgSetUnavailable(this);
	}

	public void setAnimation(RestaurantJPWaiterAnimation gui) {
		waiterGui = gui;
	}
	
	public void setRevolvingStand(RestaurantJPRevolvingStand rs) {
		revolvingStand = rs;
	}
	
	public void setGui(RestaurantJPWaiterAnimation gui) {
		waiterGui = gui;
	}
	
	public void setRevolvingOrderStand(RestaurantJPRevolvingStand orderStand){
		revolvingStand = orderStand;
	}
	
	// Utilities
	
	public void removeAllCustomers(){
		myCustomers.clear();
	}
	
	// Classes
	
	public class MyCustomer {
		public RestaurantJPCustomer customer;
		public String choice;
		public state s;
		public RestaurantJPTableClass table;
		public int check;
		
		public void myCustomer() {
			customer = null;
			choice = new String();
			table = new RestaurantJPTableClass(0);
		}
		
		RestaurantJPCustomer getCustomer(){
			return customer;
		}
		
		RestaurantJPTableClass getTable(){
			return table;
		}
	}

}
