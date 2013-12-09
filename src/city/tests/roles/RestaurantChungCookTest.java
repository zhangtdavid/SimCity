package city.tests.roles;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import utilities.MarketOrder;
import city.Application.BUILDING;
import city.Application.CityMap;
import city.Application.FOOD_ITEMS;
import city.buildings.BankBuilding;
import city.buildings.MarketBuilding;
import city.buildings.RestaurantChungBuilding;
import city.buildings.interfaces.Bank;
import city.buildings.interfaces.Market;
import city.buildings.interfaces.RestaurantChung;
import city.roles.RestaurantChungCookRole;
import city.roles.interfaces.MarketCustomerDelivery;
import city.roles.interfaces.MarketCustomerDelivery.MarketCustomerState;
import city.roles.interfaces.RestaurantChungCook.MarketOrderState;
import city.tests.agents.mocks.MockPerson;
import city.tests.animations.mocks.MockRestaurantChungAnimatedCook;
import city.tests.roles.mocks.MockMarketManager;
import city.tests.roles.mocks.MockRestaurantChungCashier;
import city.tests.roles.mocks.MockRestaurantChungCustomer;
import city.tests.roles.mocks.MockRestaurantChungHost;
import city.tests.roles.mocks.MockRestaurantChungWaiterMessageCook;
import city.tests.roles.mocks.MockRestaurantChungWaiterRevolvingStand;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Monroe Ekilah
 */
public class RestaurantChungCookTest extends TestCase {
	Bank bank;
	RestaurantChung restaurantChung;
	Market market;
	
	MockPerson cashierPerson;
	MockRestaurantChungCashier cashier;

	MockPerson cookPerson;
	RestaurantChungCookRole cook;
	MockRestaurantChungAnimatedCook cookAnim;
	
	MockPerson customerPerson;
	MockRestaurantChungCustomer customer;
	
	MockPerson hostPerson;
	MockRestaurantChungHost host;
	
	MockPerson waiterMCPerson;
	MockRestaurantChungWaiterMessageCook waiterMC;
	
	MockPerson waiterRSPerson;	
	MockRestaurantChungWaiterRevolvingStand waiterRS;
	
	MockPerson marketManagerPerson;
	MockMarketManager marketManager;
	
	Map<FOOD_ITEMS, Integer> orderItems;
	MarketOrder order;
	
	Map<FOOD_ITEMS, Integer> collectedItemsAll;
	Map<FOOD_ITEMS, Integer> collectedItemsPartial;

	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();
		CityMap.clearMap();
		
		// Bank must come first so the restaurant and market can create bankCustomerRoles
		bank = new BankBuilding("Bank", null, null);		
		CityMap.addBuilding(BUILDING.bank, bank);
		
		restaurantChung = new RestaurantChungBuilding("RestaurantChung", null, null);
		CityMap.addBuilding(BUILDING.restaurant, restaurantChung);
		
		market = new MarketBuilding("Market", null, null);
		CityMap.addBuilding(BUILDING.market, market);
	
		cashierPerson = new MockPerson("Cashier");
		cashier = new MockRestaurantChungCashier(restaurantChung);
		cashier.setPerson(cashierPerson);
		cashier.setMarketCustomerDeliveryPaymentPerson();
		
		cookPerson = new MockPerson("Cook");
		cook = new RestaurantChungCookRole(restaurantChung, 0, 12);
		cookAnim = new MockRestaurantChungAnimatedCook(cook);
		cook.setPerson(cookPerson);
		cook.setAnimation(cookAnim);

		customerPerson = new MockPerson("Customer"); 
		customer = new MockRestaurantChungCustomer();
		customer.setPerson(customerPerson);
		
		hostPerson = new MockPerson("Host"); 
		host = new MockRestaurantChungHost();
		host.setPerson(hostPerson);
		
		waiterMCPerson = new MockPerson("WaiterMC"); 
		waiterMC = new MockRestaurantChungWaiterMessageCook();
		waiterMC.setPerson(waiterMCPerson);
		
