/*
 * File: TValidationDialog.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Beatriz Mateo
 * 
 * Date: Jul 18, 2007
 * 
 * Company: Universidad de Zaragoza, CPS, DIIS, GIGA
 * 
 * License:
 * 		This program is free software: you can redistribute it and/or 
 * 		modify it under the terms of the GNU General Public License 
 * 		as published by the Free Software Foundation, either version 3
 * 		of the License, or (at your option) any later version.
 * 
 * 		This program is distributed in the hope that it will be useful,
 * 		but WITHOUT ANY WARRANTY; without even the implied warranty of
 * 		MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * 		GNU General Public License for more details.
 * 
 * 		You should have received a copy of the GNU General Public License
 *     	along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 */

package tico.rules.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import jess.JessException;

import tico.board.TBoard;
import tico.board.TProject;
import tico.components.TButton;
import tico.components.TComboBox;
import tico.components.TDialog;
import tico.configuration.TLanguage;
import tico.editor.TEditor;
import tico.interpreter.TInterpreter;
import tico.rules.TGenerateRules;
import tico.rules.TShowResults;
import tico.rules.TUser;
import tico.rules.database.TLoadLimitation;
import tico.rules.database.TLoadParameter;
import tico.rules.database.TLoadUser;


/**
 * The validation dialog class.
 * 
 * @author Beatriz Mateo
 * @version 0.1 Jul 18, 2007
 */
public class TValidationDialog extends TDialog {
	private TProject theProject=null;
	private TBoard theBoard=null;
	private TInterpreter theInterpreter=null;
	
	private TComboBox loadCombo;
	
	public Vector<Integer> limitationsValue = new Vector<Integer>();
	public Vector<String> limitationsName = new Vector<String>();
	public Vector<JSpinner> spinnerList = new Vector<JSpinner>();
	private static Vector<JLabel> rangeLabelList = new Vector<JLabel>();
	private static Vector<JLabel> labelList = new Vector<JLabel>();

	/**
	 * Creates a new <code>TValidationDialog</code> for the specified
	 * <code>editor</code> and <code>project</code>.
	 * 
	 * @param editor The specified <code>editor</code>
	 * @param project The specified <code>project</code>
	 */
	public TValidationDialog(TEditor editor, TProject project) {
		super(editor, TLanguage.getString("TValidationDialog.PROJECT_TITLE"), true);
		theProject=project;
		clearComponents();
		createComponents(editor);
	}
	
