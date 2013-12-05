package city.roles.interfaces;

import utilities.RestaurantJPMenuClass;
import city.bases.interfaces.RoleInterface;

public interface RestaurantJPCustomer extends RoleInterface {
	//Data
	
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, ReadyToOrder, Choosing, PlacingOrder, Eating, DoneEating, Paying, Leaving};
	
	public enum AgentEvent 
	{none, gotHungry, restaurantFull, DecidedToStay, followHost, seated, choiceReady, waiterCalled, outOfFood, foodArrived, readyToPay, donePaying, doneLeaving};
	// Messages
	
	public void msgHereIsCheck(int check, RestaurantJPCashier cashier);
	public void msgFollowMeToTable(RestaurantJPMenuClass menu, int tableNumber, RestaurantJPWaiter w);
	public void msgWhatWouldYouLike();
	public void msgOutOfChoice(RestaurantJPMenuClass menu);
	public void msgHereIsYourFood();

}