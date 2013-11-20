package city.tests.mock;

import java.util.Map;

import utilities.EventLog;
import city.MockRole;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketDeliveryPerson;

public class MockMarketDeliveryPerson extends MockRole implements MarketDeliveryPerson {
	
	public EventLog log = new EventLog();
	public MarketBuilding market;
	
	public MockMarketDeliveryPerson() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDeliverOrder(MarketCustomerDelivery customerDelivery,
			Map<String, Integer> i) {
		// TODO Auto-generated method stub
		
	}

}
