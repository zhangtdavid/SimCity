package city.abstracts;

import java.util.HashMap;
import java.util.Map;

import city.Application.FOOD_ITEMS;
import city.Building;
import city.interfaces.BankCustomer;

/**
 * The base building class for all SimCity201 Restaurants.
 */
public abstract class RestaurantBuildingBase extends Building implements RestaurantBuildingInterface {
	
	// Data
	
	public BankCustomer bankCustomer;
	public HashMap<FOOD_ITEMS, Food> foods = new HashMap<FOOD_ITEMS, Food>();
	public enum FoodOrderState {None, Pending, Ordered};
	
	// Constructor

	public RestaurantBuildingBase(String name) {
		super(name);
	}
    
    // Messages
    
    // Scheduler
	
	// Actions
	
	// Getters
	
	@Override
	public HashMap<FOOD_ITEMS, Food> getFoods() {
		return foods;
	}
	
	// Utilities 
	
	/**
	 * Add a new type of food to restaurant inventory (create)
	 */
	@Override
	public void addFood(FOOD_ITEMS i, Food f) {
		foods.put(i, f);
	}
	
	/**
	 * Add delivered food items to restaurant inventory (update)
	 */
	@Override
	public void incrementFoodQuantity(Map<FOOD_ITEMS, Integer> receivedItems) {
        for (FOOD_ITEMS s: receivedItems.keySet()) {
        	foods.get(s).amount += receivedItems.get(s);
        }		
	}
	
	/**
	 * Set food item quantity explicitly (update)
	 */
	@Override
	public void updateFoodQuantity(FOOD_ITEMS f, int i) {
		foods.get(f).amount = i;
	}
	
}
