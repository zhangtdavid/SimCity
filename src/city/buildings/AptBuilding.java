package city.buildings;

import city.Role;
import city.abstracts.ResidenceBuildingBase;
import city.interfaces.Landlord;
import city.interfaces.Resident;

public class AptBuilding extends ResidenceBuildingBase{
	
	// Data

	public final static int NUMBER_OF_BEDS = 5;
	
	// Constructor

	public AptBuilding(String name, Landlord l) { 
		super(name);
		this.setLandlord(l);
		// this.landlord.setResidence(this); // landlord != any of the residents
	}

	// Getters
	
	// Setters

	@Override
	public void addResident(Resident r) {
		if(residents.size() < AptBuilding.NUMBER_OF_BEDS) {
			residents.add(r);
		}
	}
	
	// Utilities
	
	@Override
	public void addRole(Role r) {
		// TODO
		return;
	}
	
}
