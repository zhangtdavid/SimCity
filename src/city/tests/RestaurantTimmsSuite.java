package city.tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import city.tests.roles.RestaurantTimmsCashierTest;
import city.tests.roles.RestaurantTimmsCookTest;
import city.tests.roles.RestaurantTimmsCustomerTest;
import city.tests.roles.RestaurantTimmsHostTest;
import city.tests.roles.RestaurantTimmsWaiterTest;

public class RestaurantTimmsSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite(RestaurantTimmsSuite.class.getName());
		suite.addTestSuite(RestaurantTimmsCashierTest.class);
		suite.addTestSuite(RestaurantTimmsCookTest.class);
		suite.addTestSuite(RestaurantTimmsCustomerTest.class);
		suite.addTestSuite(RestaurantTimmsHostTest.class);
		suite.addTestSuite(RestaurantTimmsWaiterTest.class);
		return suite;
	}

}
