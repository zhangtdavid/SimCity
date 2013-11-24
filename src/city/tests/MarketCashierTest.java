package city.tests;

import java.util.HashMap;
import java.util.Map;

import city.buildings.MarketBuilding;
import city.roles.MarketCashierRole;
import city.roles.MarketCashierRole.TransactionState;
import city.tests.mock.MockMarketCustomer;
import city.tests.mock.MockMarketCustomerDelivery;
import city.tests.mock.MockMarketCustomerDeliveryPayment;
import city.tests.mock.MockMarketDeliveryPerson;
import city.tests.mock.MockMarketEmployee;
import city.tests.mock.MockMarketManager;
import city.tests.mock.MockPerson;
import junit.framework.TestCase;

public class MarketCashierTest extends TestCase {
	
	MarketBuilding market;
	
	MockPerson customerPerson;
	MockMarketCustomer customer;
	
	MockPerson customerDeliveryPerson;
	MockMarketCustomerDelivery customerDelivery;

	MockPerson customerDeliveryPaymentPerson;
	MockMarketCustomerDeliveryPayment customerDeliveryPayment;
	
	MockPerson deliveryPersonPerson;
	MockMarketDeliveryPerson deliveryPerson;
	
	MockPerson employeePerson;
	MockMarketEmployee employee;
	
	MockPerson managerPerson;
	MockMarketManager manager;
	
	MockPerson cashierPerson;
	MarketCashierRole cashier;
	
	Map<String, Integer> order;
	Map<String, Integer> collectedItemsAll;
	Map<String, Integer> collectedItemsPartial;
	
	public void setUp() throws Exception {
		super.setUp();
		market = new MarketBuilding("Market1");
		
		customerPerson = new MockPerson("Customer"); 
		customer = new MockMarketCustomer();
//		customerPerson.setOccupation(customer); // TODO Why does this not work? Issues with role, mock role inheritance stuff
		customer.person = customerPerson;
		customer.market = market;

		customerDeliveryPerson = new MockPerson("CustomerDelivery"); 
		customerDelivery = new MockMarketCustomerDelivery();
		customerDelivery.person = customerDeliveryPerson;
		customerDelivery.market = market;
		
		customerDeliveryPaymentPerson = new MockPerson("CustomerDeliveryPayment"); 
		customerDeliveryPayment = new MockMarketCustomerDeliveryPayment();
		customerDeliveryPayment.person = customerDeliveryPaymentPerson;
		customerDeliveryPayment.market = market;
		
		deliveryPersonPerson = new MockPerson("DeliveryPerson"); 
		deliveryPerson = new MockMarketDeliveryPerson();
		deliveryPerson.person = deliveryPersonPerson;
		deliveryPerson.market = market;
		
		employeePerson = new MockPerson("Employee"); 
		employee = new MockMarketEmployee();
		employee.person = employeePerson;
		employee.market = market;
		
		managerPerson = new MockPerson("Manager"); 
		manager = new MockMarketManager();
		manager.person = managerPerson;
		manager.market = market;
		
		cashierPerson = new MockPerson("Cashier"); 
		cashier = new MarketCashierRole();
		cashier.person = cashierPerson;
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
		
//		prices.put("Steak", (16/2);
//		prices.put("Chicken", (12/2);
//		prices.put("Salad", (6/2);
//		prices.put("Pizza", (10/2);		
	}
	
	public void testNormCustomerScenario() {
		assertEquals("Employee should have an empty log.", employee.log.size(), 0);
		assertEquals("Manager should have an empty log.", manager.log.size(), 0);
		assertEquals("Customer should have an empty log.", customer.log.size(), 0);
		assertEquals("CustomerDelivery should have an empty log.", customerDelivery.log.size(), 0);
		assertEquals("CustomerDeliveryPayment should have an empty log.", customerDeliveryPayment.log.size(), 0);
		assertEquals("Cashier should have an empty log.", cashier.log.size(), 0);
		assertEquals("Cashier should have 0 transactions.", cashier.transactions.size(), 0);
		assertEquals("Market money should be 1000. It's " + market.money + "instead", market.money, 1000);
		
//		cashier.msgComputeBill(employee, customer, order, collectedItemsAll);
		assertEquals("Cashier log should have 1 entry.", cashier.log.size(), 1);
		assertTrue("Cashier log should have \"Cashier received msgComputeBill\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgComputeBill"));
		assertEquals("Cashier should have 1 transaction.", cashier.transactions.size(), 1);
		assertTrue("Cashier transactions should contain a transaction with state == Pending.", cashier.transactions.get(0).s == TransactionState.Pending);
		
