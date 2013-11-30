package city.interfaces;

import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangRevolvingStand;
import utilities.RestaurantZhangTable;
import city.animations.interfaces.RestaurantZhangAnimatedWaiter;

public interface RestaurantZhangWaiter extends RoleInterface {
	
	// Data
	
	public static final int BREAKX = 600;
	public static final int BREAKY = 600;
	public static final int BREAKTIME = 10000;
	public static final int RESTAURANTZHANGCASHIERSALARY = 100;
	
	// Messages
	
	public void msgHereIsWaiterCheck(RestaurantZhangCheck c);
	public void msgReadyToOrder(RestaurantZhangCustomer customer);
	public void msgHereIsMyChoice(RestaurantZhangCustomer customer, String choice);
	public void msgHereWasMyOrder(RestaurantZhangCustomer customer, String choice);
	public void msgLeavingTable(RestaurantZhangCustomer customer);
	public void msgSeatCustomer(RestaurantZhangTable t, RestaurantZhangCustomer c);
	public void msgGoOnBreak(boolean b);
	public void msgOutOfFood(RestaurantZhangTable t);
	public void msgOrderIsReady(String choice, RestaurantZhangTable t);
	public void msgAtDestination();
	
	// Getters
	
	public int getNumberCustomers();
	public int getNumberCustomersServed();
	
	// Setters

	public void setAnimation(RestaurantZhangAnimatedWaiter gui);
	public void setCook(RestaurantZhangCook c);
	public void setHost(RestaurantZhangHost h);
	public void setCashier(RestaurantZhangCashier c);
	public void setMenu(RestaurantZhangMenu m);
	public void setRevolvingStand(RestaurantZhangRevolvingStand rs);
	
}
