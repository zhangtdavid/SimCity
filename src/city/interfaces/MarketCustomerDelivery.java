package city.interfaces;

import java.util.Map;

public interface MarketCustomerDelivery extends RoleInterface {
	
	// Data
	
	// Constructor
	
	// Messages
	
	public abstract void msgWhatWouldYouLike(MarketEmployee e);
	public abstract void msgHereIsOrder(Map<String, Integer> collectedItems);
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
