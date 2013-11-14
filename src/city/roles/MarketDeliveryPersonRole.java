package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import city.agents.CarAgent;
import city.interfaces.MarketDeliveryPerson;
import city.Role;

public class MarketDeliveryPersonRole extends Role implements MarketDeliveryPerson {

//  Data
//	=====================================================================	
//	Market market; TODO
	
	MarketCashierRole cashier;
	private List<Role> roles = new ArrayList<Role>();

	CarAgent car;
	CarPassengerRole carPassenger;

	MarketCustomerDeliveryRole customerDelivery;
	Map<String, Integer> collectedItems;
	
//	CityMap
	
//	Gui
//	---------------------------------------------------------------
//	private MarketDeliveryPersonGui marketDeliveryPersonGui; // is this necessary?	
	
//	Constructor
//	---------------------------------------------------------------
	public MarketDeliveryPersonRole() {
		super(); // TODO
//		car = new CarAgent();
		carPassenger = new CarPassengerRole(car);
    }
	
//  Messages
//	=====================================================================	
//	Cashier
//	---------------------------------------------------------------
	public void msgDeliverOrder(MarketCustomerDeliveryRole c, Map<String, Integer> i) {
		System.out.println("Market customer received msgDeliverOrder");
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
		Boolean blocking = false;
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
		cashier.msgDeliveringItems(this);

//		for (Delivery d: deliveries) {
//        	deliveryTruckGui.doGoToAddress();
//        }
        // notify customer if there is a difference between order and collected items
		// switch into CarPassengerRole;
		
		customerDelivery.msgHereIsOrder(collectedItems);
		cashier.msgFinishedDeliveringItems(this);
		customerDelivery = null;
	}
		
	// Getters
	
	// Setters
	
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
