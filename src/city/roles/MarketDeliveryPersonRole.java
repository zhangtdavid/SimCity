package city.roles;

import java.util.HashMap;
import java.util.Map;

import trace.AlertLog;
import trace.AlertTag;
import utilities.EventLog;
import utilities.LoggedEvent;
import city.Application.FOOD_ITEMS;
import city.Role;
import city.agents.CarAgent;
import city.buildings.MarketBuilding;
import city.interfaces.CarPassenger;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketDeliveryPerson;

public class MarketDeliveryPersonRole extends Role implements MarketDeliveryPerson {

//  Data
//	=====================================================================	
	private MarketBuilding market;
	private WorkingState workingState = WorkingState.Working;
	private CarAgent car;
	private CarPassenger carPassenger;
	
	// TODO Change these to private and add getters/setters
	public int orderId;
	public MarketCustomerDelivery customerDelivery;
	public enum WorkingState {Working, GoingOffShift, NotWorking};
	public EventLog log = new EventLog();
	public Map<FOOD_ITEMS, Integer> collectedItems = new HashMap<FOOD_ITEMS, Integer>();

//	Constructor
//	---------------------------------------------------------------
	public MarketDeliveryPersonRole(MarketBuilding b, int t1, int t2) {
		super();
		market = b;
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(MarketBuilding.getWorkerSalary());
		car = new CarAgent(b); // TODO schung 99c0f4da25 (Setting b to be the current location of the car- is this correct?)
    }
	
	
//  Messages
//	=====================================================================	
//	Cashier
//	---------------------------------------------------------------
	@Override
	public void msgDeliverOrder(MarketCustomerDelivery c, Map<FOOD_ITEMS, Integer> i, int id) {
		log.add(new LoggedEvent("Market DeliveryPerson received msgDeliverOrder from Market Cashier."));
		System.out.println("Market DeliveryPerson received msgDeliverOrder from Market Cashier.");
		if (workingState != WorkingState.NotWorking) {
			customerDelivery = c;
	        for (FOOD_ITEMS s: i.keySet()) {
	        	collectedItems.put(s, i.get(s)); // initialize all values in collectedItems to 0
	        }
	        orderId = id;
	        stateChanged();
		}
	}
	
//  Scheduler
//	=====================================================================

	@Override
	public boolean runScheduler() {
		if (workingState == WorkingState.GoingOffShift) {
			if (market.deliveryPeople.size() > 1)
				workingState = WorkingState.NotWorking;
		}
		
		if (customerDelivery == null && workingState == WorkingState.NotWorking)
			super.setInactive();
		
		if (customerDelivery != null) {
			deliverItems();
			return true;
		}
		
//		// Role Scheduler
//		boolean blocking = false;
//		if (carPassenger.getActive() && carPassenger.getActivity()) {
//			blocking  = true;
//			boolean activity = carPassenger.runScheduler();
//			if (!activity) {
//				carPassenger.setActivityFinished();
//			}
//		}
//		
//		// Scheduler disposition
//		return blocking;
		return false;
	}
	
//  Actions
//	=====================================================================	
	private void deliverItems() {
		carPassenger = new CarPassengerRole(car, customerDelivery.getRestaurant());
		carPassenger.setActive();
		market.cashier.msgDeliveringItems(this);

//		// TODO schung 99c0f4da25
//      deliveryTruckGui.doGoToAddress();
// 		notify customer if there is a difference between order and collected items
// 		switch into CarPassenger;
//		while (carPassenger.getActive() && carPassenger.getActivity()) {
//			// do nothing
//		}
//		// TODO how does all this car stuff work??
		
		customerDelivery.msgHereIsOrderDelivery(collectedItems, orderId);
		market.cashier.msgFinishedDeliveringItems(this, orderId);
		customerDelivery = null;
	}
	
//  Getters and Setters
//	=====================================================================
	// Market
	@Override
	public MarketBuilding getMarket() {
		return market;
	}
	
	@Override
	public void setMarket(MarketBuilding market) {
		this.market = market;
	}
	
//	// TODO schung 99c0f4da25
//	@Override
//	public void setActive() {
//		super.setActivityBegun();
//		super.setActive();
//	}
	
	@Override
	public void setInactive(){
		workingState = WorkingState.GoingOffShift;
	}
	
//  Utilities
//	=====================================================================
	@Override
	public void print(String msg) {
        super.print(msg);
        AlertLog.getInstance().logMessage(AlertTag.MARKET, "MarketDeliveryPersonRole " + this.getPerson().getName(), msg);
    }
}
