package city.roles;

import utilities.RestaurantJPWaiterBase;
import city.Role;
import city.buildings.RestaurantJPBuilding;
import utilities.RestaurantJPTableClass;

public class RestaurantJPWaiterSharedDataRole extends RestaurantJPWaiterBase {
	 
	public RestaurantJPWaiterSharedDataRole(RestaurantJPBuilding b) {
         super(b);
	}
	
	public void sendOrderToCook(RestaurantJPWaiterRole.MyCustomer mc, String choice, RestaurantJPTableClass t) {
       // print("Going to cook for customer " + mc.customer.getName());
//      thisGui.setFoodLabel(choice, false); // Shows food ordered in animation
//      DoGoToCook();
//      WaitForAnimation();
//      Do("Adding order for customer " + mc.customer.getName() + " to order stand.");
//      myOrderStand.addOrder(new RestaurantZhangOrder(this, choice, t, myCook.getPosOfNewOrder()));
        //myCook.msgHereIsAnOrder(this, choice, t); //TODO: Remove this and implement orderstand
        //mc.state = mcState.orderCooking;
//      thisGui.setFoodLabel("", true); // Removes food ordered in animation
	 }

}
