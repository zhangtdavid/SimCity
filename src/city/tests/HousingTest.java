package city.tests;

import java.util.Date;

import junit.framework.TestCase;
import city.agents.CarAgent;
import city.agents.PersonAgent;
import city.buildings.HouseBuilding;
import city.roles.LandlordRole;
import city.roles.ResidentRole;
import city.tests.mock.MockLandlord;
import city.tests.mock.MockPerson;
import city.tests.mock.MockResident;


public class HousingTest extends TestCase{
	MockPerson person = new MockPerson("PersonName"); 
	
	public void setUp() throws Exception {
		super.setUp();
		
	}
	
	public void testHouse(){
		Date date = new Date(0);
		PersonAgent p1 = new PersonAgent("Resident 1", date);
		p1.setDate(date);
		ResidentRole p1r1 = new ResidentRole();
		p1.setOccupation(p1r1);
		p1.startThread();
		
		MockResident p2r1 = new MockResident();
		MockLandlord p2rL = new MockLandlord();
		HouseBuilding hb = new HouseBuilding("House 1", p2rL);
		p2rL.addResident(p2r1);
		p2rL.addResident(p1r1);
		hb.residents.add(p2r1);
		hb.residents.add(p1r1);
		hb.total_current_maintenance=20;
		p1r1.setHouse(hb);
		p2r1.setHouse(hb);
		p1r1.setLandlord(p2rL);
		p2r1.setLandlord(p2rL);
		
		System.out.println(p1r1.rentIsDue());
		System.out.println("Payer funds before rent: " + p1r1.getPerson().getCash());
		System.out.println("Landlord funds before rent: " + p2rL.moneyAvailable);
		p1r1.payRent();
		System.out.println("Payer funds after rent: " + p1r1.getPerson().getCash());
		System.out.println("Landlord funds after rent: " + p2rL.moneyAvailable);
		System.out.println(p1r1.rentIsDue());
	}
	
	public void testApartment(){
		
	}
}
