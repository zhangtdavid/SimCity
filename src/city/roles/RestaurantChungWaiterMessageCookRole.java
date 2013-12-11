package city.roles;

import trace.AlertLog;
import trace.AlertTag;
import utilities.RestaurantChungWaiterBase;
import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantChungAnimatedWaiter;
import city.buildings.RestaurantChungBuilding;
import city.buildings.interfaces.RestaurantChung;
import city.buildings.interfaces.RestaurantChung.MyCustomer;
import city.buildings.interfaces.RestaurantChung.MyCustomer.OrderStatus;

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
	public void tellCookOrder(MyCustomer customer, FOOD_ITEMS choice, int table) {
		this.getAnimation(RestaurantChungAnimatedWaiter.class).DoGoToCook();

		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		print("telling cook order " + choice + " for " + customer.getRestaurantChungCustomer());
		restaurant.getRestaurantChungCook().msgHereIsAnOrder(this, choice, table);
		customer.setOrderStatus(OrderStatus.Cooking);
		
		this.getAnimation(RestaurantChungAnimatedWaiter.class).DoReturnToWaiterHome();
	}
	
	@Override
	public void print(String msg) {
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTCHUNG, "RestaurantChungWaiterMessageCookRole " + this.getPerson().getName(), msg);
    }
}

