package city.buildings;

import city.bases.ResidenceBuilding;
import city.buildings.interfaces.House;
import city.gui.exteriors.CityViewBuilding;
import city.gui.interiors.HousePanel;
import city.roles.interfaces.Landlord;
import city.roles.interfaces.Resident;

public class HouseBuilding extends ResidenceBuilding implements House {

	// Data
	
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
	
	@Override
	public void addResident(Resident r) {
		if (!residents.contains(r)) {
			if (residents.isEmpty()) {
				residents.add(r);
				super.addResident(r);
				r.getPerson().getAnimation().setVisible(true);
			} else {
				throw new IllegalStateException("Only one person at a time may live in a house.");
			}
		}
	}

}
