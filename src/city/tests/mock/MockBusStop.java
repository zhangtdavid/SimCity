package city.tests.mock;

import java.util.List;

import city.abstracts.MockBuilding;
import city.gui.CityRoad;
import city.interfaces.BusPassenger;
import city.interfaces.BusStop;

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

}
