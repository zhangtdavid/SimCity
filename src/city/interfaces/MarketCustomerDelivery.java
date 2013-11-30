package city.interfaces;

import java.util.Map;

import city.Application.FOOD_ITEMS;
import city.Building;
import city.buildings.MarketBuilding;
import city.buildings.RestaurantBaseBuilding;

public interface MarketCustomerDelivery extends RoleInterface {
	
	// Data
	
	// Constructor
	
	// Messages
	
	public void msgHereIsOrderDelivery(Map<FOOD_ITEMS, Integer> collectedItems, int orderId);
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	public MarketBuilding getMarket();
	public Building getRestaurant();
	
	// Setters
	
	public void setRestaurant(RestaurantBaseBuilding restaurant);
	public void setMarket(MarketBuilding selectedMarket);
	
	// Utilities
	
	// Classes
	
}
