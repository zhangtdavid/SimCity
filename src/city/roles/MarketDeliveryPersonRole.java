package city.roles;

import java.util.HashMap;
import java.util.Map;

import trace.AlertLog;
import trace.AlertTag;
import utilities.EventLog;
import utilities.LoggedEvent;
import city.Application.FOOD_ITEMS;
import city.agents.CarAgent;
import city.agents.interfaces.Car;
import city.bases.JobRole;
import city.buildings.MarketBuilding;
import city.buildings.interfaces.Market;
import city.roles.interfaces.CarPassenger;
import city.roles.interfaces.MarketCustomerDelivery;
import city.roles.interfaces.MarketDeliveryPerson;

public class MarketDeliveryPersonRole extends JobRole implements MarketDeliveryPerson {

//  Data
//	=====================================================================	
	public EventLog log = new EventLog();

	private Market market;
	
	private Car car;
	private CarPassenger carPassenger;
	
	private MarketCustomerDelivery customerDelivery;
	private int orderId;
	private Map<FOOD_ITEMS, Integer> collectedItems = new HashMap<FOOD_ITEMS, Integer>();
	
	private WorkingState workingState;

//	Constructor
//	=====================================================================
	public MarketDeliveryPersonRole(Market b, int t1, int t2) {
		super();
		market = b;
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(MarketBuilding.WORKER_SALARY);
		car = new CarAgent(b, this); // TODO schung 99c0f4da25 (Setting b to be the current location of the car- is this correct?)
		workingState = WorkingState.Working;
    }
	
//  Activity
//	=====================================================================		
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
	
//  Messages
//	=====================================================================	
//	Cashier
//	---------------------------------------------------------------
	@Override
	public void msgDeliverOrder(MarketCustomerDelivery c, Map<FOOD_ITEMS, Integer> i, int id) {
		log.add(new LoggedEvent("Market DeliveryPerson received msgDeliverOrder from Market Cashier."));
		print("Market DeliveryPerson received msgDeliverOrder from Market Cashier.");
		customerDelivery = c;
        for (FOOD_ITEMS s: i.keySet()) {
        	collectedItems.put(s, i.get(s)); // initialize all values in collectedItems to 0
        }
        orderId = id;
        stateChanged();
	}
	
//  Scheduler
//	=====================================================================
	@Override
	public boolean runScheduler() {
		if (workingState == WorkingState.GoingOffShift && customerDelivery == null) {
			if (market.getDeliveryPeople().size() > 1) {
				market.removeDeliveryPerson(this);
				super.setInactive();				
			}
		}
		
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
		market.getCashier().msgDeliveringItems(this);

//		// TODO schung 99c0f4da25
//      deliveryTruckGui.doGoToAddress();
// 		notify customer if there is a difference between order and collected items
// 		switch into CarPassenger;
//		while (carPassenger.getActive() && carPassenger.getActivity()) {
//			// do nothing
//		}
//		// TODO how does all this car stuff work??
		
		customerDelivery.msgHereIsOrderDelivery(collectedItems, orderId);
		market.getCashier().msgFinishedDeliveringItems(this, orderId);
		customerDelivery = null;
	}
	
//  Getters
//	=====================================================================
	// Market
	@Override
	public Market getMarket() {
		return market;
	}
	
	@Override
	public MarketCustomerDelivery getCustomerDelivery() {
		return customerDelivery;
	}
	
	@Override
	public int getOrderId() {
		return orderId;
	}
	
	@Override
	public Map<FOOD_ITEMS, Integer> getCollectedItems() {
		return collectedItems;
	}
	
	@Override
	public WorkingState getWorkingState() {
		return workingState;
	}
	
//  Setters
//	=====================================================================	
	@Override
	public void setMarket(Market market) {
		this.market = market;
	}
	
//  Utilities
//	=====================================================================
	@Override
	public void print(String msg) {
		this.getPerson().printViaRole("MarketDeliveryPerson", msg);
        AlertLog.getInstance().logMessage(AlertTag.MARKET, "MarketDeliveryPersonRole " + this.getPerson().getName(), msg);
    }
}
