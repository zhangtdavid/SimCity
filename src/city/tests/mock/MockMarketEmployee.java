package city.tests.mock;

import java.util.Map;

import utilities.EventLog;
import city.MockRole;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketEmployee;

public class MockMarketEmployee extends MockRole implements MarketEmployee {
	
	public EventLog log = new EventLog();
	public MarketBuilding market;

	public MockMarketEmployee() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAssistCustomer(MarketCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAssistCustomerDelivery(MarketCustomerDelivery c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyOrder(MarketCustomer c, Map<String, Integer> o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyDeliveryOrder(MarketCustomerDelivery c,
			Map<String, Integer> o) {
		// TODO Auto-generated method stub
		
	}

}
