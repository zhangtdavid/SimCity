package city.tests.mock;

import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangTable;
import city.MockRole;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.interfaces.RestaurantZhangCashier;
import city.interfaces.RestaurantZhangCustomer;
import city.interfaces.RestaurantZhangHost;
import city.interfaces.RestaurantZhangWaiter;

public class MockRestaurantZhangCashier extends MockRole implements RestaurantZhangCashier {
	
	public MockRestaurantZhangCashier() {
		super();
	}

	@Override
	public void msgComputeBill(RestaurantZhangWaiter waiter,
			RestaurantZhangCustomer customer, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsPayment(RestaurantZhangCheck c, double cash) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment() {
		// TODO Auto-generated method stub
		return null;
	}
}
