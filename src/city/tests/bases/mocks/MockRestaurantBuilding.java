package city.tests.bases.mocks;

import java.util.HashMap;
import java.util.Map;

import city.Application.FOOD_ITEMS;
import city.bases.interfaces.RestaurantBuildingInterface;
import city.bases.interfaces.RestaurantBuildingInterface.Food;

public class MockRestaurantBuilding extends MockBuilding implements RestaurantBuildingInterface {

	public MockRestaurantBuilding(String name) {
		super(name);
	}

	@Override
	public HashMap<FOOD_ITEMS, Food> getFoods() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addFood(FOOD_ITEMS i, Food f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void incrementFoodQuantity(Map<FOOD_ITEMS, Integer> receivedItems) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFoodQuantity(FOOD_ITEMS f, int i) {
		// TODO Auto-generated method stub
		
	}

}
