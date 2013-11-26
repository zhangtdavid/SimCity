package city.roles;

import utilities.RestaurantZhangOrder;
import utilities.RestaurantZhangTable;
import city.Building;
import city.interfaces.RestaurantZhangWaiterBase;

public class RestaurantZhangWaiterSharedDataRole extends RestaurantZhangWaiterBase {
	
	public RestaurantZhangWaiterSharedDataRole(Building restaurantToWorkAt, int shiftStart_, int shiftEnd_) {
		super(restaurantToWorkAt, shiftStart_, shiftEnd_);
	}
	
	public void sendOrderToCook(RestaurantZhangWaiterBase.MyCustomer mc, String choice, RestaurantZhangTable t) {
		print("Going to cook for customer " + mc.customer.getName());
		thisGui.setFoodLabel(choice, false); // Shows food ordered in animation
		DoGoToCook();
		WaitForAnimation();
		print("Adding order for customer " + mc.customer.getName() + " to order stand.");
		myOrderStand.addOrder(new RestaurantZhangOrder(this, choice, t, myCook.getPosOfNewOrder()));
		mc.state = mcState.orderCooking;
		thisGui.setFoodLabel("", true); // Removes food ordered in animation
	}
}
