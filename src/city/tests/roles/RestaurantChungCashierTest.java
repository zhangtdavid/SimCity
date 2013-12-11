package city.tests.roles;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import utilities.MarketOrder;
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
import city.roles.RestaurantChungCashierRole;
import city.roles.interfaces.RestaurantChungCashier.TransactionState;
import city.tests.agents.mocks.MockPerson;
import city.tests.roles.mocks.MockBankManager;
import city.tests.roles.mocks.MockRestaurantChungCook;
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
public class RestaurantChungCashierTest extends TestCase {
	Bank bank;
	RestaurantChung restaurantChung;
	Market market;

	MockPerson bankManagerPerson;
	MockBankManager bankManager;
	
	MockPerson cashierPerson;
	RestaurantChungCashierRole cashier;
	
	MockPerson cookPerson;
	MockRestaurantChungCook cook;
	
	MockPerson customerPerson;
	MockRestaurantChungCustomer customer;
	
	MockPerson hostPerson;
	MockRestaurantChungHost host;
	
	MockPerson waiterMCPerson;
	MockRestaurantChungWaiterMessageCook waiterMC;
	
	MockPerson waiterRSPerson;	
	MockRestaurantChungWaiterRevolvingStand waiterRS;
	
	MockPerson abnormCustomerPerson;
	MockRestaurantChungCustomer abnormCustomer;

	Map<FOOD_ITEMS, Integer> orderItems;
	MarketOrder order;
	
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
		
		bankManagerPerson = new MockPerson("BankManager");
		bankManager = new MockBankManager();
		bankManager.setPerson(bankManagerPerson);
		
		cashierPerson = new MockPerson("Cashier");
		cashier = new RestaurantChungCashierRole(restaurantChung, 0, 12);
		cashier.setPerson(cashierPerson);
		cashier.setMarketCustomerDeliveryPaymentPerson();
		
		cookPerson = new MockPerson("Cook");
		cook = new MockRestaurantChungCook();
		cook.setPerson(cookPerson);

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
		
		abnormCustomerPerson = new MockPerson("AbnormCustomer"); 
		abnormCustomer = new MockRestaurantChungCustomer();
		abnormCustomer.setPerson(abnormCustomerPerson);
		
		bank.setManager(bankManager);

		restaurantChung.setRestaurantChungCashier(cashier);
		restaurantChung.setRestaurantChungCook(cook);
		restaurantChung.setRestaurantChungHost(host);
		
		orderItems = new HashMap<FOOD_ITEMS, Integer>();
		orderItems.put(FOOD_ITEMS.chicken, 5);
		orderItems.put(FOOD_ITEMS.pizza, 5);
		orderItems.put(FOOD_ITEMS.salad, 5);
		orderItems.put(FOOD_ITEMS.steak, 5);

