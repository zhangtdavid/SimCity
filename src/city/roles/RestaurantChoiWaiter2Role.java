package city.roles;

import utilities.RestaurantChoiOrder;
import city.animations.interfaces.RestaurantChoiAnimatedCook;
import city.interfaces.RestaurantChoiWaiterAbs;

/**
 * The key difference between Waiter2Role vs WaiterRole is
 * WaiterRole puts the orders on plating (adds to queue), whereas
 * Waiter2Role hands the orders directly to the cook (via msgHeresAnOrder)
 *  
 * @author ryanchoi
 *
 */
public class RestaurantChoiWaiter2Role extends RestaurantChoiWaiterAbs{

	public RestaurantChoiWaiter2Role() {
		super();
	}

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
		waiterGui.GoTo(RestaurantChoiAnimatedCook.restingCoordX, RestaurantChoiAnimatedCook.restingCoordY);
		try {
			inProgress.acquire();
		} catch (InterruptedException e) {
			// not supported yet
			e.printStackTrace();
		}
		waiterGui.DoLeave();
		stateChanged();
	}
}
