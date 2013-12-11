package city.tests.agents;

import java.util.Date;
import java.util.HashMap;

import junit.framework.TestCase;
import city.Application;
import city.Application.BUILDING;
import city.Application.CityMap;
import city.Application.FOOD_ITEMS;
import city.agents.PersonAgent;
import city.agents.interfaces.Person;
import city.agents.interfaces.Person.STATES;
import city.bases.interfaces.BuildingInterface;
import city.bases.interfaces.RoleInterface;
import city.tests.agents.mocks.MockBus;
import city.tests.agents.mocks.MockCar;
import city.tests.animations.mocks.MockAnimatedBusPassenger;
import city.tests.animations.mocks.MockAnimatedPerson;
import city.tests.buildings.mocks.MockBank;
import city.tests.buildings.mocks.MockBusStop;
import city.tests.buildings.mocks.MockHouse;
import city.tests.buildings.mocks.MockMarket;
import city.tests.buildings.mocks.MockRestaurant;
import city.tests.buildings.mocks.MockWorkplace;
import city.tests.roles.mocks.MockCityViewBuilding;
import city.tests.roles.mocks.MockGenericJob;

public class PersonTest extends TestCase {
	
	// IDEAS FOR TESTING
	// - What happens if the occupation is removed on the way to work?
	// - Test taking away an occupation
	// - Test forcing sleep

	// Application
	private Date date;
	private MockCityViewBuilding workplaceCityViewBuilding;
	private MockCityViewBuilding bankCityViewBuilding;
	private MockCityViewBuilding restaurantCityViewBuilding;
	private MockCityViewBuilding marketCityViewBuilding;
	private MockCityViewBuilding busStop1CityViewBuilding;
	private MockCityViewBuilding busStop2CityViewBuilding;
	private MockBank bank;
	private MockRestaurant restaurant;
	private MockMarket market;
	private MockBusStop busStop1;
	private MockBusStop busStop2;
	
	// Test
	private PersonAgent person;
	private MockAnimatedPerson animation;
	private MockWorkplace workplace;
	private MockGenericJob occupation;
	private MockHouse house;
	private MockCar car;
	private MockBus bus;

	public void setUp() throws Exception {
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		super.setUp();
		
		// Set up application environment
		date = new Date(0);
		bankCityViewBuilding =  new MockCityViewBuilding();
		bank = new MockBank("MockBank");
		bank.setCityViewBuilding(bankCityViewBuilding);
		restaurantCityViewBuilding = new MockCityViewBuilding();
		restaurant = new MockRestaurant("MockRestaurant");
		restaurant.setCityViewBuilding(restaurantCityViewBuilding);
		marketCityViewBuilding = new MockCityViewBuilding();
		market = new MockMarket("MockMarket");
		market.setCityViewBuilding(marketCityViewBuilding);
		busStop1CityViewBuilding = new MockCityViewBuilding();
		busStop1 = new MockBusStop("MockBusStop1");
		busStop1.setCityViewBuilding(busStop1CityViewBuilding);
		busStop2CityViewBuilding = new MockCityViewBuilding();
		busStop2 = new MockBusStop("MockBusStop2");
		busStop2.setCityViewBuilding(busStop2CityViewBuilding);
		Application.CityMap.clearMap();
		Application.CityMap.addBuilding(BUILDING.bank, bank);
		Application.CityMap.addBuilding(BUILDING.restaurant, restaurant);
		Application.CityMap.addBuilding(BUILDING.market, market);
		Application.CityMap.addBuilding(BUILDING.busStop, busStop1);
		Application.CityMap.addBuilding(BUILDING.busStop, busStop2);
		
		// Set up test environment
		house = new MockHouse("MockHouse");
		animation = new MockAnimatedPerson();
		person = new PersonAgent("Person", date, animation, house);
		workplace = new MockWorkplace("MockWorkplace");
		workplaceCityViewBuilding = new MockCityViewBuilding();
		workplace.setCityViewBuilding(workplaceCityViewBuilding);
		car = new MockCar("MockCar");
		bus = new MockBus("MockBus");
		
		// By default, everyone has food at home. We don't want that in the tests.
		HashMap<FOOD_ITEMS, Integer> items = new HashMap<FOOD_ITEMS, Integer>();
		items.put(FOOD_ITEMS.salad, 0);
		items.put(FOOD_ITEMS.chicken, 0);
		items.put(FOOD_ITEMS.steak, 0);
		items.put(FOOD_ITEMS.pizza, 0); 
		house.setFood(person, items);
	}
	
