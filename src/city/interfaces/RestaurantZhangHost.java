package city.interfaces;

import utilities.RestaurantZhangTable;
import city.interfaces.RestaurantZhangCustomer;
import city.animations.interfaces.RestaurantZhangAnimatedHost;

public interface RestaurantZhangHost extends RoleInterface {
	
	public void msgImEntering(RestaurantZhangCustomer cust);
	
	public void msgIWantFood(RestaurantZhangCustomer cust);
	
	public void msgImLeaving(RestaurantZhangCustomer cust);

	public void msgTableOpen(RestaurantZhangTable t);
	
	public void msgWaiterRequestBreak(RestaurantZhangWaiter w);
	
	public void msgOffBreak(RestaurantZhangWaiter w);

}
