package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.Application.FOOD_ITEMS;
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
	
	public enum WorkingState
	{Working, GoingOffShift, NotWorking};
	WorkingState workingState = WorkingState.Working;
	
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
		
	    private Map<FOOD_ITEMS, Integer> order = new HashMap<FOOD_ITEMS, Integer>();
	    int orderId;
		
		public MyMarketCustomer(MarketCustomer customer) {
			this.customer = customer;
			customerDelivery = null;
			customerDeliveryPayment = null;
		}
		public MyMarketCustomer(MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay, Map<FOOD_ITEMS, Integer> o, int id) {
			this.customer = null;
			customerDelivery = c;
			customerDeliveryPayment = cPay;
			
            for (FOOD_ITEMS item: o.keySet()) {
                order.put(item, o.get(item)); // Create a deep copy of the order map
            }
            orderId = id;
		}
	}

//	Constructor
//	---------------------------------------------------------------
	public MarketManagerRole(MarketBuilding b, int t1, int t2) {
		super();
		market = b;
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(MarketBuilding.getWorkerSalary());
	}
	
	public void setActive(){
		this.setActivityBegun();
	}
	
	public void setInActive(){
		workingState = WorkingState.GoingOffShift;
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
		if (workingState != WorkingState.NotWorking) {
			log.add(new LoggedEvent("Market Manager received msgIWouldLikeToPlaceAnOrder from Market Customer In Person."));
			System.out.println("Market Manager received msgIWouldLikeToPlaceAnOrder from Market Customer In Person.");
			customers.add(new MyMarketCustomer(c));
			stateChanged();
		}
	}
	
//	Customer (Delivery)
//	---------------------------------------------------------------
	public void msgIWouldLikeToPlaceADeliveryOrder(MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay, Map<FOOD_ITEMS, Integer> o, int id) {
		if (workingState != WorkingState.NotWorking) {
			log.add(new LoggedEvent("Market Manager received msgIWouldLikeToPlaceADeliveryOrder from Market Customer Delivery."));
			System.out.println("Market Manager received msgIWouldLikeToPlaceADeliveryOrder from Market Customer Delivery.");
			customers.add(new MyMarketCustomer(c, cPay, o, id));
			stateChanged();
		}
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
		if (workingState == WorkingState.GoingOffShift) {
			if (market.employees.size() > 1)
				workingState = WorkingState.NotWorking;
		}		
		
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
		
		if (workingState == WorkingState.NotWorking)
			super.setInactive();

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
		else {
			e.customerDelivery = c.customerDelivery;
			e.employee.msgAssistCustomerDelivery(c.customerDelivery, c.customerDeliveryPayment);
			e.s = MarketEmployeeState.GoingToPhone;
		}
	}
	
	private void giveCustomerDeliveryOrder(MyMarketEmployee e) {
		MyMarketCustomer cd = findCustomerDelivery(e.customerDelivery);
		e.employee.msgHereIsCustomerDeliveryOrder(cd.order, cd.orderId);
		e.s = MarketEmployeeState.CollectingItems;
		customers.remove(cd);
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
