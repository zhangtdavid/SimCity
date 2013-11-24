package city.tests.mock;

import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.MockRole;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketEmployee;
import city.Application.FOOD_ITEMS;

public class MockMarketCustomer extends MockRole implements MarketCustomer {
	
	public EventLog log = new EventLog();
	public MarketBuilding market;
	
	public MockMarketCustomer() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgAnimationArrivedAtMarket() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatWouldYouLike(MarketEmployee e, int loc) {
		log.add(new LoggedEvent("Customer received msgWhatWouldYouLike from employee at stall " + loc));		
		System.out.println("Customer received msgWhatWouldYouLike from employee at stall " + loc);
	}

	@Override
	public void msgHereIsOrderandBill(Map<FOOD_ITEMS, Integer> collectedItems, int bill, int id) {
		log.add(new LoggedEvent("Customer received msgHereIsOrderandBill from cashier. The bill is for " + bill));		
		System.out.println("Customer received msgHereIsOrderandBill from cashier. The bill is for " + bill);			
	}

	@Override
	public void msgPaymentReceived() {
		log.add(new LoggedEvent("Customer received msgPaymentReceived from cashier."));		
		System.out.println("Customer received msgPaymentReceived from cashier.");					
	}

	@Override
	public void msgAnimationAtEntrance() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationAtCashier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedLeaveMarket() {
		// TODO Auto-generated method stub
		
	}

}
