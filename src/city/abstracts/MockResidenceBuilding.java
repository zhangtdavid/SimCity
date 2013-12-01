package city.abstracts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import city.Application.FOOD_ITEMS;
import city.interfaces.Landlord;
import city.interfaces.Resident;

public abstract class MockResidenceBuilding extends MockBuilding implements ResidenceBuildingInterface {

	// Data
	
	private Landlord landlord;
	private int rent = 5;
	private int totalCurrentMaintenance = 0;
	private Map<FOOD_ITEMS, Integer> foodItems = new HashMap<FOOD_ITEMS, Integer>();
	protected List<Resident> residents = Collections.synchronizedList(new ArrayList<Resident>());
	
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
	
	// Utilities

	@Override
	public void addResident(Resident r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTotalCurrentMaintenance(int m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addFood(FOOD_ITEMS f, int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addFood(Map<FOOD_ITEMS, Integer> receivedItems) {
		// TODO Auto-generated method stub
		
	}

}
