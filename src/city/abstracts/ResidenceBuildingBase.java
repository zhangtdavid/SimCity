package city.abstracts;

import java.util.*;

import city.Building;
import city.Application.FOOD_ITEMS;
import city.interfaces.Landlord;
import city.interfaces.Resident;

public abstract class ResidenceBuildingBase extends Building implements ResidenceBuildingInterface {
	
	// Data

	private Landlord landlord;
	private int rent = 5;
	private int totalCurrentMaintenance = 0;
	private Map<FOOD_ITEMS, Integer> foodItems = new HashMap<FOOD_ITEMS, Integer>();
	protected List<Resident> residents = Collections.synchronizedList(new ArrayList<Resident>());
	
	// Constructor 
	
	public ResidenceBuildingBase(String name) {
		super(name);
		
		// TODO should we really be giving every home one of every food item upon instantiation?
        for (FOOD_ITEMS s: FOOD_ITEMS.values()) {
        	foodItems.put(s, 1); // Add delivered food items to inventory
        }	
    }

	// Getters
	
	@Override
	public Landlord getLandlord() {
		return landlord;
	}
	
	@Override
	public List<Resident> getResidents() {
		return residents;
	}
	
	@Override
	public int getTotalCurrentMaintenance() {
		return totalCurrentMaintenance;
	}
	
	@Override
	public int getRent() {
		return rent;
	}
	
	@Override
	public Map<FOOD_ITEMS, Integer> getFoodItems() {
		return foodItems;
	}

	// Setters
	
	@Override
	public void setRent(int r) {
		this.rent = r;
	}
	
	@Override
	public void setLandlord(Landlord l) {
		this.landlord = l;
	}
	
	@Override
	public abstract void addResident(Resident r);
	
	@Override
	public void setTotalCurrentMaintenance(int m) {
		this.totalCurrentMaintenance = m;
	}
	
	@Override
	public void addFood(FOOD_ITEMS f, int i) {
		this.foodItems.put(f, (foodItems.get(f) + i));
	}

	// Utilities
	
	@Override
	public void addFood(Map<FOOD_ITEMS, Integer> receivedItems) {
        for (FOOD_ITEMS s: receivedItems.keySet()) {
        	foodItems.put(s, foodItems.get(s)+receivedItems.get(s)); // Add delivered food items to restaurant inventory
        }		
	}
}
