package city.buildings;

import java.util.HashMap;
import java.util.Map;

import city.agents.interfaces.Person;
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

	// Utilities
	
	/**
	 * This adds a Resident to a list of residents who live in this house. (only 1 person may live in a house)
	 */
	@Override
	public void addResident(Resident r) {
		if (residents.isEmpty()) {
			this.residents.add(r);
		} else {
			throw new IllegalStateException("Only one person at a time may live in a house.");
		}
	}
	
	@Override
	public void removeResident(Resident r) {
		this.residents.remove(r);
	}

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
	 * @param p The person to move within the house.
	 */
	@Override
	public void addOccupyingPerson(Person p) {
//		HouseResidentAnimation anim = new HouseResidentAnimation(p); // this is disposed of every time the person leaves.
//		p.setHomeAnimation(anim); // set the person's home animation to this.
//		anim.setVisible(true); // set visible the animation. the animation's init. pos. is HDX/HYX.
//		allPersons.put(p, anim);
//		if(!anim.getBeingTested())
//			panel.addVisualizationElement(anim);
	}

}
