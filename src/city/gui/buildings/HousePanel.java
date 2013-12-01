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
public class HousePanel extends BuildingCard implements ActionListener{
	
	/**
	 * what does this do?
	 */
	private static final long serialVersionUID = -9051230986691103443L;
	
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
	static final int HDX = 250;
	static final int HDY = 490;
	static final int NUMBER_OF_BEDS = 1;
	
	//Constructor
	public HousePanel(Color color, Dimension panelDimension){
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
	        graphics.setColor(Color.CYAN); // Refrig 
	        graphics.fillRect(HRX,HRY,WIDTH,WIDTH);
	        graphics.setColor(Color.RED); // Stove
	        graphics.fillRect(HSX,HSY,WIDTH,WIDTH);
	        graphics.setColor(Color.DARK_GRAY); // Table
	        graphics.fillRect(HTX,HTY,WIDTH,WIDTH);
	        graphics.setColor(Color.BLACK); // Bed (1 for houses)
	        graphics.fillRect(HBXi,HBYi,WIDTH,WIDTH);
	        graphics.setColor(Color.orange); // Bed (1 for houses)
	        graphics.fillRect(HDX,HDY,WIDTH,WIDTH);
	        
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
