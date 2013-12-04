package city.roles;

import trace.AlertLog;
import trace.AlertTag;
import utilities.RestaurantChungOrder;
import utilities.RestaurantChungOrder.OrderState;
import utilities.RestaurantChungRevolvingStand;
import city.abstracts.RestaurantChungWaiterBase;
import city.abstracts.RestaurantChungWaiterBase.WCustomer.OrderStatus;
import city.animations.RestaurantChungWaiterAnimation;
import city.buildings.RestaurantChungBuilding;

/**
 * Restaurant Waiter Agent
 */
//A Waiter tends to the host and customers' requests
public class RestaurantChungWaiterRevolvingStandRole extends RestaurantChungWaiterBase {	
	RestaurantChungRevolvingStand orderStand;

	public RestaurantChungWaiterRevolvingStandRole(RestaurantChungBuilding b, int t1, int t2) {
		super();
		restaurant = b;
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(RestaurantChungBuilding.getWorkerSalary());
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
		
		print("adding order" + choice + " to stand for " + customer.c);
		
		orderStand.addOrder(new RestaurantChungOrder(this, choice, table, OrderState.Pending));
		
		customer.o.os = OrderStatus.Cooking;
		
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
        super.print(msg);
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTCHUNG, "RestaurantChungWaiterRevolvingStandRole " + this.getPerson().getName(), msg);
    }
}

