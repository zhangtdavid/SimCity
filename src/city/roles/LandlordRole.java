package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import city.Role;
import city.buildings.ResidenceBaseBuilding;
import city.interfaces.Landlord;
import city.interfaces.Resident;

public class LandlordRole extends Role implements Landlord {

	// Data
	// Murphy's Law: Anything that can go wrong will go wrong.
	Resident landlordRes; // which of the residents is the landlord? enables easy handing-off of lordship
	ResidenceBaseBuilding residence; // the landlord is the landlord of this building

	List<Resident> residents = Collections.synchronizedList(new ArrayList<Resident>());
	
	// Constructor
	public LandlordRole(){
		super();
	}
	
	// Messages
	@Override
	public void msgHeresRent(int d) {
		this.getPerson().setCash(this.getPerson().getCash()+d);
	}
	
	// Scheduler

	@Override
	public boolean runScheduler() { // landlord just collects money. 
		return false;
	}


	// Actions

	// Getters

	// Setters
	@Override
	public void setActive(){
//		if(residence.total_current_maintenance != 0){
//			
//		}
		super.setInactive();
	}
	
	@Override
	public void addResident(Resident r) {
		synchronized(residents){
			residents.add(r);	
		}
	}

	@Override
	public void removeResident(Resident r) {
		synchronized(residents){
			residents.remove(r);
		}
	}
	
	@Override
	public void changeLandlord(Resident r) {
		landlordRes = r;
	}

	@Override
	public void setRent(int d) {
		residence.setRent(d);		
	}

	@Override
	public void setResidence(ResidenceBaseBuilding b) {
		residence = b; // landlord knows where he lives
		b.landlord = this; // building knows who the landlord is
	}

	// Utilities

	// Classes

}
