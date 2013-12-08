package city.roles.interfaces;

import city.Application.FOOD_ITEMS;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.RestaurantChung.MyCustomer;

public interface RestaurantChungWaiter extends RoleInterface {
	public enum BreakState {Working, WantBreak, AskedForBreak, ApprovedForBreak, RejectedForBreak, OnBreak, ReturningToWork};
	public enum WorkingState {Working, GoingOffShift, NotWorking};
	
	// Messages
	public abstract void msgAnimationAskedForBreak();
	public abstract void msgApprovedForBreak();
	public abstract void msgRejectedForBreak();
	public abstract void msgAnimationBreakOver();
	public abstract void msgSitAtTable(RestaurantChungCustomer c, int table);
	public abstract void msgReadyToOrder(RestaurantChungCustomer c);
	public abstract void msgHereIsMyOrder(RestaurantChungCustomer c, FOOD_ITEMS choice);
	public abstract void msgOutOfItem(FOOD_ITEMS choice, int table);
	public abstract void msgOrderIsReady(FOOD_ITEMS choice, int table);
	public abstract void msgGetCheck(RestaurantChungCustomer c);
	public abstract void msgLeaving(RestaurantChungCustomer c);
	public abstract void msgHereIsBill(RestaurantChungCustomer c, int price);
	public abstract void msgAnimationAtEntrance();
	public abstract void msgAnimationAtTable();
	public abstract void msgAnimationAtCook();
	public abstract void msgAnimationAtCashier();
	public abstract void msgAnimationAtWaiterHome();
	public abstract void msgAnimationAtLine();

	
	// Getters
	WorkingState getWorkingState();
// Only applies to one waiter type
//	public RestaurantChungRevolvingStand getRevolvingStand();
//	public void setRevolvingStand(RestaurantChungRevolvingStand stand);
	
	// Setters

	// Utilities
	public void removeCustomerFromList(MyCustomer c);
}