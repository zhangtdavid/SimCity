package city.tests.mock;

import java.util.Map;

import city.MockRole;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketEmployee;

public class MockMarketCustomerDelivery extends MockRole implements MarketCustomerDelivery {

	public MockMarketCustomerDelivery() {
		// TODO Auto-generated method stub
		
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
