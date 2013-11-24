package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;

public class BuildingCard extends CityPanel {

	private static final long serialVersionUID = -7047777507717435867L;
	
	public static final int CARD_WIDTH = 500, CARD_HEIGHT = 500;

	public BuildingCard(MainFrame mf) {
		super(mf);
		this.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
		this.setMaximumSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
		this.setMinimumSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
		this.setVisible(true);
		addMouseListener(this);
		background = Color.green;
	}
	
	public BuildingCard(MainFrame mf, Color c) {
		super(mf);
		this.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
		this.setMaximumSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
		this.setMinimumSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
		this.setVisible(true);
		addMouseListener(this);
		background = c;
	}


	public void mouseClicked(MouseEvent e) {
		
	}

	
	public void mouseEntered(MouseEvent e) {
		
	}

	
	public void mouseExited(MouseEvent e) {
		
	}

	
	public void mousePressed(MouseEvent e) {
		
	}

	
	public void mouseReleased(MouseEvent e) {
		
	}


	

}
