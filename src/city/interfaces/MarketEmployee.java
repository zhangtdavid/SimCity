package city.interfaces;

import java.util.Map;

import city.RoleInterface;
import city.Application.FOOD_ITEMS;
import city.buildings.MarketBuilding;

public interface MarketEmployee extends RoleInterface {
	
	// Data
	
	// Constructor
	
	// Messages
	
	public void msgAssistCustomer(MarketCustomer c);
	public void msgAssistCustomerDelivery(MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay);
	public void msgHereIsCustomerDeliveryOrder(Map<FOOD_ITEMS, Integer> o, int id);
	public void msgHereIsMyOrder(MarketCustomer c, Map<FOOD_ITEMS, Integer> o, int id);

	// Scheduler
	
	// Actions
	
	// Getters
	
	public Market getMarket();
	
	// Setters
	
	public void setMarket(MarketBuilding market);
	
	// Utilities
	
	// Classes
	
}
