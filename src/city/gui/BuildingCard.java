package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import city.Animation;

public class BuildingCard extends JPanel {

	private static final long serialVersionUID = -7047777507717435867L;
	public List<Animation> animations = new ArrayList<Animation>();
	Color background;

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
		for(Animation animation : animations) {
			if (animation.getVisible()) {
				animation.updatePosition();
			}
		}
	}

	public void addVisualizationElement(Animation ve) {
		animations.add(ve);
	}
}
