package city.interfaces;

import java.util.Map;

import city.Application.FOOD_ITEMS;
import city.Building;
import city.RoleInterface;
import city.abstracts.RestaurantBuildingBase;
import city.buildings.MarketBuilding;

public interface MarketCustomerDelivery extends RoleInterface {
	
	// Data
	public enum MarketCustomerState {None, Ordering};
	
	// Constructor
	
	// Messages
	
	public void msgHereIsOrderDelivery(Map<FOOD_ITEMS, Integer> collectedItems, int orderId);
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	public Market getMarket();
	public Building getRestaurant();
	
	// Setters
	
	public void setRestaurant(RestaurantBuildingBase restaurant);
	public void setMarket(MarketBuilding selectedMarket);

	MarketCustomerState getState();

	void setState(MarketCustomerState state);
	
	// Utilities
	
	// Classes
	
}
