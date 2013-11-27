package city.gui;

import java.awt.Color;
import java.awt.Rectangle;

public class CityViewRestaurant extends CityViewBuilding {
	
	public CityViewRestaurant(int x, int y) {
		super(x, y, Color.red, "Restaurant 1");
		rectangle = new Rectangle(x, y, 50, 50);
	}
	
	public CityViewRestaurant(int x, int y, String ID, Color color, BuildingCard b) {
		super(x, y, color, ID, b);
		rectangle = new Rectangle(x, y, 50, 50);
	}
	
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}
}
