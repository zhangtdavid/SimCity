package city.tests.roles.mocks;

import java.beans.PropertyChangeSupport;

import utilities.LoggedEvent;
import city.roles.interfaces.RestaurantTimmsCustomer;
import city.roles.interfaces.RestaurantTimmsHost;
import city.roles.interfaces.RestaurantTimmsWaiter;
import city.tests.bases.mocks.MockRole;

public class MockRestaurantTimmsHost extends MockRole implements RestaurantTimmsHost {

	// Constructors
	
	public MockRestaurantTimmsHost() {}

	// Messages
	
	@Override
	public void msgWantSeat(RestaurantTimmsCustomer customer) {
		log.add(new LoggedEvent("Received msgWantSeat from Customer."));
	}

	@Override
	public void msgDoNotWantSeat(RestaurantTimmsCustomer customer) {
		log.add(new LoggedEvent("Received msgDoNotWantSeat from Customer."));

	}

	@Override
	public void msgLeaving(RestaurantTimmsCustomer customer, int tableNumber) {
		log.add(new LoggedEvent("Received msgLeaving from Customer. Table: " + tableNumber));

	}

	@Override
	public void msgAskForBreak(RestaurantTimmsWaiter w) {
		log.add(new LoggedEvent("Received msgAskForBreak from Waiter."));

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
