package city.tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import city.tests.roles.RestaurantChungCashierTest;
import city.tests.roles.RestaurantChungCookTest;

public class RestaurantChungSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite(RestaurantChungSuite.class.getName());
		suite.addTestSuite(RestaurantChungCashierTest.class);
		suite.addTestSuite(RestaurantChungCookTest.class);
		return suite;
	}

}
