package city.tests.buildings.mocks;

import java.beans.PropertyChangeSupport;
import java.util.List;

import city.buildings.interfaces.BusStop;
import city.gui.CityRoad;
import city.roles.interfaces.BusPassenger;
import city.tests.bases.mocks.MockBuilding;

public class MockBusStop extends MockBuilding implements BusStop {

	public MockBusStop(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public BusStop getNextStop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNextStop(BusStop b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPreviousStop(BusStop b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRoad(CityRoad road) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<BusPassenger> getWaitingList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addToWaitingList(BusPassenger p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeFromWaitingList(BusPassenger p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CityRoad getRoadLocatedOn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PropertyChangeSupport getPropertyChangeSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getBusinessIsOpen() {
		// TODO Auto-generated method stub
		return false;
	}

}
