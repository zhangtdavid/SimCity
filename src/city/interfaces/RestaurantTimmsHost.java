package city.interfaces;

public interface RestaurantTimmsHost extends RoleInterface {
	
	// Messages
	
	public void msgWantSeat(RestaurantTimmsCustomer customer);
	public void msgDoNotWantSeat(RestaurantTimmsCustomer customer);
	public void msgLeaving(RestaurantTimmsCustomer customer, int tableNumber);
	public void msgAskForBreak(RestaurantTimmsWaiter w);
	
}
