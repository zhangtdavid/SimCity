package city.tests;

import city.tests.roles.RestaurantChoiCashierTest;
import city.tests.roles.RestaurantChoiRevolvingStandCookTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class RestaurantChoiSuite {
	public static Test suite() {
		TestSuite suite = new TestSuite(RestaurantChoiSuite.class.getName());
		suite.addTestSuite(RestaurantChoiCashierTest.class);
		suite.addTestSuite(RestaurantChoiRevolvingStandCookTest.class);
		return suite;
	}
}
