package city.roles;

import java.util.List;

import city.Role;
import city.buildings.RestaurantTimmsBuilding;
import city.interfaces.RestaurantTimmsCustomer;
import city.interfaces.RestaurantTimmsHost;
import city.interfaces.RestaurantTimmsWaiter;

/**
 * Restaurant host agent.
 */
public class RestaurantTimmsHostRole extends Role implements RestaurantTimmsHost {
	
	// Data
		
	private RestaurantTimmsWaiter waiterWantingBreak;
	private List<RestaurantTimmsCustomer> customers;
	private List<RestaurantTimmsWaiter> waiters;
	private int waiterIndex = 0;
	private RestaurantTimmsBuilding rtb;
	
	// Constructor

	/**
	 * Construct a RestaurantTimmsHostRole.
	 * 
	 * @param b the RestaurantTimmsBuilding that this host will work at
	 * @param shiftStart the hour (0-23) that the role's shift begins
	 * @param shiftEnd the hour (0-23) that the role's shift ends
	 */
	public RestaurantTimmsHostRole(RestaurantTimmsBuilding b, int shiftStart, int shiftEnd) {
		super();
		this.setWorkplace(b);
		this.setSalary(RestaurantTimmsBuilding.WORKER_SALARY);
		this.setShift(shiftStart, shiftEnd);
		this.waiterWantingBreak = null;
		this.rtb = this.getWorkplace(RestaurantTimmsBuilding.class);
		this.customers = rtb.restaurantCustomers;
		this.waiters = rtb.restaurantWaiters;
	}

	// Messages

	@Override
	public void msgWantSeat(RestaurantTimmsCustomer c) {
		print("msgWantSeat");
		customers.add(c);
		stateChanged();
	}
	
	@Override
	public void msgDoNotWantSeat(RestaurantTimmsCustomer customer) {
		print("msgDoNotWantSeat");
		customers.remove(customer);
	}
	
	@Override
	public void msgLeaving(RestaurantTimmsCustomer c, int tableNumber) {
		print("msgLeaving");
		for (RestaurantTimmsBuilding.Table table : rtb.restaurantTables) {
			if (table.getNumber() == tableNumber) {
				table.setUnoccupied();
			}
		}
		stateChanged();
	}
	
	@Override
	public void msgAskForBreak(RestaurantTimmsWaiter w) {
		print("msgAskForBreak");
		waiterWantingBreak = w;
		stateChanged();
	}
	
	// Actions
	
	private void actHandleBreakRequest() {
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
	
	@Override
	public boolean runScheduler() {
		if (waiterWantingBreak != null) {
			actHandleBreakRequest();
		}
		synchronized(customers) {
			if (!customers.isEmpty() && !waiters.isEmpty()) {
				// Try to seat a customer
				for (RestaurantTimmsBuilding.Table table : rtb.restaurantTables) {
					if (!table.getOccupied()) {
						table.setOccupied();
						waiters.get(waiterIndex).msgSeatCustomer(customers.get(0), table.getNumber());
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
	
	// Set
	
	@Override
	public void setActive() {
		this.rtb = this.getWorkplace(RestaurantTimmsBuilding.class);
		this.customers = rtb.restaurantCustomers;
		this.waiters = rtb.restaurantWaiters;
		super.setActive();
		// TODO
	}

}

