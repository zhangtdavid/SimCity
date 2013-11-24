package city.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class BuildingCard extends JPanel {

	private static final long serialVersionUID = -7047777507717435867L;
	
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
}
