package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import city.interfaces.MarketManager;
import city.Role;

public class MarketManagerRole extends Role implements MarketManager {
//  Data
//	=====================================================================	
//	Market market;
	
	// TODO move to market class
	public Map<String, Integer> marketInventory = new ConcurrentHashMap<String, Integer>();
	public final Map<String, Double> marketPrices = new ConcurrentHashMap<String, Double>();
	
	boolean itemsLow;
	
//	Employees
//	---------------------------------------------------------------
	private List<MyMarketEmployee> employees = Collections.synchronizedList(new ArrayList<MyMarketEmployee>());
	private class MyMarketEmployee {
		MarketEmployeeRole employee;
		MarketEmployeeState s;
		
		public MyMarketEmployee(MarketEmployeeRole employee) {
			this.employee = employee;
			s = MarketEmployeeState.Available;
		}
	}
	private enum MarketEmployeeState
	{Available, CollectingItems};

//	Customers
//	---------------------------------------------------------------
	private List<MyMarketCustomer> customers = Collections.synchronizedList(new ArrayList<MyMarketCustomer>());
	private class MyMarketCustomer {
		MarketCustomerRole customer;
		MarketCustomerDeliveryRole customerDelivery;
		
		public MyMarketCustomer(MarketCustomerRole customer) {
			this.customer = customer;
			customerDelivery = null;
		}
		public MyMarketCustomer(MarketCustomerDeliveryRole customer) {
			customer = null;
			this.customerDelivery = customer;
		}
	}


//	Constructor
//	---------------------------------------------------------------
	public MarketManagerRole() {
		super(); // TODO
	}
	
//  Messages
//	=====================================================================
//	Customer (In Person)
//	---------------------------------------------------------------
	public void msgIWouldLikeToPlaceAnOrder(MarketCustomerRole c) {
		System.out.println("Market manager received msgIWouldLikeToPlaceAnOrder");
		
		customers.add(new MyMarketCustomer(c));
		stateChanged();
	}
	
//	Customer (Delivery)
//	---------------------------------------------------------------
	public void msgIWouldLikeToPlaceADeliveryOrder(MarketCustomerDeliveryRole c) {
		System.out.println("Market manager received msgIWouldLikeToPlaceADeliveryOrder");

		customers.add(new MyMarketCustomer(c));
		stateChanged();
	}
	
//	Employee
//	---------------------------------------------------------------
	public void msgIAmAvailableToAssist(MarketEmployeeRole e) {
		System.out.println("Market manager received msgIAmAvailableToAssist");
		
		MyMarketEmployee tempEmployee = findEmployee(e);
		if (tempEmployee == null)
			employees.add(new MyMarketEmployee(e));
		else
			tempEmployee.s = MarketEmployeeState.Available;
		stateChanged();
	}
	
	public void msgItemLow() {
		itemsLow = true;
		stateChanged();
	}

//  Scheduler
//	=====================================================================
	@Override
	public boolean runScheduler() {
		synchronized(customers) {
			if (customers.size() > 0 && employees.size() > 0) {
				synchronized(employees) {
					for (MyMarketEmployee employee : employees) {
						if (employee.s == MarketEmployeeState.Available) {
							assistCustomer(customers.get(0), employee);
							return true;
						}
					}
				}
			}
		}

		return false;
	}
	
//	Actions
//=====================================================================
//	Employee
//---------------------------------------------------------------
	private void assistCustomer(MyMarketCustomer c, MyMarketEmployee e) {
		if (c.customer != null)
			e.employee.msgAssistCustomer(c.customer);
		else
			e.employee.msgAssistCustomerDelivery(c.customerDelivery);
		e.s = MarketEmployeeState.CollectingItems;
		customers.remove(c);
			
	}
	// Getters
	
	// Setters
	
//	Utilities
//=====================================================================
	private MyMarketEmployee findEmployee(MarketEmployeeRole me) {
		for(MyMarketEmployee e : employees ){
			if(e.employee == me) {
				return e;		
			}
		}
		return null;
	}
	// Classes
}
