package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import city.bases.interfaces.AnimationInterface;

public class BuildingCard extends JPanel {

	private static final long serialVersionUID = -7047777507717435867L;

	public int panelX;
	public int panelY;
	public final int delayMS = 5;
	public List<AnimationInterface> animations = Collections.synchronizedList(new ArrayList<AnimationInterface>());
	public Color background;

	public static final int CARD_WIDTH = 500, CARD_HEIGHT = 500;

	public BuildingCard() {
		this.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
		this.setMaximumSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
		this.setMinimumSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
		this.setVisible(true);
		background = Color.green;
	}

	public BuildingCard(Color c) {
		this.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
		this.setMaximumSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
		this.setMinimumSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
		this.setVisible(true);
		background = c;
	}

	public void animate() {
		// Update the position of each visible element
		synchronized(animations) {
			for(AnimationInterface animation : animations) {
				if (animation.getVisible()) {
					animation.updatePosition();
				}
			}
		}
	}

	public void addVisualizationElement(AnimationInterface ve) {
		animations.add(ve);
	}
}
