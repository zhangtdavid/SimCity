package city.roles.interfaces;

import city.bases.interfaces.RoleInterface;

public interface RestaurantChungHost extends RoleInterface {
	public enum WorkingState {Working, GoingOffShift, NotWorking};
	public enum WaiterState {Working, WantBreak, Rejected, OnBreak};
	
	// Messages
	public abstract void msgIWantToEat(RestaurantChungCustomer c);
	public abstract void msgDecidedToStay(RestaurantChungCustomer c);
	public abstract void msgLeaving(RestaurantChungCustomer c);
	public abstract void msgTakingCustomerToTable(RestaurantChungCustomer c);
	public abstract void msgIWantToGoOnBreak(RestaurantChungWaiter w);
	public abstract void msgIAmReturningToWork(RestaurantChungWaiter w);
	public abstract void msgTableIsFree(RestaurantChungWaiter w, int t, RestaurantChungCustomer c);
	public abstract void msgFlakeAlert(RestaurantChungCustomer c, int d);	
}