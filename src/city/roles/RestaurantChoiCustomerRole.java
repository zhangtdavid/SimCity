package city.roles;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import trace.AlertLog;
import trace.AlertTag;
import utilities.RestaurantChoiMenu;
import city.Application.FOOD_ITEMS;
import city.Role;
import city.animations.RestaurantChoiCustomerAnimation;
import city.animations.interfaces.RestaurantChoiAnimatedCustomer;
import city.interfaces.RestaurantChoiCashier;
import city.interfaces.RestaurantChoiCustomer;
import city.interfaces.RestaurantChoiHost;
import city.interfaces.RestaurantChoiWaiter;

public class RestaurantChoiCustomerRole extends Role implements RestaurantChoiCustomer{

	//Data
	int amt;  
	int punishment;
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	private RestaurantChoiAnimatedCustomer customerGui;  
	private boolean currentlyHungry;
	RestaurantChoiMenu menu;
	FOOD_ITEMS choice;
	int line;
	int waitingCoordX;
	int waitingCoordY;
	private int xTableDestination, yTableDestination;
	ArrayList<FOOD_ITEMS> previousCanAfford = new ArrayList<FOOD_ITEMS>();
	boolean hasHitZero;
	boolean consideredLeaving;
	ArrayList<FOOD_ITEMS> previousChoices = new ArrayList<FOOD_ITEMS>();

	// agent correspondents
	private RestaurantChoiHost host;
	private RestaurantChoiWaiter waiter;
	private RestaurantChoiCashier cashier;
	AgentEvent event = AgentEvent.none;	
	AgentState state = AgentState.DoingNothing; //The start state

	//Constructor
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
	public void setActive(){
		super.setActive();
		gotHungry();
	}
	//Messages
	@Override
	public void msgNotifyFull(int i) {
		event = AgentEvent.notifiedFull;
		this.line = i;
		stateChanged();
	}
	@Override
	public void msgFollowMe(RestaurantChoiWaiter w,	int xd, int yd) {
		this.setWaiter(w);
		xTableDestination = xd; // now we know where we're going
		yTableDestination = yd;
		print("is following the waiter");
		event = AgentEvent.followWaiter;
		stateChanged();
	}

	@Override
	public void msgHeresYourSeat(RestaurantChoiMenu m) {
		this.menu = m;
		event = AgentEvent.seated;
		stateChanged();
	}

	@Override
	public void msgWhatWouldYouLike() {
		print("Asked to order");
		event = AgentEvent.askedForOrder;
		stateChanged();

	}

	@Override
	public void msgHeresYourNewMenu(RestaurantChoiMenu m) {
		state = AgentState.BeingSeated;
		event = AgentEvent.seated; // set states and events so that customer looks at menu
		stateChanged();
	}

	@Override
	public void msgOrderArrived() {
		print("Received food");
		event = AgentEvent.gotFood;
		stateChanged();

	}

	@Override
	public void msgHeresYourCheck(int checkValue) {
		event = AgentEvent.gotCheck;
		stateChanged();

	}

	@Override
	public void msgHeresYourChange(int amt) {
		print("Received change");
		this.getPerson().setCash(amt); // no state to change - just do it on the way out
		event = AgentEvent.gotChange;
	}

	@Override
	public void msgDoTheDishes(int length) {
		event = AgentEvent.assignedDishes;
		System.out.println("Dishes time: " + length);
		punishment = length;
	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		setFullNow();
		event = AgentEvent.doneLeaving;
		stateChanged();

	}

	@Override
	public void msgAnimationFinishedGoToCashier() {
		System.out.println(state);
		event = AgentEvent.talkToCashier;
		stateChanged();
	}

	@Override
	public void msgAnimationFinishedGoToDishes() {
		//from animation
		startDishes();
		event = AgentEvent.startDishes;
		stateChanged();
	}

