package city.tests.mock;

import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.Application.FOOD_ITEMS;
import city.abstracts.MockRole;
import city.abstracts.RestaurantBuildingBase;
import city.abstracts.RestaurantBuildingInterface;
import city.buildings.MarketBuilding;
import city.interfaces.Market;
import city.interfaces.MarketCustomerDelivery;

public class MockMarketCustomerDelivery extends MockRole implements MarketCustomerDelivery {
	
	public EventLog log = new EventLog();
	public Market market;
	
	public MockMarketCustomerDelivery() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void msgHereIsOrderDelivery(Map<FOOD_ITEMS, Integer> collectedItems, int id) {
		log.add(new LoggedEvent("CustomerDelivery received msgHereIsOrderDelivery from deliveryPerson."));		
		System.out.println("CustomerDelivery received msgHereIsOrderDelivery from deliveryPerson.");		
	}

	@Override
	public RestaurantBuildingInterface getRestaurant() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMarket(MarketBuilding selectedMarket) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Market getMarket() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRestaurant(RestaurantBuildingBase restaurant) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MarketCustomerState getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setState(MarketCustomerState state) {
		// TODO Auto-generated method stub
		
	}

}
