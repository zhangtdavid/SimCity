package city.buildings;

import java.util.List;
//import city.Animation;
import city.Application.FOOD_ITEMS;
import city.Role;
import city.gui.HousePanel;
import city.interfaces.Landlord;
import city.interfaces.Resident;

public class HouseBuilding extends ResidenceBaseBuilding {
	
	// Data
	
	public final static int NUMBER_OF_BEDS = 1;
	
	// Constructor
	
	public HouseBuilding(String name, Landlord landlord, HousePanel p) {
		super(name);
		this.landlord = landlord; // THIS IS WHO YOU PAY RENT TO. HE MIGHT NOT LIVE HERE.
		//this.landlord.setResidence(this); // keep commented if landlord != resident is an option 
	}
	
	// Getters

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
	
	// Setters

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
	
	// Utilities
	
	@Override
	public void addRole(Role r) {
		// TODO
		return;
	}

	@Override
	public void addFood(FOOD_ITEMS f, int toadd) {
		foodItems.put(f, foodItems.get(f)+toadd);
	}
	
}
