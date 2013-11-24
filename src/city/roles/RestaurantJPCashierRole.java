package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.Role;
import city.interfaces.RestaurantJPCashier;
import city.interfaces.RestaurantJPCustomer;
import city.interfaces.RestaurantJPWaiter;

public class RestaurantJPCashierRole extends Role implements RestaurantJPCashier {
															//DATA	
	public List<MyBill> Bills = Collections.synchronizedList(new ArrayList<MyBill>());
	String name;
	public Float funds = (float) 100;
	public class MyBill{
		public state s;
		RestaurantJPWaiter w = null;
		public MarketManagerRole m = null;
		public String choice;
		public RestaurantJPCustomer c;
		Float tab = (float) 0;
		public MyBill(RestaurantJPWaiter wait, MarketManagerRole market, String ch, RestaurantJPCustomer cust){
			s = state.pending;
			w = wait;
			m = market;
			choice = ch;
			c = cust;
		}
	};
	public enum state{pending, computing, charging, finished};
	
	Map<String, Float> Prices = new HashMap<String, Float>();
	public EventLog log  = new EventLog();
	public RestaurantJPCashierRole(){
		super();
		name = "Squidward";
		Prices.put("Steak", (float) 12.99);
		Prices.put("Chicken", (float) 10.99);
		Prices.put("Salad", (float) 5.99);
		Prices.put("Pizza", (float) 7.99);
	}

	public String getName() {
		return name;
	}
	public void setFunds(int m){
		funds = (float) m;
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

	public void msgPayment(RestaurantJPCustomer c, Float cash){
		//log.add(new LoggedEvent("Payment received"));
		RestaurantJPCustomer temp = c;
		//Do("Payment received from " + temp.toString());
		
		synchronized(Bills){
		for(MyBill b : Bills){
			if(b.c == c){
				if(b.tab.equals(cash))
				b.s = state.charging;
			}
		}
		}
		stateChanged();
	}
	
	public void msgFlaking(RestaurantJPCustomer c, Float cash){
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
			funds += b.tab;
			synchronized(Bills){
				Bills.remove(b);
			}
		}
		if(b.w == null){
			if(funds >= b.tab){
				funds -= b.tab;
				//b.m.msgChargePaid(b.choice);
			}
			else
				//b.m.msgCantAfford(b.choice);
			synchronized(Bills){
			Bills.remove(b);
			}
		}
	}
	
}


