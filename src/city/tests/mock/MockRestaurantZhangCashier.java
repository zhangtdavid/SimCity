package city.tests.mock;

import java.util.List;
import java.util.Map;

import utilities.LoggedEvent;
import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangMenu;
import city.abstracts.MockRole;
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
		log.add(new LoggedEvent("Making bill from " + waiter.getPerson().getName() + " for order " + choice));
	}

	@Override
	public void msgHereIsPayment(RestaurantZhangCheck c, int cash) {
		log.add(new LoggedEvent("Got check from " + c.cust.getPerson().getName() + " with payment " + cash));
	}

	@Override
	public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RestaurantZhangCheck> getPendingChecks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMenu(RestaurantZhangMenu m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHost(RestaurantZhangHost h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RestaurantZhangHost getHost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBalance() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Map<RestaurantZhangCustomer, Integer> getTabCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Integer> getMenu() {
		// TODO Auto-generated method stub
		return null;
	}
}
