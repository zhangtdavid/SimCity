package city.agents;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import city.Agent;
import city.interfaces.Bus;
import city.interfaces.BusPassenger;
import city.animations.interfaces.AnimatedBus;
import city.buildings.BusStopBuilding;

public class BusAgent extends Agent implements Bus {

	// Data
	public enum BusState {DRIVING, DROPPINGPASSENGERSOFF, PICKINGPEOPLEUP};
	public BusState myState = BusState.DRIVING; // State of bus
	public enum BusEvent {NONE, ATSTOP};
	public BusEvent myEvent = BusEvent.ATSTOP; // Event of bus
	public List<MyBusPassenger> passengerList = Collections.synchronizedList(new ArrayList<MyBusPassenger>()); // List of bus passengers
	public BusStopBuilding currentStop; // Stop the bus is at
	public BusStopBuilding nextStop; // Stop the bus is going to
	public static final double busFare = 1.50; // Fare price of bus
	public double earnedMoney = 0.00; // Amount of fare the bus earned
	public AnimatedBus animation;
	
	private Semaphore atDestination = new Semaphore(0, true);
	
	// Constructor
	public BusAgent(BusStopBuilding currentStop_, BusStopBuilding nextStop_) {
		currentStop = currentStop_;
		nextStop = nextStop_;
	}
	
	// Messages
	public void msgAtBusDestination() { // From GUI, bus is at the bus stop
		myEvent = BusEvent.ATSTOP;
		currentStop = nextStop;
		nextStop = currentStop.getNextStop();
		stateChanged();
	}

	public void msgImOffBus(BusPassenger bp) { // From BusPassengerRole, role has gotten off bus
		for(MyBusPassenger mbp : passengerList) { // Set this role's state to OFFBUS  
			if(mbp.bp == bp) {
				mbp.myPassengerState = PassengerState.OFFBUS;
				break;
			}
		}
		stateChanged();
	}

	public void msgImOnBus(BusPassenger bp, BusStopBuilding dest) { // From BusPassengerRole, role has gotten on bus
		earnedMoney += busFare; // Add fare to money
		for(MyBusPassenger mbp : passengerList) { // Set this role's state to ONBUS and its destination
			if(mbp.bp == bp) {
				mbp.myPassengerState = PassengerState.ONBUS;
				mbp.destination = dest;
				break;
			}
		}
		stateChanged();
	}
	
	// Scheduler
	
	@Override
	public boolean runScheduler() {
		if(myState == BusState.DRIVING && myEvent == BusEvent.ATSTOP) { // Bus has arrived at stop
			myState = BusState.DROPPINGPASSENGERSOFF;
			notifyPassengersToDropOff(); // Drop off passengers that are getting off here
			return true;
		}
		for(MyBusPassenger mbp1 : passengerList) { // Bus is unloading roles
			if(mbp1.myPassengerState == PassengerState.OFFBUS) { // If a role is off the bus
				passengerList.remove(mbp1); // Remove this role from the passengerList
				for(MyBusPassenger mbp2 : passengerList) {
					if(mbp2.myPassengerState == PassengerState.GETTINGOFFBUS) // If there are still roles to get off, rerun scheduler
						return true;
				}
				// If there aren't people to get off, tell roles at stop to get on
				myState = BusState.PICKINGPEOPLEUP;
				notifyPassengersAtStopToGetOn();
				return true;
			}
		}
		for(MyBusPassenger mbp1 : passengerList) { // Bus is loading roles
			if(mbp1.myPassengerState == PassengerState.ONBUS) { // If a role is on the bus
				for(MyBusPassenger mbp2 : passengerList) { // Check if any roles are getting on the bus
					if(mbp2.myPassengerState == PassengerState.GETTINGONBUS) // If a role is getting on the bus
						return true; // Rerun scheduler
				}
				// If all roles at the bus stop are loaded, set bus state/event to driving and drive to next stop
				myState = BusState.DRIVING;
				myEvent = BusEvent.NONE;
				driveToNextStop();
				return true;
			}
		}
		return false;
	}
	
	// Actions
	void notifyPassengersToDropOff() { // Tells passengers on the bus with the same currentStop to get off
		boolean passengersDroppedOff = false; // Flag to see if passengers got off
		for(MyBusPassenger mbp : passengerList) {
			if(mbp.destination == currentStop) { // If a passenger has the same stop
				passengersDroppedOff = true; // Set flag to true
				mbp.myPassengerState = PassengerState.GETTINGOFFBUS; // Set this passenger's state to getting off
				mbp.bp.msgImAtYourDestination(); // Message this passenger to get off
			}
		}
		// If no passengers were dropped off, tell people at stop to get on
		if(passengersDroppedOff == false) {
			myState = BusState.PICKINGPEOPLEUP;
			notifyPassengersAtStopToGetOn();
		}
	}

	void notifyPassengersAtStopToGetOn() { // Tells passengers at stop to get on the bus
		boolean passengersPickedUp = false; // Flag to see if passengers got on
		for(BusPassenger bp : currentStop.waitingList) {
			passengersPickedUp = true; // Set flag to true
			passengerList.add(new MyBusPassenger(bp)); // Add this passenger to the passengerList
			bp.msgBusIsHere(this); // Message this passenger to get on
		}
		// If no passengers were picked up
		if(passengersPickedUp == false) {
			myState = BusState.DRIVING; // Set bus state and event to driving and drive to next stop
			myEvent = BusEvent.NONE;
			driveToNextStop();
		}
	}

	void driveToNextStop() { // Tells 
		animation.DoGoToNextStop(nextStop); // Calls msgAtBusDestination() when finished
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		msgAtBusDestination();
	}
	// Getters
	
	// Setters
	public void setAnimation(AnimatedBus anim) {
		animation = anim;
	}
	
	// Utilities
	public void startThread() {
		super.startThread();
		stateChanged();
	}
	
	@Override
	public void msgAtDestination() {
		atDestination.release();
	}
	
	// Classes
	enum PassengerState {GETTINGONBUS, ONBUS, GETTINGOFFBUS, OFFBUS};
	class MyBusPassenger {
		PassengerState myPassengerState = PassengerState.GETTINGONBUS;
		BusPassenger bp;
		BusStopBuilding destination;
		
		MyBusPassenger(BusPassenger bp_) {
			bp = bp_;
		}
	}
}
