package city.interfaces;

import java.util.Map;

import city.Application.FOOD_ITEMS;

public interface MarketEmployee extends RoleInterface {
	
	// Data
	
	// Constructor
	
	// Messages
	
	public abstract void msgAssistCustomer(MarketCustomer c);
	public abstract void msgAssistCustomerDelivery(MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay);
	public abstract void msgHereIsCustomerDeliveryOrder(Map<FOOD_ITEMS, Integer> o, int id);
	public abstract void msgHereIsMyOrder(MarketCustomer c, Map<FOOD_ITEMS, Integer> o, int id);

	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
