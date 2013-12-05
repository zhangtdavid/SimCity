package city.gui.exteriors;

import java.awt.Color;
import java.awt.Rectangle;

import city.gui.BuildingCard;

public class CityViewHouse extends CityViewBuilding {

	public CityViewHouse(int x, int y) {
		super(x, y, Color.WHITE, "House 1");
		setRectangle(new Rectangle(x, y, 20, 20));
	}
	
	public CityViewHouse(int x, int y, String ID, Color color, BuildingCard b) {
		super(x, y, color, ID, b);
		setRectangle(new Rectangle(x, y, 20, 20));
	}
	
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

}
