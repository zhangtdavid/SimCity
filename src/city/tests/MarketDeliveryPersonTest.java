package city.tests;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import utilities.MarketOrder;
import city.Application;
import city.Application.BUILDING;
import city.Application.FOOD_ITEMS;
import city.buildings.BankBuilding;
import city.buildings.MarketBuilding;
import city.gui.buildings.MarketPanel;
import city.roles.MarketDeliveryPersonRole;
import city.tests.mock.MockMarketCashier;
import city.tests.mock.MockMarketCustomer;
import city.tests.mock.MockMarketCustomerDelivery;
import city.tests.mock.MockMarketCustomerDeliveryPayment;
import city.tests.mock.MockMarketEmployee;
import city.tests.mock.MockMarketManager;
import city.tests.mock.MockPerson;
import junit.framework.TestCase;

public class MarketDeliveryPersonTest extends TestCase {
	
	MarketPanel marketPanel;
	MarketBuilding market;
	
	MockPerson cashierPerson;
	MockMarketCashier cashier;
	
	MockPerson customerPerson;
	MockMarketCustomer customer;
	
	MockPerson customerDeliveryPerson;
	MockMarketCustomerDelivery customerDelivery;

	MockPerson customerDeliveryPaymentPerson;
	MockMarketCustomerDeliveryPayment customerDeliveryPayment;
	
	MockPerson deliveryPersonPerson;
	MarketDeliveryPersonRole deliveryPerson;
	
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
		Application.CityMap.addBuilding(BUILDING.bank, new BankBuilding("BankBuilding"));

		marketPanel = new MarketPanel(Color.blue, new Dimension(500, 500));
		market = new MarketBuilding("Market1", marketPanel);
		
		cashierPerson = new MockPerson("Cashier"); 
		cashier = new MockMarketCashier();
		cashier.setPerson(cashierPerson);
		cashier.market = market;
		
		customerPerson = new MockPerson("Customer"); 
		customer = new MockMarketCustomer();
		customer.setPerson(customerPerson);
		customer.market = market;

		customerDeliveryPerson = new MockPerson("CustomerDelivery"); 
		customerDelivery = new MockMarketCustomerDelivery();
		customerDelivery.setPerson(customerDeliveryPerson);
		customerDelivery.market = market;
		
		customerDeliveryPaymentPerson = new MockPerson("CustomerDeliveryPayment"); 
		customerDeliveryPayment = new MockMarketCustomerDeliveryPayment();
		customerDeliveryPayment.setPerson(customerDeliveryPaymentPerson);
		customerDeliveryPayment.market = market;
		
		deliveryPersonPerson = new MockPerson("DeliveryPerson"); 
		deliveryPerson = new MarketDeliveryPersonRole(market, 0, 12);
		deliveryPerson.setPerson(deliveryPersonPerson);
		deliveryPerson.setMarket(market);
		
		employeePerson = new MockPerson("Employee"); 
		employee = new MockMarketEmployee();
		employee.setPerson(employeePerson);
		employee.market = market;
		
		managerPerson = new MockPerson("Manager"); 
		manager = new MockMarketManager();
		manager.setPerson(managerPerson);
		manager.market = market;

		market.setCashier(cashier);
		market.setManager(manager);
		market.addEmployee(employee);
		
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
	
	public void testNormCustomerDeliveryScenario() {
		assertEquals("Cashier should have an empty log.", cashier.log.size(), 0);
		assertEquals("Employee should have an empty log.", employee.log.size(), 0);
		assertEquals("Manager should have an empty log.", manager.log.size(), 0);
		assertEquals("Customer should have an empty log.", customer.log.size(), 0);
		assertEquals("CustomerDelivery should have an empty log.", customerDelivery.log.size(), 0);
		assertEquals("CustomerDeliveryPayment should have an empty log.", customerDeliveryPayment.log.size(), 0);
		assertEquals("DeliveryPerson should have an empty log.", deliveryPerson.log.size(), 0);
		assertTrue("DeliveryPerson customerDelivery should be null.", deliveryPerson.getCustomerDelivery() == null);

		deliveryPerson.msgDeliverOrder(customerDelivery, collectedItemsAll, order.orderId);
		assertEquals("DeliveryPerson log should have 1 entry.", deliveryPerson.log.size(), 1);
		assertTrue("DeliveryPerson log should have \"DeliveryPerson received msgDeliverOrder\". The last event logged is " + deliveryPerson.log.getLastLoggedEvent().toString(), deliveryPerson.log.containsString("DeliveryPerson received msgDeliverOrder"));
		assertTrue("DeliveryPerson customerDelivery should be customerDelivery.", deliveryPerson.getCustomerDelivery() == customerDelivery);
		for (FOOD_ITEMS item: collectedItemsAll.keySet()) {
			assertTrue("deliveryPerson.collectedItems should be collectedItemsAll.", deliveryPerson.getCollectedItems().get(item) == collectedItemsAll.get(item));
		}
		assertTrue("deliveryPerson.orderId should be order.orderId.", deliveryPerson.getOrderId() == order.orderId);
		
		deliveryPerson.runScheduler();
//		assertEquals("Cashier log should have 1 entry.", cashier.log.size(), 1); // next actions execute too quickly
//		assertTrue("Cashier log should have \"Cashier received msgDeliveringItems\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgDeliveringItems"));
		assertEquals("CustomerDelivery log should have 1 entry.", customerDelivery.log.size(), 1);
		assertTrue("CustomerDelivery log should have \"CustomerDelivery received msgHereIsOrderDelivery\". The last event logged is " + customerDelivery.log.getLastLoggedEvent().toString(), customerDelivery.log.containsString("CustomerDelivery received msgHereIsOrderDelivery"));
		assertEquals("Cashier log should have 2 entries.", cashier.log.size(), 2);
		assertTrue("Cashier log should have \"Cashier received msgFinishedDeliveringItems\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgFinishedDeliveringItems"));
		assertTrue("DeliveryPerson customerDelivery should be null.", deliveryPerson.getCustomerDelivery() == null);
	}
}

