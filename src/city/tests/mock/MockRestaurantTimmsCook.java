package city.tests.mock;

import utilities.LoggedEvent;
import city.Application;
import city.Application.FOOD_ITEMS;
import city.abstracts.MockRole;
import city.interfaces.RestaurantTimmsCook;
import city.interfaces.RestaurantTimmsCustomer;
import city.interfaces.RestaurantTimmsWaiter;

public class MockRestaurantTimmsCook extends MockRole implements RestaurantTimmsCook {

	// Constructor
	
	public MockRestaurantTimmsCook() {}

	// Messages
	
	@Override
	public void msgCookOrder(RestaurantTimmsWaiter w, RestaurantTimmsCustomer c, Application.FOOD_ITEMS s) {
		log.add(new LoggedEvent("Received msgCookOrder from Waiter. Item: " + s.toString()));
		
	}

	@Override
	public void msgPickUpOrder(RestaurantTimmsCustomer c) {
		log.add(new LoggedEvent("Received msgPickUpOrder from Waiter."));

	}
	
	// Getters

	@Override
	public int getMenuItemPrice(FOOD_ITEMS stockItem) {
		return 0;
	}

}
