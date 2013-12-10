package city.gui.interiors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import city.bases.interfaces.AnimationInterface;
import city.gui.BuildingCard;

public class RestaurantChoiPanel extends BuildingCard implements ActionListener {

	private static final long serialVersionUID = 1255285244678935863L;
	
    private final int delayMS = 5;
	
	// personal restaurant specifications (for drawing)
	static final int TABLEX_INIT = 400; // table stuff
	static final int TABLEX_INCR = 50;
	static final int TABLEY = 250;
	static final int TABLE_COUNT = 4;   // # of tables
	static final int CRX = 150; // refrig (cook1)
	static final int CRY = -20;
	static final int CGX = 200; // grills (cook2)
	static final int CGY = -20;
	static final int CPX = 250; // plating (cook3)
	static final int CPY = -20;	
	static final int DISHES_X = 480; // dishes
	static final int DISHES_Y = 250;
	static final int WIDTH = 30; // width of all furniture
	//TODO add cashier, waiter/cook/customer waiting locations
	//TODO add bootup - cook shouldn't just pop up in the middle of the room on arrival... Or should he?
	

    public RestaurantChoiPanel(Color color) {
    	super(color);
    	
        setVisible(true);
 
    	Timer timer = new Timer(delayMS, this);
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	@Override
    public void paint(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D)graphics;
        // Clear the screen by painting a rectangle the size of the frame
        graphics2D.setColor(Color.GRAY);
        graphics2D.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);
        // PERSONAL DRAWING STARTS HERE; FIRST DRAW STATIC OBJECTS (UTILITIES, TABLES, CASHIER)
        graphics.setColor(Color.ORANGE); //drawing tables
        for(int i = 0; i < TABLE_COUNT; i++){
        	graphics.fillRect(TABLEX_INIT-2*TABLEX_INCR*(i+1), TABLEY, WIDTH, WIDTH);
        }
        graphics.setColor(Color.DARK_GRAY); // plating 
        graphics.fillRect(CPX,CPY,WIDTH,WIDTH);
        graphics.setColor(Color.BLUE); // refrigerator
        graphics.fillRect(CRX,CRY,WIDTH,WIDTH);
        graphics.setColor(Color.red); // grill
        graphics.fillRect(CGX,CGY,WIDTH,WIDTH);
        graphics.setColor(Color.WHITE); // dishes
        graphics.fillRect(DISHES_X, DISHES_Y, WIDTH, WIDTH);
        
        synchronized(animations){
			animate();
			// Update the position of each visible element
			for (AnimationInterface animation : animations) {
				if (animation.getVisible()) {
					animation.updatePosition();
					animation.draw(graphics2D);
				}
			}
		}
	}
}
