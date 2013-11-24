package city.tests.mock;

import java.util.Map;

import utilities.EventLog;
import utilities.MarketOrder;
import city.Application.FOOD_ITEMS;
import city.MockRole;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCashier;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.interfaces.MarketDeliveryPerson;
import city.interfaces.MarketEmployee;

public class MockMarketCashier extends MockRole implements MarketCashier {

	public EventLog log = new EventLog();
	public MarketBuilding market;
	
	public MockMarketCashier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgNewDeliveryPerson(MarketDeliveryPerson d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRemoveDeliveryPerson(MarketDeliveryPerson d) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgComputeBill(MarketEmployee e, MarketCustomer c,
			Map<FOOD_ITEMS, Integer> o, Map<FOOD_ITEMS, Integer> i, int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsPayment(MarketCustomer c, int money) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgComputeBill(MarketEmployee e, MarketCustomerDelivery c,
			MarketCustomerDeliveryPayment cPay, Map<FOOD_ITEMS, Integer> o, Map<FOOD_ITEMS, Integer> i, int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsPayment(MarketCustomerDeliveryPayment c, int money) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDeliveringItems(MarketDeliveryPerson d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFinishedDeliveringItems(MarketDeliveryPerson d, MarketCustomerDelivery cd) {
		// TODO Auto-generated method stub
		
	}
}
