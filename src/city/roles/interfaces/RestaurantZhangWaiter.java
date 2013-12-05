package city.roles.interfaces;

import java.util.List;

import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangRevolvingStand;
import utilities.RestaurantZhangTable;
import utilities.RestaurantZhangWaiterBase.MyCustomer;
import city.animations.interfaces.RestaurantZhangAnimatedWaiter;
import city.bases.interfaces.RoleInterface;

public interface RestaurantZhangWaiter extends RoleInterface {
	
	// Data
	
	public static final int BREAKX = 600;
	public static final int BREAKY = 600;
	public static final int BREAKTIME = 10000;
	public static final int RESTAURANTZHANGCASHIERSALARY = 100;
	public enum breakStatus {notOnBreak, wantToBreak, goingOnBreak, onBreak};
	
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
	public List<MyCustomer> getCustomerList();
	public int getNumCustomersServed();
	public RestaurantZhangCook getCook();
	public RestaurantZhangHost getHost();
	public RestaurantZhangCashier getCashier();
	public RestaurantZhangMenu getMenu();
	public RestaurantZhangRevolvingStand getOrderStand();
	public List<RestaurantZhangCheck> getCheckList();
	public breakStatus getBreakStatus();
	public RestaurantZhangAnimatedWaiter getAnimation();
	public int getNumberCustomers();
	public boolean getOnBreak();
	
	// Setters
	
	void setAnimation(RestaurantZhangAnimatedWaiter gui);
	void setCook(RestaurantZhangCook c);
	void setHost(RestaurantZhangHost h);
	void setCashier(RestaurantZhangCashier c);
	void setMenu(RestaurantZhangMenu m);
	void setRevolvingStand(RestaurantZhangRevolvingStand rs);
}
