package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;

import city.Animation;
import city.animations.interfaces.RestaurantTimmsAnimatedHost;

public class RestaurantTimmsHostAnimation extends Animation implements RestaurantTimmsAnimatedHost {
	
	// Data
	
	public static final int INITIAL_X = -20;
	public static final int INITIAL_Y = -20;
	public static final int WIDTH = 20;
	public static final int HEIGHT = 20;
	public static final Color COLOR = Color.MAGENTA;
	
	// Constructor

    public RestaurantTimmsHostAnimation() {
        xPos = INITIAL_X;
        yPos = INITIAL_Y;
        isVisible = true; // TODO solve how this should become visible
    }

	// Abstract
	
    /**
     * Since the host does not move, this method simply returns.
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
