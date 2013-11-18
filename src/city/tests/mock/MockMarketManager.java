package city.tests.mock;

import utilities.EventLog;
import city.Mock;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketEmployee;
import city.interfaces.MarketManager;

public class MockMarketManager extends Mock implements MarketManager {
	
	public MockMarketManager(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public EventLog log = new EventLog();

	@Override
	public void msgIWouldLikeToPlaceAnOrder(MarketCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWouldLikeToPlaceADeliveryOrder(MarketCustomerDelivery c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIAmAvailableToAssist(MarketEmployee e) {
		// TODO Auto-generated method stub
		
	}
}
