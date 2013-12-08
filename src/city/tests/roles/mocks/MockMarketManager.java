package city.tests.roles.mocks;

import java.beans.PropertyChangeSupport;
import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.Application.FOOD_ITEMS;
import city.buildings.interfaces.Market;
import city.roles.interfaces.MarketCustomer;
import city.roles.interfaces.MarketCustomerDelivery;
import city.roles.interfaces.MarketCustomerDeliveryPayment;
import city.roles.interfaces.MarketEmployee;
import city.roles.interfaces.MarketManager;
import city.tests.bases.mocks.MockRole;

public class MockMarketManager extends MockRole implements MarketManager {
	
	public EventLog log = new EventLog();
	public Market market;
	
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
	public void setMarket(Market market) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getItemsLow() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public WorkingState getWorkingState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PropertyChangeSupport getPropertyChangeSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStateString() {
		// TODO Auto-generated method stub
		return null;
	}
}
