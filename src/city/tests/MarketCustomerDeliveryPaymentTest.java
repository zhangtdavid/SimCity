package city.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import utilities.MarketOrder;
import utilities.MarketTransaction;
import city.Application.BUILDING;
import city.Application.CityMap;
import city.Application.FOOD_ITEMS;
import city.buildings.BankBuilding;
import city.buildings.MarketBuilding;
import city.buildings.RestaurantChungBuilding;
import city.buildings.interfaces.Bank;
import city.buildings.interfaces.Market;
import city.buildings.interfaces.RestaurantChung;
import city.roles.MarketCustomerDeliveryPaymentRole;
import city.roles.interfaces.MarketCashier.TransactionState;
import city.tests.mocks.MockMarketCashier;
import city.tests.mocks.MockMarketCustomer;
import city.tests.mocks.MockMarketCustomerDelivery;
import city.tests.mocks.MockMarketDeliveryPerson;
import city.tests.mocks.MockMarketEmployee;
import city.tests.mocks.MockMarketManager;
import city.tests.mocks.MockPerson;

public class MarketCustomerDeliveryPaymentTest extends TestCase {
	Bank bank;
	RestaurantChung restaurantChung;
	Market market;
	
	MockPerson cashierPerson;
	MockMarketCashier cashier;
	
	List<MarketTransaction> marketTransactions;
	
	MockPerson customerPerson;
	MockMarketCustomer customer;
	
	MockPerson customerDeliveryPerson;
	MockMarketCustomerDelivery customerDelivery;

	MockPerson customerDeliveryPaymentPerson;
	MarketCustomerDeliveryPaymentRole customerDeliveryPayment;
	
	MockPerson deliveryPersonPerson;
	MockMarketDeliveryPerson deliveryPerson;
	
	MockPerson employeePerson;
	MockMarketEmployee employee;
	
	MockPerson managerPerson;
	MockMarketManager manager;
	
	Map<FOOD_ITEMS, Integer> orderItems;
	MarketOrder order;
	
	Map<FOOD_ITEMS, Integer> collectedItemsAll;
	Map<FOOD_ITEMS, Integer> collectedItemsPartial;
	
	public void setUp() throws Exception {
		super.setUp();
		bank = new BankBuilding("Bank", null, null);
		CityMap.addBuilding(BUILDING.bank, bank);

		restaurantChung = new RestaurantChungBuilding("RestaurantChung", null, null);
		CityMap.addBuilding(BUILDING.restaurant, restaurantChung);
		
		market = new MarketBuilding("Market1", null, null);

		cashierPerson = new MockPerson("Cashier"); 
		cashier = new MockMarketCashier(); // this constructs a bank customer, which requires a bank
		cashier.setPerson(cashierPerson);
		cashier.setMarket(market);
		
		marketTransactions = Collections.synchronizedList(new ArrayList<MarketTransaction>());
		
		customerPerson = new MockPerson("Customer"); 
		customer = new MockMarketCustomer();
		customer.setPerson(customerPerson);
		customer.market = market;

		customerDeliveryPerson = new MockPerson("CustomerDelivery"); 
		customerDelivery = new MockMarketCustomerDelivery();
		customerDelivery.setPerson(customerDeliveryPerson);
		customerDelivery.market = market;
		
		customerDeliveryPaymentPerson = new MockPerson("CustomerDeliveryPayment"); 
		customerDeliveryPayment = new MarketCustomerDeliveryPaymentRole(restaurantChung, marketTransactions);
		customerDeliveryPayment.setPerson(customerDeliveryPaymentPerson);
		customerDeliveryPayment.setMarket(market);
		
		deliveryPersonPerson = new MockPerson("DeliveryPerson"); 
		deliveryPerson = new MockMarketDeliveryPerson();
		deliveryPerson.setPerson(deliveryPersonPerson);
		deliveryPerson.market = market;
		
		employeePerson = new MockPerson("Employee"); 
		employee = new MockMarketEmployee();
		employee.setPerson(employeePerson);
		employee.market = market;
		
		managerPerson = new MockPerson("Manager"); 
		manager = new MockMarketManager();
		manager.setPerson(managerPerson);
		manager.market = market;
		
		orderItems = new HashMap<FOOD_ITEMS, Integer>();
		orderItems.put(FOOD_ITEMS.chicken, 5);
		orderItems.put(FOOD_ITEMS.pizza, 5);
		orderItems.put(FOOD_ITEMS.salad, 5);
		orderItems.put(FOOD_ITEMS.steak, 5);
		
		order = new MarketOrder(orderItems);
		
		collectedItemsAll = new HashMap<FOOD_ITEMS, Integer>();
		collectedItemsAll.put(FOOD_ITEMS.chicken, 5);
		collectedItemsAll.put(FOOD_ITEMS.pizza, 5);
		collectedItemsAll.put(FOOD_ITEMS.salad, 5);
		collectedItemsAll.put(FOOD_ITEMS.steak, 5);
		
		collectedItemsPartial = new HashMap<FOOD_ITEMS, Integer>();
		collectedItemsPartial.put(FOOD_ITEMS.chicken, 3);
		collectedItemsPartial.put(FOOD_ITEMS.pizza, 3);
		collectedItemsPartial.put(FOOD_ITEMS.salad, 3);
		collectedItemsPartial.put(FOOD_ITEMS.steak, 3);
	}
	
