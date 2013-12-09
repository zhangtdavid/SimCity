package city.tests.roles;

import java.awt.Color;

import junit.framework.TestCase;
import city.buildings.RestaurantZhangBuilding;
import city.gui.exteriors.CityViewRestaurantZhang;
import city.gui.interiors.RestaurantZhangPanel;
import city.roles.WalkerRole;
import city.tests.agents.mocks.MockPerson;
import city.tests.animations.mocks.MockAnimatedWalker;

public class WalkerTest extends TestCase {
	MockPerson person;
	RestaurantZhangPanel RestaurantZhangPanel = new RestaurantZhangPanel(Color.black);
	RestaurantZhangBuilding destination;
	WalkerRole walker;
	MockAnimatedWalker anim;

	public void setUp() throws Exception {
		super.setUp();
		person = new MockPerson("PersonName");
		destination = new RestaurantZhangBuilding("RestZ", RestaurantZhangPanel, new CityViewRestaurantZhang(0, 0, "City View RestZ", Color.black, RestaurantZhangPanel));
		walker = new WalkerRole(destination);
		anim = new MockAnimatedWalker(walker);
		walker.setAnimation(anim);
		walker.setPerson(person);
	}

	public void testOneNormalWalkerScenario() {
		// Step 0: Check variables before startup
		assertFalse("Walker scheduler should have returned false. It didn't", walker.runScheduler());
		assertEquals("Walker's destination should be set. It isn't", walker.getDestination(), destination);
		assertEquals("Walker's state should be NOTWALKING. Instead it's: " + walker.getState().name(), walker.getState(), WalkerRole.WalkerState.NOTWALKING);
		assertEquals("Walker's event should be NONE. Instead it's: " + walker.getEvent().name(), walker.getEvent(), WalkerRole.WalkerEvent.NONE);

		// Step 1: Make the walker's role active
		walker.setActive();
		assertEquals("Walker's destination should be set. It isn't", walker.getDestination(), destination);
		assertEquals("Walker's state should be WALKING. Instead it's: " + walker.getState().name(), walker.getState(), WalkerRole.WalkerState.WALKING);
		assertEquals("Walker's event should be STARTINGTOWALK. Instead it's: " + walker.getEvent().name(), walker.getEvent(), WalkerRole.WalkerEvent.STARTINGTOWALK);
		assertTrue("Walker's animation logged the wrong thing. It reads " + anim.log.getLastLoggedEvent(), anim.log.getLastLoggedEvent().toString().contains("Walker PersonName is going to destination RestZ"));

		// Step 2: Msg the walker that he's at destination
		walker.msgImAtDestination();
		assertEquals("Walker's destination should be set. It isn't", walker.getDestination(), destination);
		assertEquals("Walker's state should be WALKING. Instead it's: " + walker.getState().name(), walker.getState(), WalkerRole.WalkerState.WALKING);
		assertEquals("Walker's event should be ATDESTINATION. Instead it's: " + walker.getEvent().name(), walker.getEvent(), WalkerRole.WalkerEvent.ATDESTINATION);

		// Step 3: Run the scheduler
		walker.runScheduler();
		assertEquals("Walker's destination should be set. It isn't", walker.getDestination(), destination);
		assertEquals("Walker's state should be NOTWALKING. Instead it's: " + walker.getState().name(), walker.getState(), WalkerRole.WalkerState.NOTWALKING);
		assertEquals("Walker's event should be NONE. Instead it's: " + walker.getEvent().name(), walker.getEvent(), WalkerRole.WalkerEvent.NONE);
	}

}
