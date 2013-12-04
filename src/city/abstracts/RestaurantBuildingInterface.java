package city.abstracts;

import java.util.HashMap;
import java.util.Map;

import city.BuildingInterface;
import city.Application.FOOD_ITEMS;
import city.abstracts.RestaurantBuildingBase.FoodOrderState;

public interface RestaurantBuildingInterface extends BuildingInterface {

    // Messages
    
    // Scheduler
	
	// Actions
	
	// Getters
	
	public HashMap<FOOD_ITEMS, Food> getFoods();
	
	// Utilities 

	public void addFood(FOOD_ITEMS i, Food f);
	public void incrementFoodQuantity(Map<FOOD_ITEMS, Integer> receivedItems);
	public void updateFoodQuantity(FOOD_ITEMS f, int i);
	
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
            this.s = FoodOrderState.None;
        }
    }
	
}