	public void testMarketOrderPaymentScenarios() {
		assertEquals("Employee should have an empty log.", employee.log.size(), 0);
		assertEquals("Manager should have an empty log.", manager.log.size(), 0);
		assertEquals("Customer should have an empty log.", customer.log.size(), 0);
		assertEquals("CustomerDelivery should have an empty log.", customerDelivery.log.size(), 0);
		assertEquals("CustomerDeliveryPayment should have an empty log.", customerDeliveryPayment.log.size(), 0);
		assertEquals("Cashier should have an empty log.", cashier.log.size(), 0);
		assertEquals("DeliveryPerson should have an empty log.", deliveryPerson.log.size(), 0);
		assertEquals("Market money should be 1000. It's " + market.getCash() + "instead", market.getCash(), 1000);
		
//		customerDeliveryPayment.msgComputeBill(employee, customer, orderItems, collectedItemsAll, order.orderId);
//		assertEquals("Cashier log should have 1 entry.", cashier.log.size(), 1);
//		assertTrue("Cashier log should have \"Cashier received msgComputeBill\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgComputeBill"));
//		assertEquals("Cashier should have 1 transaction.", cashier.getTransactions().size(), 1);
//		assertTrue("Cashier transactions should contain a transaction with state == Pending.", cashier.getTransactions().get(0).getTransactionState() == TransactionState.Pending);
//				
//		cashier.runScheduler();
//		assertTrue("Cashier transactions should contain a transaction with state == Calculating.", cashier.getTransactions().get(0).getTransactionState() == TransactionState.Calculating);
//		assertEquals("Cashier transactions should contain a transaction with bill for 110", cashier.getTransactions().get(0).getBill(), 110);
//		assertEquals("Customer log should have 1 entry.", customer.log.size(), 1);
//		assertTrue("Customer log should have \"Customer received msgHereIsOrderandBill\". The last event logged is " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Customer received msgHereIsOrderandBill"));
//
//		cashier.msgHereIsPayment(order.orderId, 110);
//		assertEquals("Cashier log should have 2 entries.", cashier.log.size(), 2);
//		assertTrue("Cashier log should have \"Cashier received msgHereIsPayment\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgHereIsPayment"));
//		assertEquals("Cashier payment variable should be 110.00. It's " + cashier.getTransactions().get(0).getPayment() + "instead", cashier.getTransactions().get(0).getPayment(), 110);
//		assertTrue("Cashier transactions should contain a transaction with state == ReceivedPayment.", cashier.getTransactions().get(0).getTransactionState() == TransactionState.ReceivedPayment);	
//		
//		cashier.runScheduler();
//		assertEquals("Customer log should have 2 entries.", customer.log.size(), 2);
//		assertTrue("Customer log should have \"Customer received msgPaymentReceived from Market Cashier\". The last event logged is " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Customer received msgPaymentReceived from Market Cashier"));
//		assertEquals("Market money should be 1110.00. It's " + market.getCash() + "instead", market.getCash(), 1110);
//		assertEquals("Cashier should have 0 transactions.", cashier.getTransactions().size(), 0);		
	}
}
