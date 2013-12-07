package city.tests.animations;

import java.util.Date;
import java.util.HashMap;

import junit.framework.TestCase;
import city.Application;
import city.Application.BUILDING;
import city.Application.FOOD_ITEMS;
import city.agents.PersonAgent;
import city.agents.interfaces.Person;
import city.animations.AptResidentAnimation;
import city.animations.HouseResidentAnimation;
import city.animations.interfaces.AnimatedPersonAtHome.Command;
import city.buildings.AptBuilding;
import city.buildings.BankBuilding;
import city.gui.exteriors.CityViewApt;
import city.gui.exteriors.CityViewBuilding;
import city.gui.interiors.AptPanel;
import city.gui.interiors.BankPanel;
import city.roles.LandlordRole;
import city.roles.ResidentRole;
import city.tests.animations.mocks.MockAnimatedPerson;
import city.tests.buildings.mocks.MockBusStop;
import city.tests.roles.mocks.MockCityViewBuilding;


public class AptAnimationTest extends TestCase{

	// Needed things
	private Date date;
	private CityViewBuilding aptCityViewBuilding; // does nothing, no gui really pops out...
	private AptPanel hp; // does nothing, no gui really pops out...
	private CityViewApt houseCityViewBuilding; // does nothing, no gui really pops out...
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
	private AptResidentAnimation homeAnimation;
	private AptBuilding apt;

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
		houseCityViewBuilding = new CityViewApt(10, 10);
		person = new PersonAgent("MovingPerson", date);
		person.setCash(0); // so he doesn't go to market or restaurant
		animation = new MockAnimatedPerson();
		person.setRoomNumber(1);
		homeAnimation = new AptResidentAnimation(person);
		resident.setPerson(person);
		person.addRole(resident);
		person.setAnimation(animation);
		person.setHomeAnimation(homeAnimation);
		resident.setLandlord(landlord);
		//person.setOccupation(null); // jobless, but is landlord TODO why doesn't this work now? no jobless people?