		waiterRSPerson = new MockPerson("WaiterRS");
		waiterRS = new MockRestaurantChungWaiterRevolvingStand();
		waiterRS.setPerson(waiterRSPerson);
		
		marketManagerPerson = new MockPerson("MarketManager");
		marketManager = new MockMarketManager();
		marketManager.setPerson(marketManagerPerson);	
		
		restaurantChung.setRestaurantChungCashier(cashier);
		restaurantChung.setRestaurantChungCook(cook);
		restaurantChung.setRestaurantChungHost(host);
		
		market.setManager(marketManager);
		
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
	/**
	 * This tests the cook ordering food when low.
	 */
	public void testOrderFoodWhenLow() {
		assertEquals("Cashier should have an empty log. It doesn't", cashier.log.size(), 0);
		assertEquals("Cook should have an empty log. It doesn't", cook.log.size(), 0);
		assertEquals("Customer should have an empty log. It doesn't", customer.log.size(), 0);
		assertEquals("Host should have an empty log. It doesn't", host.log.size(), 0);
		assertEquals("WaiterMC should have an empty log. It doesn't", waiterMC.log.size(), 0);
		assertEquals("WaiterRS should have an empty log. It doesn't", waiterRS.log.size(), 0);
		assertEquals("Cook should have 0 orders.", cook.getOrders().size(), 0);
		assertEquals("Cook should have 0 marketCustomerDeliveryRoles.", cook.getMarketCustomerDeliveryRoles().size(), 0);
		assertEquals("Cook should have 0 marketOrders.", cook.getMarketOrders().size(), 0);

		cook.msgHereIsAnOrder(waiterMC, FOOD_ITEMS.steak, 1);
		assertEquals("Cook log should have 1 entry.", cook.log.size(), 1);
		assertTrue("Cook log should have \"RestaurantChungCook received msgHereIsAnOrder\". The last event logged is " + cook.log.getLastLoggedEvent().toString(), cook.log.containsString("RestaurantChungCook received msgHereIsAnOrder"));
		assertEquals("Cook should have 1 orders.", cook.getOrders().size(), 1);
		
		restaurantChung.setFoodQuantity(FOOD_ITEMS.chicken, 5);
		restaurantChung.setFoodQuantity(FOOD_ITEMS.pizza, 5);
		restaurantChung.setFoodQuantity(FOOD_ITEMS.salad, 5);
		restaurantChung.setFoodQuantity(FOOD_ITEMS.steak, 5);
		
		cook.runScheduler();
		assertEquals("Cook should have 1 marketOrder.", cook.getMarketOrders().size(), 1);
		assertTrue("Cook marketOrders should contain a marketOrder with state == Pending.", cook.getMarketOrders().get(0).getMarketOrderState() == MarketOrderState.Pending);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		cook.runScheduler();
		assertTrue("Cook marketOrders should contain a marketOrder with state == Ordered.", cook.getMarketOrders().get(0).getMarketOrderState() == MarketOrderState.Ordered);
		assertEquals("Cook should have 1 marketCustomerDeliveryRole.", cook.getMarketCustomerDeliveryRoles().size(), 1);
		assertTrue("MarketCustomerDeliveryRole should have state == Ordering.", ((MarketCustomerDelivery) cook.getMarketCustomerDeliveryRoles().get(0)).getState() == MarketCustomerState.Ordering);
		
		cook.runScheduler();
		assertTrue("MarketCustomerDeliveryRole should have state == None.", ((MarketCustomerDelivery) cook.getMarketCustomerDeliveryRoles().get(0)).getState() == MarketCustomerState.None);
		
		((MarketCustomerDelivery) cook.getMarketCustomerDeliveryRoles().get(0)).msgHereIsOrderDelivery(collectedItemsAll, 1);

		cook.runScheduler();
		
		// Account for checking order stand time
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		cook.runScheduler();
		assertEquals("Cook should have 0 marketCustomerDeliveryRole.", cook.getMarketCustomerDeliveryRoles().size(), 0);		
	}	
}
