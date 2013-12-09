package city.tests.bases.mocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import city.Application.FOOD_ITEMS;
import city.agents.interfaces.Person;
import city.bases.interfaces.ResidenceBuildingInterface;
import city.roles.interfaces.Landlord;
import city.roles.interfaces.Resident;

public abstract class MockResidenceBuilding extends MockBuilding implements ResidenceBuildingInterface {

	// Data
	
	private Landlord landlord;
	private int rent = 5;
	private int totalCurrentMaintenance = 0;
	protected List<Resident> residents = Collections.synchronizedList(new ArrayList<Resident>());
	private HashMap<Person, HashMap<FOOD_ITEMS, Integer>> allFoodItems = new HashMap<Person, HashMap<FOOD_ITEMS, Integer>>();
	
	// Constructor

	public MockResidenceBuilding(String name) {
		super(name);
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
		// TODO Auto-generated method stub
	}

	// Utilities
	
	@Override
	public HashMap<FOOD_ITEMS, Integer> getFoodItems(Person p) {
		return allFoodItems.get(p);
	}
	
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
	public boolean removeFood(Person p, FOOD_ITEMS f, int i) {
		return true;
	}

	@Override
	public void removeResident(Resident r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFood(Person p, HashMap<FOOD_ITEMS, Integer> items) {
		this.allFoodItems.put(p, items);
	}
}
