package city.roles;

import trace.AlertLog;
import trace.AlertTag;
import city.BuildingInterface;
import city.Role;
import city.interfaces.Bus;
import city.interfaces.BusPassenger;
import city.interfaces.BusStop;

public class BusPassengerRole extends Role implements BusPassenger {
	
	// Data
	
	private BUSPASSENGERSTATE myState = BUSPASSENGERSTATE.NOTBUSSING;
	private BUSPASSENGEREVENT myEvent = BUSPASSENGEREVENT.NONE;
	private Bus myBus;
	private BusStop busStopToWaitAt;
	private BusStop destination;
	// public AnimatedBus animation;
	// private Semaphore atDestination = new Semaphore(0, true);
	
	// Constructor
	
	public BusPassengerRole(BusStop dest, BusStop stopToWaitAt) {
		destination = dest;
		busStopToWaitAt = stopToWaitAt;
	}
	
	// Messages
	
	@Override
	public void msgAtWaitingStop() {
		busStopToWaitAt.addToWaitingList(this);
		myEvent = BUSPASSENGEREVENT.ATSTOP;
		stateChanged();
	}
	
	@Override
	public void msgBusIsHere(Bus b) {
		myBus = b;
		myEvent = BUSPASSENGEREVENT.BUSISHERE;
		stateChanged();
	}
	
	@Override
	public void msgImAtYourDestination() {
		myEvent = BUSPASSENGEREVENT.ATDESTINATION;
		stateChanged();
	}
	
	// Scheduler
	
	@Override
	public boolean runScheduler() {
		if(myState == BUSPASSENGERSTATE.NOTBUSSING && myEvent == BUSPASSENGEREVENT.ATSTOP) {
			myState = BUSPASSENGERSTATE.WAITINGFORBUS;
			return true;
		}
		if(myState == BUSPASSENGERSTATE.WAITINGFORBUS && myEvent == BUSPASSENGEREVENT.BUSISHERE) {
			myState = BUSPASSENGERSTATE.GETTINGONBUS;
			getOnBus();
			return true;
		}
		if(myState == BUSPASSENGERSTATE.ONBUS && myEvent == BUSPASSENGEREVENT.ATDESTINATION) {
			myState = BUSPASSENGERSTATE.GETTINGOFFBUS;
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
		busStopToWaitAt.removeFromWaitingList(this);
		myState = BUSPASSENGERSTATE.ONBUS;
		myBus.msgImOnBus(this, destination);
	}
	
	private void getOffBus() {
		print("Getting off bus " + myBus.getName() + " at stop " + destination.getName());
		// animation.doGetOffBus(myBus); // This will call a msg to the GUI, which will pause this role until the animation is finished, and then finish this action
		myBus.msgImOffBus(this);
		myState = BUSPASSENGERSTATE.NOTBUSSING;
		myEvent = BUSPASSENGEREVENT.NONE;
		this.setInactive();
	}
	
	// Getters
	
	@Override
	public BuildingInterface getDestination() {
		return destination;
	}
	
	@Override
	public BuildingInterface getBusStopToWaitAt() {
		return busStopToWaitAt;
	}
	
	@Override
	public Bus getBus() {
		return myBus;
	}

	@Override
	public BUSPASSENGERSTATE getState() {
		return myState;
	}

	@Override
	public BUSPASSENGEREVENT getEvent() {
		return myEvent;
	}
	
	// Setters
	
	@Override
	public void setActive() {
		super.setActive();
		msgAtWaitingStop();
	}
	
	// Utilities
	
	@Override
	public void print(String msg) {
        AlertLog.getInstance().logMessage(AlertTag.BUS, "BusPassengerRole " + this.getPerson().getName(), msg);
    }
	
	// Classes

}