		//And the house, which is the real deal.
		apt = new AptBuilding("House", landlord, hp, houseCityViewBuilding);
		Application.CityMap.addBuilding(BUILDING.house,apt);
		apt.setCityViewBuilding(houseCityViewBuilding);
		person.setHome(apt);
		HashMap<FOOD_ITEMS, Integer> foods = new HashMap<FOOD_ITEMS, Integer>();
		foods.put(FOOD_ITEMS.chicken, 5); // put one chicken in the refrigerator to eat.
		foods.put(FOOD_ITEMS.salad, 0);
		foods.put(FOOD_ITEMS.steak, 0);
		foods.put(FOOD_ITEMS.pizza, 0); // there is no other food in the refrigerator
		apt.setFood(foods);
		AptResidentAnimation.beingTested = true; // turn off timers and semaphores
		HouseResidentAnimation.beingTested = true;
		System.out.println(homeAnimation.getBeingTested());
		// Set up test environment
	}
	
	public void testMovementInHouse() throws InterruptedException{

		System.out.println("");
		System.out.println("===================== Testing Animation Message Calls in House =====================");
		System.out.println("");
		
		//sanity background check
		assertEquals("There should be 5 chickens in the refrigerator of the house", (int)apt.getFoodItems().get(FOOD_ITEMS.chicken), 5);
		assertEquals("There should be 0 steaks in the refrigerator of the house", (int)apt.getFoodItems().get(FOOD_ITEMS.steak), 0);
		assertEquals("There should be 0 pizzas in the refrigerator of the house", (int)apt.getFoodItems().get(FOOD_ITEMS.pizza), 0);
		assertEquals("There should be 0 salads in the refrigerator of the house", (int)apt.getFoodItems().get(FOOD_ITEMS.salad), 0);
		assertEquals("Person should not have eaten", false, person.getHasEaten());
		assertTrue("Person should have a ResidentRole", person.getResidentRole() != null);
		assertTrue("Person should have a BankCustomerRole", person.getBankCustomerRole() != null);
		//assertEquals("Person should have 3 roles", person.getRoles().size(), 3); // TODO see first TODO
		assertEquals("Person should have a home that is a house", apt, person.getHome());
		assertTrue("Person should have a home animation (the one we set)", person.getAnimationAtHome() != null);

		//person hasn't eaten yet. Let's make him check the refrigerator. Actually we'll do that after we test a few things...
		assertEquals("Person shouldn't've eaten", person.getHasEaten(), false);
		assertEquals("Person's STATES should be STATES.none", Person.STATES.none, person.getState());
		boolean outcome;
		outcome = person.runScheduler();
		// Person should be doing a daily task (going home to cook)
		assertEquals("Person scheduler should continue running", person.runScheduler(), true);
		person.getBusPassengerRole().setInactive(); // HAS ARRIVED AT HOME
		assertEquals("Person scheduler should continue running", person.runScheduler(), true); // goes to refrig, stove, table...
		assertEquals("Person's homeAnimation should have STATES noCommand", homeAnimation.getCommand(), "noCommand");
		
		assertEquals("Scheduler should still be true", person.runScheduler(), true);
		if(outcome){} // no warning saying outcome is useless now...
		//Fast forward time
		date.setTime(date.getTime() + (Application.HALF_HOUR * 36)); // go 9 hours later, so you want to go to sleep
		person.setDate(date);
		//now person is at home BTW
		homeAnimation.setCoords(AptPanel.APT_DOOR[person.getRoomNumber()-1][0], AptPanel.APT_DOOR[person.getRoomNumber()-1][1]);
		assertEquals("xPos was just set to door's x", homeAnimation.getXPos(), AptPanel.APT_DOOR[person.getRoomNumber()-1][0]);
		assertEquals("yPos was just set to door's y", homeAnimation.getYPos(), AptPanel.APT_DOOR[person.getRoomNumber()-1][1]);
		assertEquals("Person's homeAnimation should have STATES noCommand before I tell it to go to room", homeAnimation.getCommand(), "noCommand");
		homeAnimation.goToRoom(0); // for a house, this does nothing but change your STATES
		assertEquals("Command of home Animation should be ToRoom", homeAnimation.getCommand(), Command.ToRoomEntrance.toString());
		//i would set the coords to the room entrance but for the house, it's HDX,HDY anyways.
		
		//Now go to bed to sleep.
		homeAnimation.setAcquired();
		homeAnimation.goToSleep();
		assertEquals("Command of home animation should be ToBed", homeAnimation.getCommand(), Command.ToBed.toString()); // STATES confirmed.
		homeAnimation.setCoords(AptPanel.APT_BED[person.getRoomNumber()-1][0]-20, AptPanel.APT_BED[person.getRoomNumber()-1][1]);
		assertEquals("xDest = xPos", homeAnimation.getXPos(), homeAnimation.getDestination()[0]);
		assertEquals("yDest = yPos", homeAnimation.getYPos(), homeAnimation.getDestination()[1]);
		homeAnimation.updatePosition(); // change command
		assertEquals("Command of home animation should be InBed", homeAnimation.getCommand(), Command.InBed.toString()); // STATES confirmed. "sleeping!"
		
		homeAnimation.setAcquired();
		homeAnimation.verifyFood(); // now we check cooking and eating procedure
		// let's check movement really fast
		homeAnimation.setCoords(AptPanel.APT_REFRIG[person.getRoomNumber()-1][0]-2, AptPanel.APT_REFRIG[person.getRoomNumber()-1][1]+20-2);
		homeAnimation.updatePosition();
		assertEquals("xDest = xPos", homeAnimation.getXPos(), homeAnimation.getDestination()[0]-1);
		assertEquals("yDest = yPos", homeAnimation.getYPos(), homeAnimation.getDestination()[1]-1);
		homeAnimation.setCoords(AptPanel.APT_REFRIG[person.getRoomNumber()-1][0]+2, AptPanel.APT_REFRIG[person.getRoomNumber()-1][1]+20+2);
		homeAnimation.updatePosition();
		assertEquals("xDest = xPos", homeAnimation.getXPos(), homeAnimation.getDestination()[0]+1);
		assertEquals("yDest = yPos", homeAnimation.getYPos(), homeAnimation.getDestination()[1]+1);
		
		homeAnimation.setAcquired(); // then the person would tell the animation to cookAndEatFood after acquiring the semaphore...
		homeAnimation.cookAndEatFood(FOOD_ITEMS.steak.toString()); 
		assertEquals("Command of home animation should be ToRef", homeAnimation.getCommand(), Command.ToRef.toString()); // STATES confirmed
		homeAnimation.setCoords(AptPanel.APT_REFRIG[person.getRoomNumber()-1][0], AptPanel.APT_REFRIG[person.getRoomNumber()-1][1]+20);
		assertEquals("xDest = xPos", homeAnimation.getXPos(), homeAnimation.getDestination()[0]);
		assertEquals("yDest = yPos", homeAnimation.getYPos(), homeAnimation.getDestination()[1]);
		homeAnimation.updatePosition(); // update command to toTable
		assertEquals("Status should say Found steak", "Found steak", homeAnimation.getStatus()); // check status

		assertEquals("Command of home animation should be ToStove", homeAnimation.getCommand(), Command.ToStove.toString()); // STATES confirmed
		homeAnimation.setCoords(AptPanel.APT_STOVE[person.getRoomNumber()-1][0], AptPanel.APT_STOVE[person.getRoomNumber()-1][1]+20);
		assertEquals("xDest = xPos", homeAnimation.getXPos(), homeAnimation.getDestination()[0]);
		assertEquals("yDest = yPos", homeAnimation.getYPos(), homeAnimation.getDestination()[1]);
		homeAnimation.updatePosition();
		assertEquals("Status should say Going to eat steak", "Going to eat steak", homeAnimation.getStatus()); // check status (skip "cooking");
		
		assertEquals("Command of home animation should be ToTable", homeAnimation.getCommand(), Command.ToTable.toString()); // STATES confirmed
		homeAnimation.setCoords(AptPanel.APT_TABLE[person.getRoomNumber()-1][0], AptPanel.APT_TABLE[person.getRoomNumber()-1][1]+20);
		assertEquals("xDest = xPos", homeAnimation.getXPos(), homeAnimation.getDestination()[0]);
		assertEquals("yDest = yPos", homeAnimation.getYPos(), homeAnimation.getDestination()[1]);
		homeAnimation.updatePosition();
		assertEquals("Status should say nothing", "", homeAnimation.getStatus()); // check status (skip "cooking");
		assertEquals("Command of home animation should be noCommand", homeAnimation.getCommand(), Command.noCommand.toString()); // STATES confirmed. Done!
				
		//Now leave the house.
		homeAnimation.goOutside();
		assertTrue("xDest = xPos", homeAnimation.getXPos() != homeAnimation.getDestination()[0]);
		assertFalse("yDest = yPos", homeAnimation.getYPos() == homeAnimation.getDestination()[1]);
		assertEquals("Command of home animation should be noCommand", homeAnimation.getCommand(), Command.noCommand.toString());	
		homeAnimation.setCoords(AptPanel.APT_DOOR[person.getRoomNumber()-1][0]-10, AptPanel.APT_DOOR[person.getRoomNumber()-1][1]);
		assertEquals("xDest = xPos", homeAnimation.getXPos(), homeAnimation.getDestination()[0]);
		assertEquals("yDest = yPos", homeAnimation.getYPos(), homeAnimation.getDestination()[1]);
		homeAnimation.updatePosition();
		assertEquals("Command of home animation should be noCommand", homeAnimation.getCommand(), Command.noCommand.toString()); // STATES confirmed. leaving!
		homeAnimation.updatePosition();
		}
}
