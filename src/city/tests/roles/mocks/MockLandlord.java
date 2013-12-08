package city.tests.roles.mocks;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import city.bases.ResidenceBuilding;
import city.roles.interfaces.Landlord;
import city.roles.interfaces.Resident;
import city.tests.bases.mocks.MockRole;

public class MockLandlord extends MockRole implements Landlord{
	

	Resident landlordRes; // which of the residents is the landlord? enables easy handing-off of lordship
	ResidenceBuilding house; // the landlord is the landlord of this building
	List<Resident> residents = Collections.synchronizedList(new ArrayList<Resident>());
	public int mockcash;
	
	public MockLandlord(){
		super();
	}
	
	@Override
	public void msgHeresRent(int d) {		
		mockcash += d;	
		}


	@Override
	public void setRent(int d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PropertyChangeSupport getPropertyChangeSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStateString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addResidence(ResidenceBuilding b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeResidence(ResidenceBuilding b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ResidenceBuilding> getResidences() {
		return null;		
	}

	@Override
	public void setResident(Resident r) {
		// TODO Auto-generated method stub
		
	}

}