		cashier.runScheduler();
		assertTrue("Cashier transactions should contain a transaction with state == Calculating.", cashier.transactions.get(0).s == TransactionState.Calculating);
		assertEquals("Cashier transactions should contain a transaction with bill for 110", cashier.transactions.get(0).bill, 110);
		assertEquals("Customer log should have 1 entry.", customer.log.size(), 1);
		assertTrue("Customer log should have \"Customer received msgHereIsOrderandBill\". The last event logged is " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Customer received msgHereIsOrderandBill"));

		cashier.msgHereIsPayment(customer, 110);
		assertEquals("Cashier log should have 2 entries.", cashier.log.size(), 2);
		assertTrue("Cashier log should have \"Cashier received msgHereIsPayment\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgHereIsPayment"));
		assertEquals("Cashier payment variable should be 110.00. It's " + cashier.transactions.get(0).payment + "instead", cashier.transactions.get(0).payment, 110);
		assertTrue("Cashier transactions should contain a transaction with state == ReceivedPayment.", cashier.transactions.get(0).s == TransactionState.ReceivedPayment);	
		
		cashier.runScheduler();
		assertEquals("Customer log should have 2 entries.", customer.log.size(), 2);
		assertTrue("Customer log should have \"Customer received msgPaymentReceived from cashier\". The last event logged is " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Customer received msgPaymentReceived from cashier"));
		assertEquals("Market money should be 1110.00. It's " + market.money + "instead", market.money, 1110);
		assertEquals("Cashier should have 0 transactions.", cashier.transactions.size(), 0);		
	}
	
	public void testNormCustomerDeliveryScenario() {
		assertEquals("Employee should have an empty log.", employee.log.size(), 0);
		assertEquals("Manager should have an empty log.", manager.log.size(), 0);
		assertEquals("Customer should have an empty log.", customer.log.size(), 0);
		assertEquals("CustomerDelivery should have an empty log.", customerDelivery.log.size(), 0);
		assertEquals("CustomerDeliveryPayment should have an empty log.", customerDeliveryPayment.log.size(), 0);
		assertEquals("Cashier should have an empty log.", cashier.log.size(), 0);
		assertEquals("Cashier should have 0 transactions.", cashier.transactions.size(), 0);
		assertEquals("Market money should be 1000. It's " + market.money + "instead", market.money, 1000);
		
		cashier.msgNewDeliveryPerson(deliveryPerson);
		assertEquals("Cashier log should have 1 entry.", cashier.log.size(), 1);
		assertTrue("Cashier log should have \"Cashier received msgNewDeliveryPerson\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgNewDeliveryPerson"));
		assertEquals("Cashier deliveryPeople should contain 1 Delivery Person.", cashier.deliveryPeople.size(), 1);
		assertTrue("Cashier deliveryPeople should contain a Delivery Person with available == true.", cashier.deliveryPeople.get(0).available);
		cashier.runScheduler();
		
//		cashier.msgComputeBill(employee, customerDelivery, customerDeliveryPayment, order, collectedItemsAll);
		assertEquals("Cashier log should have 2 entries.", cashier.log.size(), 2);
		assertTrue("Cashier log should have \"Cashier received msgComputeBill\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgComputeBill"));
		assertEquals("Cashier should have 1 transaction.", cashier.transactions.size(), 1);
		assertTrue("Cashier transactions should contain a transaction with state == Pending.", cashier.transactions.get(0).s == TransactionState.Pending);
				
		cashier.runScheduler();
		assertTrue("Cashier transactions should contain a transaction with state == Calculating.", cashier.transactions.get(0).s == TransactionState.Calculating);
		assertEquals("Cashier transactions should contain a transaction with bill for 110", cashier.transactions.get(0).bill, 110);
		assertEquals("CustomerDeliveryPayment log should have 1 entry.", customerDeliveryPayment.log.size(), 1);
		assertTrue("CustomerDeliveryPayment log should have \"CustomerDeliveryPayment received msgHereIsBill\". The last event logged is " + customerDeliveryPayment.log.getLastLoggedEvent().toString(), customerDeliveryPayment.log.containsString("CustomerDeliveryPayment received msgHereIsBill"));

		cashier.msgHereIsPayment(customerDeliveryPayment, 110);
		assertEquals("Cashier log should have 3 entries.", cashier.log.size(), 3);
		assertTrue("Cashier log should have \"Cashier received msgHereIsPayment\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgHereIsPayment"));
		assertEquals("Cashier payment variable should be 110. It's " + cashier.transactions.get(0).payment + "instead", cashier.transactions.get(0).payment, 110);
		assertTrue("Cashier transactions should contain a transaction with state == ReceivedPayment.", cashier.transactions.get(0).s == TransactionState.ReceivedPayment);	

		cashier.runScheduler();
