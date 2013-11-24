package city.tests;

import java.util.Date;

import junit.framework.TestCase;
import city.agents.CarAgent;
import city.agents.PersonAgent;
import city.buildings.AptBuilding;
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
	
	/**
	 * This tests the functionality of HouseBuilding, ResidentRole, and uses a MockLandlord to collect rent/maintenance.
	 * Tests: Creation of objects, runScheduler() for ResidentRole, checking if rent needs to be paid, paying rent, losing $,
	 * Landlord getting $ for it, but not the $ from maintenance, which is a net loss. Also the division of maintenance fees over people
	 * MockResident doesn't pay his share, but ResidentRole does (since its thread runs), and he pays his half-share ($10). 
	 */
	public void testHouseRentMaintenancePayAndReceive(){
		System.out.println("===============================");
		System.out.println("Testing HouseBuilding");
		System.out.println("===============================");
		Date date = new Date(0);
		PersonAgent p1 = new PersonAgent("Resident 1", date);
		p1.setCash(1000);
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
		p1r1.setResidence(hb);
		p2r1.setResidence(hb);
		p1r1.setLandlord(p2rL);
		p2r1.setLandlord(p2rL);
		
		p1.setDate(new Date(3360000)); // let's time-travel to a future date i.e. let's go to when rent is due...		
		
		System.out.println("Payer funds before rent: " + p1r1.getPerson().getCash());
		System.out.println("Landlord funds before rent: " + p2rL.mockcash);
		assertTrue("before paying rent, rentIsDue should return true", p1r1.rentIsDue());
		p1r1.payRent();
		System.out.println("Payer funds after rent: " + p1r1.getPerson().getCash());
		System.out.println("Landlord funds after rent: " + p2rL.mockcash);
		assertTrue("After paying rent, rentIsDue should return false", !p1r1.rentIsDue());
	}
	
	/**
	 * This tests the functionality of AptBuilding, ResidentRole, and uses a MockLandlord to collect rent/maintenance.
	 * Tests: Creation of objects, runScheduler() for ResidentRole, checking if rent needs to be paid, paying rent, losing $,
	 * Landlord getting $ for it, but not the $ from maintenance, which is a net loss. Also the division of maintenance fees over people
	 * MockResident doesn't pay his share, but ResidentRole does (since its thread runs), and he pays his half-share ($10). 
	 */
	public void testApartment(){
		System.out.println("===============================");
		System.out.println("Testing AptBuilding");
		System.out.println("===============================");
		Date date = new Date(0);
		PersonAgent p1 = new PersonAgent("Resident 1", date);
		p1.setCash(1000);
		p1.setDate(date);
		ResidentRole p1r1 = new ResidentRole();
		p1.setOccupation(p1r1);
		p1.startThread();

		MockResident p2r1 = new MockResident();
		MockLandlord p2rL = new MockLandlord();
		AptBuilding hb = new AptBuilding("House 1", p2rL);
		p2rL.addResident(p2r1);
		p2rL.addResident(p1r1);
		hb.residents.add(p2r1);
		hb.residents.add(p1r1);
		hb.total_current_maintenance=20;
		p1r1.setResidence(hb);
		p2r1.setResidence(hb);
		p1r1.setLandlord(p2rL);
		p2r1.setLandlord(p2rL);
		
		p1.setDate(new Date(3360000)); // let's time-travel to a future date i.e. let's go to when rent is due...		
		
		System.out.println("Payer funds before rent: " + p1r1.getPerson().getCash());
		System.out.println("Landlord funds before rent: " + p2rL.mockcash);
		assertTrue("before paying rent, rentIsDue should return true", p1r1.rentIsDue());
		p1r1.payRent();
		System.out.println("Payer funds after rent: " + p1r1.getPerson().getCash());
		System.out.println("Landlord funds after rent: " + p2rL.mockcash);
		assertTrue("After paying rent, rentIsDue should return false", !p1r1.rentIsDue());
	}
}
