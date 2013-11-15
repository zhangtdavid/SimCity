package city.tests.mock;

import java.util.Map;

import utilities.EventLog;
import city.Mock;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketDeliveryPerson;

public class MockMarketDeliveryPerson extends Mock implements MarketDeliveryPerson {
	
	public EventLog log = new EventLog();

	public MockMarketDeliveryPerson(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgDeliverOrder(MarketCustomerDelivery customerDelivery,
			Map<String, Integer> i) {
		// TODO Auto-generated method stub
		
	}
	

}
