package city.interfaces;

import city.Application;
import city.animations.interfaces.RestaurantTimmsAnimatedCook;

public interface RestaurantTimmsCook extends RoleInterface {
	
	public void msgCookOrder(RestaurantTimmsWaiter w, RestaurantTimmsCustomer c, Application.MARKET_ITEM s);
	public void msgPickUpOrder(RestaurantTimmsCustomer c);
	public void msgMarketOrderPlaced(Application.MARKET_ITEM s, Boolean inStock);
	public void msgMarketOrderDelivered(Application.MARKET_ITEM s, int quantity);
	
	public RestaurantTimmsAnimatedCook getAnimation();
	public int getMenuItemPrice(Application.MARKET_ITEM stockItem);

}
