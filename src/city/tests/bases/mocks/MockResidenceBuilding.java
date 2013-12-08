package city.tests.bases.mocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private Map<Person, Map<FOOD_ITEMS, Integer>> allFoodItems = new HashMap<Person, Map<FOOD_ITEMS, Integer>>();
	
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
	public void setFood(Person p, Map<FOOD_ITEMS, Integer> items) {
		this.allFoodItems.put(p, items);
	}

	@Override
	public void setTotalCurrentMaintenance(int m) {
		// TODO Auto-generated method stub
		
	}

	// Utilities
	
	@Override
	public void addResident(Resident r) {
		// TODO Auto-generated method stub
		
	}
}
