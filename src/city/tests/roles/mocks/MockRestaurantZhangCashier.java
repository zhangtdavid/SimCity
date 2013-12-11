package city.tests.roles.mocks;

import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;

import utilities.LoggedEvent;
import utilities.MarketOrder;
import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangMenu;
import city.buildings.interfaces.Market;
import city.roles.interfaces.MarketCustomerDeliveryPayment;
import city.roles.interfaces.RestaurantZhangCashier;
import city.roles.interfaces.RestaurantZhangCustomer;
import city.roles.interfaces.RestaurantZhangHost;
import city.roles.interfaces.RestaurantZhangWaiter;
import city.tests.bases.mocks.MockRole;

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

	@Override
	public void msgAddMarketOrder(Market m, MarketOrder o) {
		// TODO Auto-generated method stub
		
	}
}
