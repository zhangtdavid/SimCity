package city.roles.interfaces;

import java.util.Map;

import city.Application.FOOD_ITEMS;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.Market;
import city.roles.interfaces.MarketDeliveryPerson.WorkingState;

public interface MarketDeliveryPerson extends RoleInterface {
	// Data
	public enum WorkingState {Working, GoingOffShift};
	
	// Constructor
	
	// Messages
	
	public void msgDeliverOrder(MarketCustomerDelivery customerDelivery, Map<FOOD_ITEMS, Integer> i, int id);
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	public Market getMarket();
	MarketCustomerDelivery getCustomerDelivery();
	int getOrderId();
	Map<FOOD_ITEMS, Integer> getCollectedItems();
	WorkingState getWorkingState();
	
	// Setters
	public void setMarket(Market market);


	// Utilities
	
	// Classes
	
}
