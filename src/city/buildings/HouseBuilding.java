package city.buildings;

import city.RoleInterface;
import city.abstracts.ResidenceBuildingBase;
import city.animations.HouseResidentAnimation;
import city.gui.buildings.HousePanel;
import city.interfaces.House;
import city.interfaces.Landlord;
import city.interfaces.Person;
import city.interfaces.Resident;

public class HouseBuilding extends ResidenceBuildingBase implements House {
	
	// Data
	private HousePanel panel; //reference to main gui

	// Constructor
	
	public HouseBuilding(String name, Landlord l, HousePanel p) {
		super(name);
		this.setLandlord(l); // THIS IS WHO YOU PAY RENT TO. HE MIGHT NOT LIVE HERE.
		// this.landlord.setResidence(this); // keep commented if landlord != resident is an option 
	}
	
	// Getters

	// Setters
	
	/**
	 * This adds a Resident to a list of residents who live in this house. (1 person lives in this house)
	 */
	@Override
	public void addResident(Resident resident) {
		if(residents.isEmpty()) {
			//ONLY ONE PERSON PER HOUSE
			this.residents.add(resident); 
		}
		else{
			//System.out.println("Someone already lives in this house (capacity = 1)");
		}
	}		
	
	// Utilities
	
	@Override
	public void addOccupyingRole(RoleInterface ri) {
//		This doesn't apply for HouseBuilding because the PersonAgent acts as a PersonAgent in residences, not as Residents.
//		if(ri == null) { // if ri is a person with no roles (as this should be)
//			if(!super.occupyingRoleExists(r)) {
//				RestaurantChoiCustomerAnimation anim = new RestaurantChoiCustomerAnimation(c); 
//				r.setGui(anim);	
//				anim.setVisible(true);
//				panel.addVisualizationElement(anim);
//				customers.add(c);
//				super.addOccupyingRole(c, anim);
//			}
//		}
	}
	
	/**
	 * Needed to have Person p instead of RoleInterface because it's not a Resident that's doing all the cooking and sleeping, it's a Person.
	 * @param p The person to move within the house.
	 */
	@Override
	public void addOccupyingRole(Person p){
		HouseResidentAnimation anim = new HouseResidentAnimation(p);
		p.setAnimation(anim);
		anim.setVisible(true);
		panel.addVisualizationElement(anim);
	}
	
}
