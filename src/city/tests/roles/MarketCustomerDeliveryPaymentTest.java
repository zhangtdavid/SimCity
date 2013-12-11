package city.tests.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import utilities.MarketOrder;
import utilities.MarketTransaction;
import utilities.MarketTransaction.MarketTransactionState;
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
import city.tests.agents.mocks.MockPerson;
import city.tests.roles.mocks.MockMarketCashier;
import city.tests.roles.mocks.MockMarketCustomer;
import city.tests.roles.mocks.MockMarketCustomerDelivery;
import city.tests.roles.mocks.MockMarketDeliveryPerson;
import city.tests.roles.mocks.MockMarketEmployee;
import city.tests.roles.mocks.MockMarketManager;

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
		CityMap.clearMap();

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
		customerDeliveryPayment = new MarketCustomerDeliveryPaymentRole(restaurantChung, marketTransactions, cashier);
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
		
		market.setCashier(cashier);
		market.setManager(manager);
		market.addEmployee(employee);
		
		orderItems = new HashMap<FOOD_ITEMS, Integer>();
		orderItems.put(FOOD_ITEMS.chicken, 5);
		orderItems.put(FOOD_ITEMS.pizza, 5);
		orderItems.put(FOOD_ITEMS.salad, 5);
		orderItems.put(FOOD_ITEMS.steak, 5);
		
		MarketOrder.setCurrentID(0);
		order = new MarketOrder(orderItems);
		marketTransactions.add(new MarketTransaction(market, order)); // models restaurantCashier getting addMarketOrder msg from restaurantCook 
		
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
		
		customerDeliveryPayment.msgHereIsBill(110, 0);
		assertEquals("CustomerDeliveryPayment log should have 1 entry. It doesn't", customerDeliveryPayment.log.size(), 1);
		assertTrue("CustomerDeliveryPayment log should have \"MarketCustomerDeliveryPayment received msgHereIsBill\". The last event logged is " + customerDeliveryPayment.log.getLastLoggedEvent().toString(), customerDeliveryPayment.log.containsString("MarketCustomerDeliveryPayment received msgHereIsBill"));
		assertEquals("CustomerDeliveryPayment should have 1 marketTransaction. It doesn't", customerDeliveryPayment.getMarketTransactions().size(), 1);
		assertTrue("CustomerDeliveryPayment transactions should contain a transaction with state == Processing. It doesn't.", customerDeliveryPayment.getMarketTransactions().get(0).getMarketTransactionState() == MarketTransactionState.Processing);
	
		customerDeliveryPayment.runScheduler();
		assertEquals("Cashier log should have 1 entry. It doesn't", cashier.log.size(), 1);
		assertTrue("Cashier log should have \"Cashier received msgHereIsPayment\". The last event logged is " + customerDeliveryPayment.log.getLastLoggedEvent().toString(), cashier.log.containsString("MarketCashier received msgHereIsPayment"));
		assertEquals("RestaurantChung money should be 690. It's " + restaurantChung.getCash() + "instead", restaurantChung.getCash(), 690);
		assertTrue("CustomerDeliveryPayment transactions should contain a transaction with state == WaitingForConfirmation. It doesn't.", customerDeliveryPayment.getMarketTransactions().get(0).getMarketTransactionState() == MarketTransactionState.WaitingForConfirmation);
		
		customerDeliveryPayment.msgPaymentReceived(0);
		assertEquals("CustomerDeliveryPayment log should have 2 entries. It doesn't", customerDeliveryPayment.log.size(), 2);
		assertTrue("CustomerDeliveryPayment log should have \"MarketCustomerDeliveryPayment received msgPaymentReceived\". The last event logged is " + customerDeliveryPayment.log.getLastLoggedEvent().toString(), customerDeliveryPayment.log.containsString("MarketCustomerDeliveryPayment received msgPaymentReceived"));
	}
}
