package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class CityViewBank extends CityViewBuilding {
	
	public CityViewBank(int x, int y) {
		super(x, y, Color.green, "Bank 1");
		rectangle = new Rectangle(x, y, 20, 20);
	}
	
	public CityViewBank(int x, int y, String ID) {
		super(x, y, Color.green, ID);
		rectangle = new Rectangle(x, y, 20, 20);
	}
	
	public void paint(Graphics g) {
		g.setColor(color);
		g.fillOval(x, y, 20, 20);
		g.fill3DRect(x, y, 20, 20, true);
	}
	
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}
}
