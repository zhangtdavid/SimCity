package city.bases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import city.Application.FOOD_ITEMS;
import city.agents.interfaces.Person;
import city.bases.interfaces.ResidenceBuildingInterface;
import city.bases.interfaces.RoleInterface;
import city.gui.BuildingCard;
import city.gui.exteriors.CityViewBuilding;
import city.roles.interfaces.Landlord;
import city.roles.interfaces.Resident;

/**
 * This inherits building, but adds functionality for Person Animations within frames.
 */
public abstract class ResidenceBuilding extends Building implements ResidenceBuildingInterface {
	
	// Data
	
	private Landlord landlord;
	private int rent = 5;
	private int totalCurrentMaintenance = 0;
	private Map<Person, Map<FOOD_ITEMS, Integer>> allFoodItems= new HashMap<Person, Map<FOOD_ITEMS, Integer>>();
	protected List<Resident> residents = Collections.synchronizedList(new ArrayList<Resident>());
	protected Map<Person, Animation> allPersons = new HashMap<Person, Animation>();

	// Constructor 
	
	public ResidenceBuilding(String name, BuildingCard panel ,CityViewBuilding cityBuilding) {
		super(name, panel, cityBuilding);
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
	public Map<FOOD_ITEMS, Integer> getFoodItems(Person p) {
		return allFoodItems.get(p);
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
	public void setTotalCurrentMaintenance(int m) {
		this.totalCurrentMaintenance = m;
	}

	@Override
	public void setFood(Person p, Map<FOOD_ITEMS, Integer> items) {
		this.allFoodItems.put(p, items);
	}
	
	// Utilities
	
	@Override
	public abstract void addResident(Resident r);
	
	@Override
	public void removeFood(Person p, FOOD_ITEMS f, int i) {
		Map<FOOD_ITEMS, Integer> temp = allFoodItems.get(p);
		temp.put(f, temp.get(f)-i); // -i from the choice
		allFoodItems.put(p, temp); // put in the new map into allFoodItems
	}
	
	@Override
	public void addOccupyingRole(RoleInterface r) {
		throw new IllegalArgumentException("You shouldn't do this on ResidenceBuildings");
	}

}
