package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import city.AnimationInterface;
import city.gui.views.CityViewBuilding;

public abstract class CityPanel extends JPanel implements ActionListener, MouseListener {

	private static final long serialVersionUID = 7439702094727197622L;
	protected MainFrame mainframe;
	public ArrayList<CityViewBuilding> statics, movings;
	public ArrayList<AnimationInterface> animations = new ArrayList<AnimationInterface>();
	protected Color background;
	protected Timer timer;
	
	public CityPanel(MainFrame mf) {
		mainframe = mf;
		statics = new ArrayList<CityViewBuilding>();
		movings = new ArrayList<CityViewBuilding>();
		timer = new Timer(5, this);
		timer.start();
	}
	
	public void paint(Graphics g) {
		g.setColor(background);
		g.fillRect(0, 0, getWidth(), getHeight());
		moveComponents();
		drawComponents(g);
	}
	
	public void drawComponents(Graphics g) {
		for (CityViewBuilding c:movings) {
			c.paint(g);
		}
		for (AnimationInterface a : animations) {
			a.draw((Graphics2D) g);
		}
		for (CityViewBuilding c:statics) {
			c.paint(g);
		}
		
	}
	
	public void moveComponents() {
		for (CityViewBuilding c:movings) {
			c.updatePosition();
		}
		for (AnimationInterface a : animations) {
			a.updatePosition();
		}
	}
	
	public void addStatic(CityViewBuilding c) {
		statics.add(c);
		System.out.println(c.toString());
	}
	
	public int getStaticsSize() {
		return statics.size();
	}
	
	public void addMoving(CityViewBuilding c) {
		movings.add(c);
	}
	
	public void addAnimation(AnimationInterface anim) {
		animations.add(anim);
	}
	
	public void actionPerformed(ActionEvent e) {
		this.repaint();
	}

}
