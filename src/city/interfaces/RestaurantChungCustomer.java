package city.interfaces;

import city.RoleInterface;
import utilities.RestaurantChungMenu;

public interface RestaurantChungCustomer extends RoleInterface {
	
	// Messages

	public void gotHungry();
	public void msgGetInLinePosition(int positionInLine);
	public void msgNoTablesAvailable();
	public void msgSelfDecidedToLeave();
	public void msgFollowMeToTable(RestaurantChungWaiter w, RestaurantChungMenu menu);
	public void msgAnimationAtSeat();
	public void msgSelfReadyToOrder();
	public void msgWhatWouldYouLike();
	public void msgOutOfItem(String choice, RestaurantChungMenu menu);
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
	public String getOrder();

}