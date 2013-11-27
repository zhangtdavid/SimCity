package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class CityViewBusStop extends CityViewBuilding {
	
	public CityViewBusStop(int x, int y) {
		super(x, y, Color.white, "Restaurant 1");
		rectangle = new Rectangle(x, y, 50, 50);
	}
	
	public CityViewBusStop(int x, int y, String ID, Color color, BuildingCard b) {
		super(x, y, color, ID, b);
		rectangle = new Rectangle(x, y, 50, 50);
	}
	
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}
}
