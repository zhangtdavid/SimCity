package city.tests.mock;

import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.Building;
import city.MockRole;
import city.Application.FOOD_ITEMS;
import city.buildings.MarketBuilding;
import city.buildings.RestaurantBaseBuilding;
import city.interfaces.MarketCustomerDelivery;

public class MockMarketCustomerDelivery extends MockRole implements MarketCustomerDelivery {
	
	public EventLog log = new EventLog();
	public MarketBuilding market;
	
	public MockMarketCustomerDelivery() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void msgHereIsOrderDelivery(Map<FOOD_ITEMS, Integer> collectedItems, int id) {
		log.add(new LoggedEvent("CustomerDelivery received msgHereIsOrderDelivery from deliveryPerson."));		
		System.out.println("CustomerDelivery received msgHereIsOrderDelivery from deliveryPerson.");		
	}

	@Override
	public Building getRestaurant() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMarket(MarketBuilding selectedMarket) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MarketBuilding getMarket() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRestaurant(RestaurantBaseBuilding restaurant) {
		// TODO Auto-generated method stub
		
	}

}
