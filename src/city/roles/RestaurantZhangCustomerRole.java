package city.roles;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import trace.AlertLog;
import trace.AlertTag;
import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangTable;
import city.animations.interfaces.RestaurantZhangAnimatedCustomer;
import city.bases.Role;
import city.roles.interfaces.RestaurantZhangCashier;
import city.roles.interfaces.RestaurantZhangCustomer;
import city.roles.interfaces.RestaurantZhangHost;
import city.roles.interfaces.RestaurantZhangWaiter;

public class RestaurantZhangCustomerRole extends Role implements RestaurantZhangCustomer {
	
	// Data
	
	private Timer timer = new Timer(); // Timer for waiting actions
	private RestaurantZhangAnimatedCustomer customerAnimation;
	private RestaurantZhangTable myTable;
	private RestaurantZhangHost host;
	private RestaurantZhangWaiter myWaiter;
	private int waitingPosition = 0;
	private RestaurantZhangMenu customerMenu;
	private String choice;
	private RestaurantZhangCashier myCashier;
	private RestaurantZhangCheck myCheck;
	private int money;
	private int myTab = 0;
	private AGENTSTATE state = AGENTSTATE.DoingNothing;
	private AGENTEVENT event = AGENTEVENT.none;
	
	// Constructor
	
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customerAnimation so the customer can send it messages
	 */
	public RestaurantZhangCustomerRole(){
		super();
		money = Math.abs(new Random().nextInt()%40) + 25; // TODO get rid of this and replace with the actual money
//		if(name.contains("broke")) // Hack to demo non norm scenario that customer will leave
//			money = 0.99;
//		if(name.contains("thief"))
//			money = 0.00;
	}
	
	// Messages
	
	@Override
	public void msgGotHungry() {
		print("I'm hungry");
		event = AGENTEVENT.gotHungry;
		if(myTab > 0) {
			money +=25.00;
		}
		print("I have " + myTab + " tab");
		print("I have " + money + " money");
		stateChanged();
	}
	
	@Override
	public void msgHereIsYourWaitingPosition(int pos) {
		waitingPosition = pos;
		event = AGENTEVENT.gotWaitingPosition;
		stateChanged();
	}
	
	@Override
	public void msgRestaurantFull() {
		if(state != AGENTSTATE.DecidedToWait) {
			event = AGENTEVENT.restaurantFull;
			stateChanged();
		}
	}
	
	@Override
	public void msgRestaurantClosed() {
		print(state.name());
		event = AGENTEVENT.restaurantClosed;
		stateChanged();
	}
	
	@Override
	public void msgFollowMe(RestaurantZhangWaiter w, RestaurantZhangMenu menu, RestaurantZhangTable t) {
		myWaiter = w;
		event = AGENTEVENT.followWaiter;
		customerMenu = menu;
		myTable = t;
		stateChanged();
	}
	
	@Override
	public void msgWhatWouldYouLike() {
		event = AGENTEVENT.TellWaiterOrder;
		stateChanged();
	}
	
	@Override
	public void msgOrderAgain() {
		event = AGENTEVENT.OrderAgain;
		stateChanged();
	}
	
	@Override
	public void msgHereIsYourFood(String choice) {
		event = AGENTEVENT.GotFood;
		stateChanged();
	}
	
	@Override
	public void msgHereIsCustCheck(RestaurantZhangCheck c) {
		myCheck = c;
		event = AGENTEVENT.gotCheck;
		stateChanged();
	}
	
	@Override
	public void msgHereIsChange(int change) {
		money = change;
		event = AGENTEVENT.gotChange;
		stateChanged();
	}
	
	@Override
	public void msgPayLater(int tab) {
		event = AGENTEVENT.gotTab;
		money = 0;
		myTab += tab;
		stateChanged();
	}
	
