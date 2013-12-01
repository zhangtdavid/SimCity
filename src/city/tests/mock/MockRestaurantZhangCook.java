package city.tests.mock;

import java.util.Map;

import utilities.LoggedEvent;
import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangOrder;
import utilities.RestaurantZhangRevolvingStand;
import utilities.RestaurantZhangTable;
import city.Application.FOOD_ITEMS;
import city.abstracts.MockRole;
import city.abstracts.RestaurantBuildingInterface.Food;
import city.animations.interfaces.RestaurantZhangAnimatedCook;
import city.buildings.MarketBuilding;
import city.interfaces.RestaurantZhangCook;
import city.interfaces.RestaurantZhangHost;
import city.interfaces.RestaurantZhangWaiter;

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
	public void msgProcessedInvoice(String food, boolean isAvailable,
			int processedAmount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsInvoice(String food, int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RestaurantZhangAnimatedCook getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMenuTimes(RestaurantZhangMenu m, Map<FOOD_ITEMS, Food> food) {
		// TODO Auto-generated method stub
		
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
}
