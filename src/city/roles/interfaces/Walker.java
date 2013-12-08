package city.roles.interfaces;

import city.animations.interfaces.AnimatedWalker;
import city.bases.interfaces.BuildingInterface;
import city.bases.interfaces.RoleInterface;

public interface Walker extends RoleInterface {

	// Data
	
	static enum WalkerState {NOTWALKING, WALKING};
	static enum WalkerEvent {NONE, STARTINGTOWALK, ATDESTINATION};
	
	// Constructor
	
	// Messages
	
	public void msgImAtDestination();

	// Scheduler
	
	// Actions
	
	// Getters
	
	public WalkerState getState();
	public WalkerEvent getEvent();
	public BuildingInterface getDestination();
	
	// Setters
	void setAnimation(AnimatedWalker a);
	
	// Utilities
	
	// Classes
	
}
