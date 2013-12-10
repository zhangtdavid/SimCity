package city.roles;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import trace.AlertLog;
import trace.AlertTag;
import utilities.RestaurantChoiMenu;
import city.Application.FOOD_ITEMS;
import city.animations.RestaurantChoiCustomerAnimation;
import city.animations.interfaces.RestaurantChoiAnimatedCustomer;
import city.bases.Role;
import city.roles.interfaces.RestaurantChoiCashier;
import city.roles.interfaces.RestaurantChoiCustomer;
import city.roles.interfaces.RestaurantChoiHost;
import city.roles.interfaces.RestaurantChoiWaiter;

public class RestaurantChoiCustomerRole extends Role implements RestaurantChoiCustomer{

	// Data
	
	private int punishment;
	private int hungerLevel = 5; // determines length of meal
	private Timer timer = new Timer();
	private RestaurantChoiAnimatedCustomer customerGui;  
	private boolean currentlyHungry;
	private RestaurantChoiMenu menu;
	private FOOD_ITEMS choice;
	private int line;
	/*
	private int amt;   // something to do with time for dishes punishment
	private int waitingCoordX; why do i need these again? waitingCoordX is for waiting in line.
	private int waitingCoordY;*/
	private int xTableDestination, yTableDestination;
	private ArrayList<FOOD_ITEMS> previousCanAfford = new ArrayList<FOOD_ITEMS>();
	private boolean hasHitZero;
	private boolean consideredLeaving;
	private ArrayList<FOOD_ITEMS> previousChoices = new ArrayList<FOOD_ITEMS>();
	private RestaurantChoiHost host;
	private RestaurantChoiWaiter waiter;
	private RestaurantChoiCashier cashier;
	private EVENT event = EVENT.none;	
	private STATE state = STATE.DoingNothing; // The start state

	// Constructor
	
	public RestaurantChoiCustomerRole(){
		super();
		customerGui = new RestaurantChoiCustomerAnimation(this);
		//TODO consider removing this feature because it doesn't make sense in the context of simcity201
		/*cash = (int)(17*Math.random())+3; // the default customer starts with 3~20 dollars. // this is outdated
		if(this.getPerson().getName().contains("evil")){ // evil person starts with -50 dollars. 
			cash = -10;
		}*/

		//this is because whenever I set a customer hungry, I give him 3~5 more dollars.
	}
	
	// Messages
	
	@Override
	public void msgNotifyFull(int i) {
		event = EVENT.notifiedFull;
		this.line = i;
		stateChanged();
	}
	
	@Override
	public void msgFollowMe(RestaurantChoiWaiter w,	int xd, int yd) {
		this.setWaiter(w);
		xTableDestination = xd; // now we know where we're going
		yTableDestination = yd;
		print("is following the waiter");
		event = EVENT.followWaiter;
		stateChanged();
	}

	@Override
	public void msgHeresYourSeat(RestaurantChoiMenu m) {
		this.menu = m;
		event = EVENT.seated;
		stateChanged();
	}

	@Override
	public void msgWhatWouldYouLike() {
		print("Asked to order");
		event = EVENT.askedForOrder;
		stateChanged();

	}

	@Override
	public void msgHeresYourNewMenu(RestaurantChoiMenu m) {
		state = STATE.BeingSeated;
		event = EVENT.seated; // set states and events so that customer looks at menu
		stateChanged();
	}

	@Override
	public void msgOrderArrived() {
		print("Received food");
		event = EVENT.gotFood;
		stateChanged();

	}

	@Override
	public void msgHeresYourCheck(int checkValue) {
		event = EVENT.gotCheck;
		stateChanged();

	}

	@Override
	public void msgHeresYourChange(int amt) {
		print("Received change of: " + amt);
		this.getPerson().setCash(amt); // no state to change - just do it on the way out
		event = EVENT.gotChange;
	}

	@Override
	public void msgDoTheDishes(int length) {
		event = EVENT.assignedDishes;
		print("Dishes time: " + length);
		punishment = length;
	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = EVENT.seated;
		stateChanged();
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		setFullNow();
		event = EVENT.doneLeaving;
		stateChanged();

	}

	@Override
	public void msgAnimationFinishedGoToCashier() {
		print(state.toString());
		event = EVENT.talkToCashier;
		stateChanged();
	}

