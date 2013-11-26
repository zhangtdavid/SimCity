package city.interfaces;

public interface RestaurantTimmsCashier extends RoleInterface {
	
	// Messages
	
	public void msgComputeCheck(RestaurantTimmsWaiter w, RestaurantTimmsCustomer c, int money);
	public void msgMakePayment(RestaurantTimmsCustomer c, int money);
	// public abstract void msgPayMarket(Market m, int money); // TODO
	
	// Setters
	
	public void setActive();
	
}
