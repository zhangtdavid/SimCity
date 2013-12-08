package city.buildings;

import java.util.HashMap;

import city.Application.FOOD_ITEMS;
import city.bases.ResidenceBuilding;
import city.buildings.interfaces.House;
import city.gui.exteriors.CityViewBuilding;
import city.gui.interiors.HousePanel;
import city.roles.interfaces.Landlord;
import city.roles.interfaces.Resident;

public class HouseBuilding extends ResidenceBuilding implements House {

	// Data
	
	private HousePanel panel;
	public static final int NUMBER_OF_BEDS = 1;

	// Constructor

	public HouseBuilding(String name, Landlord l, HousePanel panel, CityViewBuilding cityBuilding) {
		super(name, panel, cityBuilding); 
		this.setLandlord(l); // WHO YOU PAY RENT TO. MIGHT NOT LIVE HERE // TODO Perhaps I should eliminate the landlord requirement, and have that be added separately?
		this.panel = panel;
		this.setCityViewBuilding(cityBuilding); 
	}

	// Getters
	
	@Override
	public boolean getIsFull() {
		return !residents.isEmpty();
	}
	
	// Setters

	// Utilities
	
	/**
	 * This adds a Resident to a list of residents who live in this house. (only 1 person may live in a house)
	 */
	@Override
	public void addResident(Resident r) {
		if (residents.isEmpty()) {
			// ONLY ONE PERSON PER HOUSE~!
			this.residents.add(r);
			HashMap<FOOD_ITEMS, Integer> items = new HashMap<FOOD_ITEMS, Integer>();
			items.put(FOOD_ITEMS.salad, 1);
			items.put(FOOD_ITEMS.chicken, 1);
			items.put(FOOD_ITEMS.steak, 1);
			items.put(FOOD_ITEMS.pizza, 1); // should we be putting 1 food item for each person on addition? TODO
			this.setFood(r.getPerson(), items);
		} else {
			throw new IllegalStateException("Only one person at a time may live in a house.");
		}
	}

}
