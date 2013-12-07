package city.buildings;

import java.util.HashMap;
import java.util.Map;

import city.agents.interfaces.Person;
import city.animations.HouseResidentAnimation;
import city.bases.Animation;
import city.bases.ResidenceBuilding;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.House;
import city.gui.exteriors.CityViewBuilding;
import city.gui.interiors.HousePanel;
import city.roles.interfaces.Landlord;
import city.roles.interfaces.Resident;

public class HouseBuilding extends ResidenceBuilding implements House {

	// Data
	private HousePanel panel; // reference to main gui
	public Map<Person, Animation> allPersons = new HashMap<Person, Animation>();

	// Constructor

	public HouseBuilding(String name, Landlord l, HousePanel panel, CityViewBuilding cityBuilding) {
		super(name, panel, cityBuilding);
		//Perhaps I should eliminate the landlord requirement, and have that be added separately? :/
		this.setLandlord(l); // = WHO YOU PAY RENT TO. MIGHT NOT LIVE HERE
		this.setHomeAnimationName("city.animations.RestaurantChoiCustomerAnimation"); // TODO why?
		this.panel = panel;
		this.setCityViewBuilding(cityBuilding); 
	}

	// Getters
	@Override
	public boolean getIsFull() {
		return !residents.isEmpty();
	}
	
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
		// you can put any role the person has into this for house; I just get the person through it.
		if(!this.allPersons.containsKey(ri.getPerson())){ // this prevents duplicates
			addOccupyingPerson(ri.getPerson()); // if you already are in this home, just use the one you have before!
		}
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
		HouseResidentAnimation anim = new HouseResidentAnimation(p); // this is disposed of every time the person leaves.
		p.setHomeAnimation(anim); // set the person's home animation to this.
		anim.setVisible(true); // set visible the animation. the animation's init. pos. is HDX/HYX.
		allPersons.put(p, anim);
		if(!anim.getBeingTested())
			panel.addVisualizationElement(anim);

	}

}
