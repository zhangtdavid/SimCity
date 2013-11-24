package city.interfaces;

import java.util.Map;

import city.Application.FOOD_ITEMS;

public interface MarketDeliveryPerson extends RoleInterface {

	// Data
	
	// Constructor
	
	// Messages
	
	public abstract void msgDeliverOrder(MarketCustomerDelivery customerDelivery, Map<FOOD_ITEMS, Integer> i, int id);
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
