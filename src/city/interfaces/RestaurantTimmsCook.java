package city.interfaces;

import city.Application;
import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantTimmsAnimatedCook;

public interface RestaurantTimmsCook extends RoleInterface {
	
	public void msgCookOrder(RestaurantTimmsWaiter w, RestaurantTimmsCustomer c, FOOD_ITEMS s);
	public void msgPickUpOrder(RestaurantTimmsCustomer c);
	public void msgMarketOrderPlaced(FOOD_ITEMS s, Boolean inStock);
	public void msgMarketOrderDelivered(FOOD_ITEMS s, int quantity);
	
	public RestaurantTimmsAnimatedCook getAnimation();
	public int getMenuItemPrice(FOOD_ITEMS stockItem);

}
