package utilities;

import city.buildings.interfaces.Market;

/** 
 * A class to handle transactions between RestaurantCashiers who assume MarketCustomerDeliveryPaymentRoles and MarketCasheirs 
 *
 * @author Shirley
 *
 */
public class MarketTransaction {
	private Market market;
	private MarketOrder order;
	private int bill;
	private MarketTransactionState s;
	public enum MarketTransactionState {Pending, Processing, WaitingForConfirmation, Done};
	
	public MarketTransaction (Market m, MarketOrder o) {
		setMarket(m);
		setOrder(new MarketOrder(o));
        setBill(0);
		setMarketTransactionState(MarketTransactionState.Pending);
	}
	
	// Getters
	public Market getMarket() {
		return market;
	}

	public MarketOrder getOrder() {
		return order;
	}
	
	public int getBill() {
		return bill;
	}
	
	public MarketTransactionState getMarketTransactionState() {
		return s;
	}
	
	// Setters
	public void setMarket(Market market) {
		this.market = market;
	}

	public void setOrder(MarketOrder order) {
		this.order = order;
	}

	public void setBill(int bill) {
		this.bill = bill;
	}

	public void setMarketTransactionState(MarketTransactionState s) {
		this.s = s;
	}	
}

