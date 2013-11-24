package city.interfaces;

import java.util.Map;

import city.Application.FOOD_ITEMS;

public interface MarketCustomerDelivery extends RoleInterface {
	
	// Data
	
	// Constructor
	
	// Messages
	
	public abstract void msgHereIsOrder(Map<FOOD_ITEMS, Integer> collectedItems, int id);
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
