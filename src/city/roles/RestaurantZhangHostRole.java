package city.roles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;

import trace.AlertLog;
import trace.AlertTag;
import utilities.RestaurantZhangTable;
import city.bases.Building;
import city.bases.JobRole;
import city.roles.interfaces.RestaurantZhangCustomer;
import city.roles.interfaces.RestaurantZhangHost;
import city.roles.interfaces.RestaurantZhangWaiter;

/**
 * Restaurant Host Agent
 */
public class RestaurantZhangHostRole extends JobRole implements RestaurantZhangHost {

	// Data
	
	private List<RestaurantZhangCustomer> enteringCustomers = Collections.synchronizedList(new ArrayList<RestaurantZhangCustomer>());
	private List<RestaurantZhangCustomer> waitingCustomers = Collections.synchronizedList(new ArrayList<RestaurantZhangCustomer>());
	private List<MyWaiter> myWaiterList = Collections.synchronizedList(new ArrayList<MyWaiter>());
	private Collection<RestaurantZhangTable> tables;
	private int numberOfCustomersInRestaurant = 0;
	private boolean closingRestaurant = false;

	// Constructor
	
	public RestaurantZhangHostRole(Building restaurantToWorkAt, int shiftStart_, int shiftEnd_) {
		super();
		this.setShift(shiftStart_, shiftEnd_);
		this.setWorkplace(restaurantToWorkAt);
		this.setSalary(RESTAURANTZHANGHOSTSALARY);
	}

	// Messages

	@Override
	public void msgImEntering(RestaurantZhangCustomer cust) {
		enteringCustomers.add(cust);
		numberOfCustomersInRestaurant++;
		stateChanged();
	}

	@Override
	public void msgIWantFood(RestaurantZhangCustomer cust) {
		waitingCustomers.add(cust);
		stateChanged();
	}

	@Override
	public void msgImLeaving(RestaurantZhangCustomer cust) {
		waitingCustomers.remove(cust);
		numberOfCustomersInRestaurant--;
		stateChanged();
	}

	@Override
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

	@Override
	public void msgWaiterRequestBreak(RestaurantZhangWaiter w) {
		for(MyWaiter temp : myWaiterList) {
			if(temp.w.equals(w)) {
				if(temp.mwBreakStatus == MyWaiter.BREAK_STATUS.notOnBreak) {
					temp.mwBreakStatus = MyWaiter.BREAK_STATUS.wantToBreak;
					stateChanged();
					break;
				}
			}
		}
	}

	@Override
	public void msgOffBreak(RestaurantZhangWaiter w) {
		for(MyWaiter mw : myWaiterList) {
			if(mw.w.equals(w)) {
				mw.mwBreakStatus = MyWaiter.BREAK_STATUS.notOnBreak;
			}
		}
	}

	// Scheduler
	
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
				if(mw.mwBreakStatus == MyWaiter.BREAK_STATUS.wantToBreak) {
					for(MyWaiter temp : myWaiterList) {
						if(!temp.w.equals(mw)) {
							if (temp.mwBreakStatus == MyWaiter.BREAK_STATUS.notOnBreak) {
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

	private void giveCustomerWaitingPosition(RestaurantZhangCustomer c) {
		int pos = 0;
		synchronized(waitingCustomers) {
			for(int i = 0; i < waitingCustomers.size(); i++) {
				if(waitingCustomers.get(i).getWaitingPosition() == pos) {
					pos++;
					i = -1;
				}
			}
		}
		print("Giving customer " + c.getPerson().getName() + " waiting position " + pos);
		c.msgHereIsYourWaitingPosition(pos);
	}

	private void notifyWaiter(RestaurantZhangTable t, RestaurantZhangCustomer c) {
		if(myWaiterList.size() > 0) {
			t.setOccupant(c);
			MyWaiter currentWaiter = myWaiterList.get(0);
			for(MyWaiter mw : myWaiterList){
				if(mw.mwBreakStatus == MyWaiter.BREAK_STATUS.notOnBreak) {
					currentWaiter = mw;
					break;
				}
			}
			//			Chooses appropriate waiter
			//			Checks which waiter has the least amount of customers currently assigned to them
			//			If there is a tie, choose the waiter with the least amount of total customers served
			for(int i = 1; i < myWaiterList.size(); i++) {
				if(myWaiterList.get(i).mwBreakStatus == MyWaiter.BREAK_STATUS.notOnBreak) {
					if((myWaiterList.get(i).w.getNumberCustomers() <= currentWaiter.w.getNumberCustomers())
							&& (myWaiterList.get(i).w.getNumCustomersServed() < currentWaiter.w.getNumCustomersServed())) {
						currentWaiter = myWaiterList.get(i);
					}
				} else {
					print("Waiter " + myWaiterList.get(i).w.getPerson().getName() + " is on break.");
				}
			}
			currentWaiter.w.msgSeatCustomer(t, c);
			waitingCustomers.remove(c);
		}
	}

	private void notifyWaiterBreak(MyWaiter mw, boolean canGoOnBreak) {
		if(canGoOnBreak) {
			mw.mwBreakStatus = MyWaiter.BREAK_STATUS.onBreak;
			mw.w.msgGoOnBreak(true);
		} else {
			print("Cannot let waiter " + mw.w.getPerson().getName() + " go on break");
			mw.mwBreakStatus = MyWaiter.BREAK_STATUS.notOnBreak;
			mw.w.msgGoOnBreak(false);
		}
	}
	
	// Getters
	
	@Override
	public int getNumberOfCustomersInRestaurant() {
		return numberOfCustomersInRestaurant;
	}
	
	// Setters

	@Override
	public void setTables(Collection<RestaurantZhangTable> t) {
		tables = t;
	}
	
	@Override
	public void setInactive() {
		if(numberOfCustomersInRestaurant == 0)
			super.setInactive();
		else
			closingRestaurant = true;
	}
	
	// Utilities

	@Override
	public void addWaiter(RestaurantZhangWaiter wa) {
		myWaiterList.add(new MyWaiter(wa));
		stateChanged();
	}
	
	@Override
	public void print(String msg) {
		this.getPerson().printViaRole("RestaurantZhangHost", msg);
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTZHANG, "RestaurantZhangHostRole " + this.getPerson().getName(), msg);
    }

	// Classes
	
	private static class MyWaiter {
		public enum BREAK_STATUS {notOnBreak, wantToBreak, onBreak};
		public RestaurantZhangWaiter w;
		public BREAK_STATUS mwBreakStatus = BREAK_STATUS.notOnBreak;
		
		public MyWaiter(RestaurantZhangWaiter w_) {
			w = w_;
		}
	}

}

