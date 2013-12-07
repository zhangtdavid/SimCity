package city.roles.interfaces;

import java.util.Map;

import utilities.MarketOrder;
import city.Application.FOOD_ITEMS;
import city.bases.RestaurantBuilding;
import city.bases.interfaces.RestaurantBuildingInterface;
import city.bases.interfaces.RoleInterface;
import city.buildings.MarketBuilding;
import city.buildings.interfaces.Market;

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
	public RestaurantBuildingInterface getRestaurant();
	MarketCustomerState getState();
	MarketOrder getOrder();
	
	// Setters
	public void setRestaurant(RestaurantBuilding restaurant);
	public void setMarket(MarketBuilding selectedMarket);
	void setState(MarketCustomerState state);

	
	// Utilities
	
	// Classes
	
}
