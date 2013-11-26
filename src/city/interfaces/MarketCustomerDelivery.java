package city.interfaces;

import java.util.Map;

import city.Application.FOOD_ITEMS;
import city.Building;
import city.buildings.MarketBuilding;

public interface MarketCustomerDelivery extends RoleInterface {
	
	// Data
	
	// Constructor
	
	// Messages
	
	public abstract void msgHereIsOrderDelivery(Map<FOOD_ITEMS, Integer> collectedItems, int orderId);

	public abstract Building getRestaurant();

	public abstract void setMarket(MarketBuilding selectedMarket);
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
