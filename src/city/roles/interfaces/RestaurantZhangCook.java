package city.roles.interfaces;

import java.util.List;
import java.util.Map;

import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangOrder;
import utilities.RestaurantZhangRevolvingStand;
import utilities.RestaurantZhangTable;
import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantZhangAnimatedCook;
import city.bases.interfaces.RoleInterface;
import city.buildings.MarketBuilding;

public interface RestaurantZhangCook extends RoleInterface {
	
	// Data
	public enum MarketOrderState {Pending, Ordered};
	
	public static final int RESTAURANTZHANGCOOKSALARY = 100;
	public static final int COOKX = 190;
	public static final int COOKY = 120;
	
	// Messages
	
	public void msgHereIsAnOrder(RestaurantZhangWaiter w, String choice, RestaurantZhangTable t);
	public void msgGotCompletedOrder(RestaurantZhangTable table);
	public void msgAtDestination();
	void msgHereIsOrderDelivery(Map<FOOD_ITEMS, Integer> marketOrder, int id);

	// Getters
	
	public void hackOrderIsReady(RestaurantZhangOrder o);
	public List<MarketBuilding> getMarkets();
	public RestaurantZhangCashier getCashier();
	public List<MarketCustomerDelivery> getmarketCustomerDeliveryList();
	public RestaurantZhangAnimatedCook getGui();
	public RestaurantZhangHost getHost();
	public List<RestaurantZhangOrder> getOrdersToCook();
	public RestaurantZhangRevolvingStand getOrderStand();
	public RestaurantZhangMenu getMainMenu();
	public int getPosOfNewOrder();
	public boolean getWaitingToCheckStand();
	
	// Setters
	
	public void setMenuTimes(RestaurantZhangMenu m);
	public void setAnimation(RestaurantZhangAnimatedCook gui);
	public void addMarket(MarketBuilding m);
	public void setRevolvingStand(RestaurantZhangRevolvingStand rs);
	public void setHost(RestaurantZhangHost h);
	
	// Utilities
	
}
