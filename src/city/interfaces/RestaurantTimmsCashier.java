package city.interfaces;

import city.animations.interfaces.RestaurantTimmsAnimatedCashier;

public interface RestaurantTimmsCashier extends RoleInterface {
	
	public abstract void msgComputeCheck(RestaurantTimmsWaiter w, RestaurantTimmsCustomer c, int money);
	public abstract void msgMakePayment(RestaurantTimmsCustomer c, int money);
	// public abstract void msgPayMarket(Market m, int money); // TODO

	public abstract RestaurantTimmsAnimatedCashier getAnimation();
	
}
