package city.roles;

import trace.AlertLog;
import trace.AlertTag;
import city.agents.interfaces.Car;
import city.bases.Role;
import city.bases.interfaces.BuildingInterface;
import city.roles.interfaces.CarPassenger;

public class CarPassengerRole extends Role implements CarPassenger {
	
	// Data
	
	private CarPassengerState myState = CarPassengerState.NOTDRIVING; // State of passenger
	private CarPassengerEvent myEvent = CarPassengerEvent.NONE; // Event of passenger
	private Car myCar; // Car this person is getting into
	private BuildingInterface destination; // Building this car is going to
	// private CarPassengerGui myGui; // GUI for animation
	
	// Constructor
	
	public CarPassengerRole(Car c, BuildingInterface dest_) { // Pass in the person and car this role is assigned to
		myCar = c;
		destination = dest_;
	}
	
	// Messages

	@Override
	public void msgImAtCar() { // From animation, telling this role car is ready to drive
		myEvent = CarPassengerEvent.ATCAR;
		stateChanged();
	}

	@Override
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

	// Actions
	
	private void driveCar() {
		print("Going to drive car " + myCar.getName() + " to go to " + destination);
		myCar.msgIWantToDrive(this, destination); // This will tell the car to start driving to the destination
	}

	private void getOutOfCar() {
		print("Getting out of car " + myCar.getName());
		// myGui.doGetOutOfCar(myCar); // This will pause this agent until the animation is finished
		myState = CarPassengerState.NOTDRIVING; // Reset state and event
		myEvent = CarPassengerEvent.NONE;
		this.setInactive();
	}
	
	// Getters
	
	@Override
	public CarPassengerState getState() {
		return myState;
	}
	
	@Override
	public CarPassengerEvent getEvent() {
		return myEvent;
	}
	
	@Override
	public Car getCar() {
		return myCar;
	}
	
	@Override
	public BuildingInterface getDestination() {
		return destination;
	}
	
	@Override
	public String getStateString() {
		return myState.toString();
	}
	
	// Setters
	
	@Override
	public void setActive() {
		super.setActivityBegun();
		super.setActive();
		myEvent = CarPassengerEvent.ATCAR;
	}
	
	// Utilities
	
	@Override
	public void print(String msg) {
		this.getPerson().printViaRole("CarPassenger", msg);
        AlertLog.getInstance().logMessage(AlertTag.CAR, "CarPassengerRole " + this.getPerson().getName(), msg);
    }
	
	// Classes

}
