package city.tests.buildings.mocks;

import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;

import city.Application.FOOD_ITEMS;
import city.buildings.interfaces.Market;
import city.gui.interiors.MarketPanel;
import city.roles.interfaces.BankCustomer;
import city.roles.interfaces.MarketCashier;
import city.roles.interfaces.MarketCustomerDelivery;
import city.roles.interfaces.MarketDeliveryPerson;
import city.roles.interfaces.MarketEmployee;
import city.roles.interfaces.MarketManager;
import city.tests.bases.mocks.MockBuilding;

public class MockMarket extends MockBuilding implements Market {

	public MockMarket(String name) {
		super(name);
	}

	@Override
	public void addEmployee(MarketEmployee employee) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeEmployee(MarketEmployee employee) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addDeliveryPerson(MarketDeliveryPerson deliveryPerson) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeDeliveryPerson(MarketDeliveryPerson deliveryPerson) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MarketManager getManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setManager(MarketManager manager) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MarketCashier getCashier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCashier(MarketCashier cashier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MarketPanel getMarketPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarketManager getMarketManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarketCashier getMarketCashier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BankCustomer getBankCustomer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MyMarketEmployee> getEmployees() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MyDeliveryPerson> getDeliveryPeople() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<FOOD_ITEMS, Integer> getInventory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<FOOD_ITEMS, Integer> getPrices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MyDeliveryPerson findDeliveryPerson(MarketDeliveryPerson d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MyMarketCustomer> getCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MyMarketEmployee findEmployee(MarketEmployee me) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MyMarketCustomer findCustomerDelivery(MarketCustomerDelivery cd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PropertyChangeSupport getPropertyChangeSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventory(Map<FOOD_ITEMS, Integer> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getBusinessIsOpen() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getCurrentDeliveryPerson() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setCurrentDeliveryPerson(int currentDeliveryPerson) {
		// TODO Auto-generated method stub
		
	}

}
