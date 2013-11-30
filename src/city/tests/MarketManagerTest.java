package city.tests;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import utilities.MarketOrder;
import city.Application;
import city.Application.BUILDING;
import city.Application.FOOD_ITEMS;
import city.buildings.BankBuilding;
import city.buildings.MarketBuilding;
import city.gui.MarketPanel;
import city.roles.MarketManagerRole;
import city.roles.MarketManagerRole.MyMarketEmployee.MarketEmployeeState;
import city.tests.mock.MockMarketCashier;
import city.tests.mock.MockMarketCustomer;
import city.tests.mock.MockMarketCustomerDelivery;
import city.tests.mock.MockMarketCustomerDeliveryPayment;
import city.tests.mock.MockMarketDeliveryPerson;
import city.tests.mock.MockMarketEmployee;
import city.tests.mock.MockPerson;

public class MarketManagerTest extends TestCase {
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
	MockMarketDeliveryPerson deliveryPerson;
	
	MockPerson employeePerson;
	MockMarketEmployee employee;
	
	MockPerson managerPerson;
	MarketManagerRole manager;
	
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
		deliveryPerson = new MockMarketDeliveryPerson();
		deliveryPerson.setPerson(deliveryPersonPerson);
		deliveryPerson.market = market;
		
		employeePerson = new MockPerson("Employee"); 
		employee = new MockMarketEmployee();
		employee.setPerson(employeePerson);
		employee.market = market;
		
		managerPerson = new MockPerson("Manager"); 
		manager = new MarketManagerRole(market, 0, 12);
		manager.setPerson(managerPerson);
		manager.setMarket(market);
		
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
	
