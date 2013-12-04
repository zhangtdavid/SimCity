package city.tests.mock;

import city.abstracts.MockBuilding;
import city.interfaces.Market;
import city.interfaces.MarketCashier;
import city.interfaces.MarketDeliveryPerson;
import city.interfaces.MarketEmployee;
import city.interfaces.MarketManager;

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

}
