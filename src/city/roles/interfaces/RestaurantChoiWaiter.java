package city.roles.interfaces;

import java.util.List;

import utilities.RestaurantChoiOrder;
import utilities.RestaurantChoiRevolvingStand;
import utilities.RestaurantChoiTable;
import city.Application.FOOD_ITEMS;
import city.animations.RestaurantChoiWaiterAnimation;
import city.bases.interfaces.RoleInterface;

public interface RestaurantChoiWaiter extends RoleInterface {

	// Data
	
	public static final int NTABLES = 4; // a global for the number of tables.
	public static final int xstart = 50;
	public static final int ystart = 50;
	
	// Messages
	
	public void msgSeatCustomer(RestaurantChoiCustomer c, RestaurantChoiTable t);
	public void msgReadyToOrder(RestaurantChoiCustomer c);
	public void msgHeresMyOrder(RestaurantChoiCustomer c, FOOD_ITEMS choice);
	public void msgOrderComplete(RestaurantChoiOrder o);
	public void msgImDone(RestaurantChoiCustomer c);
	public void msgOutOfThisFood(RestaurantChoiOrder o);
    public void msgCheckPlz(RestaurantChoiCustomer c, FOOD_ITEMS choice);
	public void msgHeresCheck(int total, RestaurantChoiCustomer ca);
	public void msgRelease();
	public void msgBreakOK(boolean in);

	// Scheduler
	
	// Actions
	
	// Getters
	
	public String getName();
	public List<myCustomer> getMyCustomers();
	public List<RestaurantChoiTable> getTables();
	public boolean askedForBreak();
	public boolean isOnBreak();

	// Setters
	
	public void setHost(RestaurantChoiHost h);
	public void setCook(RestaurantChoiCook c);
	public void setCashier(RestaurantChoiCashier ca);
	public void setAnimation(RestaurantChoiWaiterAnimation r);
	public void setRevolvingStand(RestaurantChoiRevolvingStand rs);
	
	// Utilities
	
	// Classes
	
	public class myCustomer {
		private RestaurantChoiCustomer c;
		private RestaurantChoiTable t;
		private RestaurantChoiOrder or;
		private int checkValue;
		private int customerState;
		
		public static final int WAITING = 0;
		public static final int WAITING_IN_LINE = 999;
		public static final int SEATED = 1;
		public static final int READY_TO_ORDER = 2;
		public static final int ORDERED = 3;
		public static final int ORDERING = 4;
		public static final int WAITING_FOR_FOOD = 5;
		public static final int SERVED = 6;
		public static final int WANTS_CHECK = 7;
		public static final int WAITING_FOR_CHECK = 8;
		public static final int WAITER_HAS_CHECK = 9;
		public static final int LEAVING = 10;
		public static final int ORDERED_BUT_OUT = 11;

		public myCustomer(RestaurantChoiCustomer c, RestaurantChoiTable t) {
			this.setC(c);
			this.setT(t);
			setCustomerState(WAITING);
			setOr(new RestaurantChoiOrder());
		}

		public RestaurantChoiCustomer getC() {
			return c;
		}

		public void setC(RestaurantChoiCustomer c) {
			this.c = c;
		}

		public int getCustomerState() {
			return customerState;
		}

		public void setCustomerState(int customerState) {
			this.customerState = customerState;
		}

		public RestaurantChoiOrder getOr() {
			return or;
		}

		public void setOr(RestaurantChoiOrder or) {
			this.or = or;
		}

		public RestaurantChoiTable getT() {
			return t;
		}

		public void setT(RestaurantChoiTable t) {
			this.t = t;
		}

		public int getCheckValue() {
			return checkValue;
		}

		public void setCheckValue(int checkValue) {
			this.checkValue = checkValue;
		}
	}
}
