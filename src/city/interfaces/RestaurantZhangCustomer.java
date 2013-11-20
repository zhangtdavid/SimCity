package city.interfaces;

import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangTable;
import city.Application;
import city.animations.interfaces.RestaurantZhangAnimatedCustomer;

public interface RestaurantZhangCustomer extends RoleInterface {
	
	int waitingPosition = 0;
	// Sent by waiter after waiter gets check from cashier
	public abstract void msgHereIsCustCheck(RestaurantZhangCheck c);
	// Sent by cashier if customer successfully pays for his bill
	public abstract void msgHereIsChange(double change);
	// Sent by cashier if customer still owes money to restaurant
	public abstract void msgPayLater(double tab);
	// Gets name
	public abstract String getName();
	public abstract void msgRestaurantFull();
	public abstract void msgHereIsYourWaitingPosition(int pos);
	public abstract void msgFollowMe(RestaurantZhangWaiter w, RestaurantZhangMenu waiterMenu, RestaurantZhangTable table);
	public abstract void msgWhatWouldYouLike();
	public abstract void msgOrderAgain();
	public abstract void msgHereIsYourFood(String choice);
	public abstract int getPos();
}