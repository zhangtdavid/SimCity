package city.animations;

import java.awt.*;

import city.Animation;
import city.animations.interfaces.RestaurantChoiAnimatedCashier;

public class RestaurantChoiCashierAnimation extends Animation implements RestaurantChoiAnimatedCashier {

	//Data
    static final int CASHIER_X = 580;
    static final int CASHIER_Y = -10;
    static final int WIDTH = 30;
	static final int HEIGHT = 20;
	static final Color CASHIER_COLOR = Color.BLACK;
	
	//Constructor

    /**
     * Cashier does not ever move
     */
	public RestaurantChoiCashierAnimation() {
		xPos = CASHIER_X;
		yPos = CASHIER_Y;
	}
	
	//Abstract
	/**
	 * No need to update position.
	 */
	@Override
	public void updatePosition() {} // cashier sits in corner

	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(CASHIER_COLOR);
		g.fillRect(xPos, yPos, WIDTH, HEIGHT);
	}

	// Movement
	
	// Getters
	
    // Setters
    
    // Utilities

}
