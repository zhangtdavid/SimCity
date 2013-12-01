package city.buildings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import city.Building;
import city.RoleInterface;
import city.gui.CityRoad;
import city.gui.buildings.BusStopPanel;
import city.gui.views.CityViewBusStop;
import city.interfaces.BusStop;
import city.roles.BusPassengerRole;

public class BusStopBuilding extends Building implements BusStop {
	
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

	@Override
	public BusStopBuilding getNextStop() {
		return nextStop;
	}
	
	// Setters
	
	@Override
	public void setNextStop(BusStopBuilding b) {
		nextStop = b;
	}
	
	@Override
	public void setPreviousStop(BusStopBuilding b) {
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
}
