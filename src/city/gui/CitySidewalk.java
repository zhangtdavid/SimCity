package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import city.bases.interfaces.AnimationInterface;
import city.gui.exteriors.CityViewBuilding;

public class CitySidewalk extends CityViewBuilding {

	// Data

	protected int velocity;
	protected double size;
	protected Color sidewalkColor;
	
	protected boolean isCrosswalk = false;
	protected CityRoad correspondingStoplight = null;

	volatile protected AnimationInterface currentOccupant = null;

	// Constructor

	public CitySidewalk(int x, int y, double size, int velocity, Color sidewalkColor) {
		super(x, y, sidewalkColor);
		this.size = size; 
		this.velocity = velocity;
		this.sidewalkColor = sidewalkColor;

		//Make the lane surface
		rectangle = new Rectangle();
		rectangle.setRect(x, y, size, size);
	}

	// Paint stuff

	@Override
	public void paint( Graphics g2 ) {
		g2.setColor( sidewalkColor );
		((Graphics2D) g2).fill( rectangle );
	}

	@Override
	public void updatePosition() { };

	// Getters

	public AnimationInterface getCurrentOccupant() {
		return currentOccupant;
	}
	
	public boolean isCrosswalk() {
		return isCrosswalk;
	}

	public CityRoad getCorrespondingStoplight() {
		return correspondingStoplight;
	}

	// Setters

	public synchronized boolean setCurrentOccupant( AnimationInterface newOccupant ) {
		if(currentOccupant == null || currentOccupant == newOccupant || newOccupant == null ) {
			currentOccupant = newOccupant;
			return true;
		}
		return false;
	}
	
	public void setCrosswalk(boolean b) {
		isCrosswalk = b;
	}

	public void setCorrespondingStoplight(CityRoad correspondingStoplight) {
		this.correspondingStoplight = correspondingStoplight;
	}
}
