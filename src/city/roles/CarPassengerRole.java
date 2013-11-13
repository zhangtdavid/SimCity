package city.roles;

import city.Role;
import city.agents.PersonAgent;
import city.interfaces.CarPassenger;
import city.agents.CarAgent;

public class CarPassengerRole extends Role implements CarPassenger {
	
	// Data
	PersonAgent myPerson;
	enum CarPassengerState {NOTDRIVING, GOINGTOCAR, DRIVING};
	CarPassengerState myState = CarPassengerState.NOTDRIVING;
	enum CarPassengerEvent {NONE, WANTTODRIVE, ATCAR, ATDESTINATION};
	CarPassengerEvent myEvent = CarPassengerEvent.NONE;
	CarAgent myCar;
	Building destination;
	CarPassengerGui myGui;
	
	// Constructor
	CarPassengerRole(PersonAgent p, CarAgent c) {
		myPerson = p;
		myCar = c;
	}
	
	// Messages
	public void msgImGoingToDrive(Building dest, CarAgent c) {
		destination = dest;
		myCar = c;
		myEvent = CarPassengerEvent.WANTTODRIVE;
		stateChanged();
	}

	public void msgImAtCar() {
		myEvent = CarPassengerEvent.ATCAR;
		stateChanged();
	}

	public void msgImAtDestination() {
		destination = null;
		myEvent = CarPassengerEvent.ATDESTINATION;
		stateChanged();
	}
	// Scheduler
	
	@Override
	public boolean runScheduler() {
		if(myState == CarPassengerState.NOTDRIVING && myEvent == CarPassengerEvent.WANTTODRIVE) {
			myState = CarPassengerState.GOINGTOCAR;
			goToCar();
		}
		if(myState == CarPassengerState.GOINGTOCAR && myEvent == CarPassengerEvent.ATCAR) {
			myState = CarPassengerState.DRIVING;
			driveCar();
		}
		if(myState == CarPassengerState.DRIVING && myEvent == CarPassengerEvent.ATDESTINATION) {
			myState = CarPassengerState.NOTDRIVING;
			getOutOfCar();
		}
		return false;
	}
	
	// Actions
	void goToCar() {
		myGui.doGoToCar(myCar); // This will call a msg to the GUI, which will animate and then call msgImAtCar() on this passenger
	}

	void driveCar() {
		myCar.msgIWantToDrive(this, destination);
	}

	void getOutOfCar() {
		myGui.doGetOutOfCar(myCar); // This will pause this agent until the animation is finished
		myState = CarPassengerState.NOTDRIVING;
		myEvent = CarPassengerEvent.NONE;
		myPerson.deactivateRole(this);
	}
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes

}
