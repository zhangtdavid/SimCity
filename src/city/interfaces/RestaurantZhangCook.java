package city.interfaces;

import java.util.Map;

import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantZhangAnimatedCook;
import city.buildings.MarketBuilding;
import city.buildings.RestaurantBaseBuilding.Food;
import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangOrder;
import utilities.RestaurantZhangRevolvingStand;
import utilities.RestaurantZhangTable;

public interface RestaurantZhangCook extends RoleInterface {
	
	// Data
	
	public static final int RESTAURANTZHANGCOOKSALARY = 100;
	public static final int COOKX = 190;
	public static final int COOKY = 120;
	
	// Messages
	
	public void msgHereIsAnOrder(RestaurantZhangWaiter w, String choice, RestaurantZhangTable t);
	public void msgGotCompletedOrder(RestaurantZhangTable table);
	public void msgAtDestination();
	public void msgProcessedInvoice(String food, boolean isAvailable, int processedAmount);
	public void msgHereIsInvoice(String food, int amount);

	// Getters
	
	public int getPosOfNewOrder();
	public RestaurantZhangAnimatedCook getGui();
	
	// Setters
	
	public void setMenuTimes(RestaurantZhangMenu m, Map<FOOD_ITEMS, Food> food);
	public void setAnimation(RestaurantZhangAnimatedCook gui);
	public void addMarket(MarketBuilding m);
	public void setRevolvingStand(RestaurantZhangRevolvingStand rs);
	public void setHost(RestaurantZhangHost h);
	
	// Utilities
	
	public void hackOrderIsReady(RestaurantZhangOrder o);
	
}
