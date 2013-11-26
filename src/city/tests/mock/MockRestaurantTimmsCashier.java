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
	
	// Constructor
	
	public MockRestaurantTimmsCashier() {}
	
	// Messages
	
	@Override
	public void msgComputeCheck(RestaurantTimmsWaiter w, RestaurantTimmsCustomer c, int money) {
		log.add(new LoggedEvent("Received msgComputeCheck from Waiter. Amount: " + money));
	}

	@Override
	public void msgMakePayment(RestaurantTimmsCustomer c, int money) {
		log.add(new LoggedEvent("Received msgMakePayment from Customer. Money: " + money));
		c.msgPaidCashier(10); // Send a message from a Cashier to reduce the customer's money
	}
	
	// Getters
	
	@Override
	public MarketCustomerDeliveryPayment getMarketPaymentRole() {
		return null;
	}

	@Override
	public int getMoneyCollected() {
		return 0;
	}

	@Override
	public int getMoneyOwed() {
		return 0;
	}

	@Override
	public List<Check> getChecks() {
		return null;
	}

}
