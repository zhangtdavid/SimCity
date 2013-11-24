package city.buildings;

import java.util.HashMap;
import java.util.Map;

import city.Building;
import city.Application.FOOD_ITEMS;

public class RestaurantChungBuilding extends Building {

//  Food
//  =====================================================================
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
	
	public RestaurantChungBuilding(String name) {
		super(name);
        // Add items and their cooking times to a map
        foods.put(FOOD_ITEMS.chicken, new Food("chicken", 10, 10, 5, 10, 16));
        foods.put(FOOD_ITEMS.pizza, new Food("pizza", 15, 10, 5, 10, 12));
        foods.put(FOOD_ITEMS.salad, new Food("salad", 5, 10, 5, 10, 6));
        foods.put(FOOD_ITEMS.steak, new Food("steak", 20, 10, 5, 10, 10));
        
        setCash(500);
	}

}