	@Override
	public void msgAnimationFinishedGoToDishes() {
		//from animation
		startDishes();
		event = EVENT.startDishes;
		stateChanged();
	}

	//Scheduler
	
	@Override
	public boolean runScheduler() {
		//		CustomerAgent is a finite state machine
		if (state == STATE.DoingNothing && event == EVENT.gotHungry) {
			print("I'm hungry");
			goToRestaurant();
			return true;
		}
		if(state == STATE.WaitingInRestaurant && event == EVENT.notifiedFull && !consideredLeaving){
			ConsiderLeaving();
			return true;
		}
		//this deals if we're in the consideration state, still waiting to decide whether or not to leave
		//while the customer is seated.
		if (state == STATE.WaitingInRestaurant && event == EVENT.followWaiter) {
			state = STATE.BeingSeated;
			goToTable();
			return true;
		}
		if (state == STATE.BeingSeated && event == EVENT.seated) {
			state = STATE.LookingAtMenu; 
			LookAtMenu();
			return true;
		}
		if(state == STATE.ReadyToOrder && event == EVENT.seated){
			waiter.msgReadyToOrder(this); // don't need to update state; just alert waiter
		}
		if (state == STATE.ReadyToOrder && event == EVENT.askedForOrder) {
			giveOrder();
			state = STATE.WaitingForFood; 
			return true;
		}
		if (state == STATE.WaitingForFood && event == EVENT.gotFood) {
			state = STATE.Eating; 
			EatFood();
			return true;
		}
		if (state == STATE.Eating && event == EVENT.doneEating) {
			state = STATE.AskForCheck;
			Checkplz();
			return true;
		}
		if(state == STATE.AskForCheck && event == EVENT.gotCheck){
			state = STATE.Leaving;
			ImLeaving();
			return true;
		}
		if(state == STATE.Leaving && event == EVENT.talkToCashier){
			Pay();
			return true;
		}
		if (state == STATE.Leaving && event == EVENT.doneLeaving) {
			state = STATE.DoingNothing;
			event = EVENT.none;
			return true;
		}
		if (state == STATE.Leaving && event == EVENT.gotChange) {
			gotChange();
			return true;
		}

		if(state == STATE.Leaving && event == EVENT.assignedDishes){
			goToDishes();
		}
		if(state == STATE.GoingToDishes && event == EVENT.startDishes){
			startDishes();
		}
		if(state == STATE.DoingDishes && event == EVENT.doneWithDishes){
			leaveAfterDishes();
		}
		return false;
	}

	// Actions
	
	private void goToRestaurant() {
		print("Going to restaurant");
		host.msgImHungry(this);//send our instance, so he can respond to us
		state = STATE.WaitingInRestaurant;
		stateChanged();
	}

	private void goToTable() {
		print("Being seated. Going to table");
		state = STATE.BeingSeated;
		customerGui.DoGoToSeat(xTableDestination, yTableDestination);
		stateChanged();
	}

	private void goToDishes() {
		state = STATE.GoingToDishes;
		customerGui.DoGoToDishes();
		stateChanged();
	}

	private void LookAtMenu() {
		print("Looking at menu for 2s");
		//looking at the menu takes just 2 seconds! then customer is ready to order.
		timer.schedule(new TimerTask(){
			public void run(){
				print("Done looking at menu");
				state = STATE.ReadyToOrder;
				stateChanged();
			}
		},2000);
	}

