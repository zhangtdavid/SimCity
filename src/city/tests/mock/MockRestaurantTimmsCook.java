package city.tests.mock;

import utilities.LoggedEvent;
import city.Application;
import city.Application.MARKET_ITEM;
import city.MockRole;
import city.animations.RestaurantTimmsCookAnimation;
import city.animations.interfaces.RestaurantTimmsAnimatedCook;
import city.interfaces.RestaurantTimmsCook;
import city.interfaces.RestaurantTimmsCustomer;
import city.interfaces.RestaurantTimmsWaiter;

public class MockRestaurantTimmsCook extends MockRole implements RestaurantTimmsCook {

	public MockRestaurantTimmsCook() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCookOrder(RestaurantTimmsWaiter w, RestaurantTimmsCustomer c, Application.MARKET_ITEM s) {
		log.add(new LoggedEvent("Received msgCookOrder from Waiter. Item: " + s.toString()));
		
	}

	@Override
	public void msgPickUpOrder(RestaurantTimmsCustomer c) {
		log.add(new LoggedEvent("Received msgPickUpOrder from Waiter."));

	}

	@Override
	public void msgMarketOrderPlaced(Application.MARKET_ITEM s, Boolean inStock) {
		log.add(new LoggedEvent("Received msgMarketOrderPlaced from Market. Item: " + s.toString() + " In Stock: " + inStock.toString()));

	}

	@Override
	public void msgMarketOrderDelivered(Application.MARKET_ITEM s, int quantity) {
		log.add(new LoggedEvent("Received msgMarketOrderDelivered from Market. Item: " + s.toString() + " Quantity: " + quantity));

	}

	@Override
	public RestaurantTimmsAnimatedCook getAnimation() {
		return new RestaurantTimmsCookAnimation();
	}

	@Override
	public int getMenuItemPrice(MARKET_ITEM stockItem) {
		// TODO Auto-generated method stub
		return 0;
	}

}
