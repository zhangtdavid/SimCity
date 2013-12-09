package city.roles;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import utilities.RestaurantChungMenu;
import city.Application.FOOD_ITEMS;
import city.animations.RestaurantChungCustomerAnimation;
import city.bases.Role;
import city.buildings.interfaces.RestaurantChung;
import city.roles.interfaces.RestaurantChungCustomer;
import city.roles.interfaces.RestaurantChungWaiter;

/**
 * Restaurant Customer agent.
 */
public class RestaurantChungCustomerRole extends Role implements RestaurantChungCustomer {
//	Data
//	=====================================================================	
	Timer timer = new Timer();
	private Semaphore atSeat = new Semaphore(0, true);
	private Semaphore atCashier = new Semaphore(0, true);
	
	RestaurantChung restaurant;
	private RestaurantChungWaiter waiter;
	
	private int hungerLevel = 10; // determines length of meal
	int bill;
	RestaurantChungMenu menu;
	private FOOD_ITEMS order;
	
	private AgentState state = AgentState.DoingNothing; //The start state
	private AgentEvent event = AgentEvent.none;

//	Constructor
//	=====================================================================
	public RestaurantChungCustomerRole(){
		super();
		state = AgentState.WaitingInRestaurant;
	}
	
//  Messages
//	=====================================================================
	@Override
	public void gotHungry() {//from animation
		print("I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	@Override
	public void msgNoTablesAvailable() {
		print("Customer received msgNoTablesAvailable");
		event = AgentEvent.noTables;		
		stateChanged();
	}
	
	@Override
	public void msgSelfDecidedToLeave() {
		event = AgentEvent.decidedToLeave;		
		stateChanged();
	}
	
	@Override
	public void msgFollowMeToTable(RestaurantChungWaiter waiter, RestaurantChungMenu menu) {
		print("Customer received msgFollowMeToTable");
		this.waiter = waiter;
		this.menu = new RestaurantChungMenu(menu);
		event = AgentEvent.followWaiter;
		stateChanged();
	}
	
	@Override
	public void msgAnimationAtSeat() {
		print("Customer received msgAnimationAtSeat");
		event = AgentEvent.seated;
		//from animation
		atSeat.release();// = true;
		stateChanged();
	}
	
	@Override
	public void msgSelfReadyToOrder() {
		event = AgentEvent.readyToOrder;
		stateChanged();
	}
	
	@Override
	public void msgWhatWouldYouLike() {
		print("Customer received msgWhatWouldYouLike");
		event = AgentEvent.askedForOrder;
		stateChanged();
	}
	
	@Override
	public void msgOutOfItem(FOOD_ITEMS choice, RestaurantChungMenu menu) {
		print("Customer received msgOutOfItem " + choice.toString());
		this.menu = new RestaurantChungMenu(menu);
		event = AgentEvent.seated;
		stateChanged();
	}
	
	@Override
	public void msgHereIsYourFood() {
		print("Customer received msgHereIsYourFood");
		event = AgentEvent.receivedFood;
		stateChanged();
	}
	
	@Override
	public void msgSelfDoneEating() {
		event = AgentEvent.doneEating;
		stateChanged();
	}
	
	@Override
	public void msgHereIsCheck(int price) {
		event = AgentEvent.receivedCheck;
		bill = price;
		stateChanged();
	}
	
	@Override
	public void msgAnimationAtCashier() {
		print("Customer received msgAnimationAtCashier");
		event = AgentEvent.atCashier;
		//from animation
		atCashier.release();// = true;
		stateChanged();	
	}
	
	@Override
	public void msgHereIsChange(int change) {
		this.getPerson().setCash(this.getPerson().getCash() + change);
		event = AgentEvent.receivedChange;
		stateChanged();		
	}
	
	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		print("Customer received msgAnimationFinishedLeaveRestaurant");
		event = AgentEvent.doneLeaving;
		super.setInactive();
	}
	
	@Override
	public void msgKickingYouOutAfterPaying(int debt) {
		print("Customer received msgKickingYouOutAfterPaying");
		bill = debt;
		state = AgentState.WaitingForCheck;
		event = AgentEvent.receivedCheck;
		stateChanged();		
	}

//  Scheduler
//	=====================================================================
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	@Override
	public boolean runScheduler() {		
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followWaiter) {
			sitDown();
			return true;
		}
	
		if (state == AgentState.Deciding && event == AgentEvent.readyToOrder) {
			callWaiter();
			return true;
		}
		
		if (state == AgentState.CallingWaiter && event == AgentEvent.askedForOrder) {
			giveOrder();
			return true;
		}
		
		if (state == AgentState.WaitingForFood && event == AgentEvent.receivedFood) {
			eatFood();
			return true;
		}
		
		if (state == AgentState.Eating && event == AgentEvent.doneEating) {
			askForCheck();
			return true;
		}
		
		if (state == AgentState.WaitingForCheck && event == AgentEvent.receivedCheck) {
			goToCashier();
			return true;
		}
		
		if (state == AgentState.GoingToCashier && event == AgentEvent.atCashier) {
			payForFood();
			return true;
		}
		
		if (state == AgentState.Paying && event == AgentEvent.receivedChange) {
			leaveRestaurant();
			return true;
		}
		
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving) {
			state = AgentState.DoingNothing;
			return true;
		}
		
//  Non-Normative Scenarios
//		---------------------------------------------------------------	
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.kickedOut) {
			leaveRestaurant();
			return true;
		}
		
		// No tables available
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.noTables) {
			decideLeaving();
			return true;
		}
		
		if (state == AgentState.DecidedToStay && event == AgentEvent.followWaiter) {
			sitDown();
			return true;
		}
		
		if (state == AgentState.DecideLeaving && event == AgentEvent.decidedToLeave) {
			leaveRestaurant();
			return true;
		}
		
		// Original order is unavailable, have to reorder
		if (state == AgentState.BeingSeated && event == AgentEvent.seated || state == AgentState.WaitingForFood && event == AgentEvent.seated) {
			decideFood();
			return true;
		}
		
		// Customer cannot afford anything
		if (state == AgentState.Deciding && event == AgentEvent.decidedToLeave) {
			leaveTable();
			return true;
		}
		
		return false;
	}