	/**
	 * The person has a first-shift job and a car.
	 * The person has enough money to deposit at the bank. The person's rent is not due. 
	 * The person will eat at a restaurant. The person has little food at home and will go to the market.
	 * The person will not eat at home, even though they went to the market, because they ate at the restaurant.
	 * 
	 * Assumes nothing about whether other tests pass or not
	 */
	public void testNormativeScenario() throws InterruptedException {
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		boolean outcome;
		
		// Further setup
		occupation = new MockGenericJob(workplace, 0, 12);
		person.setOccupation(occupation);
		person.setCar(car);
		person.setCash(Person.BANK_DEPOSIT_THRESHOLD);
		person.setAnimation(animation);
		
		// Preconditions
		assertEquals("Person should have a job", occupation, person.getOccupation());
		assertTrue("Person's job should be in the list of roles", person.getRoles().contains(occupation));
		assertTrue("Person's job should know Person owns it", occupation.getPerson().equals(person));
		assertTrue("Person should have a ResidentRole", person.getResidentRole() != null);
		assertTrue("Person should have a BankCustomerRole", person.getBankCustomerRole() != null);
		assertTrue("Person should not have any active roles", noRolesAreActiveHelper());
		assertEquals("Person should have a home that is a house", house, person.getHome());
		assertEquals("Person should have a car", car, person.getCar());
		assertEquals("Person should not have a CarPassengerRole", null, person.getCarPassengerRole());
		assertEquals("Person should not have a BusPassengerRole", null, person.getBusPassengerRole());
		assertEquals("Person should not have a MarketCustomerRole", null, person.getMarketCustomerRole());
		assertEquals("Person should not have a RestaurantCustomerRole", null, person.getRestaurantCustomerRole());
		assertEquals("Person should have exactly three roles", 3, person.getRoles().size()); // Occupation, Resident, BankCustomer
		assertEquals("Person should not have eaten", false, person.getHasEaten());
		
		// Run the scheduler. Right now it is exactly the person's shiftStart time.
		outcome = person.runScheduler();
		
		// Person should be going to work
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be going to work", Person.STATES.goingToWork, person.getState());
		assertTrue("Person should have a CarPassengerRole", person.getCarPassengerRole() != null);
		assertTrue("Person's CarPassengerRole should be active", person.getCarPassengerRole().getActive());
		assertTrue("Person's CarPassengerRole should know that Person owns it", person.getCarPassengerRole().getPerson().equals(person));
		assertTrue("Person's CarPassengerRole should be headed to the person's workplace", person.getCarPassengerRole().getDestination().equals(workplace));
		assertEquals("Person should have exactly four roles", 4, person.getRoles().size()); // Occupation, Resident, BankCustomer, CarPassenger
		
		// Run the scheduler for person.
		// - The car should leave
		// - The car should arrive
		// - The person should see the car has arrived and start working. MockOccupation's scheduler has not yet been run
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		
		// Person should have arrived at work
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should not have a CarPassengerRole", null, person.getCarPassengerRole());
		assertEquals("Person should have exactly three roles", 3, person.getRoles().size()); // Occupation, Resident, BankCustomer
		assertEquals("The work Building should be aware of the person's worker", 1, person.getOccupation().getWorkplace(BuildingInterface.class).getOccupyingRoles().size());
		assertEquals("Person should be at work", Person.STATES.atWork, person.getState());
		assertTrue("Person's job should be active", person.getOccupation().getActive());
		
		// Run the scheduler for person.
		// - MockOccupation's scheduler is run for the first time
		// - MockOccupation returns false, the person has nothing to do
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		
		// The person should have nothing to do since MockOccupation's scheduler always returns false
		assertEquals("Person scheduler should not continue running", false, outcome);
		
		// Fast-forward time until the person should leave work. Run the scheduler for person.
		incrementDateHelper(24); // It's 12 hours past shiftStart, now exactly shiftEnd
		outcome = person.runScheduler();
		
		// The person should be leaving work
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be leaving work", Person.STATES.leavingWork, person.getState());
		assertTrue("Person's job should not be active", !person.getOccupation().getActive());
		
		// Run the scheduler for person
		outcome = person.runScheduler();
		
		// The person should be going to the bank
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be going to the bank", Person.STATES.goingToBank, person.getState());
		assertEquals("The work Building should have no roles inside", 0, person.getOccupation().getWorkplace(BuildingInterface.class).getOccupyingRoles().size());
		assertTrue("Person should have a CarPassengerRole", person.getCarPassengerRole() != null);
		assertTrue("Person's CarPassengerRole should be active", person.getCarPassengerRole().getActive());
		assertTrue("Person's CarPassengerRole should know that Person owns it", person.getCarPassengerRole().getPerson().equals(person));
		assertEquals("Person's CarPassengerRole should be headed to the bank", bank, person.getCarPassengerRole().getDestination());
		assertEquals("Person should have exactly four roles", 4, person.getRoles().size()); // Occupation, Resident, BankCustomer, CarPassenger
		
		// Run the scheduler for person.
		// - The car should leave
		// - The car should arrive
		// - The person should see the car has arrived and start banking. BankCustomerRole's scheduler has not yet been run
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		
		// Person should have arrived at bank
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should not have a CarPassengerRole", null, person.getCarPassengerRole());
		assertEquals("Person should have exactly three roles", 3, person.getRoles().size()); // Occupation, Resident, BankCustomer
		assertEquals("Person should be at the bank", Person.STATES.atBank, person.getState());
		assertEquals("The BankBuilding should be aware of the person's BankCustomer", 1, bank.getOccupyingRoles().size());
		assertTrue("Person's BankCustomerRole should be active", person.getBankCustomerRole().getActive());
		assertEquals("Person's BankCustomerRole should be making a deposit", Application.BANK_SERVICE.deposit, person.getBankCustomerRole().getService());
		
		// Force the bank interaction to end. Simulate the deposit being successful. Run the scheduler for person.
		// Note- a truly successful deposit would setCash(BANK_DEPOSIT_THRESHOLD - BANK_DEPOSIT_SUM) but we want the person to go to a restaurant next
		// - The person should see that banking is done and leave
		person.getBankCustomerRole().setInactive();
		person.setCash(Person.RESTAURANT_DINING_THRESHOLD);
		outcome = person.runScheduler();
		
		// Test that the person has properly left the bank
		assertTrue("Person should still have BankCustomerRole", person.getBankCustomerRole() != null);
		assertEquals("The BankBuilding should have no roles inside", 0, bank.getOccupyingRoles().size());
		assertTrue("Person's BankCustomerRole should be inactive", !person.getBankCustomerRole().getActive());
		assertEquals("Person should have made a deposit", Person.RESTAURANT_DINING_THRESHOLD, person.getCash()); // See explanation above
		
		// Person should be going to a restaurant
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be going to a restaurant", Person.STATES.goingToRestaurant, person.getState());
		assertTrue("Person should have a CarPassengerRole", person.getCarPassengerRole() != null);
		assertTrue("Person's CarPassengerRole should be active", person.getCarPassengerRole().getActive());
		assertTrue("Person's CarPassengerRole should know that Person owns it", person.getCarPassengerRole().getPerson().equals(person));
		assertEquals("Person's CarPassengerRole should be headed to the restaurant", restaurant, person.getCarPassengerRole().getDestination());
		assertTrue("Person's RestaurantCustomerRole should be inactive", !person.getRestaurantCustomerRole().getActive());
		assertEquals("Person should have exactly five roles", 5, person.getRoles().size()); // Occupation, Resident, BankCustomer, CarPassenger, RestaurantCustomer
		
		// Run the scheduler for person.
		// - The car should leave
		// - The car should arrive
		// - The person should see the car has arrived and enter the restaurant. MockRestaurantCustomer's scheduler has not yet been run
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		
		// Person should have arrived at restaurant
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should not have a CarPassengerRole", null, person.getCarPassengerRole());
		assertEquals("Person should have exactly four roles", 4, person.getRoles().size()); // Occupation, Resident, BankCustomer, RestaurantCustomer
		assertEquals("Person should be at the restaurant", Person.STATES.atRestaurant, person.getState());
		assertEquals("The RestaurantBuilding should be aware of person's RestaurantCustomer", 1, restaurant.getOccupyingRoles().size());
		assertTrue("Person's RestaurantCustomerRole should be active", person.getRestaurantCustomerRole().getActive());
		assertEquals("Person should have eaten", true, person.getHasEaten()); // hasEaten is set true by actGoToRestaurant(), not the scheduler
		
		// Run the scheduler for person.
		// - The mock should indicate eating is finished
		// - The person should see that eating is done and leave
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		
		// Person should be going to a market
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be going to the market", Person.STATES.goingToMarket, person.getState());
		assertTrue("Person should have a CarPassengerRole", person.getCarPassengerRole() != null);
		assertTrue("Person's CarPassengerRole should be active", person.getCarPassengerRole().getActive());
		assertTrue("Person's CarPassengerRole should know that Person owns it", person.getCarPassengerRole().getPerson().equals(person));
		assertEquals("Person's CarPassengerRole should be headed to the restaurant", market, person.getCarPassengerRole().getDestination());
		assertEquals("Person should not have a RestaurantCustomerRole", null, person.getRestaurantCustomerRole());
		assertEquals("The RestaurantBuilding should have no roles inside", 0, restaurant.getOccupyingRoles().size());
		assertEquals("Person should have exactly five roles", 5, person.getRoles().size()); // Occupation, Resident, BankCustomer, CarPassenger, MarketCustomer
		assertEquals("Person's MarketCustomer should have an order of four items", 4, person.getMarketCustomerRole().getOrder().getOrderItems().size());
		
		// Run the scheduler for person.
		// - The car should leave
		// - The car should arrive
		// - The person should see the car has arrived and start shopping. MarketCustomerRole's scheduler has not yet been run
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		
		// Person should have arrived at market
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should not have a CarPassengerRole", null, person.getCarPassengerRole());
		assertEquals("Person should have exactly four roles", 4, person.getRoles().size()); // Occupation, Resident, BankCustomer, MarketCustomer
		assertEquals("The MarketBuilding should be aware of the person's MarketCustomer", 1, market.getOccupyingRoles().size());
		assertEquals("Person should be at the market", Person.STATES.atMarket, person.getState());
		assertTrue("Person's MarketCustomerRole should be active", person.getMarketCustomerRole().getActive());
		
		// Force the market interaction to end. Simulate the addition of food. Run the scheduler for person.
		// - The person should see that shopping is done and leave
		person.getMarketCustomerRole().setInactive();
		HashMap<FOOD_ITEMS, Integer> temp = new HashMap<FOOD_ITEMS, Integer>();
		temp.put(FOOD_ITEMS.chicken, 4);
		person.getHome().setFood(person, temp); // Not a realistic order, but enough to prevent us from going to the market again
		outcome = person.runScheduler();
		
		// Test that the person has properly left the market and is going home to sleep
		assertEquals("Person should not still have MarketCustomerRole", null, person.getMarketCustomerRole());
		assertEquals("The MarketBuilding should have no roles inside", 0, market.getOccupyingRoles().size());
		assertEquals("Person should be going home to sleep", STATES.goingToSleep, person.getState());
		assertEquals("Person should have exactly four roles", 4, person.getRoles().size()); // Occupation, Resident, BankCustomer, CarPassengerRole

		// Run the scheduler for person.
		// - The car should leave
		// - The car should arrive
		// - The person should see the car has arrived and go to sleep.
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		
		// Person should have arrived at home and be sleeping
		assertEquals("Person scheduler should not continue running", false, outcome);
		assertEquals("Person should not have a CarPassengerRole", null, person.getCarPassengerRole());
		assertEquals("Person should have exactly three roles", 3, person.getRoles().size()); // Occupation, Resident, BankCustomer
		assertEquals("Person should be sleeping", Person.STATES.atSleep, person.getState());
		assertEquals("Person should not have eaten", false, person.getHasEaten());
		
		// The time now should be 12, or when the person's shift is over
		// Run the clock forward so that it is still in the person's off hours
		incrementDateHelper(12); // It's 6 hours past shiftEnd
		outcome = person.runScheduler();
		
		// Person should do nothing and still be sleeping
		assertEquals("Person scheduler should not continue running", false, outcome);
		assertEquals("Person should have exactly three roles", 3, person.getRoles().size()); // Occupation, Resident, BankCustomer
		assertEquals("Person should be sleeping", Person.STATES.atSleep, person.getState());
		assertEquals("Person should not have eaten", false, person.getHasEaten());
		
		// Fast-forward the time and verify the person wakes up and starts the cycle again
		incrementDateHelper(12); // It's 6 hours forward again, or exactly ShiftStart
		outcome = person.runScheduler();
		
		// Person should be going to work
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be going to work", Person.STATES.goingToWork, person.getState());
		
		// That's one full day's cycle.
	}
	
