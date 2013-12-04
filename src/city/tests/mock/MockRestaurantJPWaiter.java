package city.tests.mock;

import utilities.LoggedEvent;
import utilities.RestaurantJPTableClass;
import city.abstracts.MockRole;
import city.interfaces.RestaurantJPCustomer;
import city.interfaces.RestaurantJPWaiter;
import city.roles.RestaurantJPCashierRole;

public class MockRestaurantJPWaiter extends MockRole implements RestaurantJPWaiter {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public MockRestaurantJPWaiter(String name) {
		super();

	}

	@Override
	public void msgHereIsCheck(int check, RestaurantJPCashierRole csh,
			RestaurantJPCustomer c) {
		log.add(new LoggedEvent("Received HereIsYourTotal from cashier. Total = "+ check));

		c.msgHereIsCheck(check, csh);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgImReadyToOrder(RestaurantJPCustomer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneEatingAndLeaving(RestaurantJPCustomer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyChoice(String myOrder, RestaurantJPCustomer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOf(String choice, RestaurantJPTableClass table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderIsReady(String choice, RestaurantJPTableClass table) {
		// TODO Auto-generated method stub
		
	}
}