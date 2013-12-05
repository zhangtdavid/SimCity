package city.roles;

import trace.AlertLog;
import trace.AlertTag;
import utilities.RestaurantChoiOrder;
import utilities.RestaurantChoiRevolvingStand;
import utilities.RestaurantChoiWaiterBase;
import city.animations.interfaces.RestaurantChoiAnimatedCook;
import city.buildings.RestaurantChoiBuilding;

/**
 * The key difference between WaiterDirectRole vs. WaiterQueueRole is
 * WaiterQueueRole puts the orders on plating (adds to queue), whereas
 * WaiterDirectRole hands the orders directly to the cook (via msgHeresAnOrder)
 */
public class RestaurantChoiWaiterDirectRole extends RestaurantChoiWaiterBase {

	// Data
	
	// Constructor
	
	public RestaurantChoiWaiterDirectRole() {
		super();
	}
	
	public RestaurantChoiWaiterDirectRole(RestaurantChoiBuilding b, int t1, int t2){
		super(b, t1, t2);
	}
	
	// Messages
	
	// Scheduler
	
	// Actions

	@Override
	public void sendOrderToCook(int i, RestaurantChoiOrder o) {
		DoGoToCookDirectly(); // acquire is in method; this goes to plating area
		myCustomers.get(i).getOr().setTableNumber(myCustomers.get(i).getT()
				.getTableNumber());
		cook.msgHeresAnOrder(myCustomers.get(i).getOr()); // and message him the order once you get there

	}
	
	private void DoGoToCookDirectly() {
		//while this is not going directly to the cook, this is very close to the real deal; otherwise i'd
		//have to be constantly asking every animation step whether I'm next to the cook or not...
		waiterGui.setAcquired();
		waiterGui.GoTo(RestaurantChoiAnimatedCook.RESTX, RestaurantChoiAnimatedCook.RESTY);
		try {
			inProgress.acquire();
		} catch (InterruptedException e) {
			// not supported yet
			e.printStackTrace();
		}
		waiterGui.DoLeave();
		stateChanged();
	}
	
	// Getters
	
	// Setters
	
	@Override
	public void setRevolvingStand(RestaurantChoiRevolvingStand rs) {
		return;
	}
	
	// Utilities
	
	@Override
	public void print(String msg) {
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTCHOI, "RestaurantChoiWaiter2Role " + this.getPerson().getName(), msg);
    }
	
}
