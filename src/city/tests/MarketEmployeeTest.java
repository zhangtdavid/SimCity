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
import city.roles.MarketEmployeeRole;
import city.roles.MarketEmployeeRole.MarketEmployeeEvent;
import city.roles.MarketEmployeeRole.MarketEmployeeState;
import city.tests.mock.MockMarketCashier;
import city.tests.mock.MockMarketCustomer;
import city.tests.mock.MockMarketCustomerDelivery;
import city.tests.mock.MockMarketCustomerDeliveryPayment;
import city.tests.mock.MockMarketDeliveryPerson;
import city.tests.mock.MockMarketManager;
import city.tests.mock.MockPerson;
import junit.framework.TestCase;

public class MarketEmployeeTest extends TestCase {
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
	MarketEmployeeRole employee;
//	MarketAnimatedEmployee employeeGui;
	
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
		deliveryPerson = new MockMarketDeliveryPerson();
		deliveryPerson.setPerson(deliveryPersonPerson);
		deliveryPerson.market = market;
		
		employeePerson = new MockPerson("Employee"); 
		employee = new MarketEmployeeRole(market, 0, 12);
		employee.setPerson(employeePerson);
		employee.setMarket(market);
//		employee.setAnimation((Animation) employeeGui);
		
		managerPerson = new MockPerson("Manager"); 
		manager = new MockMarketManager();
		manager.setPerson(managerPerson);
		manager.market = market;

		market.cashier = cashier;
		market.manager = manager;
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
	
