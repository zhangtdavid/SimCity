package city.bases;

import java.util.HashMap;
import java.util.Map;

import city.Application;
import city.Application.BUILDING;
import city.Application.FOOD_ITEMS;
import city.bases.interfaces.RestaurantBuildingInterface;
import city.buildings.interfaces.Bank;
import city.gui.BuildingCard;
import city.gui.exteriors.CityViewBuilding;
import city.roles.BankCustomerRole;
import city.roles.interfaces.BankCustomer;

/**
 * The base building class for all SimCity201 Restaurants.
 */
public abstract class RestaurantBuilding extends Building implements RestaurantBuildingInterface {
	
	// Data
	
	private BankCustomer bankCustomer;
	protected HashMap<FOOD_ITEMS, Food> foods = new HashMap<FOOD_ITEMS, Food>();
	public enum FoodOrderState {None, Pending, Ordered};
	
	// Constructor

	public RestaurantBuilding(String name, BuildingCard panel, CityViewBuilding cityBuilding) {
		super(name, panel, cityBuilding);
		bankCustomer = new BankCustomerRole(this, (Bank)Application.CityMap.findRandomBuilding(BUILDING.bank));
        // Add items and their cooking times to a map		
		addFood(FOOD_ITEMS.chicken, new Food("chicken", 6, 6, 5, 10, 10)); // item, cookingTime, amount, low, capacity, price, orderSate
		addFood(FOOD_ITEMS.pizza, new Food("pizza", 9, 6, 5, 10, 12));
		addFood(FOOD_ITEMS.salad, new Food("salad", 3, 6, 5, 10, 6));
		addFood(FOOD_ITEMS.steak, new Food("steak", 12, 6, 5, 10, 16));        
        setCash(800);
	}
    
    // Messages
    
    // Scheduler
	
	// Actions
	
	// Getters
	public BankCustomer getBankCustomer() {
		return bankCustomer;
	}

	@Override
	public HashMap<FOOD_ITEMS, Food> getFoods() {
		return foods;
	}
	
	// Setters
	
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
        	foods.get(s).setAmount(foods.get(s).getAmount() + receivedItems.get(s));
        }		
	}
	
	/**
	 * Set food item quantity explicitly (update)
	 */
	@Override
	public void setFoodQuantity(FOOD_ITEMS f, int i) {
		foods.get(f).setAmount(i);
	}

	public void setBankCustomer(BankCustomer bankCustomer) {
		this.bankCustomer = bankCustomer;
	}	
}