	private void giveOrder() {
		int naughtyOrNice = (int)(10*Math.random()); // 6/10 chance of honesty. people are bad
		if(getPerson().getName().contains("evil")){ // grading hack: if name is "evil", he will random guess from anything
			naughtyOrNice = 8; // guaranteed to lie. but this doesn't guarantee invalid order! 
		}
		//grading hack: salad only. if can't order salad, leaves.
		if(getPerson().getName().contains("salad")){
			if(hasHitZero){
				print("has hit zero things available to buy");
				choice = null;
			}else{
				int rand = (int)(Math.floor(Math.random()*4));
				if(rand == 4) rand = 3; //in the absurdly non-probable event this happens...
				switch(rand){
				case 0:
					choice = FOOD_ITEMS.steak;		
					break;
				case 1:
					choice = FOOD_ITEMS.pizza;
					break;
				case 2:
					choice = FOOD_ITEMS.chicken;
					break;
				case 3:
					choice = FOOD_ITEMS.salad;
					break;
				}
				
			}
			if(previousCanAfford.isEmpty()) hasHitZero = true;
			print("Picked order " + choice + " with cash: " + this.getPerson().getCash());
			previousChoices.add(choice); // it's memory of what he ordered previously.
			if(choice == null){
				//leave restaurant
				LeaveNow();
				waiter.msgHeresMyOrder(this, choice);
				customerGui.setOrderIcon(null, true);
			}else{
				waiter.msgHeresMyOrder(this, choice);
				customerGui.setOrderIcon(choice, false);
			}
		}
		else if(naughtyOrNice < 7){ // HONEST CASE
			choice = pickRandom(this.getPerson().getCash(),this.previousCanAfford, hasHitZero);
			if(previousCanAfford.isEmpty()) hasHitZero = true;
			print("Picked order " + choice + " with cash: " + this.getPerson().getCash() + " (this is for ease of grading; only Customer knows his cash)");
			previousChoices.add(choice); // it's memory of what he ordered previously.
			if(choice == null){
				//leave restaurant
				LeaveNow();
				waiter.msgHeresMyOrder(this, choice);
				customerGui.setOrderIcon(null, true);
			}else{
				waiter.msgHeresMyOrder(this, choice);
				customerGui.setOrderIcon(choice, false);
			}
		}
		else if(naughtyOrNice >= 7){ // MAYBE DISHONEST CASE
			choice = int2Food((int)(Math.ceil(4*Math.random()))); // pick any of the four!
			if(choice != null)
				print("Picked order " + choice.name() + " (with cash: " + this.getPerson().getCash()+")");
			else
				print("Picked no order (with cash: " + this.getPerson().getCash()+")");
			waiter.msgHeresMyOrder(this, choice);
			customerGui.setOrderIcon(choice, false);
			//i mean you could pick one of the four things you COULD afford anyways.
			//thus the chance for insufficient funds is actually not necessarily 40%.
		}
		if(choice == null){
			LeaveNow();
			waiter.msgHeresMyOrder(this, choice);
			customerGui.setOrderIcon(null, true);
		}
		stateChanged();
	}

