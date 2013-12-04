package city.agents;

import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import city.Agent;
import city.BuildingInterface;
import city.animations.interfaces.AnimatedCar;
import city.interfaces.Car;
import city.interfaces.CarPassenger;

public class CarAgent extends Agent implements Car {

	// Data
	private CARSTATE myState = CARSTATE.NOTDRIVING; // State of car
	private CAREVENT myEvent = CAREVENT.NONE; // Event for car
	private CarPassenger carPassenger; // Current passenger
	private BuildingInterface destination; // Destination to go to
	private AnimatedCar animation; // GUI for animations
	
	private Semaphore atDestination = new Semaphore(0, true);
	
	// Constructor
	public CarAgent(BuildingInterface currentLocation) { // Sets all variables to null
		super();
		carPassenger = null;
		destination = currentLocation;
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
		animation.goToDestination(destination); // This will call a msg to the GUI, which will animate and then call msgImAtCarDestination() on this car
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
	
	// Setters
	
	public void setAnimation(AnimatedCar anim) {
		animation = anim;
	}
	
	// Utilities

	@Override
	public void print(String msg) {
        AlertLog.getInstance().logMessage(AlertTag.CAR, "CarAgent " + this.getName(), msg);
    }
	
	// Classes

}
