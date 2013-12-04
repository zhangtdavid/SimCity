package city.tests.mock;

import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.abstracts.MockRole;
import city.buildings.MarketBuilding;
import city.interfaces.Market;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketEmployee;
import city.Application.FOOD_ITEMS;

public class MockMarketCustomer extends MockRole implements MarketCustomer {
	
	public EventLog log = new EventLog();
	public Market market;
	
	public MockMarketCustomer() {
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
		log.add(new LoggedEvent("Customer received msgPaymentReceived from Market Cashier."));		
		System.out.println("Customer received msgPaymentReceived from Market Cashier.");					
	}

	@Override
	public void msgAnimationFinishedLeaveMarket() {
		log.add(new LoggedEvent("Customer received msgAnimationFinishedLeaveMarket from Market Customer Animation."));		
		System.out.println("Customer received msgAnimationFinishedLeaveMarket from Market Customer Animation.");		
	}

	@Override
	public void msgAnimationAtCounter() {
		log.add(new LoggedEvent("Customer received msgAnimationAtCounter from Market Customer Animation."));		
		System.out.println("Customer received msgAnimationAtCounter from Market Customer Animation.");				
	}

	@Override
	public void msgAnimationAtCashier() {
		log.add(new LoggedEvent("Customer received msgAnimationAtCashier from Market Customer Animation."));		
		System.out.println("Customer received msgAnimationAtCashier from Market Customer Animation.");				
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
	public int checkBill() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MarketEmployee getEmployee() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<FOOD_ITEMS, Integer> getReceivedItems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLoc() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBill() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MarketCustomerEvent getMarketCustomerEvent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarketCustomerState getMarketCustomerState() {
		// TODO Auto-generated method stub
		return null;
	}

}
