package city.tests.roles.mocks;

import java.beans.PropertyChangeSupport;

import utilities.EventLog;
import utilities.LoggedEvent;
import utilities.MarketTransaction;
import city.buildings.interfaces.Market;
import city.roles.interfaces.MarketCustomerDeliveryPayment;
import city.tests.bases.mocks.MockRole;

public class MockMarketCustomerDeliveryPayment extends MockRole implements MarketCustomerDeliveryPayment {
	
	public EventLog log = new EventLog();
	public Market market;
	
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
	public Market getMarket() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMarket(Market market) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int checkBill(MarketTransaction mt) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MarketTransaction findMarketTransaction(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PropertyChangeSupport getPropertyChangeSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStateString() {
		// TODO Auto-generated method stub
		return null;
	}
}
