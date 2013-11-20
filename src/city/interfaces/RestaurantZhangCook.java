package city.interfaces;

import utilities.RestaurantZhangTable;
import city.animations.RestaurantZhangCookAnimation;

public interface RestaurantZhangCook extends RoleInterface {
	
	public abstract void msgHereIsAnOrder(RestaurantZhangWaiter w, String choice, RestaurantZhangTable t);

	public abstract void msgGotCompletedOrder(RestaurantZhangTable table);

	public abstract int getX();

	public abstract int getY();

	public abstract int getPosOfNewOrder();

	public abstract void msgAtBase();
}
