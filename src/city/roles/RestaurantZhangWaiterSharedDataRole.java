package city.roles;

import trace.AlertLog;
import trace.AlertTag;
import utilities.RestaurantZhangOrder;
import utilities.RestaurantZhangTable;
import utilities.RestaurantZhangWaiterBase;
import city.bases.Building;

public class RestaurantZhangWaiterSharedDataRole extends RestaurantZhangWaiterBase {
	
	// Constructor
	
	public RestaurantZhangWaiterSharedDataRole(Building restaurantToWorkAt, int shiftStart_, int shiftEnd_) {
		super(restaurantToWorkAt, shiftStart_, shiftEnd_);
	}
	
	// Actions
	
	protected void sendOrderToCook(RestaurantZhangWaiterBase.MyCustomer mc, String choice, RestaurantZhangTable t) {
		print("Going to cook for customer " + mc.customer.getPerson().getName());
		thisGui.setFoodLabel(choice, false); // Shows food ordered in animation
		doGoToCook();
		waitForAnimation();
		print("Adding order for customer " + mc.customer.getPerson().getName() + " to order stand.");
		myOrderStand.addOrder(new RestaurantZhangOrder(this, choice, t, myCook.getPosOfNewOrder()));
		mc.state = MyCustomer.STATE.orderCooking;
		thisGui.setFoodLabel("", true); // Removes food ordered in animation
	}
	
	// Utilities
	
	@Override
	public void print(String msg) {
		this.getPerson().printViaRole("RestaurantZhangWaiterSharedData", msg);
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTZHANG, "RestaurantZhangWaiterSharedDataRole " + this.getPerson().getName(), msg);
    }
	
}
