package city.roles;

import java.util.List;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.Building;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCashier;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.interfaces.MarketManager;
import city.roles.RestaurantChungCashierRole.MarketTransaction;
import city.roles.RestaurantChungCashierRole.MarketTransactionState;
import city.Application.FOOD_ITEMS;
import city.Role;

public class MarketCustomerDeliveryPaymentRole extends Role implements MarketCustomerDeliveryPayment {

//  Data
//	=====================================================================	
	public EventLog log = new EventLog();

	private Building restaurant;
	
	private MarketBuilding market;
	private MarketManager manager;
	private MarketCashier cashier;
	
	public List<MarketTransaction> marketTransactions; // list shared with the restaurant cashier
	
//	Constructor
//	---------------------------------------------------------------
	public MarketCustomerDeliveryPaymentRole(Building r, List<MarketTransaction> marketTransactions) {
		super(); // TODO
		restaurant = r;
		this.marketTransactions = marketTransactions;
    }	
	
//  Messages
//	=====================================================================	
//	Market Cashier
//	---------------------------------------------------------------
	public void msgHereIsBill(MarketCashier c, int bill, int id) {
		log.add(new LoggedEvent("Market CustomerDeliveryPayment received msgHereIsBill from Market Cashier."));
		System.out.println("Market CustomerDeliveryPayment received msgHereIsBill from Market Cashier.");
		MarketTransaction mt = findMarketTransaction(id);
    	mt.s = MarketTransactionState.Processing;
		cashier = c;
		mt.bill = bill;
		stateChanged();
	}

	public void msgPaymentReceived(int id) {
		log.add(new LoggedEvent("Market CustomerDelivery received msgPaymentReceived from Market Cashier."));
		System.out.println("Market customerDelivery received msgPaymentReceived from Market Cashier.");
		MarketTransaction mt = findMarketTransaction(id);
		removeMarketTransactionFromList(mt);
	}
	
//  Scheduler
//	=====================================================================	
	@Override
	public boolean runScheduler() {
		for (MarketTransaction mt : marketTransactions) {
			if (mt.s == MarketTransactionState.Processing) {
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
			cashier.msgHereIsPayment(mt.order.orderId, payment);
			restaurant.setCash(restaurant.getCash()-payment);
	    	mt.s = MarketTransactionState.WaitingForConfirmation;
		}
		// handle if bill is wrong
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
	
	// Manager
	public MarketManager getManager() {
		return manager;
	}
	
	public void setManager(MarketManager manager) {
		this.manager = manager;
	}
	
	// Cashier
	public MarketCashier getCashier() {
		return cashier;
	}
	
	public void setCashier(MarketCashier cashier) {
		this.cashier = cashier;
	}	
	
//  Utilities
//	=====================================================================
	public int checkBill(MarketTransaction mt) {
		int tempBill = 0;
        for (FOOD_ITEMS item: mt.order.orderItems.keySet()) {
        	tempBill += mt.order.orderItems.get(item)*market.prices.get(item);
        }

        if (tempBill == mt.bill)
        	return mt.bill;
        
		return -1;
	}	

	private MarketTransaction findMarketTransaction(int id) {
		for(MarketTransaction mt : marketTransactions){
			if(mt.order.orderId == id) {
				return mt;		
			}
		}
		return null;
	}
	
	public void removeMarketTransactionFromList(MarketTransaction transaction) {
		for(MarketTransaction mt: marketTransactions) {
			if(mt == transaction) {
				marketTransactions.remove(mt);
				return;
			}
		}
	}
}