	//Scheduler
	@Override
	public boolean runScheduler() {
		//		CustomerAgent is a finite state machine
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry) {
			print("I'm hungry");
			goToRestaurant();
			return true;
		}
		if(state == AgentState.WaitingInRestaurant && event == AgentEvent.notifiedFull && !consideredLeaving){
			ConsiderLeaving();
			return true;
		}
		//this deals if we're in the consideration state, still waiting to decide whether or not to leave
		//while the customer is seated.
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followWaiter) {
			state = AgentState.BeingSeated;
			goToTable();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated) {
			state = AgentState.LookingAtMenu; 
			LookAtMenu();
			return true;
		}
		if(state == AgentState.ReadyToOrder && event == AgentEvent.seated){
			waiter.msgReadyToOrder(this); // don't need to update state; just alert waiter
		}
		if (state == AgentState.ReadyToOrder && event == AgentEvent.askedForOrder) {
			giveOrder();
			state = AgentState.WaitingForFood; 
			return true;
		}
		if (state == AgentState.WaitingForFood && event == AgentEvent.gotFood) {
			state = AgentState.Eating; 
			EatFood();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating) {
			state = AgentState.AskForCheck;
			Checkplz();
			return true;
		}
		if(state == AgentState.AskForCheck && event == AgentEvent.gotCheck){
			state = AgentState.Leaving;
			ImLeaving();
			return true;
		}
		if(state == AgentState.Leaving && event == AgentEvent.talkToCashier){
			Pay();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving) {
			state = AgentState.DoingNothing;
			event = AgentEvent.none;
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.gotChange) {
			gotChange();
			return true;
		}

		if(state == AgentState.Leaving && event == AgentEvent.assignedDishes){
			goToDishes();
		}
		if(state == AgentState.GoingToDishes && event == AgentEvent.startDishes){
			startDishes();
		}
		if(state == AgentState.DoingDishes && event == AgentEvent.doneWithDishes){
			leaveAfterDishes();
		}
		return false;
	}


	//Actions
	@Override
	public void goToRestaurant() {
		System.out.println("Going to restaurant");
		host.msgImHungry(this);//send our instance, so he can respond to us
		state = AgentState.WaitingInRestaurant;
		stateChanged();
	}

	@Override
	public void goToTable() {
		System.out.println("Being seated. Going to table");
		state = AgentState.BeingSeated;
		customerGui.DoGoToSeat(xTableDestination, yTableDestination);
		stateChanged();
	}

	@Override
	public void goToDishes() {
		state = AgentState.GoingToDishes;
		customerGui.DoGoToDishes();
		stateChanged();
	}

	@Override
	public void LookAtMenu() {
		System.out.println("Looking at menu for 2s");
		//looking at the menu takes just 2 seconds! then customer is ready to order.
		timer.schedule(new TimerTask(){
			public void run(){
				System.out.println("Done looking at menu");
				state = AgentState.ReadyToOrder;
				stateChanged();
			}
		},2000);
	}

	@Override
	public void giveOrder() {
		int naughtyOrNice = (int)(10*Math.random()); // 6/10 chance of honesty. people are bad
		if(getPerson().getName().contains("evil")){ // grading hack: if name is "evil", he will random guess from anything
			naughtyOrNice = 8; // guaranteed to lie. but this doesn't guarantee invalid order! 
		}
		//grading hack: salad only. if can't order salad, leaves.
		if(getPerson().getName().contains("salad")){
			if(hasHitZero){
				System.out.println("has hit zero things available to buy");
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
			System.out.println("Picked order " + choice + " with cash: " + this.getPerson().getCash());
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
			System.out.println("Picked order " + choice + " with cash: " + this.getPerson().getCash() + " (this is for ease of grading; only Customer knows his cash)");
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
				System.out.println("Picked order " + choice.name() + " (with cash: " + this.getPerson().getCash()+")");
			else
				System.out.println("Picked no order (with cash: " + this.getPerson().getCash()+")");
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

	@Override
	public FOOD_ITEMS pickRandom(int cash, ArrayList<FOOD_ITEMS> mem,
			boolean hasHitZero) {
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
	public void EatFood() {
		System.out.println("Eating Food");
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
				event = AgentEvent.doneEating;
				//-1 sets icon to "". True eliminates "?".
				customerGui.setOrderIcon(null,true);
				stateChanged();
			}
		},
		getHungerLevel()*1000);
	}

	@Override
	public void Checkplz() {
		System.out.println("Check please");
		waiter.msgCheckPlz(this, choice);
	}

	@Override
	public void ImLeaving() {
		System.out.println("Got check, leaving table");
		waiter.msgImDone(this); // tell waiter about departure
		customerGui.DoGoToCashier();
	}

	@Override
	public void Pay() {
		cashier.msgHeresMyPayment(this, this.getPerson().getCash()); // give cashier all cash, will get change.
	}

	@Override
	public void LeaveNow() {
		waiter.msgImDone(this);
		System.out.println("Can't buy anything, leaving table");
		customerGui.DoExitRestaurant();
	}

	@Override
	public void gotChange() {
		customerGui.DoExitRestaurant();
	}

	@Override
	public void startDishes() {
		System.out.println("Doing dishes");
		state = AgentState.DoingDishes;
		timer.schedule(new TimerTask() {
			public void run() {
				event = AgentEvent.doneWithDishes;
				stateChanged();
			}
		},
		punishment); 
	}

	@Override
	public void leaveAfterDishes() {
		System.out.println("Leaving now, after doing dishes");
		state = AgentState.Leaving;
		cashier.msgDoneWithDishes(this);
		customerGui.DoExitRestaurant(); 
	}

	@Override
	public void ConsiderLeaving() {
		//notified restaurant was full. what now?
		if(state == AgentState.WaitingInRestaurant && event != AgentEvent.followWaiter){
			if(Math.random() < .5){ //50% chance of staying
				//and then don't do anything, just stay in line
				state = AgentState.WaitingInRestaurant;
				event = AgentEvent.gotHungry;
				customerGui.DoGoToWaiting(line);
			}else{
				//else leave the restaurant immediately
				//so host has to remove this customer from his list
				System.out.println("Leaving now");
				host.msgNotWaiting(this);
				System.out.println("Leaving restaurant; don't want to want in line");
				state = AgentState.Leaving;
				customerGui.DoExitRestaurant();
			}
			stateChanged();
		}
		consideredLeaving = true;
	}
	//Getters

	@Override
	public boolean isHungryNow() {
		return currentlyHungry;
	}

	@Override
	public void gotHungry() {
		setHungryNow();
		//giveCashOnHungry(); // TODO still free money problems in simcity201, remove?
		event = AgentEvent.gotHungry;
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
	public AgentState getState() {
		return state;
	}
	//Setters
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
		state = AgentState.DoingNothing;
		event = AgentEvent.none;
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


	//Utilities
	public FOOD_ITEMS int2Food(int index){
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
	
	@Override
	public void print(String msg) {
        super.print(msg);
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTCHOI, "RestaurantChoiCustomerRole " + this.getPerson().getName(), msg);
    }

}
