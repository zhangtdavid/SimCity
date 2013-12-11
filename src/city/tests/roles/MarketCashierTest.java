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
import city.buildings.interfaces.Bank;
import city.buildings.interfaces.Market;
import city.buildings.interfaces.Market.MyDeliveryPerson;
import city.roles.MarketCashierRole;
import city.roles.interfaces.MarketCashier.TransactionState;
import city.roles.interfaces.MarketCashier.WorkingState;
import city.tests.agents.mocks.MockPerson;
import city.tests.roles.mocks.MockBankManager;
import city.tests.roles.mocks.MockMarketCustomer;
import city.tests.roles.mocks.MockMarketCustomerDelivery;
import city.tests.roles.mocks.MockMarketCustomerDeliveryPayment;
import city.tests.roles.mocks.MockMarketDeliveryPerson;
import city.tests.roles.mocks.MockMarketEmployee;
import city.tests.roles.mocks.MockMarketManager;

public class MarketCashierTest extends TestCase {
	Market market;
	Bank bank;
	
	MockPerson cashierPerson;
	MarketCashierRole cashier;
	
	MockPerson cashierPerson2;
	MarketCashierRole cashier2;
	
	MockPerson customerPerson;
	MockMarketCustomer customer;
	
	MockPerson customerPerson2;
	MockMarketCustomer customer2;
	
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
	
	MockPerson bankManagerPerson;
	MockBankManager bankManager;
	
	
	Map<FOOD_ITEMS, Integer> orderItems;
	MarketOrder order;
	MarketOrder order2;
	MarketOrder order3;
	
	Map<FOOD_ITEMS, Integer> collectedItemsAll;
	Map<FOOD_ITEMS, Integer> collectedItemsPartial;
	
	public void setUp() throws Exception {
		super.setUp();
		CityMap.clearMap();

		bank = new BankBuilding("Bank", null, null);
		CityMap.addBuilding(BUILDING.bank, bank);

		market = new MarketBuilding("Market1", null, null);

		cashierPerson = new MockPerson("Cashier"); 
		cashier = new MarketCashierRole(market, 0, 12); // this constructs a bank customer, which requires a bank
		cashier.setPerson(cashierPerson);
		cashier.setMarket(market);
		cashier.setActive();
		
		// Used for shift change
		cashierPerson2 = new MockPerson("Cashier"); 
		cashier2 = new MarketCashierRole(market, 13, 24); // this constructs a bank customer, which requires a bank
		cashier2.setPerson(cashierPerson2);
		cashier2.setMarket(market);
		
		customerPerson = new MockPerson("Customer"); 
		customer = new MockMarketCustomer();
		customer.setPerson(customerPerson);
		customer.market = market;
		
		customerPerson2 = new MockPerson("Customer2"); 
		customer2 = new MockMarketCustomer();
		customer2.setPerson(customerPerson2);
		customer2.market = market;

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
		
		market.getDeliveryPeople().add(new MyDeliveryPerson(deliveryPerson));
		
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
		
		bankManagerPerson = new MockPerson("BankManager");
		bankManager = new MockBankManager();
		bankManager.setPerson(bankManagerPerson);
		bankManager.building = bank;
		
		bank.setManager(bankManager);
		
		orderItems = new HashMap<FOOD_ITEMS, Integer>();
		orderItems.put(FOOD_ITEMS.chicken, 5);
		orderItems.put(FOOD_ITEMS.pizza, 5);
		orderItems.put(FOOD_ITEMS.salad, 5);
		orderItems.put(FOOD_ITEMS.steak, 5);
		
		MarketOrder.setCurrentID(0);
		order = new MarketOrder(orderItems);
		order2 = new MarketOrder(orderItems);
		order3 = new MarketOrder(orderItems);
		
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
		assertEquals("Employee should have an empty log.", employee.log.size(), 0);
		assertEquals("Manager should have an empty log.", manager.log.size(), 0);
		assertEquals("Customer should have an empty log.", customer.log.size(), 0);
		assertEquals("CustomerDelivery should have an empty log.", customerDelivery.log.size(), 0);
		assertEquals("CustomerDeliveryPayment should have an empty log.", customerDeliveryPayment.log.size(), 0);
		assertEquals("Cashier should have an empty log.", cashier.log.size(), 0);
		assertEquals("Cashier should have 0 transactions.", cashier.getTransactions().size(), 0);
		assertEquals("DeliveryPerson should have an empty log.", deliveryPerson.log.size(), 0);
		assertEquals("Market money should be 1000. It's " + market.getCash() + "instead", market.getCash(), 1000);
		
		cashier.msgComputeBill(employee, customer, orderItems, collectedItemsAll, order.getOrderId());
		assertEquals("Cashier log should have 1 entry.", cashier.log.size(), 1);
		assertTrue("Cashier log should have \"Cashier received msgComputeBill\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgComputeBill"));
		assertEquals("Cashier should have 1 transaction.", cashier.getTransactions().size(), 1);
		assertTrue("Cashier transactions should contain a transaction with state == Pending.", cashier.getTransactions().get(0).getTransactionState() == TransactionState.Pending);
				
