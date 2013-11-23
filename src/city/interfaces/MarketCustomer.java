package city.interfaces;

import java.util.Map;

public interface MarketCustomer extends RoleInterface {

	// Data
	
	// Constructor
	
	// Messages
	
	public abstract void msgAnimationArrivedAtMarket();
	public abstract void msgWhatWouldYouLike(MarketEmployee e, int loc);
	public abstract void msgHereIsOrderandBill(Map<String, Integer> collectedItems, int bill);
	public abstract void msgPaymentReceived();

	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
