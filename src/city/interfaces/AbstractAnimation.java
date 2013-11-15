package city.interfaces;

import java.awt.Graphics2D;

public interface AbstractAnimation {
	
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
    
    // Utilities

}
