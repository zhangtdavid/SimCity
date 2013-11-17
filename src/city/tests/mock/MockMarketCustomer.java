package city.tests.mock;

import java.util.Map;

import city.MockRole;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketEmployee;

public class MockMarketCustomer extends MockRole implements MarketCustomer {

	public MockMarketCustomer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationArrivedAtMarket() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatWouldYouLike(MarketEmployee e, int loc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsOrderandBill(Map<String, Integer> collectedItems,
			double bill) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPaymentReceived() {
		// TODO Auto-generated method stub
		
	}

}
