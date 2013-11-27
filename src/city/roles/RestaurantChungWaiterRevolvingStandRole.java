package city.roles;

import trace.AlertLog;
import trace.AlertTag;
import utilities.RestaurantChungOrder;
import utilities.RestaurantChungOrder.OrderState;

/**
 * Restaurant Waiter Agent
 */
//A Waiter tends to the host and customers' requests
public class RestaurantChungWaiterRevolvingStandRole extends RestaurantChungWaiterBaseRole {	
	public RestaurantChungWaiterRevolvingStandRole() {
		super();
	}

	public void tellCookOrder(WCustomer customer, String choice, int table) {
		waiterGui.DoGoToCook();

		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		print("adding order" + choice + " to stand for " + customer.c);
		
		orderStand.addOrder(new RestaurantChungOrder(this, choice, table, OrderState.Pending));
		
		customer.o.os = OrderStatus.Cooking;
		
		waiterGui.DoReturnToWaiterHome();
	}
	
	@Override
	public void print(String msg) {
        super.print(msg);
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTCHUNG, "RestaurantChungWaiterRevolvingStandRole " + this.getPerson().getName(), msg);
    }
}