	@Override
	public void msgAnimationFinishedEnterRestaurant() {
		event = AGENTEVENT.atWaitingPosition;
		stateChanged();
	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		// From animation
		event = AGENTEVENT.Seated;
		stateChanged();
		
	}
	
	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// From animation
		event = AGENTEVENT.DoneEating;
		stateChanged();
		setInactive();
	}

	// Scheduler
	
	@Override
	public boolean runScheduler() {
		if (state == AGENTSTATE.DoingNothing && event == AGENTEVENT.gotHungry ){
			state = AGENTSTATE.AtEntrance;
			goToRestaurant();
			return true;
		}
		if (state == AGENTSTATE.AtEntrance && event == AGENTEVENT.gotWaitingPosition) {
			state = AGENTSTATE.GoingToWaitingPosition;
			goToWaitingPosition();
			return true;
		}
		if (state == AGENTSTATE.GoingToWaitingPosition && event == AGENTEVENT.atWaitingPosition) {
			state = AGENTSTATE.WaitingInRestaurant;
			notifyHost();
			return true;
		}
		if(state == AGENTSTATE.WaitingInRestaurant && event == AGENTEVENT.restaurantFull) {
			state = AGENTSTATE.ChoosingToLeave;
			chooseToLeave();
			return true;
		}
		if(state == AGENTSTATE.AtEntrance && event == AGENTEVENT.restaurantClosed) {
			state = AGENTSTATE.Leaving;
			leaveRestaurant();
			return true;
		}
		if ((state == AGENTSTATE.WaitingInRestaurant || state == AGENTSTATE.DecidedToWait) 
				&& event == AGENTEVENT.followWaiter ){
			state = AGENTSTATE.BeingSeated;
			sitDown();
			return true;
		}
		if (state == AGENTSTATE.Deciding && event == AGENTEVENT.Decided){
			state = AGENTSTATE.Ordering;
			getWaitersAttention();
			return true;
		}
		if (state == AGENTSTATE.Ordering && event == AGENTEVENT.TellWaiterOrder){
			state = AGENTSTATE.Ordered;
			giveOrderToWaiter();
			return true;
		}
		if (state == AGENTSTATE.Ordered && event == AGENTEVENT.OrderAgain) {
			state = AGENTSTATE.Deciding;
			decideOrder();
			return true;
		}
		if (state == AGENTSTATE.Ordered && event == AGENTEVENT.GotFood){
			state = AGENTSTATE.Eating;
			eatFood();
			return true;
		}
		if (state == AGENTSTATE.Eating && event == AGENTEVENT.DoneEating){
			state = AGENTSTATE.WaitingForCheck;
			askForCheck();
			return true;
		}
		if (state == AGENTSTATE.WaitingForCheck && event == AGENTEVENT.gotCheck){
			state = AGENTSTATE.PayingForCheck;
			payForCheck();
			return true;
		}
		if (state == AGENTSTATE.PayingForCheck && event == AGENTEVENT.gotChange){
			state = AGENTSTATE.Leaving;
			leaveRestaurant();
			return true;
		}
		
		if (state == AGENTSTATE.PayingForCheck && event == AGENTEVENT.gotTab){
			state = AGENTSTATE.Leaving;
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
		doGoToRestaurant();
	}
	
	private void notifyHost() {
		print("Telling host I want food");
		host.msgIWantFood(this);
	}
	
	private void chooseToLeave() {
//		if(name.contains("stayer")) {
//			print("I'm waiting for a full restaurant");
//			state = AGENTSTATE.DecidedToWait;
//			return;
//		}
//		if(name.contains("leaver")) {
//			print("I'm leaving a full restaurant");
//			host.msgImLeaving(this);
//			state = AGENTSTATE.Leaving;
//			leaveRestaurant();
//			return;
//		}
		if(new Random().nextInt(CHANCETOLEAVE) == 0) {
			print("I'm leaving a full restaurant");
			host.msgImLeaving(this);
			state = AGENTSTATE.Leaving;
			leaveRestaurant();
		} else {
			print("I'm waiting for a full restaurant");
			state = AGENTSTATE.DecidedToWait;
		}
	}

	private void sitDown() {
		print("Being seated. Going to table");
		customerAnimation.DoGoToSeat(myTable);
		state = AGENTSTATE.Deciding;
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
//					if(customerMenu.getMenu().containsKey("chicken")) {
//						choice = "chicken";
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
				event = AGENTEVENT.Decided;
				stateChanged();
			}
		},
		DECIDINGTIME);
	}
	
	private void getWaitersAttention() {
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
				event = AGENTEVENT.DoneEating;
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
		state = AGENTSTATE.Leaving;
		customerAnimation.DoExitRestaurant();
		state = AGENTSTATE.DoingNothing;
	}
	
	private void leaveRestaurantWithoutOrdering() {
		print("Leaving restaurant.");
		state = AGENTSTATE.Leaving;
		myWaiter.msgLeavingTable(this);
		customerAnimation.DoExitRestaurant();
		state = AGENTSTATE.DoingNothing;
	}
	
	private void doGoToRestaurant() {
		customerAnimation.DoGoToEntrance();
	}

	// Getters
	
	@Override
	public RestaurantZhangTable getTable() {
		return myTable;
	}
	
	@Override
	public RestaurantZhangHost getHost() {
		return host;
	}
	
	@Override
	public RestaurantZhangWaiter getWaiter() {
		return myWaiter;
	}
	
	@Override
	public int getWaitingPosition() {
		return waitingPosition;
	}
	
	@Override
	public RestaurantZhangMenu getCustomerMenu() {
		return customerMenu;
	}
	
	@Override
	public String getChoice() {
		return choice;
	}
	
	@Override
	public RestaurantZhangCashier getCashier() {
		return myCashier;
	}
	
	@Override
	public RestaurantZhangCheck getCheck() {
		return myCheck;
	}

	@Override
	public int getMoney() {
		return money;
	}
	
	@Override
	public int getTab() {
		return myTab;
	}
	
	@Override
	public AGENTSTATE getState() {
		return state;
	}

	@Override
	public AGENTEVENT getEvent() {
		return event;
	}
	
	// Setters
	
	@Override
	public void setWaitingPosition(int newPos) {
		waitingPosition = newPos;
	}
	
	@Override
	public void setAnimation(RestaurantZhangAnimatedCustomer g) {
		customerAnimation = g;
	}
	
	@Override
	public void setCashier(RestaurantZhangCashier c) {
		myCashier = c;
	}
	
	@Override
	public void setHost(RestaurantZhangHost host) {
		this.host = host;
	}
	
	@Override
	public void setMoney(int newMoney) {
		money = newMoney;
	}
	
	@Override
	public void setActive() {
		super.setActive();
		customerAnimation.setVisible(true);
		msgGotHungry();
	}
	
	@Override
	public void setInactive() {
		customerAnimation.setVisible(false);
		super.setInactive();
	}
	
	// Utilities
	
	@Override
	public void print(String msg) {
		this.getPerson().printViaRole("RestaurantZhangCustomer", msg);
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTZHANG, "RestaurantZhangCustomerRole " + this.getPerson().getName(), msg);
    }
}

