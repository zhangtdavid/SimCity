package city.roles.interfaces;

import city.bases.interfaces.RoleInterface;

public interface RestaurantJPCashier extends RoleInterface {
	
	// Data
	public enum state{pending, computing, charging, finished};
	// Messages
	
	public void msgPayment(RestaurantJPCustomer cust, int bill);
	public void msgComputeBill(RestaurantJPWaiter w, RestaurantJPCustomer c, String choice);
	public void msgFlaking(RestaurantJPCustomer c, int bill);

	// Getters
	
	public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment();

}