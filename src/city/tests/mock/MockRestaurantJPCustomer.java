package city.tests.mock;

import utilities.LoggedEvent;
import city.MockRole;
import city.interfaces.RestaurantJPCustomer;
import city.roles.RestaurantJPCashierRole;

public class MockRestaurantJPCustomer extends MockRole implements RestaurantJPCustomer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public RestaurantJPCashierRole cashier;

	public MockRestaurantJPCustomer(String name) {
		super();

	}

	@Override
	public void msgHereIsCheck(Float check, RestaurantJPCashierRole csh) {
		log.add(new LoggedEvent("Received check."));

		if(check.equals((float)10)){
			csh.msgFlaking(this, (float)0);
			return;
		}
			
		Float bill = check;
		cashier.msgPayment(this, bill);
	}

}
