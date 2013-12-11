package city.tests.roles.mocks;

import java.beans.PropertyChangeSupport;
import java.util.Map;
import java.util.Queue;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.Application.FOOD_ITEMS;
import city.buildings.interfaces.Market;
import city.buildings.interfaces.Market.DeliveryState;
import city.roles.MarketDeliveryPersonRole.MyDelivery;
import city.roles.interfaces.MarketCustomerDelivery;
import city.roles.interfaces.MarketDeliveryPerson;
import city.tests.bases.mocks.MockRole;

public class MockMarketDeliveryPerson extends MockRole implements MarketDeliveryPerson {
	
	public EventLog log = new EventLog();
	public Market market;
	public WorkingState workingState;
	
	public MockMarketDeliveryPerson() {
		workingState = WorkingState.Working;
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
	public void setMarket(Market market) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MarketCustomerDelivery getCustomerDelivery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getOrderId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Map<FOOD_ITEMS, Integer> getCollectedItems() {
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

	@Override
	public WorkingState getWorkingState() {
		return workingState;
	}

	@Override
	public void msgArrivedAtDestination() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Queue<MyDelivery> getDeliveries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeliveryState getDeliveryState() {
		// TODO Auto-generated method stub
		return null;
	}

}
