package city.roles;

import java.util.List;

import trace.AlertLog;
import trace.AlertTag;
import city.bases.ResidenceBuilding;
import city.bases.Role;
import city.roles.interfaces.Landlord;

public class LandlordRole extends Role implements Landlord {

	// Data
	
	private List<ResidenceBuilding> residencesManaged; // the landlord is the landlord of this building
	
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
	
	/**
	 * Sets rent to to int d for all residences the landlord manages.
	 */
	@Override
	public void setRent(int d) {
		for(int i = 0; i < residencesManaged.size(); i++)
		residencesManaged.get(i).setRent(d);		
	}
	
	/**
	 * No real reason to set Landlords active
	 */
	@Override
	public void setActive(){
		super.setInactive();
	}
	
	// Utilities

	@Override
	public void print(String msg) {
		this.getPerson().printViaRole("Landlord", msg);
        AlertLog.getInstance().logMessage(AlertTag.HOUSE, "LandlordRole " + this.getPerson().getName(), msg);
    }

	@Override
	/**
	 * Adds a ResidenceBuilding to the Landlord's list of buildings he owns.   
	 */
	public void addResidence(ResidenceBuilding b) {
		residencesManaged.add(b);
		b.setLandlord(this);
	}

	@Override
	/**
	 * Removes a ResidenceBuilding from the Landlord's list of buildings he owns.   
	 */
	public void removeResidence(ResidenceBuilding b) {
		residencesManaged.remove(b);
	}
	
}
