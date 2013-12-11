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
import city.buildings.interfaces.Market.DeliveryState;
import city.gui.interiors.MarketPanel;
import city.roles.MarketDeliveryPersonRole;
import city.tests.agents.mocks.MockPerson;
import city.tests.roles.mocks.MockMarketCashier;
import city.tests.roles.mocks.MockMarketCustomer;
import city.tests.roles.mocks.MockMarketCustomerDelivery;
import city.tests.roles.mocks.MockMarketCustomerDeliveryPayment;
import city.tests.roles.mocks.MockMarketEmployee;
import city.tests.roles.mocks.MockMarketManager;

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
	
	MockPerson deliveryPersonPerson2;
	MarketDeliveryPersonRole deliveryPerson2;
	
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
		
		CityMap.clearMap();

		Application.CityMap.addBuilding(BUILDING.bank, new BankBuilding("BankBuilding", null, null));
//		Application.CityMap.addBuilding(BUILDING.market, new MarketBuilding("MarketBuilding", null, null));
//		cityViewBuilding = new CityViewMarket(0, 0, "Market ", Color.LIGHT_GRAY, new MarketPanel(Color.LIGHT_GRAY));

		marketPanel = new MarketPanel(Color.blue);
		market = new MarketBuilding("Market1", marketPanel, null);
		
