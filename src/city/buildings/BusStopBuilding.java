package city.buildings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import city.Building;
import city.RoleInterface;
import city.gui.CityRoad;
import city.gui.buildings.BusStopPanel;
import city.gui.views.CityViewBusStop;
import city.interfaces.BusPassenger;
import city.interfaces.BusStop;

public class BusStopBuilding extends Building implements BusStop {
	
	// Data
	
	public BusStop nextStop = null;
	public BusStop previousStop = null;
	public List<BusPassenger> waitingList = Collections.synchronizedList(new ArrayList<BusPassenger>());
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
	
	@Override
	public List<BusPassenger> getWaitingList() {
		return waitingList;
	}

	@Override
	public BusStop getNextStop() {
		return nextStop;
	}
	
	@Override
	public CityRoad getRoadLocatedOn() {
		return roadLocatedOn;
	}
	
	// Setters
	
	@Override
	public void setNextStop(BusStop b) {
		nextStop = b;
	}
	
	@Override
	public void setPreviousStop(BusStop b) {
		previousStop = b;
	}
	
	@Override
	public void setRoad(CityRoad road) {
		this.roadLocatedOn = road;
	}
	
	// Utilities
	
	@Override
	public void addOccupyingRole(RoleInterface r) {
		// TODO
		return;
	}

	@Override
	public void addToWaitingList(BusPassenger p) {
		this.waitingList.add(p);
	}
	
	@Override
	public void removeFromWaitingList(BusPassenger p) {
		this.waitingList.remove(p);
	}
}
