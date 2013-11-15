package city.interfaces;

import java.util.Map;

public interface MarketCustomerDelivery {

	// Data
	
	// Constructor
	
	// Messages
	public abstract void msgWhatWouldYouLike(MarketEmployee e);
	public abstract void msgHereIsBill(double bill);
	public abstract void msgPaymentReceived();
	public abstract void msgHereIsOrder(Map<String, Integer> collectedItems);
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
