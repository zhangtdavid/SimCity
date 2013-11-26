package city.tests.mock;


import city.MockRole;
import city.animations.RestaurantChungCustomerAnimation;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.interfaces.RestaurantChungCashier;
import city.interfaces.RestaurantChungCustomer;
import city.interfaces.RestaurantChungHost;
import city.interfaces.RestaurantChungWaiter;
import utilities.EventLog;
import utilities.RestaurantChungMenu;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockRestaurantChungCashier extends MockRole implements RestaurantChungCashier {
	public EventLog log = new EventLog();

	@Override
	public void msgComputeBill(RestaurantChungWaiter w,
			RestaurantChungCustomer c, String order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsPayment(RestaurantChungCustomer c, int bill) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment() {
		// TODO Auto-generated method stub
		return null;
	}
}
