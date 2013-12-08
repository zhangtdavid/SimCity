package city.roles;

import trace.AlertLog;
import trace.AlertTag;
import city.animations.interfaces.AnimatedWalker;
import city.bases.Role;
import city.bases.interfaces.BuildingInterface;
import city.roles.interfaces.Walker;

public class WalkerRole extends Role implements Walker {
	
	// Data
	
	private WalkerState myState = WalkerState.NOTWALKING;
	private WalkerEvent myEvent = WalkerEvent.NONE;
	private BuildingInterface destination;
	private AnimatedWalker animation;
	
	// Constructor
	
	public WalkerRole(BuildingInterface dest_) {
		destination = dest_;
	}
	
	// Messages

	@Override
	public void msgImAtDestination() {
		this.getPerson().guiAtDestination();
		myEvent = WalkerEvent.ATDESTINATION;
		stateChanged();
	}
	
	// Scheduler
	
	@Override
	public boolean runScheduler() {
		System.out.println(myState);
		if(myState == WalkerState.NOTWALKING && myEvent == WalkerEvent.STARTINGTOWALK) { // In car, start driving
			myState = WalkerState.WALKING;
			startWalking();
			return true;
		}
		if(myState == WalkerState.WALKING && myEvent == WalkerEvent.ATDESTINATION) { // At destination, get out
			myState = WalkerState.NOTWALKING;
			arriveAtDestination();
			return true;
		}
		return false;
	}

	// Actions
	
	private void startWalking() {
		print("Going to walk to " + destination);
		animation.goToDestination(destination);
	}

	private void arriveAtDestination() {
		print("Arrived at " + destination);
		myState = WalkerState.NOTWALKING;
		myEvent = WalkerEvent.NONE;
		this.setInactive();
	}
	
	// Getters
	
	@Override
	public WalkerState getState() {
		return myState;
	}
	
	@Override
	public WalkerEvent getEvent() {
		return myEvent;
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
		myEvent = WalkerEvent.STARTINGTOWALK;
		runScheduler();
	}
	
	@Override
	public void setAnimation(AnimatedWalker a) {
		animation = a;
	}
	
	// Utilities
	
	@Override
	public void print(String msg) {
		this.getPerson().printViaRole("Walker", msg);
        AlertLog.getInstance().logMessage(AlertTag.PERSON, "WalkerRole " + this.getPerson().getName(), msg);
    }
	
	// Classes

}
