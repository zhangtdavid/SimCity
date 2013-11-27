package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class CityViewBuilding {
	//Consider creating a rectangle for every Component for better universal collision detection

	Rectangle rectangle;
	public int x, y;
	Color color;
	public String ID;
	public BuildingCard building;
	boolean isActive;
	public enum BuildingType {NONE, RESTAURANTZHANG, RESTAURANTCHOI, RESTAURANTJP, RESTAURANTTIMMS, RESTAURANTCHUNG, BANK, ROAD, HOUSE};
	BuildingType type = BuildingType.NONE;
	
	public CityViewBuilding() {
		x = 0;
		y = 0;
		color = Color.black;
		ID = "";
		isActive = true;
	}
	
	public CityViewBuilding(int x, int y) {
		this.x = x;
		this.y = y;
		color = Color.black;
		ID = "";
		isActive = true;
	}
	
	public CityViewBuilding(int x, int y, Color c) {
		this.x = x;
		this.y = y;
		color = c;
		ID = "";
		isActive = true;
	}
	
	public CityViewBuilding(int x, int y, Color c, String I) {
		this.x = x;
		this.y = y;
		color = c;
		ID = I;
		isActive = true;
	}
	
	public CityViewBuilding(int x, int y, Color c, String I, BuildingCard b) {
		this.x = x;
		this.y = y;
		color = c;
		ID = I;
		building = b;
		isActive = true;
	}
	
	public abstract void updatePosition();
	
	public void paint(Graphics g) {
		if (isActive) {
			g.setColor(color);
			g.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
			g.setColor(Color.red);
			if(building != null)
				g.drawString(building.getClass().getSimpleName(), rectangle.x, rectangle.y);
		}
	}
	
	public boolean contains(int x, int y) {
		return rectangle.contains(x, y);
	}
	
	public boolean contains(Point p) {
		return contains((int)p.getX(), (int)p.getY());
	}
	
	public void disable() {
		isActive = false;
	}
	
	public void enable() {
		isActive = true;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public void setX(int x) {
		this.x = x;
		rectangle.setLocation(x, y);
	}
	
	public void setY(int y) {
		this.y = y;
		rectangle.setLocation(x, y);
	}
	
	public void setPosition(Point p) {
		this.x = p.x;
		this.y = p.y;
		rectangle.setLocation(p);
	}
}
