package city.roles;

import trace.AlertLog;
import trace.AlertTag;
import utilities.RestaurantJPWaiterBase;
import city.buildings.RestaurantJPBuilding;

public class RestaurantJPWaiterSharedDataRole extends RestaurantJPWaiterBase {
	 
	public RestaurantJPWaiterSharedDataRole(RestaurantJPBuilding b, int shiftStart, int shiftEnd) {
         super(b, shiftStart, shiftEnd);
         this.setWorkplace(b);
 		this.setSalary(RestaurantJPBuilding.WORKER_SALARY);
 		this.setShift(shiftStart, shiftEnd);
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
	
	@Override
	public void print(String msg) {
        super.print(msg);
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTJP, "RestaurantJPWaiterSharedDataRole " + this.getPerson().getName(), msg);
    }
}
