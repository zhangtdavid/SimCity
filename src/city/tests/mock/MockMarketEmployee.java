package city.tests.mock;

import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.Application.FOOD_ITEMS;
import city.abstracts.MockRole;
import city.interfaces.Market;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.interfaces.MarketEmployee;

public class MockMarketEmployee extends MockRole implements MarketEmployee {
	
	public EventLog log = new EventLog();
	public Market market;

	public MockMarketEmployee() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAssistCustomer(MarketCustomer c) {
		log.add(new LoggedEvent("Market Employee received msgAssistCustomer from Manager."));		
		System.out.println("Market Employee received msgAssistCustomer from Manager.");		
	}

	@Override
	public void msgAssistCustomerDelivery(MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay) {
		log.add(new LoggedEvent("Market Employee received msgAssistCustomerDelivery from Manager."));		
		System.out.println("Market Employee received msgAssistCustomerDelivery from Manager.");		
	}

	public void msgHereIsMyOrder(MarketCustomer c, Map<FOOD_ITEMS, Integer> o, int id) {
		log.add(new LoggedEvent("Market Employee received msgHereIsMyOrder from MarketCustomer."));		
		System.out.println("Market Employee received msgHereIsMyOrder from MarketCustomer.");		
	}

	@Override
	public void msgHereIsCustomerDeliveryOrder(Map<FOOD_ITEMS, Integer> o, int id) {
		log.add(new LoggedEvent("Market Employee received msgHereIsCustomerDeliveryOrder from Manager."));		
		System.out.println("Market Employee received msgHereIsCustomerDeliveryOrder from Manager.");		
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
	public MarketCustomer getMarketCustomer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarketCustomerDelivery getMarketCustomerDelivery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<FOOD_ITEMS, Integer> getOrder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getOrderId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Map<FOOD_ITEMS, Integer> getCollectedItems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarketEmployeeState getMarketEmployeeState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarketEmployeeEvent getMarketEmployeeEvent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgAnimationAtPhone() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFinishedCollectingItems() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationAtCashier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationAtCounter() {
		// TODO Auto-generated method stub
		
	}

}
