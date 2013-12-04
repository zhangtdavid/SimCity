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
import city.buildings.HouseBuilding;
import city.buildings.MarketBuilding;
import city.gui.buildings.HousePanel;
import city.gui.buildings.MarketPanel;
import city.interfaces.MarketCustomer.MarketCustomerEvent;
import city.interfaces.MarketCustomer.MarketCustomerState;
import city.roles.MarketCustomerRole;
import city.tests.mock.MockLandlord;
import city.tests.mock.MockMarketCashier;
import city.tests.mock.MockMarketCustomerDelivery;
import city.tests.mock.MockMarketCustomerDeliveryPayment;
import city.tests.mock.MockMarketDeliveryPerson;
import city.tests.mock.MockMarketEmployee;
import city.tests.mock.MockMarketManager;
import city.tests.mock.MockPerson;
import junit.framework.TestCase;

public class MarketCustomerTest extends TestCase {
	MarketPanel marketPanel;	
	MarketBuilding market;
	
	HousePanel housePanel;
	
	MockPerson cashierPerson;
	MockMarketCashier cashier;
	
	MockPerson customerPerson;
	MarketCustomerRole customer;
//	MarketAnimatedCustomer marketCustomerGui;
	MockLandlord landlord;
	HouseBuilding home;
	
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
		
	Map<FOOD_ITEMS, Integer> orderItems;
	MarketOrder order;
	
	Map<FOOD_ITEMS, Integer> collectedItemsAll;
	Map<FOOD_ITEMS, Integer> collectedItemsPartial;
	
	public void setUp() throws Exception {
		super.setUp();
		Application.CityMap.addBuilding(BUILDING.bank, new BankBuilding("BankBuilding"));

		marketPanel = new MarketPanel(Color.blue, new Dimension(500, 500));
		market = new MarketBuilding("Market1", marketPanel);

		
		housePanel = new HousePanel(Color.black, new Dimension(500,500));
		
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
		customer = new MarketCustomerRole(order);
//		marketCustomerGui = new MarketCustomerAnimation(customer);
		customer.setPerson(customerPerson);
		customer.setMarket(market);
//		customer.setAnimation((Animation) marketCustomerGui);
		landlord = new MockLandlord();
		home = new HouseBuilding("House", landlord, housePanel);
		customerPerson.home = home;
		customerPerson.setCash(500);
		
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
		manager = new MockMarketManager();
		manager.setPerson(managerPerson);
		manager.market = market;

		market.setCashier(cashier);
		market.setManager(manager);
		market.addEmployee(employee);
	}
	
	public void testNormCustomerScenario() {
		assertEquals("Cashier should have an empty log.", cashier.log.size(), 0);
		assertEquals("Employee should have an empty log.", employee.log.size(), 0);
		assertEquals("Manager should have an empty log.", manager.log.size(), 0);
		assertEquals("Customer should have an empty log.", customer.log.size(), 0);
		assertEquals("CustomerDelivery should have an empty log.", customerDelivery.log.size(), 0);
		assertEquals("CustomerDeliveryPayment should have an empty log.", customerDeliveryPayment.log.size(), 0);
		assertEquals("DeliveryPerson should have an empty log.", deliveryPerson.log.size(), 0);
		assertTrue("Customer state should be None.", customer.getMarketCustomerState() == MarketCustomerState.None);
        
		customer.setActive();
		assertTrue("Customer event should be ArrivedAtMarket.", customer.getMarketCustomerEvent() == MarketCustomerEvent.ArrivedAtMarket);

		customer.runScheduler();
		assertTrue("Customer state should be WaitingForService.", customer.getMarketCustomerState() == MarketCustomerState.WaitingForService);
		assertEquals("Manager log should have 1 entry.", manager.log.size(), 1);
		assertTrue("Manager log should have \"Manager received msgIWouldLikeToPlaceAnOrder\". The last event logged is " + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Manager received msgIWouldLikeToPlaceAnOrder"));
	
		customer.msgWhatWouldYouLike(employee, 0);
		assertEquals("Customer log should have 1 entry.", customer.log.size(), 1);
		assertTrue("Customer log should have \"Customer received msgWhatWouldYouLike\". The last event logged is " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Customer received msgWhatWouldYouLike"));
		assertTrue("Customer event should be AskedForOrder.", customer.getMarketCustomerEvent() == MarketCustomerEvent.AskedForOrder);
		assertTrue("Customer employee should be employee.", customer.getEmployee() == employee);
		assertTrue("Customer loc should be 0.", customer.getLoc() == 0);
		
		customer.runScheduler();
		assertTrue("Customer state should be WaitingForOrder.", customer.getMarketCustomerState() == MarketCustomerState.WaitingForOrder);
		assertEquals("Employee log should have 1 entry.", employee.log.size(), 1);
		assertTrue("Employee log should have \"Employee received msgHereIsMyOrder\". The last event logged is actually " + employee.log.getLastLoggedEvent().toString(), employee.log.containsString("Employee received msgHereIsMyOrder"));

		customer.msgHereIsOrderandBill(collectedItemsAll, 110, order.orderId);
		assertEquals("Customer log should have 2 entries.", customer.log.size(), 2);
		assertTrue("Customer log should have \"Customer received msgHereIsOrderandBill\". The last event logged is " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Customer received msgHereIsOrderandBill"));
		assertTrue("Customer event should be OrderReady.", customer.getMarketCustomerEvent() == MarketCustomerEvent.OrderReady);
		for (FOOD_ITEMS item: collectedItemsAll.keySet()) {
			assertTrue("customer.receivedItems should be collectedItemsAll.", customer.getReceivedItems().get(item) == collectedItemsAll.get(item));
		}
		assertTrue("Customer bill should be 110.", customer.getBill() == 110);

		customer.runScheduler();
		assertTrue("Customer state should be Paying.", customer.getMarketCustomerState() == MarketCustomerState.Paying);
		assertEquals("Cashier log should have 1 entry.", cashier.log.size(), 1);
		assertTrue("Cashier log should have \"Cashier received msgHereIsPayment\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgHereIsPayment"));
		assertTrue("Customer cash should be 390.", customer.getPerson().getCash() == 390);

		customer.msgPaymentReceived();
		assertEquals("Customer log should have 3 entries.", customer.log.size(), 3);
		assertTrue("Customer log should have \"Customer received msgPaymentReceived\". The last event logged is " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Customer received msgPaymentReceived"));
		assertTrue("Customer event should be PaymentReceived.", customer.getMarketCustomerEvent() == MarketCustomerEvent.PaymentReceived);

		customer.runScheduler();
		assertTrue("Customer state should be None.", customer.getMarketCustomerState() == MarketCustomerState.None);
	}
}

