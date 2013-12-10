package city.roles.interfaces;

import java.util.List;
import java.util.Map;

import utilities.MarketOrder;
import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangMenu;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.Market;

public interface RestaurantZhangCashier extends RoleInterface {
	
	// Data
	
	public static final int CASHIERX = 0;
	public static final int CASHIERY = 200;
	public static final int RESTAURANTZHANGCASHIERSALARY = 100;
	
	// Messages
	
	public void msgComputeBill(RestaurantZhangWaiter w, RestaurantZhangCustomer c, String choice);
	public void msgHereIsPayment(RestaurantZhangCheck c, int cash);
	void msgAddMarketOrder(Market m, MarketOrder o);
	
	// Getters
	
	public List<RestaurantZhangCheck> getPendingChecks();
	public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment();
	public RestaurantZhangHost getHost();
	public int getBalance();
	public Map<RestaurantZhangCustomer, Integer> getTabCustomers();
	public Map<String, Integer> getMenu();
	
	// Setters
	
	public void setMenu(RestaurantZhangMenu m);
	public void setHost(RestaurantZhangHost h);
	
}
