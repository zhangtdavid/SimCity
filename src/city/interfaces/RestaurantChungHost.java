package city.interfaces;

import restaurant.WaiterAgentBase;

/**
 * A Waiter interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface RestaurantChungHost {
	public abstract void msgIWantToEat(RestaurantChungCustomer c);
	public abstract void msgDecidedToStay(RestaurantChungCustomer c);
	public abstract void msgLeaving(RestaurantChungCustomer c);
	public abstract void msgTakingCustomerToTable(RestaurantChungCustomer c);
	public abstract void msgWaiterAvailable(WaiterAgentBase w);
	public abstract void msgIWantToGoOnBreak(WaiterAgentBase w);
	public abstract void msgIAmReturningToWork(WaiterAgentBase w);
	public abstract void msgTableIsFree(WaiterAgentBase w, int t, RestaurantChungCustomer c);
	public abstract void msgFlakeAlert(RestaurantChungCustomer c, double d);
}