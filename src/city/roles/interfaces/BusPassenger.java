package city.roles.interfaces;

import city.agents.interfaces.Bus;
import city.bases.interfaces.BuildingInterface;
import city.bases.interfaces.RoleInterface;

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
	
	public BuildingInterface getBusStopDestination(); 
	public BuildingInterface getBusStopToWaitAt();
	public Bus getBus();
	public BUSPASSENGERSTATE getState();
	public BUSPASSENGEREVENT getEvent();
	public void msgImAtDestination();
	public void print(String string);
	BuildingInterface getDestination();
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
