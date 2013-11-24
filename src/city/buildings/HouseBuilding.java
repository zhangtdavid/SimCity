package city.buildings;

import java.util.*;

import city.interfaces.Landlord;
import city.interfaces.Resident;

public class HouseBuilding extends ResidenceBaseBuilding{
	
	public HouseBuilding(String name, Landlord landlord) { // why not Landlord landlord?
		super(name);
		this.landlord = landlord; // that'd eliminate the need for this cast
		this.landlord.setResidence(this);
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
	
	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

}