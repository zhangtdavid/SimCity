package city.tests.animations;

import java.util.Date;
import java.util.HashMap;

import junit.framework.TestCase;
import city.Application;
import city.Application.BUILDING;
import city.Application.FOOD_ITEMS;
import city.agents.interfaces.Person;
import city.animations.PersonAnimation;
import city.animations.interfaces.AnimatedPerson.Command;
import city.buildings.BankBuilding;
import city.buildings.HouseBuilding;
import city.gui.exteriors.CityViewBuilding;
import city.gui.exteriors.CityViewHouse;
import city.gui.interiors.BankPanel;
import city.gui.interiors.HousePanel;
import city.roles.LandlordRole;
import city.roles.ResidentRole;
import city.tests.agents.mocks.MockPerson;
import city.tests.buildings.mocks.MockBusStop;
import city.tests.roles.mocks.MockCityViewBuilding;

public class HouseAnimationTest extends TestCase {
	
	// Needed things
	private Date date;
	private CityViewHouse houseCityViewBuilding; // does nothing, no gui really pops out...
	private HousePanel hp; // does nothing, no gui really pops out...
	private BankPanel bp;
	private LandlordRole landlord;
	private ResidentRole resident;
	private BankBuilding bank;	
	private CityViewBuilding bankCityViewBuilding;
	private CityViewBuilding busstopCityViewBuilding;
	private CityViewBuilding busstopCityViewBuilding2;
	private MockBusStop busstop;
	private MockBusStop busstop2;
	
	
	// Being tested
	private MockPerson person;
	private PersonAnimation homeAnimation;
	private HouseBuilding house;

	
	public void setUp() throws Exception {
		//super duper standard setup
		System.out.println("===================== Setup =====================");
		super.setUp();
		
		date = new Date(0);
		//you need this to make a person...
		bankCityViewBuilding =  new MockCityViewBuilding();
		busstopCityViewBuilding = new MockCityViewBuilding();
		busstopCityViewBuilding2 = new MockCityViewBuilding();
		bank = new BankBuilding("MockBank", bp, bankCityViewBuilding);
		busstop = new MockBusStop("busstop");
		busstop2 = new MockBusStop("busstop2");
		
		Application.CityMap.clearMap();
		
		//needed things
		resident = new ResidentRole(date);
		landlord = new LandlordRole();
		resident.setPerson(person);
		landlord.setPerson(person);
		bank.setCityViewBuilding(bankCityViewBuilding);
		busstop.setCityViewBuilding(busstopCityViewBuilding);
		busstop2.setCityViewBuilding(busstopCityViewBuilding2);
		busstopCityViewBuilding.setX(40);
		busstopCityViewBuilding.setY(40);
		busstopCityViewBuilding2.setX(80);
		busstopCityViewBuilding2.setY(80);
		Application.CityMap.addBuilding(BUILDING.busStop, busstop);
		Application.CityMap.addBuilding(BUILDING.busStop, busstop2);
		Application.CityMap.addBuilding(BUILDING.bank, bank);
				
		//sort of relevant things
		houseCityViewBuilding = new CityViewHouse(10, 10);
		homeAnimation = new PersonAnimation();
		house = new HouseBuilding("House", landlord, hp, houseCityViewBuilding);
		person = new MockPerson("MovingPerson", date, homeAnimation, house);
		person.setCash(0); // so he doesn't go to market or restaurant
		
		resident.setPerson(person);
		//person.addRole(resident);
		//person.setHomeAnimation(homeAnimation);
		//person.setOccupation(null); // jobless, but is landlord TODO why doesn't this work now? no jobless people?
		
		//And the house, which is the real deal.
		Application.CityMap.addBuilding(BUILDING.house,house);
		house.setCityViewBuilding(houseCityViewBuilding);
		HashMap<FOOD_ITEMS, Integer> foods = new HashMap<FOOD_ITEMS, Integer>();
		foods.put(FOOD_ITEMS.chicken, 5); // put one chicken in the refrigerator to eat.
		foods.put(FOOD_ITEMS.salad, 0);
		foods.put(FOOD_ITEMS.steak, 0);
		foods.put(FOOD_ITEMS.pizza, 0); // there is no other food in the refrigerator
		house.setFood(person, foods);
	}
	
