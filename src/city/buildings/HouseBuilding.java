package city.buildings;

import city.RoleInterface;
import city.abstracts.ResidenceBuildingBase;
import city.gui.buildings.HousePanel;
import city.gui.views.CityViewBuilding;
import city.interfaces.House;
import city.interfaces.Landlord;
import city.interfaces.Resident;

public class HouseBuilding extends ResidenceBuildingBase implements House {
	
	// Data
	
	public final static int NUMBER_OF_BEDS = 1;
	
	// Constructor
	
	public HouseBuilding(String name, Landlord l, HousePanel panel, CityViewBuilding cityBuilding) {
		super(name, panel, cityBuilding);
		this.setLandlord(l); // THIS IS WHO YOU PAY RENT TO. HE MIGHT NOT LIVE HERE.
		// this.landlord.setResidence(this); // keep commented if landlord != resident is an option 
	}
	
	// Getters

	// Setters
	
	@Override
	public void addResident(Resident resident) {
		if(residents.isEmpty()) {
			//ONLY ONE PERSON PER HOUSE
			this.residents.add(resident); 
		}
	}
	
	// Utilities
	
	@Override
	public void addOccupyingRole(RoleInterface r) {
		// TODO
		return;
	}
	
}
