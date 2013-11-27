package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import utilities.MarketTransaction;
import utilities.RestaurantJPWaiterBase;
import city.Role;
import city.buildings.RestaurantJPBuilding;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.interfaces.RestaurantJPCashier;
import city.interfaces.RestaurantJPCustomer;
import city.interfaces.RestaurantJPWaiter;

public class RestaurantJPCashierRole extends Role implements RestaurantJPCashier {
															//DATA	
	public List<MyBill> Bills = Collections.synchronizedList(new ArrayList<MyBill>());
	String name;
	private boolean wantsInactive = false;
	MarketCustomerDeliveryPayment marketPaymentRole;
	private RestaurantJPBuilding building;
	List<MarketTransaction> marketTransactions = new ArrayList<MarketTransaction>();
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
	public enum state{pending, computing, charging, finished};
	
	Map<String, Integer> Prices = new HashMap<String, Integer>();
	public EventLog log  = new EventLog();
	public RestaurantJPCashierRole(RestaurantJPBuilding b, int shiftStart, int shiftEnd){
		super();
		building = b;
		building.setCashier(this);
		name = this.getPerson().getName();
		this.setWorkplace(b);
		this.setSalary(RestaurantJPBuilding.WORKER_SALARY);
		this.setShift(shiftStart, shiftEnd);
		marketPaymentRole = new MarketCustomerDeliveryPaymentRole(b, marketTransactions);
		Prices.put("Steak", 13);
		Prices.put("Chicken", 11);
		Prices.put("Salad", 6);
		Prices.put("Pizza", 8);
	}
	public void setInactive(){
		if(Bills.size() == 0 && building.seatedCustomers == 0){
			super.setInactive();
		}
		else
			wantsInactive = true;
	}
	
	public String getName() {
		return name;
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

	public void msgPayment(RestaurantJPCustomer c, int cash){
		//log.add(new LoggedEvent("Payment received"));
		RestaurantJPCustomer temp = c;
		//Do("Payment received from " + temp.toString());
		
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
		return false;
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
			building.funds += b.tab;
			synchronized(Bills){
				Bills.remove(b);
			}
		}
		if(b.w == null){
			if(building.funds >= b.tab){
				building.funds -= b.tab;
				//b.m.msgChargePaid(b.choice);
			}
			else
				//b.m.msgCantAfford(b.choice);
			synchronized(Bills){
			Bills.remove(b);
			}
		}
	}
	
	public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment(){
		return marketPaymentRole;
	}
}


