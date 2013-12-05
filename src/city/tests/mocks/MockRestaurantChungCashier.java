package city.tests.mocks;

import java.beans.PropertyChangeSupport;

import utilities.EventLog;
import utilities.LoggedEvent;
import utilities.MarketOrder;
import utilities.MarketTransaction;
import city.buildings.interfaces.Market;
import city.buildings.interfaces.RestaurantChung;
import city.roles.RestaurantChungCashierRole.Transaction;
import city.roles.interfaces.MarketCustomerDeliveryPayment;
import city.roles.interfaces.RestaurantChungCashier;
import city.roles.interfaces.RestaurantChungCustomer;
import city.roles.interfaces.RestaurantChungHost;
import city.roles.interfaces.RestaurantChungWaiter;
import city.tests.bases.mocks.MockRole;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockRestaurantChungCashier extends MockRole implements RestaurantChungCashier {
	public EventLog log = new EventLog();
	public Market market;

	@Override
	public void msgComputeBill(RestaurantChungWaiter w, RestaurantChungCustomer c, String order) {
		log.add(new LoggedEvent("RestaurantChungCustomer received msgComputeBill from RestaurantChungWaiter"));		
		System.out.println("RestaurantChungCustomer received msgComputeBill from RestaurantChungWaiter");			
	}

	@Override
	public void msgHereIsPayment(RestaurantChungCustomer c, int bill) {
		log.add(new LoggedEvent("RestaurantChungCustomer received msgHereIsPayment from RestaurantChungCustomer"));		
		System.out.println("RestaurantChungCustomer received msgHereIsPayment from RestaurantChungCustomer");			
	}

	@Override
	public void msgAddMarketOrder(Market selectedMarket, MarketOrder o) {
		log.add(new LoggedEvent("RestaurantChungCustomer received msgAddMarketOrder from RestaurantChungCook"));		
		System.out.println("RestaurantChungCustomer received msgAddMarketOrder from RestaurantChungCook");
	}

	@Override
	public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setRestaurant(RestaurantChung restaurant) {
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

	@Override
	public PropertyChangeSupport getPropertyChangeSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStateString() {
		// TODO Auto-generated method stub
		return null;
	}
}
