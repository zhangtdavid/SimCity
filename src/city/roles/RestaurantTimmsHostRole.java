package city.roles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import city.Role;
import city.animations.RestaurantTimmsHostAnimation;
import city.interfaces.RestaurantTimmsCustomer;
import city.interfaces.RestaurantTimmsHost;
import city.interfaces.RestaurantTimmsWaiter;

/**
 * Restaurant host agent.
 */
public class RestaurantTimmsHostRole extends Role implements RestaurantTimmsHost {
	// Data
	
	private List<RestaurantTimmsCustomer> customers = Collections.synchronizedList(new ArrayList<RestaurantTimmsCustomer>());
	private List<RestaurantTimmsWaiter> waiters = new ArrayList<RestaurantTimmsWaiter>();
	private Collection<Table> tables = new ArrayList<Table>();
	
	private RestaurantTimmsWaiter waiterWantingBreak;
	private RestaurantTimmsHostAnimation animation;
	
	private int waiterIndex = 0;
	
	// Constructor

	public RestaurantTimmsHostRole() {
		super();
		this.waiterWantingBreak = null;
	}

	// Messages

	public void msgWantSeat(RestaurantTimmsCustomer c) {
		print("msgWantSeat");
		customers.add(c);
		stateChanged();
	}
	
	public void msgDoNotWantSeat(RestaurantTimmsCustomer customer) {
		print("msgDoNotWantSeat");
		customers.remove(customer);
	}
	
	public void msgLeaving(RestaurantTimmsCustomer c, int tableNumber) {
		print("msgLeaving");
		for (Table table : tables) {
			if (table.tableNumber == tableNumber) {
				table.setUnoccupied();
			}
		}
		stateChanged();
	}
	
	public void msgAskForBreak(RestaurantTimmsWaiter w) {
		print("msgAskForBreak");
		waiterWantingBreak = w;
		stateChanged();
	}
	
	// Actions
	
	public void actHandleBreakRequest() {
		print("actHandleBreakRequest");
		Integer waitersOnBreak = 0;
		
		for (RestaurantTimmsWaiter waiter : waiters) {
			if (waiter.getWantsBreak())
				waitersOnBreak += 1;
		}
		
		if ((waitersOnBreak) >= waiters.size()) {
			waiterWantingBreak.msgAllowBreak(false);
		} else {
			waiterWantingBreak.msgAllowBreak(true);
		}
		
		waiterWantingBreak = null;
	}
	
	// Scheduler
	
	public boolean runScheduler() {
		if (waiterWantingBreak != null) {
			actHandleBreakRequest();
		}
		synchronized(customers) {
			if (!customers.isEmpty() && !waiters.isEmpty()) {
				// Try to seat a customer
				for (Table table : tables) {
					if (!table.occupied) {
						table.setOccupied();
						waiters.get(waiterIndex).msgSeatCustomer(customers.get(0), table.tableNumber);
						waiterIndex = (waiterIndex + 1) % waiters.size();
						customers.remove(0);
						return true;
					}
				}
				
				for (RestaurantTimmsCustomer customer : customers) {
					customer.msgRestaurantFull();
				}
			}
		}
		return false;
	}
	
	// Get
	
	public RestaurantTimmsHostAnimation getAnimation() {
		return this.animation;
	}
	
	public int getWaiterIndex() {
		return waiters.size();
	}
	
	// Set
	
	public void setAnimation(RestaurantTimmsHostAnimation animation) {
		this.animation = animation;
	}

	// Utilities
	
	public void addTable(Integer tableNumber) {
		tables.add(new Table(tableNumber));
		stateChanged();
	}
	
	public void addWaiter(RestaurantTimmsWaiter waiter) {
		waiters.add(waiter);
		stateChanged();
	}

	// Table Class

	private class Table {
		boolean occupied;
		Integer tableNumber;

		Table(Integer tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupied() {
			this.occupied = true;
		}

		void setUnoccupied() {
			this.occupied = false;
		}
	}
}

