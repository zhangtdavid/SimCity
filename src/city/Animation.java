package city;

import java.awt.Graphics2D;

import city.interfaces.AbstractAnimation;

/**
 * The base class for all SimCity201 agent animations.
 */
public abstract class Animation implements AbstractAnimation {
	
	// Data
	
	protected boolean isVisible = false;
	protected int xPos, yPos;
	
	// Constructor
	
	// Abstract
	
    public abstract void updatePosition();
    public abstract void draw(Graphics2D g);
    
    // Getters

    /**
     * Returns whether or not the object is visible on screen. This ensures
     * off-screen objects are not drawn and re-drawn unnecessarily. 
     */
	public boolean getVisible() {
		return isVisible;
	}
    
    public int getXPos() {
    	return xPos;
    }

    public int getYPos() {
    	return yPos;
    }
    
    // Setters
    
    // Utilities

}
