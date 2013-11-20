package city.interfaces;

import city.roles.RestaurantJPCashierRole;


/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface RestaurantJPCustomer {
	/**
	 * @param total The cost according to the cashier
	 *
	 * Sent by the waiter prompting the customer to give cashier check after the customer is done eating.
	 */
	public abstract void msgHereIsCheck(Float check, RestaurantJPCashierRole csh);

}