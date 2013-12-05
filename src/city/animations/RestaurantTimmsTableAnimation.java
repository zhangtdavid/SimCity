package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;

import city.animations.interfaces.RestaurantTimmsAnimatedTable;
import city.bases.Animation;

public class RestaurantTimmsTableAnimation extends Animation implements RestaurantTimmsAnimatedTable {

	// Data
	
	public static final int INITIAL_X = 100;
	public static final int INITIAL_Y = 100;
	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;
	public static final Color COLOR = Color.ORANGE;
	
	// Constructor
	
    /**
     * Constructor method
     *
     * Builds a table animation object. The table stays in one place on screen.
     * The first table has its corner at (20, 20) and the position multiplier
     * is used to determine where to add a table next.
     *
     * @param position the number of the table, used to determine its position
     */
	public RestaurantTimmsTableAnimation(int position) {
		this.xPos = getXPos(position);
		this.yPos = getYPos(position);
		this.setVisible(true);
	}
    
	// Abstract
	
    public void updatePosition() {
        return;
    }

	public void draw(Graphics2D g) {
		g.setColor(COLOR);
		g.fillRect(xPos, yPos, WIDTH, HEIGHT);
	}
	
	// Movement
	
	// Getters

    /**
     * Returns the xPos for a table in a particular position
     *
     * @param position the number of the table, used to determine its position
     */
    public static int getXPos(int position) {
        return (INITIAL_X + ((position % 3) * ((INITIAL_X / 2) + WIDTH)));
    }

    /**
     * Returns the yPos for a table in a particular position
     *
     * @param position the number of the table, used to determine its position
     */
    public static int getYPos(int position) {
        return (INITIAL_Y + ((position / 3) * ((INITIAL_Y / 2) + HEIGHT)));
    }
    
    // Setters
    
    // Utilities 
}
