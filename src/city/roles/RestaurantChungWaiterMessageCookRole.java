package city.roles;

import city.animations.RestaurantChungWaiterAnimation;
import city.buildings.RestaurantChungBuilding;

/**
 * Restaurant Waiter Agent
 */
//A Waiter tends to the host and customers' requests
public class RestaurantChungWaiterMessageCookRole extends RestaurantChungWaiterBaseRole {
	public RestaurantChungWaiterMessageCookRole(RestaurantChungBuilding b, int t1, int t2) {
		super();
		restaurant = b;
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(RestaurantChungBuilding.getWorkerSalary());
	}

	public void tellCookOrder(WCustomer customer, String choice, int table) {
		this.getAnimation(RestaurantChungWaiterAnimation.class).DoGoToCook();

		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		print("telling cook order " + choice + " for " + customer.c);
		cook.msgHereIsAnOrder(this, choice, table);
		customer.o.os = OrderStatus.Cooking;
		
		this.getAnimation(RestaurantChungWaiterAnimation.class).DoReturnToWaiterHome();
	}
}

