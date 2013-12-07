package city.tests.roles.mocks;

import java.beans.PropertyChangeSupport;

import utilities.LoggedEvent;
import city.roles.interfaces.MarketCustomerDeliveryPayment;
import city.roles.interfaces.RestaurantTimmsCashier;
import city.roles.interfaces.RestaurantTimmsCustomer;
import city.roles.interfaces.RestaurantTimmsWaiter;
import city.tests.bases.mocks.MockRole;

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
	public PropertyChangeSupport getPropertyChangeSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStateString() {
		// TODO Auto-generated method stub
		return null;
	}

}