	/**
	 * The person has a second-shift job and a car.
	 * 
	 * Assumes all other tests pass
	 */
	public void testSecondShiftJob() throws InterruptedException {
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		boolean outcome;
		
		// Further setup
		occupation = new MockGenericJob(workplace, 12, 0);
		person.setOccupation(occupation);
		person.setCar(car);
		person.setCash(Person.RESTAURANT_DINING_THRESHOLD);
		person.setAnimation(animation);
		
		// Preconditions
		assertEquals("Person's state should be STATE.none", Person.STATES.none, person.getState());
		
		// Run the scheduler. Right now it is hour 0, the person should move from STATE.none right into daily tasks.
		outcome = person.runScheduler();
		
		// Person should be doing a daily task (going to a restaurant)
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be going to a restaurant", Person.STATES.goingToRestaurant, person.getState());
		
		// Run the scheduler for person.
		// - The car should leave
		// - The car should arrive
		// - The person should see the car has arrived and enter the restaurant.
		// - The mock should indicate eating is finished.
		// - The person should see that eating is done and leave
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		
		// Person should be going to a market
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be going to the market", Person.STATES.goingToMarket, person.getState());
		
		// Run the scheduler for person. End the market interaction and simulate success. Run the scheduler some more.
		// - The car should leave
		// - The car should arrive
		// - The person should see the car has arrived and enter the market
		//
		// - The person should see that shopping is done and leave
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		person.getMarketCustomerRole().setInactive();
		HashMap<FOOD_ITEMS, Integer> temp = new HashMap<FOOD_ITEMS, Integer>();
		temp.put(FOOD_ITEMS.chicken, 4);
		person.getHome().setFood(person, temp); // Not a realistic order, but enough to prevent us from going to the market again		
		outcome = person.runScheduler();
		
		// Person should be going to sleep
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be going home to sleep", STATES.goingToSleep, person.getState());
		
		// Run the scheduler for person.
		// - The car should leave
		// - The car should arrive
		// - The person should see the car has arrived and go to sleep.
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		
		// Person should have arrived at home and be sleeping
		assertEquals("Person scheduler should not continue running", false, outcome);
		assertEquals("Person should be sleeping", Person.STATES.atSleep, person.getState());
		
		// The time now still be 0, just as it was at the start
		// Run the clock forward so that it is still in the person's off hours
		incrementDateHelper(12); // It's 6 hours past start
		outcome = person.runScheduler();
		
		// Person should do nothing and still be sleeping
		assertEquals("Person scheduler should not continue running", false, outcome);
		assertEquals("Person should be sleeping", Person.STATES.atSleep, person.getState());
		
		// Fast-forward the time to the person's work start
		incrementDateHelper(12); // It's 6 hours forward again, or exactly ShiftStart
		outcome = person.runScheduler();
		
		// Person should be going to work
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be going to work", Person.STATES.goingToWork, person.getState());

		// Run the scheduler for person.
		// - The car should leave
		// - The car should arrive
		// - The person should see the car has arrived and start working.
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		
		// Person should have arrived at work
		assertEquals("Person should not have a CarPassengerRole", null, person.getCarPassengerRole());
		assertEquals("Person should be at work", Person.STATES.atWork, person.getState());
		
		// Fast-forward time until the person should leave work. Run the scheduler for person.
		incrementDateHelper(24); // It's 12 hours past shiftStart, now exactly shiftEnd
		outcome = person.runScheduler();
		
		// The person should be leaving work
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be leaving work", Person.STATES.leavingWork, person.getState());
		
		// Run the scheduler.
		outcome = person.runScheduler();
		
		// The person should loop and begin doing daily tasks again.
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be going to a restaurant", Person.STATES.goingToRestaurant, person.getState());
		
		// That's one full day's cycle.
	}
	
