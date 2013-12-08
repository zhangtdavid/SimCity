package city.buildings.interfaces;

import java.util.Collection;
import java.util.List;

import utilities.RestaurantChungRevolvingStand;
import city.animations.RestaurantChungWaiterAnimation;
import city.bases.interfaces.RestaurantBuildingInterface;
import city.gui.interiors.RestaurantChungPanel;
import city.roles.interfaces.BankCustomer;
import city.roles.interfaces.RestaurantChungCashier;
import city.roles.interfaces.RestaurantChungCook;
import city.roles.interfaces.RestaurantChungCustomer;
import city.roles.interfaces.RestaurantChungHost;
import city.roles.interfaces.RestaurantChungWaiter;
import city.roles.interfaces.RestaurantChungHost.CustomerState;
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
	Table findTable(int t);
	public void incrementNumWaitingCustomers();

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
		private RestaurantChungCustomer c;
		private CustomerState s;
		private int positionInLine;
		private int debt;
		
		public MyCustomer(RestaurantChungCustomer customer, int pos) {
			c = customer;
			s = CustomerState.InRestaurant;
			setPositionInLine(pos);
			setDebt(0);
		}

		// Getters
		public RestaurantChungCustomer getRestaurantChungCustomer() {
			return c;
		}

		public CustomerState getCustomerState() {
			return s;
		}
		
		public int getPositionInLine() {
			return positionInLine;
		}
		
		public int getDebt() {
			return debt;
		}
		
		// Setters
		public void setRestaurantChungCustomer(RestaurantChungCustomer c) {
			this.c = c;
		}

		public void setCustomerState(CustomerState s) {
			this.s = s;
		}

		public void setPositionInLine(int positionInLine) {
			this.positionInLine = positionInLine;
		}

		public void setDebt(int debt) {
			this.debt = debt;
		}
		
		// Utilities	
		public void decrementPositionInLine() {
			positionInLine--;
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

	void decrementNumWaitingCustomers();
	RestaurantChungRevolvingStand getOrderStand();
}