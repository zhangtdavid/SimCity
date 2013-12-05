package city.agents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import city.agents.interfaces.Bus;
import city.animations.interfaces.AnimatedBus;
import city.bases.Agent;
import city.buildings.BusStopBuilding;
import city.buildings.interfaces.BusStop;
import city.roles.interfaces.BusPassenger;

public class BusAgent extends Agent implements Bus {

	// Data
	private BUSSTATE myState = BUSSTATE.DRIVING; // State of bus
	private BUSEVENT myEvent = BUSEVENT.ATSTOP; // Event of bus
	private List<MyBusPassenger> passengerList = Collections.synchronizedList(new ArrayList<MyBusPassenger>()); // List of bus passengers
	private BusStop currentStop; // Stop the bus is at
	private BusStop nextStop; // Stop the bus is going to
	private int earnedMoney = 0; // Amount of fare the bus earned
	private Semaphore atDestination = new Semaphore(0, true);
	private Timer timer = new Timer();
	
	// Constructor
	
	public BusAgent(BusStopBuilding currentStop_, BusStopBuilding nextStop_) {
		currentStop = currentStop_;
		nextStop = nextStop_;
	}
	
	// Messages
	
	@Override
	public void msgAtBusDestination() { // From GUI, bus is at the bus stop
		myEvent = BUSEVENT.ATSTOP;
		currentStop = nextStop;
		nextStop = currentStop.getNextStop();
		stateChanged();
	}
	
	@Override
	public void msgImOffBus(BusPassenger bp) { // From BusPassengerRole, role has gotten off bus
		for(MyBusPassenger mbp : passengerList) { // Set this role's state to OFFBUS  
			if(mbp.bp == bp) {
				mbp.myPassengerState = MyBusPassenger.MYPASSENGERSTATE.OFFBUS;
				break;
			}
		}
		stateChanged();
	}
	
	@Override
	public void msgImOnBus(BusPassenger bp, BusStop dest) { // From BusPassengerRole, role has gotten on bus
		earnedMoney += BUS_FARE; // Add fare to money
		synchronized(passengerList) {
			for(MyBusPassenger mbp : passengerList) { // Set this role's state to ONBUS and its destination
				if(mbp.bp == bp) {
					mbp.myPassengerState = MyBusPassenger.MYPASSENGERSTATE.ONBUS;
					mbp.destination = dest;
					break;
				}
			}
		}
		stateChanged();
	}
	
	@Override
	public void msgAtDestination() {
		atDestination.release();
	}
	
	// Scheduler
	
	@Override
	public boolean runScheduler() {
		if(myState == BUSSTATE.DRIVING && myEvent == BUSEVENT.ATSTOP) { // Bus has arrived at stop
			myState = BUSSTATE.DROPPINGPASSENGERSOFF;
			notifyPassengersToDropOff(); // Drop off passengers that are getting off here
			return true;
		}
		synchronized(passengerList) {
			for(MyBusPassenger mbp1 : passengerList) { // Bus is unloading roles
				if(mbp1.myPassengerState == MyBusPassenger.MYPASSENGERSTATE.OFFBUS) { // If a role is off the bus
					passengerList.remove(mbp1); // Remove this role from the passengerList
					for(MyBusPassenger mbp2 : passengerList) {
						if(mbp2.myPassengerState == MyBusPassenger.MYPASSENGERSTATE.GETTINGOFFBUS) // If there are still roles to get off, rerun scheduler
							return true;
					}
					// If there aren't people to get off, tell roles at stop to get on
					myState = BUSSTATE.PICKINGPEOPLEUP;
					notifyPassengersAtStopToGetOn();
					return true;
				}
			}
			for(MyBusPassenger mbp1 : passengerList) { // Bus is loading roles
				if(mbp1.myPassengerState == MyBusPassenger.MYPASSENGERSTATE.ONBUS) { // If a role is on the bus
					for(MyBusPassenger mbp2 : passengerList) { // Check if any roles are getting on the bus
						if(mbp2.myPassengerState == MyBusPassenger.MYPASSENGERSTATE.GETTINGONBUS) // If a role is getting on the bus
							return true; // Rerun scheduler
					}
					// If all roles at the bus stop are loaded, set bus state/event to driving and drive to next stop
					myState = BUSSTATE.DRIVING;
					myEvent = BUSEVENT.NONE;
					driveToNextStop();
					return true;
				}
			}
		}
		return false;
	}
	