	/**
	 * The person has a second-shift job and a car.
	 * Will send the person to the bank to withdraw money for rent as well.
	 * 
	 * Assumes all other tests pass
	 */
	public void testPeoplePayRent() throws InterruptedException {
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		boolean outcome;
		
		// Further setup
		occupation = new MockGenericJob(workplace, 12, 0);
		person.setOccupation(occupation);
		person.setCar(car);
		person.setCash(0);
		person.setAnimation(animation);
		
		// Preconditions
		assertEquals("Person's state should be STATE.none", Person.STATES.none, person.getState());
		
		// The time now is of course zero
		// Run the clock forward so that the person's rent will be due
		incrementDateHelper(338); // It's 7 days and 1 hour past start, enough to trigger Resident.RENT_DUE_INTERVAL
		outcome = person.runScheduler();
		
		// Person should be going to the bank
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be going to the bank", Person.STATES.goingToBank, person.getState());

		// Run the scheduler for person.
		// - The car should leave
		// - The car should arrive
		// - The person should see the car has arrived and start banking.
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		
		// Person should have arrived at bank
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be at the bank", Person.STATES.atBank, person.getState());
		assertTrue("Person's BankCustomerRole should be active", person.getBankCustomerRole().getActive());
		assertEquals("Person's BankCustomerRole should be withdrawing money", Application.BANK_SERVICE.moneyWithdraw, person.getBankCustomerRole().getService());
		
		// Force the bank interaction to end. Simulate the withdrawal being successful. Run the scheduler for person.
		// - The person should see that banking is done and leave
		person.getBankCustomerRole().setInactive();
		person.setCash(Person.RENT_MIN_THRESHOLD);
		outcome = person.runScheduler();
		
		// Person should be going to pay rent
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be going to pay rent", Person.STATES.goingToPayRent, person.getState());
		
		// Run the scheduler for person.
		// - The car should leave
		// - The car should arrive
		// - The person should see the car has arrived and start paying rent.
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		
		// Person should be at rent payment
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be at rent payment", Person.STATES.atRentPayment, person.getState());
		assertEquals("Person should have exactly three roles", 3, person.getRoles().size()); // Occupation, Resident, BankCustomer
		assertEquals("Person's ResidentRole should be active", true, person.getResidentRole().getActive());
		
		// Force the rent interaction to end. Simulate the payment being successful and set last paid date. Run the scheduler for person.
		// - The person should see that paying rent is done and leave
		person.getResidentRole().setInactive();
		person.setCash(10); // $10 has no significance, just want to decrease the cash on hand.
		person.getResidentRole().setRentLastPaid(person.getDate());
		outcome = person.runScheduler();
		
		// Person should be going to a restaurant
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be going to a restaurant", Person.STATES.goingToRestaurant, person.getState());
		
		// End of cycle
	}
	
