package city.gui;

import java.awt.Container;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = -1848134216103293730L;
	
	static MainFrame mainFrame;
    private static int WINDOWX = 1100;
    private static int WINDOWY = 700;
	
    /**
     * Main routine to start the GUI window
     * 
     * When the program is started, this is the first call. It opens the GUI window
     * and starts all further program behavior.
     *
     * @param args no input required
     */
	public static void main(String[] args) {
		mainFrame = new MainFrame();
	}
	
    /**
     * Constructor method
     * 
     * The MainFrame object is the main window of the program GUI.
     */
    public MainFrame() {
    	// Set up the window
        this.setTitle("SimCity201");
        this.setVisible(true);
        this.setResizable(false);
        this.setBounds(50, 50, WINDOWX, WINDOWY);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BoxLayout((Container) getContentPane(), BoxLayout.X_AXIS));
    }
	
}
