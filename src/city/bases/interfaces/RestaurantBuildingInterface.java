package city.bases.interfaces;

import java.util.HashMap;
import java.util.Map;

import city.Application.FOOD_ITEMS;
import city.bases.RestaurantBuilding.FoodOrderState;

public interface RestaurantBuildingInterface extends BuildingInterface {

    // Messages
    
    // Scheduler
	
	// Actions
	
	// Getters
	
	public HashMap<FOOD_ITEMS, Food> getFoods();
	
	// Utilities 

	public void addFood(FOOD_ITEMS i, Food f);
	public void incrementFoodQuantity(Map<FOOD_ITEMS, Integer> receivedItems);
	public void setFoodQuantity(FOOD_ITEMS f, int i);
	
	// Classes
	
    public class Food {
        private String item;
        private int cookingTime;
        private int amount;
        private int low;
        private int capacity;
        private int price;
        private FoodOrderState s;
        
        public Food(String item, int cookingTime, int amount, int low, int capacity, int price) {
            this.setItem(item);
            this.setCookingTime(cookingTime);
            this.setAmount(amount);
            this.setLow(low);
            this.setCapacity(capacity);
            this.setPrice(price);
            this.setFoodOrderState(FoodOrderState.None);
        }

        // Getters
		public String getItem() {
			return item;
		}

		public int getCookingTime() {
			return cookingTime;
		}
		
		public int getAmount() {
			return amount;
		}
		
		public int getLow() {
			return low;
		}
		
		public int getCapacity() {
			return capacity;
		}
		
		public int getPrice() {
			return price;
		}
		
		public FoodOrderState getFoodOrderState() {
			return s;
		}
		
		// Setters
		public void setItem(String item) {
			this.item = item;
		}

		public void setCookingTime(int cookingTime) {
			this.cookingTime = cookingTime;
		}

		public void setAmount(int amount) {
			this.amount = amount;
		}

		public void setLow(int low) {
			this.low = low;
		}

		public void setCapacity(int capacity) {
			this.capacity = capacity;
		}

		public void setPrice(int price) {
			this.price = price;
		}

		public void setFoodOrderState(FoodOrderState s) {
			this.s = s;
		}
    }	
}
