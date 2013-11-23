package city.interfaces;

import utilities.MarketOrder;

public interface MarketCashier extends RoleInterface {

	// Data
	
	// Constructor
	
	// Messages
	
	public abstract void msgNewDeliveryPerson(MarketDeliveryPerson d);
	public abstract void msgRemoveDeliveryPerson(MarketDeliveryPerson d);
	public abstract void msgComputeBill(MarketEmployee e, MarketCustomer c, MarketOrder o);
	public abstract void msgHereIsPayment(MarketCustomer c, int money);
	public abstract void msgComputeBill(MarketEmployee e, MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay, MarketOrder o);
	public abstract void msgHereIsPayment(MarketCustomerDeliveryPayment c, int money);
	public abstract void msgDeliveringItems(MarketDeliveryPerson d);
	public abstract void msgFinishedDeliveringItems(MarketDeliveryPerson d, MarketCustomerDelivery cd);
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
