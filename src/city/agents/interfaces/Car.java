package city.agents.interfaces;

import city.bases.interfaces.AgentInterface;
import city.bases.interfaces.BuildingInterface;
import city.roles.interfaces.CarPassenger;

public interface Car extends AgentInterface {
	
	// Data
	
	static enum CARSTATE {NOTDRIVING, DRIVING};
	static enum CAREVENT {NONE, PASSENGERENTERED, ATDESTINATION};
	
	// Constructor
	
	// Messages
	
	public void msgIWantToDrive(CarPassenger carPassenger, BuildingInterface destination);
	public void msgImAtCarDestination();
	public void msgAtDestination();
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	public CARSTATE getState();
	public CAREVENT getEvent();
	public Person getOwner(); // Returns the actual owner, or if it belongs to a role, the person owning the role
	public CarPassenger getPassenger();
	public BuildingInterface getDestination();
	
	// Setters
	
	// Utilities
	
	// Classes	
	
}
