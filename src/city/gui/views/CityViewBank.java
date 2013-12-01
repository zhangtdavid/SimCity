package city.gui.views;

import java.awt.Color;
import java.awt.Rectangle;

import city.gui.BuildingCard;

public class CityViewBank extends CityViewBuilding {
	
	public CityViewBank(int x, int y) {
		super(x, y, Color.red, "Bank 1");
		rectangle = new Rectangle(x, y, 50, 50);
	}
	
	public CityViewBank(int x, int y, String ID, Color color, BuildingCard b) {
		super(x, y, color, ID, b);
		rectangle = new Rectangle(x, y, 50, 50);
	}
	
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}
}
