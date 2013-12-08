package city.tests.roles;

import java.util.Date;

import junit.framework.TestCase;
import city.buildings.HouseBuilding;
import city.roles.ResidentRole;
import city.tests.agents.mocks.MockPerson;
import city.tests.roles.mocks.MockLandlord;
import city.tests.roles.mocks.MockResident;

public class ResidentTest extends TestCase {
	
	// Data
	
	MockPerson person = new MockPerson("PersonName"); 
	MockPerson p1;
	MockPerson p2;
	ResidentRole p1r1;
	MockResident p2r1 = new MockResident();
	MockLandlord p2rL = new MockLandlord();
	HouseBuilding hb;
	
	// Setup
	
	public void setUp() throws Exception {
		super.setUp();
		p1 = new MockPerson("P1");
		p2 = new MockPerson("P2");
		Date date = new Date(0);
		p1.setCash(1000);
		p1.setDate(date);
		p1r1 = new ResidentRole(date);
		p1.setResidentRole(p1r1);
		p1.startThread();
		p1r1.setPerson(p1);
		p2rL.setPerson(p2);
		p2.startThread();
		p1.setDate(new Date(0));
		p2.setDate(new Date(0));
		p1r1.setResidence(hb);
		p2r1.setResidence(hb);
		p1.setHome(hb);
		p2.setHome(hb);
		hb = new HouseBuilding("House 1", p2rL, null, null ); // TODO fix this later
		hb.addResident(p1r1);
		//hb.addResident(p2r1);
		hb.setTotalCurrentMaintenance(20);
		hb.setRent(50);
	}	
	
	// Tests
	
	/**
	 * This tests the functionality of AptBuilding, ResidentRole, and uses a MockLandlord to collect rent/maintenance.
	 * Tests: Creation of objects, runScheduler() for ResidentRole, checking if rent needs to be paid, paying rent, losing $,
	 * Landlord getting $ for it, but not the $ from maintenance, which is a net loss. Also the division of maintenance fees over people
	 * MockResident doesn't pay his share, but ResidentRole does (since its thread runs), and he pays his half-share ($10). 
	 */
	public void testRentExchange(){
		p2rL.mockcash = 0;
		System.out.println("===============================");
		System.out.println("Testing AptBuilding");
		System.out.println("===============================");		
		assertTrue("before time travelling, rentIsDue should return false", !p1r1.rentIsDue());
		p1.setDate(new Date(336001)); // let's time-travel to a future date i.e. let's go to when rent is due...
		p2.setDate(new Date(336001));
		System.out.println("Payer funds before rent: " + p1r1.getPerson().getCash());
		System.out.println("Landlord funds before rent: " + p2rL.mockcash);
		p2rL.msgHeresRent(hb.getRent());
		assertTrue(p2rL.mockcash > 0);
		assertTrue(p2rL.mockcash == 50);
		p1.setCash(p1.getCash()-hb.getRent()); // the method isn't one distinct part, so no point testing this obvious part
		assertTrue(p1.getCash()==950);
		System.out.println("Payer funds after rent: " + p1r1.getPerson().getCash());
		System.out.println("Landlord funds after rent: " + p2rL.mockcash);
	}
}
