package city.tests.roles;

import junit.framework.TestCase;
import city.buildings.RestaurantTimmsBuilding;
import city.buildings.RestaurantTimmsBuilding.Check;
import city.roles.RestaurantTimmsCashierRole;
import city.roles.interfaces.RestaurantTimmsCashier;
import city.tests.agents.mocks.MockPerson;
import city.tests.animations.mocks.MockRestaurantTimmsAnimatedCashier;
import city.tests.roles.mocks.MockBankCustomer;
import city.tests.roles.mocks.MockRestaurantTimmsCashier;
import city.tests.roles.mocks.MockRestaurantTimmsCustomer;
import city.tests.roles.mocks.MockRestaurantTimmsWaiter;

public class RestaurantTimmsCashierTest extends TestCase {

	MockRestaurantTimmsCustomer customer;
	MockRestaurantTimmsWaiter waiter;
	MockRestaurantTimmsCashier cashier2;
	MockBankCustomer bankCustomer;
	
	RestaurantTimmsBuilding rtb;
	
	MockPerson cashierPerson;
	RestaurantTimmsCashierRole cashier;
	MockRestaurantTimmsAnimatedCashier cashierAnimation;

	public void setUp() throws Exception {
		super.setUp();
		
		customer = new MockRestaurantTimmsCustomer();
		waiter = new MockRestaurantTimmsWaiter();
		cashier2 = new MockRestaurantTimmsCashier();
		bankCustomer = new MockBankCustomer();

		rtb = new RestaurantTimmsBuilding("RestaurantTimms", null, null);
		rtb.setCash(RestaurantTimmsCashier.MIN_CAPITAL);
		rtb.setBankCustomer(bankCustomer);
		
		cashierPerson = new MockPerson("Cashier");
		cashier = new RestaurantTimmsCashierRole(rtb, 0, 23);
		cashierAnimation = new MockRestaurantTimmsAnimatedCashier();
		cashier.setPerson(cashierPerson);
		cashier.setAnimation(cashierAnimation);
		rtb.setCashier(cashier);
	}
	
	public void testExactCustomerPayment() {
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		boolean outcome;
		
		// More setup
		rtb.setCash(RestaurantTimmsCashier.MIN_CAPITAL);
		int beginningMoney = cashier.getWorkplace(RestaurantTimmsBuilding.class).getCash(); 
		
		// Preconditions
		assertTrue("Cashier should have at least $100 on hand.", (beginningMoney >= 100));
		assertEquals("Cashier should have not collected any money.", cashier.getMoneyCollected(), 0);
		assertEquals("Cashier should not have any money owed.", cashier.getMoneyOwed(), 0);
		
		// Send a message from a waiter creating a check for $10
		cashier.msgComputeCheck(waiter, customer, 10);
		
		assertEquals("Cashier should have 1 check.", 1, rtb.getChecks().size());
		assertEquals("Waiter's log should be empty.", 0, waiter.log.size());
		assertEquals("Check should be in queue.", Check.State.queue, rtb.getChecks().get(0).getState());
		
		// Run scheduler
		// - Cashier computes the check
		outcome = cashier.runScheduler();
		
		assertTrue("Cashier's scheduler should return true.", outcome);
		assertEquals("Check should be unpaid.", Check.State.unpaid, rtb.getChecks().get(0).getState());
		assertEquals("Cashier should be owed $10.", 10, cashier.getMoneyOwed());
		assertEquals("Waiter's log should have 1 message.", 1, waiter.log.size());

		// Run Scheduler
		// - Allow marketCustomerDeliveryPaymentRole to become inactive (it's never had anything to do)
		// - Gratuitous run to test the scheduler returns false
		outcome = cashier.runScheduler();
		outcome = cashier.runScheduler();
		
		assertFalse("Cashier's scheduler should return false.", outcome);
		
		// Send a message from a customer to pay a check
		cashier.msgMakePayment(customer, 10);
		
		assertEquals("Customer's log should be empty.", 0, customer.log.size());
		
		// Run scheduler
		// - Cashier accepts payment
		outcome = cashier.runScheduler();
		
		assertTrue("Cashier's scheduler should return true.", outcome);
		assertEquals("Check should be paid.", Check.State.paid, rtb.getChecks().get(0).getState());
		assertEquals("Cashier should have collected $10.", 10, cashier.getMoneyCollected());
		assertEquals("Cashier should not be owed money.", 0, cashier.getMoneyOwed());
		assertEquals("Customer's log should have one message.", 1, customer.log.size());
		assertTrue("Customer should have recieved no change.", customer.log.containsString("Received msgPaidCashier. Change: 0"));
		
		// Run Scheduler
		// - Gratuitous run to test the scheduler returns false
		outcome = cashier.runScheduler();
		
		assertFalse("Cashier's scheduler should return false.", outcome);
	}
	
