package city.interfaces;

import city.animations.interfaces.RestaurantChoiAnimatedCashier;

public interface RestaurantChoiCashier extends RoleInterface{

	public abstract void msgComputeCheck(RestaurantChoiWaiter w, RestaurantChoiCustomer c, double money);
	public abstract void msgMakePayment(RestaurantChoiCustomer c, double money);
	// public abstract void msgPayMarket(Market m, double money); // TODO

	public abstract RestaurantChoiAnimatedCashier getAnimation();
	
}
