package city.interfaces;

import java.util.Map;

import city.RoleInterface;
import city.Application.FOOD_ITEMS;

public interface RestaurantChungCook extends RoleInterface {
	
	// Messages

	public void msgAnimationAtCookHome();
	public void msgAnimationAtGrill();
	public void msgAnimationAtPlating();
	// public void msgCannotFulfill(int iD, Map<String, Integer> unfulfilled);
	public void msgHereIsOrderDelivery(Map<FOOD_ITEMS, Integer> orderItems, int id);
	public void msgHereIsAnOrder(RestaurantChungWaiter restaurantChungWaiterBase, String choice, int table);

}