package city.tests.roles;

import java.awt.Color;

import junit.framework.TestCase;
import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangMenu;
import city.buildings.RestaurantZhangBuilding;
import city.gui.interiors.RestaurantZhangPanel;
import city.roles.RestaurantZhangCashierRole;
import city.tests.agents.mocks.MockPerson;
import city.tests.roles.mocks.MockRestaurantZhangCustomer;
import city.tests.roles.mocks.MockRestaurantZhangWaiterRegular;

public class RestaurantZhangCashierTest extends TestCase {
	RestaurantZhangCashierRole cashier;
	MockRestaurantZhangWaiterRegular waiter;
	MockRestaurantZhangCustomer customer;
//	MockMarket market1;
//	MockMarket market2;
	RestaurantZhangMenu menu;
	RestaurantZhangCheck check;

	public void setUp() throws Exception {
		super.setUp();
		cashier = new RestaurantZhangCashierRole(new RestaurantZhangBuilding("Building", new RestaurantZhangPanel(Color.black), null), 0, 100);
		cashier.setPerson(new MockPerson("Person"));
		waiter = new MockRestaurantZhangWaiterRegular();
		waiter.setPerson(new MockPerson("Person"));
		customer = new MockRestaurantZhangCustomer();
		customer.setPerson(new MockPerson("Person"));
//		market1 = new MockMarket("MockMarket1");
//		market2 = new MockMarket("MockMarket2");
		menu = new RestaurantZhangMenu();
		waiter.setCustomer(customer);
	}

	public void testOneNormalCustomerScenario() { // Tests when customer has more than enough money to pay
		cashier.setMenu(menu);
		// Check to make sure cashier's initial state is okay
		assertEquals("Cashier should have 0 checks. It doesn't.", cashier.getPendingChecks().size(), 0);
		assertTrue("Cashier should have 0 customers with tabs. It doesn't.", cashier.getTabCustomers().isEmpty());
		assertFalse("Cashier should have a menu of items. It doesn't.", cashier.getMenu().isEmpty());
		assertEquals("Cashier should have 10000 balance. It doesn't.", 10000, Math.round(cashier.getBalance()));
//		assertTrue("Cashier should have 0 market bills. It doesn't.", cashier.marketBills.isEmpty());

		// Step 1 of the test, ask for bill
		cashier.msgComputeBill(waiter, customer, "chicken"); //send the message from a waiter
		check = cashier.getPendingChecks().get(0);

		// Check postconditions for step 1 and preconditions for step 2
		assertEquals("Cashier should have 1 check in it. It doesn't.", cashier.getPendingChecks().size(), 1);
		assertTrue("Cashier's scheduler should have returned true (process check from waiter), but didn't.",
				cashier.runScheduler());
		assertTrue("Cashier should have 0 checks in it. It doesn't.", cashier.getPendingChecks().isEmpty());
		assertTrue("Waiter should have logged \"Received check from cashier.\" but didn't. His log reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received check from cashier."));
		assertTrue("Customer should have logged \"Received check from waiter.\" but didn't. His log reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), customer.log.containsString("Received check from waiter."));
		assertTrue("Cashier should have 0 customers with tabs. It doesn't.", cashier.getTabCustomers().isEmpty());
		assertEquals("Cashier should have 10000 balance. It doesn't.", 10000, Math.round(cashier.getBalance()));
//		assertTrue("Cashier should have 0 market bills. It doesn't.", cashier.marketBills.isEmpty());

		// Step 2 of the test, give payment to cashier
		cashier.msgHereIsPayment(check, 20); // Sent from customer

		//check postconditions for step 2
		assertEquals("Cashier should have 1 check in it. It doesn't.", 1, cashier.getPendingChecks().size());
		assertEquals("Check status should be atCustomer. It isn't.", check.status, RestaurantZhangCheck.CheckStatus.atCustomer);
		assertEquals("Check should have 20 in payment. It doesn't.", Math.round(check.payment), 20);
		assertTrue("Cashier's scheduler should have returned true (process check from customer), but didn't.",
				cashier.runScheduler());
		assertTrue("Cashier's list of checks should be empty. It isn't.", cashier.getPendingChecks().isEmpty());
		assertTrue("Cashier should have 0 customers with tabs. It doesn't.", cashier.getTabCustomers().isEmpty());
		assertTrue("Customer log should have read \"Got change 11 from cashier.\" It says instead: " + customer.log.getLastLoggedEvent(), customer.log.containsString("Got change 11 from cashier."));
		assertEquals("Cashier should have 10009 balance. It instead has " + cashier.getBalance(), 10009, cashier.getBalance());
//		assertTrue("Cashier should have 0 market bills. It doesn't.", cashier.marketBills.isEmpty());
	}

