package city.tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import city.tests.roles.MarketCashierTest;
import city.tests.roles.MarketCustomerDeliveryPaymentTest;
import city.tests.roles.MarketCustomerDeliveryTest;
import city.tests.roles.MarketCustomerTest;
import city.tests.roles.MarketDeliveryPersonTest;
import city.tests.roles.MarketEmployeeTest;
import city.tests.roles.MarketManagerTest;

public class MarketSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite(MarketSuite.class.getName());
		suite.addTestSuite(MarketCashierTest.class);
		suite.addTestSuite(MarketCustomerDeliveryPaymentTest.class);
		suite.addTestSuite(MarketCustomerDeliveryTest.class);
		suite.addTestSuite(MarketCustomerTest.class);
		suite.addTestSuite(MarketDeliveryPersonTest.class);
		suite.addTestSuite(MarketEmployeeTest.class);
		suite.addTestSuite(MarketManagerTest.class);
		return suite;
	}

}
