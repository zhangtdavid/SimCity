package city.interfaces;

import java.util.Map;

/**
 *
 * @author Monroe Ekilah
 *
 */
public interface RestaurantChungCook extends RoleInterface {

	void msgAnimationAtCookHome();

	void msgAnimationAtGrill();

	void msgAnimationAtPlating();

	void msgCannotFulfill(int iD, Map<String, Integer> unfulfilled);

	void msgOrderIsReady(int iD, Map<String, Integer> orderItems);

	void msgHereIsAnOrder(
			RestaurantChungWaiterBase restaurantChungWaiterBase,
			String choice, int table);

}