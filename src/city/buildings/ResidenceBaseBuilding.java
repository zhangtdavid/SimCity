package city.buildings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import city.Application.FOOD_ITEMS;
import city.Building;
import city.interfaces.Landlord;
import city.interfaces.Resident;

public abstract class ResidenceBaseBuilding extends Building{

	// Data
	
	public Landlord landlord;
	public List<Resident> residents = Collections.synchronizedList(new ArrayList<Resident>());
	public int rent = 5;
	public int total_current_maintenance = 0;
	public Map<FOOD_ITEMS, Integer> foodItems = new HashMap<FOOD_ITEMS, Integer>();

	// Constructor
	
	public ResidenceBaseBuilding(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	// Getters
	
	public abstract Landlord getLandlord();
	public abstract List<Resident> getResidents();
	public abstract int getTotal_current_maintenance();
	public abstract int getRent();

	// Setters
	
	public abstract void setRent(int i);
	public abstract void setLandlord(Landlord landlord);
	public abstract void setResidents(List<Resident> residents);
	public abstract void setTotal_current_maintenance(int i);

	// Utilities
	
	public void addFood(Map<FOOD_ITEMS, Integer> receivedItems) {
        for (FOOD_ITEMS s: receivedItems.keySet()) {
        	foodItems.put(s, foodItems.get(s)+receivedItems.get(s)); // Add delivered food items to refrigerator inventory
        }		
	}
}
