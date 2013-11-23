package city.interfaces;

import java.util.Map;

public interface MarketManager extends RoleInterface {

	// Data
	
	// Constructor
	
	// Messages
	
	public abstract void msgNewEmployee(MarketEmployee e);
	public abstract void msgRemoveEmployee(MarketEmployee e);
	public abstract void msgIWouldLikeToPlaceAnOrder(MarketCustomer c);
	public abstract void msgIWouldLikeToPlaceADeliveryOrder(MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay, Map<String, Integer> o);
	public abstract void msgWhatWouldCustomerDeliveryLike(MarketEmployee e);
	public abstract void msgIAmAvailableToAssist(MarketEmployee e);
	public abstract void msgItemLow();


	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
