package utilities;

import city.interfaces.Market;

/** 
 * A class to handle transactions between RestaurantCashiers who assume MarketCustomerDeliveryPaymentRoles and MarketCasheirs 
 *
 * @author Shirley
 *
 */
public class MarketTransaction {
	public Market market;
	public MarketOrder order;
	public int bill;
	public MarketTransactionState s;
	
	public MarketTransaction (Market m, MarketOrder o) {
		market = m;
		order = new MarketOrder(o);
        bill = 0;
		s = MarketTransactionState.Pending;
	}
	
	public enum MarketTransactionState {Pending, Processing, WaitingForConfirmation};
}

