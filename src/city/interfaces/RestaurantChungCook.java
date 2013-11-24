package city.interfaces;

import java.util.Map;

import city.Application.FOOD_ITEMS;

/**
 *
 * @author Monroe Ekilah
 *
 */
public interface RestaurantChungCook extends RoleInterface {

	void msgAnimationAtCookHome();

	void msgAnimationAtGrill();

	void msgAnimationAtPlating();

//	void msgCannotFulfill(int iD, Map<String, Integer> unfulfilled);

	void msgHereIsOrderDelivery(Map<FOOD_ITEMS, Integer> orderItems, int id);

	void msgHereIsAnOrder(
			RestaurantChungWaiterBase restaurantChungWaiterBase,
			String choice, int table);

}