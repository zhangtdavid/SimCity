package city.agents;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

import city.Agent;
import city.Role;
import city.interfaces.Car;
import city.interfaces.Person;

public class PersonAgent extends Agent implements Person {
	
	// Data
	
	private Date date;
	private Role occupation;
	private Car car;
	private String name;
	private List<Role> roles = new ArrayList<Role>();
	private Semaphore atDestination = new Semaphore(0, true);
	private city.animations.interfaces.AnimatedPerson animation;
	private enum State {none, goingToOccupation, goingToBank, goingToRestaurant, goingToMarket, goingHome, atOccupation, atBank, atRestaurant, atMarket, atHome, leavingOccupation };
	private State state; 
	
	// Constructor
	
    /**
     * Create a Person.
     * 
     * @param name the person's name
     * @param startDate the current simulation date
     */
	public PersonAgent(String name, Date startDate) {
		super();
		this.name = name;
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
		boolean blocking = false;
		for (Role r : roles) if (r.getActive() && r.getActivity()) {
			blocking  = true;
			boolean activity = r.runScheduler();
			if (!activity) {
				r.setActivityFinished();
			}
			break;
		}
		
		// Scheduler disposition
		return blocking;
	}
	
	// Actions
	
	// Getters
	
	@Override
	public String getName() {
		return this.name;
	}
	
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
		r.setActive(); // TODO testing only - remove!!
		r.setActivityBegun(); // TODO testing only - remove!!
	}
	
	@Override
	public void setAnimation(city.animations.interfaces.AnimatedPerson p) {
		animation = p;
	}

	@Override
	public void setCar(Car c) {
		car = c;
	}
	
	// Utilities
	
	@Override
	public void addRole(Role r) {
		roles.add(r);
		r.setPerson(this);
	}
	
//	private boolean shouldGoToWork() {
//		boolean disposition = false;
//		// if (!occupation.getActive()) {}
//		return disposition;
//	}
	
	// Classes

}
