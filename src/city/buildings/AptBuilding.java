package city.buildings;

import java.util.HashMap;
import java.util.Map;

import city.agents.interfaces.Person;
import city.animations.AptResidentAnimation;
import city.bases.Animation;
import city.bases.ResidenceBuilding;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.Apt;
import city.gui.exteriors.CityViewBuilding;
import city.gui.interiors.AptPanel;
import city.roles.interfaces.Landlord;
import city.roles.interfaces.Resident;

public class AptBuilding extends ResidenceBuilding implements Apt {

	// Data
	public AptPanel panel; //reference to main gui
	public Map<Person, Animation> allPersons = new HashMap<Person, Animation>();

	// Constructor

	public AptBuilding(String name, Landlord l, AptPanel panel, CityViewBuilding cityBuilding) {
		super(name, panel, cityBuilding);
		this.setLandlord(l);
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

	@Override
	public void addResident(Resident r) {
		if(residents.size() < Apt.NUMBER_OF_BEDS) {
			residents.add(r);
			r.getPerson().setRoomNumber(residents.size()); // could be one of 1~5 inclusive\
		}else{
			r.getPerson().print("This apartment is full (5 residents); cannot live here right now");
		}
	}

	// Utilities

	@Override
	public void addOccupyingRole(RoleInterface r) {
		// you can put any role the person has into this for house; I just get the person through it.
		if(!this.allPersons.containsKey(r.getPerson())){ // this prevents duplicates
			addOccupyingPerson(r.getPerson());
		}
		// if you already are in this home, just use the one you have before!
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
//		AptResidentAnimation anim = new AptResidentAnimation(p); // this is disposed of every time the person leaves.
//		p.setHomeAnimation(anim); // set the person's home animation to this.
//		anim.setVisible(true); // set visible the animation. the animation's init. pos. is HDX/HYX.
//		allPersons.put(p, anim);
//		if(!anim.getBeingTested())
//			panel.addVisualizationElement(anim);
	}
	
}
