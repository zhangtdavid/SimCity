package city.roles;

import java.util.List;

import trace.AlertLog;
import trace.AlertTag;
import utilities.EventLog;
import utilities.LoggedEvent;
import utilities.MarketTransaction;
import utilities.MarketTransaction.MarketTransactionState;
import city.Application.FOOD_ITEMS;
import city.bases.JobRole;
import city.bases.interfaces.RestaurantBuildingInterface;
import city.bases.interfaces.RoleInterface;
import city.buildings.interfaces.Market;
import city.roles.interfaces.MarketCustomerDeliveryPayment;

public class MarketCustomerDeliveryPaymentRole extends JobRole implements MarketCustomerDeliveryPayment {

//  Data
//	=====================================================================	
	public EventLog log = new EventLog();
	
	private RestaurantBuildingInterface restaurant;
	private Market market;
	
	private List<MarketTransaction> marketTransactions; // point to list shared with the restaurant cashier
	
	private RoleInterface parent;
	
//	Constructor
//	=====================================================================
	public MarketCustomerDeliveryPaymentRole(RestaurantBuildingInterface r, List<MarketTransaction> marketTransactions, RoleInterface parent) {
		super();
		restaurant = r;
		this.marketTransactions = marketTransactions;
		this.setWorkplace(r);
		this.parent = parent;
    }

//  Messages
//	=====================================================================	
//	Market Cashier
//	---------------------------------------------------------------
	@Override
	public void msgHereIsBill(int bill, int id) {
//		restaurant.getCashier.setActive();
		log.add(new LoggedEvent("MarketCustomerDeliveryPayment received msgHereIsBill from Market Cashier."));
		print("MarketCustomerDeliveryPayment received msgHereIsBill from Market Cashier.");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Need to wait for cashier to add order
		MarketTransaction mt = findMarketTransaction(id);
    	mt.setMarketTransactionState(MarketTransactionState.Processing);
		mt.setBill(bill);
//		if (parent.getClass() == RestaurantChungCashier.class)
//			((RestaurantChungCashier) parent).msgReceivedBill();
		parent.setActivityBegun();
		stateChanged(); // TODO need to fix this, change it to run when setActive
	}

	@Override
	public void msgPaymentReceived(int id) {
		log.add(new LoggedEvent("MarketCustomerDeliveryPayment received msgPaymentReceived from MarketCashier."));
		print("MarketCustomerDeliveryPayment received msgPaymentReceived from MarketCashier.");
		MarketTransaction mt = findMarketTransaction(id);
    	mt.setMarketTransactionState(MarketTransactionState.Done);
	}
	
//  Scheduler
//	=====================================================================	
	@Override
	public boolean runScheduler() {
		for (MarketTransaction mt : marketTransactions) {
			if (mt.getMarketTransactionState() == MarketTransactionState.Processing) {
				pay(mt);
				return true;
			}
		}
		return false;
	}
	
//  Actions
//	=====================================================================
	private void pay(MarketTransaction mt) {
		int payment = checkBill(mt);
		if (payment != -1) {
			market.getCashier().msgHereIsPayment(mt.getOrder().getOrderId(), payment);
			restaurant.setCash(restaurant.getCash()-payment);
	    	mt.setMarketTransactionState(MarketTransactionState.WaitingForConfirmation);
		}
		// handle if bill is wrong
//		else {
//			
//		}
	}

//  Getters
//	=====================================================================
	// Market
	@Override
	public Market getMarket() {
		return market;
	}
	
	public List<MarketTransaction> getMarketTransactions() {
		return marketTransactions;
	}
	
//  Setters
//	=====================================================================	
	@Override
	public void setMarket(Market market) {
		this.market = market;
	}
	
//  Utilities
//	=====================================================================
	@Override
	public int checkBill(MarketTransaction mt) {
		int tempBill = 0;
        for (FOOD_ITEMS item: mt.getOrder().getOrderItems().keySet()) {
        	tempBill += mt.getOrder().getOrderItems().get(item)*market.getPrices().get(item);
        }

        if (tempBill == mt.getBill())
        	return mt.getBill();
        
		return -1;
	}	

	@Override
	public MarketTransaction findMarketTransaction(int id) {
		for(MarketTransaction mt : marketTransactions){
			if(mt.getOrder().getOrderId() == id) {
				return mt;		
			}
		}
		return null;
	}
	
	@Override
	public void print(String msg) {
		this.getPerson().printViaRole("MarketCustomerDeliveryPayment", msg);
        AlertLog.getInstance().logMessage(AlertTag.MARKET, "MarketCustomerDeliveryPaymentRole " + this.getPerson().getName(), msg);
    }
}
