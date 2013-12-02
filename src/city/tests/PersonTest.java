package city.tests;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import city.Application;
import city.Application.BUILDING;
import city.Application.FOOD_ITEMS;
import city.RoleInterface;
import city.agents.PersonAgent;
import city.interfaces.Person;
import city.roles.BankCustomerRole;
import city.roles.ResidentRole;
import city.tests.mock.MockBank;
import city.tests.mock.MockCar;
import city.tests.mock.MockCityViewBuilding;
import city.tests.mock.MockHouse;
import city.tests.mock.MockMarket;
import city.tests.mock.MockOccupation;
import city.tests.mock.MockRestaurant;
import city.tests.mock.MockWorkplace;

public class PersonTest extends TestCase {

	// Application
	private Date date;
	private MockCityViewBuilding bankCityViewBuilding;
	private MockCityViewBuilding restaurantCityViewBuilding;
	private MockCityViewBuilding marketCityViewBuilding;
	private MockBank bank;
	private MockRestaurant restaurant;
	private MockMarket market;
	
	// Test
	private PersonAgent person;
	private MockWorkplace workplace;
	private MockOccupation occupation;
	private MockHouse house;
	private MockCar car;

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
		Application.CityMap.clearMap();
		Application.CityMap.addBuilding(BUILDING.bank, bank);
		Application.CityMap.addBuilding(BUILDING.restaurant, restaurant);
		Application.CityMap.addBuilding(BUILDING.market, market);
		
