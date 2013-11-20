package city.tests;

import java.util.HashMap;
import java.util.Map;

import city.buildings.MarketBuilding;
import city.roles.MarketCashierRole;
import city.roles.MarketCashierRole.TransactionState;
import city.tests.mock.MockMarketCustomer;
import city.tests.mock.MockMarketCustomerDelivery;
import city.tests.mock.MockMarketDeliveryPerson;
import city.tests.mock.MockMarketEmployee;
import city.tests.mock.MockMarketManager;
import junit.framework.TestCase;

public class MarketCashierTest extends TestCase {
	
	MarketBuilding market;
	MockMarketCustomer customer;
	MockMarketCustomerDelivery customerDelivery;
	MockMarketDeliveryPerson deliveryPerson;
	MockMarketEmployee employee;
	MockMarketManager manager;
	MarketCashierRole cashier;
	
	Map<String, Integer> order;
	Map<String, Integer> collectedItemsAll;
	Map<String, Integer> collectedItemsPartial;
	
	public void setUp() throws Exception {
		super.setUp();
		market = new MarketBuilding("Market1");
		
		employee = new MockMarketEmployee();
		employee.market = market;
		
		manager = new MockMarketManager();
		manager.market = market;

		customer = new MockMarketCustomer();
		customer.market = market;
		
		customerDelivery = new MockMarketCustomerDelivery();
		customerDelivery.market = market;
		
		cashier = new MarketCashierRole();
		cashier.market = market;
		
		order = new HashMap<String, Integer>();
		order.put("Steak", 5);
		order.put("Chicken", 5);
		order.put("Salad", 5);
		order.put("Pizza", 5);
		
		collectedItemsAll = new HashMap<String, Integer>();
		collectedItemsAll.put("Steak", 5);
		collectedItemsAll.put("Chicken", 5);
		collectedItemsAll.put("Salad", 5);
		collectedItemsAll.put("Pizza", 5);
		
		collectedItemsPartial = new HashMap<String, Integer>();
		collectedItemsPartial.put("Steak", 3);
		collectedItemsPartial.put("Chicken", 3);
		collectedItemsPartial.put("Salad", 3);
		collectedItemsPartial.put("Pizza", 3);
		
//		prices.put("Steak", (16.00)/2);
//		prices.put("Chicken", (12.00)/2);
//		prices.put("Salad", (6.00)/2);
//		prices.put("Pizza", (10.00)/2);		
	}
	
	public void testNormCustomerScenario() {
		assertEquals("Employee should have an empty log.", employee.log.size(), 0);
		assertEquals("Manager should have an empty log.", manager.log.size(), 0);
		assertEquals("Customer should have an empty log.", customer.log.size(), 0);
		assertEquals("CustomerDelivery should have an empty log.", customer.log.size(), 0);
		assertEquals("Cashier should have an empty log.", cashier.log.size(), 0);
		assertEquals("Cashier should have 0 transactions.", cashier.transactions.size(), 0);
				
		cashier.msgComputeBill(employee, customer, order, collectedItemsAll);
		assertEquals("Cashier log should have 1 entry.", cashier.log.size(), 1);
		assertTrue("Cashier log should have \"Cashier received msgComputeBill\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgComputeBill"));
		assertEquals("Cashier should have 1 transaction.", cashier.transactions.size(), 1);
		assertTrue("Cashier transactions should contain a transaction with state == Pending.",
		cashier.transactions.get(0).s == TransactionState.Pending);
		
		cashier.runScheduler();
		assertTrue("Cashier transactions should contain a transaction with state == Calculating.",
		cashier.transactions.get(0).s == TransactionState.Calculating);
		assertEquals("Cashier transactions should contain a transaction with bill for 110.00", cashier.transactions.get(0).bill, 110.00);
		assertEquals("Customer log should have 1 entry.", customer.log.size(), 1);
		assertTrue("Customer log should have \"Customer received msgHereIsOrderandBill\". The last event logged is " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Customer received msgHereIsOrderandBill"));

		cashier.msgHereIsPayment(customer, 110.00);
		assertEquals("Cashier log should have 2 entries.", cashier.log.size(), 2);
		assertTrue("Cashier log should have \"Cashier received msgHereIsPayment\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgHereIsPayment"));
		assertEquals("Cashier payment variable should be 110.00. It's " + cashier.transactions.get(0).payment + "instead", cashier.transactions.get(0).payment, 110.00);
		assertTrue("Cashier transactions should contain a transaction with state == ReceivedPayment.",
		cashier.transactions.get(0).s == TransactionState.ReceivedPayment);	
		
