package city.interfaces;

import java.util.Map;

public interface MarketCustomer {

	// Data
	
	// Constructor
	
	// Messages
	public abstract void msgAnimationArrivedAtMarket();
	public abstract void msgWhatWouldYouLike(MarketEmployee e, int loc);
	public abstract void msgHereIsOrderandBill(Map<String, Integer> collectedItems, double bill);
	public abstract void msgPaymentReceived();

	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
