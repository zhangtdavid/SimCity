package city.roles;

import java.util.Map;

import trace.AlertLog;
import trace.AlertTag;
import utilities.EventLog;
import utilities.LoggedEvent;
import city.Application.FOOD_ITEMS;
import city.bases.JobRole;
import city.buildings.MarketBuilding;
import city.buildings.interfaces.Market;
import city.buildings.interfaces.Market.MyMarketCustomer;
import city.buildings.interfaces.Market.MyMarketCustomer.MarketCustomerState;
import city.buildings.interfaces.Market.MyMarketEmployee;
import city.roles.interfaces.MarketCustomer;
import city.roles.interfaces.MarketCustomerDelivery;
import city.roles.interfaces.MarketCustomerDeliveryPayment;
import city.roles.interfaces.MarketEmployee;
import city.roles.interfaces.MarketManager;

public class MarketManagerRole extends JobRole implements MarketManager {
//  Data
//	=====================================================================	
	public EventLog log = new EventLog();

	private Market market;

	private boolean itemsLow;
	
	private WorkingState workingState = WorkingState.Working;
	
//	Constructor
//	=====================================================================
	public MarketManagerRole(Market b, int t1, int t2) {
		super();
		market = b;
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(MarketBuilding.WORKER_SALARY);
	}
	
//  Activity
//	=====================================================================	
//	// TODO schung 99c0f4da25
// 	@Override
//	public void setActive() {
//		super.setActivityBegun();
//		super.setActive();
//	}
	
	@Override
	public void setInactive(){
		workingState = WorkingState.GoingOffShift;
	}
	
//  Messages
//	=====================================================================	
//	Customer (In Person)
//	---------------------------------------------------------------
	@Override
	public void msgIWouldLikeToPlaceAnOrder(MarketCustomer c) {
		if (workingState != WorkingState.NotWorking) {
			log.add(new LoggedEvent("Market Manager received msgIWouldLikeToPlaceAnOrder from Market Customer In Person."));
			print("Market Manager received msgIWouldLikeToPlaceAnOrder from Market Customer In Person.");
			market.getCustomers().add(new MyMarketCustomer(c));
			stateChanged();
		}
	}
	
//	Customer (Delivery)
//	---------------------------------------------------------------
	@Override
	public void msgIWouldLikeToPlaceADeliveryOrder(MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay, Map<FOOD_ITEMS, Integer> o, int id) {
		if (workingState != WorkingState.NotWorking) {
			log.add(new LoggedEvent("Market Manager received msgIWouldLikeToPlaceADeliveryOrder from Market Customer Delivery."));
			print("Market Manager received msgIWouldLikeToPlaceADeliveryOrder from Market Customer Delivery.");
			market.getCustomers().add(new MyMarketCustomer(c, cPay, o, id));
			stateChanged();
		}
	}
	
//	Employee
//	---------------------------------------------------------------
	@Override
	public void msgWhatWouldCustomerDeliveryLike(MarketEmployee e) {
		log.add(new LoggedEvent("Market Manager received msgWhatWouldCustomerDeliveryLike from Market Employee."));
		print("Market Manager received msgWhatWouldCustomerDeliveryLike from Market Employee.");
		MyMarketEmployee tempEmployee = market.findEmployee(e);
		tempEmployee.setMarketEmployeeState(MyMarketEmployee.MarketEmployeeState.GettingOrder);
		stateChanged();
	}
	
	@Override
	public void msgIAmAvailableToAssist(MarketEmployee e) {
		log.add(new LoggedEvent("Market Manager received msgIAmAvailableToAssist from Market Employee."));
		print("Market Manager received msgIAmAvailableToAssist from Market Employee.");
		MyMarketEmployee tempEmployee = market.findEmployee(e);
		tempEmployee.setMarketEmployeeState(MyMarketEmployee.MarketEmployeeState.Available);
		stateChanged();
	}
	
	@Override
	public void msgItemLow() {
		log.add(new LoggedEvent("Market Manager received msgItemLow from Market Employee."));
		print("Market Manager received msgItemLow from Market Employee.");
		itemsLow = true;
		stateChanged();
	}

//  Scheduler
//	=====================================================================
	@Override
	public boolean runScheduler() {
		if (workingState == WorkingState.GoingOffShift) {
			if (market.getManager() != this)
				workingState = WorkingState.NotWorking;
		}		
		
		synchronized(market.getEmployees()) {
			for (MyMarketEmployee employee : market.getEmployees()) {
				if (employee.getMarketEmployeeState() == MyMarketEmployee.MarketEmployeeState.GettingOrder) {
					giveCustomerDeliveryOrder(employee);
					return true;
				}
			}
		}
		
		synchronized(market.getCustomers()) {
			for (MyMarketCustomer customer : market.getCustomers()) {
				if (customer.getState() == MarketCustomerState.WaitingForService && market.getEmployees().size() > 0) {
					synchronized(market.getEmployees()) {
						for (MyMarketEmployee employee : market.getEmployees()) {
							if (employee.getMarketEmployeeState() == MyMarketEmployee.MarketEmployeeState.Available) {
								assistCustomer(customer, employee);
								customer.setState(MarketCustomerState.GotService);
								return true;
							}
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
		if (c.getCustomer() != null) {
			e.getEmployee().msgAssistCustomer(c.getCustomer());
//			market.getCustomers().remove(c);
			e.setMarketEmployeeState(MyMarketEmployee.MarketEmployeeState.CollectingItems);			
		}
		else {
			e.setCustomerDelivery(c.getCustomerDelivery());
			e.getEmployee().msgAssistCustomerDelivery(c.getCustomerDelivery(), c.getCustomerDeliveryPayment());
			e.setMarketEmployeeState(MyMarketEmployee.MarketEmployeeState.GoingToPhone);
		}
	}
	
	private void giveCustomerDeliveryOrder(MyMarketEmployee e) {
		MyMarketCustomer cd = market.findCustomerDelivery(e.getCustomerDelivery());
		e.getEmployee().msgHereIsCustomerDeliveryOrder(cd.getOrder(), cd.getOrderId());
		e.setMarketEmployeeState(MyMarketEmployee.MarketEmployeeState.CollectingItems);
//		market.getCustomers().remove(cd);
	}
	
//  Getters
//	=====================================================================
	@Override
	public Market getMarket() {
		return market;
	}
	
	@Override
	public boolean getItemsLow() {
		return itemsLow;
	}
	
	@Override
	public WorkingState getWorkingState() {
		return workingState;
	}
	
	@Override
	public String getStateString() {
		return workingState.toString();
	}

//  Setters
//	=====================================================================
	@Override
	public void setMarket(Market market) {
		this.market = market;
	}
	
//	Utilities
//=====================================================================	
	@Override
	public void print(String msg) {
		this.getPerson().printViaRole("MarketManager", msg);
        AlertLog.getInstance().logMessage(AlertTag.MARKET, "MarketManagerRole " + this.getPerson().getName(), msg);
    }
}
