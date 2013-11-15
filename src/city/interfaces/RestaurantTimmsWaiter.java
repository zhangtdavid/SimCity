package city.interfaces;

import city.Application;

public interface RestaurantTimmsWaiter {
	
	public abstract void msgWantBreak();
	public abstract void msgAllowBreak(Boolean r);
	public abstract void msgSeatCustomer(RestaurantTimmsCustomer c, Integer n);
	public abstract void msgWantFood(RestaurantTimmsCustomer c);
	public abstract void msgOrderFood(RestaurantTimmsCustomer c, Application.MARKET_ITEMS s);
	public abstract void msgOrderPlaced(RestaurantTimmsCustomer c, Boolean inStock);
	public abstract void msgFoodReady(RestaurantTimmsCustomer c);
	public abstract void msgCheckReady();
	public abstract void msgDoNotWantFood(RestaurantTimmsCustomer c);
	public abstract void guiAtCustomer();
	public abstract void guiAtTable();
	public abstract void guiAtKitchen();
	public abstract void guiAtHome();

	public abstract String getName();
	public abstract Boolean getWantsBreak();
}
