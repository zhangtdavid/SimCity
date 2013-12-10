package city.tests.roles.mocks;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utilities.EventLog;
import utilities.LoggedEvent;
import utilities.MarketOrder;
import utilities.MarketTransaction;
import city.Application.FOOD_ITEMS;
import city.bases.Role;
import city.buildings.interfaces.Market;
import city.buildings.interfaces.RestaurantChung;
import city.roles.MarketCustomerDeliveryPaymentRole;
import city.roles.RestaurantChungCashierRole.Transaction;
import city.roles.interfaces.MarketCustomerDeliveryPayment;
import city.roles.interfaces.RestaurantChungCashier;
import city.roles.interfaces.RestaurantChungCustomer;
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
	public List<Role> roles = new ArrayList<Role>();
	public List<MarketTransaction> marketTransactions = Collections.synchronizedList(new ArrayList<MarketTransaction>());
	
	public MockRestaurantChungCashier(RestaurantChung restaurant) {
		roles.add(new MarketCustomerDeliveryPaymentRole(restaurant, marketTransactions, this));
		roles.get(0).setActive();
		roles.add((Role) restaurant.getBankCustomer());
		roles.get(1).setActive();
	}

	@Override
	public void msgComputeBill(RestaurantChungWaiter w, RestaurantChungCustomer c, FOOD_ITEMS order) {
		log.add(new LoggedEvent("RestaurantChungCashier received msgComputeBill from RestaurantChungWaiter"));		
		System.out.println("RestaurantChungCashier received msgComputeBill from RestaurantChungWaiter");			
	}

	@Override
	public void msgHereIsPayment(RestaurantChungCustomer c, int bill) {
		log.add(new LoggedEvent("RestaurantChungCashier received msgHereIsPayment from RestaurantChungCustomer"));		
		System.out.println("RestaurantChungCashier received msgHereIsPayment from RestaurantChungCustomer");			
	}

	@Override
	public void msgAddMarketOrder(Market selectedMarket, MarketOrder o) {
		log.add(new LoggedEvent("RestaurantChungCashier received msgAddMarketOrder from RestaurantChungCook"));		
		System.out.println("RestaurantChungCashier received msgAddMarketOrder from RestaurantChungCook");
	}

	@Override
	public MarketCustomerDeliveryPayment getMarketCustomerDeliveryPayment() {
		return (MarketCustomerDeliveryPayment) roles.get(0);
	}
	
	@Override
	public void setRestaurant(RestaurantChung restaurant) {
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
	public PropertyChangeSupport getPropertyChangeSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStateString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Transaction> getTransactions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MarketTransaction> getMarketTransactions() {
		// TODO Auto-generated method stub
		return null;
	}
}
