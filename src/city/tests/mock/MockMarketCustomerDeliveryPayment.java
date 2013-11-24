package city.tests.mock;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.MockRole;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCashier;
import city.interfaces.MarketCustomerDeliveryPayment;

public class MockMarketCustomerDeliveryPayment extends MockRole implements MarketCustomerDeliveryPayment {
	
	public EventLog log = new EventLog();
	public MarketBuilding market;
	
	public MockMarketCustomerDeliveryPayment() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsBill(MarketCashier c, int bill, int id) {
		log.add(new LoggedEvent("CustomerDeliveryPayment received msgHereIsBill from cashier. The bill is for " + bill));		
		System.out.println("CustomerDeliveryPayment received msgHereIsBill from cashier. The bill is for " + bill);		
	}

	@Override
	public void msgPaymentReceived(int id) {
		log.add(new LoggedEvent("CustomerDeliveryPayment received msgPaymentReceived from cashier."));		
		System.out.println("CustomerDeliveryPayment received msgPaymentReceived from cashier.");		
	}

}
