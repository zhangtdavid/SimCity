package city.tests.mock;

import java.util.Map;

import utilities.EventLog;
import city.MockRole;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.interfaces.MarketEmployee;
import city.interfaces.MarketManager;
import city.Application.FOOD_ITEMS;

public class MockMarketManager extends MockRole implements MarketManager {
	
	public EventLog log = new EventLog();
	public MarketBuilding market;
	
	@Override
	public void msgNewEmployee(MarketEmployee e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRemoveEmployee(MarketEmployee e) {
		// TODO Auto-generated method stub
		
	}
	
	public MockMarketManager() {
		// TODO Auto-generated method stub
	}

	@Override
	public void msgIWouldLikeToPlaceAnOrder(MarketCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWouldLikeToPlaceADeliveryOrder(MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay, Map<FOOD_ITEMS, Integer> o, int id) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgWhatWouldCustomerDeliveryLike(MarketEmployee marketEmployee) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIAmAvailableToAssist(MarketEmployee e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgItemLow() {
		// TODO Auto-generated method stub
		
	}
}