		order = new MarketOrder(orderItems);
	}	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	public void testOneNormalCustomerScenario()	{
		assertEquals("Cashier should have an empty log. It doesn't", cashier.log.size(), 0);
		assertEquals("Customer should have an empty log. It doesn't", customer.log.size(), 0);
		assertEquals("WaiterMessageCook should have an empty log. It doesn't", waiterMC.log.size(), 0);
		assertEquals("Cashier should have 0 transactions. It doesn't", cashier.getTransactions().size(), 0);

		cashier.msgComputeBill(waiterMC, customer, FOOD_ITEMS.steak);
		assertEquals("Cashier log should have 1 entry. It doesn't", cashier.log.size(), 1);
		assertTrue("Cashier log should have \"Cashier received msgComputeBill\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgComputeBill"));
		assertEquals("Cashier should have 1 transaction. It doesn't", cashier.getTransactions().size(), 1);
		assertTrue("Cashier transactions should contain a transaction with state == Pending. It doesn't.",
		cashier.getTransactions().get(0).getTransactionState() == TransactionState.Pending);
		
		cashier.runScheduler();
		assertTrue("Cashier transactions should contain a transaction with state == Calculating. It doesn't.",
		cashier.getTransactions().get(0).getTransactionState() == TransactionState.Calculating);
		assertEquals("Cashier transactions should contain a transaction with price 16", cashier.getTransactions().get(0).getPrice(), 16);
		assertEquals("Waiter log should have 1 entry. It doesn't", waiterMC.log.size(), 1);
		assertTrue("Waiter log should have \"Waiter received msgHereIsBill\". The last event logged is " + waiterMC.log.getLastLoggedEvent().toString(), waiterMC.log.containsString("Waiter received msgHereIsBill"));
		
		cashier.msgHereIsPayment(customer, 16);
		assertEquals("Cashier log should have 2 entries. It doesn't", cashier.log.size(), 2);
		assertTrue("Cashier log should have \"Cashier received msgHereIsPayment\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgHereIsPayment"));
		assertEquals("Cashier payment variable should be 16. It's " + cashier.getTransactions().get(0).getPayment() + "instead", cashier.getTransactions().get(0).getPayment(), 16);
		assertTrue("Cashier transactions should contain a transaction with state == ReceivedPayment. It doesn't.",
		cashier.getTransactions().get(0).getTransactionState() == TransactionState.ReceivedPayment);	
		assertEquals("Cashier money should be 816. It's " + restaurantChung.getCash() + "instead", restaurantChung.getCash(), 816);
		
		cashier.runScheduler();
		assertEquals("Cashier should have 0 transaction. It doesn't", cashier.getTransactions().size(), 0);

		assertEquals("Customer log should have 1 entry. It doesn't", 1, customer.log.size());
		assertTrue("Customer log should have \"Customer received msgHereIsChange from cashier\". The last event logged is " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Customer received msgHereIsChange from cashier"));

		cashier.runScheduler();
		assertEquals("Cashier should have 0 transactions. It doesn't", cashier.getTransactions().size(), 0);		
	}
	
	public void testOneAbnormalCustomerScenario() {
		assertEquals("Cashier should have 800. It doesn't", restaurantChung.getCash(), 800);
		assertEquals("Cashier should have an empty log. It doesn't", cashier.log.size(), 0);
		assertEquals("Customer should have an empty log. It doesn't", abnormCustomer.log.size(), 0);
		assertEquals("Waiter should have an empty log. It doesn't", waiterMC.log.size(), 0);
		assertEquals("Host should have an empty log. It doesn't", host.log.size(), 0);
		assertEquals("Cashier should have 0 transactions. It doesn't", cashier.getTransactions().size(), 0);
				
		cashier.msgComputeBill(waiterMC, abnormCustomer, FOOD_ITEMS.steak);
		assertEquals("Cashier log should have 1 entry. It doesn't", cashier.log.size(), 1);
		assertTrue("Cashier log should have \"Cashier received msgComputeBill\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgComputeBill"));
		assertEquals("Cashier should have 1 transaction. It doesn't", cashier.getTransactions().size(), 1);
		assertTrue("Cashier transactions should contain a transaction with state == Pending. It doesn't.",
		cashier.getTransactions().get(0).getTransactionState() == TransactionState.Pending);
		
		cashier.runScheduler();
		assertTrue("Cashier transactions should contain a transaction with state == Calculating. It doesn't.",
		cashier.getTransactions().get(0).getTransactionState() == TransactionState.Calculating);
		assertEquals("Cashier transactions should contain a transaction with price 16", cashier.getTransactions().get(0).getPrice(), 16);
		assertEquals("Waiter log should have 1 entry. It doesn't", waiterMC.log.size(), 1);
		assertTrue("Waiter log should have \"Waiter received msgHereIsBill\". The last event logged is " + waiterMC.log.getLastLoggedEvent().toString(), waiterMC.log.containsString("Waiter received msgHereIsBill"));
		
		cashier.msgHereIsPayment(abnormCustomer, 0);
		assertEquals("Cashier log should have 2 entries. It doesn't", cashier.log.size(), 2);
		assertTrue("Cashier log should have \"Cashier received msgHereIsPayment\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgHereIsPayment"));
		assertEquals("Cashier payment variable should be 0. It's " + cashier.getTransactions().get(0).getPayment() + "instead", cashier.getTransactions().get(0).getPayment(), 0);
		assertTrue("Cashier transactions should contain a transaction with state == ReceivedPayment. It doesn't.",
		cashier.getTransactions().get(0).getTransactionState() == TransactionState.ReceivedPayment);	
		assertEquals("Cashier should have 800. It doesn't", restaurantChung.getCash(), 800);
		
		cashier.runScheduler();
		assertTrue("Cashier transactions should contain a transaction with state == InsufficientPayment. It doesn't.",
		cashier.getTransactions().get(0).getTransactionState() == TransactionState.InsufficientPayment);

		assertEquals("Abnorm Customer log should have 1 entry. It doesn't", abnormCustomer.log.size(), 1); // TODO Something is up here
		assertTrue("Abnorm Customer log should have \"Customer received msgHereIsChange from cashier\". The last event logged is " + abnormCustomer.log.getLastLoggedEvent().toString(), abnormCustomer.log.containsString("Customer received msgHereIsChange from cashier"));

		cashier.runScheduler();
		assertTrue("Cashier transactions should contain a transaction with state == NotifiedHost. It doesn't.",
		cashier.getTransactions().get(0).getTransactionState() == TransactionState.NotifiedHost);
		assertEquals("Host log should have 1 entry. It doesn't", host.log.size(), 1);
		assertTrue("Host log should have \"Host received msgFlakeAlert\". The last event logged is " + host.log.getLastLoggedEvent().toString(), host.log.containsString("Host received msgFlakeAlert"));
		assertEquals("Cashier should note 16 in debt. It doesn't", cashier.getTransactions().get(0).getPrice()-cashier.getTransactions().get(0).getPayment(), 16);
		assertEquals("Cashier should still have 1 transaction. It doesn't", cashier.getTransactions().size(), 1);
	}

	public void testOneNormalMarketScenario() {
		assertEquals("Cashier should have 800. It doesn't", restaurantChung.getCash(), 800);
		assertEquals("Cashier should have an empty log. It doesn't", cashier.log.size(), 0);
		assertEquals("Cashier should have 0 market transactions. It doesn't", cashier.getMarketTransactions().size(), 0);

		cashier.msgAddMarketOrder(market, order);
		assertEquals("Cashier log should have 1 entry. It doesn't", cashier.log.size(), 1);
		assertTrue("Cashier log should have \"Cashier received msgAddMarketOrder\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgAddMarketOrder"));
		assertEquals("Cashier should have 1 marketTransaction. It doesn't", cashier.getMarketTransactions().size(), 1);
		assertTrue("Cashier transactions should contain a transaction with state == Pending. It doesn't.", cashier.getMarketTransactions().get(0).getMarketTransactionState() == MarketTransactionState.Pending);
		
		// MarketCustomerDeliveryPaymentRole handles payment
		market.setCash(890);
		cashier.runScheduler();
		
