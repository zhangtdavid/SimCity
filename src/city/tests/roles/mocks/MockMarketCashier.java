package city.tests.roles.mocks;

import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.Application.FOOD_ITEMS;
import city.buildings.interfaces.Market;
import city.roles.MarketCashierRole.Transaction;
import city.roles.interfaces.MarketCashier;
import city.roles.interfaces.MarketCustomer;
import city.roles.interfaces.MarketCustomerDelivery;
import city.roles.interfaces.MarketCustomerDeliveryPayment;
import city.roles.interfaces.MarketDeliveryPerson;
import city.roles.interfaces.MarketEmployee;
import city.tests.bases.mocks.MockRole;

public class MockMarketCashier extends MockRole implements MarketCashier {
	public EventLog log = new EventLog();
	public Market market;
	
	public MockMarketCashier() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgComputeBill(MarketEmployee e, MarketCustomer c, Map<FOOD_ITEMS, Integer> o, Map<FOOD_ITEMS, Integer> i, int id) {
		log.add(new LoggedEvent("MarketCashier received msgComputeBill for In Person Customer from MarketEmployee"));		
		System.out.println("MarketCashier received msgComputeBill for In Person Customer from MarketEmployee");	
	}

	@Override
	public void msgHereIsPayment(int id, int money) {
		log.add(new LoggedEvent("MarketCashier received msgHereIsPayment from MarketCustomerDeliveryPayment for " + money));		
		System.out.println("MarketCashier received msgHereIsPayment from MarketCustomerDeliveryPayment for " + money);			
	}

	@Override
	public void msgComputeBill(MarketEmployee e, MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay, Map<FOOD_ITEMS, Integer> o, Map<FOOD_ITEMS, Integer> i, int id) {
		log.add(new LoggedEvent("MarketCashier received msgComputeBill for MarketCustomerDelivery from MarketEmployee"));		
		System.out.println("MarketCashier received msgComputeBill for MarketCustomerDelivery from MarketEmployee");			
	}

	@Override
	public void msgDeliveringItems(MarketDeliveryPerson d) {
		log.add(new LoggedEvent("MarketCashier received msgDeliveringItems from MarketDeliveryPerson"));		
		System.out.println("MarketCashier received msgDeliveringItems from MarketDeliveryPerson");	
	}

	@Override
	public void msgFinishedDeliveringItems(MarketDeliveryPerson d, int id) {
		log.add(new LoggedEvent("MarketCashier received msgFinishedDeliveringItems from MarketDeliveryPerson"));		
		System.out.println("MarketCashier received msgFinishedDeliveringItems from MarketDeliveryPerson");			
	}

	@Override
	public Market getMarket() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Transaction> getTransactions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMarket(Market market) {
		// TODO Auto-generated method stub
		
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

	@Override
	public WorkingState getWorkingState() {
		// TODO Auto-generated method stub
		return null;
	}
}
