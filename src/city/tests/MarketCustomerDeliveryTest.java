package city.tests;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import utilities.MarketOrder;
import city.Application;
import city.Application.BUILDING;
import city.Application.FOOD_ITEMS;
import city.buildings.MarketBuilding;
import city.buildings.RestaurantBaseBuilding;
import city.buildings.RestaurantChungBuilding;
import city.gui.CityViewRestaurant;
import city.gui.MarketPanel;
import city.gui.RestaurantChungPanel;
import city.roles.MarketCustomerDeliveryRole;
import city.roles.MarketCustomerDeliveryRole.MarketCustomerState;
import city.tests.mock.MockMarketCashier;
import city.tests.mock.MockMarketCustomer;
import city.tests.mock.MockMarketCustomerDeliveryPayment;
import city.tests.mock.MockMarketDeliveryPerson;
import city.tests.mock.MockMarketEmployee;
import city.tests.mock.MockMarketManager;
import city.tests.mock.MockPerson;
import junit.framework.TestCase;

public class MarketCustomerDeliveryTest extends TestCase {	
	MarketPanel marketPanel;	
	MarketBuilding market;
	
	RestaurantChungPanel restaurantChungPanel;	
	RestaurantBaseBuilding restaurant;
	
	MockPerson cashierPerson;
	MockMarketCashier cashier;
	
	MockPerson customerPerson;
	MockMarketCustomer customer;
	
	MockPerson customerDeliveryPerson;
	MarketCustomerDeliveryRole customerDelivery;

	MockPerson customerDeliveryPaymentPerson;
	MockMarketCustomerDeliveryPayment customerDeliveryPayment;
	
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
		marketPanel = new MarketPanel(Color.blue, new Dimension(500, 500));
		market = new MarketBuilding("Market1", marketPanel);

		restaurantChungPanel = new RestaurantChungPanel(Color.red, new Dimension(500, 500));
		restaurant = new RestaurantChungBuilding("RestuarantChung", restaurantChungPanel, new CityViewRestaurant(0, 0));

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
		
		cashierPerson = new MockPerson("Cashier"); 
		cashier = new MockMarketCashier();
		cashier.setPerson(cashierPerson);
		cashier.market = market;
		
		customerPerson = new MockPerson("Customer");
		customer = new MockMarketCustomer();
		customer.setPerson(customerPerson);
		customer.market = market;
		
		customerDeliveryPerson = new MockPerson("CustomerDelivery"); 
		customerDelivery = new MarketCustomerDeliveryRole(restaurant, order, customerDeliveryPayment);
		customerDelivery.setPerson(customerDeliveryPerson);
		customerDelivery.setMarket(market);
		
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
		manager = new MockMarketManager();
		manager.setPerson(managerPerson);
		manager.market = market;

		market.cashier = cashier;
		market.manager = manager;
		market.addEmployee(employee);
	}
	
	public void testNormCustomerDeliveryScenario() {
		assertEquals("Cashier should have an empty log.", cashier.log.size(), 0);
		assertEquals("Employee should have an empty log.", employee.log.size(), 0);
		assertEquals("Manager should have an empty log.", manager.log.size(), 0);
		assertEquals("Customer should have an empty log.", customer.log.size(), 0);
		assertEquals("CustomerDelivery should have an empty log.", customerDelivery.log.size(), 0);
		assertEquals("CustomerDeliveryPayment should have an empty log.", customerDeliveryPayment.log.size(), 0);
		assertEquals("DeliveryPerson should have an empty log.", deliveryPerson.log.size(), 0);
		assertTrue("CustomerDelivery state should be None.", customerDelivery.state == MarketCustomerState.None);
        
		customerDelivery.setActive();
		assertTrue("CustomerDelivery state should be Ordering.", customerDelivery.state == MarketCustomerState.Ordering);

		customerDelivery.runScheduler();		
		assertTrue("CustomerDelivery state should be None.", customerDelivery.state == MarketCustomerState.None);
		assertEquals("Manager log should have 1 entry.", manager.log.size(), 1);
		assertTrue("Manager log should have \"Manager received msgIWouldLikeToPlaceADeliveryOrder\". The last event logged is " + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Manager received msgIWouldLikeToPlaceADeliveryOrder"));
		assertTrue("CustomerDelivery active should be false", !customerDelivery.getActive());
//		assertTrue("CustomerDeliveryPerson runScheduler() should return false", !customerDeliveryPerson.runScheduler());
		assertTrue("CustomerDelivery runScheduler() should return false", !customerDelivery.runScheduler());
//		assertTrue("CustomerDelivery activity should be false", !customerDelivery.getActivity());
	}
}

