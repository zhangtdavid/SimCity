package city.roles;

import trace.AlertLog;
import trace.AlertTag;
import utilities.RestaurantChoiOrder;
import utilities.RestaurantChoiRevolvingStand;
import city.interfaces.RestaurantChoiWaiterAbs;

public class RestaurantChoiWaiterRole extends RestaurantChoiWaiterAbs{

	/**
	 * The key difference between Waiter2Role vs WaiterRole is
	 * WaiterRole puts the orders on plating (adds to queue), whereas
	 * Waiter2Role hands the orders directly to the cook (via msgHeresAnOrder)
	 *  
	 * @author ryanchoi
	 *
	 */
	public RestaurantChoiWaiterRole() {
		super();
	}

	@Override
	public void sendOrderToCook(int i, RestaurantChoiOrder o) {
		DoGoToCook(); // acquire is in method; this goes to cook plating
		myCustomers.get(i).getOr().setTableNumber(myCustomers.get(i).getT()
				.getTableNumber());
		//synchronized(orderqueue){ // shared queue
			synchronized(o){ // shared order
				orderqueue.add(o); // add to queue on plating
			}
		//}
	}

	public void setRevolvingStand(RestaurantChoiRevolvingStand rs) {
		orderqueue = rs;
	}
	
	@Override
	public void print(String msg) {
        super.print(msg);
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTCHOI, "RestaurantChoiWaiterRole " + this.getPerson().getName(), msg);
    }
}
