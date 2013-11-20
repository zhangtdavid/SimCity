package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import city.buildings.MarketBuilding;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketEmployee;
import city.interfaces.MarketManager;
import city.Role;

public class MarketManagerRole extends Role implements MarketManager {
//  Data
//	=====================================================================	
	private MarketBuilding market;
	
	// TODO move to market class
	public Map<String, Integer> marketInventory = new ConcurrentHashMap<String, Integer>();
	public final Map<String, Double> marketPrices = new ConcurrentHashMap<String, Double>();
	
	boolean itemsLow;
	
//	Employees
//	---------------------------------------------------------------
	private List<MyMarketEmployee> employees = Collections.synchronizedList(new ArrayList<MyMarketEmployee>());
	private class MyMarketEmployee {
		MarketEmployee employee;
		MarketEmployeeState s;
		
		public MyMarketEmployee(MarketEmployee employee) {
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
		MarketCustomer customer;
		MarketCustomerDelivery customerDelivery;
		
		public MyMarketCustomer(MarketCustomer customer) {
			this.customer = customer;
			customerDelivery = null;
		}
		public MyMarketCustomer(MarketCustomerDelivery customer) {
			customer = null;
			this.customerDelivery = customer;
		}
	}


//	Constructor
//	---------------------------------------------------------------
	public MarketManagerRole(MarketBuilding market) {
		super();
		this.market = market;
	}
	
//  Messages
//	=====================================================================
//	Customer (In Person)
//	---------------------------------------------------------------
	public void msgIWouldLikeToPlaceAnOrder(MarketCustomer c) {
		System.out.println("Market manager received msgIWouldLikeToPlaceAnOrder");
		
		customers.add(new MyMarketCustomer(c));
		stateChanged();
	}
	
//	Customer (Delivery)
//	---------------------------------------------------------------
	public void msgIWouldLikeToPlaceADeliveryOrder(MarketCustomerDelivery c) {
		System.out.println("Market manager received msgIWouldLikeToPlaceADeliveryOrder");

		customers.add(new MyMarketCustomer(c));
		stateChanged();
	}
	
//	Employee
//	---------------------------------------------------------------
	public void msgIAmAvailableToAssist(MarketEmployee e) {
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
	private MyMarketEmployee findEmployee(MarketEmployee me) {
		for(MyMarketEmployee e : employees ){
			if(e.employee == me) {
				return e;		
			}
		}
		return null;
	}
	// Classes
}