	/**
	 * The person has no job and a car.
	 * 
	 * Assumes all other tests pass
	 */
	public void testJoblessPeople() throws InterruptedException {
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		boolean outcome;
		
		// Further setup
		person.setOccupation(null);
		person.setCar(car);
		person.setCash(Person.RESTAURANT_DINING_THRESHOLD);
		person.setAnimation(animation);
		
		// Preconditions
		assertEquals("Person's state should be STATE.none", Person.STATES.none, person.getState());
		
		// Run the scheduler. Right now it is hour 0, the person should move from STATE.none right into daily tasks.
		outcome = person.runScheduler();
		
		// Person should be doing a daily task
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be going to a restaurant", Person.STATES.goingToRestaurant, person.getState());
		
		// Run the scheduler for person.
		// - The car should leave
		// - The car should arrive
		// - The person should see the car has arrived and enter the restaurant.
		// - The mock should indicate eating is finished.
		// - The person should see that eating is done and leave
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		
		// Person should be going to a market
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be going to the market", Person.STATES.goingToMarket, person.getState());
		
		// Run the scheduler for person. End the market interaction and simulate success. Run the scheduler some more.
		// - The car should leave
		// - The car should arrive
		// - The person should see the car has arrived and enter the market
		//
		// - The person should see that shopping is done and leave
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		person.getMarketCustomerRole().setInactive();
		HashMap<FOOD_ITEMS, Integer> temp = new HashMap<FOOD_ITEMS, Integer>();
		temp.put(FOOD_ITEMS.chicken, 4);
		person.getHome().setFood(person, temp); // Not a realistic order, but enough to prevent us from going to the market again		
		outcome = person.runScheduler();
		
		// Person should be going to sleep
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be going home to sleep", STATES.goingToSleep, person.getState());
		
		// Run the scheduler for person.
		// - The car should leave
		// - The car should arrive
		// - The person should see the car has arrived and go to sleep.
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		
		// Person should have arrived at home and be sleeping
		assertEquals("Person scheduler should not continue running", false, outcome);
		assertEquals("Person should be sleeping", Person.STATES.atSleep, person.getState());
		
		// The time now should still be 0, just as it was at the start
		// Run the clock forward so that it is still in the person's off hours
		incrementDateHelper(12); // It's 6 hours past start
		outcome = person.runScheduler();
		
		// Person should do nothing and still be sleeping
		assertEquals("Person scheduler should not continue running", false, outcome);
		assertEquals("Person should be sleeping", Person.STATES.atSleep, person.getState());
		
		// Fast-forward the time to when the person should be forced to wake up (he has no job, remember)
		incrementDateHelper(12); // It's 6 hours forward again, or exactly time to wake up
		outcome = person.runScheduler();
		
		// Person should be going to a restaurant
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be going to a restaurant", Person.STATES.goingToRestaurant, person.getState());

		// Run the scheduler for person.
		// - The car should leave
		// - The car should arrive
		// - The person should see the car has arrived and enter the restaurant.
		// - The mock should indicate eating is finished.
		// - The person should see that eating is done and leave
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		
		// Person should be going to sleep (he went to the market yesterday)
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be going home to sleep", STATES.goingToSleep, person.getState());
		
		// That's one full day's cycle.
	}
	
