package city.roles;

import utilities.RestaurantJPWaiterBase;
import city.buildings.RestaurantJPBuilding;

public class RestaurantJPWaiterRole extends RestaurantJPWaiterBase {
	
	public RestaurantJPWaiterRole(RestaurantJPBuilding b, int shiftStart, int shiftEnd) {
		super(b, shiftStart, shiftEnd);
		this.setWorkplace(b);
		this.setSalary(RestaurantJPBuilding.WORKER_SALARY);
		this.setShift(shiftStart, shiftEnd);
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

