package city.gui.tabs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import city.gui.MainFrame;

public class ScenariosTab extends JPanel implements ActionListener{

	private static final long serialVersionUID = 9166425422374406573L;
	
	private MainFrame mainFrame;
	private JButton btnScenarioJ;
	private JButton btnScenarioF;
	private JButton btnScenarioO;
	private JButton btnScenarioP;
	private JButton btnScenarioQ;
	private JButton btnScenarioR;
	private JButton btnScenarioS;
	// TODO add controls like buttons, radio buttons, etc. here
	
	public ScenariosTab(MainFrame mf) {
		mainFrame = mf;
		this.setPreferredSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setMaximumSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setMinimumSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setVisible(true);

		this.setLayout(new FlowLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

        
        btnScenarioJ = new JButton("Big City Scenario A/C/E/G/J");
        btnScenarioJ.setFocusable(false);
        btnScenarioJ.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		//runBigCity();
        	}
        });
        btnScenarioJ.setAlignmentX(Component.RIGHT_ALIGNMENT);
        this.add(btnScenarioJ);
		
        btnScenarioF = new JButton("Ghost Town Scenario F");
        btnScenarioF.setFocusable(false);
        btnScenarioF.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		//runBigCity();
        	}
        });
        btnScenarioF.setAlignmentX(Component.RIGHT_ALIGNMENT);
        this.add(btnScenarioF);
        
        btnScenarioO = new JButton("Bank Robbery Scenario O");
        btnScenarioO.setFocusable(false);
        btnScenarioO.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		//runBigCity();
        	}
        });
        btnScenarioO.setAlignmentX(Component.RIGHT_ALIGNMENT);
        this.add(btnScenarioO);
        
        btnScenarioP = new JButton("Vehicle Collision Scenario P");
        btnScenarioP.setFocusable(false);
        btnScenarioP.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		//runBigCity();
        	}
        });
        btnScenarioP.setAlignmentX(Component.RIGHT_ALIGNMENT);
        this.add(btnScenarioP);
        
        btnScenarioQ = new JButton("Vehicle hits person scenario Q");
        btnScenarioQ.setFocusable(false);
        btnScenarioQ.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		//runBigCity();
        	}
        });
        btnScenarioQ.setAlignmentX(Component.RIGHT_ALIGNMENT);
        this.add(btnScenarioQ);
        
        btnScenarioR = new JButton("Weekend Behavior scenario R");
        btnScenarioR.setFocusable(false);
        btnScenarioR.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		//runBigCity();
        	}
        });
        btnScenarioR.setAlignmentX(Component.RIGHT_ALIGNMENT);
        this.add(btnScenarioR);
        
        btnScenarioS = new JButton("Shift Change Scenario S");
        btnScenarioS.setFocusable(false);
        btnScenarioS.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		//runBigCity();
        	}
        });
        btnScenarioS.setAlignmentX(Component.RIGHT_ALIGNMENT);
        this.add(btnScenarioS);
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Create all actions for buttons/control components here
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}
}
