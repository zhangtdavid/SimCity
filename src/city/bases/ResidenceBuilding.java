package city.bases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import city.Application.FOOD_ITEMS;
import city.agents.interfaces.Person;
import city.bases.interfaces.AnimationInterface;
import city.bases.interfaces.ResidenceBuildingInterface;
import city.gui.BuildingCard;
import city.gui.exteriors.CityViewBuilding;
import city.roles.interfaces.Landlord;
import city.roles.interfaces.Resident;

public abstract class ResidenceBuilding extends Building implements ResidenceBuildingInterface {
	//This inherits building, but adds functionality for Person Animations within frames.
	
	// Data
	private Landlord landlord;
	private int rent = 5;
	private int totalCurrentMaintenance = 0;
	private Map<FOOD_ITEMS, Integer> foodItems = new HashMap<FOOD_ITEMS, Integer>();
	protected List<Resident> residents = Collections.synchronizedList(new ArrayList<Resident>());
	private String homeAnimationInterfaceName;     // The interface name of the animation that interacts with this building as a tenant
	private HashMap<Person, AnimationInterface> occupyingPersons = new HashMap<Person, AnimationInterface>(); //Since the animations in homes aren't role-bound, need a Person counterpart of above.

	
	// Constructor 
	
	public ResidenceBuilding(String name, BuildingCard panel ,CityViewBuilding cityBuilding) {
		super(name, panel, cityBuilding);
		
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
	
	@Override
	public String getHomeAnimationName() {
		return homeAnimationInterfaceName;
	}
	
	@Override
	public <T extends AnimationInterface> T getOccupyingPersonAnimation(Person r, Class<T> type) {
		return type.cast(occupyingPersons.get(r));
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
	public void setHomeAnimationName(String c){
		this.homeAnimationInterfaceName = c;
	}

	// Utilities
	
	@Override
	public abstract void addOccupyingPerson(Person p);
	
	@Override
	public void addOccupyingPerson(Person p, Animation a) {
		occupyingPersons.put(p, a);
	}
	
	@Override
	public void removeOccupyingPerson(Person r) {
		occupyingPersons.remove(r);
	}
	
	@Override
	public boolean occupyingPersonExists(Person p) {
		return occupyingPersons.containsKey(p);
	}
	
	@Override
	public void setFood(Map<FOOD_ITEMS, Integer> items) {
		this.foodItems = items;
	}

	@Override
	public void addFood(FOOD_ITEMS f, int i) {
		this.foodItems.put(f, (foodItems.get(f) + i));
	}
	
	@Override
	public void removeFood(FOOD_ITEMS f, int i) {
		this.foodItems.put(f, (foodItems.get(f) - i));
	}
	
	@Override
	public void addFood(Map<FOOD_ITEMS, Integer> receivedItems) {
        for (FOOD_ITEMS s: receivedItems.keySet()) {
        	foodItems.put(s, foodItems.get(s)+receivedItems.get(s)); // Add delivered food items to restaurant inventory
        }		
	}
	
	@Override
	public abstract void addResident(Resident r);
	
	@Override
	public abstract void removeResident(Resident r);

}