		// Set up test environment
		person = new PersonAgent("Person", date);
		workplace = new MockWorkplace("MockWorkplace");
		house = new MockHouse("MockHouse");
		car = new MockCar("MockCar");
	}
	
	/**
	 * The person has a first-shift job, a house, and a car.
	 * The person has enough money to deposit at the bank. The person's rent is not due. 
	 * The person will eat at a restaurant. The person has little food at home and will go to the market.
	 */
	public void testNormativeScenario() throws InterruptedException {
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		boolean outcome;
		
		// Further setup
		occupation = new MockOccupation(workplace, 0, 12);
		// Map<FOOD_ITEMS, Integer> items = new HashMap<FOOD_ITEMS, Integer>();
		// house.setFood(items); // With no items, the person will go to the market
		person.setOccupation(occupation);
		person.setHome(house);
		person.setCar(car);
		person.setCash(Person.BANK_DEPOSIT_THRESHOLD);
		
		// Preconditions
		assertEquals("Person should have a job", occupation, person.getOccupation());
		assertTrue("Person's job should be in the list of roles", person.getRoles().contains(occupation));
		assertTrue("Person's job should know Person owns it", occupation.getPerson().equals(person));
		assertTrue("Person should have a ResidentRole", getRoleOfTypeHelper(ResidentRole.class) != null);
		assertTrue("Person should have a BankCustomerRole", getRoleOfTypeHelper(BankCustomerRole.class) != null);
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
		assertTrue("Person scheduler should continue running", outcome);
		assertEquals("Person should be going to work", Person.STATE.goingToWork, person.getState());
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
		assertTrue("Person scheduler should continue running", outcome);
		assertEquals("Person should not have a CarPassengerRole", null, person.getCarPassengerRole());
		assertEquals("Person should have exactly three roles", 3, person.getRoles().size()); // Occupation, Resident, BankCustomer
		assertEquals("Person should be at work", Person.STATE.atWork, person.getState());
		assertTrue("Person's job should be active", person.getOccupation().getActive());
		
		// Run the scheduler for person.
		// - MockOccupation's scheduler is run for the first time
		// - MockOccupation returns false, the person has nothing to do
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		
		// The person should have nothing to do since MockOccupation's scheduler always returns false
		assertTrue("Person scheduler should not continue running", !outcome);
		
		// Fast-forward time until the person should leave work. Run the scheduler for person.
		incrementDateHelper(24); // It's 12 hours past shiftStart, now exactly shiftEnd
		outcome = person.runScheduler();
		
		// The person should be leaving work
		assertTrue("Person scheduler should continue running", outcome);
		assertEquals("Person should be leaving work", Person.STATE.leavingWork, person.getState());
		assertTrue("Person's job should not be active", !person.getOccupation().getActive());
		
		// Run the scheduler for person
		outcome = person.runScheduler();
		
		// The person should be going to the bank
		assertTrue("Person scheduler should continue running", outcome);
		assertEquals("Person should be going to the bank", Person.STATE.goingToBank, person.getState());
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
		assertTrue("Person scheduler should continue running", outcome);
		assertEquals("Person should not have a CarPassengerRole", null, person.getCarPassengerRole());
		assertEquals("Person should have exactly three roles", 3, person.getRoles().size()); // Occupation, Resident, BankCustomer
		assertEquals("Person should be at the bank", Person.STATE.atBank, person.getState());
		assertTrue("Person's BankCustomerRole should be active", person.getBankCustomerRole().getActive());
		assertEquals("Person's BankCustomerRole should be making a deposit", Application.BANK_SERVICE.atmDeposit, person.getBankCustomerRole().getService());
		
		// Force the bank interaction to end. Simulate the deposit being successful. Run the scheduler for person.
		// - The person should see that banking is done and leave
		person.getBankCustomerRole().setInactive();
		person.setCash(Person.BANK_DEPOSIT_THRESHOLD - Person.BANK_DEPOSIT_SUM);
		outcome = person.runScheduler();
		
		// Test that the person has properly left the bank
		assertTrue("Person should still have BankCustomerRole", person.getBankCustomerRole() != null);
		assertTrue("Person's BankCustomerRole should be inactive", !person.getBankCustomerRole().getActive());
		assertEquals("Person should have made a deposit", (Person.BANK_DEPOSIT_THRESHOLD - Person.BANK_DEPOSIT_SUM), person.getCash());
		
		// Person should be going to a restaurant
		assertTrue("Person scheduler should continue running", outcome);
		assertEquals("Person should be going to a restaurant", Person.STATE.goingToRestaurant, person.getState());
		assertTrue("Person should have a CarPassengerRole", person.getCarPassengerRole() != null);
		assertTrue("Person's CarPassengerRole should be active", person.getCarPassengerRole().getActive());
		assertTrue("Person's CarPassengerRole should know that Person owns it", person.getCarPassengerRole().getPerson().equals(person));
		assertEquals("Person's CarPassengerRole should be headed to the restaurant", restaurant, person.getCarPassengerRole().getDestination());
		assertTrue("Person's RestaurantCustomerRole should be inactive", !person.getRestaurantCustomerRole().getActive());
		assertEquals("Person should have exactly five roles", 5, person.getRoles().size()); // Occupation, Resident, BankCustomer, CarPassenger, RestaurantCustomer
		assertEquals("The RestaurantBuilding should be aware of the soon-to-arrive person's RestaurantCustomer", 1, restaurant.getOccupyingRoles().size());
		
		// Run the scheduler for person.
		// - The car should leave
		// - The car should arrive
		// - The person should see the car has arrived and enter the restaurant. MockRestaurantCustomer's scheduler has not yet been run
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		
		// Person should have arrived at restaurant
		assertTrue("Person scheduler should continue running", outcome);
		assertEquals("Person should not have a CarPassengerRole", null, person.getCarPassengerRole());
		assertEquals("Person should have exactly four roles", 4, person.getRoles().size()); // Occupation, Resident, BankCustomer, RestaurantCustomer
		assertEquals("Person should be at the restaurant", Person.STATE.atRestaurant, person.getState());
		assertTrue("Person's RestaurantCustomerRole should be active", person.getRestaurantCustomerRole().getActive());
		assertEquals("Person should have eaten", true, person.getHasEaten()); // hasEaten is set true by actGoToRestaurant(), not the scheduler
		
		// Run the scheduler for person.
		// - The mock should indicate eating is finished
		// - The person should see that eating is done and leave
		outcome = person.runScheduler();
		outcome = person.runScheduler();
		
		// Person should be going to a market
		assertTrue("Person scheduler should continue running", outcome);
		assertEquals("Person should be going to the market", Person.STATE.goingToMarket, person.getState());
		assertTrue("Person should have a CarPassengerRole", person.getCarPassengerRole() != null);
		assertTrue("Person's CarPassengerRole should be active", person.getCarPassengerRole().getActive());
		assertTrue("Person's CarPassengerRole should know that Person owns it", person.getCarPassengerRole().getPerson().equals(person));
		assertEquals("Person's CarPassengerRole should be headed to the restaurant", market, person.getCarPassengerRole().getDestination());
		assertEquals("Person should not have a RestaurantCustomerRole", null, person.getRestaurantCustomerRole());
		assertEquals("Person should have exactly three roles", 3, person.getRoles().size()); // Occupation, Resident, BankCustomer
		
	}
	
	/**
	 * In case the person is created when the time is in the middle of their shift, they should still go to work.
	 * Assumes testNormativeScenario() passes.
	 */
	public void testPersonGoesToWorkInMiddleOfShift() throws InterruptedException {
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// Further setup
		occupation = new MockOccupation(workplace, 0, 12);
		person.setOccupation(occupation);
		person.setHome(house);
		person.setCar(car);
		
		// Run the scheduler. It is past the person's shiftStart time.
		incrementDateHelper(6); // It's 3 hours past shiftStart
		person.runScheduler();
		
		assertTrue("", true); // TODO
	}
	
	/**
	 * Sends the person to work via bus and tests that it works
	 * Assumes testNormativeScenario() and testPersonGoesToWorkInMiddleOfShift() passes.
	 */
	public void testPersonGoesToWorkByBus() throws InterruptedException {
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		assertTrue("", true); // TODO
	}
	
	// Test helpers
	
	private void incrementDateHelper(int intervals) {
		date.setTime(date.getTime() + (1800000 * intervals));
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
	
	private <T extends RoleInterface> T getRoleOfTypeHelper(Class<T> type) {
		Object found = null;
		for (RoleInterface r : person.getRoles()) {
			if (r.getClass() == type) {
				if (found != null) {
					// Not expecting a duplicate, return null
					found = null;
					break;
				}
				found = r;
			}
		}
		return type.cast(found);
	}
}
