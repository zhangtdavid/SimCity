package city.roles;

import trace.AlertLog;
import trace.AlertTag;
import city.Role;
import city.buildings.BusStopBuilding;
import city.interfaces.Bus;
import city.interfaces.BusPassenger;

public class BusPassengerRole extends Role implements BusPassenger {
	
	// Data
	
	//	private Semaphore atDestination = new Semaphore(0, true);
	
	// TODO Change these to private and add getters/setters
	public enum BusPassengerState {NOTBUSSING, WAITINGFORBUS, GETTINGONBUS, GETTINGOFFBUS, ONBUS};
	public BusPassengerState myState = BusPassengerState.NOTBUSSING;
	public enum BusPassengerEvent {NONE, ATSTOP, BUSISHERE, ATDESTINATION};
	public BusPassengerEvent myEvent = BusPassengerEvent.NONE;
	public Bus myBus;
	public BusStopBuilding busStopToWaitAt;
	public BusStopBuilding destination;
	// public AnimatedBus animation;
	
	// Constructor
	
	public BusPassengerRole(BusStopBuilding dest, BusStopBuilding stopToWaitAt) {
		destination = dest;
		busStopToWaitAt = stopToWaitAt;
	}
	
	// Messages
	
	@Override
	public void msgAtWaitingStop() {
		busStopToWaitAt.waitingList.add(this);
		myEvent = BusPassengerEvent.ATSTOP;
		stateChanged();
	}
	
	@Override
	public void msgBusIsHere(Bus b) {
		myBus = b;
		myEvent = BusPassengerEvent.BUSISHERE;
		stateChanged();
	}
	
	@Override
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
	
	private void getOnBus() {
		print("Getting on bus " + myBus.getName() + " at stop " + destination.getName() + " to go to " + destination.getName());
		// myGui.doGetOnBus(myBus); // This will call a msg to the GUI, which will pause this role until the animation is finished, and then finish this action
		this.getPerson().setCash(this.getPerson().getCash() - Bus.BUS_FARE);
		busStopToWaitAt.waitingList.remove(this);
		myState = BusPassengerState.ONBUS;
		myBus.msgImOnBus(this, destination);
	}
	
	private void getOffBus() {
		print("Getting off bus " + myBus.getName() + " at stop " + destination.getName());
		// animation.doGetOffBus(myBus); // This will call a msg to the GUI, which will pause this role until the animation is finished, and then finish this action
		myBus.msgImOffBus(this);
		myState = BusPassengerState.NOTBUSSING;
		myEvent = BusPassengerEvent.NONE;
		this.setInactive();
	}
	
	// Getters
	
	// Setters
	
	@Override
	public void setActive() {
		super.setActive();
		msgAtWaitingStop();
	}
	
	
	// Utilities
	
	@Override
	public void print(String msg) {
        super.print(msg);
        AlertLog.getInstance().logMessage(AlertTag.BUS, "BusPassengerRole " + this.getPerson().getName(), msg);
    }
	
	// Classes

}
