package city.tests;


import junit.framework.TestCase;
import city.Application.FOOD_ITEMS;
import city.buildings.RestaurantChoiBuilding;
import city.interfaces.RestaurantChoiCustomer;
import city.interfaces.RestaurantChoiWaiter;
import city.roles.RestaurantChoiCashierRole;
import city.roles.RestaurantChoiCashierRole.Check;
import city.tests.mock.MockPerson;
import city.tests.mock.MockRestaurantChoiCustomer;
import city.tests.mock.MockRestaurantChoiWaiter;

public class RestaurantChoiCashierTest extends TestCase{
	//these are instantiated for each test separately via the setUp() method.
	RestaurantChoiCashierRole cashier;
	MockPerson p;
	MockRestaurantChoiWaiter waiter;
	MockRestaurantChoiCustomer customer;
	MockRestaurantChoiCustomer customer1;
	RestaurantChoiBuilding building;
	//MockMarket market;
	//MockMarket market1;
	//MockMarket market2;

	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		p = new MockPerson("person-cashier");
		cashier = new RestaurantChoiCashierRole(); // cashier has no name		
		customer = new MockRestaurantChoiCustomer("mockcustomer");
		customer1 = new MockRestaurantChoiCustomer("mockcustomer1");
		waiter = new MockRestaurantChoiWaiter("mockwaiter");
		//building = new RestaurantChoiBuilding("restaurant1", null);
		//building.addRole(cashier);
		p.addRole(cashier);
		cashier.setPerson(p);
		/*market = new MockMarket("mockmarket");
		market1 = new MockMarket("mockmarket1");
		market2 = new MockMarket("mockmarket2");
		banker = new MockBanker("mockbanker");
		cashier.setBanker(banker);
		banker.setCashier(cashier);*/
	}	/*
	public void testOneMarket(){
		System.out.println("------------TESTING ONE MARKET ACCEPTABLE PAYMENT SCENARIO");
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.checks.size(), 0);
		assertTrue("Cashier should have nothing in marketBills pertaining to market.", cashier.marketBills.get(market) == null);
		assertEquals("CashierAgent should have an empty event log before anything. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		cashier.addMarket(market);
		cashier.money = 1000;
		market.money = 0;
		cashier.msgHeresMarketBill(market, 2, 1); // cashier needs to pay market
		assertTrue("Market should have money=0 before cashier pays but doesn't",market.money == 0);
		assertTrue("Cashier should have 1000 money", cashier.money == 1000);		
		assertTrue("Cashier should have something in marketBills.", cashier.marketBills.size() > 0);
		assertTrue("Cashier should have something in marketBills with key market > $1.", cashier.marketBills.get(market) > 1);
		assertTrue("Cashier should just take an action...", cashier.runScheduler());
		assertTrue("Cashier should take just one action", !cashier.runScheduler());
		assertTrue("banker should NOT have a withdrawal request", banker.withdrawalRequests.size() == 0);
		assertTrue("Cashier should have moneyIncoming set to 0", cashier.moneyIncoming == 0);
		assertTrue("Market should have money>0 now (since we set market money to 0)", market.money > 0);
		assertTrue("Cashier should have less than $1000 now (since we paid)", cashier.money < 1000);
		assertTrue("Cashier's entry for marketBills should be 0 now", cashier.marketBills.get(market) == 0);
		//marketbills itself isn't 0 because the entry for market is still saved...
	}

	public void testTwoMarkets(){
		//paid in full; two markets
		//JUnit does not reset variables completely so dead keys still exist in cashier.marketBills (2 at beginning of this method)
		System.out.println("------------TESTING TWO MARKET ACCEPTABLE PAYMENT SCENARIO");
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.checks.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		cashier.addMarket(market1);
		assertTrue("Cashier should $0 marketBills pertaining to market1.", cashier.marketBills.get(market1)==0);
		assertTrue("Cashier should have nothing in marketBills pertaining to market2.", cashier.marketBills.get(market2)==null);
		cashier.addMarket(market2);
		assertTrue("Cashier should $0 marketBills pertaining to market1.", cashier.marketBills.get(market1)==0);
		assertTrue("Cashier should $0 marketBills pertaining to market2.", cashier.marketBills.get(market2)==0);
		cashier.money = 1000;
		System.out.println(cashier.marketBills.size());
		market1.money = 0;
		market2.money = 0;
		assertTrue("Market1 should have money=0 before cashier pays but doesn't",market1.money == 0);
		assertTrue("Market2 should have money=0 before cashier pays but doesn't",market2.money == 0);
		assertTrue("Cashier should have 1000 money", cashier.money == 1000);		
		cashier.msgHeresMarketBill(market1, 1, 2); // (market, type, amount)
		assertTrue("Cashier should have something in marketBills with key market1 > $1.", cashier.marketBills.get(market1) > 1);
		assertTrue("Cashier should have something in marketBills with key market2 == 0.", cashier.marketBills.get(market2) == 0);
		cashier.msgHeresMarketBill(market2, 3, 1);
		assertTrue("Cashier should have something in marketBills with key market1 > $1.", cashier.marketBills.get(market1) > 1);
		assertTrue("Cashier should have something in marketBills with key market2 > $1.", cashier.marketBills.get(market2) > 1);
		assertTrue(cashier.runScheduler());
		assertTrue(cashier.runScheduler());
		assertTrue("Market1 should have some money now", market1.money>0);
		assertTrue("Market2 should have some money now", market2.money>0);
		assertTrue("banker should NOT have a withdrawal request", banker.withdrawalRequests.size() == 0);
		assertTrue("Cashier should have moneyIncoming set to 0", cashier.moneyIncoming == 0);
		assertTrue("Market should have money>0 now (since we set market money to 0", market1.money > 0);
		assertTrue("Market should have money>0 now (since we set market money to 0", market2.money > 0);
		assertTrue("Cashier should have less than 1000 now (since we paid)", cashier.money < 1000);
	}


	public void testOneNormalCustomerMarketScenario()
	{	//setUp() runs first before this test!			
		System.out.println("------------TESTING ONE CUSTOMER NORMAL SCENARIO, DON'T HAVE FODD, MARKET");
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.checks.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		//test msgCompute
		cashier.msgCompute(2, (Customer)customer, (Waiter)waiter);
		//check postconditions for msgCompute
		
		//let's say cook ordered food because he doesn't have enough. Now you have a market bill.
		assertTrue("Cashier should have nothing in marketBills pertaining to market.", cashier.marketBills.get(market) == null);
		assertEquals("CashierAgent should have an empty event log before anything. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		cashier.addMarket(market);
		cashier.money = 1000;
		market.money = 0;
		cashier.msgHeresMarketBill(market, 2, 12); // cashier needs to pay market
		assertTrue("Market should have money=0 before cashier pays but doesn't",market.money == 0);
		assertTrue("Cashier should have 1000 money", cashier.money == 1000);		
		assertTrue("Cashier should have something in marketBills.", cashier.marketBills.size() > 0);
		assertTrue("Cashier should have something in marketBills with key market > $1.", cashier.marketBills.get(market) > 1);
		assertTrue("Cashier should just take an action...", cashier.runScheduler());
		assertTrue("banker should NOT have a withdrawal request", banker.withdrawalRequests.size() == 0);
		assertTrue("Cashier should have moneyIncoming set to 0", cashier.moneyIncoming == 0);
		assertTrue("Market should have money>0 now (since we set market money to 0)", market.money > 0);
		assertTrue("Cashier should have less than $1000 now (since we paid)", cashier.money < 1000);
		assertTrue("Cashier's entry for marketBills should be 0 now", cashier.marketBills.get(market) == 0);
		//marketbills itself isn't 0 because the entry for market is still saved...
				//now the customer?
		assertEquals(
				"MockWaiter should have an empty event log before the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals(
				"MockCustomer should have an empty event log before the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer.log.toString(), 0, customer.log.size());
		assertTrue("Cashier has one check request", cashier.checks.size() == 1);
		assertTrue("Cashier should go through scheduler once", cashier.runScheduler());
		assertTrue("MockWaiter's log should have HeresCheck in it. But it has this instead: ",
				waiter.log.containsString("HeresCheck"));
		assertTrue("Check should be marked as sent to waiter" , cashier.checks.get(0).getState() == Check.GIVEN_TO_WAITER);
		assertTrue("Check's bill shouldn't be 0 dollars" , cashier.checks.get(0).bill > 0);
		assertTrue("Cashier needs exactly 1 check", cashier.checks.size() == 1);
		assertFalse(cashier.runScheduler());
		customer.choice = 2;
		//skip irrelevant waiter-customer interactions
		//test msgHeresMyPayment normal scenario
		cashier.money = 100; //let's normalize money so we can compare (don't want to calculate decimal subtractions...)
		cashier.msgHeresMyPayment(customer, 500); //loadsamone! (assume debit card used, this is balance)
		assertEquals("state of the check should be GET_PAID but isn't", cashier.checks.get(0).getState(),Check.GET_PAID);
		cashier.runScheduler();
		assertTrue("MockCustomer's log should have HeresYourChange in it. But it has this instead: ",
				customer.log.toString().contains("HeresYourChange"));
		assertEquals("Should have no checks after payment is complete", cashier.checks.size(), 0);
		assertTrue("Cashier should have more than 100 dollars! (what he started with)", cashier.money>100);
		System.out.println("money after payment " +  cashier.money);
		//done with customer interactions
	}*/
	
	public void testTwoNormalCustomersScenario()
	{	//setUp() runs first before this test!			
		cashier.money = 100;
		System.out.println("------------TESTING TWO CUSTOMER NORMAL SCENARIO");
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.checks.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		//test msgCompute
		cashier.msgCompute(FOOD_ITEMS.pizza, (RestaurantChoiCustomer)customer, (RestaurantChoiWaiter)waiter);
		cashier.msgCompute(FOOD_ITEMS.chicken, (RestaurantChoiCustomer)customer1, (RestaurantChoiWaiter)waiter); // these happen almost simultaneously
		//check postconditions for msgCompute
		assertEquals(
				"MockWaiter should have an empty event log before the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals(
				"MockCustomer should have an empty event log before the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer.log.toString(), 0, customer.log.size());
		assertTrue("Cashier has one check request", cashier.checks.size() == 2);
		assertTrue("Cashier should go through scheduler twice", cashier.runScheduler());
		assertTrue("Check should be marked as sent to waiter" , cashier.checks.get(0).getState() == Check.GIVEN_TO_WAITER);
		assertTrue("Check's bill shouldn't be 0 dollars" , cashier.checks.get(0).bill > 0);
		assertTrue("MockWaiter's log should have HeresCheck in it. But it has this instead: ",
				waiter.log.containsString("HeresCheck"));
		assertTrue("Cashier should go through scheduler twice", cashier.runScheduler());
		assertTrue("Check should be marked as sent to waiter" , cashier.checks.get(1).getState() == Check.GIVEN_TO_WAITER);
		assertTrue("Check's bill shouldn't be 0 dollars" , cashier.checks.get(1).bill > 0);
		assertTrue("MockWaiter's log should have HeresCheck in it. But it has this instead: ",
				waiter.log.containsString("HeresCheck"));
		assertTrue("Cashier should go through scheduler twice", !cashier.runScheduler());
		assertTrue("MockWaiter's log should have HeresCheck in it. But it has this instead: ",
				waiter.log.containsString("HeresCheck"));
		assertTrue("Cashier needs to have exactly 2 check", cashier.checks.size() == 2);
		assertFalse(cashier.runScheduler());
		customer.choice = 2;
		//skip irrelevant waiter-customer interactions
		//test msgHeresMyPayment normal scenario
		cashier.msgHeresMyPayment(customer, 500); //loadsamone!
		cashier.msgHeresMyPayment(customer1, 500); //loadsamone!
		assertEquals("state of the check should be GET_PAID but isn't", cashier.checks.get(0).getState(),Check.GET_PAID);
		
		assertTrue("Cashier should = 100 dollars! (what he started with)", cashier.money==100);
		assertTrue("should go through scheduler twice", cashier.runScheduler());
		assertTrue("MockCustomer's log should have HeresYourChange in it. But it has this instead: ",
				customer.log.toString().contains("HeresYourChange"));		
		assertEquals("Should have one checks after payment is complete", cashier.checks.size(), 1);
		assertTrue("Cashier should > 100 dollars! (what he started with)", cashier.money>100);
		double temp = cashier.money;
		System.out.println("Cashier money post payment1: " + cashier.money);
		assertTrue("should go through scheduler twice", cashier.runScheduler());
		assertTrue("MockCustomer's log should have HeresYourChange in it. But it has this instead: ",
				customer1.log.toString().contains("HeresYourChange"));
		System.out.println("Cashier money post payment2: " + cashier.money);
		assertEquals("Should have 0 checks after payment is complete", cashier.checks.size(), 0);
		assertTrue("should have more money than after first person paid", cashier.money > temp);
		assertTrue("Cashier should have more than 100 dollars! (what he started with)", cashier.money>100);
		System.out.println("money after payment " +  cashier.money);
		//done with customer interactions
	}
	public void testCustomerDishesScenario(){
		//set up for another test of heresMyPayment, where the customer doesn't pay enough
		System.out.println("------------TESTING CUSTOMER UNABLE TO PAY SCENARIO");
		cashier.money = 0;
		customer.choice = 2;
		customer.name = "evil";
		assertTrue("should have no checks", cashier.checks.size() ==0);
		cashier.msgCompute(FOOD_ITEMS.pizza, (RestaurantChoiCustomer)customer, (RestaurantChoiWaiter)waiter);
		System.out.println("Cashier # of checks after receiving check: " + cashier.checks.size());
		assertTrue(cashier.runScheduler());
		assertTrue("customer should have choice 2", customer.choice==2);
		assertTrue("customer name is evil", customer.getPerson().getName().equals("evil"));
		//test msgHeresMyPayment; didn't pay enough
		cashier.msgHeresMyPayment(customer,0);
		assertEquals("should have one check", cashier.checks.size(), 1);
		assertEquals("state of the check should be NOT_FULFILLED but isn't", cashier.checks.get(0).getState(), Check.NOT_FULFILLED);
		assertTrue("Cashier should do an action after receiving msgHeresMyPayment", cashier.runScheduler());
		assertTrue("MockCustomer's log should have msgDoTheDishes in it. But it has this instead: ",
				customer.log.toString().contains("msgDoTheDishes"));
		System.out.println("Customer sent to dishes");
		assertEquals("state of the check should be FULFILL_BY_DISHES but isn't", cashier.checks.get(0).getState(), Check.FULFILL_BY_DISHES);
		assertTrue("Cashier should have no money more than before", cashier.money==0);
		assertEquals("should have one check still; customer still doing dishes", cashier.checks.size(), 1);
		cashier.msgDoneWithDishes(customer);
		assertEquals("should have no checks after customer done with dishes", cashier.checks.size(), 0);
		assertTrue("Cashier should have no money more than before", cashier.money==0);
		System.out.println("Cashier # of checks after transaction: " + cashier.checks.size());
	}
	public void testDeposit(){
		
	}
	public void testWithdraw(){
		
	}
}


