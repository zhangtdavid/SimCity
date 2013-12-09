package city.bases.interfaces;

import java.util.HashMap;
import java.util.List;

import city.Application.FOOD_ITEMS;
import city.agents.interfaces.Person;
import city.roles.interfaces.Landlord;
import city.roles.interfaces.Resident;

public interface ResidenceBuildingInterface extends BuildingInterface {
	
	// Getters
	
	public Landlord getLandlord();
	public List<Resident> getResidents();
	public int getTotalCurrentMaintenance();
	public int getRent();
	public HashMap<FOOD_ITEMS, Integer> getFoodItems(Person p);
	public boolean getIsFull();
	
	// Setters

	public void setRent(int r);
	public void setLandlord(Landlord l);
	public void setTotalCurrentMaintenance(int m);
	public void setFood(Person p, HashMap<FOOD_ITEMS, Integer> items);
	
	// Utilities

	public void addResident(Resident r);
	public void removeResident(Resident r);
	public boolean removeFood(Person p, FOOD_ITEMS f, int i);

}
