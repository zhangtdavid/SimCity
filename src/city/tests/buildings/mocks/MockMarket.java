package city.tests.buildings.mocks;

import java.util.List;
import java.util.Map;

import city.Application.FOOD_ITEMS;
import city.buildings.interfaces.Market;
import city.gui.interiors.MarketPanel;
import city.roles.interfaces.BankCustomer;
import city.roles.interfaces.MarketCashier;
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
	public List<MarketEmployee> getEmployees() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MarketDeliveryPerson> getDeliveryPeople() {
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

}
