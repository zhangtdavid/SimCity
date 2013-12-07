package city.tests.roles.mocks;

import java.beans.PropertyChangeSupport;

import utilities.LoggedEvent;
import city.Application;
import city.Application.FOOD_ITEMS;
import city.roles.interfaces.RestaurantTimmsCook;
import city.roles.interfaces.RestaurantTimmsCustomer;
import city.roles.interfaces.RestaurantTimmsWaiter;
import city.tests.bases.mocks.MockRole;

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

	@Override
	public PropertyChangeSupport getPropertyChangeSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStateString() {
		// TODO Auto-generated method stub
		return null;
	}

}
