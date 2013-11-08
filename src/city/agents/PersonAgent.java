package city.agents;

import java.util.List;

import base.Agent;
import base.Role;
import city.interfaces.Person;

public class PersonAgent extends Agent implements Person {
	
	// Data
	
	public List<Role> roles;
	
	// Constructor
	
	// Messages
	
	// Scheduler
	
	@Override
	protected boolean runScheduler() {
		// Central Scheduler
		
		// Role Scheduler
		Boolean blocking = false;
		for (Role r : roles) if (r.active) {
			blocking  = true;
			r.runScheduler();
			break;
		}
		
		// Scheduler disposition
		return blocking;
	}
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	public void addRole(Role r) {
		roles.add(r);
		r.setPerson(this);
	}
	
	// Classes

}
