package city.interfaces;

import city.animations.interfaces.RestaurantTimmsAnimatedHost;

public interface RestaurantTimmsHost extends RoleInterface {
	
	public abstract void msgWantSeat(RestaurantTimmsCustomer customer);
	public abstract void msgDoNotWantSeat(RestaurantTimmsCustomer customer);
	public abstract void msgLeaving(RestaurantTimmsCustomer customer, int tableNumber);
	public abstract void msgAskForBreak(RestaurantTimmsWaiter w);
	
	public abstract RestaurantTimmsAnimatedHost getAnimation();

}
