package city.tests.mock;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.MockRole;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCustomerDeliveryPayment;

public class MockMarketCustomerDeliveryPayment extends MockRole implements MarketCustomerDeliveryPayment {
	
	public EventLog log = new EventLog();
	public MarketBuilding market;
	
	public MockMarketCustomerDeliveryPayment() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsBill(int bill) {
		log.add(new LoggedEvent("Customer received msgHereIsBill from cashier. The bill is for " + bill));		
		System.out.println("Customer received msgHereIsBill from cashier. The bill is for " + bill);		
	}

	@Override
	public void msgPaymentReceived() {
		log.add(new LoggedEvent("Customer received msgPaymentReceived from cashier."));		
		System.out.println("Customer received msgPaymentReceived from cashier.");		
	}

}
