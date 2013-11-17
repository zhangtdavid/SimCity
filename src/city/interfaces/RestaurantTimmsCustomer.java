package city.interfaces;

import city.Application;
import city.animations.interfaces.RestaurantTimmsAnimatedCustomer;

public interface RestaurantTimmsCustomer extends RoleInterface {
	
	public abstract void msgGoToRestaurant();
	public abstract void msgRestaurantFull();
	public abstract void msgGoToTable(RestaurantTimmsWaiter w, int position);
	public abstract void msgOrderFromWaiter();
	public abstract void msgWaiterDeliveredFood(Application.MARKET_ITEMS stockItem);
	public abstract void msgPaidCashier(int change);
	public abstract void guiAtLine();
	public abstract void guiAtTable();
	public abstract void guiAtCashier();
	public abstract void guiAtExit();
	
	public abstract RestaurantTimmsAnimatedCustomer getAnimation();

}