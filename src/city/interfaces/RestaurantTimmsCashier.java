package city.interfaces;

import java.util.List;

import city.roles.RestaurantTimmsCashierRole.Check;

public interface RestaurantTimmsCashier extends RoleInterface {
	
	// Messages
	
	public void msgComputeCheck(RestaurantTimmsWaiter w, RestaurantTimmsCustomer c, int money);
	public void msgMakePayment(RestaurantTimmsCustomer c, int money);
	// public abstract void msgPayMarket(Market m, int money); // TODO
	
	// Getters
	
	public MarketCustomerDeliveryPayment getMarketPaymentRole();
	public int getMoneyCollected();
	public int getMoneyOwed();
	public List<Check> getChecks();
	
	// Setters
	
	public void setActive();
	
}
