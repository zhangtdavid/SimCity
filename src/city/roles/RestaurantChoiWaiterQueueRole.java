package city.roles;

import trace.AlertLog;
import trace.AlertTag;
import utilities.RestaurantChoiOrder;
import utilities.RestaurantChoiRevolvingStand;
import utilities.RestaurantChoiWaiterBase;
import city.buildings.RestaurantChoiBuilding;

/**
 * The key difference between WaiterDirectRole vs. WaiterQueueRole is
 * WaiterQueueRole puts the orders on plating (adds to queue), whereas
 * WaiterDirectRole hands the orders directly to the cook (via msgHeresAnOrder)
 */
public class RestaurantChoiWaiterQueueRole extends RestaurantChoiWaiterBase {

	// Data
	
	// Constructor
	
	public RestaurantChoiWaiterQueueRole() {
		super();
	}
	
	public RestaurantChoiWaiterQueueRole(RestaurantChoiBuilding b, int t1, int t2){
		super(b, t1, t2);
	}
	
	// Messages
	
	// Scheduler
	
	// Actions

	@Override
	protected void sendOrderToCook(int i, RestaurantChoiOrder o) {
		DoGoToCook(); // acquire is in method; this goes to cook plating
		myCustomers.get(i).getOr().setTableNumber(myCustomers.get(i).getT()
				.getTableNumber());
		//synchronized(orderqueue){ // shared queue
			synchronized(o){ // shared order
				orderqueue.add(o); // add to queue on plating
			}
		//}
	}

	// Getters
	
	// Setters
	
	@Override
	public void setRevolvingStand(RestaurantChoiRevolvingStand rs) {
		orderqueue = rs;
	}
	
	// Utilities
	
	@Override
	public void print(String msg) {
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTCHOI, "RestaurantChoiWaiterRole " + this.getPerson().getName(), msg);
    }
}
