package city.agents;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

import city.Agent;
import city.Building;
import city.Role;
import city.buildings.BusStopBuilding;
import city.interfaces.Car;
import city.interfaces.Person;
import city.roles.BusPassengerRole;
import city.roles.CarPassengerRole;

public class PersonAgent extends Agent implements Person {
	
	// Data
	
	private Date date;
	private Role occupation;
	private Building workplace;
	private Car car;
	private CarPassengerRole carPassengerRole;
	private BusPassengerRole busPassengerRole;
	private String name;
	private List<Role> roles = new ArrayList<Role>();
	private Semaphore atDestination = new Semaphore(0, true);
	private city.animations.interfaces.AnimatedPerson animation;
	private enum State {none, goingToWork, goingToBank, goingToRestaurant, goingToMarket, goingHome, atWork, atBank, atRestaurant, atMarket, atHome, leavingWork };
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
		if (state == State.goingToWork) {
			if (car != null) {
				// if ()
			} else {
				
			}
		} else if (shouldGoToWork()) {
			actGoToWork();
			return true;
		}
		if (state == State.leavingWork) {
			
		} else if (shouldLeaveWork()) {
			actLeaveWork();
			return true;
		}
		if (!shouldGoToWork() && !shouldLeaveWork()) {
			return true;
		}
		
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
	
	/**
	 * Sends the person to work. Once the person's occupation role is active,
	 */
	private void actGoToWork() {
		if (car != null) {
			//carPassengerRole = new CarPassengerRole(car, workplace);
			carPassengerRole.setActive();
			carPassengerRole.setPerson(this);
			roles.add(carPassengerRole);
		} else {
			// BusStopBuilding b = findClosestBusStop();
			// animation.goToBusStop(b);
			//busPassengerRole = new BusPassengerRole(workplace);
			busPassengerRole.setActive();
			busPassengerRole.setPerson(this);
			roles.add(busPassengerRole);
		}
		state = State.goingToWork;
	}
	
	private void actLeaveWork() {
		
	}
	
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
	
	@Override
	public void setWorkplace(Building b) {
		workplace = b;
	}
	
	// Utilities
	
	@Override
	public void addRole(Role r) {
		roles.add(r);
		r.setPerson(this);
	}
	
	/**
	 * If the person is not at work and the current time is at or within
	 * their working hours, then the person should go to work.
	 * 
	 * @return true if the person should go to work
	 */
	private boolean shouldGoToWork() {
		boolean disposition = false;
		if (!occupation.getActive() && inShiftRange()) {
			disposition = true;
		}
		return disposition;
	}
	
	/**
	 * If the person is at work and the current time is outside their
	 * working hours, then the person should leave work.
	 * 
	 * @return true if 
	 */
	private boolean shouldLeaveWork() {
		boolean disposition = false;
		if (occupation.getActive() && !inShiftRange()) {
			disposition = true;
		}
		return disposition;
	}
	
	/**
	 * Tests whether the current time is at or within the occupation's working hours
	 * 
	 * @param r the role object representing the person's occupation
	 * @return true if the current time is at or within their working hours
	 */
	private boolean inShiftRange() {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int time = c.get(Calendar.HOUR_OF_DAY);
		
		boolean disposition = false;
		// TODO do we need to give the person enough time to get to work?
		if (time >= occupation.getShiftStart() && time <= occupation.getShiftEnd()) {
			disposition = true;
		}
		return disposition;
	}
	
	/**
	 * 
	 */
//	private BusStopBuilding findClosestBusStop() {
//		return;
//	}
	
	// Classes

}
