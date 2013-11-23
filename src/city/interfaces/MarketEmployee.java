package city.interfaces;

import java.util.Map;

public interface MarketEmployee extends RoleInterface {
	
	// Data
	
	// Constructor
	
	// Messages
	
	public abstract void msgAssistCustomer(MarketCustomer c);
	public abstract void msgAssistCustomerDelivery(MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay);
	public abstract void msgHereIsCustomerDeliveryOrder(Map<String, Integer> o);
	public abstract void msgHereIsMyOrder(MarketCustomer c, Map<String, Integer> o);

	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
