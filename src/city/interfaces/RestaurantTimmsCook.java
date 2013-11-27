package city.interfaces;

import city.Application;

public interface RestaurantTimmsCook extends RoleInterface {
	
	// Messages
	
	public void msgCookOrder(RestaurantTimmsWaiter w, RestaurantTimmsCustomer c, Application.FOOD_ITEMS s);
	public void msgPickUpOrder(RestaurantTimmsCustomer c);
	
	// Getters
	
	public int getMenuItemPrice(Application.FOOD_ITEMS stockItem);
	
	// Setters
	
	public void setActive();
	
}
