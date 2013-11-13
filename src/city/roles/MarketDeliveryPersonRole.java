package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import city.interfaces.MarketDeliveryPerson;
import city.Role;

public class MarketDeliveryPersonRole extends Role implements MarketDeliveryPerson {

//  Data
//	=====================================================================	
//	Market market; TODO
	
	MarketCashierRole cashier;
//	Car car;
//  CarPassengerRole

	private List<Delivery> deliveries = Collections.synchronizedList(new ArrayList<Delivery>());
	private class Delivery {
		MarketCustomerDeliveryRole customerDelivery;
		Map<String, Integer> collectedItems;
		
		public Delivery(MarketCustomerDeliveryRole c, Map<String, Integer> i) {
			customerDelivery = c;
	        for (String s: i.keySet()) {
	        	collectedItems.put(s, i.get(s)); // initialize all values in collectedItems to 0
	        }
	    }
	}
	
//	CityMap
	
//	Gui
//	---------------------------------------------------------------
//	private MarketDeliveryPersonGui marketDeliveryPersonGui; // is this necessary?	
	
//	Constructor
//	---------------------------------------------------------------
	public MarketDeliveryPersonRole() {
		super(); // TODO
    }
	
//  Messages
//	=====================================================================	
//	Cashier
//	---------------------------------------------------------------
	public void msgDeliverOrder(MarketCustomerDeliveryRole c, Map<String, Integer> collectedItems) {
		System.out.println("Market customer received msgDeliverOrder");
		deliveries.add(new Delivery(c, collectedItems));
		stateChanged();
	}
	
//  Scheduler
//	=====================================================================

	@Override
	public boolean runScheduler() {
		synchronized(deliveries) {
			if (deliveries.size() > 0) {
				deliverItems();
			}
		}
		
		Boolean blocking = false;
		for (Role r : roles) if (r.active) {
			blocking  = true;
			r.runScheduler();
			break;
		}
		
		// Scheduler disposition
		return blocking;
		
		return false;
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
		
		cashier.msgFinishedDeliveringItems(this);
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
