package city.tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import city.tests.roles.BankCustomerTest;
import city.tests.roles.BankManagerTest;
import city.tests.roles.BankTellerTest;

public class BankSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite(BankSuite.class.getName());
		suite.addTestSuite(BankCustomerTest.class);
		suite.addTestSuite(BankManagerTest.class);
		suite.addTestSuite(BankTellerTest.class);
		return suite;
	}

}