	/**
	 * Creates a new <code>TValidationDialog</code> for the specified
	 * <code>editor</code> and <code>board</code>.
	 * 
	 * @param editor The specified <code>editor</code>
	 * @param board The specified <code>board</code>
	 */
	public TValidationDialog(TEditor editor, TBoard board) {
		super(editor, TLanguage.getString("TValidationDialog.BOARD_TITLE"), true);
		theBoard=board;
		clearComponents();
		createComponents(editor);
	}
	
	
	// The dialog for interpreter validation
	public TValidationDialog(TInterpreter interpreter) {
		super(interpreter, TLanguage.getString("TValidationDialog.INTERPRETER_TITLE"), true);
		theInterpreter=interpreter;
		clearComponents();
		createComponents(interpreter);
	}
	
	
	// The dialog validation
	public TValidationDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        clearComponents();
    }
	
	
	private void clearComponents(){
		limitationsName.clear();
		limitationsValue.clear();
		spinnerList.clear();
		labelList.clear();
		rangeLabelList.clear();
	}
	
	
	// Create all components of the editor dialog and place them
	private void createComponents(TEditor editor) {	
		
		JTabbedPane tabbedPane = new JTabbedPane();
		// Create entry user panel
		tabbedPane.addTab(TLanguage.getString("TValidationDialog.TAB"), 
				createEntryPanel());
		add(tabbedPane);
        
        // Place components
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(tabbedPane, BorderLayout.NORTH);
		getContentPane().add(createButtonPanel(editor), BorderLayout.SOUTH);

		// Display the dialog
		setDefaultCloseOperation(TDialog.DISPOSE_ON_CLOSE);

		setResizable(false);
		pack();

		setLocationRelativeTo(editor);
		setVisible(true);
	}
	
	// Create all components of the interpreter dialog and place them
	private void createComponents(TInterpreter interpreter) {	
		
		JTabbedPane tabbedPane = new JTabbedPane();
		// Create entry user panel
		tabbedPane.addTab(TLanguage.getString("TValidationDialog.TAB"), 
				createEntryPanel());
		add(tabbedPane);
        
        // Place components
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(tabbedPane, BorderLayout.NORTH);
		getContentPane().add(createButtonPanel(interpreter), BorderLayout.SOUTH);

		// Display the dialog
		setDefaultCloseOperation(TDialog.DISPOSE_ON_CLOSE);

		setResizable(false);
		pack();

		setLocationRelativeTo(interpreter);
		setVisible(true);
	}
	
	private JPanel createEntryPanel() {	
		JPanel entryPanel = new JPanel();
		
		// Place the components
		GridBagConstraints c = new GridBagConstraints();
		entryPanel.setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 1, 10);
		c.gridx = 0;
		c.gridy = 0;
		entryPanel.add(createSpinnerPanel(), c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 1, 10);
		c.gridx = 0;
		c.gridy = 1;
		entryPanel.add(createComboPanel(), c);
		
        return entryPanel;
	}
	
	
	protected JPanel createSpinnerPanel() {
		JPanel spinnerPanel = new JPanel();
		createSpinners();
		
		// Place the components
		GridBagConstraints c = new GridBagConstraints();
		spinnerPanel.setLayout(new GridBagLayout());
		spinnerPanel.setBorder(new TitledBorder(BorderFactory
				.createEtchedBorder(Color.WHITE, new Color(165, 163, 151)),
				TLanguage.getString("TUsersAdminDialog.TEXT_TITLE")));
		
		int y = 0;
		for (int i=0; i<spinnerList.size(); i++) {
			labelList.get(i).setHorizontalAlignment(SwingConstants.LEFT);
			c.fill = GridBagConstraints.BOTH;
			c.insets = new Insets(5, 10, 5, 15);
			c.gridx = 0;
			c.gridy = y;
			spinnerPanel.add(labelList.get(i), c);
			
			c.fill = GridBagConstraints.BOTH;
			c.insets = new Insets(5, 5, 5, 10);
			c.gridx = 1; 
			c.gridy = y;
			spinnerPanel.add(rangeLabelList.get(i), c);
			
			c.fill = GridBagConstraints.BOTH;
			c.insets = new Insets(5, 5, 5, 10);
			c.gridx = 2; 
			c.gridy = y;
			spinnerPanel.add(spinnerList.get(i), c);
			y++;
		}
		return spinnerPanel;
	}
	
	
	private JPanel createComboPanel() {	
		JPanel comboPanel = new JPanel();
		
		// Create comboBox
		Vector<String> nameList = new Vector<String>();
		// Create the users vector
		Vector<TUser> userList = new Vector<TUser>();
		TLoadUser load_u = new TLoadUser();
		userList = load_u.loadAllUsers();
		
		nameList.add("");
		for (int i=0; i<userList.size(); i++)
			nameList.add(userList.get(i).getName());
        loadCombo = new TComboBox(nameList);
        loadCombo.setSelectedIndex(0);
        
		// Create user buttons
		TButton loadButton = new TButton(TLanguage.
				getString("TValidationDialog.LOAD_USER_BUTTON"));
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TLoadUser load_u = new TLoadUser();
				String userName=(String)loadCombo.getSelectedItem();
				if (userName!="") {
					TUser user = new TUser();
					user = load_u.loadUserByName(userName);
					// Get the values of XML
					
					// reset spinnerList
					for (int j=0; j<spinnerList.size(); j++) {
						JSpinner spinner = spinnerList.get(j);
						spinner.setValue(0);
						spinnerList.set(j, spinner);
					}
					for (int j=0; j<user.getAttributeCount(); j++) {
						JSpinner spinner = spinnerList.get(j);
						spinner.setValue(user.getAttribute(j).getValue());
						spinnerList.set(j, spinner);
					}	
				}
			}
		});

		// Place the components
		GridBagConstraints c = new GridBagConstraints();
		comboPanel.setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 1, 10);
		c.gridx = 0;
		c.gridy = 0;
		comboPanel.add(loadCombo, c);
		
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 1;
		comboPanel.add(loadButton, c);
		
        return comboPanel;
	}
	
	
	private void createSpinners() {
		TLoadLimitation load_l = new TLoadLimitation();
		limitationsName = load_l.getLimitationsName();
		
		Vector<String> list = new Vector<String>();
		Vector<Integer> sizes = new Vector<Integer>();
		
		for (int i=0; i<limitationsName.size(); i++)
			if (!list.contains(limitationsName.get(i))){
				list.add(limitationsName.get(i));
				sizes.add(1);
			} else 
				sizes.setElementAt(Integer.valueOf(sizes.lastElement())+1,
						sizes.indexOf(sizes.lastElement()));
		for (int i=0; i<list.size(); i++) {
			// Get number of values of limitations
				
			SpinnerModel model =
				new SpinnerNumberModel(0, //initial value
			                           0, //min
			                           sizes.get(i)-1, //max
			                           1); //step
			JSpinner spinner = new JSpinner(model);
			spinnerList.add(spinner);
			
			if (TLanguage.getString("TValidationDialog." + list.elementAt(i).toUpperCase()).startsWith("!"))
				labelList.add(new JLabel(list.elementAt(i)));
			else
				labelList.add(new JLabel(TLanguage.getString("TValidationDialog."
				+list.elementAt(i).toUpperCase())));
				
			int init = 0;
			String range = Integer.valueOf(init).toString() + "/" 
				+ Integer.valueOf(sizes.get(i)-1).toString();
			rangeLabelList.add(new JLabel(range));
		}
	}
	
	
	// creates the button panel for the editor to place the buttons for help,
	// validation and close
	private JPanel createButtonPanel(final TEditor editor) {		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		// button to help user
		TButton helpButton = new TButton(TLanguage.
				getString("TValidationDialog.BUTTONDESIGN"));
		helpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

		        for (int j=0; j<spinnerList.size(); j++)
		        	limitationsValue.add((Integer) spinnerList.elementAt(j).getValue());
		        limitationsValue.setSize(spinnerList.size());
		        
		        // get the different limitationNames
		        Vector<String> nameList = new Vector<String>();
		        for (int i=0; i<limitationsName.size(); i++)
					if (!nameList.contains(limitationsName.get(i)))
						nameList.add(limitationsName.get(i));
		        
				// Get the minimum values of board
				TLoadParameter load_p = new TLoadParameter();
				load_p.getParametersForValidation(nameList, limitationsValue);
				
				dispose();
				// Create the text area dialog containing the design rules
				TShowResults results = new TShowResults();
				// design rules for project or current board
				if (theProject!=null) {
					// rules for project
					results.designRules("project");
				}
				else {
					// rules for board
					results.designRules("board");
				}
				results.createDialog(editor,"DESIGNRULES");
			}
		});
		buttonPanel.add(helpButton);
		
		// button to validate
		TButton validateButton = new TButton(TLanguage.
				getString("TValidationDialog.BUTTONVALIDATE"));
		validateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// get the limitation values
		        for (int j=0; j<spinnerList.size(); j++)
		        	limitationsValue.add((Integer) spinnerList.elementAt(j).getValue());
		        limitationsValue.setSize(spinnerList.size());
		        
		        // get the different limitation names
		        Vector<String> nameList = new Vector<String>();
		        for (int i=0; i<limitationsName.size(); i++)
					if (!nameList.contains(limitationsName.get(i)))
						nameList.add(limitationsName.get(i));
		       
				TLoadParameter load_p = new TLoadParameter();
				load_p.getParametersForValidation(nameList, limitationsValue);
				
				//validate project or current board
				if (theProject!=null) {
					// Validate the project
					TGenerateRules gRules = new TGenerateRules();
					try {
						gRules.getProjectAttributes(theProject);
					} catch (JessException e1) {
						e1.printStackTrace();
					}
				}
				else {
					// Validate the board
					TGenerateRules gRules = new TGenerateRules();
					try {
						gRules.getBoardAttributes(theBoard);
					} catch (JessException e1) {
						e1.printStackTrace();
					}
				}
				dispose();
				// Create the text area dialog containing the results
				TShowResults results = new TShowResults();
				results.createDialog(editor,"VALIDATION");
			}
		});
		buttonPanel.add(validateButton);
		
		// button close dialog
		TButton closeButton = new TButton(TLanguage.
				getString("TValidationDialog.CLOSE_BUTTON"));
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPanel.add(closeButton);
		
		return buttonPanel;
	}
	
	
	// creates the button panel for the interpreter to place the buttons for help,
	// validation and close
	private JPanel createButtonPanel(final TInterpreter interpreter) {		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		// button to help user
		TButton helpButton = new TButton(TLanguage.
				getString("TValidationDialog.BUTTONDESIGN"));
		helpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// get the limitation values
		        for (int j=0; j<spinnerList.size(); j++)
		        	limitationsValue.add((Integer) spinnerList.elementAt(j).getValue());
		        limitationsValue.setSize(spinnerList.size());
		        
		        // get the different limitation names
		        Vector<String> nameList = new Vector<String>();
		        for (int i=0; i<limitationsName.size(); i++)
					if (!nameList.contains(limitationsName.get(i)))
						nameList.add(limitationsName.get(i));
		        
				// Get the minimum values of board
				TLoadParameter load_p = new TLoadParameter();
				load_p.getParametersForValidation(nameList, limitationsValue);
				
				// Create the text area dialog containing the design rules
				TShowResults results = new TShowResults();
				// design rules for interpreter
				results.designRules("interpreter");
				results.createDialog(interpreter, "DESIGNRULES");
				
				dispose();
			}
		});
		buttonPanel.add(helpButton);
		
		// button to validate
		TButton validateButton = new TButton(TLanguage.
				getString("TValidationDialog.BUTTONVALIDATE"));
		validateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

		        for (int j=0; j<spinnerList.size(); j++)
		        	limitationsValue.add((Integer) spinnerList.elementAt(j).getValue());
		        limitationsValue.setSize(spinnerList.size());

		        // get the different limitation names
		        Vector<String> nameList = new Vector<String>();
		        for (int i=0; i<limitationsName.size(); i++)
					if (!nameList.contains(limitationsName.get(i)))
						nameList.add(limitationsName.get(i));
		        
				TLoadParameter load_p = new TLoadParameter();
				load_p.getParametersForValidation(nameList, limitationsValue);
				
				//validate interpreter
				TGenerateRules gRules = new TGenerateRules();
				try {
					gRules.getInterpreterAttributes(theInterpreter);
				} catch (JessException e1) {
					e1.printStackTrace();
				}
				dispose();
				// Create the text area dialog containing the results
				TShowResults results = new TShowResults();
				results.createDialog(interpreter,"VALIDATION");
			}
		});
		buttonPanel.add(validateButton);
		
		// button close dialog
		TButton closeButton = new TButton(TLanguage.
				getString("TValidationDialog.CLOSE_BUTTON"));
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPanel.add(closeButton);
		
		return buttonPanel;
	}
}