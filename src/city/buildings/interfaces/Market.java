package city.buildings.interfaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import city.Application.FOOD_ITEMS;
import city.bases.interfaces.BuildingInterface;
import city.gui.interiors.MarketPanel;
import city.roles.interfaces.BankCustomer;
import city.roles.interfaces.MarketCashier;
import city.roles.interfaces.MarketCustomer;
import city.roles.interfaces.MarketCustomerDelivery;
import city.roles.interfaces.MarketCustomerDeliveryPayment;
import city.roles.interfaces.MarketDeliveryPerson;
import city.roles.interfaces.MarketEmployee;
import city.roles.interfaces.MarketManager;

public interface Market extends BuildingInterface {
	public enum DeliveryState {None, Pending, Delivering, Arrived, ReturningToMarket};

	// Employee
	public void addEmployee(MarketEmployee employee);
	public void removeEmployee(MarketEmployee employee);

	// Delivery Person
	public void addDeliveryPerson(MarketDeliveryPerson deliveryPerson);
	public void removeDeliveryPerson(MarketDeliveryPerson deliveryPerson);

	// Getters
	public MarketPanel getMarketPanel();
	public MarketManager getMarketManager();
	public MarketCashier getMarketCashier();
	public MarketManager getManager();
	public MarketCashier getCashier();
	public BankCustomer getBankCustomer();
	public List<MyMarketEmployee> getEmployees();
	public List<MyDeliveryPerson> getDeliveryPeople();
	public List<MyMarketCustomer> getCustomers();
	public Map<FOOD_ITEMS, Integer> getInventory();
	public Map<FOOD_ITEMS, Integer> getPrices();
	
	// Setters
	public void setManager(MarketManager manager);
	public void setCashier(MarketCashier cashier);
	public void setInventory(Map<FOOD_ITEMS, Integer> map);

	
	// Utilities
	public MyMarketEmployee findEmployee(MarketEmployee me);
	public MyMarketCustomer findCustomerDelivery(MarketCustomerDelivery cd);
	
	// Classes
	public class MyDeliveryPerson {
		private MarketDeliveryPerson deliveryPerson;
		
		public MyDeliveryPerson(MarketDeliveryPerson d) {
			deliveryPerson = d;
		}
		
		// Getters
		public MarketDeliveryPerson getDeliveryPerson() {
			return deliveryPerson;
		}
		
		// Setters
		public void setDeliveryPerson(MarketDeliveryPerson deliveryPerson) {
			this.deliveryPerson = deliveryPerson;
		}
		
	}

	MyDeliveryPerson findDeliveryPerson(MarketDeliveryPerson d);
	
	// Classes
	public static class MyMarketEmployee {
		private MarketEmployee employee;
		private MarketCustomerDelivery customerDelivery;
		public enum MarketEmployeeState {Available, GoingToPhone, GettingOrder, CollectingItems};
		private MarketEmployeeState s;
		
		public MyMarketEmployee(MarketEmployee employee) {
			this.employee = employee;
			customerDelivery = null;
			s = MarketEmployeeState.Available;
		}
		
		// Getters
		public MarketEmployee getEmployee() {
			return employee;
		}
		
		public MarketCustomerDelivery getCustomerDelivery() {
			return customerDelivery;
		}
		
		public MarketEmployeeState getMarketEmployeeState() {
			return s;
		}
		
		// Setters
		public void setEmployee(MarketEmployee employee) {
			this.employee = employee;
		}
		
		public void setCustomerDelivery(MarketCustomerDelivery customerDelivery) {
			this.customerDelivery = customerDelivery;
		}
		
		public void setMarketEmployeeState(MarketEmployeeState s) {
			this.s = s;
		}
	}
	
	public class MyMarketCustomer {
		private MarketCustomer customer;
		private MarketCustomerDelivery customerDelivery;
		private MarketCustomerDeliveryPayment customerDeliveryPayment;
		public enum MarketCustomerState {WaitingForService, GotService};
		private MarketCustomerState state;
	    private Map<FOOD_ITEMS, Integer> order = new HashMap<FOOD_ITEMS, Integer>();
	    private int orderId;
		
		public MyMarketCustomer(MarketCustomer customer) {
			this.customer = customer;
			customerDelivery = null;
			customerDeliveryPayment = null;
			state = MarketCustomerState.WaitingForService;
		}
		
		public MyMarketCustomer(MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay, Map<FOOD_ITEMS, Integer> o, int id) {
			this.customer = null;
			customerDelivery = c;
			customerDeliveryPayment = cPay;
			state = MarketCustomerState.WaitingForService;
			
            for (FOOD_ITEMS item: o.keySet()) {
                order.put(item, o.get(item)); // Create a deep copy of the order map
            }
            orderId = id;
		}
		
		// Getters
		public MarketCustomer getCustomer() {
			return customer;
		}
		
		public MarketCustomerDelivery getCustomerDelivery() {
			return customerDelivery;
		}
		
		public MarketCustomerDeliveryPayment getCustomerDeliveryPayment() {
			return customerDeliveryPayment;
		}
		
		public MarketCustomerState getState() {
			return state;
		}
		
		public Map<FOOD_ITEMS, Integer> getOrder() {
			return order;
		}
		
		public int getOrderId() {
			return orderId;
		}
		
		// Setters
		public void setCustomer(MarketCustomer customer) {
			this.customer = customer;
		}
		
		public void setCustomerDelivery(MarketCustomerDelivery customerDelivery) {
			this.customerDelivery = customerDelivery;
		}
		
		public void setCustomerDeliveryPayment(
				MarketCustomerDeliveryPayment customerDeliveryPayment) {
			this.customerDeliveryPayment = customerDeliveryPayment;
		}
		
		public void setState(MarketCustomerState state) {
			this.state = state;
		}
		
		public void setOrderId(int orderId) {
			this.orderId = orderId;
		}
	}

	int getCurrentDeliveryPerson();
	void setCurrentDeliveryPerson(int currentDeliveryPerson);
}