	public void testCustomerPaymentWithChange() {
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		boolean outcome;
		
		// Send a message from a waiter creating a check for $10
		cashier.msgComputeCheck(waiter, customer, 10);
		
		// Run scheduler
		// - Cashier computes the check
		outcome = cashier.runScheduler();
		
		assertTrue("Cashier's scheduler should return true.", outcome);
		
		// Send a message from a customer with more than enough money to pay check
		cashier.msgMakePayment(customer, 11);
		
		// Run Scheduler
		// - Cashier accepts payment
		outcome = cashier.runScheduler();
		
		assertTrue("Cashier's scheduler should return true.", outcome);	
		assertEquals("Check should be paid.", Check.State.paid, rtb.getChecks().get(0).getState());
		assertEquals("Cashier should have collected $10.", 10, cashier.getMoneyCollected());
		assertEquals("Cashier should not be owed money.", 0, cashier.getMoneyOwed());
		assertTrue("Customer should have recieved $1 change.", customer.log.containsString("Received msgPaidCashier. Change: 1"));
		
		// Run Scheduler
		// - Allow marketCustomerDeliveryPaymentRole to become inactive (it's never had anything to do)
		// - Gratuitous run to test the scheduler returns false
		outcome = cashier.runScheduler();
		outcome = cashier.runScheduler();
		
		assertFalse("Cashier's scheduler should return false.", outcome);
	}
	
	public void testCustomerNonPayment() {
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		boolean outcome;
		
		// Send a message from a waiter creating a check for $10
		cashier.msgComputeCheck(waiter, customer, 10);
		
		// Run scheduler
		// - Cashier computes the check
		outcome = cashier.runScheduler();
		
		assertTrue("Cashier's scheduler should return true.", outcome);
		
		// Send a message from a customer without enough money to pay check
		cashier.msgMakePayment(customer, 5);
		
		// Run Scheduler
		// - Cashier accepts payment
		outcome = cashier.runScheduler();
		
		assertTrue("Cashier's scheduler should return true.", outcome);		
		assertEquals("Check should be unpaid.", Check.State.unpaid, rtb.getChecks().get(0).getState());
		assertEquals("Cashier should not have collected money.", 0, cashier.getMoneyCollected());
		assertEquals("Cashier should be owed $10.", 10, cashier.getMoneyOwed());
		assertTrue("Customer should have recieved full ($5) change.", customer.log.containsString("Received msgPaidCashier. Change: 5"));
		
		// Run Scheduler
		// - Allow marketCustomerDeliveryPaymentRole to become inactive (it's never had anything to do)
		// - Gratuitous run to test the scheduler returns false
		outcome = cashier.runScheduler();
		outcome = cashier.runScheduler();
		
		assertFalse("Cashier's scheduler should return false.", outcome);
		
		// Send a message from a waiter adding another $10 to the check
		cashier.msgComputeCheck(waiter, customer, 10);
		
		assertEquals("Cashier should have 1 check.", 1, rtb.getChecks().size());
		assertEquals("Check should be in queue.", Check.State.queue, rtb.getChecks().get(0).getState());
		assertTrue("Cashier's scheduler should return true.", cashier.runScheduler());
		assertEquals("Check should be unpaid.", Check.State.unpaid, rtb.getChecks().get(0).getState());
		assertEquals("Check should be for $20.", 20, rtb.getChecks().get(0).getAmount());
		assertEquals("Cashier should be owed $20.", 20, cashier.getMoneyOwed());
		assertFalse("Cashier's scheduler should return false.", cashier.runScheduler());
		
		// Send a message from a customer with enough money to pay the check
		cashier.msgMakePayment(customer, 20);

		assertTrue("Cashier's scheduler should return true.", cashier.runScheduler());
		assertEquals("Check should be paid.", Check.State.paid, rtb.getChecks().get(0).getState());
		assertEquals("Cashier should have collected $20.", 20, cashier.getMoneyCollected());
		assertEquals("Cashier should not be owed money.", 0, cashier.getMoneyOwed());
		assertTrue("Customer should have recieved no change.", customer.log.containsString("Received msgPaidCashier. Change: 0"));
		assertFalse("Cashier's scheduler should return false.", cashier.runScheduler());
	}
	
	public void testCashierLeaving() {
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		boolean outcome;
		
		// More set-up
		cashier.setActive();
		
		// Preconditions
		assertTrue("Cashier should be active", cashier.getActive());
		assertEquals("Building should have this cashier", cashier, rtb.getCashier());
		
		// Run Scheduler
		// - Allow marketCustomerDeliveryPaymentRole to become inactive (it's never had anything to do)
		// - Gratuitous run to test the scheduler returns false
		outcome = cashier.runScheduler();
		outcome = cashier.runScheduler();
		
		assertFalse("Cashier's scheduler should return false.", outcome);
		assertTrue("Cashier should be active", cashier.getActive());
		
		cashier.setInactive();
	
		// Run Scheduler
		// - Allow cashier to make a decision
		outcome = cashier.runScheduler();
		
		assertFalse("Cashier's scheduler should return false.", outcome);
		assertTrue("Cashier should still be active.", cashier.getActive());
		
		rtb.getBankCustomer().setInactive();
		rtb.setCashier(cashier2);
		
		// Run Scheduler
		// - Allow cashier to make a decision
		outcome = cashier.runScheduler();
		
		assertFalse("Cashier's scheduler should return false.", outcome);
		assertFalse("Cashier should not be active.", cashier.getActive());
	}
	
	public void testBankDeposit() {
		assertTrue("Placeholder", true);
	}
	
}
