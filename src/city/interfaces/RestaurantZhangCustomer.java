package city.interfaces;

import city.RoleInterface;
import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangTable;

public interface RestaurantZhangCustomer extends RoleInterface {
	
	// Data
	
	public static final int WAITING_POSITION = 0;
	
	// Messages
	
	public void msgHereIsCustCheck(RestaurantZhangCheck c);
	public void msgHereIsChange(int change);
	public void msgPayLater(int tab);
	public void msgRestaurantFull();
	public void msgHereIsYourWaitingPosition(int pos);
	public void msgFollowMe(RestaurantZhangWaiter w, RestaurantZhangMenu waiterMenu, RestaurantZhangTable table);
	public void msgWhatWouldYouLike();
	public void msgOrderAgain();
	public void msgHereIsYourFood(String choice);
	public void msgAnimationFinishedEnterRestaurant();
	public void msgAnimationFinishedGoToSeat();
	public void msgAnimationFinishedLeaveRestaurant();
	public void gotHungry();
	public void msgRestaurantClosed();
	
	// Getters
	
	public int getPos();
	
}