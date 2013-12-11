package city.roles;

import trace.AlertLog;
import trace.AlertTag;
import city.animations.interfaces.RestaurantTimmsAnimatedHost;
import city.bases.JobRole;
import city.buildings.RestaurantTimmsBuilding;
import city.buildings.interfaces.RestaurantTimms;
import city.roles.interfaces.RestaurantTimmsCustomer;
import city.roles.interfaces.RestaurantTimmsHost;
import city.roles.interfaces.RestaurantTimmsWaiter;

/**
 * Restaurant host agent.
 */
public class RestaurantTimmsHostRole extends JobRole implements RestaurantTimmsHost {
	
	// Data
		
	private RestaurantTimmsWaiter waiterWantingBreak;
	private RestaurantTimms rtb;
	private boolean shiftOver;
	
	// Constructor

	/**
	 * Construct a RestaurantTimmsHostRole.
	 * 
	 * @param b the RestaurantTimmsBuilding that this host will work at
	 * @param shiftStart the hour (0-23) that the role's shift begins
	 * @param shiftEnd the hour (0-23) that the role's shift ends
	 */
	public RestaurantTimmsHostRole(RestaurantTimms b, int shiftStart, int shiftEnd) {
		super();
		this.setWorkplace(b);
		this.setSalary(RestaurantTimms.WORKER_SALARY);
		this.setShift(shiftStart, shiftEnd);
		this.waiterWantingBreak = null;
		this.rtb = b;
		b.setHost(this);
		this.shiftOver = false;
	}

	// Messages

	@Override
	public void msgWantSeat(RestaurantTimmsCustomer c) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		rtb.addCustomer(c, this);
		stateChanged();
	}
	
	@Override
	public void msgDoNotWantSeat(RestaurantTimmsCustomer c) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		rtb.removeCustomer(c);
	}
	
	@Override
	public void msgLeaving(RestaurantTimmsCustomer c, int tableNumber) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		for (RestaurantTimmsBuilding.Table table : rtb.getTables()) {
			if (table.getNumber() == tableNumber) {
				table.setUnoccupied();
			}
		}
		stateChanged();
	}
	
	@Override
	public void msgAskForBreak(RestaurantTimmsWaiter w) {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		waiterWantingBreak = w;
		stateChanged();
	}
	
	
	// Scheduler
	
	@Override
	public boolean runScheduler() {
		
		if (shiftOver && !rtb.getHost().equals(this)) {
			print("Leaving shift.");
			super.setInactive();
			return false;
		}
		
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
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
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
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		rtb.setHost(this);
		this.shiftOver = false;
		this.getAnimation(RestaurantTimmsAnimatedHost.class).setVisible(true);
		super.setActive();
	}
	
	@Override
	public void setInactive() {
		print(Thread.currentThread().getStackTrace()[1].getMethodName());
		shiftOver = true;
	}
	
	@Override
	public void print(String msg) {
		this.getPerson().printViaRole("RestaurantTimmsHost", msg);
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTTIMMS, "RestaurantTimmsHostRole " + this.getPerson().getName(), msg);
    }

}

