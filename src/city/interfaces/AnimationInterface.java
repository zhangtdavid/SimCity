package city.interfaces;

import java.awt.Graphics2D;

/**
 * This interface specifies the absolute minimum an interface or class representing an animation must provide.
 * 
 * The following code uses AnimationInterface:
 * 		- 
 */
public interface AnimationInterface {
	
	// Data
	
	// Constructor
	
	// Abstract
	
    public void updatePosition();
    public void draw(Graphics2D g);
    
    // Movement
    
    // Getters

	public boolean getVisible();
    public int getXPos();
    public int getYPos();
    
    // Setters
    
    public void setVisible(boolean b);
    
    // Utilities

}
