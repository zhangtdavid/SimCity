package city.tests.mock;

import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.Application.FOOD_ITEMS;
import city.abstracts.MockRole;
import city.buildings.MarketBuilding;
import city.interfaces.Market;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketDeliveryPerson;

public class MockMarketDeliveryPerson extends MockRole implements MarketDeliveryPerson {
	
	public EventLog log = new EventLog();
	public Market market;
	
	public MockMarketDeliveryPerson() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDeliverOrder(MarketCustomerDelivery customerDelivery, Map<FOOD_ITEMS, Integer> i, int id) {
		log.add(new LoggedEvent("Delivery Person received msgDeliverOrder from Cashier."));		
		System.out.println("Delivery Person received msgDeliverOrder from Cashier.");		
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

}
