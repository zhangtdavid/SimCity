package city.buildings;

import java.util.*;
import city.Building;
import city.Role;
import city.interfaces.Landlord;
import city.interfaces.Resident;

public class ApartmentBuilding extends Building {
	public Landlord landlord;
	public List<Resident> residents = Collections.synchronizedList(new ArrayList<Resident>());
	public double rent = 5;
	public double total_current_maintenance = 0;
	public Date lastCheckedForMaintenance;
	
	public ApartmentBuilding(String name, Role landlord) { // why not Landlord landlord?
		super(name);
		this.landlord = (Landlord) landlord; // that'd eliminate the need for this cast
	
	}
}