package city.interfaces;

import java.util.Map;

import city.Application.FOOD_ITEMS;

public interface MarketCustomer extends RoleInterface {

	// Data
	
	// Constructor
	
	// Messages
	
	public abstract void msgAnimationArrivedAtMarket();
	public abstract void msgWhatWouldYouLike(MarketEmployee e, int loc);
	public abstract void msgHereIsOrderandBill(Map<FOOD_ITEMS, Integer> collectedItems, int bill, int id);
	public abstract void msgPaymentReceived();

	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
