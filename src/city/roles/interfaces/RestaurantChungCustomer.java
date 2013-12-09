package city.roles.interfaces;

import utilities.RestaurantChungMenu;
import city.Application.FOOD_ITEMS;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.RestaurantChung;

public interface RestaurantChungCustomer extends RoleInterface {
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, DecidedToStay, BeingSeated, DecideLeaving, Deciding, CallingWaiter, WaitingForFood, Eating, WaitingForCheck, GoingToCashier, Paying, WaitingForChange, Leaving};
	public enum AgentEvent
	{none, gotHungry, followWaiter, noTables, decidedToLeave, seated, readyToOrder, askedForOrder, receivedFood, doneEating, receivedCheck, atCashier, receivedChange, doneLeaving, kickedOut};
	
	// Messages
	public void gotHungry();
	public void msgNoTablesAvailable();
	public void msgSelfDecidedToLeave();
	public void msgFollowMeToTable(RestaurantChungWaiter w, RestaurantChungMenu menu);
	public void msgAnimationAtSeat();
	public void msgSelfReadyToOrder();
	public void msgWhatWouldYouLike();
	public void msgOutOfItem(FOOD_ITEMS food_ITEMS, RestaurantChungMenu menu);
	public void msgHereIsYourFood();
	public void msgSelfDoneEating();
	public void msgHereIsCheck(int price);
	public void msgAnimationAtCashier();
	public void msgHereIsChange(int change);
	public void msgAnimationFinishedLeaveRestaurant();
	public void msgKickingYouOutAfterPaying(int debt);
	
	// Getters
	public int getHungerLevel();
	public String getState();
	public FOOD_ITEMS getOrder();

	// Setters
	public void setHungerLevel(int hungerLevel);
	public void setRestaurant(RestaurantChung r);
}