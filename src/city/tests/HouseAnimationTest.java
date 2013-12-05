package city.tests;

import java.util.Date;
import java.util.HashMap;

import junit.framework.TestCase;
import city.Application;
import city.Application.BUILDING;
import city.Application.CityMap;
import city.Application.FOOD_ITEMS;
import city.agents.PersonAgent;
import city.animations.HouseResidentAnimation;
import city.animations.interfaces.AnimatedPersonAtHome.Command;
import city.buildings.BankBuilding;
import city.buildings.BusStopBuilding;
import city.buildings.HouseBuilding;
import city.gui.buildings.HousePanel;
import city.gui.buildings.BankPanel;
import city.gui.views.CityViewBuilding;
import city.gui.views.CityViewHouse;
import city.interfaces.Person;
import city.roles.LandlordRole;
import city.roles.ResidentRole;
import city.tests.animations.mock.MockAnimatedPerson;
import city.tests.mock.MockBank;
import city.tests.mock.MockBus;
import city.tests.mock.MockBusStop;
import city.tests.mock.MockCityViewBuilding;

public class HouseAnimationTest extends TestCase {
	
	// Needed things
	private Date date;
	private CityViewHouse houseCityViewBuilding; // does nothing, no gui really pops out...
	private HousePanel hp; // does nothing, no gui really pops out...
	private BankPanel bp;
	private MockAnimatedPerson animation;
	private LandlordRole landlord;
	private ResidentRole resident;
	private BankBuilding bank;	
	private CityViewBuilding bankCityViewBuilding;
	private CityViewBuilding busstopCityViewBuilding;
	private CityViewBuilding busstopCityViewBuilding2;
	private MockBusStop busstop;
	private MockBusStop busstop2;
	
	
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
		house.setCityViewBuilding(houseCityViewBuilding);
		person.setHome(house);
		HashMap<FOOD_ITEMS, Integer> foods = new HashMap<FOOD_ITEMS, Integer>();
		foods.put(FOOD_ITEMS.chicken, 5); // put one chicken in the refrigerator to eat.
		foods.put(FOOD_ITEMS.salad, 0);
		foods.put(FOOD_ITEMS.steak, 0);
		foods.put(FOOD_ITEMS.pizza, 0); // there is no other food in the refrigerator
		house.setFood(foods);
		homeAnimation.beingTested = true; // turn off timers and semaphores
		System.out.println(homeAnimation.getBeingTested());
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
		assertEquals("Person scheduler should continue running", person.runScheduler(), true);
		person.getBusPassengerRole().setInactive(); // HAS ARRIVED AT HOME
		assertEquals("Person scheduler should continue running", person.runScheduler(), true); // goes to refrig, stove, table...
		assertEquals("Person's homeAnimation should have state noCommand", homeAnimation.getCommand(), "noCommand");
		
		assertEquals("Scheduler should still be true", person.runScheduler(), true);

		//Fast forward time
		date.setTime(date.getTime() + (Application.HALF_HOUR * 36)); // go 9 hours later, so you want to go to sleep
		person.setDate(date);
		//now person is at home BTW
		homeAnimation.setCoords(HousePanel.HDX, HousePanel.HDY);
		assertEquals("xPos was just set to door's x", homeAnimation.getXPos(), HousePanel.HDX);
		assertEquals("yPos was just set to door's y", homeAnimation.getYPos(), HousePanel.HDY);
		assertEquals("Person's homeAnimation should have state noCommand before I tell it to go to room", homeAnimation.getCommand(), "noCommand");
		homeAnimation.goToRoom(0); // for a house, this does nothing but change your state
		assertEquals("Command of home Animation should be ToRoom", homeAnimation.getCommand(), Command.ToRoomEntrance.toString());
		//i would set the coords to the room entrance but for the house, it's HDX,HDY anyways.
		
