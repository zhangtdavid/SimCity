package city.roles;

import city.Role;
import city.interfaces.CarPassenger;
import city.interfaces.Car;
import city.Building;

public class CarPassengerRole extends Role implements CarPassenger {
	
	// Data
	public enum CarPassengerState {NOTDRIVING, GOINGTOCAR, DRIVING};
	public CarPassengerState myState = CarPassengerState.NOTDRIVING; // State of passenger
	public enum CarPassengerEvent {NONE, WANTTODRIVE, ATCAR, ATDESTINATION};
	public CarPassengerEvent myEvent = CarPassengerEvent.NONE; // Event of passenger
	public Car myCar; // Car this person is getting into
	public Building destination; // Building this car is going to
//	CarPassengerGui myGui; // GUI for animation
	
	// Constructor
	public CarPassengerRole(Car c) { // Pass in the person and car this role is assigned to
		myCar = c;
	}
	
	// Messages
	public void msgImGoingToDrive() { // From setactive, telling this role he/she wants to drive
		myEvent = CarPassengerEvent.WANTTODRIVE;
		stateChanged();
	}

	public void msgImAtCar() { // From animation, telling this role car is ready to drive
		myEvent = CarPassengerEvent.ATCAR;
		stateChanged();
	}

	public void msgImAtDestination() { // From car, telling this role he/she is at destination
		destination = null;
		myEvent = CarPassengerEvent.ATDESTINATION;
		stateChanged();
	}
	// Scheduler
	
	@Override
	public boolean runScheduler() {
		if(myState == CarPassengerState.NOTDRIVING && myEvent == CarPassengerEvent.WANTTODRIVE) { // Wants to drive, go to car
			myState = CarPassengerState.GOINGTOCAR;
			goToCar();
			return true;
		}
		if(myState == CarPassengerState.GOINGTOCAR && myEvent == CarPassengerEvent.ATCAR) { // In car, start driving
			myState = CarPassengerState.DRIVING;
			driveCar();
			return true;
		}
		if(myState == CarPassengerState.DRIVING && myEvent == CarPassengerEvent.ATDESTINATION) { // At destination, get out
			myState = CarPassengerState.NOTDRIVING;
			getOutOfCar();
			return true;
		}
		return false;
	}
	
	// Actions
	void goToCar() {
//		myGui.doGoToCar(myCar); // This will call a msg to the GUI, which will animate and then call msgImAtCar() on this passenger
		msgImAtCar();
	}

	void driveCar() {
		myCar.msgIWantToDrive(this, destination); // This will tell the car to start driving to the destination
	}

	void getOutOfCar() {
//		myGui.doGetOutOfCar(myCar); // This will pause this agent until the animation is finished
		myState = CarPassengerState.NOTDRIVING; // Reset state and event
		myEvent = CarPassengerEvent.NONE;
		this.setInactive();
	}
	// Getters
	
	// Setters
//	void setGui(CarPassengerGui gui) {
//		myGui = gui;
//	}
	
	// Utilities
	public void setActive(Building dest) {
		destination = dest;
		setActive();
		msgImGoingToDrive();
	}
	
	// Classes

}