//		assertTrue("Cashier transactions should contain a transaction with state == PendingDelivery.", cashier.transactions.get(0).s == TransactionState.PendingDelivery); // following instructions execute too quickly
		assertEquals("CustomerDeliveryPayment log should have 2 entries.", customerDeliveryPayment.log.size(), 2);
		assertTrue("CustomerDeliveryPayment log should have \"CustomerDeliveryPayment received msgPaymentReceived from cashier\". The last event logged is " + customerDeliveryPayment.log.getLastLoggedEvent().toString(), customerDeliveryPayment.log.containsString("CustomerDeliveryPayment received msgPaymentReceived from cashier"));
		assertEquals("Market money should be 1110. It's " + market.money + "instead", market.money, 1110);
		assertTrue("Cashier transactions should contain a transaction with state == Delivering.", cashier.transactions.get(0).s == TransactionState.Delivering);
		assertEquals("Delivery Person log should have 1 entry.", deliveryPerson.log.size(), 1);
		assertTrue("Delivery Person log should have \"Delivery Person received msgDeliverOrder\". The last event logged is " + deliveryPerson.log.getLastLoggedEvent().toString(), deliveryPerson.log.containsString("Delivery Person received msgDeliverOrder"));
		
		cashier.msgDeliveringItems(deliveryPerson);
		assertEquals("Cashier log should have 4 entries.", cashier.log.size(), 4);
		assertTrue("Cashier log should have \"Cashier received msgDeliveringItems\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgDeliveringItems"));
		assertTrue("Cashier deliveryPeople should contain a Delivery Person with available == false.", !cashier.deliveryPeople.get(0).available);
		
		cashier.msgFinishedDeliveringItems(deliveryPerson, customerDelivery);
		assertEquals("Cashier log should have 5 entries.", cashier.log.size(), 5);
		assertTrue("Cashier log should have \"Cashier received msgFinishedDeliveringItems\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgFinishedDeliveringItems"));
		assertEquals("Cashier should have 0 transactions.", cashier.transactions.size(), 0);
		assertTrue("Cashier deliveryPeople should contain a Delivery Person with available == true.", cashier.deliveryPeople.get(0).available);
	}
	
	public void testNonNormCustomerDeliveryScenarioDeliveryPersonUnavailable() {
		// set delivery person state to unavailable
		// set delivery person state to available
		// run scheduler
		
		assertEquals("Employee should have an empty log.", employee.log.size(), 0);
		assertEquals("Manager should have an empty log.", manager.log.size(), 0);
		assertEquals("Customer should have an empty log.", customer.log.size(), 0);
		assertEquals("CustomerDelivery should have an empty log.", customerDelivery.log.size(), 0);
		assertEquals("CustomerDeliveryPayment should have an empty log.", customerDeliveryPayment.log.size(), 0);
		assertEquals("Cashier should have an empty log.", cashier.log.size(), 0);
		assertEquals("Cashier should have 0 transactions.", cashier.transactions.size(), 0);
		assertEquals("Market money should be 1000. It's " + market.money + "instead", market.money, 1000);
		
		cashier.msgNewDeliveryPerson(deliveryPerson);
		assertEquals("Cashier log should have 1 entry.", cashier.log.size(), 1);
		assertTrue("Cashier log should have \"Cashier received msgNewDeliveryPerson\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgNewDeliveryPerson"));
		assertEquals("Cashier deliveryPeople should contain 1 Delivery Person.", cashier.deliveryPeople.size(), 1);
		assertTrue("Cashier deliveryPeople should contain a Delivery Person with available == true.", cashier.deliveryPeople.get(0).available);
		cashier.deliveryPeople.get(0).available = false;
		assertTrue("Cashier deliveryPeople should contain a Delivery Person with available == false.", !cashier.deliveryPeople.get(0).available);
		cashier.runScheduler();
		
