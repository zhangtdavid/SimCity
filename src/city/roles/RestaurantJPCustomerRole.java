package city.roles;


import utilities.RestaurantJPMenuClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import city.Role;
import city.animations.RestaurantJPCustomerAnimation;
import city.interfaces.RestaurantJPCustomer;
import city.interfaces.RestaurantJPWaiter;

/**
 * Restaurant customer agent.
 */
public class RestaurantJPCustomerRole extends Role implements RestaurantJPCustomer {
	private String name = new String();
	private int hungerLevel = 5;        // determines length of meal
	int currentTable;
	Timer timer = new Timer();
	private RestaurantJPWaiter waiter;    
	private RestaurantJPCustomerAnimation customerGui;
	private RestaurantJPHostRole host;
	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, ReadyToOrder, Choosing, PlacingOrder, Eating, DoneEating, Paying, Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state
	public enum AgentEvent 
	{none, gotHungry, restaurantFull, DecidedToStay, followHost, seated, choiceReady, waiterCalled, outOfFood, foodArrived, readyToPay, donePaying, doneLeaving};
	AgentEvent event = AgentEvent.none;
	RestaurantJPMenuClass myMenu = new RestaurantJPMenuClass();
	String myOrder = new String();
	
	Float cash = (float) 20;
	Float bill = (float) 0;
	
	RestaurantJPCashierRole cashier;
	
	public RestaurantJPCustomerRole(String n){
		super();
		name = n;
		if(name.equals("Flake"))
			cash = (float) 0;
		myMenu = new RestaurantJPMenuClass();
	}

	public RestaurantJPCustomerRole(RestaurantJPHostRole h, RestaurantJPCashierRole c) {
		host = h;
		cashier = c;
		// TODO Auto-generated constructor stub
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(RestaurantJPHostRole h) {
		host = h;
	}
    public void setWaiter(RestaurantJPWaiter w)
    {
    	waiter = w;
    }
	public String getCustomerName() {
		return name;
	}
	
//------------------------------------------------------------------------ Messages

	public void gotHungry() {//from animation
		print("I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}

	public void msgFollowMeToTable(RestaurantJPMenuClass menu, int tableNumber, RestaurantJPWaiter waiter) {
		//Do("SitAtTable message recieved from " + waiter);
		currentTable = tableNumber;
		setWaiter(waiter);
		myMenu = menu;
		event = AgentEvent.followHost;
		stateChanged();
	}
	
	public void msgGotOrder(String order){
		//Do("GotOrder message received from User");
		myOrder = order;
		event = AgentEvent.choiceReady;
		stateChanged();
	}
	
	public void msgWhatWouldYouLike(){
		//Do("WhatWouldYouLike message recieved from " + waiter.toString());
		event = AgentEvent.waiterCalled;
		stateChanged();
	}
	
	public void msgOutOfChoice(RestaurantJPMenuClass menu){
		//Do("Out of choice message received from " + waiter.toString());
		myMenu = menu;
		event = AgentEvent.outOfFood;
		stateChanged();
	}
	
	public void msgHereIsYourFood(){
		//Do("HereIsFood message received from " + waiter.toString());
		event = AgentEvent.foodArrived;
		stateChanged();
	}
	
	public void msgHereIsCheck(Float check, RestaurantJPCashierRole csh){
		//Do("Check received");
		bill = new Float(check);
		cashier = csh;
		event = AgentEvent.readyToPay;
		stateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}
	
	public void msgRestaurantFull(){
		//Do("Received Restaraunt Full message");
		event = AgentEvent.restaurantFull;
		stateChanged();
	}

//--------------------------------------------------------------------------------------------------SCHEDULER
	
	public boolean runScheduler() {
		//	CustomerAgent is a finite state machine
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.restaurantFull){
			DecideToLeave();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followHost ){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.ReadyToOrder;
			CallWaiter();
			return true;
		}

		if (state == AgentState.ReadyToOrder && event == AgentEvent.waiterCalled){
			state = AgentState.Choosing;
			DecideOrder();
			return true;
		}
		if (state == AgentState.Choosing && event == AgentEvent.choiceReady){
			state = AgentState.PlacingOrder;
			PlaceOrder();
			return true;
		}
		if (state == AgentState.PlacingOrder && event == AgentEvent.outOfFood){
			state = AgentState.Choosing;
			DecideOrder();
			return true;
		}
		if (state == AgentState.PlacingOrder && event == AgentEvent.foodArrived){
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		if (state == AgentState.DoneEating && event == AgentEvent.readyToPay){
			state = AgentState.Paying;
			Pay();
			return true;
		}
		if (state == AgentState.Paying && event == AgentEvent.donePaying){
			state = AgentState.Leaving;
			LeaveTable();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			return true;
		}
		return false;
	}

//-----------------------------------------------------------------------------------------------------------Actions

	public void goToRestaurant() {
		host.msgIWantToEat(this);//send our instance, so he can respond to us
	}

	private void SitDown() {
		customerGui.DoGoToSeat(currentTable);   //hack; only one table
	}
	
	private void CallWaiter()
	{
		waiter.msgImReadyToOrder(this);
	}

	private void DecideOrder(){
		//Do("inside decide order");
		List<String> cannotAfford = new ArrayList<String>();
		for(String food : myMenu.foods){
			if(cash < myMenu.Prices.get(food) && !name.equals("Flake"))
				cannotAfford.add(food);
		}
		for(String food : cannotAfford){
			myMenu.remove(food);
		}
		if(myMenu.foods.isEmpty()){
			waiter.msgDoneEatingAndLeaving(this);
			customerGui.DoExitRestaurant();
			//state = state.DoingNothing;
			return;
		}
		else if(myMenu.find(name))
			myOrder = name;
		else
			myOrder = myMenu.randomSelect();
		this.msgGotOrder(myOrder); 			
	}
	
	private void PlaceOrder(){
		waiter.msgHereIsMyChoice(myOrder, this);
		customerGui.DoPlaceOrder(myOrder);
	}
	
	private void EatFood() {
		customerGui.DoEatFood();
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				print("Done eating, cookie=" + cookie);
				state = AgentState.DoneEating;
				//isHungry = false;
				stateChanged();
			}
		},
		5000);//getHungerLevel() * 1000);//how long to wait before running task
	}

	private void Pay(){
		cash -= bill;
		if(cash > 0)
			cashier.msgPayment(this, bill);
		else
			cashier.msgFlaking(this, bill);
		if(name.equals("Flake"))
			cash = (float) 50;
		event = AgentEvent.donePaying;
	}
	
	private void LeaveTable() {
		waiter.msgDoneEatingAndLeaving(this);
		customerGui.DoExitRestaurant();
	}
	
	private void DecideToLeave(){
		int leave = (int) (Math.random() * 2);
		if(leave == 1){
			host.msgLeaving(this);
			customerGui.DoExitRestaurant();
			state = AgentState.DoingNothing;
			event = AgentEvent.none;
		}
		else{
			state = AgentState.WaitingInRestaurant;
			event = AgentEvent.DecidedToStay;
		}
	}

	// Accessors, etc.

	public String getName() {
		return name;
	}
	
	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(RestaurantJPCustomerAnimation g) {
		customerGui = g;
	}

	public RestaurantJPCustomerAnimation getGui() {
		return customerGui;
	}

	public void setAnimation(RestaurantJPCustomerAnimation gui) {
		customerGui = gui;
		
	}

}
