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
	private HashMap<Person, HashMap<FOOD_ITEMS, Integer>> allFoodItems= new HashMap<Person, HashMap<FOOD_ITEMS, Integer>>();
	protected List<Resident> residents = Collections.synchronizedList(new ArrayList<Resident>());
	protected Map<Person, Animation> allPersons = new HashMap<Person, Animation>();

	// Constructor 
	
	public ResidenceBuilding(String name, BuildingCard panel ,CityViewBuilding cityBuilding) {
		super(name, panel, cityBuilding);
    }

	// Getters
	
	@Override
	public boolean getBusinessIsOpen() {
		return false;
	}
	
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
	public HashMap<FOOD_ITEMS, Integer> getFoodItems(Person p) {
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
	public void setFood(Person p, HashMap<FOOD_ITEMS, Integer> items) {
		this.allFoodItems.put(p, items);
	}
	
	// Utilities
	
	@Override
	public void addResident(Resident r) {
		if (!residents.contains(r)) {
			throw new IllegalStateException("The base class's addResident() should not be called before the resident is added.");
		}
		
		HashMap<FOOD_ITEMS, Integer> items = new HashMap<FOOD_ITEMS, Integer>();
		items.put(FOOD_ITEMS.salad, 1);
		items.put(FOOD_ITEMS.chicken, 1);
		items.put(FOOD_ITEMS.steak, 1);
		items.put(FOOD_ITEMS.pizza, 1); 
		this.setFood(r.getPerson(), items);
		r.getPerson().setRoomNumber(residents.size());

	}
	
	@Override
	public void removeResident(Resident r) {
		residents.remove(r);
	}
	
	@Override
	public boolean removeFood(Person p, FOOD_ITEMS f, int i) {
		HashMap<FOOD_ITEMS, Integer> temp = allFoodItems.get(p);
		if(allFoodItems.get(p).get(f) != null){
			temp.put(f, temp.get(f)-i); // -i from the choice
			allFoodItems.put(p, temp); // put in the new map into allFoodItems
			return true;
		}else{
			p.print("Found no food in the refrigerator, that's weird. (CURSE YOU CONTROL PANEL)");
			return false;
		}
	}
	
	@Override
	public void addOccupyingRole(RoleInterface r) {
		throw new IllegalArgumentException("You shouldn't do this on ResidenceBuildings");
	}

}
