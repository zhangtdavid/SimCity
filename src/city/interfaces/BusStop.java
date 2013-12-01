package city.interfaces;

import city.BuildingInterface;
import city.buildings.BusStopBuilding;
import city.gui.CityRoad;

public interface BusStop extends BuildingInterface {

	public BusStop getNextStop();

	public void setNextStop(BusStopBuilding b);

	public void setPreviousStop(BusStopBuilding b);

	public void setRoad(CityRoad road);

}