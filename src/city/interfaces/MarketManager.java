package city.interfaces;

import java.util.Map;

import city.roles.MarketCustomerDeliveryRole;
import city.roles.MarketEmployeeRole;

public interface MarketManager {

	// Data
	
	// Constructor
	
	// Messages
	public abstract void msgIWouldLikeToPlaceAnOrder(MarketCustomer c);
	public abstract void msgIWouldLikeToPlaceADeliveryOrder(MarketCustomerDelivery c);
	public abstract void msgIAmAvailableToAssist(MarketEmployee e);

	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
