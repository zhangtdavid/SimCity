package city.roles;

import trace.AlertLog;
import trace.AlertTag;
import city.Role;
import city.interfaces.Car;
import city.interfaces.CarPassenger;
import city.Building;

public class CarPassengerRole extends Role implements CarPassenger {
	
	// Data
	public enum CarPassengerState {NOTDRIVING, DRIVING};
	public CarPassengerState myState = CarPassengerState.NOTDRIVING; // State of passenger
	public enum CarPassengerEvent {NONE, ATCAR, ATDESTINATION};
	public CarPassengerEvent myEvent = CarPassengerEvent.NONE; // Event of passenger
	public Car myCar; // Car this person is getting into
	public Building destination; // Building this car is going to
//	CarPassengerGui myGui; // GUI for animation
	
	// Constructor
	public CarPassengerRole(Car c, Building dest_) { // Pass in the person and car this role is assigned to
		myCar = c;
		destination = dest_;
	}
	
	// Messages

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
		if(myState == CarPassengerState.NOTDRIVING && myEvent == CarPassengerEvent.ATCAR) { // In car, start driving
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

	void driveCar() {
		print("Going to drive car " + myCar.getName() + " to go to " + destination);
		myCar.msgIWantToDrive(this, destination); // This will tell the car to start driving to the destination
	}

	void getOutOfCar() {
		print("Getting out of car " + myCar.getName());
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
	
	@Override
	public void setActive() {
		super.setActivityBegun();
		super.setActive();
		myEvent = CarPassengerEvent.ATCAR;
	}
	
	// Utilities
	@Override
	public void print(String msg) {
        super.print(msg);
        AlertLog.getInstance().logMessage(AlertTag.CAR, "CarPassengerRole " + this.getPerson().getName(), msg);
    }
	// Classes

}
