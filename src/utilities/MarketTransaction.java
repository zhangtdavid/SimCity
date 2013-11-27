package utilities;

import city.buildings.MarketBuilding;

/** 
 * A class to handle transactions between RestaurantCashiers who assume MarketCustomerDeliveryPaymentRoles and MarketCasheirs 
 *
 * @author Shirley
 *
 */
public class MarketTransaction {
	public MarketBuilding market;
	public MarketOrder order;
	public int bill;
	public MarketTransactionState s;
	
	public MarketTransaction (MarketBuilding m, MarketOrder o) {
		market = m;
		order = new MarketOrder(o);
        bill = 0;
		s = MarketTransactionState.Pending;
	}
	
	public enum MarketTransactionState
	{Pending, Processing, WaitingForConfirmation};
}

