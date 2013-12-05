package city.roles.interfaces;

import utilities.RestaurantJPTableClass;
import city.bases.interfaces.RoleInterface;
import city.roles.RestaurantJPCashierRole;

public interface RestaurantJPWaiter extends RoleInterface {

	// Messages
	
	public void msgHereIsCheck(int check, RestaurantJPCashierRole csh, RestaurantJPCustomer c);
	public void msgImReadyToOrder(RestaurantJPCustomer customer);
	public void msgDoneEatingAndLeaving(RestaurantJPCustomer customer);
	public void msgHereIsMyChoice(String myOrder, RestaurantJPCustomer customer);
	public void msgOutOf(String choice, RestaurantJPTableClass table);
	public void msgOrderIsReady(String choice, RestaurantJPTableClass t);	
}