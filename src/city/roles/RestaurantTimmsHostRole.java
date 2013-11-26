package city.roles;

import city.Role;
import city.animations.interfaces.RestaurantTimmsAnimatedHost;
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
		this.rtb = b;
	}

	// Messages

	@Override
	public void msgWantSeat(RestaurantTimmsCustomer c) {
		print("msgWantSeat");
		rtb.addCustomer(c, this);
		stateChanged();
	}
	
	@Override
	public void msgDoNotWantSeat(RestaurantTimmsCustomer c) {
		print("msgDoNotWantSeat");
		rtb.removeCustomer(c);
	}
	
	@Override
	public void msgLeaving(RestaurantTimmsCustomer c, int tableNumber) {
		print("msgLeaving");
		for (RestaurantTimmsBuilding.Table table : rtb.getTables()) {
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
	
	
	// Scheduler
	
	@Override
	public boolean runScheduler() {
		if (waiterWantingBreak != null) {
			actHandleBreakRequest();
		}
		if (!rtb.getWaitingCustomers().isEmpty() && !rtb.getWaiters().isEmpty()) {
			// Try to seat a customer
			for (RestaurantTimmsBuilding.Table table : rtb.getTables()) {
				if (!table.getOccupied()) {
					table.setOccupied();
					RestaurantTimmsWaiter w = rtb.getWaiter();
					RestaurantTimmsCustomer c = rtb.getCustomer(this, w);
					w.msgSeatCustomer(c, table.getNumber());
					return true;
				}
			}
			
			for (RestaurantTimmsCustomer c : rtb.getWaitingCustomers()) {
				c.msgRestaurantFull();
			}
		}
		return false;
	}
	
	
	// Actions
	
	private void actHandleBreakRequest() {
		print("actHandleBreakRequest");
		Integer waitersOnBreak = 0;
		
		for (RestaurantTimmsWaiter w : rtb.getWaiters()) {
			if (w.getWantsBreak())
				waitersOnBreak += 1;
		}
		
		if ((waitersOnBreak) >= rtb.getWaiters().size()) {
			waiterWantingBreak.msgAllowBreak(false);
		} else {
			waiterWantingBreak.msgAllowBreak(true);
		}
		
		waiterWantingBreak = null;
	}

	// Getters
	
	// Setters
	
	@Override
	public void setActive() {
		rtb.setHost(this);
		this.getAnimation(RestaurantTimmsAnimatedHost.class).setVisible(true);
		super.setActive();
	}

}