//		cashier.msgMarketOrderBill(market, 0, 30.0);
//		assertEquals("Cashier log should have 1 entry. It doesn't", 1, cashier.log.size());
//		assertTrue("Cashier log should have \"Cashier received msgMarketOrderBill\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgMarketOrderBill"));
//		assertEquals("Cashier should have 1 market transaction. It doesn't", cashier.marketTransactions.size(), 1);
//		assertTrue("Cashier market transactions should contain a transaction with state == Pending. It doesn't.",
//		cashier.marketTransactions.get(0).s == TransactionState.Pending);
//		assertEquals("Cashier market transactions should contain a transaction with bill 30.0", cashier.marketTransactions.get(0).bill, 30.0);
//		
//		cashier.runScheduler();
//		assertEquals("Cashier should have 0 market transactions. It doesn't", cashier.marketTransactions.size(), 0);
//		assertEquals("Market log should have 1 entry. It doesn't", 1, market.log.size());
//		assertTrue("Market log should have \"Market received msgHereIsPayment from Cashier\". The last event logged is " + market.log.getLastLoggedEvent().toString(), market.log.containsString("Market received msgHereIsPayment from Cashier"));
//		assertEquals("Cashier should have 470.0. It doesn't", cashier.money, 470.0);
	}
		
//	public void testOneNormalAndOneAbnormalCustomerScenario() {
//	//setUp() runs first before this test!
//	assertEquals("Cashier should have 500.0. It doesn't", cashier.money, 500.0);
//	assertEquals("Cashier should have an empty log. It doesn't", cashier.log.size(), 0);
//	assertEquals("Customer should have an empty log. It doesn't", customer.log.size(), 0);
//	assertEquals("Customer should have an empty log. It doesn't", abnormCustomer.log.size(), 0);
//	assertEquals("Waiter should have an empty log. It doesn't", waiter.log.size(), 0);
//	assertEquals("Cashier should have 0 transactions. It doesn't", cashier.transactions.size(), 0);
//		
//	cashier.msgComputeBill(waiter, customer, "Steak");
//	assertEquals("Cashier log should have 1 entry. It doesn't", cashier.log.size(), 1);
//	assertTrue("Cashier log should have \"Cashier received msgComputeBill\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgComputeBill"));
//	assertEquals("Cashier should have 1 transaction. It doesn't", cashier.transactions.size(), 1);
//	assertTrue("Cashier transactions should contain a transaction with state == Pending. It doesn't.",
//	cashier.transactions.get(0).s == TransactionState.Pending);
//
//	cashier.msgComputeBill(waiter, abnormCustomer, "Steak");
//	assertEquals("Cashier log should have 2 entries. It doesn't", cashier.log.size(), 2);
//	assertTrue("Cashier log should have \"Cashier received msgComputeBill\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgComputeBill"));
//	assertEquals("Cashier should have 1 transaction. It doesn't", cashier.transactions.size(), 1);
//	assertTrue("Cashier transactions should contain a transaction with state == Pending. It doesn't.",
//	cashier.transactions.get(1).getTransactionState() == TransactionState.Pending);
//	
//	cashier.runScheduler();
//	assertTrue("Cashier transactions should contain a transaction with state == Calculating. It doesn't.",
//	cashier.transactions.get(0).getTransactionState() == TransactionState.Calculating);
//	assertEquals("Cashier transactions should contain a transaction with price 15.99", cashier.transactions.get(0).price, 15.99);
//	assertEquals("Waiter log should have 1 entry. It doesn't", 1, waiter.log.size());
//	assertTrue("Waiter log should have \"Waiter received msgHereIsBill\". The last event logged is " + waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Waiter received msgHereIsBill"));
//	
//	cashier.pickAndExecuteAnAction();
//	assertTrue("Cashier transactions should contain a transaction with state == Calculating. It doesn't.",
//	cashier.transactions.get(1).getTransactionState() == TransactionState.Calculating);
//	assertEquals("Cashier transactions should contain a transaction with price 15.99", cashier.transactions.get(1).price, 15.99);
//	assertEquals("Waiter log should have 2 entries. It doesn't", 2, waiter.log.size());
//	assertTrue("Waiter log should have \"Waiter received msgHereIsBill\". The last event logged is " + waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Waiter received msgHereIsBill"));
//
//	cashier.msgHereIsPayment(customer, 15.99);
//	assertEquals("Cashier log should have 3 entries. It doesn't", cashier.log.size(), 3);
//	assertTrue("Cashier log should have \"Cashier received msgHereIsPayment\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgHereIsPayment"));
//	assertEquals("Cashier payment variable should be 15.99. It's " + cashier.transactions.get(0).payment + "instead", cashier.transactions.get(0).payment, 15.99);
//	assertTrue("Cashier transactions should contain a transaction with state == ReceivedPayment. It doesn't.",
//	cashier.transactions.get(0).getTransactionState() == TransactionState.ReceivedPayment);	
//	assertEquals("Cashier money should be 515.99. It's " + cashier.money + "instead", cashier.money, 515.99);
//	
//	cashier.runScheduler();
//	assertTrue("Cashier transactions should contain a transaction with state == Done. It doesn't.",
//	cashier.transactions.get(0).getTransactionState() == TransactionState.Done);
//	assertEquals("Cashier should give 0.00 in change. It doesn't", cashier.transactions.get(0).payment-cashier.transactions.get(0).price, 0.0);
//
//	assertEquals("Customer log should have 1 entry. It doesn't", 1, customer.log.size());
//	assertTrue("Customer log should have \"Customer received msgHereIsChange from cashier\". The last event logged is " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Customer received msgHereIsChange from cashier"));
//
//	cashier.msgHereIsPayment(customer, 0.0);
//	assertEquals("Cashier log should have 4 entries. It doesn't", cashier.log.size(), 4);
//	assertTrue("Cashier log should have \"Cashier received msgHereIsPayment\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgHereIsPayment"));
//	assertEquals("Cashier payment variable should be 0.0. It's " + cashier.transactions.get(1).payment + "instead", cashier.transactions.get(1).payment, 0.0);
//	assertTrue("Cashier transactions should contain a transaction with state == ReceivedPayment. It doesn't.",
//	cashier.transactions.get(1).getTransactionState() == TransactionState.ReceivedPayment);	
//	assertEquals("Cashier should have 519.99. It doesn't", cashier.money, 515.99);
//
//	assertEquals("Abnorm Customer log should have 1 entry. It doesn't", 1, abnormCustomer.log.size());
//	assertTrue("Abnorm Customer log should have \"Customer received msgHereIsChange from cashier\". The last event logged is " + abnormCustomer.log.getLastLoggedEvent().toString(), abnormCustomer.log.containsString("Customer received msgHereIsChange from cashier"));
//
//	cashier.runScheduler();
//	assertTrue("Cashier transactions should contain a transaction with state == InsufficientPayment. It doesn't.",
//	cashier.transactions.get(1).getTransactionState() == TransactionState.InsufficientPayment);
//
//	cashier.runScheduler();
//	assertTrue("Cashier transactions should contain a transaction with state == NotifiedHost. It doesn't.",
//	cashier.transactions.get(0).getTransactionState() == TransactionState.NotifiedHost);
//	assertEquals("Host log should have 1 entry. It doesn't", host.log.size(), 1);
//	assertTrue("Host log should have \"Host received msgFlakeAlert\". The last event logged is " + host.log.getLastLoggedEvent().toString(), host.log.containsString("Host received msgFlakeAlert"));
//	assertEquals("Cashier should note 15.99 in debt. It doesn't", cashier.transactions.get(1).price-cashier.transactions.get(1).payment, 15.99);
//	assertEquals("Cashier should still have 2 transactions. It doesn't", cashier.transactions.size(), 2);
//	
//	cashier.runScheduler();
//	assertEquals("Cashier should have 1 transactions. It doesn't", cashier.transactions.size(), 1);
//}
	
