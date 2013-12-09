package city.buildings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import city.Application;
import city.bases.Building;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.BusStop;
import city.gui.CityRoad;
import city.gui.exteriors.CityViewBuilding;
import city.gui.interiors.BusStopPanel;
import city.roles.interfaces.BusPassenger;

public class BusStopBuilding extends Building implements BusStop {
	
	// Data
	
	public BusStop nextStop = null;
	public BusStop previousStop = null;
	public List<BusPassenger> waitingList = Collections.synchronizedList(new ArrayList<BusPassenger>());
	private CityRoad roadLocatedOn;
	
	// Constructor
	
	public BusStopBuilding(String name, BusStopPanel panel, CityViewBuilding cityBuilding) {
		super(name, panel, cityBuilding);
		roadLocatedOn = Application.CityMap.findClosestRoad(this);
	}
	
	// Getters
	
	@Override
	public boolean getBusinessIsOpen() {
		return false;
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