	public void testTwoNonNormativeCustomerScenario() { // Tests when customer doesn't have enough money for the first time and pays it the second time
		cashier.setMenu(menu);
		// Check to make sure cashier's initial state is okay
		assertEquals("Cashier should have 0 checks. It doesn't.", cashier.getPendingChecks().size(), 0);
		assertTrue("Cashier should have 0 customers with tabs. It doesn't.", cashier.getTabCustomers().isEmpty());
		assertFalse("Cashier should have a menu of items. It doesn't.", cashier.getMenu().isEmpty());
		assertEquals("Cashier should have 10000 balance. It doesn't.", 10000, Math.round(cashier.getBalance()));
//		assertTrue("Cashier should have 0 market bills. It doesn't.", cashier.marketBills.isEmpty());

		// Step 1 of the test, ask for bill
		cashier.msgComputeBill(waiter, customer, "chicken"); //send the message from a waiter
		check = cashier.getPendingChecks().get(0);

		// Check postconditions for step 1 and preconditions for step 2
		assertEquals("Cashier should have 1 check in it. It doesn't.", cashier.getPendingChecks().size(), 1);
		assertTrue("Cashier's scheduler should have returned true (process check from waiter), but didn't.",
				cashier.runScheduler());
		assertTrue("Cashier should have 0 checks in it. It doesn't.", cashier.getPendingChecks().isEmpty());
		assertTrue("Waiter should have logged \"Received check from cashier.\" but didn't. His log reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received check from cashier."));
		assertTrue("Customer should have logged \"Received check from waiter.\" but didn't. His log reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), customer.log.containsString("Received check from waiter."));
		assertTrue("Cashier should have 0 customers with tabs. It doesn't.", cashier.getTabCustomers().isEmpty());
		assertEquals("Cashier should have 10000 balance. It doesn't.", 10000, Math.round(cashier.getBalance()));
//		assertTrue("Cashier should have 0 market bills. It doesn't.", cashier.marketBills.isEmpty());

		// Step 2 of the test, give payment to cashier
		cashier.msgHereIsPayment(check, 2); // Sent from customer

		//check postconditions for step 2
		assertEquals("Cashier should have 1 check in it. It doesn't.", 1, cashier.getPendingChecks().size());
		assertEquals("Check status should be atCustomer. It isn't.", check.status, RestaurantZhangCheck.CheckStatus.atCustomer);
		assertEquals("Check should have 2 in payment. It doesn't.", Math.round(check.payment), 2);
		assertTrue("Cashier's scheduler should have returned true (process check from customer), but didn't.",
				cashier.runScheduler());
		assertTrue("Cashier's list of checks should be empty. It isn't.", cashier.getPendingChecks().isEmpty());
		assertEquals("Cashier should have 1 customers with tabs. It doesn't.", cashier.getTabCustomers().size(), 1);
		assertTrue("Customer log should have read \"Got tab 7 from cashier.\" It didn't.", customer.log.containsString("Got tab 7 from cashier."));
		assertEquals("Cashier should have 10002 balance. It doesn't.", 10002, Math.round(cashier.getBalance()));
//		assertTrue("Cashier should have 0 market bills. It doesn't.", cashier.marketBills.isEmpty());

		// Step 3 of the test, ask for bill again
		cashier.msgComputeBill(waiter, customer, "chicken"); //send the message from a waiter
		check = cashier.getPendingChecks().get(0);

		// Check postconditions for step 3 and preconditions for step 4
		assertEquals("Cashier should have 1 check in it. It doesn't.", cashier.getPendingChecks().size(), 1);
		assertTrue("Cashier's scheduler should have returned true (process check from waiter), but didn't.",
				cashier.runScheduler());
		assertTrue("Cashier should have 0 checks in it. It doesn't.", cashier.getPendingChecks().isEmpty());
		assertTrue("Waiter should have logged \"Received check from cashier.\" but didn't. His log reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received check from cashier."));
		assertTrue("Customer should have logged \"Received check from waiter.\" but didn't. His log reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), customer.log.containsString("Received check from waiter."));
		assertEquals("Cashier should have 10002 balance. It doesn't.", 10002, Math.round(cashier.getBalance()));
//		assertTrue("Cashier should have 0 market bills. It doesn't.", cashier.marketBills.isEmpty());

		// Step 4 of the test, give payment to cashier
		cashier.msgHereIsPayment(check, 20); // Sent from customer

		//check postconditions for step 4
		assertEquals("Cashier should have 1 check in it. It doesn't.", 1, cashier.getPendingChecks().size());
		assertEquals("Check status should be atCustomer. It isn't.", check.status, RestaurantZhangCheck.CheckStatus.atCustomer);
		assertEquals("Check should have 20 in payment. It doesn't.", Math.round(check.payment), 20);
		assertTrue("Cashier's scheduler should have returned true (process check from customer), but didn't.",
				cashier.runScheduler());
		assertTrue("Cashier's list of checks should be empty. It isn't.", cashier.getPendingChecks().isEmpty());
		assertEquals("Cashier should have 0 customers with tabs. It doesn't.", cashier.getTabCustomers().size(), 0);
		assertTrue("Customer log should have read \"Got change 4 from cashier.\" It reads instead " + customer.log.getLastLoggedEvent(), customer.log.containsString("Got change 4 from cashier."));
		assertEquals("Cashier should have 10018 balance. It doesn't." + customer.log.getLastLoggedEvent(), 10018, cashier.getBalance());
//		assertTrue("Cashier should have 0 market bills. It doesn't.", cashier.marketBills.isEmpty());
	}

//	public void testThreeNormalMarketScenario() { // Tests when one market pays
//		int bill = 10;
//		cashier.setMenu(menu);
//		// Check to make sure cashier's initial state is okay
//		assertEquals("Cashier should have 0 checks. It doesn't.", cashier.getPendingChecks().size(), 0);
//		assertTrue("Cashier should have 0 customers with tabs. It doesn't.", cashier.getTabCustomers().isEmpty());
//		assertFalse("Cashier should have a menu of items. It doesn't.", cashier.getMenu().isEmpty());
//		assertEquals("Cashier should have 10000 balance. It doesn't.", 10000, Math.round(cashier.balance));
//		assertTrue("Cashier should have 0 market bills. It doesn't.", cashier.marketBills.isEmpty());
//
//		// Step 1 of the test, get bill from market
//		cashier.msgHereIsMarketBill(market1, bill);
//
//		// Check postconditions for step 1 and preconditions for step 2
//		assertEquals("Cashier should have 1 market bill. It doesn't.", cashier.marketBills.size(), 1);
//		assertTrue("Cashier's scheduler should have returned true (process check from first market), but didn't.",
//				cashier.runScheduler());
//		assertTrue("Cashier should have 0 checks in it. It doesn't.", cashier.getPendingChecks().isEmpty());
//		assertTrue("Cashier should have 0 market bills in it. It doesn't.", cashier.marketBills.isEmpty());
//		assertTrue("Market should have logged \"Recieved payment of 10\" but didn't. His log reads instead: " 
//				+ market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Received payment of 10"));
//		assertTrue("Cashier should have 0 customers with tabs. It doesn't.", cashier.getTabCustomers().isEmpty());
//		assertEquals("Cashier should have 9990 balance. It doesn't.", 9990, Math.round(cashier.balance));
//	}

//	public void testFourNormalMarketScenario() { // Tests when two markets pay
//		int bill1 = 10;
//		int bill2 = 20;
//		cashier.setMenu(menu);
//		// Check to make sure cashier's initial state is okay
//		assertEquals("Cashier should have 0 checks. It doesn't.", cashier.getPendingChecks().size(), 0);
//		assertTrue("Cashier should have 0 customers with tabs. It doesn't.", cashier.getTabCustomers().isEmpty());
//		assertFalse("Cashier should have a menu of items. It doesn't.", cashier.getMenu().isEmpty());
//		assertEquals("Cashier should have 10000 balance. It doesn't.", 10000, Math.round(cashier.balance));
//		assertTrue("Cashier should have 0 market bills. It doesn't.", cashier.marketBills.isEmpty());
//
//		// Step 1 of the test, get bill from market1
//		cashier.msgHereIsMarketBill(market1, bill1);
//
//		// Check postconditions for step 1 and preconditions for step 2
//		assertEquals("Cashier should have 1 market bill. It doesn't.", cashier.marketBills.size(), 1);
//		assertTrue("Cashier should have 0 checks in it. It doesn't.", cashier.getPendingChecks().isEmpty());
//		assertTrue("Cashier should have 0 customers with tabs. It doesn't.", cashier.getTabCustomers().isEmpty());
//		assertEquals("Cashier should have 10000 balance. It doesn't.", 10000, Math.round(cashier.balance));
//
//		// Step 2 of the test, run the scheduler
//		assertTrue("Cashier's scheduler should have returned true (process bill from first market), but didn't.",
//				cashier.runScheduler());
//		assertTrue("Cashier should have 0 checks in it. It doesn't.", cashier.getPendingChecks().isEmpty());
//		assertTrue("Cashier should have 0 market bills in it. It doesn't.", cashier.marketBills.isEmpty());
//		assertTrue("Market1 should have logged \"Received payment of 10\" but didn't. His log reads instead: " 
//				+ market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Received payment of 10"));
//		assertTrue("Cashier should have 0 customers with tabs. It doesn't.", cashier.getTabCustomers().isEmpty());
//		assertEquals("Cashier should have 9990 balance. It doesn't.", 9990, Math.round(cashier.balance));
//
//		// Step 3 of the test, get bill from market2
//		cashier.msgHereIsMarketBill(market2, bill2);
//
//		// Check postconditions for step 3 and preconditions for step 4
//		assertEquals("Cashier should have 1 market bills. It doesn't.", cashier.marketBills.size(), 1);
//		assertTrue("Cashier should have 0 checks in it. It doesn't.", cashier.getPendingChecks().isEmpty());
//		assertTrue("Cashier should have 0 customers with tabs. It doesn't.", cashier.getTabCustomers().isEmpty());
//		assertEquals("Cashier should have 9990 balance. It doesn't.", 9990, Math.round(cashier.balance));
//
//		// Step 4 of the test, run the scheduler
//		assertTrue("Cashier's scheduler should have returned true (process bill from second market), but didn't.",
//				cashier.runScheduler());
//		assertTrue("Cashier should have 0 checks in it. It doesn't.", cashier.getPendingChecks().isEmpty());
//		assertTrue("Cashier should have 0 market bills in it. It doesn't.", cashier.marketBills.isEmpty());
//		assertTrue("Market2 should have logged \"Recieved payment of 20\" but didn't. His log reads instead: " 
//				+ market2.log.getLastLoggedEvent().toString(), market2.log.containsString("Received payment of 20"));
//		assertTrue("Cashier should have 0 customers with tabs. It doesn't.", cashier.getTabCustomers().isEmpty());
//		assertEquals("Cashier should have 9970 balance. It doesn't.", 9970, Math.round(cashier.balance));
//	}
	
