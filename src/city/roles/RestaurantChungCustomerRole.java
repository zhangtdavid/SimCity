package city.roles;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import utilities.RestaurantChungMenu;
import city.Role;
import city.animations.RestaurantChungCustomerAnimation;
import city.buildings.RestaurantChungBuilding;
import city.interfaces.RestaurantChungCustomer;
import city.interfaces.RestaurantChungWaiter;

/**
 * Restaurant Customer agent.
 */
public class RestaurantChungCustomerRole extends Role implements RestaurantChungCustomer {
//	Data
//	=====================================================================	
	Timer timer = new Timer();
	RestaurantChungBuilding restaurant;
	private RestaurantChungWaiter waiter;
	private Semaphore atSeat = new Semaphore(0, true);
	private Semaphore atCashier = new Semaphore(0, true);
	
	private int hungerLevel = 10; // determines length of meal
	int positionInLine;
	int bill;
	RestaurantChungMenu menu;
	private String order;
	
	private AgentState state = AgentState.DoingNothing; //The start state
	private AgentEvent event = AgentEvent.none;

//	Constructor
//	====================================================================
	public RestaurantChungCustomerRole(){
		super();
	}

//	public void setActive(){
//		this.setActivityBegun();
//	}
	
//  Messages
//	=====================================================================
	@Override
	public void gotHungry() {//from animation
		print("I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}

	@Override
	public void msgGetInLinePosition(int pos) {
		print("Customer received msgGetInLinePosition " + pos); // TODO
		positionInLine = pos;
		event = AgentEvent.getInLine;
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
	public void msgOutOfItem(String choice, RestaurantChungMenu menu) {
		print("Customer received msgOutOfItem " + choice);
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
	public boolean runScheduler() {		
		if (state == AgentState.DoingNothing && event == AgentEvent.getInLine || state == AgentState.WaitingInRestaurant && event == AgentEvent.getInLine) {
			getInLine();
			return true;
		}
		
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
	private void getInLine() {
		print("Getting in position " + positionInLine + " of the line at restaurant");
		state = AgentState.WaitingInRestaurant;
		this.getAnimation(RestaurantChungCustomerAnimation.class).DoGoToWaitingArea(positionInLine);
		event = AgentEvent.standInLine;
	}
	
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
					if (menu.items.get(randInt).price > money) {
						menu.items.remove(randInt);						
					}
					else {
						order = menu.items.get(randInt).item;
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
			restaurant.cashier.msgHereIsPayment(this, this.getPerson().getCash());
			this.getPerson().setCash(0);
			return;
		}
		
		restaurant.cashier.msgHereIsPayment(this, bill);		
		this.getPerson().setCash(this.getPerson().getCash() - bill);
	}
	
	private void leaveRestaurant() {
		print("Leaving");
		state = AgentState.Leaving;
		restaurant.host.msgLeaving(this);
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
			restaurant.host.msgDecidedToStay(this);
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
	public String getOrder() {
		return order;
	}
	
//	Setters
//	=====================================================================	
	@Override
	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
	}	
	
	@Override
	public void setRestaurant(RestaurantChungBuilding r) {
		restaurant = r;
	}
	
//  Utilities
//	=====================================================================
	@Override
	public void print(String msg) {
        super.print(msg);
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTCHUNG, "RestaurantChungCustomerRole " + this.getPerson().getName(), msg);
    }

}
