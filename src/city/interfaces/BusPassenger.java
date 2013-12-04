package city.interfaces;

import city.BuildingInterface;
import city.RoleInterface;

public interface BusPassenger extends RoleInterface {

	// Data
	
	static enum BUSPASSENGERSTATE {NOTBUSSING, WAITINGFORBUS, GETTINGONBUS, GETTINGOFFBUS, ONBUS};
	static enum BUSPASSENGEREVENT {NONE, ATSTOP, BUSISHERE, ATDESTINATION};
	
	// Constructor
	
	// Messages
	
	public void msgBusIsHere(Bus b);
	public void msgImAtYourDestination();
	public void msgAtWaitingStop();
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	public BuildingInterface getDestination(); 
	public BuildingInterface getBusStopToWaitAt();
	public Bus getBus();
	public BUSPASSENGERSTATE getState();
	public BUSPASSENGEREVENT getEvent();
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
