/*package city.tests;

import junit.framework.TestCase;
import city.roles.RestaurantTimmsCashierRole;
import city.roles.RestaurantTimmsCashierRole.Check;
import city.tests.mock.MockPerson;
import city.tests.mock.MockRestaurantTimmsCustomer;
import city.tests.mock.MockRestaurantTimmsWaiter;

public class RestaurantTimmsCashierTest extends TestCase {
	
	MockRestaurantTimmsCustomer customer;
	// TODO
	//	MockRestaurantTimmsMarket market0;
	//	MockRestaurantTimmsMarket market1;
	MockRestaurantTimmsWaiter waiter;
	
	MockPerson cashierPerson;
	RestaurantTimmsCashierRole cashier;

	public void setUp() throws Exception {
		super.setUp();
		customer = new MockRestaurantTimmsCustomer();
		
		cashierPerson = new MockPerson("Cashier");
		cashier = new RestaurantTimmsCashierRole(cashierPerson);
		
		// TODO
		// this.market0 = new MockMarket("MockMarket0");
		// this.market1 = new MockMarket("MockMarket1");
		
		waiter = new MockRestaurantTimmsWaiter();
	}
	
	public void testExactCustomerPayment() {
		int beginningMoney = cashier.moneyOnHand;
		
		// Preconditions
		assertEquals("Cashier should have 0 bills.", cashier.bills.size(), 0);
		assertTrue("Cashier should have at least $100 on hand.", (beginningMoney >= 100));
		assertEquals("Cashier should have not collected any money.", cashier.moneyCollected, 0);
		assertEquals("Cashier should not have any money owed.", cashier.moneyOwed, 0);
		
		// Send a message from a waiter creating a check for $10
		cashier.msgComputeCheck(waiter, customer, 10);
		
		assertEquals("Cashier should have 1 check.", 1, cashier.checks.size());
		assertEquals("Waiter's log should be empty.", 0, waiter.log.size());
		assertEquals("Check should be in queue.", Check.State.queue, cashier.checks.get(0).getState());
		assertTrue("Cashier's scheduler should return true.", cashier.runScheduler());
		assertEquals("Check should be unpaid.", Check.State.unpaid, cashier.checks.get(0).getState());
		assertEquals("Cashier should be owed $10.", 10, cashier.moneyOwed);
		assertEquals("Waiter's log should have 1 message.", 1, waiter.log.size());
		assertFalse("Cashier's scheduler should return false.", cashier.runScheduler());
		
		// Send a message from a customer to pay a check
		cashier.msgMakePayment(customer, 10);
		
		assertEquals("Customer's log should be empty.", 0, customer.log.size());
		assertTrue("Cashier's scheduler should return true.", cashier.runScheduler());
		assertEquals("Check should be paid.", Check.State.paid, cashier.checks.get(0).getState());
		assertEquals("Cashier should have collected $10.", 10, cashier.moneyCollected);
		assertEquals("Cashier should not be owed money.", 0, cashier.moneyOwed);
		assertEquals("Customer's log should have one message.", 1, customer.log.size());
		assertTrue("Customer should have recieved no change.", customer.log.containsString("Received msgPaidCashier. Change: 0"));
		assertFalse("Cashier's scheduler should return false.", cashier.runScheduler());
	}
	
	public void testCustomerPaymentWithChange() {
		// Send a message from a waiter creating a check for $10
		cashier.msgComputeCheck(waiter, customer, 10);
		
		assertTrue("Cashier's scheduler should return true.", cashier.runScheduler());
		
		// Send a message from a customer with more than enough money to pay check
		cashier.msgMakePayment(customer, 11);
		
		assertTrue("Cashier's scheduler should return true.", cashier.runScheduler());
		assertEquals("Check should be paid.", Check.State.paid, cashier.checks.get(0).getState());
		assertEquals("Cashier should have collected $10.", 10, cashier.moneyCollected);
		assertEquals("Cashier should not be owed money.", 0, cashier.moneyOwed);
		assertTrue("Customer should have recieved $1 change.", customer.log.containsString("Received msgPaidCashier. Change: 1"));
		assertFalse("Cashier's scheduler should return false.", cashier.runScheduler());
	}
	
	public void testCustomerNonPayment() {		
		// Send a message from a waiter creating a check for $10
		cashier.msgComputeCheck(waiter, customer, 10);
		
		assertTrue("Cashier's scheduler should return true.", cashier.runScheduler());
		
		// Send a message from a customer without enough money to pay check
		cashier.msgMakePayment(customer, 5);
		
		assertTrue("Cashier's scheduler should return true.", cashier.runScheduler());
		assertEquals("Check should be unpaid.", Check.State.unpaid, cashier.checks.get(0).getState());
		assertEquals("Cashier should not have collected money.", 0, cashier.moneyCollected);
		assertEquals("Cashier should be owed $10.", 10, cashier.moneyOwed);
		assertTrue("Customer should have recieved full ($5) change.", customer.log.containsString("Received msgPaidCashier. Change: 5"));
		assertFalse("Cashier's scheduler should return false.", cashier.runScheduler());
		
		// Send a message from a waiter adding another $10 to the check
		cashier.msgComputeCheck(waiter, customer, 10);
		
		assertEquals("Cashier should have 1 check.", 1, cashier.checks.size());
		assertEquals("Check should be in queue.", Check.State.queue, cashier.checks.get(0).getState());
		assertTrue("Cashier's scheduler should return true.", cashier.runScheduler());
		assertEquals("Check should be unpaid.", Check.State.unpaid, cashier.checks.get(0).getState());
		assertEquals("Check should be for $20.", 20, cashier.checks.get(0).getAmount());
		assertEquals("Cashier should be owed $20.", 20, cashier.moneyOwed);
		assertFalse("Cashier's scheduler should return false.", cashier.runScheduler());
		
		// Send a message from a customer with enough money to pay the check
		cashier.msgMakePayment(customer, 20);

		assertTrue("Cashier's scheduler should return true.", cashier.runScheduler());
		assertEquals("Check should be paid.", Check.State.paid, cashier.checks.get(0).getState());
		assertEquals("Cashier should have collected $20.", 20, cashier.moneyCollected);
		assertEquals("Cashier should not be owed money.", 0, cashier.moneyOwed);
		assertTrue("Customer should have recieved no change.", customer.log.containsString("Received msgPaidCashier. Change: 0"));
		assertFalse("Cashier's scheduler should return false.", cashier.runScheduler());
	}
	
// TODO
//	public void testSingleMarketSinglePayment() {
//		int beginningMoney = cashier.moneyOnHand;
//		
//		// Preconditions
//		assertEquals("Cashier should have 0 bills.", cashier.bills.size(), 0);
//		assertTrue("Cashier should have at least $100 on hand.", (beginningMoney >= 100));
//		assertEquals("Cashier should have not collected any money.", cashier.moneyCollected, 0);
//		assertEquals("Cashier should not have any money owed.", cashier.moneyOwed, 0);
//		
//		// Send a message from a market creating a bill for $10
//		cashier.msgPayMarket(market0, 10);
//		
//		assertEquals("Cashier should have 1 bill.", 1, cashier.bills.size());
//		assertEquals("Markets's log should be empty.", 0, market0.log.size());
//		assertEquals("Bill should be in queue.", Bill.State.queue, cashier.bills.get(0).getState());
//		assertEquals("Bill should be for $10.", 10, cashier.bills.get(0).getAmount());
//		assertTrue("Cashier's scheduler should return true.", cashier.runScheduler());
//		assertEquals("Bill should be paid.", Bill.State.paid, cashier.bills.get(0).getState());
//		assertEquals("Bill should be for $0.", 0, cashier.bills.get(0).getAmount());
//		assertEquals("Cashier should have $10 less on hand.", (beginningMoney - 10), cashier.moneyOnHand);
//		assertEquals("Market's log should have 1 message.", 1, market0.log.size());
//		assertFalse("Cashier's scheduler should return false.", cashier.runScheduler());	
//	}
	
// TODO
//	public void testSingleMarketMultiplePayment() {
//		int beginningMoney = cashier.moneyOnHand;
//		
//		// Send two messages from a market creating a single bill for $20 total
//		cashier.msgPayMarket(market0, 10);
//		cashier.msgPayMarket(market0, 10);
//		
//		assertEquals("Cashier should have 1 bill.", 1, cashier.bills.size());
//		assertEquals("Markets's log should be empty.", 0, market0.log.size());
//		assertEquals("Bill should be in queue.", Bill.State.queue, cashier.bills.get(0).getState());
//		assertEquals("Bill should be for $20.", 20, cashier.bills.get(0).getAmount());
//		assertTrue("Cashier's scheduler should return true.", cashier.runScheduler());
//		assertEquals("Bill should be paid.", Bill.State.paid, cashier.bills.get(0).getState());
//		assertEquals("Bill should be for $0.", 0, cashier.bills.get(0).getAmount());
//		assertEquals("Cashier should have $20 less on hand.", (beginningMoney - 20), cashier.moneyOnHand);
//		assertEquals("Market's log should have 1 message.", 1, market0.log.size());
//		assertFalse("Cashier's scheduler should return false.", cashier.runScheduler());	
//	}
	
// TODO
//	public void testMultipleMarketPayment() {
//		int beginningMoney = cashier.moneyOnHand;
//		
//		// Send two messages from two markets creating two bills, each for $10
//		cashier.msgPayMarket(market0, 10);
//		cashier.msgPayMarket(market1, 10);
//		
//		assertEquals("Cashier should have 2 bills.", 2, cashier.bills.size());
//		for (Bill bill : cashier.bills) {
//			assertEquals("Bill should be in queue.", Bill.State.queue, bill.getState());
//			assertTrue("Cashier's scheduler should return true.", cashier.runScheduler());
//		}
//		for (Bill bill : cashier.bills) {
//			assertEquals("Bill should be paid.", Bill.State.paid, bill.getState());
//			assertEquals("Bill should be for $0.", 0, bill.getAmount());
//		}
//		assertEquals("Cashier should have $20 less on hand.", (beginningMoney - 20), cashier.moneyOnHand);
//		assertEquals("Market0's log should have 1 message.", 1, market0.log.size());
//		assertEquals("Market1's log should have 1 message.", 1, market1.log.size());
//		assertFalse("Cashier's scheduler should return false.", cashier.runScheduler());
//	}
	
// TODO
//	public void testMarketFailedPayment() {
//		int beginningMoney = cashier.moneyOnHand;
//		
//		// Send a message from a market creating a bill for $501. The maximum amount of money a cashier can start with is $500.
//		cashier.msgPayMarket(market0, 501);
//		
//		assertTrue("Cashier's scheduler should return true.", cashier.runScheduler());
//		assertEquals("Bill should be unpaid.", Bill.State.unpaid, cashier.bills.get(0).getState());
//		assertEquals("Bill should be for $501.", 501, cashier.bills.get(0).getAmount());
//		assertFalse("Cashier's scheduler should return false.", cashier.runScheduler());
//		
//		// Send another message adding to the bill. This puts it back in the queue for pay attempts.
//		cashier.msgPayMarket(market0, 1);
//		
//		// Give cashier enough money to pay the previous bills
//		cashier.moneyOnHand = (cashier.moneyOnHand + 502);
//		
//		assertEquals("Bill should be in queue.", Bill.State.queue, cashier.bills.get(0).getState());
//		assertEquals("Bill should be for $502.", 502, cashier.bills.get(0).getAmount());
//		assertTrue("Cashier's scheduler should return true.", cashier.runScheduler());
//		assertEquals("Bill should be paid.", Bill.State.paid, cashier.bills.get(0).getState());
//		assertEquals("Bill should be for $0.", 0, cashier.bills.get(0).getAmount());
//		assertEquals("Cashier should have the same amount of money it started with.", beginningMoney, cashier.moneyOnHand);
//		assertEquals("Market's log should have 1 message.", 1, market0.log.size());
//		assertFalse("Cashier's scheduler should return false.", cashier.runScheduler());	
//	}
	
}
*/