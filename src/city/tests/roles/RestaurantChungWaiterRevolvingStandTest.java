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
import city.buildings.interfaces.RestaurantChung.MyCustomer;
import city.buildings.interfaces.RestaurantChung.MyCustomer.CheckState;
import city.buildings.interfaces.RestaurantChung.MyCustomer.OrderStatus;
import city.buildings.interfaces.RestaurantChung.MyCustomer.WaiterCustomerState;
import city.buildings.interfaces.RestaurantChung.MyWaiter;
import city.gui.interiors.RestaurantChungPanel;
import city.roles.RestaurantChungWaiterRevolvingStandRole;
import city.tests.agents.mocks.MockPerson;
import city.tests.animations.mocks.MockRestaurantChungAnimatedWaiter;
import city.tests.roles.mocks.MockMarketManager;
import city.tests.roles.mocks.MockRestaurantChungCashier;
import city.tests.roles.mocks.MockRestaurantChungCook;
import city.tests.roles.mocks.MockRestaurantChungCustomer;
import city.tests.roles.mocks.MockRestaurantChungHost;
import city.tests.roles.mocks.MockRestaurantChungWaiterMessageCook;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Monroe Ekilah
 */
public class RestaurantChungWaiterRevolvingStandTest extends TestCase {
	Bank bank;
	RestaurantChung restaurantChung;
	Market market;
	
	MockPerson cashierPerson;
	MockRestaurantChungCashier cashier;

	MockPerson cookPerson;
	MockRestaurantChungCook cook;
	
	MockPerson customerPerson;
	MockRestaurantChungCustomer customer;
	
	MockPerson hostPerson;
	MockRestaurantChungHost host;
	
	MockPerson waiterMCPerson;
	MockRestaurantChungWaiterMessageCook waiterMC;
	
	MockPerson waiterRSPerson;	
	RestaurantChungWaiterRevolvingStandRole waiterRS;
	MockRestaurantChungAnimatedWaiter waiterAnim;
	
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
		cook = new MockRestaurantChungCook();
		cook.setPerson(cookPerson);

		customerPerson = new MockPerson("Customer"); 
		customer = new MockRestaurantChungCustomer();
		customer.setPerson(customerPerson);
		customer.setRestaurant(restaurantChung);
		
		hostPerson = new MockPerson("Host"); 
		host = new MockRestaurantChungHost();
		host.setPerson(hostPerson);
		
		waiterMCPerson = new MockPerson("WaiterMC"); 
		waiterMC = new MockRestaurantChungWaiterMessageCook();
		waiterMC.setPerson(waiterMCPerson);
		
		waiterRSPerson = new MockPerson("WaiterRS");
		waiterRS = new RestaurantChungWaiterRevolvingStandRole(restaurantChung, 0, 12);
		waiterRS.setPerson(waiterRSPerson);
		waiterAnim = new MockRestaurantChungAnimatedWaiter(waiterRS);
		waiterRS.setAnimation(waiterAnim);		
		waiterAnim.addTable(RestaurantChungPanel.TABLEX, RestaurantChungPanel.TABLEY);
		
		marketManagerPerson = new MockPerson("MarketManager");
		marketManager = new MockMarketManager();
		marketManager.setPerson(marketManagerPerson);	
		
		restaurantChung.setRestaurantChungCashier(cashier);
		restaurantChung.setRestaurantChungCook(cook);
		restaurantChung.setRestaurantChungHost(host);
		restaurantChung.getWaiters().add(new MyWaiter(waiterRS));
		restaurantChung.getCustomers().add(new MyCustomer(customer));		
		
