package city.roles;

import trace.AlertLog;
import trace.AlertTag;
import utilities.RestaurantChungOrder;
import utilities.RestaurantChungOrder.OrderState;
import utilities.RestaurantChungRevolvingStand;
import utilities.RestaurantChungWaiterBase;
import city.buildings.interfaces.RestaurantChung.MyCustomer.OrderStatus;
import city.Application.FOOD_ITEMS;
import city.animations.RestaurantChungWaiterAnimation;
import city.buildings.RestaurantChungBuilding;
import city.buildings.interfaces.RestaurantChung;
import city.buildings.interfaces.RestaurantChung.MyCustomer;

/**
 * Restaurant Waiter Agent
 */
//A Waiter tends to the host and customers' requests
public class RestaurantChungWaiterRevolvingStandRole extends RestaurantChungWaiterBase {	
	RestaurantChungRevolvingStand orderStand;

	public RestaurantChungWaiterRevolvingStandRole(RestaurantChung b, int t1, int t2) {
		super();
		restaurant = b;
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(RestaurantChungBuilding.WORKER_SALARY);
	}

	// Messages
	@Override
	public void tellCookOrder(MyCustomer customer, FOOD_ITEMS choice, int table) {
		this.getAnimation(RestaurantChungWaiterAnimation.class).DoGoToCook();

		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		print("adding order" + choice + " to stand for " + customer.getRestaurantChungCustomer());
		
		orderStand.addOrder(new RestaurantChungOrder(this, choice, table, OrderState.Pending));
		
		customer.setOrderStatus(OrderStatus.Cooking);
		
		this.getAnimation(RestaurantChungWaiterAnimation.class).DoReturnToWaiterHome();
	}
	
	// Getters
	public RestaurantChungRevolvingStand getRevolvingStand() {
		return orderStand;
	}
	
	// Setters
	public void setRevolvingStand(RestaurantChungRevolvingStand stand) {
		orderStand = stand;
	}
	
	@Override
	public void print(String msg) {
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTCHUNG, "RestaurantChungWaiterRevolvingStandRole " + this.getPerson().getName(), msg);
    }
}