		cashier.runScheduler();
		assertTrue("Cashier transactions should contain a transaction with state == Calculating.", cashier.getTransactions().get(0).getTransactionState() == TransactionState.Calculating);
		assertEquals("Cashier transactions should contain a transaction with bill for 110", cashier.getTransactions().get(0).getBill(), 110);
		assertEquals("Customer log should have 1 entry.", customer.log.size(), 1);
		assertTrue("Customer log should have \"Customer received msgHereIsOrderandBill\". The last event logged is " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Customer received msgHereIsOrderandBill"));

		cashier.msgHereIsPayment(order.getOrderId(), 110);
		assertEquals("Cashier log should have 2 entries.", cashier.log.size(), 2);
		assertTrue("Cashier log should have \"Cashier received msgHereIsPayment\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgHereIsPayment"));
		assertEquals("Cashier payment variable should be 110.00. It's " + cashier.getTransactions().get(0).getPayment() + "instead", cashier.getTransactions().get(0).getPayment(), 110);
		assertTrue("Cashier transactions should contain a transaction with state == ReceivedPayment.", cashier.getTransactions().get(0).getTransactionState() == TransactionState.ReceivedPayment);	
		
		cashier.runScheduler();
		assertEquals("Customer log should have 2 entries.", customer.log.size(), 2);
		assertTrue("Customer log should have \"Customer received msgPaymentReceived from Market Cashier\". The last event logged is " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Customer received msgPaymentReceived from Market Cashier"));
		assertEquals("Market money should be 1110. It's " + market.getCash() + "instead", market.getCash(), 1110);
		assertEquals("Cashier should have 0 transactions.", cashier.getTransactions().size(), 0);
	}
	
	public void testNormCustomerDeliveryScenario() {
		assertEquals("Employee should have an empty log.", employee.log.size(), 0);
		assertEquals("Manager should have an empty log.", manager.log.size(), 0);
		assertEquals("Customer should have an empty log.", customer.log.size(), 0);
		assertEquals("CustomerDelivery should have an empty log.", customerDelivery.log.size(), 0);
		assertEquals("CustomerDeliveryPayment should have an empty log.", customerDeliveryPayment.log.size(), 0);
		assertEquals("Cashier should have an empty log.", cashier.log.size(), 0);
		assertEquals("Cashier should have 0 transactions.", cashier.getTransactions().size(), 0);
		assertEquals("DeliveryPerson should have an empty log.", deliveryPerson.log.size(), 0);
		assertEquals("Market money should be 1000. It's " + market.getCash() + "instead", market.getCash(), 1000);
		
		cashier.msgComputeBill(employee, customerDelivery, customerDeliveryPayment, orderItems, collectedItemsAll, order.getOrderId());
		assertEquals("Cashier log should have 1 entry.", cashier.log.size(), 1);
		assertTrue("Cashier log should have \"Cashier received msgComputeBill\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgComputeBill"));
		assertEquals("Cashier should have 1 transaction.", cashier.getTransactions().size(), 1);
		assertTrue("Cashier transactions should contain a transaction with state == Pending.", cashier.getTransactions().get(0).getTransactionState() == TransactionState.Pending);
		
		cashier.runScheduler();
		assertTrue("Cashier transactions should contain a transaction with state == Calculating.", cashier.getTransactions().get(0).getTransactionState() == TransactionState.Calculating);
		assertEquals("Cashier transactions should contain a transaction with bill for 110", cashier.getTransactions().get(0).getBill(), 110);
		assertEquals("CustomerDeliveryPayment log should have 1 entry.", customerDeliveryPayment.log.size(), 1);
		assertTrue("CustomerDeliveryPayment log should have \"CustomerDeliveryPayment received msgHereIsBill\". The last event logged is " + customerDeliveryPayment.log.getLastLoggedEvent().toString(), customerDeliveryPayment.log.containsString("CustomerDeliveryPayment received msgHereIsBill"));
		
