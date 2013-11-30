package city.interfaces;

import java.util.HashMap;
import java.util.Map;

import city.Application.FOOD_ITEMS;
import city.buildings.RestaurantBaseBuilding.Food;

public interface RestaurantBaseBuildingInterface extends BuildingInterface {

    // Messages
    
    // Scheduler
	
	// Actions
	
	// Getters
	
	public HashMap<FOOD_ITEMS, Food> getFoods();
	
	// Utilities 

	public void addFood(FOOD_ITEMS i, Food f);
	public void incrementFoodQuantity(Map<FOOD_ITEMS, Integer> receivedItems);
	public void updateFoodQuantity(FOOD_ITEMS f, int i);

}