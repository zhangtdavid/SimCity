package city.gui.interiors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Timer;

import city.bases.interfaces.AnimationInterface;

/**
 * A House contains: - Refrigerator - Stove - Table - Bed One person lives in a
 * house. Some people own their own houses. (One person one house?)
 */
public class HousePanel extends ResidenceBasePanel {


	private static final long serialVersionUID = -9051230986691103443L;
	// Data
	private final int delayMS = 5;
	public static final int HDX = 250;
	public static final int HDY = 480;
	public static final int HRX = -10; // house refrigerator
	public static final int HRY = 100;
	public static final int HSX = -10; // house stove
	public static final int HSY = 250;
	public static final int HTX = -10; // house table
	public static final int HTY = 400;
	public static final int HBXi = 490; // initial house bed
	public static final int HBYi = 50;
	public static final int HBYint = 100; // y-Interval for house beds

	// in aptbuilding, first bed is at 490x50, next is 490x150, 490x250,
	// 490x350, 490x450. (5 max)

	// Constructor
	public HousePanel(Color color) {
		super(color);

		setVisible(true);

		Timer timer = new Timer(delayMS, this);
		timer.start();
	}

	public void paintComponent(Graphics graphics) {
		Graphics2D graphics2D = (Graphics2D) graphics;
		// Clear the screen by painting a rectangle the size of the frame
		graphics2D.setColor(Color.getHSBColor((float) 37, (float) .53,
				(float) .529)); // nice subtle gray
		graphics2D.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);

		// Draw static elements (furniture)
		graphics.setColor(Color.CYAN); // Refrig
		graphics.fillRect(HRX, HRY, WIDTH, WIDTH);
		graphics.setColor(Color.RED); // Stove
		graphics.fillRect(HSX, HSY, WIDTH, WIDTH);
		graphics.setColor(Color.DARK_GRAY); // Table
		graphics.fillRect(HTX, HTY, WIDTH, WIDTH);
		graphics.setColor(Color.orange); // Bed (1 for houses)
		graphics.fillRect(HBXi, HBYi, WIDTH, WIDTH);
		graphics.setColor(Color.white); // Door (1 for houses)
		graphics.fillRect(HDX, HDY, WIDTH, WIDTH);

		// Update the position of each visible element
		// Draw each visible element after updating their positions
		animate();

		synchronized(animations) {
			for (AnimationInterface animation : animations) {
				if (animation.getVisible()) {
					animation.updatePosition();
					animation.draw(graphics2D);
				}
			}
		}
	}
}
