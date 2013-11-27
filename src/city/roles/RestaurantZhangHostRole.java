package city.roles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;

import trace.AlertLog;
import trace.AlertTag;
import utilities.RestaurantZhangTable;
import city.Building;
import city.Role;
import city.interfaces.RestaurantZhangCustomer;
import city.interfaces.RestaurantZhangHost;
import city.interfaces.RestaurantZhangWaiter;

/**
 * Restaurant Host Agent
 */
public class RestaurantZhangHostRole extends Role implements RestaurantZhangHost {
	private static final int RESTAURANTZHANGHOSTSALARY = 100;
	private List<RestaurantZhangCustomer> enteringCustomers = Collections.synchronizedList(new ArrayList<RestaurantZhangCustomer>());
	private List<RestaurantZhangCustomer> waitingCustomers = Collections.synchronizedList(new ArrayList<RestaurantZhangCustomer>());
	private List<MyWaiter> myWaiterList = Collections.synchronizedList(new ArrayList<MyWaiter>());
	private Collection<RestaurantZhangTable> tables;
	public int numberOfCustomersInRestaurant = 0;

	private String name;
	private boolean closingRestaurant = false;

	public RestaurantZhangHostRole(Building restaurantToWorkAt, int shiftStart_, int shiftEnd_) {
		super();
		this.setShift(shiftStart_, shiftEnd_);
		this.setWorkplace(restaurantToWorkAt);
		this.setSalary(RESTAURANTZHANGHOSTSALARY);
	}

	public String getName() {
		return super.getPerson().getName();
	}

	// Messages

	public void msgImEntering(RestaurantZhangCustomer cust) {
		enteringCustomers.add(cust);
		numberOfCustomersInRestaurant++;
		stateChanged();
	}

	public void msgIWantFood(RestaurantZhangCustomer cust) {
		waitingCustomers.add(cust);
		stateChanged();
	}

	public void msgImLeaving(RestaurantZhangCustomer cust) {
		waitingCustomers.remove(cust);
		numberOfCustomersInRestaurant--;
		stateChanged();
	}

	public void msgTableOpen(RestaurantZhangTable t) {
		for (RestaurantZhangTable table : tables) {
			if (table.equals(t)) {
				print("Table " + table.tableNumber + " is unoccupied.");
				table.setUnoccupied();
				stateChanged();
				break;
			}
		}
	}

	public void msgWaiterRequestBreak(RestaurantZhangWaiter w) {
		for(MyWaiter temp : myWaiterList) {
			if(temp.w.equals(w)) {
				if(temp.mwBreakStatus == MWBreakStatus.notOnBreak) {
					temp.mwBreakStatus = MWBreakStatus.wantToBreak;
					stateChanged();
					break;
				}
			}
		}
	}

