package city.tests.mock;

import java.util.Map;

import utilities.EventLog;
import city.MockRole;
import city.buildings.MarketBuilding;
import city.interfaces.MarketCustomer;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.MarketCustomerDeliveryPayment;
import city.interfaces.MarketEmployee;

public class MockMarketEmployee extends MockRole implements MarketEmployee {
	
	public EventLog log = new EventLog();
	public MarketBuilding market;

	public MockMarketEmployee() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAssistCustomer(MarketCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAssistCustomerDelivery(MarketCustomerDelivery c, MarketCustomerDeliveryPayment cPay) {
		// TODO Auto-generated method stub
		
	}

	public void msgHereIsMyOrder(MarketCustomer c, Map<String, Integer> o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsCustomerDeliveryOrder(Map<String, Integer> o) {
		// TODO Auto-generated method stub
		
	}

}
