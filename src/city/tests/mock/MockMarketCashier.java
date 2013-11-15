package city.tests.mock;

import java.util.Map;

import utilities.EventLog;
import city.Mock;
import city.interfaces.MarketCashier;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketDeliveryPerson;
import city.interfaces.MarketEmployee;

public class MockMarketCashier extends Mock implements MarketCashier {
	
	public EventLog log = new EventLog();

	public MockMarketCashier(String name) {
		super(name);
		// TODO Auto-generated constructor stub
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
