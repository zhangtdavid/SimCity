package city.interfaces;

/**
 * A Waiter interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface RestaurantChungHost extends RoleInterface {
	public abstract void msgIWantToEat(RestaurantChungCustomer c);
	public abstract void msgDecidedToStay(RestaurantChungCustomer c);
	public abstract void msgLeaving(RestaurantChungCustomer c);
	public abstract void msgTakingCustomerToTable(RestaurantChungCustomer c);
	public abstract void msgWaiterAvailable(RestaurantChungWaiterBase w);
	public abstract void msgIWantToGoOnBreak(RestaurantChungWaiterBase w);
	public abstract void msgIAmReturningToWork(RestaurantChungWaiterBase w);
	public abstract void msgTableIsFree(RestaurantChungWaiterBase w, int t, RestaurantChungCustomer c);
	public abstract void msgFlakeAlert(RestaurantChungCustomer c, int d);
}