		cashier.msgHereIsPayment(order.getOrderId(), 110);
		assertEquals("Cashier log should have 2 entries.", cashier.log.size(), 2);
		assertTrue("Cashier log should have \"Cashier received msgHereIsPayment\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgHereIsPayment"));
		assertEquals("Cashier payment variable should be 110. It's " + cashier.getTransactions().get(0).getPayment() + "instead", cashier.getTransactions().get(0).getPayment(), 110);
		assertTrue("Cashier transactions should contain a transaction with state == ReceivedPayment.", cashier.getTransactions().get(0).getTransactionState() == TransactionState.ReceivedPayment);	
		
		cashier.runScheduler();		
//		assertTrue("Cashier transactions should contain a transaction with state == PendingDelivery.", cashier.getTransactions().get(0).getTransactionState() == TransactionState.PendingDelivery); // following instructions execute too quickly
		assertEquals("CustomerDeliveryPayment log should have 2 entries.", customerDeliveryPayment.log.size(), 2);
		assertTrue("CustomerDeliveryPayment log should have \"CustomerDeliveryPayment received msgPaymentReceived from cashier\". The last event logged is " + customerDeliveryPayment.log.getLastLoggedEvent().toString(), customerDeliveryPayment.log.containsString("CustomerDeliveryPayment received msgPaymentReceived from cashier"));
		assertEquals("Market money should be 1110. It's " + market.getCash() + "instead", market.getCash(), 1110);
		assertEquals("Delivery Person log should have 1 entry.", deliveryPerson.log.size(), 1);
		assertTrue("Delivery Person log should have \"Delivery Person received msgDeliverOrder\". The last event logged is " + deliveryPerson.log.getLastLoggedEvent().toString(), deliveryPerson.log.containsString("Delivery Person received msgDeliverOrder"));
		assertTrue("Cashier transactions should contain a transaction with state == Delivering.", cashier.getTransactions().get(0).getTransactionState() == TransactionState.Delivering);
		
		cashier.msgDeliveringItems(deliveryPerson);
		assertEquals("Cashier log should have 3 entries.", cashier.log.size(), 3);
		assertTrue("Cashier log should have \"Cashier received msgDeliveringItems\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgDeliveringItems"));
		
		cashier.msgFinishedDeliveringItems(deliveryPerson, order.getOrderId());
		assertEquals("Cashier log should have 4 entries.", cashier.log.size(), 4);
		assertTrue("Cashier log should have \"Cashier received msgFinishedDeliveringItems\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgFinishedDeliveringItems"));
		assertEquals("Cashier should have 0 transactions.", cashier.getTransactions().size(), 0);
	}
	
	public void testBankDeposit() {
		market.setCash(1100);
		cashier.runScheduler(); // Sets bankCustomer active
		assertEquals("MarketBankCustomerRole should be active.", market.getBankCustomer().getActive(), true);
		assertTrue("MarketBankCustomerRole should have state == entering.",  market.getBankCustomer().getStateString() == "entering");
		
		cashier.runScheduler(); // Run directDeposit in bankCustomer
		
		market.getBankCustomer().msgDepositCompleted();
		assertTrue("MarketBankCustomerRole should have state == exit.",  market.getBankCustomer().getStateString() == "exit");
		assertEquals("MarketBankCustomerRole should be active.", market.getBankCustomer().getActive(), true);
		assertEquals("MarketBankCustomerRole should have activity.", market.getBankCustomer().getActivity(), true);

		cashier.runScheduler();
		assertTrue("MarketBankCustomerRole should have state == inProgress.",  market.getBankCustomer().getStateString() == "inProgress");
		assertEquals("Market money should be 500. It's " + market.getCash() + "instead", market.getCash(), 500);

	}
	
	public void testMultipleCustomersandShiftChange() {
		assertTrue("Cashier should have workingState == Working.",  cashier.getWorkingState() == WorkingState.Working);
		cashier.setInactive();
		assertTrue("Cashier should have workingState == GoingOffShift.",  cashier.getWorkingState() == WorkingState.GoingOffShift);
		
		// First customer
		cashier.msgComputeBill(employee, customer, orderItems, collectedItemsAll, order.getOrderId());
		assertEquals("Cashier log should have 1 entry.", cashier.log.size(), 1);
		assertTrue("Cashier log should have \"Cashier received msgComputeBill\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgComputeBill"));
		assertEquals("Cashier should have 1 transaction.", cashier.getTransactions().size(), 1);
		assertTrue("Cashier transactions should contain a transaction with state == Pending.", cashier.getTransactions().get(0).getTransactionState() == TransactionState.Pending);
	
		cashier.runScheduler();
		assertTrue("Cashier transactions should contain a transaction with state == Calculating.", cashier.getTransactions().get(0).getTransactionState() == TransactionState.Calculating);
		assertEquals("Cashier transactions should contain a transaction with bill for 110", cashier.getTransactions().get(0).getBill(), 110);
		assertEquals("Customer log should have 1 entry.", customer.log.size(), 1);
		assertTrue("Customer log should have \"Customer received msgHereIsOrderandBill\". The last event logged is " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Customer received msgHereIsOrderandBill"));

		// Second customer
		cashier.msgComputeBill(employee, customerDelivery, customerDeliveryPayment, orderItems, collectedItemsAll, order2.getOrderId());
		assertEquals("Cashier log should have 2 entries.", cashier.log.size(), 2);
		assertTrue("Cashier log should have \"Cashier received msgComputeBill\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgComputeBill"));
		assertEquals("Cashier should have 2 transactions.", cashier.getTransactions().size(), 2);
		assertTrue("Cashier transactions should contain a transaction with state == Pending.", cashier.getTransactions().get(1).getTransactionState() == TransactionState.Pending);

		cashier.runScheduler();
		assertTrue("Cashier transactions should contain a transaction with state == Calculating.", cashier.getTransactions().get(1).getTransactionState() == TransactionState.Calculating);
		assertEquals("Cashier transactions should contain a transaction with bill for 110", cashier.getTransactions().get(1).getBill(), 110);
		assertEquals("CustomerDeliveryPayment log should have 1 entry.", customerDeliveryPayment.log.size(), 1);
		assertTrue("CustomerDeliveryPayment log should have \"CustomerDeliveryPayment received msgHereIsBill\". The last event logged is " + customerDeliveryPayment.log.getLastLoggedEvent().toString(), customerDeliveryPayment.log.containsString("CustomerDeliveryPayment received msgHereIsBill"));
		
		assertEquals("Cashier should be active.", cashier.getActive(), true);
		
		// Change cashiers
		market.setCashier(cashier2);
		cashier2.setActive();
		cashier.runScheduler();
		assertTrue("Cashier should have workingState == NotWorking.",  cashier.getWorkingState() == WorkingState.NotWorking);
		
		// Attempted 3rd customer, shouldn't happen in normal application
		// First customer
		cashier.msgComputeBill(employee, customer2, orderItems, collectedItemsAll, order3.getOrderId());
		assertEquals("Cashier log should have 3 entries.", cashier.log.size(), 3);
		assertTrue("Cashier log should have \"Cashier received msgComputeBill\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgComputeBill"));
		assertEquals("Cashier should have 2 transactions.", cashier.getTransactions().size(), 2);
		
		cashier.msgHereIsPayment(order.getOrderId(), 110);
		assertEquals("Cashier log should have 4 entries.", cashier.log.size(), 4);
		assertTrue("Cashier log should have \"Cashier received msgHereIsPayment\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgHereIsPayment"));
		assertEquals("Cashier payment variable should be 110.00. It's " + cashier.getTransactions().get(0).getPayment() + "instead", cashier.getTransactions().get(0).getPayment(), 110);
		assertTrue("Cashier transactions should contain a transaction with state == ReceivedPayment.", cashier.getTransactions().get(0).getTransactionState() == TransactionState.ReceivedPayment);	

		cashier.runScheduler();
		assertEquals("Customer log should have 2 entries.", customer.log.size(), 2);
		assertTrue("Customer log should have \"Customer received msgPaymentReceived from Market Cashier\". The last event logged is " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Customer received msgPaymentReceived from Market Cashier"));
		assertEquals("Market money should be 1110. It's " + market.getCash() + "instead", market.getCash(), 1110);
		assertEquals("Cashier should have 1 transactions.", cashier.getTransactions().size(), 1);
		
		cashier.msgHereIsPayment(order2.getOrderId(), 110);
		assertEquals("Cashier log should have 5 entries.", cashier.log.size(), 5);
		assertTrue("Cashier log should have \"Cashier received msgHereIsPayment\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgHereIsPayment"));
		assertEquals("Cashier payment variable should be 110. It's " + cashier.getTransactions().get(0).getPayment() + "instead", cashier.getTransactions().get(0).getPayment(), 110);
		assertTrue("Cashier transactions should contain a transaction with state == ReceivedPayment.", cashier.getTransactions().get(0).getTransactionState() == TransactionState.ReceivedPayment);	

		cashier.runScheduler();
