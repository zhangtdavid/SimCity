package city.tests.mock;

import utilities.EventLog;
import utilities.LoggedEvent;
import utilities.MarketOrder;
import city.MockRole;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCustomerDelivery;

public class MockMarketCustomerDelivery extends MockRole implements MarketCustomerDelivery {
	
	public EventLog log = new EventLog();
	public MarketBuilding market;
	
	public MockMarketCustomerDelivery() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void msgHereIsOrder(MarketOrder o) {
		log.add(new LoggedEvent("CustomerDelivery received msgHereIsOrder from deliveryPerson."));		
		System.out.println("CustomerDelivery received msgHereIsOrder from deliveryPerson.");		
	}

}
