package city.tests.mock;

import utilities.LoggedEvent;
import city.MockRole;
import city.animations.RestaurantTimmsHostAnimation;
import city.animations.interfaces.RestaurantTimmsAnimatedHost;
import city.interfaces.RestaurantTimmsCustomer;
import city.interfaces.RestaurantTimmsHost;
import city.interfaces.RestaurantTimmsWaiter;

public class MockRestaurantTimmsHost extends MockRole implements RestaurantTimmsHost {

	public MockRestaurantTimmsHost() {
		// TODO Auto-generated method stub
		
	}

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

}
