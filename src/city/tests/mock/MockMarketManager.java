package city.tests.mock;

import city.MockRole;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketEmployee;
import city.interfaces.MarketManager;

public class MockMarketManager extends MockRole implements MarketManager {

	public MockMarketManager() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWouldLikeToPlaceAnOrder(MarketCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWouldLikeToPlaceADeliveryOrder(MarketCustomerDelivery c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIAmAvailableToAssist(MarketEmployee e) {
		// TODO Auto-generated method stub
		
	}

}
