package city.gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;
import javax.swing.Timer;

public class BuildingView extends JPanel implements MouseListener, ActionListener {
	
	private static final long serialVersionUID = 157865152829475516L;
	
	Map<String, BuildingCard> cards = Collections.synchronizedMap(new HashMap<String, BuildingCard>());
	MainFrame mainframe;
	public static final int VIEW_WIDTH = 500, VIEW_HEIGHT = 500;
	CardLayout layout;
	private final int delayMS = 5;
	
	public BuildingView(MainFrame mf) {
		
		this.setPreferredSize(new Dimension(VIEW_WIDTH, VIEW_HEIGHT));
		this.setMaximumSize(new Dimension(VIEW_WIDTH, VIEW_HEIGHT));
		this.setMinimumSize(new Dimension(VIEW_WIDTH, VIEW_HEIGHT));
		this.setVisible(true);
		addMouseListener(this);
		this.mainframe = mf;
		
		cards.put("null", new BuildingCard(Color.DARK_GRAY));

		layout = new CardLayout();
		this.setLayout(layout);
		for (String key:cards.keySet()) {
			this.add(cards.get(key), key);
		}
		
		layout.show(this, "null");
		
		Timer timer = new Timer(delayMS, this);
		timer.start();
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
		}
	}

	public void actionPerformed(ActionEvent arg0) {
		synchronized(cards) {
			Iterator<Entry<String, BuildingCard>> it = cards.entrySet().iterator();
			while(it.hasNext()) {
				Map.Entry<String, BuildingCard> temp = it.next();
				if(temp.getValue().isVisible()) {
					temp.getValue().repaint();
				} else {
					temp.getValue().animate();
				}
			}
		}
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
