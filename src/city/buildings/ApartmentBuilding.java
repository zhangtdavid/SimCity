package city.buildings;

import city.RoleInterface;
import city.abstracts.ResidenceBuildingBase;
import city.interfaces.Apartment;
import city.interfaces.Landlord;
import city.interfaces.Resident;

public class ApartmentBuilding extends ResidenceBuildingBase implements Apartment {
	
	// Data
	
	// Constructor

	public ApartmentBuilding(String name, Landlord l) { 
		super(name);
		this.setLandlord(l);
		// this.landlord.setResidence(this); // landlord != any of the residents
	}

	// Getters
	
	// Setters

	@Override
	public void addResident(Resident r) {
		if(residents.size() < Apartment.NUMBER_OF_BEDS) {
			residents.add(r);
		}
	}
	
	// Utilities
	
	@Override
	public void addOccupyingRole(RoleInterface r) {
		// TODO
		return;
	}
	
}
