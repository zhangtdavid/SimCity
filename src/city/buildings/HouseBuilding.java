package city.buildings;

import java.util.HashMap;
import java.util.Map;

import city.Application.FOOD_ITEMS;
import city.Building;
import city.Role;

public class HouseBuilding extends Building {
	
	// Data
	
	public Map<FOOD_ITEMS, Integer> foodItems = new HashMap<FOOD_ITEMS, Integer>();
	
	// Constructor
	
	public HouseBuilding(String name, Role manager) {
		super(name);
		// TODO Auto-generated constructor stub
	}

}
