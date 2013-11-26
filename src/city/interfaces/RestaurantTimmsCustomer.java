package city.interfaces;

import city.Application;

public interface RestaurantTimmsCustomer extends RoleInterface {
	
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
	
	// Setters
	
	public void setActive();
	
}