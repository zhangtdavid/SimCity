package city.agents;

import city.Agent;
import city.interfaces.Car;
import city.roles.CarPassengerRole;

public class CarAgent extends Agent implements Car {

	// Data
	enum CarState {NOTDRIVING, DRIVING};
	CarState myState = CarState.NOTDRIVING; // State of car
	enum CarEvent {NONE, PASSENGERENTERED, ATDESTINATION};
	CarEvent myEvent = CarEvent.NONE; // Event for car
	CarPassengerRole carPassenger; // Current passenger
	Building destination; // Destination to go to
	CarGui myGui; // GUI for animations
	
	// Constructor
	CarAgent() { // Sets all variables to null
		carPassenger = null;
		destination = null;
		myGui = null;
	}
	
	// Messages
	void msgIWantToDrive(CarPassengerRole cpr, Building dest) { // From CarPassengerRole, tells car to go somewhere
		carPassenger = cpr;
		destination = dest;
		myEvent = CarEvent.PASSENGERENTERED;
		stateChanged();
	}

	void msgImAtCarDestination() { // From myGui, tells car it is at destination
		carPassenger = null;
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
		myGui.goToDestination(destination); // This will call a msg to the GUI, which will animate and then call msgImAtCarDestination() on this car
	}

	void stopDriving() { // Call to passenger at destination, set this car inactive
		carPassenger.msgImAtDestination();
		carPassenger = null;
	}
	
	// Getters
	
	
	// Setters
	void setGui(CarGui gui) {
		myGui = gui;
	}
	
	// Utilities
	
	// Classes

}