		//Now go to bed to sleep.
		homeAnimation.setAcquired();
		homeAnimation.goToSleep();
		assertEquals("Command of home animation should be ToBed", homeAnimation.getCommand(), Command.ToBed.toString()); // state confirmed.
		homeAnimation.setCoords(HousePanel.HBXi, HousePanel.HBYi);
		assertEquals("xDest = xPos", homeAnimation.getXPos(), homeAnimation.getDestination()[0]);
		assertEquals("yDest = yPos", homeAnimation.getYPos(), homeAnimation.getDestination()[1]);
		homeAnimation.updatePosition(); // change command
		assertEquals("Command of home animation should be InBed", homeAnimation.getCommand(), Command.InBed.toString()); // state confirmed. "sleeping!"
		
		homeAnimation.setAcquired();
		homeAnimation.verifyFood();
		// let's check movement really fast
		homeAnimation.setCoords(HousePanel.HRX-2, HousePanel.HRY-2);
		homeAnimation.updatePosition();
		assertEquals("xDest = xPos", homeAnimation.getXPos(), homeAnimation.getDestination()[0]-1);
		assertEquals("yDest = yPos", homeAnimation.getYPos(), homeAnimation.getDestination()[1]-1);
		homeAnimation.setCoords(HousePanel.HRX+2, HousePanel.HRY+2);
		homeAnimation.updatePosition();
		assertEquals("xDest = xPos", homeAnimation.getXPos(), homeAnimation.getDestination()[0]+1);
		assertEquals("yDest = yPos", homeAnimation.getYPos(), homeAnimation.getDestination()[1]+1);
		
		assertEquals("Command of home Animation should be ToRef", homeAnimation.getCommand(), Command.ToRef.toString());
		homeAnimation.setCoords(HousePanel.HRX, HousePanel.HRY); // at refrigerator
		assertEquals("xDest = xPos", homeAnimation.getXPos(), homeAnimation.getDestination()[0]);
		assertEquals("yDest = yPos", homeAnimation.getYPos(), homeAnimation.getDestination()[1]);
		homeAnimation.updatePosition(); // now done with going to refrigerator.
		
		homeAnimation.setAcquired(); // then the person would tell the animation to cookAndEatFood after acquiring the semaphore...
		homeAnimation.cookAndEatFood(); 
		assertEquals("Command of home animation should be ToStove", homeAnimation.getCommand(), Command.ToStove.toString()); // state confirmed
		homeAnimation.setCoords(HousePanel.HSX, HousePanel.HSY);
		assertEquals("xDest = xPos", homeAnimation.getXPos(), homeAnimation.getDestination()[0]);
		assertEquals("yDest = yPos", homeAnimation.getYPos(), homeAnimation.getDestination()[1]);
		homeAnimation.updatePosition(); // update command to toTable
		assertEquals("Command of home animation should be ToTable", homeAnimation.getCommand(), Command.ToTable.toString()); // state confirmed
		homeAnimation.setCoords(HousePanel.HTX, HousePanel.HTY);
		assertEquals("xDest = xPos", homeAnimation.getXPos(), homeAnimation.getDestination()[0]);
		assertEquals("yDest = yPos", homeAnimation.getYPos(), homeAnimation.getDestination()[1]);
		homeAnimation.updatePosition();
		assertEquals("Command of home animation should be noCommand", homeAnimation.getCommand(), Command.noCommand.toString()); // state confirmed. Done!
				
		//Now leave the house.
		homeAnimation.goOutside();
		assertTrue("xDest = xPos", homeAnimation.getXPos() != homeAnimation.getDestination()[0]);
		assertFalse("yDest = yPos", homeAnimation.getYPos() == homeAnimation.getDestination()[1]);
		assertEquals("Command of home animation should be ToDoor", homeAnimation.getCommand(), Command.ToDoor.toString()); // state confirmed. leaving!		
		homeAnimation.setCoords(HousePanel.HDX, HousePanel.HDY);
		assertEquals("xDest = xPos", homeAnimation.getXPos(), homeAnimation.getDestination()[0]);
		assertEquals("yDest = yPos", homeAnimation.getYPos(), homeAnimation.getDestination()[1]);
		homeAnimation.updatePosition();
		assertEquals("Command of home animation should be AtDoor", homeAnimation.getCommand(), Command.AtDoor.toString()); // state confirmed. leaving!
		}
}