	public void testFiveNonNormativeCustomerScenario() { // Tests when customer doesn't have enough money for the first time
		cashier.setMenu(menu);
		// Check to make sure cashier's initial state is okay
		assertEquals("Cashier should have 0 checks. It doesn't.", cashier.getPendingChecks().size(), 0);
		assertTrue("Cashier should have 0 customers with tabs. It doesn't.", cashier.getTabCustomers().isEmpty());
		assertFalse("Cashier should have a menu of items. It doesn't.", cashier.getMenu().isEmpty());
		assertEquals("Cashier should have 10000 balance. It doesn't.", 10000, Math.round(cashier.getBalance()));
//		assertTrue("Cashier should have 0 market bills. It doesn't.", cashier.marketBills.isEmpty());

		// Step 1 of the test, ask for bill
		cashier.msgComputeBill(waiter, customer, "chicken"); //send the message from a waiter
		check = cashier.getPendingChecks().get(0);

		// Check postconditions for step 1 and preconditions for step 2
		assertEquals("Cashier should have 1 check in it. It doesn't.", cashier.getPendingChecks().size(), 1);
		assertTrue("Cashier's scheduler should have returned true (process check from waiter), but didn't.",
				cashier.runScheduler());
		assertTrue("Cashier should have 0 checks in it. It doesn't.", cashier.getPendingChecks().isEmpty());
		assertTrue("Waiter should have logged \"Received check from cashier.\" but didn't. His log reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received check from cashier."));
		assertTrue("Customer should have logged \"Received check from waiter.\" but didn't. His log reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), customer.log.containsString("Received check from waiter."));
		assertTrue("Cashier should have 0 customers with tabs. It doesn't.", cashier.getTabCustomers().isEmpty());
		assertEquals("Cashier should have 10000 balance. It doesn't.", 10000, Math.round(cashier.getBalance()));
//		assertTrue("Cashier should have 0 market bills. It doesn't.", cashier.marketBills.isEmpty());

		// Step 2 of the test, give payment to cashier
		cashier.msgHereIsPayment(check, 2); // Sent from customer

		//check postconditions for step 2
		assertEquals("Cashier should have 1 check in it. It doesn't.", 1, cashier.getPendingChecks().size());
		assertEquals("Check status should be atCustomer. It isn't.", check.status, RestaurantZhangCheck.CheckStatus.atCustomer);
		assertEquals("Check should have 2 in payment. It doesn't.", Math.round(check.payment), 2);
		assertTrue("Cashier's scheduler should have returned true (process check from customer), but didn't.",
				cashier.runScheduler());
		assertTrue("Cashier's list of checks should be empty. It isn't.", cashier.getPendingChecks().isEmpty());
		assertEquals("Cashier should have 1 customers with tabs. It doesn't.", cashier.getTabCustomers().size(), 1);
		assertTrue("Customer log should have read \"Got tab 7 from cashier.\" It didn't.", customer.log.containsString("Got tab 7 from cashier."));
		assertEquals("Cashier should have 10002 balance. It doesn't.", 10002, Math.round(cashier.getBalance()));
//		assertTrue("Cashier should have 0 market bills. It doesn't.", cashier.marketBills.isEmpty());
	}
	
//	public void testSixNormalCustomerMarketScenario() { // Tests when customer and market successfully pay and are paid, respectively
//		cashier.setMenu(menu);
//		// Check to make sure cashier's initial state is okay
//		assertEquals("Cashier should have 0 checks. It doesn't.", cashier.getPendingChecks().size(), 0);
//		assertTrue("Cashier should have 0 customers with tabs. It doesn't.", cashier.getTabCustomers().isEmpty());
//		assertFalse("Cashier should have a menu of items. It doesn't.", cashier.getMenu().isEmpty());
//		assertEquals("Cashier should have 10000 balance. It doesn't.", 10000, Math.round(cashier.balance));
//		assertTrue("Cashier should have 0 market bills. It doesn't.", cashier.marketBills.isEmpty());
//
//		// Step 1 of the test, ask for bill
//		cashier.msgComputeBill(waiter, customer, "chicken"); //send the message from a waiter
//		check = cashier.getPendingChecks().get(0);
//
//		// Check postconditions for step 1 and preconditions for step 2
//		assertEquals("Cashier should have 1 check in it. It doesn't.", cashier.getPendingChecks().size(), 1);
//		assertTrue("Cashier's scheduler should have returned true (process check from waiter), but didn't.",
//				cashier.runScheduler());
//		assertTrue("Cashier should have 0 checks in it. It doesn't.", cashier.getPendingChecks().isEmpty());
//		assertTrue("Waiter should have logged \"Received check from cashier.\" but didn't. His log reads instead: " 
//				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received check from cashier."));
//		assertTrue("Customer should have logged \"Received check from waiter.\" but didn't. His log reads instead: " 
//				+ waiter.log.getLastLoggedEvent().toString(), customer.log.containsString("Received check from waiter."));
//		assertTrue("Cashier should have 0 customers with tabs. It doesn't.", cashier.getTabCustomers().isEmpty());
//		assertEquals("Cashier should have 10000 balance. It doesn't.", 10000, Math.round(cashier.balance));
//		assertTrue("Cashier should have 0 market bills. It doesn't.", cashier.marketBills.isEmpty());
//		
//		// Step 2 of the test, get bill from market
//		int bill = 10;
//		cashier.msgHereIsMarketBill(market1, bill);
//
//		// Check postconditions for step 2 and preconditions for step 3
//		assertEquals("Cashier should have 1 market bill. It doesn't.", cashier.marketBills.size(), 1);
//		assertTrue("Cashier should have 0 checks in it. It doesn't.", cashier.getPendingChecks().isEmpty());
//		assertTrue("Cashier should have 0 customers with tabs. It doesn't.", cashier.getTabCustomers().isEmpty());
//		
//		// Step 3 of the test, give payment to cashier from customer
//		cashier.msgHereIsPayment(check, 20); // Sent from customer
//
//		//check postconditions for step 3 and preconditions for step 4
//		assertEquals("Cashier should have 1 check in it. It doesn't.", 1, cashier.getPendingChecks().size());
//		assertEquals("Check status should be atCustomer. It isn't.", check.status, Check.CheckStatus.atCustomer);
//		assertEquals("Check should have 20 in payment. It doesn't.", Math.round(check.payment), 20);
//		assertTrue("Cashier's scheduler should have returned true (process check from customer), but didn't.",
//				cashier.runScheduler());
//		assertTrue("Cashier's list of checks should be empty. It isn't.", cashier.getPendingChecks().isEmpty());
//		assertTrue("Cashier should have 0 customers with tabs. It doesn't.", cashier.getTabCustomers().isEmpty());
//		assertTrue("Customer log should have read \"Got change 11.01 from cashier.\" It says instead: " + customer.log.getLastLoggedEvent(), customer.log.containsString("Got change 11.01 from cashier."));
//		assertEquals("Cashier should have 10000 balance. It instead has " + cashier.balance, 10008.99, cashier.balance);
//		assertEquals("Cashier should have 1 market bills. It doesn't.", cashier.marketBills.size(), 1);
//		
//		// Step 4, run scheduler for market payment
//		assertTrue("Cashier's scheduler should have returned true (process bill from first market), but didn't.",
//				cashier.runScheduler());
//		assertTrue("Cashier should have 0 checks in it. It doesn't.", cashier.getPendingChecks().isEmpty());
//		assertTrue("Cashier should have 0 market bills in it. It doesn't.", cashier.marketBills.isEmpty());
//		assertTrue("Market1 should have logged \"Received payment of 10\" but didn't. His log reads instead: " 
//				+ market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Received payment of 10"));
//		assertTrue("Cashier should have 0 customers with tabs. It doesn't.", cashier.getTabCustomers().isEmpty());
//		assertEquals("Cashier should have 9999 balance. It doesn't.", 9999, Math.round(cashier.balance));
//	}
}