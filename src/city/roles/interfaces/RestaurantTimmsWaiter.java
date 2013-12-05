package city.roles.interfaces;

import java.util.List;

import city.Application;
import city.bases.interfaces.RoleInterface;
import city.roles.RestaurantTimmsWaiterRole.InternalCustomer;

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
	public List<InternalCustomer> getCustomers();
	
}
