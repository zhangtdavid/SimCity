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
import city.interfaces.Apartment;

/**
 * An apartment contains: - 5 Refrigerators - 5 Stoves - 5 Tables - 5 Beds Up to
 * five people can live in an apartment.
 */
public class AptPanel extends ResidenceBasePanel {

	private static final long serialVersionUID = -9051230986691103443L;

	// Data
	private int panelX;
	private int panelY;
	private final int delayMS = 5;
	private List<Animation> animations = new ArrayList<Animation>();

	// static furniture assets
	public static final int APT_REFRIG[][] = { { 100, 0 }, { 100, 100 }, { 100, 200 },
			{ 100, 300 }, { 100, 400 } }; // apt refrigerator coords
	public static final int APT_STOVE[][] = { { 200, 0 }, { 200, 100 }, { 200, 200 },
			{ 200, 300 }, { 200, 400 } }; // same for stove, & for below,
											// self-explanatory
	public static final int APT_TABLE[][] = { { 300, 0 }, { 300, 100 }, { 300, 200 },
			{ 300, 300 }, { 300, 400 } };
	public static final int APT_BED[][] = { { 490, 40 }, { 490, 140 }, { 490, 240 },
			{ 490, 340 }, { 490, 440 } };
	public static final int ADX = -10;
	public static final int ADY = 490;
	// every apt has 5 beds regardless of how many people there are; already
	// furnished!
	public static final int NUMBER_OF_BEDS = 5; // could also refer to
											// AptBuilding.NUMBER_OF_BEDS if
											// desired

	// in aptbuilding, first bed is at 490x50, next is 490x150, 490x250,
	// 490x350, 490x450. (5 max)

	// Constructor
	public AptPanel(Color color, Dimension panelDimension) {
		super(color, panelDimension);
	}

	public void paintComponent(Graphics graphics) {
		Graphics2D graphics2D = (Graphics2D) graphics;

		// Clear the screen by painting a rectangle the size of the frame
		graphics2D.setColor(Color.getHSBColor((float) 37, (float) .53,
				(float) .529)); // nice subtle gray
		graphics2D.fillRect(0, 0, panelX, panelY);

		// Draw static elements (furniture); everyone gets their own stuff in
		// apartments.
		graphics.setColor(Color.CYAN); // Refrig: cyan
		for (int i = 0; i < NUMBER_OF_BEDS; i++)
			graphics.fillRect(APT_REFRIG[i][0], APT_REFRIG[i][1], WIDTH, WIDTH);
		graphics.setColor(Color.RED); // Stove: red
		for (int i = 0; i < NUMBER_OF_BEDS; i++)
			graphics.fillRect(APT_STOVE[i][0], APT_STOVE[i][1], WIDTH, WIDTH);
		graphics.setColor(Color.DARK_GRAY); // Table: dark gray
		for (int i = 0; i < NUMBER_OF_BEDS; i++)
			graphics.fillRect(APT_TABLE[i][0], APT_TABLE[i][1], WIDTH, WIDTH);
		graphics.setColor(Color.ORANGE); // bed: orange
		for (int i = 0; i < NUMBER_OF_BEDS; i++)
			graphics.fillRect(APT_BED[i][0], APT_BED[i][1], WIDTH, WIDTH);
		graphics.setColor(Color.BLACK); // door: black
		graphics.fillRect(ADX, ADY, WIDTH, WIDTH);
		// now draw lines dividing the rooms
		graphics.drawLine(50, 0, 50, 50); // begin vertical lines. Maybe replace
											// these with thicker objects e.g.
											// rects?
		graphics.drawLine(50, 100, 50, 150);
		graphics.drawLine(50, 200, 50, 250);
		graphics.drawLine(50, 300, 50, 350);
		graphics.drawLine(50, 400, 50, 450); // end vertical lines
		graphics.drawLine(50, 100, 50, 500);
		graphics.drawLine(50, 200, 50, 500);
		graphics.drawLine(50, 300, 50, 500);
		graphics.drawLine(50, 400, 50, 500); // begin horizontal lines (can skip
												// one because top line is OK)

		// Update the position of each visible element
		for (Animation animation : animations) {
			if (animation.getVisible()) {
				animation.updatePosition();
			}
		}

		// Draw each visible element after updating their positions
		// TODO generates concurrent modification exception
		for (Animation animation : animations) {
			if (animation.getVisible()) {
				animation.draw(graphics2D);
			}
		}
	}
	
	@Override // from BuildingCard?
	public void addVisualizationElement(Animation ve) {
		animations.add(ve);
	}
}
