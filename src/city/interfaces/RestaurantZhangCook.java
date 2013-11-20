package city.interfaces;

import utilities.RestaurantZhangTable;
import city.interfaces.RestaurantZhangCustomer;
import city.animations.interfaces.RestaurantZhangAnimatedCook;

public interface RestaurantZhangCook extends RoleInterface {
	
	public abstract void msgHereIsAnOrder(RestaurantZhangWaiter w, String choice, RestaurantZhangTable t);

	public abstract void msgGotCompletedOrder(RestaurantZhangTable table);

	public abstract int getX();

	public abstract int getY();

	public abstract int getPosOfNewOrder();

}
