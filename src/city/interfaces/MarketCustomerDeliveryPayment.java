package city.interfaces;

public interface MarketCustomerDeliveryPayment extends RoleInterface {
	
	// Data
	
	// Constructor
	
	// Messages
	
	public abstract void msgHereIsBill(MarketCashier c, int bill, int id);
	public abstract void msgPaymentReceived(int id);
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
