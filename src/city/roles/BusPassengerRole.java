package city.roles;

import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import city.Application;
import city.agents.interfaces.Bus;
import city.animations.BusPassengerAnimation;
import city.animations.interfaces.AnimatedBusPassenger;
import city.bases.Role;
import city.bases.interfaces.AnimationInterface;
import city.bases.interfaces.BuildingInterface;
import city.buildings.interfaces.BusStop;
import city.roles.interfaces.BusPassenger;

public class BusPassengerRole extends Role implements BusPassenger {
	
	// Data
	
	private BUSPASSENGERSTATE myState = BUSPASSENGERSTATE.NOTBUSSING;
	private BUSPASSENGEREVENT myEvent = BUSPASSENGEREVENT.NONE;
	private Bus myBus;
	private BusStop busStopToWaitAt;
	private BusStop busStopDestination;
	private BuildingInterface destination;
	private AnimatedBusPassenger animation;
	private Semaphore atDestination = new Semaphore(0, true);
	
	// Constructor
	
	public BusPassengerRole(BusStop destinationBusStop, BusStop stopToWaitAt, BuildingInterface destination) {
		busStopDestination = destinationBusStop;
		busStopToWaitAt = stopToWaitAt;
		this.destination = destination;
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
		if(animation == null) {
			animation = new BusPassengerAnimation(this, busStopToWaitAt, Application.sidewalks);
			Application.getMainFrame().cityView.addAnimation(animation);
		}
		stateChanged();
	}
	
	@Override
	public void msgImAtYourDestination() {
		myEvent = BUSPASSENGEREVENT.ATDESTINATION;
		stateChanged();
	}
	
	@Override
	public void msgImAtDestination() { // This is at bus
		atDestination.release();
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
		print("Getting on bus " + myBus.getName() + " at stop " + busStopDestination.getName() + " to go to " + busStopDestination.getName());
		animation.goToBus();
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.getPerson().setCash(this.getPerson().getCash() - Bus.BUS_FARE);
		busStopToWaitAt.removeFromWaitingList(this);
		myState = BUSPASSENGERSTATE.ONBUS;
		myBus.msgImOnBus(this, busStopDestination);
	}
	
	private void getOffBus() {
		print("Getting off bus " + myBus.getName() + " at stop " + busStopDestination.getName());
		animation.getOffBus();
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		myBus.msgImOffBus(this);
		myState = BUSPASSENGERSTATE.NOTBUSSING;
		myEvent = BUSPASSENGEREVENT.NONE;
		this.setInactive();
	}
	
	// Getters
	
	@Override
	public BuildingInterface getBusStopDestination() {
		return busStopDestination;
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
	
	@Override
	public String getStateString() {
		return myState.toString();
	}
	
	@Override
	public BuildingInterface getDestination() {
		return destination;
	}
	
	// Setters
	
	@Override
	public void setActive() {
		super.setActive();
		msgAtWaitingStop();
	}
	
	@Override
	public void setAnimation(AnimationInterface a) {
		animation = (AnimatedBusPassenger) a;
	}
	
	// Utilities
	
	@Override
	public void print(String msg) {
		this.getPerson().printViaRole("BusPassenger", msg);
        AlertLog.getInstance().logMessage(AlertTag.BUS, "BusPassengerRole " + this.getPerson().getName(), msg);
    }
	
	// Classes

}
