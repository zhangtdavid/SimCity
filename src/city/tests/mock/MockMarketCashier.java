package city.tests.mock;

import java.util.Map;

import city.MockRole;
import city.interfaces.MarketCashier;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketDeliveryPerson;
import city.interfaces.MarketEmployee;

public class MockMarketCashier extends MockRole implements MarketCashier {

	public MockMarketCashier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgComputeBill(MarketEmployee e, MarketCustomer c,
			Map<String, Integer> order, Map<String, Integer> collectedItems) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsPayment(MarketCustomer c, double money) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgComputeBill(MarketEmployee e, MarketCustomerDelivery c,
			Map<String, Integer> order, Map<String, Integer> collectedItems) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsPayment(MarketCustomerDelivery c, double money) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDeliveringItems(MarketDeliveryPerson d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFinishedDeliveringItems(MarketDeliveryPerson d) {
		// TODO Auto-generated method stub
		
	}

}
