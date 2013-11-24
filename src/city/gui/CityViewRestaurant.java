package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class CityViewRestaurant extends CityViewBuilding {
	
	public CityViewRestaurant(int x, int y) {
		super(x, y, Color.red, "Restaurant 1");
		rectangle = new Rectangle(x, y, 20, 20);
	}
	
	public CityViewRestaurant(int x, int y, String ID) {
		super(x, y, Color.red, ID);
		rectangle = new Rectangle(x, y, 20, 20);
	}
	
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}
}