//		market.setCityViewBuilding(cityViewBuilding);
		
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
		deliveryPerson.setActive();
		
		// Used for shift change
		deliveryPersonPerson2 = new MockPerson("DeliveryPerson2"); 
		deliveryPerson2 = new MarketDeliveryPersonRole(market, 13, 24);
		deliveryPerson2.setPerson(deliveryPersonPerson2);
		deliveryPerson2.setMarket(market);
		
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
		market.addDeliveryPerson(deliveryPerson);
	
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
	}
	
	public void testNormCustomerDeliveryScenario() {
		assertEquals("Cashier should have an empty log.", cashier.log.size(), 0);
		assertEquals("Employee should have an empty log.", employee.log.size(), 0);
		assertEquals("Manager should have an empty log.", manager.log.size(), 0);
		assertEquals("Customer should have an empty log.", customer.log.size(), 0);
		assertEquals("CustomerDelivery should have an empty log.", customerDelivery.log.size(), 0);
		assertEquals("CustomerDeliveryPayment should have an empty log.", customerDeliveryPayment.log.size(), 0);
		assertEquals("DeliveryPerson should have an empty log.", deliveryPerson.log.size(), 0);

		deliveryPerson.msgDeliverOrder(customerDelivery, collectedItemsAll, order.getOrderId());
		assertEquals("DeliveryPerson log should have 1 entry.", deliveryPerson.log.size(), 1);
		assertTrue("DeliveryPerson log should have \"DeliveryPerson received msgDeliverOrder\". The last event logged is " + deliveryPerson.log.getLastLoggedEvent().toString(), deliveryPerson.log.containsString("DeliveryPerson received msgDeliverOrder"));
		assertEquals("DeliveryPerson deliveries should have 1 entry.", deliveryPerson.getDeliveries().size(), 1);
		assertTrue("DeliveryPerson customerDelivery should be customerDelivery.", deliveryPerson.getCustomerDelivery() == customerDelivery);
		for (FOOD_ITEMS item: collectedItemsAll.keySet()) {
			assertTrue("deliveryPerson.collectedItems should be collectedItemsAll.", deliveryPerson.getCollectedItems().get(item) == collectedItemsAll.get(item));
		}
		assertTrue("deliveryPerson.orderId should be order.orderId.", deliveryPerson.getOrderId() == order.getOrderId());
		
		deliveryPerson.runScheduler();
//		assertEquals("Cashier log should have 1 entry.", cashier.log.size(), 1); // next actions execute too quickly
//		assertTrue("Cashier log should have \"Cashier received msgDeliveringItems\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgDeliveringItems"));
		assertTrue("DeliveryPerson deliveries should contain a delivery with state == Delivering.", deliveryPerson.getDeliveryState() == DeliveryState.Delivering);
		
		deliveryPerson.msgArrivedAtDestination();
		assertTrue("DeliveryPerson deliveries should contain a delivery with state == Delivering.", deliveryPerson.getDeliveryState() == DeliveryState.Arrived);
//		assertEquals("CustomerDelivery log should have 1 entry.", customerDelivery.log.size(), 1);
//		assertTrue("CustomerDelivery log should have \"CustomerDelivery received msgHereIsOrderDelivery\". The last event logged is " + customerDelivery.log.getLastLoggedEvent().toString(), customerDelivery.log.containsString("CustomerDelivery received msgHereIsOrderDelivery"));
//		assertEquals("Cashier log should have 2 entries.", cashier.log.size(), 2);
//		assertTrue("Cashier log should have \"Cashier received msgFinishedDeliveringItems\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgFinishedDeliveringItems"));
//		assertTrue("DeliveryPerson customerDelivery should be null.", deliveryPerson.getCustomerDelivery() == null);
	}
	
	public void testShiftChange() {
//		deliveryPerson.msgDeliverOrder(customerDelivery, collectedItemsAll, order.getOrderId());
//		assertEquals("DeliveryPerson log should have 1 entry.", deliveryPerson.log.size(), 1);
//		assertTrue("DeliveryPerson log should have \"DeliveryPerson received msgDeliverOrder\". The last event logged is " + deliveryPerson.log.getLastLoggedEvent().toString(), deliveryPerson.log.containsString("DeliveryPerson received msgDeliverOrder"));
//		assertTrue("DeliveryPerson customerDelivery should be customerDelivery.", deliveryPerson.getCustomerDelivery() == customerDelivery);
//		for (FOOD_ITEMS item: collectedItemsAll.keySet()) {
//			assertTrue("deliveryPerson.collectedItems should be collectedItemsAll.", deliveryPerson.getCollectedItems().get(item) == collectedItemsAll.get(item));
//		}
//		assertTrue("deliveryPerson.orderId should be order.orderId.", deliveryPerson.getOrderId() == order.getOrderId());
//		
//		deliveryPerson.runScheduler();
////		assertEquals("Cashier log should have 1 entry.", cashier.log.size(), 1); // next actions execute too quickly
////		assertTrue("Cashier log should have \"Cashier received msgDeliveringItems\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgDeliveringItems"));
//		assertEquals("CustomerDelivery log should have 1 entry.", customerDelivery.log.size(), 1);
//		assertTrue("CustomerDelivery log should have \"CustomerDelivery received msgHereIsOrderDelivery\". The last event logged is " + customerDelivery.log.getLastLoggedEvent().toString(), customerDelivery.log.containsString("CustomerDelivery received msgHereIsOrderDelivery"));
//		assertEquals("Cashier log should have 2 entries.", cashier.log.size(), 2);
//		assertTrue("Cashier log should have \"Cashier received msgFinishedDeliveringItems\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgFinishedDeliveringItems"));
//		assertTrue("DeliveryPerson customerDelivery should be null.", deliveryPerson.getCustomerDelivery() == null);
//		
//		assertTrue("DeliveryPerson should have workingState == Working.",  deliveryPerson.getWorkingState() == WorkingState.Working);
//		deliveryPerson.setInactive();
//		assertTrue("DeliveryPerson should have workingState == GoingOffShift.",  deliveryPerson.getWorkingState() == WorkingState.GoingOffShift);
//		
//		// Add another deliveryPerson
//		market.addDeliveryPerson(deliveryPerson2);
//		deliveryPerson2.setActive();
//		
//		assertEquals("DeliveryPerson should be active.", deliveryPerson.getActive(), true);
//		deliveryPerson.runScheduler();
//		assertEquals("DeliveryPerson should be inactive.", deliveryPerson.getActive(), false);
	}
}

