package city.buildings;

import java.util.HashMap;
import java.util.Map;

import city.Application.FOOD_ITEMS;
import city.Building;
import city.interfaces.BankCustomer;
import city.roles.BankCustomerRole;

/**
 * The base restaurant class for all SimCity201 Restaurants.
 */
public abstract class RestaurantBaseBuilding extends Building {
	
	// Data
	
	public BankCustomer bankCustomer;
	public Map<FOOD_ITEMS, Food> foods = new HashMap<FOOD_ITEMS, Food>();
	public enum FoodOrderState {None, Pending, Ordered};
	
	// Constructor

	public RestaurantBaseBuilding(String name) {
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
	
	// Classes
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
            this.price = price;
            s = FoodOrderState.None;
        }
    }
}
