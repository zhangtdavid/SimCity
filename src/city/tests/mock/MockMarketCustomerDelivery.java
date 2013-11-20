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
	public void msgHereIsBill(double bill) {
		log.add(new LoggedEvent("Customer received msgHereIsBill from cashier. The bill is for " + bill));		
		System.out.println("Customer received msgHereIsBill from cashier. The bill is for " + bill);		
	}

	@Override
	public void msgPaymentReceived() {
		log.add(new LoggedEvent("Customer received msgPaymentReceived from cashier."));		
		System.out.println("Customer received msgPaymentReceived from cashier.");		
	}

	@Override
	public void msgHereIsOrder(Map<String, Integer> collectedItems) {
		log.add(new LoggedEvent("Customer received msgHereIsOrder from deliveryPerson."));		
		System.out.println("Customer received msgHereIsOrder from deliveryPerson.");		
	}

}
