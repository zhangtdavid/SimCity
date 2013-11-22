package city.roles;

import java.util.Collections;
import java.util.List;

import city.interfaces.Landlord;
import city.interfaces.Resident;

public class LandlordRole extends ResidentRole implements Landlord{

	// Data
	// Murphy's Law: Anything that can go wrong will go wrong.
	final double murphyProbability = 0.01; // odds of a house needing maintenance per murphyInterval
	final int murphyInterval = 100; // # of clock ticks per checking if house needs maintenance
	
	List<Resident> residents = Collections.synchronizedList(null);
	
	// Constructor
	public LandlordRole(){
		super();
	}
	
	// Messages
	@Override
	public void msgHeresRent(double d) {
		//this_person.addMoney(d);
	}

	@Override
	public void msgHeresMaintenanceFee(double d) {
		//this_person.addMoney(d);
	}

	@Override
	public void msgFoundProblem() {
		//maintenanceFee= 15+Math.rand()*100;
	}
	
	
	// Scheduler

	@Override
	public boolean runScheduler() {
		// TODO Auto-generated method stub
		return false;
	}


	// Actions

	// Getters

	// Setters

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
	// Classes
}