		cashier.runScheduler();
		assertEquals("Market money should be 1100.00. It's " + market.money + "instead", market.money, 1100.00);
		assertEquals("Customer log should have 2 entries.", customer.log.size(), 2);
		assertTrue("Customer log should have \"Customer received msgPaymentReceived from cashier\". The last event logged is " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Customer received msgPaymentReceived from cashier"));
		assertEquals("Cashier should have 0 transactions.", cashier.transactions.size(), 0);		
	}
	
	public void testNormCustomerDeliveryScenario() {
//		assertEquals("Employee should have an empty log.", employee.log.size(), 0);
//		assertEquals("Manager should have an empty log.", manager.log.size(), 0);
//		assertEquals("Customer should have an empty log.", customer.log.size(), 0);
//		assertEquals("CustomerDelivery should have an empty log.", customer.log.size(), 0);
//		assertEquals("Cashier should have an empty log.", cashier.log.size(), 0);
//		assertEquals("Cashier should have 0 transactions.", cashier.transactions.size(), 0);
//		
//		cashier.msgNewDeliveryPerson(deliveryPerson);
//		assertTrue("Cashier log should have \"Cashier received msgComputeBill\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgComputeBill"));
//		
//		cashier.msgComputeBill(employee, customerDelivery, order, collectedItemsAll);
//		assertEquals("Customer log should have 1 entry.", customer.log.size(), 1);
//		assertTrue("Cashier log should have \"Cashier received msgComputeBill\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgComputeBill"));
//		assertEquals("Cashier should have 1 transaction.", cashier.transactions.size(), 1);
//		assertTrue("Cashier transactions should contain a transaction with state == Pending.",
//		cashier.transactions.get(0).s == TransactionState.Pending);
//		
//		cashier.runScheduler();
//		assertTrue("Cashier transactions should contain a transaction with state == Calculating.",
//		cashier.transactions.get(0).s == TransactionState.Calculating);
//		assertEquals("Cashier transactions should contain a transaction with bill for 110.00", cashier.transactions.get(0).bill, 110.00);
//		assertEquals("Customer log should have 1 entry.", customerDelivery.log.size(), 1);
//		assertTrue("Customer log should have \"Customer received msgHereIsBill\". The last event logged is " + customer.log.getLastLoggedEvent().toString(), customerDelivery.log.containsString("Customer received msgHereIsBill"));
//
//		cashier.msgHereIsPayment(customerDelivery, 110.00);
//		assertEquals("Cashier log should have 2 entries.", cashier.log.size(), 2);
//		assertTrue("Cashier log should have \"Cashier received msgHereIsPayment\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgHereIsPayment"));
//		assertEquals("Cashier payment variable should be 110.00. It's " + cashier.transactions.get(0).payment + "instead", cashier.transactions.get(0).payment, 110.00);
//		assertTrue("Cashier transactions should contain a transaction with state == ReceivedPayment.",
//		cashier.transactions.get(0).s == TransactionState.ReceivedPayment);	
//		
//		cashier.runScheduler();
//		assertEquals("Market money should be 1100.00. It's " + market.money + "instead", market.money, 1100.00);
//		assertEquals("Customer log should have 2 entries.", customer.log.size(), 2);
//		assertTrue("Customer log should have \"Customer received msgPaymentReceived from cashier\". The last event logged is " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Customer received msgPaymentReceived from cashier"));
//		assertEquals("Cashier should have 0 transactions.", cashier.transactions.size(), 0);		
	}
	
	public void testNonNormCustomerCollectedItemsPartialScenario() {
		
	}
	
	public void testNonNormCustomerNotEnoughMoneyScenario() {
		
	}
}
