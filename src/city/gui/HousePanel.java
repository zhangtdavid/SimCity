package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import city.Animation;

/**
 * A House contains:
 * - Refrigerator
 * - Stove
 * - Table
 * - Bed
 * One person lives in a house.
 * Some people own their own houses. (One person one house?)
 */
public class HousePanel extends BuildingCard implements ActionListener{
	
	//Data
	private int panelX;
    private int panelY;
    private final int delayMS = 5;
	private List<Animation> animations = new ArrayList<Animation>();
	static final int HRX = -10; // house refrigerator
	static final int HRY = 100;
	static final int HSX = -10; // house stove
	static final int HSY = 250;
	static final int HTX = -10; // house table
	static final int HTY = 400;
	static final int HBXi = 490; // initial house bed
	static final int HBYi = 50;
	static final int HBYint = 100; // y-Interval for house beds
	// every house has 5 beds regardless of how many people there are; already furnished!
	static final int numBeds = 5; 
	// thus first bed is at 490x50, next is 490x150, 490x250, 490x350, 490x450. (5 max)
	
	//Constructor
	public HousePanel(MainFrame mf, Color color, Dimension panelDimension){
	  	super(color);
    	panelX = panelDimension.width;
    	panelY = panelDimension.height;
    	
        setVisible(true);
 
    	Timer timer = new Timer(delayMS, this);
    	timer.start();
	}
	
	 public void paintComponent(Graphics graphics) {
	        Graphics2D graphics2D = (Graphics2D)graphics;

	        // Clear the screen by painting a rectangle the size of the frame
	        graphics2D.setColor(Color.getHSBColor((float)37, (float).53, (float).529)); // nice subtle gray
	        graphics2D.fillRect(0, 0, panelX, panelY);

	        // Update the position of each visible element
	        for(Animation animation : animations) {
	        	if (animation.getVisible()) {
	                animation.updatePosition();
	            }
	        }

	        // Draw each visible element after updating their positions
	        // TODO generates concurrent modification exception
	        for(Animation animation : animations) {
	            if (animation.getVisible()) {
	                animation.draw(graphics2D);
	            }
	        }
	    }
	    
	    public void addVisualizationElement(Animation ve) {
	    	animations.add(ve);
	    }
	
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {repaint();}
}
