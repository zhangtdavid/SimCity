package city.gui.exteriors;

import java.awt.Color;
import java.awt.Rectangle;

import city.gui.BuildingCard;

public class CityViewMarket extends CityViewBuilding{	
	public CityViewMarket(int x, int y, String ID, Color color, BuildingCard b) {
		super(x, y, color, ID, b);
		setRectangle(new Rectangle(x, y, 20, 20));
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

}
