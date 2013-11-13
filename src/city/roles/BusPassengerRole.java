package city.roles;

import city.Role;
import city.interfaces.BusPassenger;
import city.agents.BusAgent;
import city.agents.PersonAgent;
import city.buildings.BusStopBuilding;

public class BusPassengerRole extends Role implements BusPassenger {
	
	// Data
	PersonAgent myPerson;
	enum BusPassengerState {NOTBUSSING, GOINGTOSTOP, WAITINGFORBUS, GETTINGONBUS, GETTINGOFFBUS, ONBUS};
	BusPassengerState myState = BusPassengerState.NOTBUSSING;
	enum BusPassengerEvent {NONE, WANTTOBUS, ATSTOP, BUSISHERE, ATDESTINATION};
	BusPassengerEvent myEvent = BusPassengerEvent.NONE;
	BusAgent myBus;
	BusStopBuilding busStopToWaitAt;
	BusStopBuilding destination;
	// CarPassengerGui myGui;
	
	// Constructor
	BusPassengerRole(PersonAgent p) {
		myPerson = p;
	}
	
	// Messages
	public void msgImGoingToBus(BusStopBuilding dest, BusStopBuilding stopToWaitAt) {
		destination = dest;
		busStopToWaitAt = stopToWaitAt;
		myEvent = BusPassengerEvent.WANTTOBUS;
		stateChanged();
	}
	public void msgAtWaitingStop() {
		busStopToWaitAt.waitingList.add(this);
		myEvent = BusPassengerEvent.ATSTOP;
		stateChanged();
	}
	public void msgBusIsHere(BusAgent b) {
		myBus = b;
		myEvent = BusPassengerEvent.BUSISHERE;
		stateChanged();
	}
	public void msgImAtYourDestination() {
		myEvent = BusPassengerEvent.ATDESTINATION;
		stateChanged();
	}
	
	// Scheduler
	
	@Override
	public boolean runScheduler() {
		if(myState == BusPassengerState.NOTBUSSING && myEvent == BusPassengerEvent.WANTTOBUS) {
			myState = BusPassengerState.GOINGTOSTOP;
			goToStopToWaitAt();
		}
		if(myState == BusPassengerState.GOINGTOSTOP && myEvent == BusPassengerEvent.ATSTOP) {
			myState = BusPassengerState.WAITINGFORBUS;
		}
		if(myState == BusPassengerState.WAITINGFORBUS && myEvent == BusPassengerEvent.BUSISHERE) {
			myState = BusPassengerState.GETTINGONBUS;
			getOnBus();
		}
		if(myState == BusPassengerState.ONBUS && myEvent == BusPassengerEvent.ATDESTINATION) {
			myState = BusPassengerState.GETTINGOFFBUS;
			getOffBus();
		}
		return false;
	}
	
	// Actions
	void goToStopToWaitAt() {
//		myGui.doGoToStop(busStopToWaitAt); // This will call a msg to the GUI, which will animate and then call msgAtWaitingStop() on this passenger
		msgAtWaitingStop();
	}
	
	void getOnBus() {
//		myGui.doGetOnBus(myBus); // This will call a msg to the GUI, which will pause this role until the animation is finished, and then finish this action
//		myPerson.money -= myBus.busFare;
		myBus.msgImOnBus(this, destination);
		myState = BusPassengerState.ONBUS;
	}
	
	void getOffBus() {
//		myGui.doGetOffBus(myBus); // This will call a msg to the GUI, which will pause this role until the animation is finished, and then finish this action
		myBus.msgImOffBus(this);
		myState = BusPassengerState.NOTBUSSING;
		myEvent = BusPassengerEvent.NONE;
		this.setInactive();
	}
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes

}
