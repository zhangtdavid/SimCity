package city;

import java.util.concurrent.Semaphore;

import utilities.StringUtil;
import city.interfaces.AbstractAgent;

/**
 * The base class for all SimCity201 agents.
 */
public abstract class Agent implements AbstractAgent {
	
    private Semaphore stateChange = new Semaphore(1, true);
    private AgentThread agentThread;
    
    protected Agent() { }
    
    /**
     * Agents must implement this scheduler to perform any actions appropriate for the
     * current state.  Will be called whenever a state change has occurred,
     * and will be called repeated as long as it returns true.
     *
     * @return true iff some action was executed that might have changed the state.
     */
    public abstract boolean runScheduler();
    
    /**
     * Return agent name for messages.  Default is to return Java instance name.
     * 
     * @return String the agent name or the Java instance name.
     */
    public String getName() {
        return StringUtil.shortName(this);
    }
    
    /**
     * Agent code calls this method to activate the scheduler.
     */
    public void stateChanged() {
        stateChange.release();
    }

    /**
     * Print message.
     */
    public void print(String msg) {
        print(msg, null);
    }

    /**
     * Print message with exception stack trace.
     */
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
     * Start agent scheduler thread.  Should be called once when agent is created.
     */
    public synchronized void startThread() {
        if (agentThread == null) {
            agentThread = new AgentThread(getName());
            agentThread.start();
        } else {
            agentThread.interrupt();
        }
    }

    /**
     * Stop agent scheduler thread.
     */
    public void stopThread() {
        if (agentThread != null) {
            agentThread.stopAgent();
            agentThread = null;
        }
    }
    
    /**
     * Pause and resume the scheduler thread.
     */
    public void pauseResume() {
    	agentThread.pauseResume();
    }

    /**
     * Agent scheduler thread, calls respondToStateChange() whenever a state change has been signaled.
     */
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
        
        public void pauseResume() {
        	doGoOn = !doGoOn;
        }

        private void stopAgent() {
            goOn = false;
            this.interrupt();
        }
    }
}