//	
//	public void testTwoNormalMarketScenario() {
//		assertEquals("Cashier should have 500.0. It doesn't", cashier.money, 500.0);
//		assertEquals("Cashier should have an empty log. It doesn't", cashier.log.size(), 0);
//		assertEquals("Market should have an empty log. It doesn't", market.log.size(), 0);
//		assertEquals("Market2 should have an empty log. It doesn't", market2.log.size(), 0);
//		assertEquals("Cashier should have 0 market transactions. It doesn't", cashier.marketTransactions.size(), 0);
//
//		cashier.msgMarketOrderBill(market, 0, 30.0);
//		assertEquals("Cashier log should have 1 entry. It doesn't", 1, cashier.log.size());
//		assertTrue("Cashier log should have \"Cashier received msgMarketOrderBill\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgMarketOrderBill"));
//		assertEquals("Cashier should have 1 market transaction. It doesn't", cashier.marketTransactions.size(), 1);
//		assertTrue("Cashier market transactions should contain a transaction with state == Pending. It doesn't.",
//		cashier.marketTransactions.get(0).s == TransactionState.Pending);
//		assertEquals("Cashier market transactions should contain a transaction with bill 30.0", cashier.marketTransactions.get(0).bill, 30.0);
//		cashier.msgMarketOrderBill(market2, 0, 100.0);
//		assertEquals("Cashier log should have 2 entries. It doesn't", 2, cashier.log.size());
//		assertTrue("Cashier log should have \"Cashier received msgMarketOrderBill\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgMarketOrderBill"));
//		assertEquals("Cashier should have 2 market transactions. It doesn't", cashier.marketTransactions.size(), 2);
//		assertTrue("Cashier market transactions should contain a transaction with state == Pending. It doesn't.",
//		cashier.marketTransactions.get(1).s == TransactionState.Pending);
//		assertEquals("Cashier market transactions should contain a transaction with bill 100.0", cashier.marketTransactions.get(1).bill, 100.0);
//		
//		cashier.runScheduler();
//		assertEquals("Cashier should have 1 market transaction. It doesn't", cashier.marketTransactions.size(), 1);
//		assertEquals("Market log should have 1 entry. It doesn't", 1, market.log.size());
//		assertTrue("Market log should have \"Market received msgHereIsPayment from Cashier\". The last event logged is " + market.log.getLastLoggedEvent().toString(), market.log.containsString("Market received msgHereIsPayment from Cashier"));
//		assertEquals("Cashier should have 470.0. It doesn't", cashier.money, 470.0);
//		
//		cashier.runScheduler();
//		assertEquals("Cashier should have 0 market transactions. It doesn't", cashier.marketTransactions.size(), 0);
//		assertEquals("Market2 log should have 1 entry. It doesn't", 1, market2.log.size());
//		assertTrue("Market2 log should have \"Market received msgHereIsPayment from Cashier\". The last event logged is " + market2.log.getLastLoggedEvent().toString(), market2.log.containsString("Market received msgHereIsPayment from Cashier"));
//		assertEquals("Cashier should have 370.0. It doesn't", cashier.money, 370.0);
//	}
//	
//	public void testOneNormalCustomerandOneNormalMarketScenario() {
//		assertEquals("Cashier should have 500.0. It doesn't", cashier.money, 500.0);
//		
//		assertEquals("Cashier should have an empty log. It doesn't", cashier.log.size(), 0);
//		assertEquals("Customer should have an empty log. It doesn't", customer.log.size(), 0);
//		assertEquals("Waiter should have an empty log. It doesn't", waiter.log.size(), 0);
//		assertEquals("Market should have an empty log. It doesn't", market.log.size(), 0);
//		assertEquals("Cashier should have 0 market transactions. It doesn't", cashier.marketTransactions.size(), 0);
//
//		cashier.msgComputeBill(waiter, customer, "Steak");
//		assertEquals("Cashier log should have 1 entry. It doesn't", 1, cashier.log.size());
//		assertTrue("Cashier log should have \"Cashier received msgComputeBill\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgComputeBill"));
//		assertEquals("Cashier should have 1 transaction. It doesn't", cashier.transactions.size(), 1);
//		assertTrue("Cashier transactions should contain a transaction with state == Pending. It doesn't.",
//		cashier.transactions.get(0).s == TransactionState.Pending);
//
//		cashier.msgMarketOrderBill(market, 0, 30.0);
//		assertEquals("Cashier log should have 2 entries. It doesn't", 2, cashier.log.size());
//		assertTrue("Cashier log should have \"Cashier received msgMarketOrderBill\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgMarketOrderBill"));
//		assertEquals("Cashier should have 1 market transaction. It doesn't", cashier.marketTransactions.size(), 1);
//		assertTrue("Cashier market transactions should contain a transaction with state == Pending. It doesn't.",
//		cashier.marketTransactions.get(0).s == TransactionState.Pending);
//		assertEquals("Cashier market transactions should contain a transaction with bill 30.0", cashier.marketTransactions.get(0).bill, 30.0);
//
//		cashier.runScheduler();
//		assertTrue("Cashier transactions should contain a transaction with state == Calculating. It doesn't.",
//		cashier.transactions.get(0).s == TransactionState.Calculating);
//		assertEquals("Cashier should still have 1 market transaction. It doesn't", cashier.marketTransactions.size(), 1);
//		assertTrue("Cashier market transactions should contain a transaction with state == Pending. It doesn't.",
//		cashier.marketTransactions.get(0).s == TransactionState.Pending);
//		assertEquals("Cashier transactions should contain a transaction with price 15.99", cashier.transactions.get(0).price, 15.99);
//		assertEquals("Waiter log should have 1 entry. It doesn't", 1, waiter.log.size());
//		assertTrue("Waiter log should have \"Waiter received msgHereIsBill\". The last event logged is " + waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Waiter received msgHereIsBill"));
//	
//		cashier.runScheduler();
//		assertEquals("Cashier should have 0 market transactions. It doesn't", cashier.marketTransactions.size(), 0);
//		assertEquals("Cashier should have 1 transaction. It doesn't", cashier.transactions.size(), 1);
//		assertEquals("Market log should have 1 entry. It doesn't", 1, market.log.size());
//		assertTrue("Market log should have \"Market received msgHereIsPayment from Cashier\". The last event logged is " + market.log.getLastLoggedEvent().toString(), market.log.containsString("Market received msgHereIsPayment from Cashier"));
//		assertEquals("Cashier should have 470.0. It doesn't", cashier.money, 470.0);
//
//		cashier.msgHereIsPayment(customer, 15.99);
//		assertEquals("Cashier log should have 3 entries. It doesn't", 3, cashier.log.size());
//		assertTrue("Cashier log should have \"Cashier received msgHereIsPayment\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgHereIsPayment"));
//		assertEquals("Cashier payment variable should be 15.99. It's " + cashier.transactions.get(0).payment + "instead", cashier.transactions.get(0).payment, 15.99);
//		assertTrue("Cashier transactions should contain a transaction with state == ReceivedPayment. It doesn't.",
//		cashier.transactions.get(0).s == TransactionState.ReceivedPayment);	
//		assertEquals("Cashier money should be 485.99. It's " + cashier.money + "instead", cashier.money, 485.99);
//		
//		cashier.runScheduler();
//		assertTrue("Cashier transactions should contain a transaction with state == Done. It doesn't.",
//		cashier.transactions.get(0).s == TransactionState.Done);
//		assertEquals("Cashier should give 0.00 in change. It doesn't", cashier.transactions.get(0).payment-cashier.transactions.get(0).price, 0.0);
//
//		assertEquals("Customer log should have 1 entry. It doesn't", 1, customer.log.size());
//		assertTrue("Customer log should have \"Customer received msgHereIsChange from cashier\". The last event logged is " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Customer received msgHereIsChange from cashier"));
//
//		cashier.runScheduler();
//		assertEquals("Cashier should have 0 transactions. It doesn't", cashier.transactions.size(), 0);
//	}
}
