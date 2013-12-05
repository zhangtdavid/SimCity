package city.roles.interfaces;

import city.Application;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.RestaurantTimms;
import city.roles.RestaurantTimmsCustomerRole.State;

public interface RestaurantTimmsCustomer extends RoleInterface {
	
	// Data
	
	public static final int HUNGER = 5;
	public static final int PICKINESS = 3;
	
	// Messages
	
	public void msgRestaurantFull();
	public void msgGoToTable(RestaurantTimmsWaiter w, int position);
	public void msgOrderFromWaiter();
	public void msgWaiterDeliveredFood(Application.FOOD_ITEMS stockItem);
	public void msgPaidCashier(int change);
	public void guiAtLine();
	public void guiAtTable();
	public void guiAtCashier();
	public void guiAtExit();
	
	// Getters
	
	public State getState();
	public Application.FOOD_ITEMS getOrderItem();
	
	// Setters
	
	public void setRestaurantTimmsBuilding(RestaurantTimms b);
	
}