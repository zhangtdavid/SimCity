package city.roles;

import utilities.RestaurantZhangTable;
import city.interfaces.RestaurantZhangWaiterBase;

public class RestaurantZhangWaiterRegularRole extends RestaurantZhangWaiterBase {

	public RestaurantZhangWaiterRegularRole(String name, int shiftStart_, int shiftEnd_) {
		super(name, shiftStart_, shiftEnd_);
	}
	
	public void sendOrderToCook(RestaurantZhangWaiterBase.MyCustomer mc, String choice, RestaurantZhangTable t) {
		print("Going to cook for customer " + mc.customer.getName());
		thisGui.setFoodLabel(choice, false); // Shows food ordered in animation
		DoGoToCook();
		WaitForAnimation();
		myCook.msgHereIsAnOrder(this, choice, t);
		mc.state = mcState.orderCooking;
		thisGui.setFoodLabel("", true); // Removes food ordered in animation
	}
}
