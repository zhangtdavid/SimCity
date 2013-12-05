package city.tests.animations;

import java.util.Date;

import city.Application;
import city.Application.BUILDING;
import city.agents.PersonAgent;
import city.animations.AptResidentAnimation;
import city.buildings.ApartmentBuilding;
import city.gui.exteriors.CityViewBuilding;
import city.gui.interiors.AptPanel;
import city.roles.LandlordRole;
import city.roles.ResidentRole;
import city.tests.animations.mocks.MockAnimatedPerson;


public class AptAnimationTest {

	// Needed things
	private Date date;
	private CityViewBuilding aptCityViewBuilding; // does nothing, no gui really pops out...
	private AptPanel hp; // does nothing, no gui really pops out...
	private MockAnimatedPerson animation;
	private LandlordRole landlord;
	private ResidentRole resident;
	
	// Being tested
	private PersonAgent person;
	private AptResidentAnimation homeAnimation;
	private ApartmentBuilding house;

	public void setUp() throws Exception {
		date = new Date(0);

		person = new PersonAgent("MovingPerson", date);
		animation = new MockAnimatedPerson();
		homeAnimation = new AptResidentAnimation(person);
		person.addRole(resident);
		resident.setLandlord(landlord);
		
		house = new ApartmentBuilding("Apartment", landlord, hp, aptCityViewBuilding);
		Application.CityMap.clearMap();
		Application.CityMap.addBuilding(BUILDING.house,house); // TODO incorporate a base class? rename house as residence 
		person.setHome(house);
		
		// Set up test environment
	}
}
