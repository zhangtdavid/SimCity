package city.tests.mock;

import java.beans.PropertyChangeSupport;

import utilities.LoggedEvent;
import city.abstracts.MockRole;
import city.interfaces.RestaurantTimmsCustomer;
import city.interfaces.RestaurantTimmsHost;
import city.interfaces.RestaurantTimmsWaiter;

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
