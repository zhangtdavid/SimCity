package city.roles;

import city.Role;
import city.interfaces.BusPassenger;
import city.interfaces.Bus;
import city.buildings.BusStopBuilding;

public class BusPassengerRole extends Role implements BusPassenger {
	
	// Data
	public enum BusPassengerState {NOTBUSSING, WAITINGFORBUS, GETTINGONBUS, GETTINGOFFBUS, ONBUS};
	public BusPassengerState myState = BusPassengerState.NOTBUSSING;
	public enum BusPassengerEvent {NONE, ATSTOP, BUSISHERE, ATDESTINATION};
	public BusPassengerEvent myEvent = BusPassengerEvent.NONE;
	public Bus myBus;
	public BusStopBuilding busStopToWaitAt;
	public BusStopBuilding destination;
	// CarPassengerGui myGui;
	
	// Constructor
	public BusPassengerRole(BusStopBuilding dest, BusStopBuilding stopToWaitAt) {
		destination = dest;
		busStopToWaitAt = stopToWaitAt;
	}
	
	// Messages
	public void msgAtWaitingStop() {
		busStopToWaitAt.waitingList.add(this);
		myEvent = BusPassengerEvent.ATSTOP;
		stateChanged();
	}
	public void msgBusIsHere(Bus b) {
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
		if(myState == BusPassengerState.NOTBUSSING && myEvent == BusPassengerEvent.ATSTOP) {
			myState = BusPassengerState.WAITINGFORBUS;
			return true;
		}
		if(myState == BusPassengerState.WAITINGFORBUS && myEvent == BusPassengerEvent.BUSISHERE) {
			myState = BusPassengerState.GETTINGONBUS;
			getOnBus();
			return true;
		}
		if(myState == BusPassengerState.ONBUS && myEvent == BusPassengerEvent.ATDESTINATION) {
			myState = BusPassengerState.GETTINGOFFBUS;
			getOffBus();
			return true;
		}
		return false;
	}
	
	// Actions
	
	void getOnBus() {
//		myGui.doGetOnBus(myBus); // This will call a msg to the GUI, which will pause this role until the animation is finished, and then finish this action
//		myPerson.money -= myBus.busFare;
		busStopToWaitAt.waitingList.remove(this);
		myState = BusPassengerState.ONBUS;
		myBus.msgImOnBus(this, destination);
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
	public void setActive() {
		super.setActive();
		msgAtWaitingStop();
	}
	// Classes

}
