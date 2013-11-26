package city.buildings;

import java.util.*;

import city.Animation;
import city.Application.FOOD_ITEMS;
import city.Role;
import city.animations.RestaurantZhangCookAnimation;
import city.animations.RestaurantZhangCustomerAnimation;
import city.animations.RestaurantZhangWaiterAnimation;
import city.gui.HousePanel;
import city.interfaces.Landlord;
import city.interfaces.Resident;
import city.roles.ResidentRole;
import city.roles.RestaurantZhangCashierRole;
import city.roles.RestaurantZhangCookRole;
import city.roles.RestaurantZhangCustomerRole;
import city.roles.RestaurantZhangHostRole;
import city.roles.RestaurantZhangWaiterRegularRole;
import city.roles.RestaurantZhangWaiterSharedDataRole;

public class HouseBuilding extends ResidenceBaseBuilding {
	
	// Data
	HousePanel panel;
	public final static int NUMBER_OF_BEDS = 1;
	Map<Role, Animation> allRoles = new HashMap<Role, Animation>();
	// Constructor
	
	public HouseBuilding(String name, Landlord landlord, HousePanel p) {
		super(name);
		this.panel = p;
		this.landlord = landlord; // THIS IS WHO YOU PAY RENT TO. HE MIGHT NOT LIVE HERE.
		//this.landlord.setResidence(this); // relevant if landlord != resident 
		this.setResidentRole("city.roles.ResidentRole"); // inferred by extrapolation... lol
		this.setResidentAnimation("city.animations.ResidentAnimation");
	}

	public Landlord getLandlord() {
		return landlord;
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public int getRent() {
		return rent;
	}

	public int getTotal_current_maintenance() {
		return total_current_maintenance;
	}

	public void setLandlord(Landlord landlord) {
		this.landlord = landlord;
	}
	public void setTotal_current_maintenance(int total_current_maintenance) {
		this.total_current_maintenance = total_current_maintenance;
	}
	
	public void setRent(int rent) {
		this.rent = rent;
	}
	
	public void addResident(Resident resident) {
		if(residents.isEmpty()) //ONLY ONE PERSON PER HOUSE
			this.residents.add(resident); 
	}
	public Role addRole(Role r) {
		if(r instanceof ResidentRole) {
			ResidentRole c = (ResidentRole)r;
			if(!allRoles.containsKey(c)) {
				//TODO add personAnimation or something here for going around in the house.
				c.setAnimation(anim);
				anim.isVisible = true;
				panel.addVisualizationElement(anim);
				residents.add(c);
				allRoles.put(c, anim);
			}
			return c;
		}
		return null;
	}

	@Override
	public void addFood(FOOD_ITEMS f, int toadd) {
		foodItems.put(f, foodItems.get(f)+toadd);
	}
	
}
