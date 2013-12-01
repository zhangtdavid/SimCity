package city.gui.buildings;

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
import city.buildings.AptBuilding;
import city.gui.BuildingCard;

/**
 * A House contains:
 * - Refrigerator
 * - Stove
 * - Table
 * - Bed
 * One person lives in a house.
 * Some people own their own houses. (One person one house?)
 */
public class AptPanel extends BuildingCard implements ActionListener{
	
	private static final long serialVersionUID = -9051230986691103443L;
	
	//Data
	private int panelX;
    private int panelY;
    private final int delayMS = 5;
	private List<Animation> animations = new ArrayList<Animation>();

	static final int ARX = -10; // apt refrigerator
	static final int ARY = 100;
	static final int ASX = -10; // apt stove
	static final int ASY = 250;
	static final int ATX = -10; // apt table
	static final int ATY = 400;
	static final int ABXi = 490; // initial apt bed
	static final int ABYi = 50;
	static final int ABYint = 100; // y-Interval for apt beds
	// every apt has 5 beds regardless of how many people there are; already furnished!
	static final int NUMBER_OF_BEDS = 5; // could also refer to AptBuilding.NUMBER_OF_BEDS if desired
	// in aptbuilding, first bed is at 490x50, next is 490x150, 490x250, 490x350, 490x450. (5 max)
	
	//Constructor
	public AptPanel(Color color, Dimension panelDimension){
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
	        
	        // Draw static elements (furniture)
	        // are we going to share to utilities or not? decision drawn here. (we share for now... waiting should be straightforward)
	        graphics.setColor(Color.CYAN); // Refrig  
	        graphics.fillRect(ARX,ARY,WIDTH,WIDTH);
	        graphics.setColor(Color.RED); // Stove
	        graphics.fillRect(ASX,ASY,WIDTH,WIDTH);
	        graphics.setColor(Color.DARK_GRAY); // Table
	        graphics.fillRect(ATX,ATY,WIDTH,WIDTH);
	        graphics.setColor(Color.BLACK); 
	        for(int i = 0; i < AptBuilding.NUMBER_OF_BEDS; i++){
		        graphics.fillRect(ABXi,ABYi+ABYint,WIDTH,WIDTH);
	        }// Bed (5 for apts)
	        
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
