package city.buildings;

import java.util.HashMap;

import city.Application.FOOD_ITEMS;
import city.bases.ResidenceBuilding;
import city.buildings.interfaces.Apt;
import city.gui.exteriors.CityViewBuilding;
import city.gui.interiors.AptPanel;
import city.roles.interfaces.Landlord;
import city.roles.interfaces.Resident;

public class AptBuilding extends ResidenceBuilding implements Apt {

	// Data
	
	public AptPanel panel;
	public final static int NUMBER_OF_BEDS = 5;

	// Constructor

	public AptBuilding(String name, Landlord l, AptPanel panel, CityViewBuilding cityBuilding) {
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

	@Override
	public void addResident(Resident r) {
		if(residents.size() < NUMBER_OF_BEDS) {
			residents.add(r);
			HashMap<FOOD_ITEMS, Integer> items = new HashMap<FOOD_ITEMS, Integer>();
			items.put(FOOD_ITEMS.salad, 1);
			items.put(FOOD_ITEMS.chicken, 1);
			items.put(FOOD_ITEMS.steak, 1);
			items.put(FOOD_ITEMS.pizza, 1); // should we be putting 1 food item for each person on addition? TODO
			this.setFood(r.getPerson(), items);
			r.getPerson().setRoomNumber(residents.size()); // could be one of 1~5 inclusive\
		}else{
			r.getPerson().print("This apartment is full (5 residents); cannot live here right now");
		}
	}
	
}
