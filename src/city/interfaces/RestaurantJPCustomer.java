package city.interfaces;

import city.roles.RestaurantJPCashierRole;

public interface RestaurantJPCustomer extends RoleInterface {
	/**
	 * @param total The cost according to the cashier
	 *
	 * Sent by the waiter prompting the customer to give cashier check after the customer is done eating.
	 */
	public abstract void msgHereIsCheck(Float check, RestaurantJPCashierRole csh);

}