	public void testNormCustomerScenario() {
		assertEquals("Cashier should have an empty log.", cashier.log.size(), 0);
		assertEquals("Employee should have an empty log.", employee.log.size(), 0);
		assertEquals("Manager should have an empty log.", manager.log.size(), 0);
		assertEquals("Customer should have an empty log.", customer.log.size(), 0);
		assertEquals("CustomerDelivery should have an empty log.", customerDelivery.log.size(), 0);
		assertEquals("CustomerDeliveryPayment should have an empty log.", customerDeliveryPayment.log.size(), 0);
		assertEquals("DeliveryPerson should have an empty log.", deliveryPerson.log.size(), 0);

		manager.msgNewEmployee(employee);
		assertEquals("Manager log should have 1 entry.", manager.log.size(), 1);
		assertTrue("Manager log should have \"Manager received msgNewEmployee\". The last event logged is " + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Manager received msgNewEmployee"));
		assertEquals("Manager should have 1 employee in employee list.", manager.employees.size(), 1);
		assertTrue("Manager employees should contain an employee with state == Available.", manager.employees.get(0).s == MarketEmployeeState.Available);

		assertTrue("Manager scheduler should return false", !manager.runScheduler());

		manager.msgIWouldLikeToPlaceAnOrder(customer);
		assertEquals("Manager log should have 2 entries.", manager.log.size(), 2);
		assertTrue("Manager log should have \"Manager received msgIWouldLikeToPlaceAnOrder\". The last event logged is " + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Manager received msgIWouldLikeToPlaceAnOrder"));
		assertEquals("Manager should have 1 customer in customer list.", manager.customers.size(), 1);
		
		manager.runScheduler();
		assertEquals("Employee log should have 1 entry.", employee.log.size(), 1);
		assertTrue("Employee log should have \"Employee received msgAssistCustomer\". The last event logged is " + employee.log.getLastLoggedEvent().toString(), employee.log.containsString("Employee received msgAssistCustomer"));
		assertEquals("Manager should have 0 customer in customer list.", manager.customers.size(), 0);
		assertTrue("Manager employees should contain an employee with state == CollectingItems.", manager.employees.get(0).s == MarketEmployeeState.CollectingItems);

		manager.msgIAmAvailableToAssist(employee);
		assertEquals("Manager log should have 3 entries.", manager.log.size(), 3);
		assertTrue("Manager log should have \"Manager received msgIAmAvailableToAssist\". The last event logged is " + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Manager received msgIAmAvailableToAssist"));
		assertTrue("Manager employees should contain an employee with state == Available.", manager.employees.get(0).s == MarketEmployeeState.Available);
	}
	
	public void testNormCustomerDeliveryScenario() {
		assertEquals("Cashier should have an empty log.", cashier.log.size(), 0);
		assertEquals("Employee should have an empty log.", employee.log.size(), 0);
		assertEquals("Manager should have an empty log.", manager.log.size(), 0);
		assertEquals("Customer should have an empty log.", customer.log.size(), 0);
		assertEquals("CustomerDelivery should have an empty log.", customerDelivery.log.size(), 0);
		assertEquals("CustomerDeliveryPayment should have an empty log.", customerDeliveryPayment.log.size(), 0);
		assertEquals("DeliveryPerson should have an empty log.", deliveryPerson.log.size(), 0);

		manager.msgNewEmployee(employee);
		assertEquals("Manager log should have 1 entry.", manager.log.size(), 1);
		assertTrue("Manager log should have \"Manager received msgNewEmployee\". The last event logged is " + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Manager received msgNewEmployee"));
		assertEquals("Manager should have 1 employee in employee list.", manager.employees.size(), 1);
		assertTrue("Manager employees should contain an employee with customerDelivery == null.", manager.employees.get(0).customerDelivery == null);
		assertTrue("Manager employees should contain an employee with state == Available.", manager.employees.get(0).s == MarketEmployeeState.Available);

		assertTrue("Manager scheduler should return false", !manager.runScheduler());

		manager.msgIWouldLikeToPlaceADeliveryOrder(customerDelivery, customerDeliveryPayment, order.orderItems, order.orderId);
		assertEquals("Manager log should have 2 entries.", manager.log.size(), 2);
		assertTrue("Manager log should have \"Manager received msgIWouldLikeToPlaceADeliveryOrder\". The last event logged is " + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Manager received msgIWouldLikeToPlaceADeliveryOrder"));
		assertEquals("Manager should have 1 customer in customer list.", manager.customers.size(), 1);
		
		manager.runScheduler();
		assertEquals("Employee log should have 1 entry.", employee.log.size(), 1);
		assertTrue("Employee log should have \"Employee received msgAssistCustomerDelivery\". The last event logged is " + employee.log.getLastLoggedEvent().toString(), employee.log.containsString("Employee received msgAssistCustomerDelivery"));
		assertTrue("Manager employees should contain an employee with customerDelivery == customerDelivery.", manager.employees.get(0).customerDelivery == customerDelivery);
		assertTrue("Manager employees should contain an employee with state == GoingToPhone.", manager.employees.get(0).s == MarketEmployeeState.GoingToPhone);

		manager.msgWhatWouldCustomerDeliveryLike(employee);
		assertEquals("Manager log should have 3 entries.", manager.log.size(), 3);
		assertTrue("Manager log should have \"Manager received msgWhatWouldCustomerDeliveryLike\". The last event logged is " + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Manager received msgWhatWouldCustomerDeliveryLike"));
		assertTrue("Manager employees should contain an employee with state == GettingOrder.", manager.employees.get(0).s == MarketEmployeeState.GettingOrder);

		manager.runScheduler();
		assertEquals("Employee log should have 2 entries.", employee.log.size(), 2);
		assertTrue("Employee log should have \"Employee received msgHereIsCustomerDeliveryOrder\". The last event logged is " + employee.log.getLastLoggedEvent().toString(), employee.log.containsString("Employee received msgHereIsCustomerDeliveryOrder"));
		assertTrue("Manager employees should contain an employee with state == CollectingItems.", manager.employees.get(0).s == MarketEmployeeState.CollectingItems);
		assertEquals("Manager should have 0 customer in customer list.", manager.customers.size(), 0);

		manager.msgIAmAvailableToAssist(employee);
		assertEquals("Manager log should have 4 entries.", manager.log.size(), 4);
		assertTrue("Manager log should have \"Manager received msgIAmAvailableToAssist\". The last event logged is " + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Manager received msgIAmAvailableToAssist"));
		assertTrue("Manager employees should contain an employee with state == Available.", manager.employees.get(0).s == MarketEmployeeState.Available);
	}
}
