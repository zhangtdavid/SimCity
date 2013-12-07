package city.gui.exteriors;

import java.awt.Color;
import java.awt.Rectangle;

import city.gui.BuildingCard;

public class CityViewApt extends CityViewBuilding {
	public CityViewApt(int x, int y) {
		super(x, y, Color.WHITE, "Apt 1");
		setRectangle(new Rectangle(x, y, 25, 25));
	}
	
	public CityViewApt(int x, int y, String ID, Color color, BuildingCard b) {
		super(x, y, color, ID, b);
		setRectangle(new Rectangle(x, y, 25, 25));
	}
	
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

}