	public void testMovementInHouse() throws InterruptedException{
		System.out.println("");
		System.out.println("===================== Testing Animation Message Calls in House =====================");
		System.out.println("");
		
		//sanity background check
		assertEquals("There should be 5 chickens in the refrigerator of the house", (int)house.getFoodItems(person).get(FOOD_ITEMS.chicken), 5);
		assertEquals("There should be 0 steaks in the refrigerator of the house", (int)house.getFoodItems(person).get(FOOD_ITEMS.steak), 0);
		assertEquals("There should be 0 pizzas in the refrigerator of the house", (int)house.getFoodItems(person).get(FOOD_ITEMS.pizza), 0);
		assertEquals("There should be 0 salads in the refrigerator of the house", (int)house.getFoodItems(person).get(FOOD_ITEMS.salad), 0);
		assertEquals("Person should not have eaten", false, person.getHasEaten());
		assertTrue("Person should have a ResidentRole", person.getResidentRole() != null);
		assertTrue("Person should have a BankCustomerRole", person.getBankCustomerRole() != null);
		assertEquals("Person should have a home that is a house", house, person.getHome());
		//assertTrue("Person should have a home animation (the one we set)", person.getAnimationAtHome() != null);//TODO fix this

		//person hasn't eaten yet. Let's make him check the refrigerator. Actually we'll do that after we test a few things...
		assertEquals("Person shouldn't've eaten", person.getHasEaten(), false);
		assertEquals("Person's STATES should be STATES.none", Person.STATES.none, person.getState());
		boolean outcome;
		outcome = person.runScheduler();
		// Person should be doing a daily task (going home to cook)
		assertEquals("Person scheduler should continue running", person.runScheduler(), true);
		assertEquals("Person scheduler should continue running", person.runScheduler(), true); // goes to refrig, stove, table...
		assertEquals("Person's homeAnimation should have STATES noCommand", homeAnimation.getCommand(), "ToRef");
		
		assertEquals("Scheduler should return false now", person.runScheduler(), false);
		if(outcome){} // no warning saying outcome is useless now...
		//Fast forward time
		date.setTime(date.getTime() + (Application.HALF_HOUR * 36)); // go 9 hours later, so you want to go to sleep
		person.setDate(date);
		//now person is at home BTW
		homeAnimation.setCoords(HousePanel.HDX, HousePanel.HDY);
		assertEquals("xPos was just set to door's x", homeAnimation.getXPos(), HousePanel.HDX);
		assertEquals("yPos was just set to door's y", homeAnimation.getYPos(), HousePanel.HDY);
		assertEquals("The person wants to sleep", homeAnimation.getCommand(), "ToBed");
		//i would set the coords to the room entrance but for the house, it's HDX,HDY anyways.
		
		//Now go to bed to sleep.
		homeAnimation.setAcquired();
		homeAnimation.goToSleep();
		assertEquals("Command of home animation should be ToBed", homeAnimation.getCommand(), Command.ToBed.toString()); // STATES confirmed.
		homeAnimation.setCoords(HousePanel.HBXi-20, HousePanel.HBYi);
		assertEquals("xDest = xPos", homeAnimation.getXPos(), homeAnimation.getDestination()[0]);
		assertEquals("yDest = yPos", homeAnimation.getYPos(), homeAnimation.getDestination()[1]);
		homeAnimation.updatePosition(); // change command
		assertEquals("Command of home animation should be InBed", homeAnimation.getCommand(), Command.InBed.toString()); // STATES confirmed. "sleeping!"
		
		homeAnimation.setAcquired();
		homeAnimation.verifyFood(); // now we check cooking and eating procedure
		// let's check movement really fast
		homeAnimation.setCoords(HousePanel.HRX-2+20, HousePanel.HRY-2);
		homeAnimation.updatePosition();
		assertEquals("xDest = xPos", homeAnimation.getXPos(), homeAnimation.getDestination()[0]-1);
		assertEquals("yDest = yPos", homeAnimation.getYPos(), homeAnimation.getDestination()[1]-1);
		homeAnimation.setCoords(HousePanel.HRX+20+2, HousePanel.HRY+2);
		homeAnimation.updatePosition();
		assertEquals("xDest = xPos", homeAnimation.getXPos(), homeAnimation.getDestination()[0]+1);
		assertEquals("yDest = yPos", homeAnimation.getYPos(), homeAnimation.getDestination()[1]+1);
		
		homeAnimation.setAcquired(); // then the person would tell the animation to cookAndEatFood after acquiring the semaphore...
		homeAnimation.cookAndEatFood(FOOD_ITEMS.steak.toString()); 
		assertEquals("Command of home animation should be ToRef", homeAnimation.getCommand(), Command.ToRef.toString()); // STATES confirmed
		homeAnimation.setCoords(HousePanel.HRX+20, HousePanel.HRY);
		assertEquals("xDest = xPos", homeAnimation.getXPos(), homeAnimation.getDestination()[0]);
		assertEquals("yDest = yPos", homeAnimation.getYPos(), homeAnimation.getDestination()[1]);
		homeAnimation.updatePosition(); // update command to toTable
		assertEquals("Status should say Found steak", "Found steak", homeAnimation.getStatus()); // check status

		assertEquals("Command of home animation should be ToStove", homeAnimation.getCommand(), Command.ToStove.toString()); // STATES confirmed
		homeAnimation.setCoords(HousePanel.HSX+20, HousePanel.HSY);
		assertEquals("xDest = xPos", homeAnimation.getXPos(), homeAnimation.getDestination()[0]);
		assertEquals("yDest = yPos", homeAnimation.getYPos(), homeAnimation.getDestination()[1]);
		homeAnimation.updatePosition();
		assertEquals("Status should say Cooking steak", "Cooking steak", homeAnimation.getStatus()); // check status (with mockperson, can not skip "cooking");
		
		assertEquals("Command of home animation should be ToTable", homeAnimation.getCommand(), Command.StationaryAtStove.toString()); // STATES confirmed
		Thread.sleep(5000); // cooking
		assertEquals("Command of home animation should be ToTable", homeAnimation.getCommand(), Command.ToTable.toString()); // cooked; confirmed
		homeAnimation.setCoords(HousePanel.HTX+20, HousePanel.HTY);
		assertEquals("xDest = xPos", homeAnimation.getXPos(), homeAnimation.getDestination()[0]);
		assertEquals("yDest = yPos", homeAnimation.getYPos(), homeAnimation.getDestination()[1]); // teleport to the table now
		homeAnimation.updatePosition(); // updated
		assertEquals("Status should say Eating steak", "Eating steak", homeAnimation.getStatus()); // eating...;
		Thread.sleep(5000); // go grab a drink				
		homeAnimation.updatePosition();
		assertEquals("Status should say nothing", "", homeAnimation.getStatus()); // check status (skip "cooking");
		assertEquals("Command of home animation should be noCommand", homeAnimation.getCommand(), Command.noCommand.toString()); // STATES confirmed. Done!
				
		//Now leave the house.
		homeAnimation.goOutside();
		assertTrue("xDest = xPos", homeAnimation.getXPos() != homeAnimation.getDestination()[0]);
		assertFalse("yDest = yPos", homeAnimation.getYPos() == homeAnimation.getDestination()[1]);
		assertEquals("Command of home animation should be ToDoor", homeAnimation.getCommand(), Command.ToDoor.toString());	
		homeAnimation.setCoords(HousePanel.HDX, HousePanel.HDY+10);
		assertEquals("xDest = xPos", homeAnimation.getXPos(), homeAnimation.getDestination()[0]);
		assertEquals("yDest = yPos", homeAnimation.getYPos(), homeAnimation.getDestination()[1]);
		homeAnimation.updatePosition();
		assertEquals("Command of home animation should be noCommand", homeAnimation.getCommand(), Command.noCommand.toString()); // STATES confirmed. leaving!
		homeAnimation.updatePosition();
		}}
