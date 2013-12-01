package city.abstracts;

import utilities.EventLog;
import utilities.StringUtil;
import city.AgentInterface;

/**
 * The base class for all SimCity201 agent mocks.
 * 
 * This class implements everything required by AgentInterface so that
 * mocks themselves may focus only on the code of the class they're mocking.
 * 
 * This class also implements the event log used by all mocks.
 */
public abstract class MockAgent implements AgentInterface {
	
	// Data
	
	public EventLog log = new EventLog();
	
	// Constructor
	
	// Messages
	
	// Scheduler
	
	// Actions
	
	// Getters
	
    @Override
    public String getName() {
        return StringUtil.shortName(this);
    }
	
	// Setters
	
	// Utilities
	
    @Override
	public void stateChanged() {}

    @Override
    public void print(String msg) {}
    
    @Override
    public void print(String msg, Throwable e) {}
   
    @Override
    public synchronized void startThread() {}

    @Override
    public void stopThread() {}
    
    @Override
    public void pauseOrResumeThread() {}

}
