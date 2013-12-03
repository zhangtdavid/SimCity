package city.tests.mock;

import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
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
		log.add(new LoggedEvent("Market Manager received msgIWouldLikeToPlaceAnOrder from MarketCustomer."));		
		System.out.println("Market Manager received msgIWouldLikeToPlaceAnOrder from MarketCustomer.");				
	}

	@Override
	public void msgIWouldLikeToPlaceADeliveryOrder(MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay, Map<FOOD_ITEMS, Integer> o, int id) {
		log.add(new LoggedEvent("Market Manager received msgIWouldLikeToPlaceADeliveryOrder from Market Customer Delivery."));		
		System.out.println("Market Manager received msgIWouldLikeToPlaceADeliveryOrder from Market Customer Delivery.");				
	}
	
	@Override
	public void msgWhatWouldCustomerDeliveryLike(MarketEmployee marketEmployee) {
		log.add(new LoggedEvent("Market Manager received msgWhatWouldCustomerDeliveryLike from Market Employee."));		
		System.out.println("Market Manager received msgWhatWouldCustomerDeliveryLike from Market Employee.");		
	}

	@Override
	public void msgIAmAvailableToAssist(MarketEmployee e) {
		log.add(new LoggedEvent("Market Manager received msgIAmAvailableToAssist from Market Employee."));		
		System.out.println("Market Manager received msgIAmAvailableToAssist from Market Employee.");		
	}

	@Override
	public void msgItemLow() {
		log.add(new LoggedEvent("Market Manager received msgIAmAvailableToAssist from Market Employee."));		
		System.out.println("Market Manager received msgIAmAvailableToAssist from Market Employee.");				
	}

	@Override
	public MarketBuilding getMarket() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMarket(MarketBuilding market) {
		// TODO Auto-generated method stub
		
	}
}
