package city.buildings;

import city.agents.interfaces.Person;
import city.bases.ResidenceBuilding;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.Apartment;
import city.gui.exteriors.CityViewBuilding;
import city.gui.interiors.AptPanel;
import city.roles.interfaces.Landlord;
import city.roles.interfaces.Resident;

public class ApartmentBuilding extends ResidenceBuilding implements Apartment {
	
	// Data
	public AptPanel panel; //reference to main gui

	// Constructor

	public ApartmentBuilding(String name, Landlord l, AptPanel panel, CityViewBuilding cityBuilding) {
		super(name, panel, cityBuilding);
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
		// NOTHING TO BE DONE HERE
	}

	@Override
	public void addOccupyingPerson(Person p) {
		//TODO AptResidentAnimation anim = new AptResidentAnimation(p);
		//p.setAnimation(anim);
		//anim.setVisible(true);
		//panel.addVisualizationElement(anim);
		
	}
	
}
