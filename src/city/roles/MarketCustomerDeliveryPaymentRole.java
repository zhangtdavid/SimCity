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
import city.buildings.interfaces.Market;
import city.roles.interfaces.MarketCustomerDeliveryPayment;

public class MarketCustomerDeliveryPaymentRole extends JobRole implements MarketCustomerDeliveryPayment {

//  Data
//	=====================================================================	
	private EventLog log = new EventLog();
	
	private RestaurantBuildingInterface restaurant;
	private Market market;
	
	private List<MarketTransaction> marketTransactions; // point to list shared with the restaurant cashier
	
//	Constructor
//	=====================================================================
	public MarketCustomerDeliveryPaymentRole(RestaurantBuildingInterface r, List<MarketTransaction> marketTransactions) {
		super();
		restaurant = r;
		this.marketTransactions = marketTransactions;
		this.setWorkplace(r);
    }

//  Messages
//	=====================================================================	
//	Market Cashier
//	---------------------------------------------------------------
	@Override
	public void msgHereIsBill(int bill, int id) {
		log.add(new LoggedEvent("Market CustomerDeliveryPayment received msgHereIsBill from Market Cashier."));
		System.out.println("Market CustomerDeliveryPayment received msgHereIsBill from Market Cashier.");
		MarketTransaction mt = findMarketTransaction(id);
    	mt.s = MarketTransactionState.Processing;
		mt.bill = bill;
		runScheduler(); // TODO need to fix this, change it to run when setActive
	}

	@Override
	public void msgPaymentReceived(int id) {
		log.add(new LoggedEvent("Market CustomerDelivery received msgPaymentReceived from Market Cashier."));
		System.out.println("Market customerDelivery received msgPaymentReceived from Market Cashier.");
		//	MarketTransaction mt = findMarketTransaction(id);
		//	removeMarketTransactionFromList(mt); // this might have cause when cashier is adding to the list
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
			market.getCashier().msgHereIsPayment(mt.order.orderId, payment);
			restaurant.setCash(restaurant.getCash()-payment);
	    	mt.s = MarketTransactionState.WaitingForConfirmation;
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
        for (FOOD_ITEMS item: mt.order.orderItems.keySet()) {
        	tempBill += mt.order.orderItems.get(item)*market.getPrices().get(item);
        }

        if (tempBill == mt.bill)
        	return mt.bill;
        
		return -1;
	}	

	@Override
	public MarketTransaction findMarketTransaction(int id) {
		for(MarketTransaction mt : marketTransactions){
			if(mt.order.orderId == id) {
				return mt;		
			}
		}
		return null;
	}
	
	@Override
	public void removeMarketTransactionFromList(MarketTransaction transaction) {
		for(MarketTransaction mt: marketTransactions) {
			if(mt == transaction) {
				marketTransactions.remove(mt);
				return;
			}
		}
	}
	
	@Override
	public void print(String msg) {
        AlertLog.getInstance().logMessage(AlertTag.MARKET, "MarketCustomerDeliveryPaymentRole " + this.getPerson().getName(), msg);
    }
}