	/**
	 * In case the person is created when the time is in the middle of their shift, they should still go to work.
	 * Assumes all other tests pass
	 */
	public void testPersonGoesToWorkInMiddleOfShift() throws InterruptedException {
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		boolean outcome;
		
		// Further setup
		occupation = new MockGenericJob(workplace, 0, 12);
		person.setOccupation(occupation);
		person.setCar(car);
		
		// Run the scheduler. It is past the person's shiftStart time.
		incrementDateHelper(6); // It's 3 hours past shiftStart
		outcome = person.runScheduler();
		
		// Person should be going to work
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be going to work", Person.STATES.goingToWork, person.getState());
	}
	
	/**
	 * Sends the person to work via bus. Person has a first-shift job.
	 * 
	 * Assumes all other tests pass
	 */
	public void testPersonGoesToWorkByBus() throws InterruptedException {
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		boolean outcome;
		
		// Further setup
		occupation = new MockGenericJob(workplace, 0, 12);
		person.setOccupation(occupation);
		person.setCash(51);
		person.setAnimation(animation);
		
		// Preconditions
		assertEquals("Person should have a job", occupation, person.getOccupation());
		assertEquals("Person should not have a car", null, person.getCar());
		assertEquals("Person should not have a CarPassengerRole", null, person.getCarPassengerRole());
		assertEquals("Person should not have a BusPassengerRole", null, person.getBusPassengerRole());
		assertEquals("Person should have exactly three roles", 3, person.getRoles().size()); // Occupation, Resident, BankCustomer
		
		// Run the scheduler. Right now it is exactly the person's shiftStart time.
		outcome = person.runScheduler();
		
		// Person should be going to work
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be going to work", Person.STATES.goingToWork, person.getState());
		assertTrue("Person should have a BusPassengerRole", person.getBusPassengerRole() != null);
		assertTrue("Person's BusPassengerRole should be active", person.getBusPassengerRole().getActive());
		assertTrue("Person's BusPassengerRole should know that Person owns it", person.getBusPassengerRole().getPerson().equals(person));
		assertEquals("Person's BusPassengerRole should be leaving from the bus stop nearest the person", CityMap.findClosestBuilding(BUILDING.busStop, person), person.getBusPassengerRole().getBusStopToWaitAt());
		assertEquals("Person's BusPassengerRole should be riding to the bus stop nearest the person's workplace", CityMap.findClosestBuilding(BUILDING.busStop, workplace), person.getBusPassengerRole().getBusStopDestination());
		assertEquals("Person should have exactly four roles", 4, person.getRoles().size()); // Occupation, Resident, BankCustomer, BusPassenger
		
		// Run the scheduler for person.
		// - The BusPassengerRole should start
		// - The BusPassengerRole should stop, it's waiting on a mock bus that hasn't arrived (and won't!)
		// - The PersonAgent should stop, since it has nothing else to do
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		
		// Person should be doing nothing
		assertEquals("Person scheduler should not continue running", false, outcome);
		
		// Tell the bus passenger it has arrived. Run the scheduler for person.
		//
		// - Gets on the bus
		//
		// - Rides the bus
		// - Gets off the bus and goes to work
		person.getBusPassengerRole().setAnimation(new MockAnimatedBusPassenger(person.getBusPassengerRole()));
		person.getBusPassengerRole().msgBusIsHere(bus);
		person.getBusPassengerRole().msgImAtDestination();
		outcome = person.runScheduler();
		person.getBusPassengerRole().msgImAtYourDestination();
		person.getBusPassengerRole().msgImAtDestination();
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		
		// Person should have arrived at work
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should not have a BusPassengerRole", null, person.getBusPassengerRole());
		assertEquals("Person should have exactly three roles", 3, person.getRoles().size()); // Occupation, Resident, BankCustomer
		assertEquals("Person should be at work", Person.STATES.atWork, person.getState());
		assertTrue("Person's job should be active", person.getOccupation().getActive());
	}
	
