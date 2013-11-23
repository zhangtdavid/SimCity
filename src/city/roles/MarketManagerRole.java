package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketCustomerDeliveryPayment;
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
		MarketCustomerDelivery customerDelivery;
		MarketEmployeeState s;
		
		public MyMarketEmployee(MarketEmployee employee) {
			this.employee = employee;
			customerDelivery = null;
			s = MarketEmployeeState.Available;
		}
	}
	private enum MarketEmployeeState
	{Available, GoingToPhone, GettingOrder, CollectingItems};

//	Customers
//	---------------------------------------------------------------
	private List<MyMarketCustomer> customers = Collections.synchronizedList(new ArrayList<MyMarketCustomer>());
	private class MyMarketCustomer {
		MarketCustomer customer;
		MarketCustomerDelivery customerDelivery;
		MarketCustomerDeliveryPayment customerDeliveryPayment;
		
	    private Map<String, Integer> order = new HashMap<String, Integer>();
		
		public MyMarketCustomer(MarketCustomer customer) {
			this.customer = customer;
			customerDelivery = null;
		}
		public MyMarketCustomer(MarketCustomerDelivery customer, Map<String, Integer> o) {
			this.customer = null;
			this.customerDelivery = customer;
			
            for (String item: o.keySet()) {
                order.put(item, o.get(item)); // Create a deep copy of the order map
            }
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
	public void msgIWouldLikeToPlaceADeliveryOrder(MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay, Map<String, Integer> o) {
		log.add(new LoggedEvent("Market Manager received msgIWouldLikeToPlaceADeliveryOrder from Market Customer Delivery."));
		System.out.println("Market Manager received msgIWouldLikeToPlaceADeliveryOrder from Market Customer Delivery.");
		customers.add(new MyMarketCustomer(c, o));
		stateChanged();
	}
	
//	Employee
//	---------------------------------------------------------------
	public void msgWhatWouldCustomerDeliveryLike(MarketEmployee e) {
		MyMarketEmployee tempEmployee = findEmployee(e);
		tempEmployee.s = MarketEmployeeState.GettingOrder;
		stateChanged();
	}
	
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
		synchronized(employees) {
			for (MyMarketEmployee employee : employees) {
				if (employee.s == MarketEmployeeState.GettingOrder) {
					giveCustomerDeliveryOrder(employee);
					return true;
				}
			}
		}
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
		if (c.customer != null) {
			e.employee.msgAssistCustomer(c.customer);
			customers.remove(c);
			e.s = MarketEmployeeState.CollectingItems;			
		}
		else{
			e.customerDelivery = c.customerDelivery;
			e.employee.msgAssistCustomerDelivery(c.customerDelivery, c.customerDeliveryPayment);
			e.s = MarketEmployeeState.GoingToPhone;
		}
	}
	
	private void giveCustomerDeliveryOrder(MyMarketEmployee e) {
		MyMarketCustomer cd = findCustomerDelivery(e.customerDelivery);
		e.employee.msgHereIsCustomerDeliveryOrder(cd.order);
		e.s = MarketEmployeeState.CollectingItems;
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
	
	private MyMarketCustomer findCustomerDelivery(MarketCustomerDelivery cd) {
		for(MyMarketCustomer c : customers ){
			if(c.customerDelivery == cd) {
				return c;		
			}
		}
		return null;
	}
}
