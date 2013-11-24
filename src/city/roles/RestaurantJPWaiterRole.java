package city.roles;

import city.buildings.RestaurantJPBuilding;
import utilities.RestaurantJPWaiterBase;

public class RestaurantJPWaiterRole extends RestaurantJPWaiterBase {
	
	public RestaurantJPWaiterRole(RestaurantJPBuilding b) {
		super(b);
	}
	
	public void GiveOrderToCook(MyCustomer myC){
		waiterGui.DoGoToCook();
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myC.s = state.waitingForFood;
		building.cook.msgHereIsAnOrder(this, myC.choice, myC.table);
	}
		// TODO Auto-generated constructor stub
}

