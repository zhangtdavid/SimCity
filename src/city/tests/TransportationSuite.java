package city.tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import city.tests.roles.BusPassengerTest;
import city.tests.roles.CarPassengerTest;
import city.tests.roles.WalkerTest;

public class TransportationSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite(TransportationSuite.class.getName());
		suite.addTestSuite(BusPassengerTest.class);
		suite.addTestSuite(CarPassengerTest.class);
		suite.addTestSuite(WalkerTest.class);
		return suite;
	}

}
