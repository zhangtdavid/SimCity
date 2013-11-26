package city.interfaces;

import city.Application;

public interface RestaurantTimmsWaiter extends RoleInterface {
	
	// Messages
	
	public void msgWantBreak();
	public void msgAllowBreak(Boolean r);
	public void msgSeatCustomer(RestaurantTimmsCustomer c, int n);
	public void msgWantFood(RestaurantTimmsCustomer c);
	public void msgOrderFood(RestaurantTimmsCustomer c, Application.FOOD_ITEMS s);
	public void msgOrderPlaced(RestaurantTimmsCustomer c, Boolean inStock);
	public void msgFoodReady(RestaurantTimmsCustomer c);
	public void msgCheckReady();
	public void msgDoNotWantFood(RestaurantTimmsCustomer c);
	public void guiAtCustomer();
	public void guiAtTable();
	public void guiAtKitchen();
	public void guiAtHome();
	
	// Getters

	public Boolean getWantsBreak();
	
	// Setters
	
	public void setActive();
	
}
