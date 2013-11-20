package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;

import city.Animation;
import city.animations.interfaces.RestaurantTimmsAnimatedCook;

public class RestaurantTimmsCookAnimation extends Animation implements RestaurantTimmsAnimatedCook {
	
	// Data
	
	public static final int INITIAL_X = 400;
	public static final int INITIAL_Y = 400;
	public static final int WIDTH = 20;
	public static final int HEIGHT = 20;
	
    // Constructor
	
    /**
     * The cook stays in one position on-screen.
     */
	public RestaurantTimmsCookAnimation() {
        xPos = INITIAL_X;
        yPos = INITIAL_Y;
	}
    
	// Abstract
	
    /**
     * Since the cook does not move, this method simply returns void.
     */
    public void updatePosition() {
        return;
    }

	public void draw(Graphics2D g) {
		g.fillRect(xPos, yPos, WIDTH, HEIGHT);
	}

	// Movement
	
	// Getters
	
    // Setters
    
    // Utilities
	
}
