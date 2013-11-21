package city.roles;

import city.Role;
import city.agents.PersonAgent;
import city.interfaces.Landlord;
import city.interfaces.Person;
import city.interfaces.Resident;

public class ResidentRole extends Role implements Resident{
	// Data
	ResidentState rstate = ResidentState.none;
	Person this_person;
	Landlord landlord;
	
	// Constructor
	public ResidentRole(Person p){
		this_person = p;
	}
	
	// Messages
	@Override
	public void msgPayForMaintenance(double d) {
		
	}
	// Scheduler
	@Override
	public boolean runScheduler() {
		if(rstate == ResidentState.needToPayMaintenance){
			System.out.println("need to pay maintenance!");
			payMaintenance();
			return true;
		}
		if(rstate == ResidentState.needToPayRent){
			System.out.println("need to pay rent!");
			payRent();
			return true;
		}
		return false;
	}

	// Actions
	public void payMaintenance(){
		
	}
	
	public void payRent(){
		
	}
	// Getters
	
	// Setters

	// Utilities

	// Classes
}
