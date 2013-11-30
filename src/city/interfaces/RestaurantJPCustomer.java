package city.interfaces;

import utilities.RestaurantJPMenuClass;

public interface RestaurantJPCustomer extends RoleInterface {

	// Messages
	
	public void msgHereIsCheck(int check, RestaurantJPCashier cashier);
	public void msgFollowMeToTable(RestaurantJPMenuClass menu, int tableNumber, RestaurantJPWaiter w);
	public void msgWhatWouldYouLike();
	public void msgOutOfChoice(RestaurantJPMenuClass menu);
	public void msgHereIsYourFood();

}