package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;

import city.animations.interfaces.RestaurantChoiAnimatedFurniture;
import city.bases.Animation;

public class RestaurantChoiFurnitureAnimation extends Animation implements RestaurantChoiAnimatedFurniture  {

	//Data
	
    
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
	public int getTableX(int position) {
		return TABLEX_INIT-2*TABLEX_INCR*(position+1);
    }
	
	public int getTableY(int position) {
		return TABLEY;
    }
	
	public int getRefrigeratorX(){
		return CRX;
	}
	public int getRefrigeratorY(){
		return CRY;
	}
	public int getPlatingX(){
		return CPX;
	}
	public int getPlatingY(){
		return CPY;
	}
	public int getGrillX(){
		return CGX;
	}
	public int getGrillY(){
		return CGY;
	}
	// Setters
	
	// Utilities
}
