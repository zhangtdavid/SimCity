package city.tests.mock;

import utilities.EventLog;
import utilities.MarketOrder;
import utilities.MarketTransaction;
import city.abstracts.MockRole;
import city.buildings.MarketBuilding;
import city.buildings.RestaurantChungBuilding;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.interfaces.RestaurantChungCashier;
import city.interfaces.RestaurantChungCustomer;
import city.interfaces.RestaurantChungHost;
import city.interfaces.RestaurantChungWaiter;
import city.roles.RestaurantChungCashierRole.Transaction;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockRestaurantChungCashier extends MockRole implements RestaurantChungCashier {
	public EventLog log = new EventLog();

	@Override
	public void msgComputeBill(RestaurantChungWaiter w,
			RestaurantChungCustomer c, String order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsPayment(RestaurantChungCustomer c, int bill) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgAddMarketOrder(MarketBuilding selectedMarket, MarketOrder o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRestaurant(RestaurantChungBuilding restaurant) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHost(RestaurantChungHost host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMarketCustomerDeliveryPaymentPerson() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBankCustomerPerson() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int checkBill(MarketTransaction t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Transaction findTransaction(RestaurantChungCustomer c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarketTransaction findMarketTransaction(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeOrderFromList(Transaction transaction) {
		// TODO Auto-generated method stub
		
	}
}
