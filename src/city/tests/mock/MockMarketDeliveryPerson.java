package city.tests.mock;

import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
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
	public void msgDeliverOrder(MarketCustomerDelivery customerDelivery, Map<String, Integer> i) {
		log.add(new LoggedEvent("Delivery Person received msgHereIsOrder from Cashier."));		
		System.out.println("Delivery Person received msgHereIsOrder from Cashier.");		
	}

}
