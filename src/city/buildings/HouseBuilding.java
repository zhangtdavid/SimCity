package city.buildings;

import java.util.HashMap;
import java.util.Map;

import city.Application.MARKET_ITEM;
import city.Building;
import city.Role;

public class HouseBuilding extends Building {
	
	// Data
	
	public Map<MARKET_ITEM, Integer> foodItems = new HashMap<MARKET_ITEM, Integer>();
	
	// Constructor
	
	public HouseBuilding(String name, Role manager) {
		super(name);
		// TODO Auto-generated constructor stub
	}

}
