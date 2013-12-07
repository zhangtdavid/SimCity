package city.bases.interfaces;

import java.util.List;
import java.util.Map;

import city.Application.FOOD_ITEMS;
import city.agents.interfaces.Person;
import city.bases.Animation;
import city.roles.interfaces.Landlord;
import city.roles.interfaces.Resident;

public interface ResidenceBuildingInterface extends BuildingInterface {
	
	// Getters
	
	public Landlord getLandlord();
	public List<Resident> getResidents();
	public int getTotalCurrentMaintenance();
	public int getRent();
	public Map<FOOD_ITEMS, Integer> getFoodItems();
	public <T extends AnimationInterface> T getOccupyingPersonAnimation(Person r, Class<T> type);
	public String getHomeAnimationName();
	public boolean getIsFull();
	
	// Setters

	public void setRent(int r);
	public void setLandlord(Landlord l);
	public void addResident(Resident r);
	public void setTotalCurrentMaintenance(int m);
	public void setFood(Map<FOOD_ITEMS, Integer> items);
	public void setHomeAnimationName(String c);
	
	// Utilities

	public void addFood(FOOD_ITEMS f, int i);
	public void removeFood(FOOD_ITEMS f, int i);
	public void addFood(Map<FOOD_ITEMS, Integer> receivedItems);
	public boolean occupyingPersonExists(Person p);
	public void removeOccupyingPerson(Person r);
	public void addOccupyingPerson(Person p, Animation a);
	public void addOccupyingPerson(Person p);

}
