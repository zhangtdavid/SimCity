package utilities;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.Semaphore;

import city.Application.FOOD_ITEMS;
import city.animations.RestaurantChoiWaiterAnimation;
import city.animations.interfaces.RestaurantChoiAnimatedCashier;
import city.animations.interfaces.RestaurantChoiAnimatedFurniture;
import city.animations.interfaces.RestaurantChoiAnimatedWaiter;
import city.bases.JobRole;
import city.buildings.RestaurantChoiBuilding;
import city.roles.interfaces.RestaurantChoiCashier;
import city.roles.interfaces.RestaurantChoiCook;
import city.roles.interfaces.RestaurantChoiCustomer;
import city.roles.interfaces.RestaurantChoiHost;
import city.roles.interfaces.RestaurantChoiWaiter;

public abstract class RestaurantChoiWaiterBase extends JobRole implements RestaurantChoiWaiter {

	// Data
	
	protected List<myCustomer> myCustomers = new ArrayList<myCustomer>();
	private List<RestaurantChoiTable> tables;
	private RestaurantChoiHost host;
	protected RestaurantChoiCook cook;
	private RestaurantChoiCashier cashier;
	private String name;
	protected Semaphore inProgress = new Semaphore(0, true);
	protected RestaurantChoiAnimatedWaiter waiterGui;
	private ArrayList<FOOD_ITEMS> outOf = new ArrayList<FOOD_ITEMS>();
	private boolean breakRequested;
	private boolean onBreak;
	protected RestaurantChoiRevolvingStand orderqueue;
	private boolean wantsToLeave;
	private RestaurantChoiBuilding building;
	
	// Constructor
	
