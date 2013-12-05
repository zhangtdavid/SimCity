package city.bases;

import java.util.concurrent.Semaphore;

import city.bases.interfaces.AgentInterface;
import city.bases.interfaces.AnimationInterface;
import utilities.StringUtil;

/**
 * The base class for all SimCity201 agents.
 */
public abstract class Agent implements AgentInterface {
	
	// Data
	
    private Semaphore stateChange = new Semaphore(1, true);
    private AgentThread agentThread;
    private AnimationInterface animation;
    
    // Constructor
    
    // Messages
    
    // Scheduler
    
    /**
     * Agents must implement this scheduler to perform any actions appropriate for the
     * current state.  Will be called whenever a state change has occurred,
     * and will be called repeated as long as it returns true.
     *
     * @throws InterruptedException 
     * @return true iff some action was executed that might have changed the state.
     */
   public abstract boolean runScheduler() throws InterruptedException;
    
    // Actions
    
    // Getters
    
    /**
     * Return agent name for messages.  Default is to return Java instance name.
     * 
     * @return String the agent name or the Java instance name.
     */
    @Override
    public String getName() {
        return StringUtil.shortName(this);
    }
    
    @Override
    public AnimationInterface getAnimation() {
    	return this.animation;
    }
    
    // Setters
    
    @Override
    public void setAnimation(AnimationInterface a) {
    	this.animation = a;
    }
    
    // Utilities
    
    /**
     * Agent code calls this method to activate the scheduler.
     */
    @Override
    public void stateChanged() {
        stateChange.release();
    }

    /**
     * Print message.
     */
    @Override
	public void print(String msg) {
        print(msg, null);
    }

    /**
     * Print message with exception stack trace.
     */
    @Override
    public void print(String msg, Throwable e) {
        StringBuffer sb = new StringBuffer();
        sb.append(getName());
        sb.append(": ");
        sb.append(msg);
        sb.append("\n");
        if (e != null) {
            sb.append(StringUtil.stackTraceString(e));
        }
        System.out.print(sb.toString());
    }

    /**
     * Start agent thread.  Should be called once when agent is created.
     */
    @Override
    public synchronized void startThread() {
        if (agentThread == null) {
            agentThread = new AgentThread(getName());
            agentThread.start();
        } else {
            agentThread.interrupt();
        }
    }

    /**
     * Stop agent thread.
     */
    @Override
    public void stopThread() {
        if (agentThread != null) {
            agentThread.stopAgent();
            agentThread = null;
        }
    }
    
    /**
     * Pause and resume agent thread.
     */
    public void pauseOrResumeThread() {
    	agentThread.pauseResume();
    }
    
    // Classes

    private class AgentThread extends Thread {
        private volatile boolean goOn = false;
        private boolean doGoOn = true;

        private AgentThread(String name) {
            super(name);
        }

        public void run() {
            goOn = true;

            while (goOn) {
                try {
                	if (doGoOn) {
	                    stateChange.acquire();
	                    while (runScheduler()) ;
                	}
                } catch (InterruptedException e) {
                	// Nothing
                } catch (Exception e) {
                    print("Unexpected exception caught in Agent thread:", e);
                }
            }
        }
        
        private void pauseResume() {
        	doGoOn = !doGoOn;
        }

        private void stopAgent() {
            goOn = false;
            this.interrupt();
        }
    }
}
