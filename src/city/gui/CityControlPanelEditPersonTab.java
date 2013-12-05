package city.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import utilities.DataModel;
import city.Application;
import city.RoleInterface;
import city.interfaces.Person;

public class CityControlPanelEditPersonTab extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = 9166425422374406573L;
	
	// Data
	
	private MainFrame mainFrame;
	private DataModel dataModel;
	private DefaultListModel<Person> peopleListModel;
	private DefaultListModel<RoleInterface> roleListModel;
	private JScrollPane scrollPeople;
	private JList<Person> listPeople;
	private JPanel panelName;
	private JLabel labelName;
	private JTextField textName;
	private JPanel panelCash;
	private JLabel labelCash;
	private JFormattedTextField textCash;
	private JPanel panelControl;
	private JButton btnRevert;
	private JButton btnSave;
	private JPanel panelState;
	private JLabel labelState;
	private JLabel labelStateValue;
	private Person listPeopleSelection; // Needed so that we can deactivate old listeners
	private JPanel panelRoles;
	private JScrollPane scrollRoles;
	private JList<RoleInterface> listRoles;
	private JLabel labelRoles;
	private RoleInterface listRolesSelection;
	private JPanel panelRoleState;
	private JLabel labelRoleState;
	private JLabel labelRoleStateValue;
	private JPanel panelRoleActive;
	private JLabel labelRoleActive;
	private JLabel labelRoleActiveValue;
	private JPanel panelRoleActivity;
	private JLabel labelRoleActivity;
	private JLabel labelRoleActivityValue;
	
	// Constructor
		
	public CityControlPanelEditPersonTab(MainFrame mf) {
		// Set up variables
		this.mainFrame = mf;
		this.setVisible(true);

		// Set up the JPanel
		this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.setAlignmentY(Component.TOP_ALIGNMENT);
		this.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.setPreferredSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setMaximumSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setMinimumSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// The DataModel allows the list objects to auto-update
		this.dataModel = Application.getModel();
		this.dataModel.getPropertyChangeSupport().addPropertyChangeListener(DataModel.PEOPLE, this);
		
		// The ListModel is an object which stores what the JList displays
		this.peopleListModel = new DefaultListModel<Person>();
		this.roleListModel = new DefaultListModel<RoleInterface>();
        
		// Scroll for the list
		scrollPeople = new JScrollPane();
		scrollPeople.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPeople.setPreferredSize(new Dimension(300, 150));
		scrollPeople.setMinimumSize(new Dimension(300, 150));
		scrollPeople.setMaximumSize(new Dimension(300, 150));
		scrollPeople.setBorder(new LineBorder(SystemColor.menu, 3));
		this.add(scrollPeople);
		
        // Create the JList
		listPeople = new JList<Person>(peopleListModel);
		listPeople.addListSelectionListener(new ListSelectionListener() {
			/**
			 * When a Person is selected from the list, this is the code that kicks off the GUI update
			 * It also sets up a listener so that the GUI will update with changes for as long as that Person remains selected
			 */
			public void valueChanged(ListSelectionEvent arg0) {
				if (listPeople.getSelectedValue() != null) {
					if (listPeopleSelection != null) {
						// Remove the listener from the previous selection
						listPeopleSelection.getPropertyChangeSupport().removePropertyChangeListener(CityControlPanelEditPersonTab.this);
					}
					updatePersonFormValues(listPeople.getSelectedValue());
					listPeople.getSelectedValue().getPropertyChangeSupport().addPropertyChangeListener(CityControlPanelEditPersonTab.this);
					listPeopleSelection = listPeople.getSelectedValue();
				} else {
					setPersonFormBlank();
				}
			}
		});
		listPeople.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listPeople.setBackground(Color.WHITE);
		listPeople.setAlignmentY(Component.TOP_ALIGNMENT);
		listPeople.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPeople.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = -1088518111443365535L;
			
			/**
			 * Allows Person objects to be displayed as custom text (in this case, their names) in the JList
			 */
			@Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof Person) {
                    ((JLabel) renderer).setText(((Person) value).getName());
                }
                return renderer;
            }
        });
        scrollPeople.setViewportView(listPeople);
        
        // Create the state area
        panelState = new JPanel();
        panelState.setPreferredSize(new Dimension(300, 20));
        panelState.setMinimumSize(new Dimension(300, 20));
        panelState.setMaximumSize(new Dimension(300, 20));
        add(panelState);
        panelState.setLayout(new BoxLayout(panelState, BoxLayout.X_AXIS));
        
        labelState = new JLabel("State");
        labelState.setBorder(new EmptyBorder(0, 10, 0, 10));
        labelState.setPreferredSize(new Dimension(70, 16));
        labelState.setMinimumSize(new Dimension(70, 16));
        labelState.setMaximumSize(new Dimension(70, 16));
        panelState.add(labelState);
        
        labelStateValue = new JLabel("null");
        labelStateValue.setFont(getFont().deriveFont(Font.BOLD));
        panelState.add(labelStateValue);
        
        // Create the name control
        panelName = new JPanel();
        panelName.setMinimumSize(new Dimension(300, 30));
        panelName.setMaximumSize(new Dimension(300, 30));
        panelName.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        panelName.setPreferredSize(new Dimension(300, 30));
        add(panelName);
        panelName.setLayout(new BoxLayout(panelName, BoxLayout.X_AXIS));
        
        labelName = new JLabel("Name");
        labelName.setPreferredSize(new Dimension(70, 14));
        labelName.setMinimumSize(new Dimension(70, 14));
        labelName.setMaximumSize(new Dimension(70, 14));
        labelName.setBorder(new EmptyBorder(0, 10, 0, 10));
        panelName.add(labelName);
        
        textName = new JTextField();
        textName.setPreferredSize(new Dimension(200, 20));
        textName.setMinimumSize(new Dimension(200, 20));
        textName.setMaximumSize(new Dimension(200, 20));
        panelName.add(textName);
        textName.setColumns(10);
        
        // Create the cash control
        panelCash = new JPanel();
        panelCash.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        panelCash.setPreferredSize(new Dimension(300, 30));
        panelCash.setMinimumSize(new Dimension(300, 30));
        panelCash.setMaximumSize(new Dimension(300, 30));
        add(panelCash);
        panelCash.setLayout(new BoxLayout(panelCash, BoxLayout.X_AXIS));
        
        labelCash = new JLabel("Cash");
        labelCash.setPreferredSize(new Dimension(70, 14));
        labelCash.setMinimumSize(new Dimension(70, 14));
        labelCash.setMaximumSize(new Dimension(70, 14));
        labelCash.setBorder(new EmptyBorder(0, 10, 0, 10));
        panelCash.add(labelCash);
        
        NumberFormat n = NumberFormat.getCurrencyInstance(getDefaultLocale());
        n.setRoundingMode(RoundingMode.DOWN);
        n.setParseIntegerOnly(true);
        textCash = new JFormattedTextField(n);
        textCash.setPreferredSize(new Dimension(200, 20));
        textCash.setMinimumSize(new Dimension(200, 20));
        textCash.setMaximumSize(new Dimension(200, 20));
        textCash.setColumns(10);
        panelCash.add(textCash);
        
        // Create the revert/save controls
        panelControl = new JPanel();
        panelControl.setPreferredSize(new Dimension(300, 30));
        panelControl.setMinimumSize(new Dimension(300, 30));
        panelControl.setMaximumSize(new Dimension(300, 30));
        panelControl.setLayout(new BoxLayout(panelControl, BoxLayout.X_AXIS));
        add(panelControl);
        
        btnRevert = new JButton("Revert");
        btnRevert.setFocusable(false);
        btnRevert.addActionListener(new ActionListener() {
        	/**
        	 * When the revert button is clicked, this code loads the current data about the person
        	 */
        	public void actionPerformed(ActionEvent arg0) {
        		if (!listPeople.isSelectionEmpty())
        			updatePersonFormValues(listPeopleSelection);
        	}
        });
        btnRevert.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panelControl.add(btnRevert);
        
        btnSave = new JButton("Save");
        btnSave.setFocusable(false);
        btnSave.addActionListener(new ActionListener() {
        	/**
        	 * When the save button is clicked, this code saves the changed data about the person
        	 */
        	public void actionPerformed(ActionEvent arg0) {
        		if (!listPeople.isSelectionEmpty())
        			saveChangedPerson(listPeopleSelection);
        	}
        });
        btnSave.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panelControl.add(btnSave);
        
        // Add roles panel
        panelRoles = new JPanel();
        panelRoles.setBorder(new EmptyBorder(10, 0, 0, 0));
        panelRoles.setLayout(new BorderLayout(0, 0));
        panelRoles.setPreferredSize(new Dimension(300, 90));
        panelRoles.setMinimumSize(new Dimension(300, 90));
        panelRoles.setMaximumSize(new Dimension(300, 90));
        add(panelRoles);
        
        // Add roles label
        labelRoles = new JLabel("Person's Roles");
        labelRoles.setBorder(new EmptyBorder(0, 10, 0, 10));
        labelRoles.setPreferredSize(new Dimension(140, 20));
        labelRoles.setMinimumSize(new Dimension(140, 20));
        labelRoles.setMaximumSize(new Dimension(140, 20));
        labelRoles.setFont(getFont().deriveFont(Font.BOLD));
        panelRoles.add(labelRoles, BorderLayout.NORTH);
        
        // Add roles scroll pane
        scrollRoles = new JScrollPane();
        scrollRoles.setBorder(new LineBorder(SystemColor.menu, 3));
        scrollRoles.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollRoles.setPreferredSize(new Dimension(300, 60));
        scrollRoles.setMinimumSize(new Dimension(300, 60));
        scrollRoles.setMaximumSize(new Dimension(300, 60));
        panelRoles.add(scrollRoles, BorderLayout.CENTER);
        
        // Add roles list
        listRoles = new JList<RoleInterface>(roleListModel);
        listRoles.addListSelectionListener(new ListSelectionListener() {
			/**
			 * When a Role is selected from the list, this is the code that kicks off the GUI update
			 * It also sets up a listener so that the GUI will update with changes for as long as that Role remains selected
			 */
			public void valueChanged(ListSelectionEvent arg0) {
				if (listRoles.getSelectedValue() != null) {
					if (listRolesSelection != null) {
						// Remove the listener from the previous selection
						listRolesSelection.getPropertyChangeSupport().removePropertyChangeListener(CityControlPanelEditPersonTab.this);
					}
					updateRoleFormValues(listRoles.getSelectedValue());
					listRoles.getSelectedValue().getPropertyChangeSupport().addPropertyChangeListener(CityControlPanelEditPersonTab.this);
					listRolesSelection = listRoles.getSelectedValue();
				} else {
					setRoleFormBlank();
				}
			}
		});
        listRoles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listRoles.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 3801676414916520494L;

			/**
			 * Allows RoleInterface objects to be displayed as custom text in the JList
			 */
			@Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof RoleInterface) {
                    ((JLabel) renderer).setText(((RoleInterface) value).getClass().getName());
                }
                return renderer;
            }
        });
        scrollRoles.setViewportView(listRoles);
        
        // Create role state area
        panelRoleState = new JPanel();
        add(panelRoleState);
        panelRoleState.setLayout(new BoxLayout(panelRoleState, BoxLayout.X_AXIS));
        panelRoleState.setPreferredSize(new Dimension(300, 20));
        panelRoleState.setMinimumSize(new Dimension(300, 20));
        panelRoleState.setMaximumSize(new Dimension(300, 20));
        
        labelRoleState = new JLabel("State");
        labelRoleState.setPreferredSize(new Dimension(100, 16));
        labelRoleState.setMinimumSize(new Dimension(100, 16));
        labelRoleState.setMaximumSize(new Dimension(100, 16));
        labelRoleState.setBorder(new EmptyBorder(0, 10, 0, 10));
        panelRoleState.add(labelRoleState);
        
        labelRoleStateValue = new JLabel("null");
        labelRoleStateValue.setFont(getFont().deriveFont(Font.BOLD));
        panelRoleState.add(labelRoleStateValue);
        
        // Create role active area
        panelRoleActive = new JPanel();
        add(panelRoleActive);
        panelRoleActive.setLayout(new BoxLayout(panelRoleActive, BoxLayout.X_AXIS));
        panelRoleActive.setPreferredSize(new Dimension(300, 20));
        panelRoleActive.setMinimumSize(new Dimension(300, 20));
        panelRoleActive.setMaximumSize(new Dimension(300, 20));
        
        labelRoleActive = new JLabel("Is Active");
        labelRoleActive.setPreferredSize(new Dimension(100, 16));
        labelRoleActive.setMinimumSize(new Dimension(100, 16));
        labelRoleActive.setMaximumSize(new Dimension(100, 16));
        labelRoleActive.setBorder(new EmptyBorder(0, 10, 0, 10));
        panelRoleActive.add(labelRoleActive);
        
        labelRoleActiveValue = new JLabel("null");
        labelRoleActiveValue.setFont(getFont().deriveFont(Font.BOLD));
        panelRoleActive.add(labelRoleActiveValue);
        
        // Create role activity area
        panelRoleActivity = new JPanel();
        add(panelRoleActivity);
        panelRoleActivity.setLayout(new BoxLayout(panelRoleActivity, BoxLayout.X_AXIS));
        panelRoleActivity.setPreferredSize(new Dimension(300, 20));
        panelRoleActivity.setMinimumSize(new Dimension(300, 20));
        panelRoleActivity.setMaximumSize(new Dimension(300, 20));
        
        labelRoleActivity = new JLabel("Has Activity");
        labelRoleActivity.setPreferredSize(new Dimension(100, 16));
        labelRoleActivity.setMinimumSize(new Dimension(100, 16));
        labelRoleActivity.setMaximumSize(new Dimension(100, 16));
        labelRoleActivity.setBorder(new EmptyBorder(0, 10, 0, 10));
        panelRoleActivity.add(labelRoleActivity);
        
        labelRoleActivityValue = new JLabel("null");
        labelRoleActivityValue.setFont(getFont().deriveFont(Font.BOLD));
        panelRoleActivity.add(labelRoleActivityValue);
	}
	
	// Listeners
	
	/**
	 * This updates the list when people are added and removed
	 * This updates the GUI when the currently selected Person from the list is changed
	 */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    propertyChange(evt);
                }
            });
            return;
        }
        if (evt.getSource() == dataModel) {
        	// A person has been added or removed from the list
            if (DataModel.PEOPLE.equals(evt.getPropertyName())) {
                if (evt.getOldValue() != null && evt.getNewValue() == null) {
                    peopleListModel.removeElement(evt.getOldValue());
                } else if (evt.getOldValue() == null && evt.getNewValue() != null) {
                    peopleListModel.addElement((Person) evt.getNewValue());
                }
            }
        }
        if (evt.getSource() == listPeopleSelection) {
        	if (Person.STATE.equals(evt.getPropertyName())) {
        		updatePersonFormValues(listPeopleSelection);
        	}
        	if (Person.ROLES.equals(evt.getPropertyName())) {
                if (evt.getOldValue() != null && evt.getNewValue() == null) {
                    roleListModel.removeElement(evt.getOldValue());
                } else if (evt.getOldValue() == null && evt.getNewValue() != null) {
                    roleListModel.addElement((RoleInterface) evt.getNewValue());
                }
        	}
        }
    }

    // Getters
    
	public MainFrame getMainFrame() {
		return mainFrame;
	}
	
	// Utilities
	
	public void updatePersonFormValues(Person p) {
		labelStateValue.setText(p.getState().toString());
		textName.setText(p.getName());
		textCash.setValue(p.getCash());
		roleListModel.clear();
		for (RoleInterface r : p.getRoles()) {
			roleListModel.addElement(r);
		}
	}
	
	public void updateRoleFormValues(RoleInterface r) {
		labelRoleStateValue.setText(r.getStateString());
		labelRoleActiveValue.setText(String.valueOf(r.getActive()));
		labelRoleActivityValue.setText(String.valueOf(r.getActivity()));
	}
	
	public void setPersonFormBlank() {
		labelStateValue.setText("null");
		textName.setText("");
		textCash.setText("");
		roleListModel.clear();
		setRoleFormBlank();
	}
	
	public void setRoleFormBlank() {
		labelRoleStateValue.setText("null");
		labelRoleActiveValue.setText("null");
		labelRoleActivityValue.setText("null");
	}
	
	public void saveChangedPerson(Person p) {
		p.setName(textName.getText());
		try {
			textCash.commitEdit();
		} catch (ParseException e) {}
		p.setCash(Integer.parseInt(textCash.getValue().toString()));
		listPeople.repaint();
	}
}
