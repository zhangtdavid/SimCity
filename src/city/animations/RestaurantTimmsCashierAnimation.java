package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;

import city.animations.interfaces.RestaurantTimmsAnimatedCashier;
import city.bases.Animation;

public class RestaurantTimmsCashierAnimation extends Animation implements RestaurantTimmsAnimatedCashier {
	
	// Data
	
	public static final int INITIAL_X = 400;
	public static final int INITIAL_Y = 50;
	public static final int WIDTH = 20;
	public static final int HEIGHT = 20;
	public static final Color COLOR = Color.YELLOW;
	
	// Constructor
	
    /**
     * The cashier stays in one position on-screen.
     */
	public RestaurantTimmsCashierAnimation() {
        xPos = INITIAL_X;
        yPos = INITIAL_Y;
    }
    
	// Abstract
	
    /**
     * Since the cashier does not move, this method simply returns void.
     */
	@Override
    public void updatePosition() {
        return;
    }

	@Override
	public void draw(Graphics2D g) {
		g.setColor(COLOR);
		g.fillRect(xPos, yPos, WIDTH, HEIGHT);
	}
	
	// Movement
	
	// Getters
	
    // Setters
    
    // Utilities
	
}
