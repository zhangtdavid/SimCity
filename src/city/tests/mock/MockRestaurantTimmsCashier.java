package city.tests.mock;

import java.util.List;

import utilities.LoggedEvent;
import city.MockRole;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.interfaces.RestaurantTimmsCashier;
import city.interfaces.RestaurantTimmsCustomer;
import city.interfaces.RestaurantTimmsWaiter;
import city.roles.RestaurantTimmsCashierRole.Check;

public class MockRestaurantTimmsCashier extends MockRole implements RestaurantTimmsCashier {
	
	public MockRestaurantTimmsCashier() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgComputeCheck(RestaurantTimmsWaiter w, RestaurantTimmsCustomer c, int money) {
		log.add(new LoggedEvent("Received msgComputeCheck from Waiter. Amount: " + money));

	}

	@Override
	public void msgMakePayment(RestaurantTimmsCustomer c, int money) {
		log.add(new LoggedEvent("Received msgMakePayment from Customer. Money: " + money));
		c.msgPaidCashier(money);
	}

	@Override
	public MarketCustomerDeliveryPayment getMarketPaymentRole() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMoneyCollected() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMoneyOwed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Check> getChecks() {
		// TODO Auto-generated method stub
		return null;
	}

}
