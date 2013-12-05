package city.buildings;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import city.Application;
import city.Building;
import city.RoleInterface;
import city.gui.CityRoad;
import city.gui.buildings.BusStopPanel;
import city.gui.views.CityViewBuilding;
import city.gui.views.CityViewBusStop;
import city.interfaces.BusPassenger;
import city.interfaces.BusStop;

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
	
	public BusStopBuilding(String name) {
		super(name, new BusStopPanel(Color.black), new CityViewBusStop(1, 1));
	};
	
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