//		assertTrue("Cashier transactions should contain a transaction with state == PendingDelivery.", cashier.getTransactions().get(0).getTransactionState() == TransactionState.PendingDelivery); // following instructions execute too quickly
		assertEquals("CustomerDeliveryPayment log should have 2 entries.", customerDeliveryPayment.log.size(), 2);
		assertTrue("CustomerDeliveryPayment log should have \"CustomerDeliveryPayment received msgPaymentReceived from cashier\". The last event logged is " + customerDeliveryPayment.log.getLastLoggedEvent().toString(), customerDeliveryPayment.log.containsString("CustomerDeliveryPayment received msgPaymentReceived from cashier"));
		assertEquals("Market money should be 1220. It's " + market.getCash() + "instead", market.getCash(), 1220);
		assertEquals("Delivery Person log should have 1 entry.", deliveryPerson.log.size(), 1);
		assertTrue("Delivery Person log should have \"Delivery Person received msgDeliverOrder\". The last event logged is " + deliveryPerson.log.getLastLoggedEvent().toString(), deliveryPerson.log.containsString("Delivery Person received msgDeliverOrder"));
		assertTrue("Cashier transactions should contain a transaction with state == Delivering.", cashier.getTransactions().get(0).getTransactionState() == TransactionState.Delivering);

		cashier.msgDeliveringItems(deliveryPerson);
		assertEquals("Cashier log should have 6 entries.", cashier.log.size(), 6);
		assertTrue("Cashier log should have \"Cashier received msgDeliveringItems\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgDeliveringItems"));
		
		cashier.msgFinishedDeliveringItems(deliveryPerson, order2.getOrderId());
		assertEquals("Cashier log should have 7 entries.", cashier.log.size(), 7);
		assertTrue("Cashier log should have \"Cashier received msgFinishedDeliveringItems\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgFinishedDeliveringItems"));
		assertEquals("Cashier should have 0 transactions.", cashier.getTransactions().size(), 0);

		assertEquals("Cashier should be active.", cashier.getActive(), true);
		cashier.runScheduler();
		assertEquals("Cashier should be inactive.", cashier.getActive(), false);
	}
	