//  Actions
//	=====================================================================	
	private void sitDown() {
		print("Sitting down");
		state = AgentState.BeingSeated;
		try {
			atSeat.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void decideFood() {
		print("Deciding food");
		state = AgentState.Deciding;
		order = null; // erases any old orders
		// Randomly select food from the menu
		final int money = this.getPerson().getCash(); // Need to do this to get access to money in the timer task
		timer.schedule(new TimerTask() {
		public void run() {
				Random rand = new Random();
				while (order == null) {
					if (menu.items.size() == 0) {
						msgSelfDecidedToLeave();
						return;
					}
					int randInt = rand.nextInt(menu.items.size());
					if (menu.items.get(randInt).getPrice() > money) {
						menu.items.remove(randInt);						
					}
					else {
						order = menu.items.get(randInt).getItem();
						print("Finished deciding food, customer wants " + order);
						msgSelfReadyToOrder();	
					}
				}
			}
		},
		1000);
	}
	
	private void callWaiter() {
		print("Call Waiter");
		state = AgentState.CallingWaiter;
		waiter.msgReadyToOrder(this);
	}
	
	private void giveOrder() {
		print("Ordering " + order);
		state = AgentState.WaitingForFood;
		waiter.msgHereIsMyOrder(this, order);
	}

	private void eatFood() {
		print("Eating Food");
		state = AgentState.Eating;
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			public void run() {
				print("Done eating " + order);
				msgSelfDoneEating();
				//isHungry = false;
			}
		},
		getHungerLevel() * 800);//how long to wait before running task
	}

	private void askForCheck() {
		print("Asking for check");
		state = AgentState.WaitingForCheck;
		waiter.msgGetCheck(this);
	}
	
	private void goToCashier() {
		print("Going to cashier");
		state = AgentState.GoingToCashier;
		this.getAnimation(RestaurantChungCustomerAnimation.class).DoGoToCashier();		
	}
	
	private void payForFood() {
		print("Paying for food");
		state = AgentState.Paying;
		
		if (bill > this.getPerson().getCash()) {
			restaurant.getRestaurantChungCashier().msgHereIsPayment(this, this.getPerson().getCash());
			this.getPerson().setCash(0);
			return;
		}
		
		restaurant.getRestaurantChungCashier().msgHereIsPayment(this, bill);		
		this.getPerson().setCash(this.getPerson().getCash() - bill);
	}
	
	private void leaveRestaurant() {
		print("Leaving");
		state = AgentState.Leaving;
		restaurant.getRestaurantChungHost().msgLeaving(this);
		this.getAnimation(RestaurantChungCustomerAnimation.class).DoExitRestaurant();
	}
	
//  Non-Normative Scenarios
//	---------------------------------------------------------------	
	private void decideLeaving() {
		state = AgentState.DecideLeaving;
		Random rand = new Random();
		int leaving = rand.nextInt(2);

		if (leaving == 0) {
			print("Decided to leave");
			msgSelfDecidedToLeave();
		}
		else if(leaving == 1) {
			print("Decided to stay");
			state = AgentState.DecidedToStay;
			restaurant.getRestaurantChungHost().msgDecidedToStay(this);
		}
	}
	
	private void leaveTable() {
		print("Leaving");
		state = AgentState.Leaving;
		waiter.msgLeaving(this);
		this.getAnimation(RestaurantChungCustomerAnimation.class).DoExitRestaurant();
	}
	
//	Getters
//	=====================================================================	
	@Override
	public int getHungerLevel() {
		return hungerLevel;
	}
	
	@Override
	public String getState() {
		return state.toString();
	}
	
	@Override
	public FOOD_ITEMS getOrder() {
		return order;
	}
	
	@Override
	public String getStateString() {
		return state.toString();
	}
	
//	Setters
//	=====================================================================	
	@Override
	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
	}	
	
	@Override
	public void setRestaurant(RestaurantChung r) {
		restaurant = r;
	}
	
//  Utilities
//	=====================================================================
	@Override
	public void print(String msg) {
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTCHUNG, "RestaurantChungCustomerRole " + this.getPerson().getName(), msg);
    }

}
