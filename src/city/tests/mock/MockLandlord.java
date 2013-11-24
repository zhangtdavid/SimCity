package city.tests.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import city.MockRole;
import city.buildings.HouseBuilding;
import city.interfaces.Landlord;
import city.interfaces.Resident;

public class MockLandlord extends MockRole implements Landlord{
	

	Resident landlordRes; // which of the residents is the landlord? enables easy handing-off of lordship
	HouseBuilding house; // the landlord is the landlord of this building
	List<Resident> residents = Collections.synchronizedList(new ArrayList<Resident>());
	
	public MockLandlord(){
		super();
	}
	
	@Override
	public void msgHeresRent(int d) {		
		this.getPerson().setCash(this.getPerson().getCash()+d);	
		}

	@Override
	public void addResident(Resident r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeResident(Resident r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeLandlord(Resident r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHouse(HouseBuilding b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRent(int d) {
		// TODO Auto-generated method stub
		
	}

}