	public void msgOffBreak(RestaurantZhangWaiter w) {
		for(MyWaiter mw : myWaiterList) {
			if(mw.w.equals(w)) {
				mw.mwBreakStatus = MWBreakStatus.notOnBreak;
			}
		}
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */

	@Override
	public boolean runScheduler() {
		try {
			if(closingRestaurant && numberOfCustomersInRestaurant == 0) {
				super.setInactive();
				closingRestaurant = false;
			}
			if(!enteringCustomers.isEmpty()) {
				if(closingRestaurant == true) {
					print("Telling customer restaurant is closed");
					enteringCustomers.get(0).msgRestaurantClosed();
					enteringCustomers.remove(enteringCustomers.get(0));
					return true;
				}
				giveCustomerWaitingPosition(enteringCustomers.get(0));
				enteringCustomers.remove(enteringCustomers.get(0));
				return true;
			}

			if(!waitingCustomers.isEmpty()) {
				for (RestaurantZhangTable table : tables) {
					if (!table.isOccupied()) {
						if (!waitingCustomers.isEmpty()) {
							notifyWaiter(table, waitingCustomers.get(0));
							return true;
						}
					}
				}
				if(!waitingCustomers.isEmpty()) {
					for(RestaurantZhangCustomer c : waitingCustomers)
						c.msgRestaurantFull();
				}
				return true;
			}
			for(MyWaiter mw : myWaiterList) {
				if(mw.mwBreakStatus == MWBreakStatus.wantToBreak) {
					for(MyWaiter temp : myWaiterList) {
						if(!temp.w.equals(mw)) {
							if (temp.mwBreakStatus == MWBreakStatus.notOnBreak) {
								notifyWaiterBreak(mw, true);
								return true;
							}
						}
					}
					// Only waiter left
					notifyWaiterBreak(mw, false);
					return true;
				}
			}
			return false;
		} catch(ConcurrentModificationException cmeHost) {
			return false;
		}
	}

	// Actions

	void giveCustomerWaitingPosition(RestaurantZhangCustomer c) {
		int pos = 0;
		synchronized(waitingCustomers) {
			for(int i = 0; i < waitingCustomers.size(); i++) {
				if(waitingCustomers.get(i).waitingPosition == pos) {
					pos++;
					i = -1;
				}
			}
		}
		print("Giving customer " + c.getName() + " waiting position " + pos);
		c.msgHereIsYourWaitingPosition(pos);
	}

	void notifyWaiter(RestaurantZhangTable t, RestaurantZhangCustomer c) {
		if(myWaiterList.size() > 0) {
			t.setOccupant(c);
			MyWaiter currentWaiter = myWaiterList.get(0);
			for(MyWaiter mw : myWaiterList){
				if(mw.mwBreakStatus == MWBreakStatus.notOnBreak) {
					currentWaiter = mw;
					break;
				}
			}
			//			Chooses appropriate waiter
			//			Checks which waiter has the least amount of customers currently assigned to them
			//			If there is a tie, choose the waiter with the least amount of total customers served
			for(int i = 1; i < myWaiterList.size(); i++) {
				if(myWaiterList.get(i).mwBreakStatus == MWBreakStatus.notOnBreak) {
					if((myWaiterList.get(i).w.getNumberCustomers() <= currentWaiter.w.getNumberCustomers())
							&& (myWaiterList.get(i).w.getNumberCustomersServed() < currentWaiter.w.getNumberCustomersServed())) {
						currentWaiter = myWaiterList.get(i);
					}
				} else {
					print("Waiter " + myWaiterList.get(i).w.getName() + " is on break.");
				}
			}
			currentWaiter.w.msgSeatCustomer(t, c);
			waitingCustomers.remove(c);
		}
	}

	void notifyWaiterBreak(MyWaiter mw, boolean canGoOnBreak) {
		if(canGoOnBreak) {
			mw.mwBreakStatus = MWBreakStatus.onBreak;
			mw.w.msgGoOnBreak(true);
		} else {
			print("Cannot let waiter " + mw.w.getName() + " go on break");
			mw.mwBreakStatus = MWBreakStatus.notOnBreak;
			mw.w.msgGoOnBreak(false);
		}
	}
	//utilities

	public void addWaiter(RestaurantZhangWaiter wa) {
		myWaiterList.add(new MyWaiter(wa));
		stateChanged();
	}

	public void setTables(Collection<RestaurantZhangTable> t) {
		tables = t;
	}

	enum MWBreakStatus {notOnBreak, wantToBreak, onBreak};
	private class MyWaiter {
		RestaurantZhangWaiter w;
		MWBreakStatus mwBreakStatus = MWBreakStatus.notOnBreak;
		public MyWaiter(RestaurantZhangWaiter w_) {
			w = w_;
		}
	}

	public void setInactive() {
		if(numberOfCustomersInRestaurant == 0)
			super.setInactive();
		else
			closingRestaurant = true;
	}
	
	public void print(String msg) {
//        super.print(msg);
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTZHANG, "RestaurantZhangHostRole " + this.getPerson().getName(), msg);
    }
}

