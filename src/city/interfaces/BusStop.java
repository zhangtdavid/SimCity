package city.interfaces;

import java.util.List;

import city.BuildingInterface;
import city.gui.CityRoad;

public interface BusStop extends BuildingInterface {

	public BusStop getNextStop();
	
	public List<BusPassenger> getWaitingList();

	public void setNextStop(BusStop b);

	public void setPreviousStop(BusStop b);

	public void setRoad(CityRoad road);
	
	public void addToWaitingList(BusPassenger p);
	
	public void removeFromWaitingList(BusPassenger p);

}