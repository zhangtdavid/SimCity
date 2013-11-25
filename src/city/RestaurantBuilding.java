package city;

import java.util.HashMap;
import java.util.Map;

import city.Application.FOOD_ITEMS;

/**
 * The base restaurant class for all SimCity201 Restaurants.
 * 
 */
public abstract class RestaurantBuilding extends Building {
	
	// Data
	
	public Map<FOOD_ITEMS, Food> foods = new HashMap<FOOD_ITEMS, Food>();
    public class Food {
        public String item;
        public int cookingTime;
        public int amount;
        public int low;
        public int capacity;
        public int price;
        public FoodOrderState s;
        
        public Food(String item, int cookingTime, int amount, int low, int capacity, int price) {
            this.item = item;
            this.cookingTime = cookingTime;
            this.amount = amount;
            this.low = low;
            this.capacity = capacity;
            s = FoodOrderState.None;
        }
    }
    public enum FoodOrderState
    {None, Pending, Ordered};
	
	// Constructor

	public RestaurantBuilding(String name) {
		super(name);
	}
    
    // Messages
    
    // Scheduler
	
	// Actions
	
	// Getters
	
	// Utilities 
	public void addFood(Map<FOOD_ITEMS, Integer> receivedItems) {
        for (FOOD_ITEMS s: receivedItems.keySet()) {
        	foods.get(s).amount += receivedItems.get(s); // Add delivered food items to restaurant inventory
        }		
	}
}
