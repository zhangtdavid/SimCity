package city.roles;

import trace.AlertLog;
import trace.AlertTag;
import utilities.RestaurantJPWaiterBase;
import city.buildings.RestaurantJPBuilding;
import city.buildings.interfaces.RestaurantJP;
import city.roles.interfaces.RestaurantJPCustomer;

public class RestaurantJPWaiterSharedDataRole extends RestaurantJPWaiterBase {
	 
	public RestaurantJPWaiterSharedDataRole(RestaurantJPBuilding b, int shiftStart, int shiftEnd) {
         super(b, shiftStart, shiftEnd);
         this.setWorkplace(b);
 		this.setSalary(RestaurantJP.WORKER_SALARY);
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
	public void msgHereIsCheck(int check, RestaurantJPCashierRole csh,
			RestaurantJPCustomer c) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void print(String msg) {
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTJP, "RestaurantJPWaiterSharedDataRole " + this.getPerson().getName(), msg);
    }
}
