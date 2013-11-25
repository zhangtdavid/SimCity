package city.interfaces;

import city.Application;

public interface RestaurantTimmsCook extends RoleInterface {
	
	public void msgCookOrder(RestaurantTimmsWaiter w, RestaurantTimmsCustomer c, Application.FOOD_ITEMS s);
	public void msgPickUpOrder(RestaurantTimmsCustomer c);
	public void msgMarketOrderPlaced(Application.FOOD_ITEMS s, Boolean inStock);
	public void msgMarketOrderDelivered(Application.FOOD_ITEMS s, int quantity);
	
	public int getMenuItemPrice(Application.FOOD_ITEMS stockItem);
	
}
