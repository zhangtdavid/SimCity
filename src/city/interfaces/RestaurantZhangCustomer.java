package city.interfaces;

import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangTable;

public interface RestaurantZhangCustomer extends RoleInterface {
	
	int waitingPosition = 0;
	// Sent by waiter after waiter gets check from cashier
	public abstract void msgHereIsCustCheck(RestaurantZhangCheck c);
	// Sent by cashier if customer successfully pays for his bill
	public abstract void msgHereIsChange(int change);
	// Sent by cashier if customer still owes money to restaurant
	public abstract void msgPayLater(int tab);
	// Gets name
	public abstract String getName();
	public abstract void msgRestaurantFull();
	public abstract void msgHereIsYourWaitingPosition(int pos);
	public abstract void msgFollowMe(RestaurantZhangWaiter w, RestaurantZhangMenu waiterMenu, RestaurantZhangTable table);
	public abstract void msgWhatWouldYouLike();
	public abstract void msgOrderAgain();
	public abstract void msgHereIsYourFood(String choice);
	public abstract int getPos();
	public abstract void msgAnimationFinishedEnterRestaurant();
	public abstract void msgAnimationFinishedGoToSeat();
	public abstract void msgAnimationFinishedLeaveRestaurant();
	public abstract void gotHungry();
	public abstract void msgRestaurantClosed();
}