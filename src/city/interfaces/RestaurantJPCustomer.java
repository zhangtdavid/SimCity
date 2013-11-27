package city.interfaces;

import utilities.RestaurantJPMenuClass;
import city.roles.RestaurantJPCashierRole;

public interface RestaurantJPCustomer extends RoleInterface {
	/**
	 * @param total The cost according to the cashier
	 *
	 * Sent by the waiter prompting the customer to give cashier check after the customer is done eating.
	 */
	public abstract void msgHereIsCheck(int check, RestaurantJPCashier cashier);

	public abstract void msgFollowMeToTable(RestaurantJPMenuClass menu,
			int tableNumber, RestaurantJPWaiter restaurantJPWaiterBase);

	public abstract void msgWhatWouldYouLike();

	public abstract void msgOutOfChoice(RestaurantJPMenuClass menu);

	public abstract void msgHereIsYourFood();

}