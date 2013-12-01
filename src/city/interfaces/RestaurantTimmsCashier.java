package city.interfaces;

import city.RoleInterface;

public interface RestaurantTimmsCashier extends RoleInterface {
	
	// Messages
	
	public void msgComputeCheck(RestaurantTimmsWaiter w, RestaurantTimmsCustomer c, int money);
	public void msgMakePayment(RestaurantTimmsCustomer c, int money);
	
	// Getters
	
	public MarketCustomerDeliveryPayment getMarketPaymentRole();
	public int getMoneyCollected();
	public int getMoneyOwed();
	
}
