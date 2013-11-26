package city.roles;

import java.util.HashMap;
import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.agents.CarAgent;
import city.buildings.MarketBuilding;
import city.interfaces.CarPassenger;
import city.interfaces.MarketCashier;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketDeliveryPerson;
import city.Application.FOOD_ITEMS;
import city.Role;

public class MarketDeliveryPersonRole extends Role implements MarketDeliveryPerson {

//  Data
//	=====================================================================	
	public EventLog log = new EventLog();

	private MarketBuilding market;
	
	public MarketCustomerDelivery customerDelivery;
	
	public enum WorkingState
	{Working, GoingOffShift, NotWorking};
	WorkingState workingState = WorkingState.Working;
	
	private CarAgent car;
	private CarPassenger carPassenger;
	
	public Map<FOOD_ITEMS, Integer> collectedItems = new HashMap<FOOD_ITEMS, Integer>();
	public int orderId;
	
//	CityMap
	
//	Gui
//	---------------------------------------------------------------
//	private MarketDeliveryPersonGui marketDeliveryPersonGui; // is this necessary?	
	
//	Constructor
//	---------------------------------------------------------------
	public MarketDeliveryPersonRole(MarketBuilding b, int t1, int t2) {
		super();
		market = b;
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(MarketBuilding.getWorkerSalary());
		car = new CarAgent();
    }
	
//	public void setActive(){
//		this.setActivityBegun();
//	}
	
	public void setInactive(){
		workingState = WorkingState.GoingOffShift;
	}
	
//  Messages
//	=====================================================================	
//	Cashier
//	---------------------------------------------------------------
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

//      deliveryTruckGui.doGoToAddress();
        // notify customer if there is a difference between order and collected items
		// switch into CarPassenger;
		
//		while (carPassenger.getActive() && carPassenger.getActivity()) {
//			// do nothing
//		}

		// TODO how does all this car stuff work??
		
		customerDelivery.msgHereIsOrderDelivery(collectedItems, orderId);
		market.cashier.msgFinishedDeliveringItems(this, orderId);
		customerDelivery = null;
	}
	
//  Getters and Setters
//	=====================================================================
	// Market
	public MarketBuilding getMarket() {
		return market;
	}
	
	public void setMarket(MarketBuilding market) {
		this.market = market;
	}
	
//  Utilities
//	=====================================================================
}
