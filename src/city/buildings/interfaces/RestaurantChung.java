package city.buildings.interfaces;

import java.util.Collection;
import java.util.List;

import utilities.RestaurantChungRevolvingStand;
import city.Application.FOOD_ITEMS;
import city.animations.RestaurantChungWaiterAnimation;
import city.bases.interfaces.RestaurantBuildingInterface;
import city.gui.interiors.RestaurantChungPanel;
import city.roles.interfaces.BankCustomer;
import city.roles.interfaces.RestaurantChungCashier;
import city.roles.interfaces.RestaurantChungCook;
import city.roles.interfaces.RestaurantChungCustomer;
import city.roles.interfaces.RestaurantChungHost;
import city.roles.interfaces.RestaurantChungWaiter;
import city.roles.interfaces.RestaurantChungHost.WaiterState;

public interface RestaurantChung extends RestaurantBuildingInterface {	
	//	Getters
	public RestaurantChungPanel getRestaurantChungPanel();
	public RestaurantChungHost getRestaurantChungHost();
	public RestaurantChungCashier getRestaurantChungCashier();
	public RestaurantChungCook getRestaurantChungCook();
	public BankCustomer getBankCustomer();
	public List<MyWaiter> getWaiters();
	public List<MyCustomer> getCustomers();
	public Collection<Table> getTables();
	public int getNumWaitingCustomers();
	RestaurantChungRevolvingStand getOrderStand();

	//	Setters
	public void setRestaurantChungHost(RestaurantChungHost host);
	public void setRestaurantChungCashier(RestaurantChungCashier cashier);
	void setNumWaitingCustomers(int num);
	public void setRestaurantChungCook(RestaurantChungCook cook);
	
	//	Utilities
	public void addWaiter(RestaurantChungWaiter w, RestaurantChungWaiterAnimation anim);
	public void removeWaiter(RestaurantChungWaiter waiter);
	void removeCustomerFromList(MyCustomer c);
	MyWaiter findWaiter(RestaurantChungWaiter w);
	MyCustomer findCustomer(RestaurantChungCustomer ca);
	MyCustomer findCustomer(int t);
	Table findTable(int t);
	
	// Classes
	public class MyWaiter {
		private RestaurantChungWaiter w;
		private WaiterState s;
		private int numCustomers;
		
		public MyWaiter(RestaurantChungWaiter w) {
			this.w = w;
			s = WaiterState.Working;
			numCustomers = 0;
		}
		
		// Getters
		public RestaurantChungWaiter getRestaurantChungWaiter() {
			return w;
		}
		
		public WaiterState getWaiterState() {
			return s;
		}
		
		public int getNumCustomers() {
			return numCustomers;
		}		
		
		// Setters
		public void seRestaurantChungWaiter(RestaurantChungWaiter w) {
			this.w = w;
		}

		public void setWaiterState(WaiterState s) {
			this.s = s;
		}

		public void setNumCustomers(int numCustomers) {
			this.numCustomers = numCustomers;
		}
		
		// Utilities
		public void incrementNumCustomers() {
			numCustomers++;
		}
		
		public void decrementNumCustomers() {
			numCustomers--;
		}
	}
	
	public class MyCustomer {
		public enum HostCustomerState {WaitingInLine, WaitingToBeSeated, GettingSeated, DecidingToLeave, Seated, Done};
		public enum WaiterCustomerState {None, Waiting, Seated, ReadyToOrder, Asked, Eating, WaitingForCheck, Leaving};
		public enum CheckState {None, AskedForBill, ReceivedBill, DeliveredBill};
		public enum OrderStatus {None, Ordered, Cooking, Cancelled, DoneCooking, PickedUp, Delivered};

		private RestaurantChungCustomer c;
		private RestaurantChungWaiter waiter;
		private int table;
		private FOOD_ITEMS choice;
		private int bill;
		private int debt;
		private HostCustomerState hs;
		private WaiterCustomerState ws;
		private CheckState cs;
		private OrderStatus os;
		
		public MyCustomer(RestaurantChungCustomer customer) {
			c = customer;
			waiter = null;
			table = -1;
			choice = null;
			bill = 0;
			setDebt(0);
			hs = HostCustomerState.WaitingInLine;
			ws = WaiterCustomerState.None;
			cs = CheckState.None;
			os = OrderStatus.None;
		}

		// Getters
		public RestaurantChungCustomer getRestaurantChungCustomer() {
			return c;
		}

		public RestaurantChungWaiter getWaiter() {
			return waiter;
		}
		
		public int getTable() {
			return table;
		}
		
		public FOOD_ITEMS getChoice() {
			return choice;
		}
		
		public int getBill() {
			return bill;
		}
		
		public int getDebt() {
			return debt;
		}
		
		public HostCustomerState getHostCustomerState() {
			return hs;
		}
		
		public WaiterCustomerState getWaiterCustomerState() {
			return ws;
		}
		
		public CheckState getCheckState() {
			return cs;
		}
		
		public OrderStatus getOrderStatus() {
			return os;
		}
		
		// Setters
		public void setRestaurantChungCustomer(RestaurantChungCustomer c) {
			this.c = c;
		}
		
		public void setWaiter(RestaurantChungWaiter waiter) {
			this.waiter = waiter;
		}

		public void setTable(int table) {
			this.table = table;
		}
		
		public void setChoice(FOOD_ITEMS choice) {
			this.choice = choice;
		}
		
		public void setBill(int bill) {
			this.bill = bill;
		}


		public void setDebt(int debt) {
			this.debt = debt;
		}
		
		public void setHostCustomerState(HostCustomerState s) {
			hs = s;
		}
		
		public void setWaiterCustomerState(WaiterCustomerState s) {
			ws = s;
		}
		
		public void setCheckState(CheckState cs) {
			this.cs = cs;
		}
		
		public void setOrderStatus(OrderStatus os) {
			this.os = os;
		}
	}
	
	public class Table {
		private RestaurantChungCustomer c;
		private int tableNumber;
		
		public Table(int table) {
			c = null;
			setTableNumber(table);
		}
		
		// Getters
		public RestaurantChungCustomer getOccupant() {
			return c;
		}
		
		public int getTableNumber() {
			return tableNumber;
		}
		
		// Setters
		public void setOccupant(RestaurantChungCustomer customer) {
			c = customer;
		}
		public void setUnoccupied() {
			c = null;
		}
		
		public void setTableNumber(int tableNumber) {
			this.tableNumber = tableNumber;
		}
		
		// Utilities
		public String toString() {
			return "table " + getTableNumber();
		}
	}
}