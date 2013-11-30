package city.tests.mock;

import utilities.EventLog;
import utilities.LoggedEvent;
import utilities.MarketTransaction;
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
	public void msgHereIsBill(int bill, int id) {
		log.add(new LoggedEvent("CustomerDeliveryPayment received msgHereIsBill from cashier. The bill is for " + bill));		
		System.out.println("CustomerDeliveryPayment received msgHereIsBill from cashier. The bill is for " + bill);		
	}

	@Override
	public void msgPaymentReceived(int id) {
		log.add(new LoggedEvent("CustomerDeliveryPayment received msgPaymentReceived from cashier."));		
		System.out.println("CustomerDeliveryPayment received msgPaymentReceived from cashier.");		
	}

	@Override
	public MarketBuilding getMarket() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMarket(MarketBuilding market) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int checkBill(MarketTransaction mt) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void removeMarketTransactionFromList(MarketTransaction transaction) {
		// TODO Auto-generated method stub
		
	}
}
