package city.tests;

import city.buildings.MarketBuilding;
import city.interfaces.MarketCashier;
import city.roles.MarketEmployeeRole;
import city.tests.mock.MockMarketCashier;
import city.tests.mock.MockMarketCustomer;
import city.tests.mock.MockMarketCustomerDelivery;
import city.tests.mock.MockMarketEmployee;
import city.tests.mock.MockMarketManager;
import junit.framework.TestCase;

public class MarketCashierTest extends TestCase {
	
	MarketBuilding market;
	MockMarketEmployee employee;
	MockMarketManager manager;
	MockMarketCustomer customer;
	MockMarketCustomerDelivery customerDelivery;
	MarketCashier cashier;
	
	public void setUp() throws Exception {
		super.setUp();
		market = new MarketBuilding("Market1");
		employee = new MarketEmployeeRole(market);
		manager = new MockMarketManager(market);
		customer = new MockMarketCustomer(market);
		customerDelivery = new MockMarketCustomerDelivery(market);
		cashier = new MockMarketCashier(market);
	}
	
}
