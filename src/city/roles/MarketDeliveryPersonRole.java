package city.roles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.agents.CarAgent;
import city.buildings.MarketBuilding;
import city.interfaces.CarPassenger;
import city.interfaces.MarketCashier;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketDeliveryPerson;
import city.Role;

public class MarketDeliveryPersonRole extends Role implements MarketDeliveryPerson {

//  Data
//	=====================================================================	
	public EventLog log = new EventLog();

	private MarketBuilding market;
	
	private MarketCashier cashier;
	private List<Role> roles = new ArrayList<Role>();

	private CarAgent car;
	private CarPassenger carPassenger;

	private MarketCustomerDelivery customerDelivery;
	private Map<String, Integer> collectedItems;
	
//	CityMap
	
//	Gui
//	---------------------------------------------------------------
//	private MarketDeliveryPersonGui marketDeliveryPersonGui; // is this necessary?	
	
//	Constructor
//	---------------------------------------------------------------
	public MarketDeliveryPersonRole() {
		super(); // TODO
//		car = new CarAgent();
    }
	
//  Messages
//	=====================================================================	
//	Cashier
//	---------------------------------------------------------------
	public void msgDeliverOrder(MarketCustomerDelivery c, Map<String, Integer> i) {
		log.add(new LoggedEvent("Market Customer received msgDeliverOrder from Market Cashier."));
		System.out.println("Market deliveryPerson received msgDeliverOrder from Market Cashier.");
		customerDelivery = c;
        for (String s: i.keySet()) {
        	collectedItems.put(s, i.get(s)); // initialize all values in collectedItems to 0
        }
        stateChanged();
	}
	
//  Scheduler
//	=====================================================================

	@Override
	public boolean runScheduler() {
		if (customerDelivery != null) {
			deliverItems();
		}
		
		// Role Scheduler
		boolean blocking = false;
		for (Role r : roles) if (r.getActive()) {
			if (carPassenger.getActive()) {
				blocking  = true;
				carPassenger.runScheduler();
			}
		}
		
		// Scheduler disposition
		return blocking;
	}
	
//  Actions
//	=====================================================================	
	private void deliverItems() {
		carPassenger = new CarPassengerRole(car, market); // TODO Update this to restaurant
		cashier.msgDeliveringItems(this);

//		for (Delivery d: deliveries) {
//        	deliveryTruckGui.doGoToAddress();
//        }
        // notify customer if there is a difference between order and collected items
		// switch into CarPassenger;
		
		customerDelivery.msgHereIsOrder(collectedItems);
		cashier.msgFinishedDeliveringItems(this, customerDelivery);
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
	
	// Cashier
	public MarketCashier getCashier() {
		return cashier;
	}
	
	public void setCashier(MarketCashier cashier) {
		this.cashier = cashier;
	}
	
//  Utilities
//	=====================================================================	

	
	//	private Transaction findTransaction(MarketCustomerRole c) {
//		for(Transaction t : transactions ){
//			if(t.customer == c) {
//				return t;		
//			}
//		}
//		return null;
//	}

	// Classes
}
