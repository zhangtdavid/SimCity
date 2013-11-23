package city.gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.JPanel;

public class BuildingView extends JPanel implements MouseListener, ActionListener {
	
	private static final long serialVersionUID = 157865152829475516L;
	
	HashMap<String, BuildingCard> cards;
	MainFrame mainframe;
	public static final int VIEW_WIDTH = 500, VIEW_HEIGHT = 500;
	CardLayout layout;
	
	public BuildingView(MainFrame mf) {
		
		this.setPreferredSize(new Dimension(VIEW_WIDTH, VIEW_HEIGHT));
		this.setMaximumSize(new Dimension(VIEW_WIDTH, VIEW_HEIGHT));
		this.setMinimumSize(new Dimension(VIEW_WIDTH, VIEW_HEIGHT));
		this.setVisible(true);
		addMouseListener(this);
		this.mainframe = mf;
		
		cards = new HashMap<String, BuildingCard>();
		cards.put("null", new BuildingCard(mf, Color.DARK_GRAY));
		cards.put("Road", new BuildingCard(mf));
		cards.put("Restaurant 1", new BuildingCard(mf, Color.blue));
		cards.put("Restaurant 2", new BuildingCard(mf, Color.red));

		layout = new CardLayout();
		this.setLayout(layout);
		for (String key:cards.keySet()) {
			this.add(cards.get(key), key);
		}
		

		layout.show(this, "null");
	}
	
	public boolean addView(BuildingCard panel, String key) {
		if (cards.containsKey(key))
			return false;
		cards.put(key, panel);
		this.add(cards.get(key), key);
		return true;
	}
	
	public void setView(String key) {
		if (cards.containsKey(key)) {
			layout.show(this, key);
//			city.info.setText(key);
		}
	}

	
	public void actionPerformed(ActionEvent arg0) {
		
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
