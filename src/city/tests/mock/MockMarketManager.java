package city.tests.mock;

import java.util.List;
import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.abstracts.MockRole;
import city.buildings.MarketBuilding;
import city.interfaces.Market;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.interfaces.MarketEmployee;
import city.interfaces.MarketManager;
import city.roles.MarketManagerRole.MyMarketCustomer;
import city.roles.MarketManagerRole.MyMarketEmployee;
import city.Application.FOOD_ITEMS;

public class MockMarketManager extends MockRole implements MarketManager {
	
	public EventLog log = new EventLog();
	public Market market;
	
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
	public Market getMarket() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMarket(MarketBuilding market) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getItemsLow() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<MyMarketEmployee> getEmployees() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MyMarketCustomer> getCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorkingState getWorkingState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MyMarketEmployee findEmployee(MarketEmployee me) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MyMarketCustomer findCustomerDelivery(MarketCustomerDelivery cd) {
		// TODO Auto-generated method stub
		return null;
	}
}
