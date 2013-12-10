package city.tests.roles;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import utilities.MarketOrder;
import city.Application;
import city.Application.BUILDING;
import city.Application.CityMap;
import city.Application.FOOD_ITEMS;
import city.buildings.BankBuilding;
import city.buildings.MarketBuilding;
import city.buildings.interfaces.Market.MyMarketEmployee.MarketEmployeeState;
import city.gui.interiors.MarketPanel;
import city.roles.MarketManagerRole;
import city.roles.interfaces.MarketManager.WorkingState;
import city.tests.agents.mocks.MockPerson;
import city.tests.roles.mocks.MockMarketCashier;
import city.tests.roles.mocks.MockMarketCustomer;
import city.tests.roles.mocks.MockMarketCustomerDelivery;
import city.tests.roles.mocks.MockMarketCustomerDeliveryPayment;
import city.tests.roles.mocks.MockMarketDeliveryPerson;
import city.tests.roles.mocks.MockMarketEmployee;

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
	
	MockPerson managerPerson2;
	MarketManagerRole manager2;
	
	Map<FOOD_ITEMS, Integer> orderItems;
	MarketOrder order;
	
	Map<FOOD_ITEMS, Integer> collectedItemsAll;
	Map<FOOD_ITEMS, Integer> collectedItemsPartial;
	
	public void setUp() throws Exception {
		super.setUp();
		CityMap.clearMap();

		Application.CityMap.addBuilding(BUILDING.bank, new BankBuilding("BankBuilding", null, null));

		marketPanel = new MarketPanel(Color.blue);
		market = new MarketBuilding("Market1", marketPanel, null);
		
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
		market.addEmployee(employee);
	
		managerPerson = new MockPerson("Manager"); 
		manager = new MarketManagerRole(market, 0, 12);
		manager.setPerson(managerPerson);
		manager.setMarket(market);
		manager.setActive();
		
		managerPerson2 = new MockPerson("Manager2"); 
		manager2 = new MarketManagerRole(market, 13, 24);
		manager2.setPerson(managerPerson2);
		manager2.setMarket(market);
		
		orderItems = new HashMap<FOOD_ITEMS, Integer>();
		orderItems.put(FOOD_ITEMS.chicken, 5);
		orderItems.put(FOOD_ITEMS.pizza, 5);
		orderItems.put(FOOD_ITEMS.salad, 5);
		orderItems.put(FOOD_ITEMS.steak, 5);
		
		MarketOrder.setCurrentID(0);
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
		
		market.setManager(manager);
	}
	
	public void testNormCustomerScenario() {
		assertEquals("Cashier should have an empty log.", cashier.log.size(), 0);
		assertEquals("Employee should have an empty log.", employee.log.size(), 0);
		assertEquals("Manager should have an empty log.", manager.log.size(), 0);
		assertEquals("Customer should have an empty log.", customer.log.size(), 0);
		assertEquals("CustomerDelivery should have an empty log.", customerDelivery.log.size(), 0);
		assertEquals("CustomerDeliveryPayment should have an empty log.", customerDeliveryPayment.log.size(), 0);
		assertEquals("DeliveryPerson should have an empty log.", deliveryPerson.log.size(), 0);

		assertTrue("Manager scheduler should return false", !manager.runScheduler());

		manager.msgIWouldLikeToPlaceAnOrder(customer);
		assertEquals("Manager log should have 1 entry.", manager.log.size(), 1);
		assertTrue("Manager log should have \"Manager received msgIWouldLikeToPlaceAnOrder\". The last event logged is " + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Manager received msgIWouldLikeToPlaceAnOrder"));
		assertEquals("Market should have 1 customer in customer list.", market.getCustomers().size(), 1);
		
		manager.runScheduler();
		assertEquals("Employee log should have 1 entry.", employee.log.size(), 1);
		assertTrue("Employee log should have \"Employee received msgAssistCustomer\". The last event logged is " + employee.log.getLastLoggedEvent().toString(), employee.log.containsString("Employee received msgAssistCustomer"));
		assertTrue("Market employees should contain an employee with state == CollectingItems.", market.getEmployees().get(0).getMarketEmployeeState() == MarketEmployeeState.CollectingItems);

		manager.msgIAmAvailableToAssist(employee);
		assertEquals("Manager log should have 2 entries.", manager.log.size(), 2);
		assertTrue("Manager log should have \"Manager received msgIAmAvailableToAssist\". The last event logged is " + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Manager received msgIAmAvailableToAssist"));
		assertTrue("Market employees should contain an employee with state == Available.", market.getEmployees().get(0).getMarketEmployeeState() == MarketEmployeeState.Available);
	}
	
	public void testNormCustomerDeliveryScenario() {
		assertEquals("Cashier should have an empty log.", cashier.log.size(), 0);
		assertEquals("Employee should have an empty log.", employee.log.size(), 0);
		assertEquals("Manager should have an empty log.", manager.log.size(), 0);
		assertEquals("Customer should have an empty log.", customer.log.size(), 0);
		assertEquals("CustomerDelivery should have an empty log.", customerDelivery.log.size(), 0);
		assertEquals("CustomerDeliveryPayment should have an empty log.", customerDeliveryPayment.log.size(), 0);
		assertEquals("DeliveryPerson should have an empty log.", deliveryPerson.log.size(), 0);

		manager.msgIWouldLikeToPlaceADeliveryOrder(customerDelivery, customerDeliveryPayment, order.getOrderItems(), order.getOrderId());
		assertEquals("Manager log should have 1 entry.", manager.log.size(), 1);
		assertTrue("Manager log should have \"Manager received msgIWouldLikeToPlaceADeliveryOrder\". The last event logged is " + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Manager received msgIWouldLikeToPlaceADeliveryOrder"));
		assertEquals("Manager should have 1 customer in customer list.", market.getCustomers().size(), 1);
		
		manager.runScheduler();
		assertEquals("Employee log should have 1 entry.", employee.log.size(), 1);
		assertTrue("Employee log should have \"Employee received msgAssistCustomerDelivery\". The last event logged is " + employee.log.getLastLoggedEvent().toString(), employee.log.containsString("Employee received msgAssistCustomerDelivery"));
		assertTrue("Market employees should contain an employee with customerDelivery == customerDelivery.", market.getEmployees().get(0).getCustomerDelivery() == customerDelivery);
		assertTrue("Market employees should contain an employee with state == GoingToPhone.", market.getEmployees().get(0).getMarketEmployeeState() == MarketEmployeeState.GoingToPhone);

		manager.msgWhatWouldCustomerDeliveryLike(employee);
		assertEquals("Manager log should have 2 entries.", manager.log.size(), 2);
		assertTrue("Manager log should have \"Manager received msgWhatWouldCustomerDeliveryLike\". The last event logged is " + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Manager received msgWhatWouldCustomerDeliveryLike"));
		assertTrue("Market employees should contain an employee with state == GettingOrder.", market.getEmployees().get(0).getMarketEmployeeState() == MarketEmployeeState.GettingOrder);

		manager.runScheduler();
		assertEquals("Employee log should have 2 entries.", employee.log.size(), 2);
		assertTrue("Employee log should have \"Employee received msgHereIsCustomerDeliveryOrder\". The last event logged is " + employee.log.getLastLoggedEvent().toString(), employee.log.containsString("Employee received msgHereIsCustomerDeliveryOrder"));
		assertTrue("Market employees should contain an employee with state == CollectingItems.", market.getEmployees().get(0).getMarketEmployeeState() == MarketEmployeeState.CollectingItems);
//		assertEquals("Manager should have 0 customer in customer list.", market.getCustomers().size(), 0);

		manager.msgIAmAvailableToAssist(employee);
		assertEquals("Manager log should have 3 entries.", manager.log.size(), 3);
		assertTrue("Manager log should have \"Manager received msgIAmAvailableToAssist\". The last event logged is " + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Manager received msgIAmAvailableToAssist"));
		assertTrue("Market employees should contain an employee with state == Available.", market.getEmployees().get(0).getMarketEmployeeState() == MarketEmployeeState.Available);
	}
	
	public void testMultipleCustomersandShiftChange() {
		assertTrue("Manager should have workingState == Working.",  manager.getWorkingState() == WorkingState.Working);
		manager.setInactive();
		assertTrue("Manager should have workingState == GoingOffShift.",  manager.getWorkingState() == WorkingState.GoingOffShift);
		
		// First customer
		manager.msgIWouldLikeToPlaceAnOrder(customer);
		assertEquals("Manager log should have 1 entry.", manager.log.size(), 1);
		assertTrue("Manager log should have \"Manager received msgIWouldLikeToPlaceAnOrder\". The last event logged is " + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Manager received msgIWouldLikeToPlaceAnOrder"));
		assertEquals("Market should have 1 customer in customer list.", market.getCustomers().size(), 1);
		
		manager.runScheduler();
		assertEquals("Employee log should have 1 entry.", employee.log.size(), 1);
		assertTrue("Employee log should have \"Employee received msgAssistCustomer\". The last event logged is " + employee.log.getLastLoggedEvent().toString(), employee.log.containsString("Employee received msgAssistCustomer"));
		assertTrue("Market employees should contain an employee with state == CollectingItems.", market.getEmployees().get(0).getMarketEmployeeState() == MarketEmployeeState.CollectingItems);

		// Second customer
		manager.msgIWouldLikeToPlaceADeliveryOrder(customerDelivery, customerDeliveryPayment, order.getOrderItems(), order.getOrderId());
		assertEquals("Manager log should have 2 entries.", manager.log.size(), 2);
		assertTrue("Manager log should have \"Manager received msgIWouldLikeToPlaceADeliveryOrder\". The last event logged is " + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Manager received msgIWouldLikeToPlaceADeliveryOrder"));
		assertEquals("Manager should have 2 customers in customer list.", market.getCustomers().size(), 2);
		
		manager.runScheduler();
		assertEquals("Employee log should have 1 entry.", employee.log.size(), 1);
//		assertTrue("Employee log should have \"Employee received msgAssistCustomerDelivery\". The last event logged is " + employee.log.getLastLoggedEvent().toString(), employee.log.containsString("Employee received msgAssistCustomerDelivery"));
//		assertTrue("Market employees should contain an employee with customerDelivery == customerDelivery.", market.getEmployees().get(0).getCustomerDelivery() == customerDelivery);
//		assertTrue("Market employees should contain an employee with state == GoingToPhone.", market.getEmployees().get(0).getMarketEmployeeState() == MarketEmployeeState.GoingToPhone);

		// Change manager
		market.setManager(manager2);
		manager2.setActive();
		manager.runScheduler();
		assertTrue("Manager should have workingState == NotWorking.",  manager.getWorkingState() == WorkingState.NotWorking);
		
		// First customer
		manager.msgIAmAvailableToAssist(employee);
		assertEquals("Manager log should have 3 entries.", manager.log.size(), 3);
		assertTrue("Manager log should have \"Manager received msgIAmAvailableToAssist\". The last event logged is " + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Manager received msgIAmAvailableToAssist"));
		assertTrue("Market employees should contain an employee with state == Available.", market.getEmployees().get(0).getMarketEmployeeState() == MarketEmployeeState.Available);	

		// Second
		manager.msgWhatWouldCustomerDeliveryLike(employee);
		assertEquals("Manager log should have 4 entries.", manager.log.size(), 4);
		assertTrue("Manager log should have \"Manager received msgWhatWouldCustomerDeliveryLike\". The last event logged is " + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Manager received msgWhatWouldCustomerDeliveryLike"));
		assertTrue("Market employees should contain an employee with state == GettingOrder.", market.getEmployees().get(0).getMarketEmployeeState() == MarketEmployeeState.GettingOrder);

		manager.runScheduler();
		assertEquals("Employee log should have 2 entries.", employee.log.size(), 2);
		assertTrue("Employee log should have \"Employee received msgHereIsCustomerDeliveryOrder\". The last event logged is " + employee.log.getLastLoggedEvent().toString(), employee.log.containsString("Employee received msgHereIsCustomerDeliveryOrder"));
		assertTrue("Market employees should contain an employee with state == CollectingItems.", market.getEmployees().get(0).getMarketEmployeeState() == MarketEmployeeState.CollectingItems);
//		assertEquals("Manager should have 0 customer in customer list.", market.getCustomers().size(), 0);

		manager.msgIAmAvailableToAssist(employee);
		assertEquals("Manager log should have 5 entries.", manager.log.size(), 5);
		assertTrue("Manager log should have \"Manager received msgIAmAvailableToAssist\". The last event logged is " + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Manager received msgIAmAvailableToAssist"));
		assertTrue("Market employees should contain an employee with state == Available.", market.getEmployees().get(0).getMarketEmployeeState() == MarketEmployeeState.Available);

//		assertEquals("Manager should be active.", manager.getActive(), true);
		manager.runScheduler();
		assertEquals("Manager should be inactive.", manager.getActive(), false);	
	}
}
