package city.tests.buildings.mocks;

import city.agents.interfaces.Person;
import city.bases.Animation;
import city.bases.interfaces.AnimationInterface;
import city.buildings.interfaces.House;
import city.tests.bases.mocks.MockResidenceBuilding;

public class MockHouse extends MockResidenceBuilding implements House {

	public MockHouse(String name) {
		super(name);
	}

	@Override
	public <T extends AnimationInterface> T getOccupyingPersonAnimation(
			Person r, Class<T> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHomeAnimationName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setHomeAnimationName(String c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean occupyingPersonExists(Person p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeOccupyingPerson(Person r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addOccupyingPerson(Person p, Animation a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addOccupyingPerson(Person p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getIsFull() {
		// TODO Auto-generated method stub
		return false;
	}

}
