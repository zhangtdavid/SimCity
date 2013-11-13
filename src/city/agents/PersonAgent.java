package city.agents;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import city.Agent;
import city.Role;
import city.interfaces.Person;

public class PersonAgent extends Agent implements Person {
	
	// Data
	
	private Date date;
	
	public List<Role> roles = new ArrayList<Role>();
	
	// Constructor
	
	public PersonAgent(Date d) {
		super();
		date = d;
	}
	
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
	
	@Override
	public void setDate(Date d) {
		date = d;
		stateChanged();
	}
	
	// Utilities
	
	public void addRole(Role r) {
		roles.add(r);
		r.setPerson(this);
	}
	
	// Classes

}
