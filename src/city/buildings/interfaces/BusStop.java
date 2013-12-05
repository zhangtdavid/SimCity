package city.buildings.interfaces;

import java.util.List;

import city.bases.interfaces.BuildingInterface;
import city.gui.CityRoad;
import city.roles.interfaces.BusPassenger;

public interface BusStop extends BuildingInterface {

	public BusStop getNextStop();
	
	public List<BusPassenger> getWaitingList();
	
	public CityRoad getRoadLocatedOn();

	public void setNextStop(BusStop b);

	public void setPreviousStop(BusStop b);

	public void setRoad(CityRoad road);
	
	public void addToWaitingList(BusPassenger p);
	
	public void removeFromWaitingList(BusPassenger p);

}