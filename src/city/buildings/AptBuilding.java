package city.buildings;

import java.util.*;
import city.Building;
import city.Role;
import city.interfaces.Landlord;
import city.interfaces.Resident;

public class AptBuilding extends ResidenceBaseBuilding{
	
	public AptBuilding(String name, Landlord landlord) { 
		super(name); // set building name
		this.landlord = landlord;
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
