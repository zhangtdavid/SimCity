package city.interfaces;

import city.Application;

public interface RestaurantTimmsWaiter extends RoleInterface {
	
	public abstract void msgWantBreak();
	public abstract void msgAllowBreak(Boolean r);
	public abstract void msgSeatCustomer(RestaurantTimmsCustomer c, int n);
	public abstract void msgWantFood(RestaurantTimmsCustomer c);
	public abstract void msgOrderFood(RestaurantTimmsCustomer c, Application.FOOD_ITEMS s);
	public abstract void msgOrderPlaced(RestaurantTimmsCustomer c, Boolean inStock);
	public abstract void msgFoodReady(RestaurantTimmsCustomer c);
	public abstract void msgCheckReady();
	public abstract void msgDoNotWantFood(RestaurantTimmsCustomer c);
	public abstract void guiAtCustomer();
	public abstract void guiAtTable();
	public abstract void guiAtKitchen();
	public abstract void guiAtHome();

	public abstract Boolean getWantsBreak();
}
