package city.tests.bases.mocks;

import utilities.EventLog;
import utilities.StringUtil;
import city.bases.interfaces.AgentInterface;
import city.bases.interfaces.AnimationInterface;

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
	
	public boolean runScheduler() throws InterruptedException {
		return false;
	}
	
	// Actions
	
	// Getters
	
    @Override
    public String getName() {
        return StringUtil.shortName(this);
    }
    
	@Override
	public AnimationInterface getAnimation() {
		// TODO Auto-generated method stub
		return null;
	}
	
	// Setters
	
	@Override
	public void setAnimation(AnimationInterface a) {
		// TODO Auto-generated method stub
		
	}
	
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
