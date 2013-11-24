package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

public abstract class CityPanel extends JPanel implements ActionListener, MouseListener {

	private static final long serialVersionUID = 7439702094727197622L;
	protected MainFrame mainframe;
	public ArrayList<CityViewBuilding> statics, movings;
	protected Color background;
	protected Timer timer;
	
	public CityPanel(MainFrame mf) {
		mainframe = mf;
		statics = new ArrayList<CityViewBuilding>();
		movings = new ArrayList<CityViewBuilding>();
		timer = new Timer(50, this);
		timer.start();
	}
	
	public void paint(Graphics g) {
		g.setColor(background);
		g.fillRect(0, 0, getWidth(), getHeight());
		moveComponents();
		drawComponents(g);
	}
	
	public void drawComponents(Graphics g) {
		for (CityViewBuilding c:statics) {
			c.paint(g);
		}
		
		for (CityViewBuilding c:movings) {
			c.paint(g);
		}
	}
	
	public void moveComponents() {
		for (CityViewBuilding c:movings) {
			c.updatePosition();
		}
	}
	
	public void addStatic(CityViewBuilding c) {
		statics.add(c);
	}
	
	public void addMoving(CityViewBuilding c) {
		movings.add(c);
	}
	
	public void actionPerformed(ActionEvent e) {
		this.repaint();
	}

}
