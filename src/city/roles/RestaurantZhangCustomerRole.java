package city.roles;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangTable;
import city.Role;
import city.animations.RestaurantZhangCustomerAnimation;
import city.animations.interfaces.RestaurantZhangAnimatedCustomer;
import city.interfaces.RestaurantZhangCashier;
import city.interfaces.RestaurantZhangCustomer;
import city.interfaces.RestaurantZhangHost;
import city.interfaces.RestaurantZhangWaiter;

/**
 * Restaurant customer agent.
 */
public class RestaurantZhangCustomerRole extends Role implements RestaurantZhangCustomer {
	private String name;
	private int hungerLevel = 5; // Determines length of meal
	private static final int DECIDINGTIME = 3000;
	private static final int EATINGTIME = 6000;
	private static final int CHANCETOLEAVE = 2;
	Timer timer = new Timer(); // Timer for waiting actions
	private RestaurantZhangAnimatedCustomer customerAnimation;
	
	public RestaurantZhangTable myTable;

	// Agent correspondents
	public RestaurantZhangHost host;
	public RestaurantZhangWaiter myWaiter;
	
	public int waitingPosition;
	
	// Menu and choices
	public RestaurantZhangMenu customerMenu;
	public String choice;
	
	public RestaurantZhangCashier myCashier;
	public RestaurantZhangCheck myCheck;
	public int money;
	public int myTab = 0;
	
	public enum AgentState
	{DoingNothing, AtEntrance, GoingToWaitingPosition, WaitingInRestaurant, ChoosingToLeave, DecidedToWait, BeingSeated, Deciding, Ordering, Ordered, Eating, WaitingForCheck, PayingForCheck, Leaving};
	public AgentState state = AgentState.DoingNothing;

