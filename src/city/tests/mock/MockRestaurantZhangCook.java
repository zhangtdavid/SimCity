package city.tests.mock;

import utilities.LoggedEvent;
import utilities.RestaurantZhangTable;
import city.MockRole;
import city.interfaces.RestaurantZhangCook;
import city.interfaces.RestaurantZhangWaiter;

public class MockRestaurantZhangCook extends MockRole implements RestaurantZhangCook {
	
	public MockRestaurantZhangCook() {
		super();
	}

	@Override
	public void msgHereIsAnOrder(RestaurantZhangWaiter w, String choice, RestaurantZhangTable t) {
		log.add(new LoggedEvent("Order from " + w.getName() + " for " + choice + " at table " + t.tableNumber));
		
	}

	@Override
	public void msgGotCompletedOrder(RestaurantZhangTable table) {
		log.add(new LoggedEvent("Order finished for table " + table.tableNumber));
	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return 0;
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
}
