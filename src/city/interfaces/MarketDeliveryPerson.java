package city.interfaces;

import java.util.Map;

import city.RoleInterface;
import city.Application.FOOD_ITEMS;
import city.buildings.MarketBuilding;

public interface MarketDeliveryPerson extends RoleInterface {

	// Data
	
	// Constructor
	
	// Messages
	
	public void msgDeliverOrder(MarketCustomerDelivery customerDelivery, Map<FOOD_ITEMS, Integer> i, int id);
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	public Market getMarket();
	
	// Setters

	public void setMarket(MarketBuilding market);
	
	// Utilities
	
	// Classes
	
}
