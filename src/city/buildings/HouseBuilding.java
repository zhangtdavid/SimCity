package city.buildings;

import java.util.HashMap;
import java.util.Map;

import city.Animation;
import city.RoleInterface;
import city.abstracts.ResidenceBuildingBase;
import city.animations.HouseResidentAnimation;
import city.gui.buildings.HousePanel;
import city.gui.views.CityViewBuilding;
import city.interfaces.House;
import city.interfaces.Landlord;
import city.interfaces.Person;
import city.interfaces.Resident;

public class HouseBuilding extends ResidenceBuildingBase implements House {

	// Data
	private HousePanel panel; // reference to main gui
	public Map<Person, Animation> allPersons = new HashMap<Person, Animation>();

	// Constructor
	
	public HouseBuilding(String name, Landlord l, HousePanel panel, CityViewBuilding cityBuilding) {
		super(name, panel, cityBuilding);
		//Perhaps I should eliminate the landlord requirement, and have that be added separately? :/
				this.setLandlord(l); // = WHO YOU PAY RENT TO. MIGHT NOT LIVE HERE
				// this.landlord.setResidence(this); 
				// keep^ commented if landlord != one of the residents is an option
				this.setHomeAnimationName("city.animations.RestaurantChoiCustomerAnimation");
				this.panel = panel;
				this.setCityViewBuilding(cityBuilding); 
	}

	// Getters

	// Setters

	/**
	 * This adds a Resident to a list of residents who live in this house. (1
	 * person lives in this house)
	 */
	@Override
	public void addResident(Resident resident) {
		if (residents.isEmpty()) {
			// ONLY ONE PERSON PER HOUSE~!
			this.residents.add(resident);
		} else {
			// System.out.println("Someone already lives in this house (capacity = 1)");
		}
	}

	// Utilities

	@Override
	public void addOccupyingRole(RoleInterface ri) {
		// This doesn't apply for HouseBuilding because the PersonAgent acts as
		// a PersonAgent in residences, not as Residents. 
	}

	/**
	 * Needed to have Person p instead of RoleInterface because it's not a
	 * Resident that's doing all the cooking and sleeping, it's a Person.
	 * 
	 * @param p
	 *            The person to move within the house.
	 */
	@Override
	public void addOccupyingPerson(Person p) {
		HouseResidentAnimation anim = new HouseResidentAnimation(p);
		p.setHomeAnimation(anim);
		anim.setVisible(true);
		panel.addVisualizationElement(anim);
		
	}
}
