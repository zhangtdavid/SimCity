package city.roles;

import trace.AlertLog;
import trace.AlertTag;
import city.abstracts.RestaurantChungWaiterBase;
import city.abstracts.RestaurantChungWaiterBase.WCustomer.OrderStatus;
import city.animations.RestaurantChungWaiterAnimation;
import city.buildings.RestaurantChungBuilding;
import city.interfaces.RestaurantChung;

/**
 * Restaurant Waiter Agent
 */
//A Waiter tends to the host and customers' requests
public class RestaurantChungWaiterMessageCookRole extends RestaurantChungWaiterBase {
	public RestaurantChungWaiterMessageCookRole(RestaurantChung b, int t1, int t2) {
		super();
		this.restaurant = b;
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(RestaurantChungBuilding.WORKER_SALARY);
	}

	// Messages
	@Override
	public void tellCookOrder(WCustomer customer, String choice, int table) {
		this.getAnimation(RestaurantChungWaiterAnimation.class).DoGoToCook();

		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		print("telling cook order " + choice + " for " + customer.c);
		restaurant.getRestaurantChungCook().msgHereIsAnOrder(this, choice, table);
		customer.o.os = OrderStatus.Cooking;
		
		this.getAnimation(RestaurantChungWaiterAnimation.class).DoReturnToWaiterHome();
	}
	
	@Override
	public void print(String msg) {
        super.print(msg);
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTCHUNG, "RestaurantChungWaiterMessageCookRole " + this.getPerson().getName(), msg);
    }
}

