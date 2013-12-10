package city.bases.interfaces;

import java.awt.Graphics2D;

import city.bases.Agent;

/**
 * This interface specifies the absolute minimum an interface or class representing an animation must provide.
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
    public Agent getAgent();
    
    // Setters
    
    public void setVisible(boolean b);
    public void setAgent(Agent a);
    public void setUgly(boolean newUgly);
    
    // Utilities

}
