package city.gui.exteriors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import city.gui.BuildingCard;

public abstract class CityViewBuilding {
	
	// Data
	
	public enum BUILDINGTYPE {NONE, APT, MARKET, BUSSTOP, RESTAURANTZHANG, RESTAURANTCHOI, RESTAURANTJP, RESTAURANTTIMMS, RESTAURANTCHUNG, BANK, ROAD, HOUSE};
	
	protected Rectangle rectangle;
	protected int x;
	protected int y;
	protected Color color;
	protected String ID;
	protected BuildingCard building;
	protected boolean isActive;
	protected BUILDINGTYPE type = BUILDINGTYPE.NONE;
	
	protected boolean isUgly = false;
	
	// Constructors
	
	public CityViewBuilding() {
		x = 0;
		y = 0;
		rectangle = new Rectangle(0, 0, 0, 0);
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
	
	// Painting
	
	public abstract void updatePosition();

	public void paint(Graphics g) {
		if (isActive) {
			g.setColor(color);
			g.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
			g.setColor(Color.red);
			if(building != null)
				g.drawString(ID, rectangle.x, rectangle.y);
		}
	}

	// Getters

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public String getID() {
		return ID;
	}
	
	public Rectangle getRectangle() {
		return rectangle;
	}
	
	public BuildingCard getBuilding() {
		return building;
	}
	
	// Setters

	public void setX(int x) {
		this.x = x;
		rectangle.setLocation(x, y);
	}

	public void setY(int y) {
		this.y = y;
		rectangle.setLocation(getX(), y);
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		rectangle.setLocation(x, y);
	}

	public void setID(String iD) {
		ID = iD;
	}
	
	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}
	
	public void setBuilding(BuildingCard building) {
		this.building = building;
	}
	
	public void setUgly(boolean newUgly) {
		isUgly = newUgly;
	}
	
	// Utilities
	
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
}
