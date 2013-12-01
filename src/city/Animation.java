package city;

import java.awt.Graphics2D;

/**
 * The base class for all SimCity201 agent animations.
 */
public abstract class Animation implements AnimationInterface {
	
	// Data
	
	private boolean isVisible = false;
	protected int xPos, yPos;
	
	// Constructor
	
	// Abstract
	
	@Override
    public abstract void updatePosition();
	
	@Override
    public abstract void draw(Graphics2D g);
    
    // Getters

    /**
     * Returns whether or not the object is visible on screen. This ensures
     * off-screen objects are not drawn and re-drawn unnecessarily. 
     */
	@Override
	public boolean getVisible() {
		return isVisible;
	}
    
	@Override
    public int getXPos() {
    	return xPos;
    }

	@Override
    public int getYPos() {
    	return yPos;
    }
    
    // Setters
	
	@Override
	public void setVisible(boolean b) {
		this.isVisible = b;
	}
    
    // Utilities

}
