package city.tests.mock;

import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.MockRole;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketEmployee;

public class MockMarketCustomerDelivery extends MockRole implements MarketCustomerDelivery {
	
	public EventLog log = new EventLog();
	public MarketBuilding market;
	
	public MockMarketCustomerDelivery() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgWhatWouldYouLike(MarketEmployee e) {
		log.add(new LoggedEvent("Customer received msgHereIsOrder from employee."));		
		System.out.println("Customer received msgHereIsOrder from employee.");		
	}

	@Override
	public void msgHereIsOrder(Map<String, Integer> collectedItems) {
		log.add(new LoggedEvent("Customer received msgHereIsOrder from deliveryPerson."));		
		System.out.println("Customer received msgHereIsOrder from deliveryPerson.");		
	}

}
