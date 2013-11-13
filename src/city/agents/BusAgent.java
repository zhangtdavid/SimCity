package city.agents;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import city.Agent;
import city.interfaces.Bus;
import city.roles.BusPassengerRole;

public class BusAgent extends Agent implements Bus {

	// Data
	enum BusState {DRIVING, DROPPINGPASSENGERSOFF, PICKINGPEOPLEUP};
	BusState myState = BusState.DRIVING;
	enum BusEvent {NONE, ATSTOP};
	BusEvent myEvent = BusEvent.NONE;
	List<MyBusPassenger> passengerList = Collections.synchronizedList(new ArrayList<MyBusPassenger>());
	CityMap busMap;
	Building currentStop;
	Building nextStop;
	static final double busFare = 1.50;
	double earnedMoney = 0.00;
	BusGui myGui;
	
	// Constructor
	BusAgent(Building currentStop_, Building nextStop_) {
		currentStop = currentStop_;
		nextStop = nextStop_;
	}
	
	// Messages
	void msgAtBusDestination() {
		myEvent = BusEvent.ATSTOP;
		currentStop = nextStop;
		nextStop = busMap.getNextStopOf(currentStop);
		stateChanged();
	}

	void msgImOffBus(BusPassengerRole bpr) {
		earnedMoney += busFare;
		for(MyBusPassenger mbp : passengerList) {
			if(mbp.bpr == bpr) {
				mbp.myPassengerState = PassengerState.OFFBUS;
				break;
			}
		}
		stateChanged();
	}

	void msgImOnBus(BusPassengerRole bpr, Location dest) {
		earnedMoney += busFare;
		for(MyBusPassenger mbp : passengerList) {
			if(mbp.bpr == bpr) {
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
		if(myState == BusState.DRIVING && myEvent == BusEvent.ATSTOP) {
			myState = BusState.DROPPINGPASSENGERSOFF;
			notifyPassengersToDropOff();
			return true;
		}
		for(MyBusPassenger mbp1 : passengerList) {
			if(mbp1.myPassengerState == PassengerState.OFFBUS) {
				passengerList.remove(mbp1);
				for(MyBusPassenger mbp2 : passengerList) {
					if(mbp2.myPassengerState == PassengerState.GETTINGOFFBUS)
						return true;
				}
				myState = BusState.PICKINGPEOPLEUP;
				notifyPassengersAtStopToGetOn();
				return true;
			}
		}
		for(MyBusPassenger mbp1 : passengerList) {
			if(mbp1.myPassengerState == PassengerState.ONBUS) {
				passengerList.remove(mbp1);
				for(MyBusPassenger mbp2 : passengerList) {
					if(mbp2.myPassengerState == PassengerState.GETTINGONBUS)
						return true;
				}
				myState = BusState.DRIVING;
				myEvent = BusEvent.NONE;
				driveToNextStop();
				return true;
			}
		}
		return false;
	}
	
	// Actions
	void notifyPassengersToDropOff() {
		boolean passengersDroppedOff = false;
		for(MyBusPassenger mbp : passengerList) {
			if(mbp.destination == currentStop) {
				passengersDroppedOff = true;
				mbp.myPassengerState = PassengerState.GETTINGOFFBUS;
				mbp.msgImAtYourDestination();
			}
		}
		if(passengersDroppedOff == false) {
			myState = BusState.PICKINGPEOPLEUP;
			notifyPassengersAtStopToGetOn();
		}
	}

	void notifyPassengersAtStopToGetOn() {
		boolean passengersPickedUp = false;
		for(BusPassengerRole bpr : currentStop.waitingList) {
			passengersPickedUp = true;
			bpr.msgBusIsHere(this);
			passengerList.add(new MyBusPassenger(bpr)); // set state of my myBusPassenger to GETTINGONBUS
		}
		if(passengersPickedUp == false) {
			myState = BusState.DRIVING;
			myEvent = BusEvent.NONE;
			driveToNextStop();
		}
	}

	void driveToNextStop() {
		myGui.DoGoToNextStop(nextStop); // Calls msgAtBusDestination() when finished
	}
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
	enum PassengerState {GETTINGONBUS, ONBUS, GETTINGOFFBUS, OFFBUS};
	class MyBusPassenger {
		PassengerState myPassengerState = PassengerState.GETTINGONBUS;
		BusPassengerRole bpr;
		Building destination;
		
		MyBusPassenger(BusPassengerRole bpr_) {
			bpr = bpr_;
		}
	}
}
