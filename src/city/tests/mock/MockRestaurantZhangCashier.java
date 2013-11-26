package city.tests.mock;

import utilities.LoggedEvent;
import utilities.RestaurantZhangCheck;
import city.MockRole;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.interfaces.RestaurantZhangCashier;
import city.interfaces.RestaurantZhangCustomer;
import city.interfaces.RestaurantZhangWaiter;

public class MockRestaurantZhangCashier extends MockRole implements RestaurantZhangCashier {
	
	public MockRestaurantZhangCashier() {
		super();
	}

	@Override
	public void msgComputeBill(RestaurantZhangWaiter waiter,
			RestaurantZhangCustomer customer, String choice) {
		log.add(new LoggedEvent("Making bill from " + waiter.getName() + " for order " + choice));
	}

	@Override
	public void msgHereIsPayment(RestaurantZhangCheck c, double cash) {
		log.add(new LoggedEvent("Got check from " + c.cust.getName() + " with payment " + cash));
	}

	@Override
	public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment() {
		// TODO Auto-generated method stub
		return null;
	}
}