	/**
	 * Initializes both types of waiters for RestaurantChoi
	 * 
	 * @param b : for RestaurantChoiBuilding
	 * @param t1 : Start of shift
	 * @param t2 : End of shift
	 */
	public RestaurantChoiWaiterBase(RestaurantChoiBuilding b, int t1, int t2) {		
		super();
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(RestaurantChoiBuilding.getWorkerSalary());
		building = b;
		
		// make some tables
		tables = new ArrayList<RestaurantChoiTable>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new RestaurantChoiTable(ix)); // how you add to a collection
		}
	}	

	/**
	 * Initializes both types of waiters for RestaurantChoi
	 * Secondary constructor for mechanics testing
	 */
	public RestaurantChoiWaiterBase() {
		tables = new ArrayList<RestaurantChoiTable>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new RestaurantChoiTable(ix));// how you add to a collection
		}
	}

	// Messages
	
	@Override
	public void msgRelease() {
		inProgress.release();
		stateChanged();
	}
	
	@Override
	public void msgBreakOK(boolean in) {
		onBreak = in;
		if (in)
			print("now on break!");
		if (!in) {
			breakRequested = false;
		}	
	}

	@Override
	public void msgSeatCustomer(RestaurantChoiCustomer c, RestaurantChoiTable t) {
		// you're given customer and a table
		// find the assigned table, add customer to myCustomers
		for (int i = 0; i < tables.size(); i++) {
			if (tables.get(i).getTableNumber() == t.getTableNumber()) {
				myCustomers.add(new myCustomer(c, t));
			}
		}// basically guaranteed to find the table above
		// set event to SeatCustomer so waiter does that.
		stateChanged();
	}

	@Override
	public void msgReadyToOrder(RestaurantChoiCustomer c) {
		for (int i = 0; i < myCustomers.size(); i++) {
			if (c.getPerson().getName().equals(myCustomers.get(i).getC().getPerson().getName())) {
				myCustomers.get(i).setCustomerState(myCustomer.READY_TO_ORDER);
				print("need to take an order from " + c.getPerson().getName());
				stateChanged();
			}
		}

	}

	@Override
	public void msgHeresMyOrder(RestaurantChoiCustomer c, FOOD_ITEMS choice) {
		synchronized (myCustomers) {
			if (choice == null) {
				// if choice is -1 (customer can't buy anything)
				for (int i = 0; i < myCustomers.size(); i++) {
					if (c.equals(myCustomers.get(i).getC())) {
						myCustomers.get(i).setCustomerState(myCustomer.LEAVING);
						waiterGui.DoLeave();
					}
				}
			} else {
				if (c.getState() == RestaurantChoiCustomer.STATE.ReadyToOrder) {
					// Order received, so mark that in myCustomers.
					for (int i = 0; i < myCustomers.size(); i++) {
						if (c.equals(myCustomers.get(i).getC())
								&& myCustomers.get(i).getCustomerState() == myCustomer.ORDERING) {
							myCustomers.get(i).setCustomerState(myCustomer.ORDERED);
							print("Took order from customer " + c.getPerson().getName());
							myCustomers.get(i).setOr(new RestaurantChoiOrder(choice,
									myCustomers.get(i).getT().getTableNumber(), this));
						}
					}
				}
			}
		}
		stateChanged();
	}

	@Override
	public void msgOrderComplete(RestaurantChoiOrder o) {
		o.setState(RestaurantChoiOrder.READY_AND_NOTIFIED); // verif - useless...
		stateChanged();
	}

	@Override
	public void msgImDone(RestaurantChoiCustomer c) {
		for (int i = 0; i < myCustomers.size(); i++) {
			if (c.equals(myCustomers.get(i).getC())) {
				// print("marked as leaving " + c.getName());
				myCustomers.get(i).setCustomerState(myCustomer.LEAVING);
			}
		}
		stateChanged();
	}

	@Override
	public void msgOutOfThisFood(RestaurantChoiOrder o) {
		print("received msg from cook: out of food "
				+ o.getChoice());
		outOf.add(o.getChoice()); // add this choice to list of foods I don't
		// have
		for (int i = 0; i < myCustomers.size(); i++) {
			// find the customer that the order belongs to by table
			// identification
			if (myCustomers.get(i).getT().getTableNumber() == o.getTableNumber()) {
				myCustomers.get(i).setCustomerState(myCustomer.ORDERED_BUT_OUT);
				stateChanged();
				// go back to this person and ask for order again.
			}
		}
	}

	@Override
	public void msgCheckPlz(RestaurantChoiCustomer c, FOOD_ITEMS choice) {
		for (int i = 0; i < myCustomers.size(); i++) {
			if (c.equals(myCustomers.get(i).getC())
					&& myCustomers.get(i).getCustomerState() == myCustomer.SERVED) {
				myCustomers.get(i).setCustomerState(myCustomer.WANTS_CHECK);
			}
		}
		stateChanged();
	}

	@Override
	public void msgHeresCheck(int amt, RestaurantChoiCustomer c) {
		print("Received check from cashier");
		for (int i = 0; i < myCustomers.size(); i++) {
			if (myCustomers.get(i).getC().equals(c)) {
				myCustomers.get(i).setCheckValue(amt);
				myCustomers.get(i).setCustomerState(myCustomer.WAITER_HAS_CHECK);
			}
		}
		stateChanged();

	}

	// Scheduler
	
	@Override
	public boolean runScheduler() {
		// the scheduler is so clean now!!
		try{
			if(wantsToLeave && myCustomers.size() == 0){
				super.setInactive();
				wantsToLeave = false;
			}
			if (needToRetakeOrder())
				return true;
			if (needToDeliverFood())
				return true;
			if (needToGiveCheck())
				return true;
			if (needToNotifyHost())
				return true;
			if (needToTakeOrder())
				return true;
			if (needToGetCheck())
				return true;
			if (needToSeatCustomer())
				return true;
			if (needToSendOrderToCook())
				return true;
			if (needToGetOrderFromCook())
				return true;
		}catch(ConcurrentModificationException ce){ // as per instructions...
			print("Caught concurrent modification exception by try catch and returning false");
			return false;
		}
		return false;

	}

	// Actions
	
	/*
	private void offBreak() {
		onBreak = false;
		breakRequested = false;
		host.msgImBack(this);
		print("No longer on break");
	}

	private void requestBreak() {
		host.msgIWantABreak(this);
		breakRequested = true;
		print("Requested a break");
	}
	*/

	private void seatCustomer(myCustomer customer) {
		customer.getC().msgFollowMe(this, customer.getT().getxCoord(), customer.getT().getyCoord());
		DoSeatCustomer(customer.getC(), customer.getT());
		try {
			inProgress.acquire();
		} catch (InterruptedException e) {
			// not supported yet
			e.printStackTrace();
		}
		customer.setCustomerState(myCustomer.SEATED);
		customer.getC().msgHeresYourSeat(building.menu); // we're allowed to spawn menus
		// by now, inProgress has already released. NO NEED to do it again in
		// DoLeave();
		waiterGui.DoLeave(); // head back to 50,50
	}

	private void GiveFood(myCustomer mc, RestaurantChoiOrder or) {
		// for loop for all myCustomers
		// if your customer matches order or's customer then give it to that
		// person
		for (int i = 0; i < tables.size(); i++) {
			if (tables.get(i).getTableNumber() == or.getTableNumber()
					&& i < tables.size()) {
				mc.getC().msgOrderArrived(); // to correct customer
				mc.setCustomerState(myCustomer.SERVED);
				i = tables.size(); // break out~
			}
		}
	}

	private void DoSeatCustomer(RestaurantChoiCustomer customer, RestaurantChoiTable table) {
		print("Seating " + customer.getPerson().getName() + " at " + table + " @ (" + table.getxCoord() + ", " + table.getyCoord() + ")");
		waiterGui.setAcquired();
		waiterGui.DoBringToTable(customer, table.getxCoord(), table.getyCoord());
	}

	private void DoGoToTable(RestaurantChoiTable table) {
		waiterGui.setAcquired();
		waiterGui.GoTo(table.getxCoord(), table.getyCoord());
		try {
			inProgress.acquire();
		} catch (InterruptedException e) {
			// not supported yet
			e.printStackTrace();
		}
		stateChanged();
	}
	
	protected void DoGoToCook() {
		waiterGui.setAcquired();
		waiterGui.GoTo(RestaurantChoiAnimatedFurniture.CPX, RestaurantChoiAnimatedFurniture.CPY);
		try {
			inProgress.acquire();
		} catch (InterruptedException e) {
			// not supported yet
			e.printStackTrace();
		}
		waiterGui.DoLeave();
		stateChanged();
	}
	
	protected abstract void sendOrderToCook(int i, RestaurantChoiOrder o);

	private void DoGoToCashier() {
		waiterGui.setAcquired();
		waiterGui.GoTo(RestaurantChoiAnimatedCashier.CASHIER_X, RestaurantChoiAnimatedCashier.CASHIER_Y);
		try {
			inProgress.acquire();
		} catch (InterruptedException e) {
			// not supported yet
			e.printStackTrace();
		}
		waiterGui.DoLeave();
		stateChanged();

	}

	// Getters
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<myCustomer> getMyCustomers() {
		return myCustomers;
	}

	@Override
	public List<RestaurantChoiTable> getTables() {
		return tables;
	}

	@Override
	public boolean askedForBreak() {
		return breakRequested;
	}

	@Override
	public boolean isOnBreak() {
		return onBreak;
	}

	// Setters
	
	@Override
	public void setHost(RestaurantChoiHost h) {
		host = h;

	}

	@Override
	public void setCook(RestaurantChoiCook c) {
		cook = c;

	}

	@Override
	public void setCashier(RestaurantChoiCashier ca) {
		cashier = ca;
	}
	
	@Override
	public void setAnimation(RestaurantChoiWaiterAnimation r) {
		waiterGui = r;
	}

	@Override
	public void setInactive() { 
		if(myCustomers.size() == 0){
			super.setInactive();
			building.getHost().msgSetUnavailable(this);
		}
		else{
			wantsToLeave = true;
			building.getHost().msgSetUnavailable(this); // tell boss i'm done
		}
	}
	
	@Override
	public abstract void setRevolvingStand(RestaurantChoiRevolvingStand rs);
	
	// Utilities

	private boolean needToSeatCustomer() {
		for (int i = 0; i < myCustomers.size(); i++) {
			if (myCustomers.get(i).getCustomerState() == myCustomer.WAITING || myCustomers.get(i).getCustomerState() == myCustomer.WAITING_IN_LINE) {
				// if there are any empty tables
				for (int j = 0; j < tables.size(); j++) {
					if (!tables.get(j).isOccupied()) {
						// seat the customer at that table.
						if(!myCustomers.get(i).getC().getLocation()){
							waiterGui.toTopRight();
						}else if(myCustomers.get(i).getC().getLocation()){
							waiterGui.toWaitingZone();
						}
						waiterGui.setAcquired();
						try {
							inProgress.acquire();
						} catch (InterruptedException e) {
							// not supported yet
							e.printStackTrace();
						}
						seatCustomer(myCustomers.get(i));
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean needToTakeOrder() {
		for (int i = 0; i < myCustomers.size(); i++) {
			/*if (myCustomers.get(i).getC().getChoice() == null) {
				return false;
			}*/
			if (myCustomers.get(i).getCustomerState() == myCustomer.READY_TO_ORDER) {
				// then go to the customer and take his order.
				DoGoToTable(myCustomers.get(i).getT()); // semaphore acquire is in
				// method
				// then ask for his order
				myCustomers.get(i).getC().msgWhatWouldYouLike();
				myCustomers.get(i).setCustomerState(myCustomer.ORDERING);
				return true;
			}
		}
		return false;
	}

	private boolean needToSendOrderToCook() {
		for (int i = 0; i < myCustomers.size(); i++) {
			if (myCustomers.get(i).getOr().getState() == RestaurantChoiOrder.ORDERED) {
				// go to the cook
				myCustomers.get(i).setCustomerState(myCustomer.WAITING_FOR_FOOD);
				print("Now taking order to cook");
				sendOrderToCook(i, myCustomers.get(i).getOr());
				waiterGui.DoLeave();
				return true;
			}
		}
		return false;
	}

	private boolean needToGetOrderFromCook() {
		for (int i = 0; i < myCustomers.size(); i++) {
			if (myCustomers.get(i).getCustomerState() == myCustomer.WAITING_FOR_FOOD) {
				if (myCustomers.get(i).getOr().getState() == RestaurantChoiOrder.READY_AND_NOTIFIED) {
					// go to the cook
					DoGoToCook(); // acquire is in method
					// update the order, affects both sides
					myCustomers.get(i).getOr().setState(RestaurantChoiOrder.GIVEN_TO_WAITER);
					// set waiter icon to having the order. we can only pass
					// back 1 order at a time.
					waiterGui.setOrderIcon(myCustomers.get(i).getOr().getChoice());
					stateChanged();
					return true;
				}
			}
		}
		return false;
	}	

	private boolean needToNotifyHost() {
		for (int i = 0; i < myCustomers.size(); i++) {
			if (myCustomers.get(i).getCustomerState() == myCustomer.LEAVING) {
				print("notify Host of empty table "
						+ myCustomers.get(i).getT().getTableNumber());
				host.msgTablesClear(this, myCustomers.get(i).getT()); // notify clear
				// table
				myCustomers.remove(myCustomers.get(i)); // remove customer from
				// list
				print("removed the customer");
				// host will set the table to unoccupied.
				return true;
			}
		}
		return false;
	}

	private boolean needToDeliverFood() {
		synchronized (myCustomers) {
			for (int i = 0; i < myCustomers.size(); i++) {
				// if state of a customer's order is GIVEN TO WAITER,
				if (myCustomers.get(i).getOr().getState() == RestaurantChoiOrder.GIVEN_TO_WAITER) {
					// GO TO THE CUSTOMER THE ORDER BELONGS TO.
					DoGoToTable(myCustomers.get(i).getT()); // acquire is inside
					// method
					// GIVE THE FOOD TO THE CUSTOMER
					GiveFood(myCustomers.get(i), myCustomers.get(i).getOr());
					// SET CUSTOMER ORDER ICON TO SERVED
					myCustomers.get(i).getC().getGui().setOrderIcon(
							myCustomers.get(i).getC().getChoice(), true);
					// NOW WAITER DOES NOT HAVE ICON.
					waiterGui.setOrderIcon(null);
					myCustomers.get(i).getOr().setState(RestaurantChoiOrder.GIVEN_TO_CUSTOMER);
					waiterGui.DoLeave();
					return true;
				}
			}
		}
		return false;
	}

	private boolean needToRetakeOrder() {
		for (int i = 0; i < myCustomers.size(); i++) {
			if (myCustomers.get(i).getCustomerState() == myCustomer.ORDERED_BUT_OUT) {
				DoGoToTable(myCustomers.get(i).getT()); // acquire is inside method;
				// all gui stuff
				RestaurantChoiMenu m = new RestaurantChoiMenu();
				for (int j = 0; j < outOf.size(); j++) {
					m.dontHaveThis.add(outOf.get(j));
					// now remove the food from outOf so we don't tell others
					// ...if cook gets the food between this customer and the
					// next
				}
				print("Giving revised menu");
				myCustomers.get(i).getC().msgHeresYourNewMenu(m); 
				// now customer does LookAtMenu() again
				waiterGui.DoLeave();
				myCustomers.get(i).setCustomerState(myCustomer.SEATED);
				return true;
			}
		}
		return false;	
	}

	private boolean needToGetCheck() {
		for (int i = 0; i < myCustomers.size(); i++) {
			if (myCustomers.get(i).getCustomerState() == myCustomer.WANTS_CHECK) {
				print("need to get check");
				DoGoToTable(myCustomers.get(i).getT()); // first go to table to
				// recognize
				DoGoToCashier(); // then go to waiter
				cashier.msgCompute(myCustomers.get(i).getC().getChoice(),
						myCustomers.get(i).getC(), this);
				myCustomers.get(i).setCustomerState(myCustomer.WAITING_FOR_CHECK);
				// then go to cashier and get check, then come back.
			}
		}
		return false;
	}
	
	private boolean needToGiveCheck() {
		for (int i = 0; i < myCustomers.size(); i++) {
			if (myCustomers.get(i).getCustomerState() == myCustomer.WAITER_HAS_CHECK) {
				print("need to give check");
				DoGoToTable(myCustomers.get(i).getT()); // first go to table
				// then give customer the check
				myCustomers.get(i).getC()
				.msgHeresYourCheck(myCustomers.get(i).getCheckValue());
				myCustomers.get(i).setCustomerState(myCustomer.LEAVING);
				waiterGui.DoLeave();
			}
		}
		return false;
	}
}
