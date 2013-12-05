package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import trace.AlertLog;
import trace.AlertTag;
import utilities.EventLog;
import utilities.MarketOrder;
import utilities.MarketTransaction;
import city.Application;
import city.Application.FOOD_ITEMS;
import city.Role;
import city.animations.interfaces.RestaurantChoiAnimatedCashier;
import city.buildings.MarketBuilding;
import city.buildings.RestaurantChoiBuilding;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.interfaces.RestaurantChoi;
import city.interfaces.RestaurantChoiCashier;
import city.interfaces.RestaurantChoiCustomer;
import city.interfaces.RestaurantChoiWaiter;

public class RestaurantChoiCashierRole extends Role implements RestaurantChoiCashier {
	
	// Data
	
	private int moneyIncoming = 0; // 0 = no money in transit; 1 = money in transit
	private EventLog log = new EventLog();
	private RestaurantChoiAnimatedCashier cashierGui;
	private boolean wantsToLeave;
	private RestaurantChoiBuilding building;
	private ArrayList<Check> checks = new ArrayList<Check>();
	private List<MarketTransaction> marketTransactions = Collections.synchronizedList(new ArrayList<MarketTransaction>());
	private List<Role> roles = new ArrayList<Role>();

	// Constructor
	
	/**
	 * Initializes Cashier for RestaurantChoi
	 * @param b : for RestaurantChoiBuilding
	 * @param t1 : Start of shift
	 * @param t2 : End of shift
	 */
	public RestaurantChoiCashierRole(RestaurantChoiBuilding b, int t1, int t2){
		super();
		building = b;
		FOOD_COST.put(FOOD_ITEMS.steak, 16);
		FOOD_COST.put(FOOD_ITEMS.pizza, 11);
		FOOD_COST.put(FOOD_ITEMS.chicken, 9);
		FOOD_COST.put(FOOD_ITEMS.salad, 6);
		this.setShift(t1, t2);
		this.setWorkplace(b);
		this.setSalary(RestaurantChoiBuilding.getWorkerSalary());
		building.getBankCustomer().setPerson(this.getPerson());
		roles.add((Role) building.getBankCustomer());
		roles.add(new MarketCustomerDeliveryPaymentRole(building, marketTransactions));
	}

	public RestaurantChoiCashierRole(){ // for testing mechanics
		super();
		FOOD_COST.put(FOOD_ITEMS.steak, 16);
		FOOD_COST.put(FOOD_ITEMS.pizza, 11);
		FOOD_COST.put(FOOD_ITEMS.chicken, 9);
		FOOD_COST.put(FOOD_ITEMS.salad, 6);
	}

	// Messages

	@Override
	public void msgCompute(FOOD_ITEMS choice, RestaurantChoiCustomer c,
			RestaurantChoiWaiter w) {
		checks.add(new Check(choice, c, w));
		stateChanged();

	}
	
	@Override
	public void msgHeresMyPayment(RestaurantChoiCustomer c, int allHisCash){
		//find the customer's check as you get a payment
		synchronized(checks){
			for(int i = 0; i < checks.size(); i++){
				if(c.equals(checks.get(i).getca())){
					if(allHisCash < checks.get(i).getBill()){ // if not enough money, SEND TO DISHES
						checks.get(i).setPayment(allHisCash); // first take all his money
						checks.get(i).setState(Check.NOT_FULFILLED); // then mark as needing to work
						break;
					}else{ // else proceed as normal
						checks.get(i).setState(Check.GET_PAID);
						checks.get(i).setPayment(allHisCash);
						break;
					}
				}
			}
		}
		stateChanged();
	}

	/**
	 * When the cook requests an order from a market, he forwards the cashier a bill.
	 */
	@Override
	public void msgAddMarketOrder(MarketBuilding m, MarketOrder o) {
		marketTransactions.add(new MarketTransaction(m, o));	
	}

	/*
 	public void msgHeresYourMarketBill(Market m, int type, int amount){
		double owed = foodCost.get(type)*amount;
		synchronized(marketBills){
			marketBills.put(m, owed);
		}
		stateChanged();
	}
	 */
	
	@Override
	public void msgHeresYourMoney(int withdrawal) {
		building.setCash(building.getCash()+withdrawal);
		moneyIncoming = NOT_IN_TRANSIT;
		stateChanged();
	}

	@Override
	public void msgDoneWithDishes(RestaurantChoiCustomer c) {
		synchronized(checks){
			for(int i = 0; i < checks.size(); i++){
				if(checks.get(i).getca().equals(c)){
					checks.remove(checks.get(i)); // done with dishes, fulfilled, so remove from list
				}
			}	
		}
	}

	// Scheduler
	
