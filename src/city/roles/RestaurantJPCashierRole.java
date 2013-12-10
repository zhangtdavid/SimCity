package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trace.AlertLog;
import trace.AlertTag;
import utilities.EventLog;
import utilities.LoggedEvent;
import utilities.MarketTransaction;
import city.Application.BANK_SERVICE;
import city.Application.TRANSACTION_TYPE;
import city.bases.JobRole;
import city.bases.Role;
import city.buildings.RestaurantJPBuilding;
import city.buildings.interfaces.RestaurantJP;
import city.roles.interfaces.MarketCustomerDeliveryPayment;
import city.roles.interfaces.RestaurantJPCashier;
import city.roles.interfaces.RestaurantJPCustomer;
import city.roles.interfaces.RestaurantJPWaiter;

public class RestaurantJPCashierRole extends JobRole implements RestaurantJPCashier {
	
	//DATA	
	
	public List<MyBill> Bills = Collections.synchronizedList(new ArrayList<MyBill>());
	String name;
	private boolean wantsInactive = false;
	MarketCustomerDeliveryPayment marketPaymentRole;
	private RestaurantJPBuilding building;
	List<MarketTransaction> marketTransactions = new ArrayList<MarketTransaction>();
	private List<Role> roles = new ArrayList<Role>();
	Map<String, Integer> Prices = new HashMap<String, Integer>();
	public EventLog log  = new EventLog();
	
	//Constructor
	
	public RestaurantJPCashierRole(RestaurantJPBuilding b, int shiftStart, int shiftEnd){
		super();
		building = b;
		building.setCashier(this);
		//name = this.getPerson().getName();
		this.setWorkplace(b);
		this.setSalary(RestaurantJP.WORKER_SALARY);
		this.setShift(shiftStart, shiftEnd);
		marketPaymentRole = new MarketCustomerDeliveryPaymentRole(b, marketTransactions, this);
		Prices.put("Steak", 13);
		Prices.put("Chicken", 11);
		Prices.put("Salad", 6);
		Prices.put("Pizza", 8);
	}
	
//MSGS-----------------------------------------------------------------------------------------------------------
	
	public void msgComputeBill(RestaurantJPWaiter w, RestaurantJPCustomer c, String choice) {
		log.add(new LoggedEvent("ComputeBill message received"));
		//Do("ComputeBill message received");
		synchronized(Bills){
		Bills.add(new MyBill(w, null, choice, c));
		}
		stateChanged();
	}

	/**
	 * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
	 * 
	 * @param total The cost according to the cashier
	 */
	public void msgPayment(RestaurantJPCustomer c, int cash){
		// log.add(new LoggedEvent("Payment received"));
		// RestaurantJPCustomer temp = c;
		// Do("Payment received from " + temp.toString());
		
		synchronized(Bills){
		for(MyBill b : Bills){
			if(b.c == c){
				if(b.tab == cash)
				b.s = state.charging;
			}
		}
		}
		stateChanged();
	}
	
	public void msgFlaking(RestaurantJPCustomer c, int bill){
		//log.add(new LoggedEvent("Customer flaked!"));
		//Do("Customer flaked!");
		stateChanged();
	}
	
	public void msgPayForMarketOrder(String f, MarketManagerRole m){
		//log.add(new LoggedEvent("Market Order charge received"));
		//Do("Market order charge received from " + m.toString() + " for " + f);
		synchronized(Bills){
		Bills.add(new MyBill(null, m, f, null));
		}
		stateChanged();
	}
	/*public void msgWaiterHasGoneOnBreak(WaiterAgent oldWaiter, WaiterAgent newWaiter){
		Do("Handling change in waiter");
		for(Order o : orders){
			if(o.w == oldWaiter)
				o.w = newWaiter;
		}
	}*/
	
//SCHEDULER------------------------------------------------------------------------
	
	public boolean runScheduler() {
		//ready, makingOrder, orderReady
		if(wantsInactive && Bills.size() == 0 && building.seatedCustomers == 0){
			super.setInactive();
			wantsInactive = false;
		}
		synchronized(Bills){
			for(MyBill b : Bills){
			if(b.s == state.pending)
			{
				ComputeIt(b);
				return true;
			}
		}
		}
		synchronized(Bills){
		for(MyBill b : Bills){
			if(b.s == state.charging)
			{
				ChargeIt(b);
				return true;
			}
		}
		}
		if(building.getCash() > 2000){
			building.getBankCustomer().setActive(BANK_SERVICE.atmDeposit, building.getCash() - 2000, TRANSACTION_TYPE.business);
			roles.add((Role) building.getBankCustomer());
			building.setCash(2000);
		}
		
		boolean blocking = false;
		for (Role r : roles) if (r.getActive() && r.getActivity()) {
			blocking  = true;
			boolean activity = r.runScheduler();
			if (!activity) {
				r.setActivityFinished();
			}
			break;
		}
		return blocking;
		
	}

// ACTIONS----------------------------------------------------------------------------------------

	private void ComputeIt(MyBill b) {
		if(b.m == null){
			b.s = state.computing;
			b.tab += Prices.get(b.choice);
			b.w.msgHereIsCheck(b.tab, this, b.c);
		}
		if(b.w == null){
			b.tab = Prices.get(b.choice)*5;
			b.s = state.charging;
		}
	}
	private void ChargeIt(MyBill b) {
		if(b.m == null){
			building.setCash(building.getCash() + b.tab);
			synchronized(Bills){
				Bills.remove(b);
			}
		}
		if(b.w == null){
			if(building.getCash() >= b.tab){
				building.setCash(building.getCash() - b.tab);
				//b.m.msgChargePaid(b.choice);
			}
			else
				//b.m.msgCantAfford(b.choice);
			synchronized(Bills){
			Bills.remove(b);
			}
		}
	}

	//Getters
	
	public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment(){
		return marketPaymentRole;
	}
	
	public String getName() {
		return name;
	}

	//Setters
	
	public void setInactive(){
		if(Bills.size() == 0 && building.seatedCustomers == 0){
			this.getPerson().setCash(this.getPerson().getCash() + RestaurantJP.WORKER_SALARY);
			super.setInactive();
		}
		else
			wantsInactive = true;
	}
	
	//Utilities
	
	@Override
	public void print(String msg) {
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTJP, "RestaurantJPCashierRole " + this.getPerson().getName(), msg);
    }
	
	//Classes
	
	public class MyBill{
		public state s;
		RestaurantJPWaiter w = null;
		public MarketManagerRole m = null;
		public String choice;
		public RestaurantJPCustomer c;
		int tab = 0;
		public MyBill(RestaurantJPWaiter wait, MarketManagerRole market, String ch, RestaurantJPCustomer cust){
			s = state.pending;
			w = wait;
			m = market;
			choice = ch;
			c = cust;
		}
	};

}


