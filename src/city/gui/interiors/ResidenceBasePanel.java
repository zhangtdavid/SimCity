package city.gui.interiors;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import city.agents.interfaces.Person;
import city.bases.Animation;
import city.gui.BuildingCard;

public abstract class ResidenceBasePanel extends BuildingCard implements
		ActionListener {

	// Data
	private static final long serialVersionUID = 6350170306014756337L;
	private Person person; // use to send messages (arrivedAtX) & room#
	public static final int WIDTH = 20;

	// Constructor
	public ResidenceBasePanel(Color color) {
		super(color);
		setVisible(true);
		Timer timer = new Timer(delayMS, this);
		timer.start();
	}

	// Getters

	// Setters

	// Utility

	public void actionPerformed(ActionEvent arg0) {
		
	}

}
