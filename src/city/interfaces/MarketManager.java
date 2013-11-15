package city.interfaces;

public interface MarketManager extends AbstractRole {

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
