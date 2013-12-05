package city.roles.interfaces;

import java.util.Collection;

import utilities.RestaurantZhangTable;
import city.bases.interfaces.RoleInterface;

public interface RestaurantZhangHost extends RoleInterface {
	
	// Data
	
	public static final int RESTAURANTZHANGHOSTSALARY = 100;
	
	// Messages
	
	public void msgImEntering(RestaurantZhangCustomer cust);
	public void msgIWantFood(RestaurantZhangCustomer cust);
	public void msgImLeaving(RestaurantZhangCustomer cust);
	public void msgTableOpen(RestaurantZhangTable t);
	public void msgWaiterRequestBreak(RestaurantZhangWaiter w);
	public void msgOffBreak(RestaurantZhangWaiter w);
	
	// Getters
	
	public int getNumberOfCustomersInRestaurant();
	
	// Setters
	
	public void setTables(Collection<RestaurantZhangTable> t);
	
	// Utilities
	
	public void addWaiter(RestaurantZhangWaiter wa);

}
