package city.roles;

import utilities.RestaurantJPWaiterBase;
import city.buildings.RestaurantJPBuilding;

public class RestaurantJPWaiterSharedDataRole extends RestaurantJPWaiterBase {
	 
	public RestaurantJPWaiterSharedDataRole(RestaurantJPBuilding b) {
         super(b);
	}
	
	public void GiveOrderToCook(RestaurantJPWaiterRole.MyCustomer myC) {
		waiterGui.DoGoToCook();
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myC.s = state.waitingForFood;
		revolvingStand.addOrder(building.cook.new Order(this, myC.choice, myC.table));
	}
}
