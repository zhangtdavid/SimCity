package city.interfaces;

import city.Application;
import city.animations.interfaces.RestaurantTimmsAnimatedCook;

public interface RestaurantTimmsCook {
	
	public void msgCookOrder(RestaurantTimmsWaiter w, RestaurantTimmsCustomer c, Application.MARKET_ITEMS s);
	public void msgPickUpOrder(RestaurantTimmsCustomer c);
	public void msgMarketOrderPlaced(Application.MARKET_ITEMS s, Boolean inStock);
	public void msgMarketOrderDelivered(Application.MARKET_ITEMS s, int quantity);
	
	public RestaurantTimmsAnimatedCook getAnimation();
	public int getMenuItemPrice(Application.MARKET_ITEMS stockItem);

}
