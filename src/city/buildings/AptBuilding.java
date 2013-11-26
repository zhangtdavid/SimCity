package city.buildings;

import java.util.List;

import city.Application.FOOD_ITEMS;
import city.Role;
import city.interfaces.Landlord;
import city.interfaces.Resident;

public class AptBuilding extends ResidenceBaseBuilding{
	
	// Data

	public final static int NUMBER_OF_BEDS = 5;
	
	// Constructor

	public AptBuilding(String name, Landlord landlord) { 
		super(name); // set building name
		this.landlord = landlord;
		//this.landlord.setResidence(this); // landlord != any of the residents
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

	public void addResident(Resident res) {
		if(residents.size() < AptBuilding.NUMBER_OF_BEDS) residents.add(res);
	}

	@Override
	public void addFood(FOOD_ITEMS f, int toadd) {
		foodItems.put(f, foodItems.get(f)+toadd);
	}
	
	// Utilities
	
	@Override
	public void addRole(Role r) {
		// TODO
		return;
	}
	
}