//		cashier.msgComputeBill(employee, customerDelivery, customerDeliveryPayment, order, collectedItemsAll);
		assertEquals("Cashier log should have 2 entries.", cashier.log.size(), 2);
		assertTrue("Cashier log should have \"Cashier received msgComputeBill\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgComputeBill"));
		assertEquals("Cashier should have 1 transaction.", cashier.transactions.size(), 1);
		assertTrue("Cashier transactions should contain a transaction with state == Pending.", cashier.transactions.get(0).s == TransactionState.Pending);
				
		cashier.runScheduler();
		assertTrue("Cashier transactions should contain a transaction with state == Calculating.", cashier.transactions.get(0).s == TransactionState.Calculating);
		assertEquals("Cashier transactions should contain a transaction with bill for 110", cashier.transactions.get(0).bill, 110);
		assertEquals("CustomerDeliveryPayment log should have 1 entry.", customerDeliveryPayment.log.size(), 1);
		assertTrue("CustomerDeliveryPayment log should have \"CustomerDeliveryPayment received msgHereIsBill\". The last event logged is " + customerDeliveryPayment.log.getLastLoggedEvent().toString(), customerDeliveryPayment.log.containsString("CustomerDeliveryPayment received msgHereIsBill"));

		cashier.msgHereIsPayment(customerDeliveryPayment, 110);
		assertEquals("Cashier log should have 3 entries.", cashier.log.size(), 3);
		assertTrue("Cashier log should have \"Cashier received msgHereIsPayment\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgHereIsPayment"));
		assertEquals("Cashier payment variable should be 110. It's " + cashier.transactions.get(0).payment + "instead", cashier.transactions.get(0).payment, 110);
		assertTrue("Cashier transactions should contain a transaction with state == ReceivedPayment.", cashier.transactions.get(0).s == TransactionState.ReceivedPayment);	

		cashier.runScheduler();
//		assertTrue("Cashier transactions should contain a transaction with state == PendingDelivery.", cashier.transactions.get(0).s == TransactionState.PendingDelivery); // following instructions execute too quickly
		assertEquals("CustomerDeliveryPayment log should have 2 entries.", customerDeliveryPayment.log.size(), 2);
		assertTrue("CustomerDeliveryPayment log should have \"CustomerDeliveryPayment received msgPaymentReceived from cashier\". The last event logged is " + customerDeliveryPayment.log.getLastLoggedEvent().toString(), customerDeliveryPayment.log.containsString("CustomerDeliveryPayment received msgPaymentReceived from cashier"));
		assertEquals("Market money should be 1110. It's " + market.money + "instead", market.money, 1110);
		assertTrue("Cashier transactions should contain a transaction with state == PendingDelivery.", cashier.transactions.get(0).s == TransactionState.PendingDelivery); // following instructions execute too quickly
		
		cashier.deliveryPeople.get(0).available = true;
		assertTrue("Cashier deliveryPeople should contain a Delivery Person with available == true.", cashier.deliveryPeople.get(0).available);
		
		cashier.runScheduler();
		assertTrue("Cashier transactions should contain a transaction with state == Delivering.", cashier.transactions.get(0).s == TransactionState.Delivering);
		assertEquals("Delivery Person log should have 1 entry.", deliveryPerson.log.size(), 1);
		assertTrue("Delivery Person log should have \"Delivery Person received msgDeliverOrder\". The last event logged is " + deliveryPerson.log.getLastLoggedEvent().toString(), deliveryPerson.log.containsString("Delivery Person received msgDeliverOrder"));
		
		cashier.msgDeliveringItems(deliveryPerson);
		assertEquals("Cashier log should have 4 entries.", cashier.log.size(), 4);
		assertTrue("Cashier log should have \"Cashier received msgDeliveringItems\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgDeliveringItems"));
		assertTrue("Cashier deliveryPeople should contain a Delivery Person with available == false.", !cashier.deliveryPeople.get(0).available);
		
		cashier.msgFinishedDeliveringItems(deliveryPerson, customerDelivery);
		assertEquals("Cashier log should have 5 entries.", cashier.log.size(), 5);
		assertTrue("Cashier log should have \"Cashier received msgFinishedDeliveringItems\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgFinishedDeliveringItems"));
		assertEquals("Cashier should have 0 transactions.", cashier.transactions.size(), 0);
		assertTrue("Cashier deliveryPeople should contain a Delivery Person with available == true.", cashier.deliveryPeople.get(0).available);
	}
	
	public void testNonNormCustomerCollectedItemsPartialScenario() {
		
	}
	
	public void testNonNormCustomerNotEnoughMoneyScenario() {
		
	}
}