		market.setManager(marketManager);
		
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
	/**
	 * This tests the cook ordering food when low.
	 */
	public void testOneNormalCustomerScenario() {
		assertEquals("Cashier should have an empty log. It doesn't", cashier.log.size(), 0);
		assertEquals("Cook should have an empty log. It doesn't", cook.log.size(), 0);
		assertEquals("Customer should have an empty log. It doesn't", customer.log.size(), 0);
		assertEquals("Host should have an empty log. It doesn't", host.log.size(), 0);
		assertEquals("WaiterMC should have an empty log. It doesn't", waiterMC.log.size(), 0);
		assertEquals("WaiterRS should have an empty log. It doesn't", waiterRS.log.size(), 0);

		restaurantChung.setFoodQuantity(FOOD_ITEMS.chicken, 5);
		restaurantChung.setFoodQuantity(FOOD_ITEMS.pizza, 5);
		restaurantChung.setFoodQuantity(FOOD_ITEMS.salad, 5);
		restaurantChung.setFoodQuantity(FOOD_ITEMS.steak, 5);
		
		waiterRS.msgSitAtTable(customer, 1);
		assertEquals("Customer waiter should be waiterRS.", restaurantChung.findCustomer(customer).getWaiter(), waiterRS);
		assertEquals("Customer table should be 1.", restaurantChung.findCustomer(customer).getTable(), 1);
		assertEquals("Customer state should be Waiting.", restaurantChung.findCustomer(customer).getWaiterCustomerState(), WaiterCustomerState.Waiting);
		
		waiterRS.runScheduler();
		assertEquals("Host log should have 1 entry. It doesn't", host.log.size(), 1);
		assertTrue("Host log should have \"RestaurantChungHost received msgTakingCustomerToTable\". The last event logged is " + host.log.getLastLoggedEvent().toString(), host.log.containsString("RestaurantChungHost received msgTakingCustomerToTable"));
		assertEquals("Customer state should be Seated.", restaurantChung.findCustomer(customer).getWaiterCustomerState(), WaiterCustomerState.Seated);
		assertEquals("Customer log should have 1 entry. It doesn't", customer.log.size(), 1);
		assertTrue("Customer log should have \"RestaurantChungCustomer received msgFollowMeToTable\". The last event logged is " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("RestaurantChungCustomer received msgFollowMeToTable"));

		waiterRS.msgReadyToOrder(customer);
		assertEquals("Customer state should be ReadyToOrder.", restaurantChung.findCustomer(customer).getWaiterCustomerState(), WaiterCustomerState.ReadyToOrder);

		waiterRS.runScheduler();
		assertEquals("Customer log should have 2 entries. It doesn't", customer.log.size(), 2);
		assertTrue("Customer log should have \"RestaurantChungCustomer received msgWhatWouldYouLike\". The last event logged is " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("RestaurantChungCustomer received msgWhatWouldYouLike"));
		assertEquals("Customer state should be Asked.", restaurantChung.findCustomer(customer).getWaiterCustomerState(), WaiterCustomerState.Asked);

		waiterRS.msgHereIsMyOrder(customer, FOOD_ITEMS.steak);
		assertEquals("Order status should be Ordered.", restaurantChung.findCustomer(customer).getOrderStatus(), OrderStatus.Ordered);
		assertEquals("Customer choice should be steak ", restaurantChung.findCustomer(customer).getChoice(), FOOD_ITEMS.steak);

		waiterRS.runScheduler();
		assertEquals("Order stand should have 1 order. It doesn't", restaurantChung.getOrderStand().getOrderList().size(), 1);
		assertEquals("Order status should be Cooking.", restaurantChung.findCustomer(customer).getOrderStatus(), OrderStatus.Cooking);

		waiterRS.msgOrderIsReady(FOOD_ITEMS.steak, 1);
		assertEquals("Order status should be DoneCooking.", restaurantChung.findCustomer(customer).getOrderStatus(), OrderStatus.DoneCooking);

		waiterRS.runScheduler();
		assertEquals("Order status should be PickedUp.", restaurantChung.findCustomer(customer).getOrderStatus(), OrderStatus.PickedUp);

		waiterRS.runScheduler();
		assertEquals("Order status should be Delivered.", restaurantChung.findCustomer(customer).getOrderStatus(), OrderStatus.Delivered);
		assertEquals("Customer log should have 3 entries. It doesn't", customer.log.size(), 3);
		assertTrue("Customer log should have \"RestaurantChungCustomer received msgHereIsYourFood\". The last event logged is " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("RestaurantChungCustomer received msgHereIsYourFood"));
		assertEquals("Customer state should be Eating.", restaurantChung.findCustomer(customer).getWaiterCustomerState(), WaiterCustomerState.Eating);

		waiterRS.msgGetCheck(customer);
		assertEquals("Customer state should be WaitingForCheck.", restaurantChung.findCustomer(customer).getWaiterCustomerState(), WaiterCustomerState.WaitingForCheck);
		
		waiterRS.runScheduler();
		assertEquals("Check state should be WaitingForCheck.", restaurantChung.findCustomer(customer).getCheckState(), CheckState.AskedForBill);
		assertEquals("Cashier log should have 1 entry. It doesn't", cashier.log.size(), 1);
		assertTrue("Cashier log should have \"RestaurantChungCashier received msgComputeBill\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("RestaurantChungCashier received msgComputeBill"));

		waiterRS.msgHereIsBill(customer, 16);
		assertEquals("Check state should be ReceivedBill.", restaurantChung.findCustomer(customer).getCheckState(), CheckState.ReceivedBill);
		assertEquals("Customer bill should be 16.", restaurantChung.findCustomer(customer).getBill(), 16);
		
		waiterRS.runScheduler();
		assertEquals("Check state should be DeliveredBill.", restaurantChung.findCustomer(customer).getCheckState(), CheckState.DeliveredBill);
		assertEquals("Customer log should have 4 entries. It doesn't", customer.log.size(), 4);
		assertTrue("Customer log should have \"Received msgHereIsCheck\". The last event logged is " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received msgHereIsCheck"));
		assertEquals("Host log should have 2 entries. It doesn't", host.log.size(), 2);
		assertTrue("Host log should have \"RestaurantChungHost received msgTableIsFree\". The last event logged is " + host.log.getLastLoggedEvent().toString(), host.log.containsString("RestaurantChungHost received msgTableIsFree"));
		
		
//		cook.runScheduler();
//		assertEquals("Cook should have 1 marketOrder.", cook.getMarketOrders().size(), 1);
//		assertTrue("Cook marketOrders should contain a marketOrder with state == Pending.", cook.getMarketOrders().get(0).getMarketOrderState() == MarketOrderState.Pending);
//
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		cook.runScheduler();
//		assertTrue("Cook marketOrders should contain a marketOrder with state == Ordered.", cook.getMarketOrders().get(0).getMarketOrderState() == MarketOrderState.Ordered);
//		assertEquals("Cook should have 1 marketCustomerDeliveryRole.", cook.getMarketCustomerDeliveryRoles().size(), 1);
//		assertTrue("MarketCustomerDeliveryRole should have state == Ordering.", ((MarketCustomerDelivery) cook.getMarketCustomerDeliveryRoles().get(0)).getState() == MarketCustomerState.Ordering);
//		
//		cook.runScheduler();
//		assertTrue("MarketCustomerDeliveryRole should have state == None.", ((MarketCustomerDelivery) cook.getMarketCustomerDeliveryRoles().get(0)).getState() == MarketCustomerState.None);
//		
//		((MarketCustomerDelivery) cook.getMarketCustomerDeliveryRoles().get(0)).msgHereIsOrderDelivery(collectedItemsAll, 1);
//
//		cook.runScheduler();
//		
//		// Account for checking order stand time
//		try {
//			Thread.sleep(6000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		cook.runScheduler();
//		assertEquals("Cook should have 0 marketCustomerDeliveryRole.", cook.getMarketCustomerDeliveryRoles().size(), 0);		
	}	
}
