package city.roles.interfaces;

import utilities.RestaurantChoiTable;
import utilities.RestaurantChoiWaiterBase;
import city.animations.interfaces.RestaurantChoiAnimatedHost;
import city.bases.interfaces.RoleInterface;

public interface RestaurantChoiHost extends RoleInterface{
	
	// Data
	
	// Messages
	
	public void msgImHungry(RestaurantChoiCustomer c);
	public void msgImBack(RestaurantChoiWaiterBase w);
	public void msgIWantABreak(RestaurantChoiWaiterBase w);
	public void msgNotWaiting(RestaurantChoiCustomer c);
	public void msgTablesClear(RestaurantChoiWaiterBase w, RestaurantChoiTable table);
	public void msgSetUnavailable(RestaurantChoiWaiterBase waiter);
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	public RestaurantChoiAnimatedHost getAnimation();
	
	// Setters
	
	public void setAnimation(RestaurantChoiAnimatedHost in);
	
	// Utilities

	public void addWaiter(RestaurantChoiWaiterBase w);

}
