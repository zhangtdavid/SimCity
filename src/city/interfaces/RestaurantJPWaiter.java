package city.interfaces;

import utilities.RestaurantJPTableClass;
import city.roles.RestaurantJPCashierRole;

public interface RestaurantJPWaiter extends RoleInterface {
	/**
	 * @param total The cost according to the cashier
	 *
	 * Sent by the cashier giving the waiter a check to pass on to the cashier.
	 */
	public abstract void msgHereIsCheck(Float check, RestaurantJPCashierRole csh, RestaurantJPCustomer c);

	public abstract void msgImReadyToOrder(RestaurantJPCustomer customer);

	public abstract void msgDoneEatingAndLeaving(RestaurantJPCustomer customer);

	public abstract void msgHereIsMyChoice(String myOrder, RestaurantJPCustomer customer);

	public abstract void msgOutOf(String choice, RestaurantJPTableClass table);

	public abstract void msgOrderIsReady(String choice,
			RestaurantJPTableClass table);	
}