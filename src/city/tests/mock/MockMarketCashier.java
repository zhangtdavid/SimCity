package city.tests.mock;

import java.util.List;
import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.Application.FOOD_ITEMS;
import city.abstracts.MockRole;
import city.interfaces.Market;
import city.interfaces.MarketCashier;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.interfaces.MarketDeliveryPerson;
import city.interfaces.MarketEmployee;
import city.roles.MarketCashierRole.MyDeliveryPerson;
import city.roles.MarketCashierRole.Transaction;

public class MockMarketCashier extends MockRole implements MarketCashier {

	public EventLog log = new EventLog();
	public Market market;
	
	public MockMarketCashier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgNewDeliveryPerson(MarketDeliveryPerson d) {
		log.add(new LoggedEvent("Market Cashier received msgNewDeliveryPerson from Market Manager"));		
		System.out.println("Market Cashier received msgNewDeliveryPerson from Market Manager");		
	}

	@Override
	public void msgRemoveDeliveryPerson(MarketDeliveryPerson d) {
		log.add(new LoggedEvent("Market Cashier received msgRemoveDeliveryPerson from Market Manager"));		
		System.out.println("Market Cashier received msgRemoveDeliveryPerson from Market Manager");		
	}
	
	@Override
	public void msgComputeBill(MarketEmployee e, MarketCustomer c, Map<FOOD_ITEMS, Integer> o, Map<FOOD_ITEMS, Integer> i, int id) {
		log.add(new LoggedEvent("Market Cashier received msgComputeBill for In Person Customer from Market Employee"));		
		System.out.println("Market Cashier received msgComputeBill for In Person Customer from Market Employee");	
	}

	@Override
	public void msgHereIsPayment(int id, int money) {
		log.add(new LoggedEvent("Market Cashier received msgHereIsPayment from Market Customer for " + money));		
		System.out.println("Market Cashier received msgHereIsPayment from Market Customer for " + money);			
	}

	@Override
	public void msgComputeBill(MarketEmployee e, MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay, Map<FOOD_ITEMS, Integer> o, Map<FOOD_ITEMS, Integer> i, int id) {
		log.add(new LoggedEvent("Market Cashier received msgComputeBill for Delivery Customer from Market Employee"));		
		System.out.println("Market Cashier received msgComputeBill for Delivery Customer from Market Employee");			
	}

	@Override
	public void msgDeliveringItems(MarketDeliveryPerson d) {
		log.add(new LoggedEvent("Market Cashier received msgDeliveringItems from Market Delivery Person"));		
		System.out.println("Market Cashier received msgDeliveringItems from Market Delivery Person");	
	}

	@Override
	public void msgFinishedDeliveringItems(MarketDeliveryPerson d, int id) {
		log.add(new LoggedEvent("Market Cashier received msgFinishedDeliveringItems from Market Delivery Person"));		
		System.out.println("Market Cashier received msgFinishedDeliveringItems from Market Delivery Person");			
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
	public List<MyDeliveryPerson> getDeliveryPeople() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMarket(Market market) {
		// TODO Auto-generated method stub
		
	}
}
