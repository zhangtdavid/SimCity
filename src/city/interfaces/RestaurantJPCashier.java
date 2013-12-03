package city.interfaces;

public interface RestaurantJPCashier extends RoleInterface {
	
	// Data
	
	// Messages
	
	public void msgPayment(RestaurantJPCustomer cust, int bill);
	public void msgComputeBill(RestaurantJPWaiter w, RestaurantJPCustomer c, String choice);
	public void msgFlaking(RestaurantJPCustomer c, int bill);

	// Getters
	
	public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment();

}