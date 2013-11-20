package city.animations;

import java.awt.*;

import city.Animation;
import city.animations.interfaces.RestaurantChoiAnimatedFurniture;

public class RestaurantChoiFurnitureAnimation extends Animation implements RestaurantChoiAnimatedFurniture  {

	//Data
	private static final int TABLEX_INIT = 400;
	private static final int TABLEX_INCR = 50;
    private static final int TABLEY = 250;
    private static final int CPX = 250; // plating (cook3)
    private static final int CPY = -20;
    private static final int CRX = 150; // refrig (cook1)
    private static final int CRY = -20;
    private static final int CGX = 200; // grills (cook2)
    private static final int CGY = -20;
    private static final int DISHES_X = 550; // grills (cook2)
    private static final int DISHES_Y = 250;
    private static final int WIDTH = 30;
    private static final int TABLE_COUNT = 4;
    
	//constructor
    /**
     * Constructor method
     *
     * Builds a table animation object. The table stays in one place on screen.
     * The first table has its corner at (20, 20) and the position multiplier
     * is used to determine where to add a table next.
     *
     * @param position the number of the table, used to determine its position
     */
	public RestaurantChoiFurnitureAnimation(int position) {
		this.xPos = getTableX(position);
		this.yPos = getTableY(position);
	}
    
	
	//Abstract
	@Override
	public void updatePosition() {} // Furniture doesn't move

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.ORANGE); //drawing tables
        for(int i = 0; i < TABLE_COUNT; i++){
        	g.fillRect(TABLEX_INIT-2*TABLEX_INCR*(i+1), TABLEY, WIDTH, WIDTH);
        }
        g.setColor(Color.DARK_GRAY); // plating 
        g.fillRect(CPX,CPY,WIDTH,WIDTH);
        g.setColor(Color.BLUE); // refrigerator
        g.fillRect(CRX,CRY,WIDTH,WIDTH);
        g.setColor(Color.red); // grill
        g.fillRect(CGX,CGY,WIDTH,WIDTH);
        g.setColor(Color.WHITE); // dishes
        g.fillRect(DISHES_X, DISHES_Y, WIDTH, WIDTH);
        
		
	}

	// Movement: n/a
	
	
	// Getters
	public static int getTableX(int position) {
		return TABLEX_INIT-2*TABLEX_INCR*(position+1);
    }
	
	public static int getTableY(int position) {
		return TABLEY;
    }
	
	public static int getRefrigeratorX(){
		return CRX;
	}
	public static int getRefrigeratorY(){
		return CRY;
	}
	public static int getPlatingX(){
		return CPX;
	}
	public static int getPlatingY(){
		return CPY;
	}
	public static int getGrillX(){
		return CGX;
	}
	public static int getGrillY(){
		return CGY;
	}
	// Setters
	
	// Utilities
}
