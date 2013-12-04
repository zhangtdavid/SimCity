package city.roles;

import trace.AlertLog;
import trace.AlertTag;
import utilities.RestaurantZhangTable;
import city.Building;
import city.abstracts.RestaurantZhangWaiterBase;

public class RestaurantZhangWaiterRegularRole extends RestaurantZhangWaiterBase {
	
	// Constructor

	public RestaurantZhangWaiterRegularRole(Building restaurantToWorkAt, int shiftStart_, int shiftEnd_) {
		super(restaurantToWorkAt, shiftStart_, shiftEnd_);
	}
	
	// Actions
	
	protected void sendOrderToCook(RestaurantZhangWaiterBase.MyCustomer mc, String choice, RestaurantZhangTable t) {
		print("Going to cook for customer " + mc.customer.getPerson().getName());
		thisGui.setFoodLabel(choice, false); // Shows food ordered in animation
		DoGoToCook();
		WaitForAnimation();
		myCook.msgHereIsAnOrder(this, choice, t);
		mc.state = MyCustomer.STATE.orderCooking;
		thisGui.setFoodLabel("", true); // Removes food ordered in animation
	}
	
	// Utilities
	
	@Override
	public void print(String msg) {
        super.print(msg);
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTZHANG, "RestaurantZhangWaiterRegularRole " + this.getPerson().getName(), msg);
    }
}
