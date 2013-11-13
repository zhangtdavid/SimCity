package city.agents;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

import city.Agent;
import city.Role;
import city.interfaces.Person;

public class PersonAgent extends Agent implements Person {
	
	// Data
	
	private Date date;
	private Role occupation;
	private Agent car; // TODO replace this with the appropriate object type
	private List<Role> roles = new ArrayList<Role>();
	private Semaphore atDestination = new Semaphore(0, true);
	private city.animations.interfaces.Person animation;
	private enum State {none, goingToOccupation, goingToBank, goingToRestaurant, goingToMarket, goingHome, atOccupation, atBank, atRestaurant, atMarket, atHome, leavingOccupation };
	private State state; 
	
	// Constructor
	
    /**
     * Create a Person.
     * 
     * @param startDate the current simulation Date
     * @param occupation the single Role that is the Person's occupation
     */
	public PersonAgent(Date startDate) {
		super();
		this.date = startDate;
	}
	
	// Messages
	
	@Override
	public void guiAtDestination() {
		atDestination.release();
	}
	
	// Scheduler
	
	@Override
	protected boolean runScheduler() {
		// Central Scheduler
		
		// Role Scheduler
		Boolean blocking = false;
		for (Role r : roles) if (r.getActive()) {
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
	
	@Override
	public void setOccupation(Role r) {
		occupation = r;
		addRole(r);
	}
	
	@Override
	public void setAnimation(city.animations.interfaces.Person p) {
		animation = p;
	}

	@Override
	public void setCar(Agent c) {
		car = c;
	}
	
	// Utilities
	
	@Override
	public void addRole(Role r) {
		roles.add(r);
		r.setPerson(this);
	}
	
	// Classes

}
