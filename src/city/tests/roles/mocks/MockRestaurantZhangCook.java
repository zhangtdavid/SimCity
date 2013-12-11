package city.tests.roles.mocks;

import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;

import utilities.LoggedEvent;
import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangOrder;
import utilities.RestaurantZhangRevolvingStand;
import utilities.RestaurantZhangTable;
import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantZhangAnimatedCook;
import city.buildings.MarketBuilding;
import city.roles.interfaces.MarketCustomerDelivery;
import city.roles.interfaces.RestaurantZhangCashier;
import city.roles.interfaces.RestaurantZhangCook;
import city.roles.interfaces.RestaurantZhangHost;
import city.roles.interfaces.RestaurantZhangWaiter;
import city.tests.bases.mocks.MockRole;

public class MockRestaurantZhangCook extends MockRole implements RestaurantZhangCook {
	
	public MockRestaurantZhangCook() {
		super();
	}

	@Override
	public void msgHereIsAnOrder(RestaurantZhangWaiter w, String choice, RestaurantZhangTable t) {
		log.add(new LoggedEvent("Order from " + w.getPerson().getName() + " for " + choice + " at table " + t.tableNumber));
		
	}

	@Override
	public void msgGotCompletedOrder(RestaurantZhangTable table) {
		log.add(new LoggedEvent("Order finished for table " + table.tableNumber));
	}

	@Override
	public int getPosOfNewOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void msgAtDestination() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RestaurantZhangAnimatedCook getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAnimation(RestaurantZhangAnimatedCook gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addMarket(MarketBuilding m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRevolvingStand(RestaurantZhangRevolvingStand rs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHost(RestaurantZhangHost h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hackOrderIsReady(RestaurantZhangOrder o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<MarketBuilding> getMarkets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RestaurantZhangCashier getCashier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MarketCustomerDelivery> getmarketCustomerDeliveryList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RestaurantZhangHost getHost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RestaurantZhangOrder> getOrdersToCook() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RestaurantZhangRevolvingStand getOrderStand() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RestaurantZhangMenu getMainMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getWaitingToCheckStand() {
		// TODO Auto-generated method stub
		return false;
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
	public void msgHereIsOrderDelivery(Map<FOOD_ITEMS, Integer> marketOrder,
			int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMenuTimes(RestaurantZhangMenu m) {
		// TODO Auto-generated method stub
		
	}
}
