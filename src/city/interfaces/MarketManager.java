package city.interfaces;

import java.util.Map;

import city.RoleInterface;
import city.Application.FOOD_ITEMS;
import city.buildings.MarketBuilding;

public interface MarketManager extends RoleInterface {

	// Data
	
	// Constructor
	
	// Messages
	
	public void msgNewEmployee(MarketEmployee e);
	public void msgRemoveEmployee(MarketEmployee e);
	public void msgIWouldLikeToPlaceAnOrder(MarketCustomer c);
	public void msgIWouldLikeToPlaceADeliveryOrder(MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay, Map<FOOD_ITEMS, Integer> o, int id);
	public void msgWhatWouldCustomerDeliveryLike(MarketEmployee e);
	public void msgIAmAvailableToAssist(MarketEmployee e);
	public void msgItemLow();

	// Scheduler
	
	// Actions
	
	// Getters
	
	public MarketBuilding getMarket();
	
	// Setters
	
	public void setMarket(MarketBuilding market);
	
	// Utilities
	
	// Classes
	
}
