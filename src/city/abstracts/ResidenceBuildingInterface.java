package city.abstracts;

import java.util.List;
import java.util.Map;

import city.BuildingInterface;
import city.Application.FOOD_ITEMS;
import city.interfaces.Landlord;
import city.interfaces.Resident;

public interface ResidenceBuildingInterface extends BuildingInterface {
	
	// Getters
	
	public Landlord getLandlord();
	public List<Resident> getResidents();
	public int getTotalCurrentMaintenance();
	public int getRent();
	public Map<FOOD_ITEMS, Integer> getFoodItems();

	// Setters

	public void setRent(int r);
	public void setLandlord(Landlord l);
	public void addResident(Resident r);
	public void setTotalCurrentMaintenance(int m);
	public void setFood(Map<FOOD_ITEMS, Integer> items);

	// Utilities

	public void addFood(FOOD_ITEMS f, int i);
	public void addFood(Map<FOOD_ITEMS, Integer> receivedItems);

}
