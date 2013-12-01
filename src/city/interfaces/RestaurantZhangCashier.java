package city.interfaces;

import java.util.List;

import city.RoleInterface;
import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangMenu;

public interface RestaurantZhangCashier extends RoleInterface {
	
	// Data
	
	public static final int CASHIERX = 0;
	public static final int CASHIERY = 200;
	public static final int RESTAURANTZHANGCASHIERSALARY = 100;
	
	// Messages
	
	public void msgComputeBill(RestaurantZhangWaiter w, RestaurantZhangCustomer c, String choice);
	public void msgHereIsPayment(RestaurantZhangCheck c, int cash);
	
	// Getters
	
	public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment();
	public List<RestaurantZhangCheck> getPendingChecks();
	
	// Setters
	
	public void setMenu(RestaurantZhangMenu m);
	public void setHost(RestaurantZhangHost h);
	
}
