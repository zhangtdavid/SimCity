package city.roles;

import trace.AlertLog;
import trace.AlertTag;
import city.agents.interfaces.Car;
import city.bases.Role;
import city.bases.interfaces.BuildingInterface;
import city.roles.interfaces.CarPassenger;
import city.roles.interfaces.MarketDeliveryPerson;

public class CarPassengerRole extends Role implements CarPassenger {
	
	// Data

	private CarPassengerState myState = CarPassengerState.NOTDRIVING; // State of passenger
	private CarPassengerEvent myEvent = CarPassengerEvent.NONE; // Event of passenger
	private Car myCar; // Car this person is getting into
	private BuildingInterface destination; // Building this car is going to
	
	private MarketDeliveryPerson parent;
	
	// Constructor
	
	/**
	 * Constructs a CarPassengerRole for a PersonAgent. 
	 * 
	 * PersonAgent does not keep these roles after they're used once.
	 * 
	 * @param c The CarAgent that this role will be "driving"
	 * @param destination The destination to travel to
	 */
	public CarPassengerRole(Car c, BuildingInterface destination) {
		this.myCar = c;
		this.destination = destination;
	}
	
	public CarPassengerRole(Car c, BuildingInterface destination, MarketDeliveryPerson parent) {
		this.myCar = c;
		this.destination = destination;
		this.parent = parent;
	}
	
	// Messages

	/**
	 * From animation, telling this role car is ready to drive
	 */
	@Override
	public void msgImAtCar() {
		myEvent = CarPassengerEvent.ATCAR;
		if (parent != null)
			parent.setActivityBegun();
		stateChanged();
	}

	/**
	 * From car, telling this role he/she is at destination
	 */
	@Override
	public void msgImAtDestination() { 
		myEvent = CarPassengerEvent.ATDESTINATION;
		if (parent != null)
			parent.setActivityBegun();
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
		myState = CarPassengerState.NOTDRIVING; // Reset state and event
		myEvent = CarPassengerEvent.NONE;
		if (parent != null)
			parent.msgArrivedAtDestination();
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
	
	@Override
	public MarketDeliveryPerson getParent() {
		return parent;
	}
	
	// Setters
	
	@Override
	public void setActive() {
		super.setActivityBegun();
		super.setActive();
		myEvent = CarPassengerEvent.ATCAR;
		if (parent != null)
			parent.setActivityBegun();
	}
	
	// Utilities
	
	@Override
	public void print(String msg) {
		this.getPerson().printViaRole("CarPassenger", msg);
        AlertLog.getInstance().logMessage(AlertTag.CAR, "CarPassengerRole " + this.getPerson().getName(), msg);
    }
	
	// Classes

}
