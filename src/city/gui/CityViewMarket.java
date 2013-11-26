package city.gui;

import java.awt.Color;
import java.awt.Rectangle;

public class CityViewMarket extends CityViewBuilding{

	public CityViewMarket(int x, int y) {
		super(x, y, Color.BLUE, "Market 1");
		rectangle = new Rectangle(x, y, 20, 20);
	}
	
	public CityViewMarket(int x, int y, String ID, Color color, BuildingCard b) {
		super(x, y, color, ID, b);
		rectangle = new Rectangle(x, y, 20, 20);
	}
	
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

}
