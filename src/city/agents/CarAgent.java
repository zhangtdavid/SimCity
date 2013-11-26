package city.agents;

import city.Agent;
import city.Building;
import city.interfaces.Car;
import city.interfaces.CarPassenger;

public class CarAgent extends Agent implements Car {

	// Data
	public enum CarState {NOTDRIVING, DRIVING};
	public CarState myState = CarState.NOTDRIVING; // State of car
	public enum CarEvent {NONE, PASSENGERENTERED, ATDESTINATION};
	public CarEvent myEvent = CarEvent.NONE; // Event for car
	public CarPassenger carPassenger; // Current passenger
	public Building destination; // Destination to go to
	//CarGui myGui; // GUI for animations
	
	// Constructor
	public CarAgent() { // Sets all variables to null
		super();
		carPassenger = null;
		destination = null;
	}
	
	// Messages
	public void msgIWantToDrive(CarPassenger cp, Building dest) { // From CarPassengerRole, tells car to go somewhere
		carPassenger = cp;
		destination = dest;
		myEvent = CarEvent.PASSENGERENTERED;
		stateChanged();
	}

	void msgImAtCarDestination() { // From myGui, tells car it is at destination
		destination = null;
		myEvent = CarEvent.ATDESTINATION;
		stateChanged();
	}
	
	// Scheduler
	@Override
	public boolean runScheduler() {
		if(myState == CarState.NOTDRIVING && myEvent == CarEvent.PASSENGERENTERED) { // Passenger got in car, go to destination
			myState = CarState.DRIVING;
			goToDestination();
			return true;
		}
		if(myState == CarState.DRIVING && myEvent == CarEvent.ATDESTINATION) { // Car arrived at destination, stop and notify passenger
			myState = CarState.NOTDRIVING;
			myEvent = CarEvent.NONE;
			stopDriving();
			return true;
		}
		return false;
	}
	
	// Actions
	void goToDestination() { // Call to GUI to go to destination, goes to sleep and then woken up by GUI
		print("Going to destination " + destination.getName());
//		myGui.goToDestination(destination); // This will call a msg to the GUI, which will animate and then call msgImAtCarDestination() on this car
		msgImAtCarDestination();
	}

	void stopDriving() { // Call to passenger at destination, set this car inactive
		print("Stopping driving");
		carPassenger.msgImAtDestination();
		carPassenger = null;
	}
	
	// Getters
	
	
	// Setters
//	void setGui(CarGui gui) {
//		myGui = gui;
//	}
	
	// Utilities
	
	// Classes

}
