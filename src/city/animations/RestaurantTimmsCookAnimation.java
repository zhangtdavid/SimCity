package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;

import city.animations.interfaces.RestaurantTimmsAnimatedCook;
import city.bases.Animation;

public class RestaurantTimmsCookAnimation extends Animation implements RestaurantTimmsAnimatedCook {
	
	// Data
	
	public static final int INITIAL_X = 400;
	public static final int INITIAL_Y = 400;
	public static final int WIDTH = 20;
	public static final int HEIGHT = 20;
	public static final Color COLOR = Color.RED;
	
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
		g.setColor(COLOR);
		g.fillRect(xPos, yPos, WIDTH, HEIGHT);
	}

	// Movement
	
	// Getters
	
    // Setters
    
    // Utilities
	
}
