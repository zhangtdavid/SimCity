package city.interfaces;

import java.util.Map;

public interface MarketCashier extends RoleInterface {

	// Data
	
	// Constructor
	
	// Messages
	
	public abstract void msgNewDeliveryPerson(MarketDeliveryPerson d);
	public abstract void msgRemoveDeliveryPerson(MarketDeliveryPerson d);
	public abstract void msgComputeBill(MarketEmployee e, MarketCustomer c, Map<String, Integer> order, Map<String, Integer> collectedItems);
	public abstract void msgHereIsPayment(MarketCustomer c, double money);
	public abstract void msgComputeBill(MarketEmployee e, MarketCustomerDelivery c, Map<String, Integer> order, Map<String, Integer> collectedItems);
	public abstract void msgHereIsPayment(MarketCustomerDelivery c, double money);
	public abstract void msgDeliveringItems(MarketDeliveryPerson d);
	public abstract void msgFinishedDeliveringItems(MarketDeliveryPerson d, MarketCustomerDelivery cd);
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