	/**
	 * The person has a second-shift job and a car.
	 * 
	 * Assumes all other tests pass
	 */
	public void testPersonEatsAtHome() throws InterruptedException {
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		boolean outcome;
		
		// Further setup
		occupation = new MockGenericJob(workplace, 12, 0);
		person.setOccupation(occupation);
		person.setCar(car);
		person.setCash(0);
		person.setAnimation(animation);
		person.setCurrentLocation(person.getHome());
		animation.setAgent(person);
		HashMap<FOOD_ITEMS, Integer> temp = new HashMap<FOOD_ITEMS, Integer>();
		temp.put(FOOD_ITEMS.chicken, 5);// One item will be removed after cooking, need 4 to stop from going to market next
		person.getHome().setFood(person, temp); // Not a realistic order, but enough to prevent us from going to the market again 
		
		// Preconditions
		assertEquals("Person's state should be STATE.none", Person.STATES.none, person.getState());
		
		// Run the scheduler. Right now it is hour 0, the person should move from STATE.none right into daily tasks.
		outcome = person.runScheduler();
		
		// Person should be doing a daily task (going home to cook)
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be going to cook", Person.STATES.goingToCook, person.getState());
		
		// Run the scheduler for person.
		// - The person should go to cook immediately since it's already at home
		outcome = person.runScheduler();
		
		// Person should be cooking
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be cooking", Person.STATES.atCooking, person.getState());
		
		// Run the scheduler for person
		// - The person should finish cooking/eating and go to sleep
		outcome = person.runScheduler();
		
		// Person should be going to sleep
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be going to sleep", Person.STATES.goingToSleep, person.getState());
		
		// Finished
	}
	
	// Test helpers
	
	private void incrementDateHelper(int intervals) {
		date.setTime(date.getTime() + (Application.HALF_HOUR * intervals));
		person.setDate(date);
	}
	
	private boolean noRolesAreActiveHelper() {
		boolean disposition = true;
		for (RoleInterface r : person.getRoles()) {
			if (r.getActive() == true) {
				disposition = false;
				break;
			}
		}
		return disposition;
	}

}
