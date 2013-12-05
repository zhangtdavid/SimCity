package city.roles.interfaces;

import city.bases.interfaces.RoleInterface;

public interface RestaurantTimmsCashier extends RoleInterface {
	
	// Data
	
	public static final int MIN_CAPITAL = 500;
	public static final int MAX_CAPITAL = 2000;
	
	// Messages
	
	public void msgComputeCheck(RestaurantTimmsWaiter w, RestaurantTimmsCustomer c, int money);
	public void msgMakePayment(RestaurantTimmsCustomer c, int money);
	
	// Getters
	
	public MarketCustomerDeliveryPayment getMarketPaymentRole();
	public int getMoneyCollected();
	public int getMoneyOwed();
	
}
