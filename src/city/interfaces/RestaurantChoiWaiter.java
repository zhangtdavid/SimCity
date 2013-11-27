package city.interfaces;

import java.util.List;

import utilities.RestaurantChoiOrder;
import utilities.RestaurantChoiTable;
import city.Application.FOOD_ITEMS;

public interface RestaurantChoiWaiter extends RoleInterface{

	//true messages
	public abstract void msgSeatCustomer(RestaurantChoiCustomer restaurantChoiCustomer,
			RestaurantChoiTable restaurantChoiTable);
	public abstract void msgReadyToOrder(RestaurantChoiCustomer c);
	public abstract void msgHeresMyOrder(RestaurantChoiCustomer c, FOOD_ITEMS choice);
	public abstract void msgOrderComplete(RestaurantChoiOrder o);
	public abstract void msgImDone(RestaurantChoiCustomer c);
	public abstract void msgOutOfThisFood(RestaurantChoiOrder o);
    public abstract void msgCheckPlz(RestaurantChoiCustomer c, FOOD_ITEMS choice);
	public abstract void msgHeresCheck(int total, RestaurantChoiCustomer ca);
	//more logistics-related methods
	public abstract void msgRelease();
	public abstract String getName();
	public abstract void msgBreakOK(boolean b);
	public abstract void offBreak();
	public abstract List<myCustomer> getMyCustomers();
	public abstract List<RestaurantChoiTable> getTables();
	public abstract void requestBreak();
	public abstract boolean askedForBreak();
	public abstract boolean isOnBreak();
	//really gui-related activities
	public abstract void seatCustomer(myCustomer customer);
	public abstract void GiveFood(myCustomer mc, RestaurantChoiOrder or);
	public abstract void DoSeatCustomer(RestaurantChoiCustomer customer, RestaurantChoiTable table);
	public abstract void DoGoToTable(RestaurantChoiTable table);
	public abstract void DoGoToCashier();
	public abstract void DoGoToCook();
	//shorthand methods
	public abstract boolean needToSeatCustomer();
	public abstract boolean needToTakeOrder();
	public abstract boolean needToSendOrderToCook();
	public abstract boolean needToGetOrderFromCook();
	public abstract boolean needToNotifyHost();
	public abstract boolean needToDeliverFood();
	public abstract boolean needToRetakeOrder();
	public abstract boolean needToGetCheck();
	public abstract boolean needToGiveCheck();
	//setters
	public abstract void setHost(RestaurantChoiHost h);
	public abstract void setCook(RestaurantChoiCook c);
	public abstract void setCashier(RestaurantChoiCashier ca);
	public abstract void setInactive();
	
	public class myCustomer {
		private RestaurantChoiCustomer c;
		private RestaurantChoiTable t;
		private RestaurantChoiOrder or;
		//private boolean inWaitingZone; GUI-related things not matter now
		private int checkValue;
		private int customerState;
		//	private int Y;
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
/*
		public void setWaitingLocation(boolean in){
			inWaitingZone = in;
		}*/

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