//	public void testNonNormCustomerDeliveryScenarioDeliveryPersonUnavailable() {
		// set delivery person state to unavailable
		// set delivery person state to available
		// run scheduler
		
//		assertEquals("Employee should have an empty log.", employee.log.size(), 0);
//		assertEquals("Manager should have an empty log.", manager.log.size(), 0);
//		assertEquals("Customer should have an empty log.", customer.log.size(), 0);
//		assertEquals("CustomerDelivery should have an empty log.", customerDelivery.log.size(), 0);
//		assertEquals("CustomerDeliveryPayment should have an empty log.", customerDeliveryPayment.log.size(), 0);
//		assertEquals("Cashier should have an empty log.", cashier.log.size(), 0);
//		assertEquals("Cashier should have 0 transactions.", cashier.transactions.size(), 0);
//		assertEquals("Market money should be 1000. It's " + market.money + "instead", market.money, 1000);
//		
//		cashier.msgNewDeliveryPerson(deliveryPerson);
//		assertEquals("Cashier log should have 1 entry.", cashier.log.size(), 1);
//		assertTrue("Cashier log should have \"Cashier received msgNewDeliveryPerson\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgNewDeliveryPerson"));
//		assertEquals("Cashier deliveryPeople should contain 1 Delivery Person.", cashier.deliveryPeople.size(), 1);
//		assertTrue("Cashier deliveryPeople should contain a Delivery Person with available == true.", cashier.deliveryPeople.get(0).available);
//		cashier.deliveryPeople.get(0).available = false;
//		assertTrue("Cashier deliveryPeople should contain a Delivery Person with available == false.", !cashier.deliveryPeople.get(0).available);
//		cashier.runScheduler();
//		
////		cashier.msgComputeBill(employee, customerDelivery, customerDeliveryPayment, order, collectedItemsAll);
//		assertEquals("Cashier log should have 2 entries.", cashier.log.size(), 2);
//		assertTrue("Cashier log should have \"Cashier received msgComputeBill\". The last event logged is " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgComputeBill"));
//		assertEquals("Cashier should have 1 transaction.", cashier.transactions.size(), 1);
//		assertTrue("Cashier transactions should contain a transaction with state == Pending.", cashier.transactions.get(0).s == TransactionState.Pending);
//				
//		cashier.runScheduler();
//		assertTrue("Cashier transactions should contain a transaction with state == Calculating.", cashier.transactions.get(0).s == TransactionState.Calculating);
//		assertEquals("Cashier transactions should contain a transaction with bill for 110", cashier.transactions.get(0).bill, 110);
//		assertEquals("CustomerDeliveryPayment log should have 1 entry.", customerDeliveryPayment.log.size(), 1);
//		assertTrue("CustomerDeliveryPayment log should have \"CustomerDeliveryPayment received msgHereIsBill\". The last event logged is " + customerDeliveryPayment.log.getLastLoggedEvent().toString(), customerDeliveryPayment.log.containsString("CustomerDeliveryPayment received msgHereIsBill"));
//
//		cashier.msgHereIsPayment(customerDeliveryPayment, 110);
//		assertEquals("Cashier log should have 3 entries.", cashier.log.size(), 3);
//		assertTrue("Cashier log should have \"Cashier received msgHereIsPayment\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgHereIsPayment"));
//		assertEquals("Cashier payment variable should be 110. It's " + cashier.transactions.get(0).payment + "instead", cashier.transactions.get(0).payment, 110);
//		assertTrue("Cashier transactions should contain a transaction with state == ReceivedPayment.", cashier.transactions.get(0).s == TransactionState.ReceivedPayment);	
//
//		cashier.runScheduler();
////		assertTrue("Cashier transactions should contain a transaction with state == PendingDelivery.", cashier.transactions.get(0).s == TransactionState.PendingDelivery); // following instructions execute too quickly
//		assertEquals("CustomerDeliveryPayment log should have 2 entries.", customerDeliveryPayment.log.size(), 2);
//		assertTrue("CustomerDeliveryPayment log should have \"CustomerDeliveryPayment received msgPaymentReceived from cashier\". The last event logged is " + customerDeliveryPayment.log.getLastLoggedEvent().toString(), customerDeliveryPayment.log.containsString("CustomerDeliveryPayment received msgPaymentReceived from cashier"));
//		assertEquals("Market money should be 1110. It's " + market.money + "instead", market.money, 1110);
//		assertTrue("Cashier transactions should contain a transaction with state == PendingDelivery.", cashier.transactions.get(0).s == TransactionState.PendingDelivery); // following instructions execute too quickly
//		
//		cashier.deliveryPeople.get(0).available = true;
//		assertTrue("Cashier deliveryPeople should contain a Delivery Person with available == true.", cashier.deliveryPeople.get(0).available);
//		
//		cashier.runScheduler();
//		assertTrue("Cashier transactions should contain a transaction with state == Delivering.", cashier.transactions.get(0).s == TransactionState.Delivering);
//		assertEquals("Delivery Person log should have 1 entry.", deliveryPerson.log.size(), 1);
//		assertTrue("Delivery Person log should have \"Delivery Person received msgDeliverOrder\". The last event logged is " + deliveryPerson.log.getLastLoggedEvent().toString(), deliveryPerson.log.containsString("Delivery Person received msgDeliverOrder"));
//		
//		cashier.msgDeliveringItems(deliveryPerson);
//		assertEquals("Cashier log should have 4 entries.", cashier.log.size(), 4);
//		assertTrue("Cashier log should have \"Cashier received msgDeliveringItems\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgDeliveringItems"));
//		assertTrue("Cashier deliveryPeople should contain a Delivery Person with available == false.", !cashier.deliveryPeople.get(0).available);
//		
//		cashier.msgFinishedDeliveringItems(deliveryPerson, customerDelivery);
//		assertEquals("Cashier log should have 5 entries.", cashier.log.size(), 5);
//		assertTrue("Cashier log should have \"Cashier received msgFinishedDeliveringItems\". The last event logged is actually " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Cashier received msgFinishedDeliveringItems"));
//		assertEquals("Cashier should have 0 transactions.", cashier.transactions.size(), 0);
//		assertTrue("Cashier deliveryPeople should contain a Delivery Person with available == true.", cashier.deliveryPeople.get(0).available);
//	}
	
//	public void testNonNormCustomerCollectedItemsPartialScenario() {
//		
//	}
//	
//	public void testNonNormCustomerNotEnoughMoneyScenario() {
//		
//	}
}
