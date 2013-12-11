package utilities;

import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantChungAnimatedWaiter;
import city.bases.JobRole;
import city.buildings.interfaces.RestaurantChung;
import city.buildings.interfaces.RestaurantChung.MyCustomer;
import city.buildings.interfaces.RestaurantChung.MyCustomer.CheckState;
import city.buildings.interfaces.RestaurantChung.MyCustomer.OrderStatus;
import city.buildings.interfaces.RestaurantChung.MyCustomer.WaiterCustomerState;
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
			restaurant.findCustomer(c).setWaiter(this);
			restaurant.findCustomer(c).setTable(table);
			restaurant.findCustomer(c).setWaiterCustomerState(WaiterCustomerState.Waiting);
			stateChanged();
		}
	}
	
	@Override
	public void msgReadyToOrder(RestaurantChungCustomer c) {
		print("Waiter received msgReadyToOrder");
		restaurant.findCustomer(c).setWaiterCustomerState(WaiterCustomerState.ReadyToOrder);
		print("customer is ready to order");
		stateChanged();
	}
	
	@Override
	public void msgHereIsMyOrder(RestaurantChungCustomer c, FOOD_ITEMS choice) {
		print("Waiter received msgHereIsMyOrder");
		restaurant.findCustomer(c).setChoice(choice);
		restaurant.findCustomer(c).setOrderStatus(OrderStatus.Ordered);
		print("customer ordered " + choice);
		stateChanged();
	}
	
	@Override
	public void msgOutOfItem(FOOD_ITEMS choice, int table) {
		print("Waiter received msgOutOfItem " + choice);
		restaurant.findCustomer(table).setOrderStatus(OrderStatus.Cancelled);
		menu.findItem(restaurant.findCustomer(table).getChoice()).setAvailable(false);
		stateChanged();
	}
	
	@Override
	public void msgOrderIsReady(FOOD_ITEMS choice, int table) {
		print("Waiter received msgOrderIsReady");
		restaurant.findCustomer(table).setOrderStatus(OrderStatus.DoneCooking);
		print("customer's order of " + choice + " is ready");		
		stateChanged();
	}
	
	@Override
	public void msgGetCheck(RestaurantChungCustomer c) {
		print("Waiter received msgGetCheck");
		restaurant.findCustomer(c).setWaiterCustomerState(WaiterCustomerState.WaitingForCheck);
		stateChanged();
	}
	
	@Override
	public void msgLeaving(RestaurantChungCustomer c) {
		print("Waiter received msgLeaving");
		restaurant.findCustomer(c).setWaiterCustomerState(WaiterCustomerState.Leaving);
		print("customer is leaving");
		stateChanged();
	}

	//	Cashier
	//	---------------------------------------------------------------
	@Override
	public void msgHereIsBill(RestaurantChungCustomer c, int price) {
		print("Waiter received msgHereIsBill");
		restaurant.findCustomer(c).setCheckState(CheckState.ReceivedBill);
		restaurant.findCustomer(c).setBill(price);
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
			if (restaurant.findWaiter(this).getNumCustomers() == 0)
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

		for (MyCustomer customer : restaurant.getCustomers()) {
			if (customer.getWaiter() == this && customer.getOrderStatus() == MyCustomer.OrderStatus.Cancelled) {
				try {
					informCustomerOfCancellation(customer);
				} catch (InterruptedException e) {}
				return true;
			}
		}
		
		for (MyCustomer customer : restaurant.getCustomers()) {
			if (customer.getWaiter() == this && customer.getWaiterCustomerState() == MyCustomer.WaiterCustomerState.Waiting) {
				try {
					seatCustomer(customer);
				} catch (InterruptedException e) {}
				return true;
			}
		}
		
		for (MyCustomer customer : restaurant.getCustomers()) {
			if (customer.getWaiter() == this && customer.getWaiterCustomerState() == MyCustomer.WaiterCustomerState.ReadyToOrder) {
				try {
					takeOrder(customer);
				} catch (InterruptedException e) {}
				return true;
			}
		}
		
		for (MyCustomer customer : restaurant.getCustomers()) {
			if (customer.getWaiter() == this && customer.getOrderStatus() == MyCustomer.OrderStatus.Ordered) {
				tellCookOrder(customer, customer.getChoice(), customer.getTable());
				return true;
			}
		}
		
		for (MyCustomer customer : restaurant.getCustomers()) {
			if (customer.getWaiter() == this && customer.getOrderStatus() == MyCustomer.OrderStatus.DoneCooking) {
				try {
					pickUpOrder(customer);
				} catch (InterruptedException e) {}
				return true;
			}
		}
		
		for (MyCustomer customer : restaurant.getCustomers()) {
			if (customer.getWaiter() == this && customer.getOrderStatus() == MyCustomer.OrderStatus.PickedUp) {
				try {
					deliverOrder(customer);
				} catch (InterruptedException e) {}
				return true;
			}
		}
		
		for (MyCustomer customer : restaurant.getCustomers()) {
			if (customer.getWaiter() == this && customer.getWaiterCustomerState() == MyCustomer.WaiterCustomerState.WaitingForCheck && customer.getCheckState() == MyCustomer.CheckState.None) {
				try {
					getCheck(customer);
				} catch (InterruptedException e) {}
				return true;
			}
		}
		
		for (MyCustomer customer : restaurant.getCustomers()) {
			if (customer.getWaiter() == this && customer.getCheckState() == MyCustomer.CheckState.ReceivedBill) {
				try {
					giveCheck(customer);
				} catch (InterruptedException e) {}
				return true;
			}
		}
		
		for (MyCustomer customer : restaurant.getCustomers()) {
			if (customer.getWaiter() == this && customer.getCheckState() == MyCustomer.CheckState.DeliveredBill) {
				customer.setWaiter(null);
				return true;
			}
		}
		
		// Non-norm when customer cannot afford anything
		for (MyCustomer customer : restaurant.getCustomers()) {
			if (customer.getWaiter() == this && customer.getWaiterCustomerState() == MyCustomer.WaiterCustomerState.Leaving) {
				removeCustomer(customer);
				return true;
			}
		}
		
		
		boolean haveCustomers = false;
		for (MyCustomer customer : restaurant.getCustomers()) {
			if (customer.getWaiter() == this) {
				haveCustomers = true;
			}
		}

		if (workingState == WorkingState.NotWorking && haveCustomers == false) {
			restaurant.removeWaiter(this);
			super.setInactive();
			this.getAnimation(RestaurantChungAnimatedWaiter.class).removeFromWaiterHomePositions();
		}
		
		if (state == BreakState.ApprovedForBreak && haveCustomers == false) {
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
		this.getAnimation(RestaurantChungAnimatedWaiter.class).setOffBreak();
		restaurant.getRestaurantChungHost().msgIAmReturningToWork(this);
		state = BreakState.Working;
	}
	
	private void goOnBreak() {
		print("Waiter going on break");
		state = BreakState.OnBreak;
		this.getAnimation(RestaurantChungAnimatedWaiter.class).DoGoOnBreak();
	}
	
	private void returnToWork() {
		print("Waiter returning to work");
		state = BreakState.Working;
		restaurant.getRestaurantChungHost().msgIAmReturningToWork(this);
		this.getAnimation(RestaurantChungAnimatedWaiter.class).DoReturnToWaiterHome();		
	}
	
	//	Customer
	//	---------------------------------------------------------------
	private void seatCustomer(MyCustomer customer) throws InterruptedException {	
		print("Waiter seating " + customer.getRestaurantChungCustomer() + " at " + customer.getTable());
		this.getAnimation(RestaurantChungAnimatedWaiter.class).DoGoToCustomerLine();
		atLine.acquire();
		restaurant.getRestaurantChungHost().msgTakingCustomerToTable(customer.getRestaurantChungCustomer());
		this.getAnimation(RestaurantChungAnimatedWaiter.class).DoBringToTable(customer.getRestaurantChungCustomer(), customer.getTable()-1);
		customer.getRestaurantChungCustomer().msgFollowMeToTable(this, menu);
		atTable.acquire();
		customer.setWaiterCustomerState(WaiterCustomerState.Seated);
		this.getAnimation(RestaurantChungAnimatedWaiter.class).DoReturnToWaiterHome();
	}
	
	private void informCustomerOfCancellation(MyCustomer customer) throws InterruptedException {
		this.getAnimation(RestaurantChungAnimatedWaiter.class).DoGoToTable(customer.getTable()-1);
		atTable.acquire();
		customer.getRestaurantChungCustomer().msgOutOfItem(customer.getChoice(), menu);
		// Resets all menu items to available after informing a single customer
		for (int i = 0; i < menu.items.size(); i++) {
			menu.items.get(i).setAvailable(true);
		}
		customer.setWaiterCustomerState(WaiterCustomerState.Seated);
		customer.setOrderStatus(OrderStatus.None);	

		this.getAnimation(RestaurantChungAnimatedWaiter.class).DoReturnToWaiterHome();
	}
	
	private void takeOrder(MyCustomer customer) throws InterruptedException {
		this.getAnimation(RestaurantChungAnimatedWaiter.class).DoGoToTable(customer.getTable()-1);
		atTable.acquire();
		print("taking order from " + customer.getRestaurantChungCustomer());		
		customer.getRestaurantChungCustomer().msgWhatWouldYouLike();
		customer.setWaiterCustomerState(WaiterCustomerState.Asked);
		
		this.getAnimation(RestaurantChungAnimatedWaiter.class).DoReturnToWaiterHome();
	}
	
	protected abstract void tellCookOrder(MyCustomer customer, FOOD_ITEMS f, int table);
	
	private void pickUpOrder(MyCustomer customer) throws InterruptedException {
		print("picking up order from cook");
		this.getAnimation(RestaurantChungAnimatedWaiter.class).DoGoToCook();
		atCook.acquire();
		customer.setOrderStatus(OrderStatus.PickedUp);
	}
	
	private void deliverOrder(MyCustomer customer) throws InterruptedException {
		this.getAnimation(RestaurantChungAnimatedWaiter.class).DoDeliverFood(customer.getTable()-1, customer.getChoice().toString());
		atTable.acquire();	
		customer.setOrderStatus(MyCustomer.OrderStatus.Delivered);
		customer.getRestaurantChungCustomer().msgHereIsYourFood();
		customer.setWaiterCustomerState(WaiterCustomerState.Eating);
		this.getAnimation(RestaurantChungAnimatedWaiter.class).DoReturnToWaiterHome();
	}
	
	private void getCheck(MyCustomer customer) throws InterruptedException {
		this.getAnimation(RestaurantChungAnimatedWaiter.class).DoGoToCashier();
		atCashier.acquire();
		customer.setCheckState(CheckState.AskedForBill);
		restaurant.getRestaurantChungCashier().msgComputeBill(this, customer.getRestaurantChungCustomer(), customer.getChoice());
	}
	
	private void giveCheck(MyCustomer customer) throws InterruptedException {
		this.getAnimation(RestaurantChungAnimatedWaiter.class).DoGoToTable(customer.getTable()-1);
		atTable.acquire();	
		customer.setCheckState(CheckState.DeliveredBill);
		customer.getRestaurantChungCustomer().msgHereIsCheck(customer.getBill());
		this.getAnimation(RestaurantChungAnimatedWaiter.class).DoReturnToWaiterHome();
		restaurant.getRestaurantChungHost().msgTableIsFree(this, customer.getTable(), customer.getRestaurantChungCustomer());
	}

	private void removeCustomer(MyCustomer customer) {
		restaurant.getRestaurantChungHost().msgTableIsFree(this, customer.getTable(), customer.getRestaurantChungCustomer());
		removeCustomerFromList(customer);
	}
	
//  Getters
//  ====================================================================
	@Override
	public WorkingState getWorkingState() {
		return workingState;
	}
	
//  Utilities
//	=====================================================================
	
	@Override
	public void removeCustomerFromList(MyCustomer c) {
		for(int i = 0; i < restaurant.getCustomers().size(); i ++) {
			if(restaurant.getCustomers().get(i) == c) {
				restaurant.getCustomers().remove(c);
			}
		}
	}
	
	@Override
	public void print(String msg) {
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTCHUNG, "RestaurantChungWaiterBaseRole " + this.getPerson().getName(), msg);
    }
}

