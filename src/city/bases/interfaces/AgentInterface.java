package city.bases.interfaces;

/**
 * This interface specifies the absolute minimum an interface or class representing an agent must provide.
 * 
 * The following code uses AgentInterface:
 * 		- The Agent class itself implements this interface simply as sanity test.
 * 		- Interfaces which are only ever implemented as agents (Person, Car, Bus) extend this
 * 		  interface. This guarantees that only classes that are (or that mock) real agents will 
 * 		  be allowed to implement those interfaces.
 * 		- MockAgent implements this interface. This guarantees that mocks of agents act exactly
 * 		  like their interfaces.
 */
public interface AgentInterface {

	// Data
	
	// Constructor
	
	// Messages
	
	// Scheduler
	
	public boolean runScheduler() throws InterruptedException;
	
	// Actions
	
	// Getters
	
	public String getName();
	public AnimationInterface getAnimation();
	
	// Setters
	
	public void setAnimation(AnimationInterface a);
	
	// Utilities
	
	public void stateChanged();
    public void print(String msg);
    public void print(String msg, Throwable e);
    public void startThread();
    public void stopThread();
    public void pauseOrResumeThread();
    
    // Classes

}