	// Actions
	
	private void notifyPassengersToDropOff() { // Tells passengers on the bus with the same currentStop to get off
		print("Notifying passengers to be dropped off");
		boolean passengersDroppedOff = false; // Flag to see if passengers got off
		synchronized(passengerList) {
			for(MyBusPassenger mbp : passengerList) {
				if(mbp.destination == currentStop) { // If a passenger has the same stop
					print("Dropping off passenger " + mbp.bp.getPerson().getName() + " at " + currentStop.getName());
					passengersDroppedOff = true; // Set flag to true
					mbp.myPassengerState = MyBusPassenger.MYPASSENGERSTATE.GETTINGOFFBUS; // Set this passenger's state to getting off
					mbp.bp.msgImAtYourDestination(); // Message this passenger to get off
				}
			}
		}
		// If no passengers were dropped off, tell people at stop to get on
		if(passengersDroppedOff == false) {
			print("No passengers getting off at " + currentStop.getName());
			myState = BUSSTATE.PICKINGPEOPLEUP;
			notifyPassengersAtStopToGetOn();
		}
	}

	private void notifyPassengersAtStopToGetOn() { // Tells passengers at stop to get on the bus
		print("Notifying passengers at stop to get on");
		boolean passengersPickedUp = false; // Flag to see if passengers got on
		synchronized(currentStop.getWaitingList()) {
			for(BusPassenger bp : currentStop.getWaitingList()) {
				print("Picking up passenger " + bp.getPerson().getName() + " at " + currentStop.getName());
				passengersPickedUp = true; // Set flag to true
				passengerList.add(new MyBusPassenger(bp)); // Add this passenger to the passengerList
				bp.msgBusIsHere(this); // Message this passenger to get on
			}
		}
		// Wait for bus
		timer.schedule(new TimerTask() {
			public void run() {
				print("Finished waiting for passengers");
				atDestination.release();
			}
		}, (long)2000);
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// If no passengers were picked up
		if(passengersPickedUp == false) {
			print("No passengers picked up at " + currentStop.getName());
			myState = BUSSTATE.DRIVING; // Set bus state and event to driving and drive to next stop
			myEvent = BUSEVENT.NONE;
			driveToNextStop();
		}
	}

	private void driveToNextStop() { // Tells 
		print("Driving to stop " + nextStop.getName());
		((AnimatedBus) this.getAnimation()).doGoToNextStop(nextStop); // Calls msgAtBusDestination() when finished
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		msgAtBusDestination();
	}
	
	// Getters
	
	@Override
	public BUSSTATE getState() {
		return myState;
	}
	
	@Override
	public BUSEVENT getEvent() {
		return myEvent;
	}
	
	@Override
	public List<MyBusPassenger> getPassengerList() {
		return passengerList;
	}
	
	@Override
	public BusStop getCurrentStop() {
		return currentStop;
	}
	
	@Override
	public BusStop getNextStop() {
		return nextStop;
	}
	
	@Override
	public int getEarnedMoney() {
		return earnedMoney;
	}
	
	// Setters
	
	// Utilities
	
	@Override
	public void startThread() {
		super.startThread();
		stateChanged();
	}
	
	@Override
	public void print(String msg) {
        AlertLog.getInstance().logMessage(AlertTag.BUS, "BusAgent " + this.getName(), msg);
    }
	
	// Classes

	public static class MyBusPassenger {
		public enum MYPASSENGERSTATE {GETTINGONBUS, ONBUS, GETTINGOFFBUS, OFFBUS};
		private MYPASSENGERSTATE myPassengerState = MYPASSENGERSTATE.GETTINGONBUS;
		private BusPassenger bp;
		private BusStop destination;
		
		MyBusPassenger(BusPassenger bp_) {
			bp = bp_;
		}
		
		// Getters
		
		public MYPASSENGERSTATE getState() {
			return myPassengerState;
		}
		
		public BusPassenger getPassenger() {
			return bp;
		}
		
		public BusStop getDestination() {
			return destination;
		}
		
		// Setters
		
		public void setState(MYPASSENGERSTATE newState) {
			myPassengerState = newState;
		}
		
		public void setPassenger(BusPassenger newPassenger) {
			bp = newPassenger;
		}
		
		public void setDestination(BusStop newDestination) {
			destination = newDestination;
		}
	}
}