	private void EatFood() {
		print("Eating Food");
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			Object cookie = 1;

			public void run() {
				print("Done eating, cookie=" + cookie);
				event = EVENT.doneEating;
				//-1 sets icon to "". True eliminates "?".
				customerGui.setOrderIcon(null,true);
				stateChanged();
			}
		},
		getHungerLevel()*1000);
	}

	private void Checkplz() {
		print("Check please");
		waiter.msgCheckPlz(this, choice);
	}

	private void ImLeaving() {
		print("Got check, leaving table");
		waiter.msgImDone(this); // tell waiter about departure
		customerGui.DoGoToCashier();
	}

	private void Pay() {
		cashier.msgHeresMyPayment(this, this.getPerson().getCash()); // give cashier all cash, will get change.
	}

	private void LeaveNow() {
		waiter.msgImDone(this);
		print("Can't buy anything, leaving table");
		customerGui.DoExitRestaurant();
		super.setInactive();
	}

	private void gotChange() {
		customerGui.DoExitRestaurant();
		super.setInactive();
	}

	private void startDishes() {
		print("Doing dishes");
		state = STATE.DoingDishes;
		timer.schedule(new TimerTask() {
			public void run() {
				event = EVENT.doneWithDishes;
				stateChanged();
			}
		},
		punishment); 
	}

	private void leaveAfterDishes() {
		print("Leaving now, after doing dishes");
		state = STATE.Leaving;
		cashier.msgDoneWithDishes(this);
		customerGui.DoExitRestaurant();
		super.setInactive();
	}

	private void ConsiderLeaving() {
		//notified restaurant was full. what now?
		if(state == STATE.WaitingInRestaurant && event != EVENT.followWaiter){
			if(Math.random() < .5){ //50% chance of staying
				//and then don't do anything, just stay in line
				state = STATE.WaitingInRestaurant;
				event = EVENT.gotHungry;
				customerGui.DoGoToWaiting(line);
			}else{
				//else leave the restaurant immediately
				//so host has to remove this customer from his list
				print("Leaving now");
				host.msgNotWaiting(this);
				print("Leaving restaurant; don't want to want in line");
				state = STATE.Leaving;
				customerGui.DoExitRestaurant();
				super.setInactive();
			}
			stateChanged();
		}
		consideredLeaving = true;
	}
	
	// Getters

	@Override
	public boolean isHungryNow() {
		return currentlyHungry;
	}

	@Override
	public void gotHungry() {
		setHungryNow();
		//giveCashOnHungry(); // TODO still free money problems in simcity201, remove?
		event = EVENT.gotHungry;
		stateChanged();
		//choice = null; TODO check if this causes the person to leave automatically
		consideredLeaving = false;
	}

	@Override
	public boolean getLocation() {
		if(consideredLeaving){ 
			//consideredLeaving only true if customer waited in line; aka is waiting in waiting zone
			return true; // if return true, customer is waiting in designated location (200,200).
		}
		return false; //if can be seated instantly, he'll be grabbed from (-40, -40).
	}

	@Override
	public FOOD_ITEMS getChoice() {
		return this.choice;
	}


	@Override
	public RestaurantChoiAnimatedCustomer getGui() {
		return customerGui;
	}

	@Override
	public int getHungerLevel() {
		return hungerLevel;
	}

	@Override
	public STATE getState() {
		return state;
	}
	
	@Override
	public String getStateString() {
		return state.toString();
	}
	
	// Setters
	
	@Override
	public void setWaiter(RestaurantChoiWaiter w) {
		waiter = w;
	}

	@Override
	public void setHost(RestaurantChoiHost h) {
		host = h;
	}

	@Override
	public void setCashier(RestaurantChoiCashier c) {
		cashier = c;
	}

	@Override
	public void setHungryNow() {
		//basically reset all the fields that need to be reset right here
		customerGui.resetLocation();
		currentlyHungry = true;
		state = STATE.DoingNothing;
		event = EVENT.none;
		previousCanAfford.clear();
		hasHitZero = false;
		customerGui.setOrderIcon(null,true); // reset order icon
		previousChoices.clear();
		//money hacks obsolete (salad = 4$, etc)
	}

	@Override
	public void setFullNow() {
		currentlyHungry = false;
	}

	@Override
	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
	}

	@Override
	public void setGui(RestaurantChoiAnimatedCustomer anim) {
		customerGui = anim;
	}
	
	@Override
	public void setActive() {
		super.setActive();
		gotHungry();
	}

	// Utilities 
	
	private FOOD_ITEMS int2Food(int index) {
		switch(index){
		case 0:
			return FOOD_ITEMS.steak;
		case 1:
			return FOOD_ITEMS.pizza;
		case 2:
			return FOOD_ITEMS.chicken;
		case 3:
			return FOOD_ITEMS.salad;
		default: 
			return null;
		}
	}
	
	private FOOD_ITEMS pickRandom(int cash, ArrayList<FOOD_ITEMS> mem, boolean hasHitZero) {
		//GOTTA BE AN OPTION THAT HE CAN AFFORD. If he can't afford anything, he has to leave.
		if(mem.isEmpty() && !hasHitZero){ // only do this if mem is empty; i.e. if this is the first time. 
			//this is because it's impossible for someone to order a 2nd time legitimately otherwise
			for(int i = 0; i<=menu.getNumberOfItems()-1; i++){ // 1 thru 4 because our items go from 1 to 4...
				if(cash >= menu.foodPrice.get(int2Food(i))){
					if(menu.isAvailable(int2Food(i)))
						mem.add(int2Food(i)); // add the food ID to list of things we can order
				}
			}
		}
		if(mem.isEmpty()) hasHitZero = true; // first check if it's empty here
		if(mem.size() == 0 && hasHitZero) return null;

		int x = (int)Math.ceil(mem.size()*Math.random());
		if(x == mem.size()) x--; // can't be .size() since it's going to access mem...
		FOOD_ITEMS y = mem.get(x);
		mem.remove(mem.get(x)); // remove from list of things we can order. then we can use it as memory.
		if(mem.isEmpty()) hasHitZero = true; //and check again if it's empty here
		return y; 	
	}
	
	@Override
	public void print(String msg) {
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTCHOI, "RestaurantChoiCustomerRole " + this.getPerson().getName(), msg);
    }

}
