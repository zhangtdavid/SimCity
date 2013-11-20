package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketEmployee;
import city.interfaces.MarketManager;
import city.Role;

public class MarketManagerRole extends Role implements MarketManager {
//  Data
//	=====================================================================	
	public EventLog log = new EventLog();

	private MarketBuilding market;
	
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
	public MarketManagerRole() {
		super();
	}
	
//  Messages
//	=====================================================================
//	Market
//	---------------------------------------------------------------
	public void msgNewEmployee(MarketEmployee e) {
		log.add(new LoggedEvent("Market Manager received msgNewEmployee from Market."));
		System.out.println("Market Manager received msgNewEmployee from Market.");
		employees.add(new MyMarketEmployee(e));
		stateChanged();
	}
	
	public void msgRemoveEmployee(MarketEmployee e) {
		log.add(new LoggedEvent("Market Manager received msgRemoveEmployee from Market."));
		System.out.println("Market Manager received msgRemoveEmployee from Market.");
		MyMarketEmployee me = findEmployee(e);
		employees.remove(me);
		stateChanged();
	}
	
//	Customer (In Person)
//	---------------------------------------------------------------
	public void msgIWouldLikeToPlaceAnOrder(MarketCustomer c) {
		log.add(new LoggedEvent("Market Manager received msgIWouldLikeToPlaceAnOrder from Market Customer In Person."));
		System.out.println("Market Manager received msgIWouldLikeToPlaceAnOrder from Market Customer In Person.");
		customers.add(new MyMarketCustomer(c));
		stateChanged();
	}
	
//	Customer (Delivery)
//	---------------------------------------------------------------
	public void msgIWouldLikeToPlaceADeliveryOrder(MarketCustomerDelivery c) {
		log.add(new LoggedEvent("Market Manager received msgIWouldLikeToPlaceADeliveryOrder from Market Customer Delivery."));
		System.out.println("Market Manager received msgIWouldLikeToPlaceADeliveryOrder from Market Customer In Person.");
		customers.add(new MyMarketCustomer(c));
		stateChanged();
	}
	
//	Employee
//	---------------------------------------------------------------
	public void msgIAmAvailableToAssist(MarketEmployee e) {
		log.add(new LoggedEvent("Market Manager received msgIAmAvailableToAssist from Market Employee."));
		System.out.println("Market Manager received msgIAmAvailableToAssist from Market Employee.");
		MyMarketEmployee tempEmployee = findEmployee(e);
//		if (tempEmployee == null)
//			employees.add(new MyMarketEmployee(e));
//		else
		tempEmployee.s = MarketEmployeeState.Available;
		stateChanged();
	}
	
	public void msgItemLow() {
		log.add(new LoggedEvent("Market Manager received msgItemLow from Market Employee."));
		System.out.println("Market Manager received msgItemLow from Market Employee.");
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
	
	
//  Getters and Setters
//	=====================================================================
	// Market
	public MarketBuilding getMarket() {
		return market;
	}
	
	public void setMarket(MarketBuilding market) {
		this.market = market;
	}
	
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
}
