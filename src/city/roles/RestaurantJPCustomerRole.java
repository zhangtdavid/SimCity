package city.roles;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import trace.AlertLog;
import trace.AlertTag;
import utilities.RestaurantJPMenuClass;
import city.animations.RestaurantJPCustomerAnimation;
import city.bases.Role;
import city.buildings.RestaurantJPBuilding;
import city.roles.interfaces.RestaurantJPCashier;
import city.roles.interfaces.RestaurantJPCustomer;
import city.roles.interfaces.RestaurantJPWaiter;

/**
 * Restaurant customer agent.
 */
public class RestaurantJPCustomerRole extends Role implements RestaurantJPCustomer {
	//Data
	
	private int hungerLevel = 5;        // determines length of meal
	int currentTable;
	RestaurantJPBuilding building;
	Timer timer = new Timer();
	private RestaurantJPWaiter waiter;    
	private RestaurantJPCustomerAnimation customerGui;
	private AgentState state = AgentState.DoingNothing;//The start state
	AgentEvent event = AgentEvent.none;
	RestaurantJPMenuClass myMenu = new RestaurantJPMenuClass();
	String myOrder = new String();
	int bill = 0;
	private RestaurantJPCashier cashier;
	private RestaurantJPHostRole host;

	//Constructor
	
	public RestaurantJPCustomerRole() {
		super();
		myMenu = new RestaurantJPMenuClass();
		// TODO Auto-generated constructor stub
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
	
	public void msgHereIsCheck(int check, RestaurantJPCashier csh){
		//Do("Check received");
		bill = check;
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

//-------------------------------------------------------------------------------------------Actions

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
			if(this.getPerson().getCash() < myMenu.Prices.get(food))
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
		else if(myMenu.find(this.getPerson().getName()))
			myOrder = this.getPerson().getName();
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
		this.getPerson().setCash(this.getPerson().getCash() - bill);
		if(this.getPerson().getCash() > 0)
			cashier.msgPayment(this, bill);
		else
			cashier.msgFlaking(this, bill);
		event = AgentEvent.donePaying;
	}
	
	private void LeaveTable() {
		waiter.msgDoneEatingAndLeaving(this);
		customerGui.DoExitRestaurant();
		super.setInactive();
	}
	
	private void DecideToLeave(){
		int leave = (int) (Math.random() * 2);
		if(leave == 1){
			host.msgLeaving(this);
			customerGui.DoExitRestaurant();
			state = AgentState.DoingNothing;
			event = AgentEvent.none;
			super.setInactive();
		}
		else{
			state = AgentState.WaitingInRestaurant;
			event = AgentEvent.DecidedToStay;
		}
	}

	
	
	
	//Getters
	
	public String getName() {
		return this.getPerson().getName();
	}
	
	public int getHungerLevel() {
		return hungerLevel;
	}
	
	public RestaurantJPCustomerAnimation getGui() {
		return customerGui;
	}
	
	//Setters
	
	public void setActive(){
		super.setActive();
		customerGui.DoEnterRestaurant();
		gotHungry();
	}
	
	public void setBuilding(RestaurantJPBuilding b){
		building = b;
	}
	
    public void setWaiter(RestaurantJPWaiter w)
    {
    	waiter = w;
    }
    
    public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
	}
	
    public void setGui(RestaurantJPCustomerAnimation g) {
		customerGui = g;
	}
    
    public void setAnimation(RestaurantJPCustomerAnimation gui) {
		customerGui = gui;
		
	}
    
    public void setCashier(RestaurantJPCashier csh) {
		// TODO Auto-generated method stub
		cashier = csh;
	}
    
	//Utilities
	
	public String toString() {
		return "customer " + getName();
	}

	@Override
	public void print(String msg) {
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTJP, "RestaurantJPCustomerRole " + this.getPerson().getName(), msg);
    }


	public void setHost(RestaurantJPHostRole host) {
		this.host = host;
		// TODO Auto-generated method stub
		
	}
}

