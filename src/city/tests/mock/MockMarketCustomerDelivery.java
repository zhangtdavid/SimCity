package city.tests.mock;

import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.MockRole;
import city.Application.FOOD_ITEMS;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCustomerDelivery;

public class MockMarketCustomerDelivery extends MockRole implements MarketCustomerDelivery {
	
	public EventLog log = new EventLog();
	public MarketBuilding market;
	
	public MockMarketCustomerDelivery() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void msgHereIsOrder(Map<FOOD_ITEMS, Integer> collectedItems, int id) {
		log.add(new LoggedEvent("CustomerDelivery received msgHereIsOrder from deliveryPerson."));		
		System.out.println("CustomerDelivery received msgHereIsOrder from deliveryPerson.");		
	}

}
