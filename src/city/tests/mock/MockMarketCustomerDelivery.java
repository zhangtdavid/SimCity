package city.tests.mock;

import java.util.Map;

import utilities.EventLog;
import city.Mock;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketEmployee;

public class MockMarketCustomerDelivery extends Mock implements MarketCustomerDelivery {
	
	public EventLog log = new EventLog();

	public MockMarketCustomerDelivery(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgWhatWouldYouLike(MarketEmployee e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsBill(double bill) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPaymentReceived() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsOrder(Map<String, Integer> collectedItems) {
		// TODO Auto-generated method stub
		
	}
	

}
