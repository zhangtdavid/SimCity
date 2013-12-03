package city.gui.views;

import java.awt.Color;
import java.awt.Rectangle;

import city.gui.BuildingCard;

public class CityViewBusStop extends CityViewBuilding {
	
	public CityViewBusStop(int x, int y) {
		super(x, y, Color.white, "Restaurant 1");
		setRectangle(new Rectangle(x, y, 50, 50));
	}
	
	public CityViewBusStop(int x, int y, String ID, Color color, BuildingCard b) {
		super(x, y, color, ID, b);
		setRectangle(new Rectangle(x, y, 50, 50));
	}
	
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}
}
