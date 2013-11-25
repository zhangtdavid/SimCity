package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import city.Animation;
import city.roles.RestaurantChungHostRole;

public class MarketPanel extends BuildingCard implements ActionListener {

	private static final long serialVersionUID = 1355285244678935863L;

	private Timer timer = new Timer(5, this );
	
//	Fixed Numbers
//	=====================================================================
    public static final int COUNTERX = 0, COUNTERY = 350;
    public static final int COUNTERW = 500, COUNTERH = 25;
    public static final int SHELFX = 10, SHELFY = 25;
    public static final int SHELFW = 30, SHELFH = 150;
    public static final int SHELFGAP = 20;

	private int panelX;
	private int panelY;
	private final int delayMS = 5;
	private List<Animation> animations = new ArrayList<Animation>();
	
	
	public MarketPanel(Color color, Dimension panelDimension) {
		super(color);
		panelX = panelDimension.width;
		panelY = panelDimension.height;

		setVisible(true);

		Timer timer = new Timer(delayMS, this);
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {
		repaint();
	}
	
	@Override
	public void paint(Graphics graphics) {
		Graphics2D g2 = (Graphics2D)graphics;
		// Clear the screen by painting a rectangle the size of the frame
		g2.setColor(background);
		g2.fillRect(0, 0, panelX, panelY);

        // kitchen area
//        g2.setColor(Color.YELLOW);
//        g2.fillRect(KITCHEN1X, KITCHEN1Y, KITCHEN1W, KITCHEN1L);
//        g2.fillRect(KITCHEN1X, KITCHEN2Y, KITCHEN2W, KITCHEN2L);
//        
//        g2.setColor(Color.GRAY);
//        g2.fillRect(GRILLX, GRILLY, GRILLW, GRILLL);
		
		// Update the position of each visible element
		for(Animation animation : animations) {
//			if (animation.getVisible()) {
				animation.updatePosition();
//			}
		}

		// Draw each visible element after updating their positions
		// TODO generates concurrent modification exception
		for(Animation animation : animations) {
//			if (animation.getVisible()) {
				animation.draw(g2);
//			}
		}
	}

	public void addVisualizationElement(Animation ve) {
		animations.add(ve);
	}
}