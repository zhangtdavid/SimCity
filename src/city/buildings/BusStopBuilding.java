package city.buildings;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import city.Building;
import city.Role;
import city.gui.BusStopPanel;
import city.gui.CityRoad;
import city.gui.CityViewBusStop;
import city.roles.BusPassengerRole;

public class BusStopBuilding extends Building {
	
	// Data
	
	public BusStopBuilding nextStop = null;
	public BusStopBuilding previousStop = null;
	public List<BusPassengerRole> waitingList = Collections.synchronizedList(new ArrayList<BusPassengerRole>());
	public CityRoad roadLocatedOn;
	
	// Constructor
	
	public BusStopBuilding(String name, BusStopPanel panel, CityViewBusStop cityBuilding) {
		super(name);
		this.setPanel(panel);
		this.setCityViewBuilding(cityBuilding);
	}
	
	BusStopBuilding(String name, BusStopBuilding nextStop, BusStopBuilding previousStop) {
		super(name);
		this.nextStop = nextStop; // Assign the nextStop argument to this stop's nextStop
		nextStop.previousStop = this; // Assign the nextStop's previousStop to this
		this.previousStop = previousStop; // Assign the previousStop argument to this stop's previousStop
		previousStop.nextStop = this; // Assign the previousStop's nextStop to this
	}
	
	// Getters
	
	public BusStopBuilding(String name) {
		super(name);
	}

	public BusStopBuilding getNextStop() {
		return nextStop;
	}
	
	// Setters
	
	public void setNextStop(BusStopBuilding b) {
		nextStop = b;
	}
	
	public void setPreviousStop(BusStopBuilding b) {
		previousStop = b;
	}
	
	public void setRoad(CityRoad road) {
		this.roadLocatedOn = road;
	}
	
	// Utilities
	
	@Override
	public void addRole(Role r) {
		// TODO
		return;
	}
}
