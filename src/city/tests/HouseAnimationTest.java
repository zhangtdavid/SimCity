package city.tests;

import java.util.Date;
import java.util.HashMap;

import junit.framework.TestCase;
import city.Application;
import city.Application.BUILDING;
import city.Application.FOOD_ITEMS;
import city.agents.PersonAgent;
import city.animations.HouseResidentAnimation;
import city.buildings.BankBuilding;
import city.buildings.HouseBuilding;
import city.gui.buildings.HousePanel;
import city.gui.views.CityViewBuilding;
import city.interfaces.Person;
import city.roles.LandlordRole;
import city.roles.ResidentRole;
import city.tests.animations.mock.MockAnimatedPerson;
import city.tests.mock.MockBank;
import city.tests.mock.MockCityViewBuilding;

public class HouseAnimationTest extends TestCase {
	
	// Needed things
	private Date date;
	private CityViewBuilding houseCityViewBuilding; // does nothing, no gui really pops out...
	private HousePanel hp; // does nothing, no gui really pops out...
	private MockAnimatedPerson animation;
	private LandlordRole landlord;
	private ResidentRole resident;
	private BankBuilding bank;	
	private CityViewBuilding bankCityViewBuilding;

	// Being tested
	private PersonAgent person;
	private HouseResidentAnimation homeAnimation;
	private HouseBuilding house;

	
	public void setUp() throws Exception {
		//super duper standard setup
		System.out.println("===================== Setup =====================");
		super.setUp();
		
		date = new Date(0);
		//you need this to make a person...
		bankCityViewBuilding =  new MockCityViewBuilding();
		bank = new BankBuilding("MockBank");
		
		//what's this for? :x
		Application.CityMap.clearMap();
		Application.CityMap.restaurantNumber = 0; // TODO remove, part of David's debugging code
		
		//needed things
		resident = new ResidentRole(date);
		landlord = new LandlordRole();
		resident.setPerson(person);
		landlord.setPerson(person);
		bank.setCityViewBuilding(bankCityViewBuilding);
		Application.CityMap.addBuilding(BUILDING.bank, bank);
		
		//sort of relevant things
		person = new PersonAgent("MovingPerson", date);
		person.setCash(0); // so he doesn't go to market or restaurant
		animation = new MockAnimatedPerson();
		homeAnimation = new HouseResidentAnimation(person);
		person.addRole(resident);
		person.setAnimation(animation);
		person.setHomeAnimation(homeAnimation);
		resident.setLandlord(landlord);
		person.setOccupation(null); // jobless, but is landlord
		
		//And the house, which is the real deal.
		house = new HouseBuilding("House", landlord, hp, houseCityViewBuilding);
		Application.CityMap.addBuilding(BUILDING.house,house);
		person.setHome(house);
		HashMap<FOOD_ITEMS, Integer> foods = new HashMap<FOOD_ITEMS, Integer>();
		foods.put(FOOD_ITEMS.chicken, 5); // put one chicken in the refrigerator to eat.
		foods.put(FOOD_ITEMS.salad, 0);
		foods.put(FOOD_ITEMS.steak, 0);
		foods.put(FOOD_ITEMS.pizza, 0); // there is no other food in the refrigerator
		house.setFood(foods);
				
	}
	
	public void testMovementInHouse() throws InterruptedException{
		System.out.println("");
		System.out.println("===================== Testing Animation Message Calls in House =====================");
		System.out.println("");
		//sanity background check
		assertEquals("There should be 5 chickens in the refrigerator of the house", (int)house.getFoodItems().get(FOOD_ITEMS.chicken), 5);
		assertEquals("There should be 0 steaks in the refrigerator of the house", (int)house.getFoodItems().get(FOOD_ITEMS.steak), 0);
		assertEquals("There should be 0 pizzas in the refrigerator of the house", (int)house.getFoodItems().get(FOOD_ITEMS.pizza), 0);
		assertEquals("There should be 0 salads in the refrigerator of the house", (int)house.getFoodItems().get(FOOD_ITEMS.salad), 0);
		assertEquals("Person should not have eaten", false, person.getHasEaten());
		assertTrue("Person should have a ResidentRole", person.getResidentRole() != null);
		assertTrue("Person should have a BankCustomerRole", person.getBankCustomerRole() != null);
		assertEquals("Person should have 3 roles", person.getRoles().size(), 3);
		assertEquals("Person should have a home that is a house", house, person.getHome());
		assertTrue("Person should have a home animation (the one we set)", person.getAnimationAtHome() != null);
		//person hasn't eaten yet. Let's make him check the refrigerator.
		assertEquals("Person shouldn't've eaten", person.getHasEaten(), false);
		assertEquals("Person's state should be STATE.none", Person.STATE.none, person.getState());
		
		boolean outcome;
		outcome = person.runScheduler();
		
		// Person should be doing a daily task (going home to cook)
		assertEquals("Person scheduler should continue running", true, outcome);
		assertEquals("Person should be going to cook", Person.STATE.goingToCook, person.getState());
		
		date.setTime(date.getTime() + (Application.HALF_HOUR * 36)); // go 9 hours later, so you want to go to sleep
		person.setDate(date);
		//now, get him home
		//person.
		
	}
}
