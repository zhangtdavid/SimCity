package city.abstracts;

import utilities.EventLog;
import city.BuildingInterface;

/**
 * The base class for all SimCity201 building mocks.
 * 
 * This class implements most things required by BuildingInterface so that
 * mocks themselves may focus only on their particular behaviors.
 * 
 * This class also implements the event log used by all mocks.
 */
public abstract class MockBuilding implements BuildingInterface {
	
	// Data
	
	public EventLog log = new EventLog();
	
	// Constructor
	
	// Messages
	
	// Scheduler	
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities

}
