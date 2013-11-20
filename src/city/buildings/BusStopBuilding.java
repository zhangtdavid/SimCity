package city.buildings;

import java.util.List;
import java.util.ArrayList;

import city.Building;
import city.roles.BusPassengerRole;

public class BusStopBuilding extends Building {
	BusStopBuilding nextStop = null;
	BusStopBuilding previousStop = null;
	public List<BusPassengerRole> waitingList = new ArrayList<BusPassengerRole>();
	
	public BusStopBuilding(String name) {
		super(name, null);
	}
	
	BusStopBuilding(String name, BusStopBuilding nextStop, BusStopBuilding previousStop) {
		super(name, null);
		this.nextStop = nextStop; // Assign the nextStop argument to this stop's nextStop
		nextStop.previousStop = this; // Assign the nextStop's previousStop to this
		this.previousStop = previousStop; // Assign the previousStop argument to this stop's previousStop
		previousStop.nextStop = this; // Assign the previousStop's nextStop to this
	}
	
	public BusStopBuilding getNextStop() {
		return nextStop;
	}
}