	public enum AgentEvent 
	{none, gotWaitingPosition, atWaitingPosition, gotHungry, restaurantFull, restaurantClosed, followWaiter, Seated, Decided, TellWaiterOrder, OrderAgain, GotFood, gotCheck, gotChange, gotTab, DoneEating};
	public AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customerAnimation so the customer can send it messages
	 */
	public RestaurantZhangCustomerRole(){
		super();
		money = Math.abs(new Random().nextInt()%40) + 5; // TODO get rid of this and replace with the actual money
//		if(name.contains("broke")) // Hack to demo non norm scenario that customer will leave
//			money = 0.99;
//		if(name.contains("thief"))
//			money = 0.00;
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(RestaurantZhangHost host) {
		this.host = host;
	}

	public String getCustomerName() {
		return name;
	}
	
	public void gotHungry() {//from animation
		print("I'm hungry");
		event = AgentEvent.gotHungry;
		if(myTab > 0) {
			money +=25.00;
		}
		print("I have " + myTab + " tab");
		print("I have " + money + " money");
		stateChanged();
	}
	
	// Messages
	
	public void msgHereIsYourWaitingPosition(int pos) {
		waitingPosition = pos;
		event = AgentEvent.gotWaitingPosition;
		stateChanged();
	}
	
	public void msgRestaurantFull() {
		if(state != AgentState.DecidedToWait) {
			event = AgentEvent.restaurantFull;
			stateChanged();
		}
	}
	
	public void msgRestaurantClosed() {
		print(state.name());
		event = AgentEvent.restaurantClosed;
		stateChanged();
	}
	
	public void msgFollowMe(RestaurantZhangWaiter w, RestaurantZhangMenu menu, RestaurantZhangTable t) {
		myWaiter = w;
		event = AgentEvent.followWaiter;
		customerMenu = menu;
		myTable = t;
		stateChanged();
	}
	
	public void msgWhatWouldYouLike() {
		event = AgentEvent.TellWaiterOrder;
		stateChanged();
	}
	
	public void msgOrderAgain() {
		event = AgentEvent.OrderAgain;
		stateChanged();
	}
	
	public void msgHereIsYourFood(String choice) {
		event = AgentEvent.GotFood;
		stateChanged();
	}
	
	public void msgHereIsCustCheck(RestaurantZhangCheck c) {
		myCheck = c;
		event = AgentEvent.gotCheck;
		stateChanged();
	}
	
	public void msgHereIsChange(int change) {
		money = change;
		event = AgentEvent.gotChange;
		stateChanged();
	}
	
	public void msgPayLater(int tab) {
		event = AgentEvent.gotTab;
		money = 0;
		myTab += tab;
		stateChanged();
	}
	
	public void msgAnimationFinishedEnterRestaurant() {
		event = AgentEvent.atWaitingPosition;
		stateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		// From animation
		event = AgentEvent.Seated;
		stateChanged();
		
	}
	
	public void msgAnimationFinishedLeaveRestaurant() {
		// From animation
		event = AgentEvent.DoneEating;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	@Override
	public boolean runScheduler() {
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.AtEntrance;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.AtEntrance && event == AgentEvent.gotWaitingPosition) {
			state = AgentState.GoingToWaitingPosition;
			goToWaitingPosition();
			return true;
		}
		if (state == AgentState.GoingToWaitingPosition && event == AgentEvent.atWaitingPosition) {
			state = AgentState.WaitingInRestaurant;
			notifyHost();
			return true;
		}
		if(state == AgentState.WaitingInRestaurant && event == AgentEvent.restaurantFull) {
			state = AgentState.ChoosingToLeave;
			chooseToLeave();
			return true;
		}
		if(state == AgentState.AtEntrance && event == AgentEvent.restaurantClosed) {
			state = AgentState.Leaving;
			leaveRestaurant();
			return true;
		}
		if ((state == AgentState.WaitingInRestaurant || state == AgentState.DecidedToWait) 
				&& event == AgentEvent.followWaiter ){
			state = AgentState.BeingSeated;
			sitDown();
			return true;
		}
		if (state == AgentState.Deciding && event == AgentEvent.Decided){
			state = AgentState.Ordering;
			getWaiter();
			return true;
		}
		if (state == AgentState.Ordering && event == AgentEvent.TellWaiterOrder){
			state = AgentState.Ordered;
			giveOrderToWaiter();
			return true;
		}
		if (state == AgentState.Ordered && event == AgentEvent.OrderAgain) {
			state = AgentState.Deciding;
			decideOrder();
			return true;
		}
		if (state == AgentState.Ordered && event == AgentEvent.GotFood){
			state = AgentState.Eating;
			eatFood();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.DoneEating){
			state = AgentState.WaitingForCheck;
			askForCheck();
			return true;
		}
		if (state == AgentState.WaitingForCheck && event == AgentEvent.gotCheck){
			state = AgentState.PayingForCheck;
			payForCheck();
			return true;
		}
		if (state == AgentState.PayingForCheck && event == AgentEvent.gotChange){
			state = AgentState.Leaving;
			leaveRestaurant();
			return true;
		}
		
		if (state == AgentState.PayingForCheck && event == AgentEvent.gotTab){
			state = AgentState.Leaving;
			leaveRestaurant();
			return true;
		}
		return false;
	}

	// Actions

	private void goToRestaurant() {
		print("Going to restaurant entrance");
		host.msgImEntering(this);
	}
	
	private void goToWaitingPosition() {
		print("Going to waiting position");
		customerAnimation.setWaitingPosition(30, 30 + 31 * waitingPosition);
		DoGoToRestaurant();
	}
	
	private void notifyHost() {
		print("Telling host I want food");
		host.msgIWantFood(this);
	}
	
	private void chooseToLeave() {
//		if(name.contains("stayer")) {
//			print("I'm waiting for a full restaurant");
//			state = AgentState.DecidedToWait;
//			return;
//		}
//		if(name.contains("leaver")) {
//			print("I'm leaving a full restaurant");
//			host.msgImLeaving(this);
//			state = AgentState.Leaving;
//			leaveRestaurant();
//			return;
//		}
		if(new Random().nextInt(CHANCETOLEAVE) == 0) {
			print("I'm leaving a full restaurant");
			host.msgImLeaving(this);
			state = AgentState.Leaving;
			leaveRestaurant();
		} else {
			print("I'm waiting for a full restaurant");
			state = AgentState.DecidedToWait;
		}
	}

	private void sitDown() {
		print("Being seated. Going to table");
		customerAnimation.DoGoToSeat(myTable);
		state = AgentState.Deciding;
		decideOrder();
	}
	
	private void decideOrder() {
		customerAnimation.setFoodLabel("", true);
		timer.schedule(new TimerTask() {
			public void run() {
				choice = customerMenu.randomChoice(money);
//				if(name.contains("thief")) { // Hack to make customer steal
//					choice = customerMenu.randomChoice(50.00);
//				}
//				if(name.contains("chicken")) {
//					if(customerMenu.getMenu().containsKey("Chicken")) {
//						choice = "Chicken";
//					} else {
//						choice = "None";
//					}
//				}
				if(choice == "None") {
					print("No more items on menu, leaving");
					leaveRestaurantWithoutOrdering();
					return;
				} else if(choice == "Expensive") {
					print("Cannot afford anything on menu, leaving");
					leaveRestaurantWithoutOrdering();
					return;
				}
				print("Decided order: " + choice);
				event = AgentEvent.Decided;
				stateChanged();
			}
		},
		DECIDINGTIME);
	}
	
	private void getWaiter() {
		print("Getting waiter.");
		myWaiter.msgReadyToOrder(this);
	}
	
	private void giveOrderToWaiter() {
		print("Giving order " + choice + " to waiter.");
		myWaiter.msgHereIsMyChoice(this, choice);
		customerAnimation.setFoodLabel(choice, false);
	}
	
	private void eatFood() {
		print("Eating Food : " + choice);
		customerAnimation.setFoodLabel(choice, true);
		timer.schedule(new TimerTask() {
			public void run() {
				print("Done eating " + choice);
				event = AgentEvent.DoneEating;
				stateChanged();
			}
		},
		EATINGTIME);
	}
	
	private void askForCheck() {
		print("Asking waiter for check");
		myWaiter.msgHereWasMyOrder(this, this.choice);
	}
	
	private void payForCheck() {
		print("Paying for check, check amount is: " + myCheck.price);
		myWaiter.msgLeavingTable(this);
		myCashier.msgHereIsPayment(myCheck, money);
	}

	private void leaveRestaurant() {
		print("Leaving restaurant.");
		state = AgentState.Leaving;
		customerAnimation.DoExitRestaurant();
		state = AgentState.DoingNothing;
	}
	
	private void leaveRestaurantWithoutOrdering() {
		print("Leaving restaurant.");
		state = AgentState.Leaving;
		myWaiter.msgLeavingTable(this);
		customerAnimation.DoExitRestaurant();
		state = AgentState.DoingNothing;
	}
	
	private void DoGoToRestaurant() {
		customerAnimation.DoGoToEntrance();
	}

	// Accessors, etc.

	public String getName() {
		return super.getPerson().getName();
	}
	
	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setAnimation(RestaurantZhangAnimatedCustomer g) {
		customerAnimation = g;
	}
	
	public void setCashier(RestaurantZhangCashier c) {
		myCashier = c;
	}
	
	public RestaurantZhangAnimatedCustomer getGui() {
		return customerAnimation;
	}
	
	public int getPos() {
		return waitingPosition;
	}
	
	public void setActive() {
		super.setActive();
		gotHungry();
	}
}

