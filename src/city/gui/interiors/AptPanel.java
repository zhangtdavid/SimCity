package city.gui.interiors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Timer;

import city.bases.interfaces.AnimationInterface;
import city.buildings.AptBuilding;

/**
 * An apartment contains: - 5 Refrigerators - 5 Stoves - 5 Tables - 5 Beds Up to
 * five people can live in an apartment.
 */
public class AptPanel extends ResidenceBasePanel {

	private static final long serialVersionUID = -9051230986691103443L;

	// in aptbuilding, first bed is at 490x50, next is 490x150, 490x250,
	// 490x350, 490x450. (5 max)
	// Data
	private final int delayMS = 5;

	//Access tip: [roomNumber-1][x=0||y=1]
	public static final int APT_REFRIG[][] = { { 100, 0 }, { 100, 100 },
			{ 100, 200 }, { 100, 300 }, { 100, 400 } };
	// apt refrigerator coords
	public static final int APT_STOVE[][] = { { 200, 0 }, { 200, 100 },
			{ 200, 200 }, { 200, 300 }, { 200, 400 } };
	public static final int APT_TABLE[][] = { { 300, 0 }, { 300, 100 },
			{ 300, 200 }, { 300, 300 }, { 300, 400 } };
	public static final int APT_BED[][] = { { 490, 40 }, { 490, 140 },
			{ 490, 240 }, { 490, 340 }, { 490, 440 } };
	public static final int APT_DOOR[][] = {{-10, 70}, {-10, 170}, {-10, 270}, {-10, 370}, {-10, 470}};
	// every apt has 5 beds regardless of how many people there are; already
	// furnished!

	// Constructor
	public AptPanel(Color color) {
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
	
		for (int i = 0; i < AptBuilding.NUMBER_OF_BEDS; i++){
			graphics.setColor(Color.CYAN); // Refrig: cyan
			graphics.fillRect(APT_REFRIG[i][0], APT_REFRIG[i][1], WIDTH, WIDTH);
			graphics.setColor(Color.RED); // Stove: red
			
			graphics.fillRect(APT_STOVE[i][0], APT_STOVE[i][1], WIDTH, WIDTH);
			graphics.setColor(Color.DARK_GRAY); // Table: dark gray
			
			graphics.fillRect(APT_TABLE[i][0], APT_TABLE[i][1], WIDTH, WIDTH);
			graphics.setColor(Color.ORANGE); // bed: orange
			
			graphics.fillRect(APT_BED[i][0], APT_BED[i][1], WIDTH, WIDTH);
			graphics.setColor(Color.WHITE); // door: white
			graphics.fillRect(APT_DOOR[i][0], APT_DOOR[i][1], WIDTH, WIDTH);
		}
		// now draw lines dividing the rooms
		graphics.setColor(Color.YELLOW);
		graphics.drawLine(0, 100, 500, 100);
		graphics.drawLine(0, 200, 500, 200);
		graphics.drawLine(0, 300, 500, 300);
		graphics.drawLine(0, 400, 500, 400); //end horizontal lines

		animate();
		// Update and draw the position of each visible element
		for (AnimationInterface animation : animations) {
				if (animation.getVisible()) {
					animation.updatePosition();
					animation.draw(graphics2D);
				}
		}
	}
}
