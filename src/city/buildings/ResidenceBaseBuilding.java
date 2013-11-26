package city.buildings;

import java.util.*;

import city.Building;
import city.Application.FOOD_ITEMS;
import city.interfaces.Landlord;
import city.interfaces.Resident;

public abstract class ResidenceBaseBuilding extends Building{

	public Landlord landlord;
	public List<Resident> residents = Collections.synchronizedList(new ArrayList<Resident>());
	public int rent = 5;
	public int total_current_maintenance = 0;
	public Map<FOOD_ITEMS, Integer> foodItems = new HashMap<FOOD_ITEMS, Integer>();

	public ResidenceBaseBuilding(String name) {
		super(name);
        for (FOOD_ITEMS s: FOOD_ITEMS.values()) {
        	foodItems.put(s, 1); // Add delivered food items to restaurant inventory
        }	
    }

	//getters abstract
	public abstract Landlord getLandlord();
	public abstract List<Resident> getResidents();
	public abstract int getTotal_current_maintenance();
	public abstract int getRent();

	//setters abstract
	public abstract void setRent(int i);
	public abstract void setLandlord(Landlord landlord);
	public abstract void addResident(Resident resident);
	public abstract void setTotal_current_maintenance(int i);
	public abstract void addFood(FOOD_ITEMS f, int toadd);

	public void addFood(Map<FOOD_ITEMS, Integer> receivedItems) {
        for (FOOD_ITEMS s: receivedItems.keySet()) {
        	foodItems.put(s, foodItems.get(s)+receivedItems.get(s)); // Add delivered food items to restaurant inventory
        }		
	}
}