/*
	public void testMarketCashierBankerInteraction()
	{
		System.out.println("------------TESTING USE-BANKER-FOR PAYMENT SCENARIO (extra credit part)");			
		//market-banker interaction testing
		cashier.addMarket(market);
		//now test if the cashier lacks money.
		cashier.money = 0;
		market.money = 0;
		cashier.msgHeresMarketBill(market, 2, 1);
		assertTrue("Market should have money=0 before cashier pays but doesn't",market.money == 0);
		assertTrue("Cashier should have something in marketBills.", cashier.marketBills.size() > 0);
		assertTrue("Cashier should have something in marketBills with key market > $1.", cashier.marketBills.get(market) > 1);
		assertTrue("Cashier should do an action", cashier.runScheduler());
		assertTrue("banker should have a withdrawal request", banker.withdrawalRequests.size() == 1);
		assertTrue("Cashier should have moneyIncoming set to 1", cashier.moneyIncoming == 1);
		assertTrue("Market should have money=0 but doesn't", market.money == 0);
		assertTrue("Banker should execute an action", banker.runScheduler());
		assertTrue("Cashier asked for $1000, should have 1000, since we set money to 0", cashier.money == 1000);
		assertTrue("Cashier should take action", cashier.runScheduler());
		assertTrue("Market should have money>0 now (since we set market money to 0", market.money > 0);
		System.out.println("Market $ " +  market.money);
		System.out.println("Cashier $ " +  cashier.money);
	}	*/