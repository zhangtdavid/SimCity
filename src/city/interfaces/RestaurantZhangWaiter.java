package city.interfaces;

import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangTable;

public interface RestaurantZhangWaiter extends RoleInterface {
	// Sent by cashier to give check to waiter, to be given to customer
	public abstract void msgHereIsWaiterCheck(RestaurantZhangCheck c);
	// Gets name
	public abstract String getName();
	public abstract void msgReadyToOrder(RestaurantZhangCustomer customer);
	public abstract void msgHereIsMyChoice(RestaurantZhangCustomer customer,
			String choice);
	public abstract void msgHereWasMyOrder(RestaurantZhangCustomer customer,
			String choice);
	public abstract void msgLeavingTable(RestaurantZhangCustomer customer);
	public abstract int getNumberCustomers();
	public abstract int getNumberCustomersServed();
	public abstract void msgSeatCustomer(RestaurantZhangTable t, RestaurantZhangCustomer c);
	public abstract void msgGoOnBreak(boolean b);
	public abstract void msgOutOfFood(RestaurantZhangTable t);
	public abstract void msgOrderIsReady(String choice, RestaurantZhangTable t);
	public abstract void msgAtTable();
}