	@Override
	public boolean runScheduler() {
		boolean blocking = false;
		for (Role r : roles){
			if (r.getActive() && r.getActivity()) {
				blocking  = true;
				boolean activity = r.runScheduler();
				if (!activity) {
					r.setActivityFinished();
				}
				break;
			}
		}
		if(wantsToLeave && checks.isEmpty() && building.seatedCustomers == 0 && marketTransactions.isEmpty()){
			wantsToLeave = false;
			super.setInactive();
		}
		//market interactions
		/*	synchronized(marketBills){
					for(int i = 0; i < markets.size(); i++){
						if(marketBills.get(markets.get(i)) > 0){  // double rounding problems? we'll see
							print(marketBills.get(markets.get(i)));
							//if we don't have enough money, get money from the bank
							//assume the restaurant is successful and has unlimited money for the quarter
							if(money < marketBills.get(markets.get(i)) && moneyIncoming == NOT_IN_TRANSIT){
								getMoney();
								moneyIncoming = IN_TRANSIT;
								return true;
							}else if(money > marketBills.get(markets.get(i))){ // if has to pay and can pay
								payMarketBill(markets.get(i), marketBills.get(markets.get(i)));
								print("paying bill");
								return true;
							}
						}
					}
				}*/

		//customer interactions
		for(int i = 0; i < checks.size(); i++){
			if(checks.get(i).getState() == Check.NOT_FULFILLED){
				sendToDishes(checks.get(i));
				return true;
			}
		}
		for(int i = 0; i < checks.size(); i++){
			//if there are any bills that haven't been calculated, calculate and notify waiter.
			if(checks.get(i).getState() == Check.RECEIVED){
				returnCheck(checks.get(i));
				return true;
			}
		}
		for(int i = 0; i < checks.size(); i++){
			if(checks.get(i).getState() == Check.GET_PAID){
				returnChange(checks.get(i));
				return true;
			}
		}
		if(building.getCash() > RestaurantChoi.DEPOSIT_THRESHOLD){
			print("before depositing: " + building.getCash());
			this.depositMoney();
			print("after depositing: " + building.getCash());
		}
		if(building.getCash() < RestaurantChoi.WITHDRAW_THRESHOLD) this.getMoney();
		return blocking;
	}
	
	// Actions
	
	private void returnCheck(Check c) {
		c.getwa().msgHeresCheck(c.getBill(), c.getca());
		c.setState(Check.GIVEN_TO_WAITER);		
	}

	private void sendToDishes(Check ch) {
		ch.getca().msgDoTheDishes((int)(ch.getBill() - ch.getPayment())*3000);
		ch.setState(Check.FULFILL_BY_DISHES); // the deed is done
	}

	private void returnChange(Check ch) {
		print("Received customer payment of " + ch.getPayment());
		int change = ch.getPayment()-ch.getBill();
		building.setCash(building.getCash()+ch.getBill());
		ch.getca().msgHeresYourChange(change);
		checks.remove(ch);		
	}
	
	/*
	private void payMarketBill(Market m, double payment) {
		m.msgHeresYourPayment(payment);
		money-=payment;
		synchronized(marketBills){
			marketBills.put(m, 0.0); // set bill to 0
		}

	}
	 */

	// Getters
	
	@Override
	public RestaurantChoiAnimatedCashier getAnimation() {
		return this.cashierGui;
	}
	
	@Override
	public ArrayList<Check> getChecks() {
		return this.checks;
	}
	
	@Override
	public EventLog getLog() {
		return log;
	}
	
	@Override
	public int getMoneyIncoming() {
		return moneyIncoming;
	}

	//Setters
	
	/*   
	@Override 
	public void setBanker(Banker b){
		restaurantBanker = b; // only one banker (we can trust...) TODO fix so that this matches with bank in simcity201
    } // fixed in setBankCustomer (2 below)
    

	@Override
    public void addMarket(Market m){
     	markets.add(m); // TODO fix so that this matches with market in simcity201
	 	marketBills.put(m,0.0);
	}
	 */ 

	@Override
	public void setGui(RestaurantChoiAnimatedCashier r) {
		this.cashierGui = r;
	}

	@Override
	public void setInactive(){
		if(checks.isEmpty() && this.building.seatedCustomers == 0){ // if no checks and no seated customers
			super.setInactive(); // end role and leave restaurant
		}
		else
			wantsToLeave = true; // if there are things to deal with, set yourself as not wanting more things to do
	}
	
	// Utilities

	private void getMoney() { //TODO bank needs to incorporate withdrawal
		//moneyIncoming = IN_TRANSIT;
		//this.building.bankConnection.setActive(TODO[something that makes withdraw work], RestaurantChoiBuilding.DAILY_CAPITAL-building.getCash(), Application.TRANSACTION_TYPE.business);
	}

	private void depositMoney() {
		int toDep=building.getCash()-RestaurantChoi.DAILY_CAPITAL;
		this.building.getBankCustomer().setActive(Application.BANK_SERVICE.atmDeposit, toDep, Application.TRANSACTION_TYPE.business);
		building.setCash(building.getCash()-toDep);
	}

	@Override
	public void print(String msg) {
        super.print(msg);
        AlertLog.getInstance().logMessage(AlertTag.RESTAURANTCHOI, "RestaurantChoiCashierRole " + this.getPerson().getName(), msg);
    }

	// Classes
	
	public class Check {
		public final static int RECEIVED = 0;
		public final static int GIVEN_TO_WAITER = 1;
		public final static int GET_PAID = 2;
		public final static int NOT_FULFILLED = -5;
		public final static int FULFILL_BY_DISHES = 3;
		public int bill;
		private int payment;
		private int state;
		private RestaurantChoiCustomer ca;
		private RestaurantChoiWaiter wa;

		public Check(FOOD_ITEMS choice, RestaurantChoiCustomer c, RestaurantChoiWaiter w){
			this.setwa(w);
			setBill(FOOD_COST.get(choice)); // takes choice, gives cost.
			this.setca(c);
			setState(RECEIVED);
			setPayment(0);
		}

		public RestaurantChoiCustomer getca() {
			return ca;
		}
		public void setca(RestaurantChoiCustomer ca) {
			this.ca = ca;
		}
		public int getBill() {
			return bill;
		}
		public void setBill(int bill) {
			this.bill = bill;
		}
		public int getPayment() {
			return payment;
		}
		public void setPayment(int payment) {
			this.payment = payment;
		}
		public int getState() {
			return state;
		}
		public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment() {
			return (MarketCustomerDeliveryPayment) roles.get(0);
		}
		public void setState(int state) {
			this.state = state;
		}
		public RestaurantChoiWaiter getwa() {
			return wa;
		}
		public void setwa(RestaurantChoiWaiter wa) {
			this.wa = wa;
		}
	}

}
