package city.roles.interfaces;

import java.util.Collection;
import java.util.List;

import city.bases.interfaces.RoleInterface;
import city.roles.RestaurantChungHostRole.HCustomer;
import city.roles.RestaurantChungHostRole.MyWaiter;
import city.roles.RestaurantChungHostRole.Table;

public interface RestaurantChungHost extends RoleInterface {
	public enum WorkingState {Working, GoingOffShift, NotWorking};
	public enum WaiterState {Working, WantBreak, Rejected, OnBreak};
	public enum CustomerState {InRestaurant, WaitingInLine, WaitingToBeSeated, GettingSeated, DecidingToLeave, Seated, Done};
	
	// Messages
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

	// Getters
	List<HCustomer> getCustomers();
	Collection<Table> getTables();
	int getNumTables();
		
	// Utilities
	void addTable();
	Table findTable(int t);
	HCustomer findCustomer(RestaurantChungCustomer ca);
	MyWaiter findWaiter(RestaurantChungWaiter w);
		

	
}