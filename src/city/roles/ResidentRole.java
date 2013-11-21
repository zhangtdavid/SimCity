package city.roles;

import city.Role;
import city.interfaces.Landlord;
import city.interfaces.Resident;

public class ResidentRole extends Role implements Resident {
	
	// Data
	
	ResidentState rstate = ResidentState.none;
	Landlord landlord;
	
	// Constructor
	
	public ResidentRole(){
		super();
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
	
	public void payMaintenance() {
		
	}
	
	public void payRent() {
		
	}
	
	// Getters
	
	// Setters

	// Utilities

	// Classes
}
