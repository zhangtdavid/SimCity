package city.tests.bases.mocks;

import utilities.EventLog;
import city.bases.Agent;
import city.bases.interfaces.AnimationInterface;

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
	private Agent agent;
	
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
	
	@Override
	public Agent getAgent() {
		return agent;
	}
	
	// Setters
	
	@Override
	public void setAgent(Agent a) {
		this.agent = a;
	}
	
	@Override
	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		
	}
	
	// Utilities

}
