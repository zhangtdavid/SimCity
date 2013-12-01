package city.abstracts;

import utilities.EventLog;
import city.AnimationInterface;

/**
 * The base class for all SimCity201 agent animation mocks.
 * 
 * This class implements most things required by AnimationInterface so that
 * mocks themselves may focus only on their particular behaviors.
 * 
 * This class also implements the event log used by all mocks.
 */
public abstract class MockAnimation implements AnimationInterface {
	
	// Data
	
	private boolean isVisible = false;
	private int xPos, yPos;
	
	public EventLog log = new EventLog();
	
	// Constructor
	
	// Messages
	
	// Scheduler	
	
	// Actions
	
	// Getters
	
	@Override
	public boolean getVisible() {
		return isVisible;
	}
	
	@Override
    public int getXPos() {
    	return xPos;
    }

	@Override
    public int getYPos() {
    	return yPos;
    }
	
	// Setters
	
	@Override
	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		
	}
	
	// Utilities

}
