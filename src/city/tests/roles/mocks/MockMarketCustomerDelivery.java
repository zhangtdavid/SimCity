package city.tests.roles.mocks;

import java.beans.PropertyChangeSupport;
import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import utilities.MarketOrder;
import city.Application.FOOD_ITEMS;
import city.bases.RestaurantBuilding;
import city.bases.interfaces.RestaurantBuildingInterface;
import city.buildings.MarketBuilding;
import city.buildings.interfaces.Market;
import city.roles.interfaces.MarketCustomerDelivery;
import city.tests.bases.mocks.MockRole;

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
	public void setRestaurant(RestaurantBuilding restaurant) {
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
	public MarketOrder getOrder() {
		// TODO Auto-generated method stub
		return null;
	}

}
