package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import trace.AlertLog;
import trace.AlertTag;
import city.Role;
import city.buildings.ResidenceBaseBuilding;
import city.interfaces.Landlord;
import city.interfaces.Resident;

public class LandlordRole extends Role implements Landlord {

	// Data
	
	private Resident landlordRes; // which of the residents is the landlord? enables easy handing-off of lordship
	private ResidenceBaseBuilding residence; // the landlord is the landlord of this building
	private List<Resident> residents = Collections.synchronizedList(new ArrayList<Resident>());
	
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
	public void setRent(int d) {
		residence.setRent(d);		
	}
	
	@Override
	public void setActive(){
		super.setInactive();
	}
	
	@Override
	public void setResidence(ResidenceBaseBuilding b) {
		residence = b; // landlord knows where he lives
		b.landlord = this; // building knows who the landlord is
	}
	
	// Utilities
	
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

	// Utilities
	
	@Override
	public void print(String msg) {
        super.print(msg);
        AlertLog.getInstance().logMessage(AlertTag.HOUSE, "LandlordRole " + this.getPerson().getName(), msg);
    }
	
	// Classes
	
}
