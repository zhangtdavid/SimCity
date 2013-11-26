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
	public abstract void msgWaiterAvailable(RestaurantChungWaiter w);
	public abstract void msgIWantToGoOnBreak(RestaurantChungWaiter w);
	public abstract void msgIAmReturningToWork(RestaurantChungWaiter w);
	public abstract void msgTableIsFree(RestaurantChungWaiter w, int t, RestaurantChungCustomer c);
	public abstract void msgFlakeAlert(RestaurantChungCustomer c, int d);
	public abstract void msgNewWaiter(RestaurantChungWaiter waiter);
	public abstract void msgRemoveWaiter(RestaurantChungWaiter waiter);
}