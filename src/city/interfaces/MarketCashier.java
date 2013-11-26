package city.interfaces;

import java.util.Map;

import city.Application.FOOD_ITEMS;

public interface MarketCashier extends RoleInterface {

	// Data
	
	// Constructor
	
	// Messages
	
	public abstract void msgNewDeliveryPerson(MarketDeliveryPerson d);
	public abstract void msgRemoveDeliveryPerson(MarketDeliveryPerson d);
	public abstract void msgComputeBill(MarketEmployee e, MarketCustomer c, Map<FOOD_ITEMS, Integer> o, Map<FOOD_ITEMS, Integer> i, int id);
	public abstract void msgComputeBill(MarketEmployee e, MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay, Map<FOOD_ITEMS, Integer> o, Map<FOOD_ITEMS, Integer> i, int id);
	public abstract void msgHereIsPayment(int id, int money);
	public abstract void msgDeliveringItems(MarketDeliveryPerson d);
	public abstract void msgFinishedDeliveringItems(MarketDeliveryPerson d, int orderId);
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
