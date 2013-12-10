package city.tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import city.tests.roles.RestaurantZhangCashierTest;
import city.tests.roles.RestaurantZhangCookTest;
import city.tests.roles.RestaurantZhangCustomerTest;
import city.tests.roles.RestaurantZhangWaiterRegularTest;
import city.tests.roles.RestaurantZhangWaiterSharedDataTest;

public class RestaurantZhangSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite(RestaurantZhangSuite.class.getName());
		suite.addTestSuite(RestaurantZhangCashierTest.class);
		suite.addTestSuite(RestaurantZhangCookTest.class);
		suite.addTestSuite(RestaurantZhangCustomerTest.class);
		suite.addTestSuite(RestaurantZhangWaiterSharedDataTest.class);
		suite.addTestSuite(RestaurantZhangWaiterRegularTest.class);
		return suite;
	}

}
