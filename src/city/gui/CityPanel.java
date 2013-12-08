package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import city.Application;
import city.bases.interfaces.AnimationInterface;
import city.gui.exteriors.CityViewBuilding;



public abstract class CityPanel extends JPanel implements ActionListener, MouseListener {

	private static final long serialVersionUID = 7439702094727197622L;
	protected MainFrame mainframe;
	public List<CityViewBuilding> statics = Collections.synchronizedList(new ArrayList<CityViewBuilding>());
	public List<CityViewBuilding> movings = Collections.synchronizedList(new ArrayList<CityViewBuilding>());
	public List<AnimationInterface> animations = Collections.synchronizedList(new ArrayList<AnimationInterface>());
	protected Color background;
	protected Timer timer;

	public CityPanel(MainFrame mf) {
		mainframe = mf;
		timer = new Timer(((Double) (Application.INTERVAL * 0.005)).intValue(), this);
		timer.start();
	}

	public void paint(Graphics g) {
		g.setColor(background);
		g.fillRect(0, 0, getWidth(), getHeight());
		moveComponents();
		drawComponents(g);
	}

	public void drawComponents(Graphics g) {
		synchronized(movings) {
			for (CityViewBuilding c:movings) {
				c.paint(g);
			}
		}
		synchronized(animations) {
			for (AnimationInterface a : animations) {
				if(a.getVisible())
					a.draw((Graphics2D) g);
			}
		}
		synchronized(statics) {
			for (CityViewBuilding c:statics) {
				c.paint(g);
			}
		}
	}

	public void moveComponents() {
		synchronized(movings) {
			for (CityViewBuilding c:movings) {
				c.updatePosition();
			}
		}
		synchronized(animations) {
			for (AnimationInterface a : animations) {
				a.updatePosition();
			}
		}
	}

	public void addStatic(CityViewBuilding c) {
		statics.add(c);
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
	
	public void removeAnimation(AnimationInterface a) {
		animations.remove(a);
	}

	public void actionPerformed(ActionEvent e) {
		this.repaint();
	}

}
