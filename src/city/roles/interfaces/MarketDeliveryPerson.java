package city.roles.interfaces;

import java.util.Map;
import java.util.Queue;

import city.Application.FOOD_ITEMS;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.Market;
import city.buildings.interfaces.Market.DeliveryState;
import city.roles.MarketDeliveryPersonRole.MyDelivery;

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

	void msgArrivedAtDestination();

	Queue<MyDelivery> getDeliveries();

	DeliveryState getDeliveryState();


	// Utilities
	
	// Classes
	
}