	public void testNormCustomerScenario() {
		assertEquals("Cashier should have an empty log.", cashier.log.size(), 0);
		assertEquals("Employee should have an empty log.", employee.log.size(), 0);
		assertEquals("Manager should have an empty log.", manager.log.size(), 0);
		assertEquals("Customer should have an empty log.", customer.log.size(), 0);
		assertEquals("CustomerDelivery should have an empty log.", customerDelivery.log.size(), 0);
		assertEquals("CustomerDeliveryPayment should have an empty log.", customerDeliveryPayment.log.size(), 0);
		assertTrue("Employee state should be None.", employee.state == MarketEmployeeState.None);
		assertEquals("DeliveryPerson should have an empty log.", deliveryPerson.log.size(), 0);
        for (FOOD_ITEMS item: order.orderItems.keySet()) {
    		assertTrue("market.inventory should be 50.", market.inventory.get(item) == 50);
        }
        
		employee.msgAssistCustomer(customer);
		assertEquals("Employee log should have 1 entry.", employee.log.size(), 1);
		assertTrue("Employee log should have \"Employee received msgAssistCustomer\". The last event logged is " + employee.log.getLastLoggedEvent().toString(), employee.log.containsString("Employee received msgAssistCustomer"));
		assertTrue("Employee event should be AskedToAssistCustomer.", employee.event == MarketEmployeeEvent.AskedToAssistCustomer);
		assertTrue("Employee customer should be customer.", employee.customer == customer);
		
		employee.runScheduler();
		assertTrue("Employee state should be AskedForOrder.", employee.state == MarketEmployeeState.AskedForOrder);
		assertEquals("Customer log should have 1 entry.", customer.log.size(), 1);
		assertTrue("Customer log should have \"Customer received msgWhatWouldYouLike\". The last event logged is " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Customer received msgWhatWouldYouLike"));

		employee.msgHereIsMyOrder(customer, order.orderItems, order.orderId);
		assertEquals("Employee log should have 2 entries.", employee.log.size(), 2);
		assertTrue("Employee log should have \"Employee received msgHereIsMyOrder\". The last event logged is actually " + employee.log.getLastLoggedEvent().toString(), employee.log.containsString("Employee received msgHereIsMyOrder"));
		assertTrue("Employee event should be OrderReceived.", employee.event == MarketEmployeeEvent.OrderReceived);
		assertTrue("employee.orderId should be order.orderId.", employee.orderId == order.orderId);
        for (FOOD_ITEMS item: order.orderItems.keySet()) {
    		assertTrue("employee.orderItems should be order.orderItems.", employee.order.get(item) == order.orderItems.get(item));
        }
		
		employee.runScheduler();
        for (FOOD_ITEMS item: order.orderItems.keySet()) {
    		assertTrue("employee.collectedItems should be order.orderItems.", employee.collectedItems.get(item) == order.orderItems.get(item));
        }
        for (FOOD_ITEMS item: order.orderItems.keySet()) {
    		assertTrue("market.inventory should be market.inventory - order.orderItems.", market.inventory.get(item) == 45);
        }
		assertEquals("Cashier log should have 1 entry.", cashier.log.size(), 1);
		assertTrue("Cashier log should have \"Cashier received msgComputeBill\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgComputeBill"));
		assertEquals("Manager log should have 1 entry.", manager.log.size(), 1);
		assertTrue("Manager log should have \"Manager received msgIAmAvailableToAssist\". The last event logged is " + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Manager received msgIAmAvailableToAssist"));
		assertTrue("Employee customer should be null.", employee.customer == null);
		assertTrue("Employee state should be None.", employee.state == MarketEmployeeState.None);
	}
	public void testNormCustomerDeliveryScenario() {
		assertEquals("Cashier should have an empty log.", cashier.log.size(), 0);
		assertEquals("Employee should have an empty log.", employee.log.size(), 0);
		assertEquals("Manager should have an empty log.", manager.log.size(), 0);
		assertEquals("Customer should have an empty log.", customer.log.size(), 0);
		assertEquals("CustomerDelivery should have an empty log.", customerDelivery.log.size(), 0);
		assertEquals("CustomerDeliveryPayment should have an empty log.", customerDeliveryPayment.log.size(), 0);
		assertTrue("Employee state should be None.", employee.state == MarketEmployeeState.None);
		assertEquals("DeliveryPerson should have an empty log.", deliveryPerson.log.size(), 0);
        for (FOOD_ITEMS item: order.orderItems.keySet()) {
    		assertTrue("market.inventory should be 50.", market.inventory.get(item) == 50);
        }

		employee.msgAssistCustomerDelivery(customerDelivery, customerDeliveryPayment);
		assertEquals("Employee log should have 1 entry.", employee.log.size(), 1);
		assertTrue("Employee log should have \"Employee received msgAssistCustomer\". The last event logged is " + employee.log.getLastLoggedEvent().toString(), employee.log.containsString("Employee received msgAssistCustomer"));
		assertTrue("Employee event should be AskedToAssistCustomer.", employee.event == MarketEmployeeEvent.AskedToAssistCustomer);
		assertTrue("Employee customerDelivery should be customerDelivery.", employee.customerDelivery == customerDelivery);
		assertTrue("Employee customerDeliveryPayment should be customerDeliveryPayment.", employee.customerDeliveryPayment == customerDeliveryPayment);

		employee.runScheduler();
		assertTrue("Employee state should be AskedForOrder.", employee.state == MarketEmployeeState.AskedForOrder);
		assertEquals("Manager log should have 1 entry.", manager.log.size(), 1);
		assertTrue("Manager log should have \"Manager received msgWhatWouldCustomerDeliveryLike\". The last event logged is " + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Manager received msgWhatWouldCustomerDeliveryLike"));

		employee.msgHereIsCustomerDeliveryOrder(order.orderItems, order.orderId);
		assertEquals("Employee log should have 2 entries.", employee.log.size(), 2);
		assertTrue("Employee log should have \"Employee received msgHereIsCustomerDeliveryOrder\". The last event logged is actually " + employee.log.getLastLoggedEvent().toString(), employee.log.containsString("Employee received msgHereIsCustomerDeliveryOrder"));
		assertTrue("Employee event should be OrderReceived.", employee.event == MarketEmployeeEvent.OrderReceived);
		assertTrue("employee.orderId should be order.orderId.", employee.orderId == order.orderId);
        for (FOOD_ITEMS item: order.orderItems.keySet()) {
    		assertTrue("employee.orderItems should be order.orderItems.", employee.order.get(item) == order.orderItems.get(item));
        }
		
		employee.runScheduler();
        for (FOOD_ITEMS item: order.orderItems.keySet()) {
    		assertTrue("employee.collectedItems should be order.orderItems.", employee.collectedItems.get(item) == order.orderItems.get(item));
        }
        for (FOOD_ITEMS item: order.orderItems.keySet()) {
    		assertTrue("market.inventory should be market.inventory - order.orderItems.", market.inventory.get(item) == 45);
        }
		assertEquals("Cashier log should have 1 entry.", cashier.log.size(), 1);
		assertTrue("Cashier log should have \"Cashier received msgComputeBill\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgComputeBill"));
		assertEquals("Manager log should have 2 entries.", manager.log.size(), 2);
		assertTrue("Manager log should have \"Manager received msgIAmAvailableToAssist\". The last event logged is " + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Manager received msgIAmAvailableToAssist"));
		assertTrue("Employee customer should be null.", employee.customer == null);
		assertTrue("Employee state should be None.", employee.state == MarketEmployeeState.None);
	}
}

