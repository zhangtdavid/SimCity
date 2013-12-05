package city.agents;

import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import city.agents.interfaces.Car;
import city.agents.interfaces.Person;
import city.animations.interfaces.AnimatedCar;
import city.bases.Agent;
import city.bases.interfaces.BuildingInterface;
import city.bases.interfaces.RoleInterface;
import city.roles.interfaces.CarPassenger;

public class CarAgent extends Agent implements Car {

	// Data
	private CARSTATE myState = CARSTATE.NOTDRIVING; // State of car
	private CAREVENT myEvent = CAREVENT.NONE; // Event for car
	private CarPassenger carPassenger; // Current passenger
	private BuildingInterface destination; // Destination to go to
	private Person personOwner; // The owner of the car (whether or not the owner is the passenger)
	private RoleInterface roleOwner;
	
	private Semaphore atDestination = new Semaphore(0, true);
	
	// Constructor
	public CarAgent(BuildingInterface currentLocation, Person o) { // Sets all variables to null
		super();
		personOwner = o;
		roleOwner = null;
		carPassenger = null;
		destination = currentLocation;
		o.setCar(this);
	}
	
	public CarAgent(BuildingInterface currentLocation, RoleInterface o) { // Sets all variables to null
		super();
		personOwner = null;
		roleOwner = o;
		carPassenger = null;
		destination = currentLocation;
		// o.setCar(this); // TODO maybe?
	}
	
	// Messages
	
	@Override
	public void msgIWantToDrive(CarPassenger cp, BuildingInterface dest) { // From CarPassengerRole, tells car to go somewhere
		carPassenger = cp;
		destination = dest;
		myEvent = CAREVENT.PASSENGERENTERED;
		stateChanged();
	}
	
	@Override
	public void msgImAtCarDestination() { // From myGui, tells car it is at destination
		destination = null;
		myEvent = CAREVENT.ATDESTINATION;
		stateChanged();
	}
	
	@Override
	public void msgAtDestination() {
		atDestination.release();
	}
	
	// Scheduler
	
	@Override
	public boolean runScheduler() {
		if(myState == CARSTATE.NOTDRIVING && myEvent == CAREVENT.PASSENGERENTERED) { // Passenger got in car, go to destination
			myState = CARSTATE.DRIVING;
			goToDestination();
			return true;
		}
		if(myState == CARSTATE.DRIVING && myEvent == CAREVENT.ATDESTINATION) { // Car arrived at destination, stop and notify passenger
			myState = CARSTATE.NOTDRIVING;
			myEvent = CAREVENT.NONE;
			stopDriving();
			return true;
		}
		return false;
	}
	
	// Actions
	
	private void goToDestination() { // Call to GUI to go to destination, goes to sleep and then woken up by GUI
		print("Going to drive");
		((AnimatedCar) this.getAnimation()).goToDestination(destination); // This will call a msg to the GUI, which will animate and then call msgImAtCarDestination() on this car
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		msgImAtCarDestination();
	}

	private void stopDriving() { // Call to passenger at destination, set this car inactive
		print("Stopping driving");
		carPassenger.msgImAtDestination();
		carPassenger = null;
	}
	
	// Getters
	
	@Override
	public CARSTATE getState() {
		return myState;
	}
	
	@Override
	public CAREVENT getEvent() {
		return myEvent;
	}
	
	@Override
	public CarPassenger getPassenger() {
		return carPassenger;
	}
	
	@Override
	public BuildingInterface getDestination() {
		return destination;
	}
	
	/**
	 * Returns the actual owner, or if it belongs to a role, the person owning the role
	 */
	@Override
	public Person getOwner() {
		if (personOwner != null) {
			return personOwner;
		} else {
			return roleOwner.getPerson();
		}
	}
	
	// Setters
	
	// Utilities

	@Override
	public void print(String msg) {
        AlertLog.getInstance().logMessage(AlertTag.CAR, "CarAgent " + this.getName(), msg);
    }
	
	// Classes

}
