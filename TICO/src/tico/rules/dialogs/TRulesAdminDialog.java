/*
 * File: TRulesAdminDialog.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Beatriz Mateo
 * 
 * Date: Oct 20, 2007
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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import tico.components.TButton;
import tico.components.TComboBox;
import tico.components.TDialog;
import tico.configuration.TLanguage;
import tico.configuration.TSetup;
import tico.editor.TEditor;
import tico.rules.TAttribute;
import tico.rules.TRule;
import tico.rules.database.TDeleteRule;
import tico.rules.database.TLoadParameter;
import tico.rules.database.TLoadRule;
import tico.rules.database.TSaveRule;
import tico.rules.database.TWriteLanguageFile;

/**
 * The validation dialog class.
 * 
 * @author Beatriz Mateo
 * @version 0.1 Oct 20, 2007
 */
public class TRulesAdminDialog extends TDialog implements ActionListener {
	/**
	 * Creates a new <code>TRulesAdminDialog</code> for the specified
	 * <code>editor</code>.
	 * 
	 * @param editor The specified <code>editor</code>
	 */
	
	private static String RULES_FILE_PATH = "conf" + File.separator + "rules.xml";
	private static File RULES_FILE = new File(RULES_FILE_PATH);
	private static String DEFAULT_RULES_FILE_PATH = "conf" + File.separator + "default_rules.xml";
	private static File DEFAULT_RULES_FILE = new File(DEFAULT_RULES_FILE_PATH);
	
	private TComboBox selectCombo;
	private TComboBox functionCombo;
	private TComboBox attributeCombo;
	private TComboBox attributeCombo2;
	private TComboBox parameterCombo;
	private JTextField languageField;
	
	private JRadioButton projectRadio;
	private JRadioButton boardRadio;
	private JRadioButton cellRadio;
	private JRadioButton allRadio;
	
	private Vector<TAttribute> attributeList = new Vector<TAttribute>();
	private Vector<TAttribute> parameterList = new Vector<TAttribute>();	
	private Vector<TRule> ruleList = new Vector<TRule>();
	

	// Creates the dialog
	public TRulesAdminDialog(TEditor editor) {
		super(editor, TLanguage.getString("TRulesAdminDialog.TITLE"), true);
		createComponents(editor);
	}

	
	// Creates and places components
	private void createComponents(TEditor editor) {		
		JTabbedPane tabbedPane = new JTabbedPane();
		// Create entry user panel
		tabbedPane.addTab(TLanguage.getString("TRulesAdminDialog.TAB"), 
				createEntryPanel(editor));
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
	
	
	private JPanel createEntryPanel(TEditor editor) {	
		JPanel entryPanel = new JPanel();
		entryPanel.setBorder(new TitledBorder(BorderFactory
				.createEtchedBorder(Color.WHITE, new Color(165, 163, 151)),
				TLanguage.getString("TRulesAdminDialog.TEXT_TITLE")));
		
		// Create components
		getRuleElements();
		createSelectCombo();
		createFunctionCombo();
		createAttributeCombo();
		createParameterCombo();
		createRadioButtons();
		
		// Place the components
		GridBagConstraints c = new GridBagConstraints();
		entryPanel.setLayout(new GridBagLayout());
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 10, 5, 15);
		c.gridx = 0;
		c.gridy = 0;
		entryPanel.add(createRulePanel(), c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 10, 5, 15);
		c.gridx = 0;
		c.gridy = 1;
		entryPanel.add(createRuleButtonPanel(editor), c);

        return entryPanel;
	}
	
	private void getRuleElements() {
		TLoadRule rules = new TLoadRule();
		ruleList = rules.loadAllRules();
	}
	
	
	private void createSelectCombo() {
		selectCombo = new TComboBox();
		selectCombo.addItem("");
		for (int i=0; i<ruleList.size(); i++) {
			if (ruleList.get(i).getFunctionXML().compareTo("lightness")!=0) {
				String[] fun = ruleList.get(i).getFunction().split("'");
				selectCombo.addItem(ruleList.get(i).getName() + ": " +
						ruleList.get(i).getAttribute().getName() + " " + fun[1] + " " +
						ruleList.get(i).getParameter().getName());
			} else {
				String[] fun = ruleList.get(i).getFunction().split("'");
				selectCombo.addItem(ruleList.get(i).getName() + ": " +
					ruleList.get(i).getAttribute().getName() + ", " + 
					ruleList.get(i).getAttribute2().getName() + " " + fun[1] + " " +
					ruleList.get(i).getParameter().getName());
			}
		}
		selectCombo.setMinimumSize(new Dimension(460, 25));
		selectCombo.setPreferredSize(new Dimension(460, 25));
		
		selectCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// set in combo boxes the corresponding values
				if (selectCombo.getSelectedItem()!=null 
						&& ((String) selectCombo.getSelectedItem()).compareTo("")!=0) {
					String[] name = ((String) selectCombo.getSelectedItem()).split(": "); 
					String ruleName = name [0];
					for (int i=0; i<ruleList.size(); i++)
						if (ruleName.compareTo(ruleList.get(i).getName())==0) {
							if (ruleList.get(i).getType().compareTo("interpreter")!=0) {
							functionCombo.setSelectedItem(ruleList.get(i).getFunction());
							attributeCombo.setSelectedItem(ruleList.get(i).getAttribute().getName());
							parameterCombo.setSelectedItem(ruleList.get(i).getParameter().getName());
							languageField.setText(ruleList.get(i).getMessage());
							if (ruleList.get(i).getAttribute2()!=null) 
								attributeCombo2.setSelectedItem(ruleList.get(i).getAttribute2().getName());
							
							functionCombo.setEnabled(true);
							attributeCombo.setEnabled(true);
							parameterCombo.setEnabled(true);
							languageField.setEnabled(true);
							} else {
								functionCombo.setEnabled(false);
								attributeCombo.setEnabled(false);
								parameterCombo.setEnabled(false);
								languageField.setEnabled(false);
							}
						}
				}
			}
		});
		selectCombo.setSelectedIndex(0);
	}
	
	
	private void createFunctionCombo() {
		Vector<String> funNames = new Vector<String>();
		for (int i=0; i<ruleList.size(); i++)
			if (!funNames.contains(ruleList.get(i).getFunction()))
					funNames.add(ruleList.get(i).getFunction());
		
		functionCombo = new TComboBox();
		functionCombo.addItem("");
		for (int i=0; i<funNames.size(); i++)
			functionCombo.addItem(funNames.get(i));
		
		functionCombo.setSelectedIndex(0);
		functionCombo.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				Vector<String> auxAttrib = new Vector<String>();
				Vector<String> auxParam = new Vector<String>();
				JComboBox cb = (JComboBox)e.getSource();
		        String funName = (String)cb.getSelectedItem();
		        String funNameXML = null;
		        // obtain the XML name
		        if (funName!=null && funName.compareTo("")!=0) {
		        	for (int i=0; i<ruleList.size(); i++)
		            	if (funName.compareTo(ruleList.get(i).getFunction())==0)
		            		funNameXML = ruleList.get(i).getFunctionXML();
		        	parameterCombo.removeAllItems();
		        	attributeCombo.removeAllItems();
		        	// compare type of function
		        	if (funNameXML.compareTo("exist")==0 || funNameXML.compareTo("type")==0) {
		        		// select only the correct parameter type
		        		for (int i=0; i<parameterList.size(); i++)
		        			if (parameterList.get(i).getValue().getClass()==(Object)new Boolean(true).getClass())
		        				auxParam.add(parameterList.get(i).getName());
		        		// select only the correct attribute type
		        		for (int i=0; i<attributeList.size(); i++)
		        			if (attributeList.get(i).getValue().getClass()==(Object)new Boolean(true).getClass())
		        				auxAttrib.add(attributeList.get(i).getName());
		        	} else {
		        		// select only the correct parameter type
		        		for (int i=0; i<parameterList.size(); i++)
		        			if (parameterList.get(i).getValue().getClass()==(Object)new Integer(0).getClass())
		        				auxParam.add(parameterList.get(i).getName());
		        		// select only the correct attribute type
		        		for (int i=0; i<attributeList.size(); i++)
		        			if (attributeList.get(i).getValue().getClass()==(Object)new Integer(0).getClass())
		        				auxAttrib.add(attributeList.get(i).getName());
		        	}
		        	parameterCombo.addItem("");
		        	for (int i=0; i<auxParam.size(); i++)
		        		parameterCombo.addItem(auxParam.get(i));
		        	
		        	attributeCombo.addItem("");
		        	for (int i=0; i<auxAttrib.size(); i++)
		        		attributeCombo.addItem(auxAttrib.get(i));
		        	
		        	if (funNameXML.compareTo("lightness")==0) {
		        		attributeCombo2.removeAllItems();
		        		attributeCombo2.setEnabled(true);
		        		attributeCombo2.addItem("");
		        		for (int i=0; i<auxAttrib.size(); i++)
		        			attributeCombo2.addItem(auxAttrib.get(i));
		        	} else attributeCombo2.setEnabled(false);
		        }
			}
		});
	}
	
	
	private void createAttributeCombo() {
		// All attribute names
		attributeCombo = new TComboBox();
		attributeCombo.addItem("");		
		attributeList.add(new TAttribute(TLanguage.getString("Rules.PROJECTNUMBOARDS"), 
				"projectNumBoards", (Object)new Integer(0)));
		attributeList.add(new TAttribute(TLanguage.getString("Rules.BOARDHEIGHT"),
				"boardHeight", (Object)new Integer(0)));
		attributeList.add(new TAttribute(TLanguage.getString("Rules.BOARDWIDTH"),
				"boardWidth", (Object)new Integer(0)));
		attributeList.add(new TAttribute(TLanguage.getString("Rules.BOARDNOIMAGE"),
				"boardNoImage", (Object)new Boolean(false)));
		attributeList.add(new TAttribute(TLanguage.getString("Rules.BOARDSOUND"),
				"boardSound", (Object)new Boolean(false)));
		attributeList.add(new TAttribute(TLanguage.getString("Rules.BOARDLIGHTNESS"),
				"boardLightness", (Object)new Integer(0)));
		attributeList.add(new TAttribute(TLanguage.getString("Rules.CELLHEIGHT"),
				"cellHeight", (Object)new Integer(0)));
		attributeList.add(new TAttribute(TLanguage.getString("Rules.CELLWIDTH"),
				"cellWidth", (Object)new Integer(0)));
		attributeList.add(new TAttribute(TLanguage.getString("Rules.CELLLIGHTNESS"),
				"cellLightness", (Object)new Integer(0)));
		attributeList.add(new TAttribute(TLanguage.getString("Rules.CELLTEXT"),
				"cellText", (Object)new Integer(0)));
		attributeList.add(new TAttribute(TLanguage.getString("Rules.CELLTEXTSIZE"),
				"cellTextSize", (Object)new Integer(0)));
		attributeList.add(new TAttribute(TLanguage.getString("Rules.CELLTEXTTYPE"),
				"cellTextType", (Object)new Boolean(false)));
		attributeList.add(new TAttribute(TLanguage.getString("Rules.CELLTEXTFONT"),
				"cellTextFont", (Object)new Boolean(false)));
		attributeList.add(new TAttribute(TLanguage.getString("Rules.CELLTEXTSTYLE"),
				"cellTextStyle", (Object)new Boolean(false)));
		attributeList.add(new TAttribute(TLanguage.getString("Rules.CELLTEXTLIGHTNESS"),
				"cellTextLightness", (Object)new Integer(0)));
		attributeList.add(new TAttribute(TLanguage.getString("Rules.CELLNOIMAGE"),
				"cellNoImage", (Object)new Boolean(false)));
		attributeList.add(new TAttribute(TLanguage.getString("Rules.CELLIMAGEHEIGHT"),
				"cellImageHeight", (Object)new Integer(0)));
		attributeList.add(new TAttribute(TLanguage.getString("Rules.CELLIMAGEWIDTH"),
				"cellImageWidth", (Object)new Integer(0)));
		attributeList.add(new TAttribute(TLanguage.getString("Rules.CELLSOUND"),
				"cellSound", (Object)new Boolean(false)));
		attributeList.add(new TAttribute(TLanguage.getString("Rules.CELLBORDERWIDTH"),
				"cellBorderWidth", (Object)new Integer(0)));
		attributeList.add(new TAttribute(TLanguage.getString("Rules.CELLBORDERLIGHTNESS"),
				"cellBorderLightness", (Object)new Integer(0)));
		attributeList.add(new TAttribute(TLanguage.getString("Rules.CELLROW"),
				"cellRow", (Object)new Integer(0)));
		attributeList.add(new TAttribute(TLanguage.getString("Rules.CELLCOLUMN"),
				"cellColumn", (Object)new Integer(0)));
		attributeList.add(new TAttribute(TLanguage.getString("Rules.CELLNOALTERNATIVEICON"),
				"cellNoAlternativeIcon", (Object)new Boolean(false)));
		attributeList.add(new TAttribute(TLanguage.getString("Rules.CELLICONHEIGHT"),
				"cellIconHeight", (Object)new Integer(0)));
		attributeList.add(new TAttribute(TLanguage.getString("Rules.CELLICONWIDTH"),
				"cellIconWidth", (Object)new Integer(0)));
		
		for (int i=0; i<attributeList.size(); i++)
			attributeCombo.addItem(attributeList.get(i).getName());
		
		attributeCombo.setSelectedIndex(0);
		
		attributeCombo2 = new TComboBox();
		attributeCombo2.setEnabled(false);
	}
	
	
	private void createParameterCombo() {		
		parameterList.removeAllElements();
		TLoadParameter params = new TLoadParameter();
		parameterList = params.loadAllParameters();
		
		parameterCombo = new TComboBox();
		parameterCombo.addItem("");
		for (int i=0; i<parameterList.size(); i++)
			parameterCombo.addItem(parameterList.get(i).getName());
		
		parameterCombo.setSelectedIndex(0);
	}

	
	private void createRadioButtons() {
		projectRadio = new JRadioButton(TLanguage.getString("TRulesAdminDialog.PROJECT"));
		projectRadio.setActionCommand("project");
        boardRadio = new JRadioButton(TLanguage.getString("TRulesAdminDialog.BOARD"));
        boardRadio.setActionCommand("board");
        cellRadio = new JRadioButton(TLanguage.getString("TRulesAdminDialog.CELL"));
        cellRadio.setActionCommand("cell");
        allRadio = new JRadioButton(TLanguage.getString("TRulesAdminDialog.ALL"));
        allRadio.setActionCommand("all");
        
        ButtonGroup group = new ButtonGroup();
        group.add(projectRadio);
        group.add(boardRadio);
        group.add(cellRadio);
        group.add(allRadio);
        
        projectRadio.addActionListener(this);
        boardRadio.addActionListener(this);
        cellRadio.addActionListener(this);
        allRadio.addActionListener(this);
	}
	
	
	/**
	 * Update the selectCombo when a radio button is selected
	 */
	public void actionPerformed(ActionEvent e) {
		selectCombo.removeAllItems();
		selectCombo.addItem("");
		if (e.getActionCommand().compareTo("all")==0) {
			for (int i=0; i<ruleList.size(); i++) {
				String[] fun = ruleList.get(i).getFunction().split("'");
				selectCombo.addItem(ruleList.get(i).getName() + ": " +
					ruleList.get(i).getAttribute().getName() + " " + fun[1] + " " +
					ruleList.get(i).getParameter().getName());
			}
		} else {
		for (int i=0; i<ruleList.size(); i++)
			if (ruleList.get(i).getType().compareTo(e.getActionCommand())==0)  {
				String[] fun = ruleList.get(i).getFunction().split("'");
				selectCombo.addItem(ruleList.get(i).getName() + ": " +
						ruleList.get(i).getAttribute().getName() + " " + fun[1] + " " +
						ruleList.get(i).getParameter().getName());
			}
		}
	}
	
	private void updateCombo() {
		getRuleElements();
		selectCombo.removeAllItems();
		selectCombo.addItem("");
		if (allRadio.getActionCommand().compareTo("all")==0) {
			for (int i=0; i<ruleList.size(); i++) {
				String[] fun = ruleList.get(i).getFunction().split("'");
				selectCombo.addItem(ruleList.get(i).getName() + ": " +
					ruleList.get(i).getAttribute().getName() + " " + fun[1] + " " +
					ruleList.get(i).getParameter().getName());
			}
		} else {
		for (int i=0; i<ruleList.size(); i++) {
			String type;
			if (projectRadio.getActionCommand().compareTo("project")==0)
				type = "project";
			else if (boardRadio.getActionCommand().compareTo("board")==0)
				type = "board";
			else if (cellRadio.getActionCommand().compareTo("cell")==0)
				type = "cell";
			else type = "all";
			if (ruleList.get(i).getType().compareTo(type)==0)  {
				String[] fun = ruleList.get(i).getFunction().split("'");
				selectCombo.addItem(ruleList.get(i).getName() + ": " +
						ruleList.get(i).getAttribute().getName() + " " + fun[1] + " " +
						ruleList.get(i).getParameter().getName());
			}
		}
		}
	}

	private JPanel createRulePanel() {
		JPanel rulePanel = new JPanel();
		
		JLabel selectLabel = new JLabel(TLanguage.getString("TRulesAdminDialog.SELECT"));
		JLabel functionLabel = new JLabel(TLanguage.getString("TRulesAdminDialog.FUNCTION"));
		JLabel attributeLabel = new JLabel(TLanguage.getString("TRulesAdminDialog.ATTRIBUTE"));
		JLabel parameterLabel = new JLabel(TLanguage.getString("TRulesAdminDialog.PARAMETER"));
		JLabel languageLabel = new JLabel(TLanguage.getString("TRulesAdminDialog.LANGUAGE")
				+ TSetup.getLanguage().toLowerCase());
		JLabel infoLabel = new JLabel(TLanguage.getString("TRulesAdminDialog.INFOLABEL"));
		languageField = new JTextField(80);
		languageField.setColumns(20);

		// Place the components
		GridBagConstraints c = new GridBagConstraints();
		rulePanel.setLayout(new GridBagLayout());

		selectLabel.setHorizontalAlignment(SwingConstants.LEFT);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 10, 5, 15);
		c.gridx = 0;
		c.gridy = 0;
		rulePanel.add(selectLabel, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 1, 10);
		c.gridx = 0;
		c.gridy = 1;
		rulePanel.add(selectCombo, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 10, 5, 15);
		c.gridx = 0;
		c.gridy = 2;
		rulePanel.add(projectRadio, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 1, 10);
		c.gridx = 0;
		c.gridy = 3;
		rulePanel.add(boardRadio, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 1, 10);
		c.gridx = 0;
		c.gridy = 4;
		rulePanel.add(cellRadio, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 1, 10);
		c.gridx = 0;
		c.gridy = 5;
		rulePanel.add(allRadio, c);
		
		functionLabel.setHorizontalAlignment(SwingConstants.LEFT);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 10, 5, 15);
		c.gridx = 1;
		c.gridy = 0;
		rulePanel.add(functionLabel, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 1, 10);
		c.gridx = 1; 
		c.gridy = 1; 
		rulePanel.add(functionCombo, c);
		
		attributeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 10, 5, 15);
		c.gridx = 1;
		c.gridy = 2;
		rulePanel.add(attributeLabel, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 1, 10);
		c.gridx = 1; 
		c.gridy = 3; 
		rulePanel.add(attributeCombo, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 1, 10);
		c.gridx = 1; 
		c.gridy = 4; 
		rulePanel.add(attributeCombo2, c);
		
		parameterLabel.setHorizontalAlignment(SwingConstants.LEFT);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 10, 5, 15);
		c.gridx = 1;
		c.gridy = 5;
		rulePanel.add(parameterLabel, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 1, 10);
		c.gridx = 1;
		c.gridy = 6;
		rulePanel.add(parameterCombo, c);
		
		languageLabel.setHorizontalAlignment(SwingConstants.LEFT);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 10, 5, 15);
		c.gridx = 1;
		c.gridy = 7;
		rulePanel.add(languageLabel, c);
				
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 1, 10);
		c.gridx = 1; 
		c.gridy = 8; 
		rulePanel.add(languageField, c);
		
		infoLabel.setHorizontalAlignment(SwingConstants.LEFT);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 10, 5, 15);
		c.gridx = 1;
		c.gridy = 9;
		rulePanel.add(infoLabel, c);
		
		return rulePanel;
	}
	
	
	private JPanel createRuleButtonPanel(final TEditor editor) {		
		JPanel ruleButtonPanel = new JPanel();
		ruleButtonPanel.setLayout(new FlowLayout());

		// button to remove rules
		TButton removeButton = new TButton(TLanguage.
				getString("TRulesAdminDialog.REMOVE_BUTTON"));
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// remove the rule selected in selectCombo
				String[] name = ((String) selectCombo.getSelectedItem()).split(": ");
				if (name[0]!=null && name[0].compareTo("")!=0) {
					String ruleName = null;
					for (int i=0; i<ruleList.size(); i++)
						if (name[0].compareTo(ruleList.get(i).getName())==0)
							ruleName = ruleList.get(i).getNameXML();

					Object[] options = {TLanguage.getString(
							"TRulesAdminDialog.ACCEPT_OPTION"),
							TLanguage.getString(
							"TRulesAdminDialog.CANCEL_OPTION")};
				
					int n = JOptionPane.showOptionDialog(null,
							TLanguage.getString("TRulesAdminDialog.DELETE_MESAGE"),
							TLanguage.getString("WARNING"),
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.WARNING_MESSAGE,
							null,
							options,
							options[1]);
				
					if (n == JOptionPane.YES_OPTION) {
						TDeleteRule delRule = new TDeleteRule();
						int ret = delRule.deleteRuleByName(ruleName.toUpperCase());
						if (ret!=-1)
							JOptionPane.showMessageDialog(null,
									TLanguage.getString("TRulesAdminDialog.DELETE_OK_MESSAGE"), 
									TLanguage.getString("INFO"),
									JOptionPane.INFORMATION_MESSAGE);
						else
							JOptionPane.showMessageDialog(null,
									TLanguage.getString("TRulesAdminDialog.DELETE_ERROR_MESSAGE"), 
									TLanguage.getString("ERROR"),
									JOptionPane.ERROR_MESSAGE);
						
						updateCombo();
						selectCombo.setSelectedIndex(0);
						functionCombo.setSelectedIndex(0);
						attributeCombo.setSelectedIndex(0);
						parameterCombo.setSelectedIndex(0);
						languageField.setText("");
					}
				}
			}
		});
		
		ruleButtonPanel.add(removeButton);
		
		// button to edit rules
		TButton editButton = new TButton(TLanguage.
				getString("TRulesAdminDialog.EDIT_BUTTON"));
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// edit the rule selected in selectCombo
				String[] name = ((String) selectCombo.getSelectedItem()).split(": ");
				if (name[0]!=null && name[0].compareTo("")!=0) {
					TRule rule = new TRule();
					//String ruleName = null;
					for (int i=0; i<ruleList.size(); i++) {
						if (name[0].compareTo(ruleList.get(i).getName())==0) {
							rule.setNameXML(ruleList.get(i).getNameXML());
							if (ruleList.get(i).getType().compareTo("interpreter")!=0) 
								saveRule(rule);
						}
					}
					updateCombo();
					}	
				}
			});
		
		ruleButtonPanel.add(editButton);
		
		// button to add rules
		TButton addButton = new TButton(TLanguage.
				getString("TRulesAdminDialog.ADD_BUTTON"));
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new TAddRuleDialog(editor);
				updateRules();
			}
		});
		ruleButtonPanel.add(addButton);
		
		// button to load rules
		TButton loadButton = new TButton(TLanguage.
				getString("TRulesAdminDialog.LOAD_BUTTON"));
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] options = {TLanguage.getString(
						"TRulesAdminDialog.ACCEPT_OPTION"),
						TLanguage.getString(
						"TRulesAdminDialog.CANCEL_OPTION")};

				int n = JOptionPane.showOptionDialog(null,
						TLanguage.getString("TRulesAdminDialog.LOAD_RULES_MESSAGE"),
						TLanguage.getString("WARNING"),
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE,
						null,
						options,
						options[1]);

				if (n == JOptionPane.YES_OPTION) {
					// load and save predefined rules
					try {
						copy(DEFAULT_RULES_FILE, RULES_FILE);
						JOptionPane.showMessageDialog(null,
								TLanguage.getString("TRulesAdminDialog.LOAD_OK_MESSAGE"), 
								TLanguage.getString("INFO"),
								JOptionPane.INFORMATION_MESSAGE);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null,
								TLanguage.getString("TRulesAdminDialog.LOAD_ERROR_MESSAGE"), 
								TLanguage.getString("ERROR"),
								JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
					
					updateRules();
				}
				
			}
		});
		ruleButtonPanel.add(loadButton);
		
		return ruleButtonPanel;
	}
	
	
	private JPanel createButtonPanel(final TEditor editor) {		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		// button to close dialog
		TButton closeButton = new TButton(TLanguage.
				getString("TRulesAdminDialog.CLOSE_BUTTON"));
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPanel.add(closeButton);
		
		return buttonPanel;
	}
		
	
	private void saveRule(TRule rule) {
		// Get function name
		if (functionCombo.getSelectedIndex()!=0) {
			for (int i=0; i<ruleList.size(); i++)
				if (((String) functionCombo.getSelectedItem()).compareTo(ruleList.get(i).getFunction())==0)
					rule.setFunctionXML(ruleList.get(i).getFunctionXML());
		} else {
			JOptionPane.showMessageDialog(null,
					TLanguage.getString("TAddRule.INFO_BLANK_FUN"), 
					TLanguage.getString("WARNING"),
					JOptionPane.WARNING_MESSAGE);
		}
		// Get attribute
		String atr = (String) attributeCombo.getSelectedItem();
		if (atr==null || atr.compareTo("")==0) {
			JOptionPane.showMessageDialog(null,
					TLanguage.getString("TAddRule.INFO_BLANK_ATR"), 
					TLanguage.getString("WARNING"),
					JOptionPane.WARNING_MESSAGE);
		} else
			for (int i=0; i<attributeList.size(); i++)
				if (attributeList.get(i).getName().compareTo(atr)==0)
					rule.setAttribute(new TAttribute(null, attributeList.get(i).getNameXML()));
		// Get attribute2
		if (rule.getFunctionXML().compareTo("lightness")==0) {
			String atr2 = (String) attributeCombo2.getSelectedItem();
			if (atr2==null || atr2.compareTo("")==0) {
				JOptionPane.showMessageDialog(null,
						TLanguage.getString("TAddRule.INFO_BLANK_ATR"), 
						TLanguage.getString("WARNING"),
						JOptionPane.WARNING_MESSAGE);
			} else
				for (int i=0; i<attributeList.size(); i++)
					if (attributeList.get(i).getName().compareTo(atr2)==0)
						rule.setAttribute2(new TAttribute(null, attributeList.get(i).getNameXML()));
		}
		// Get parameter			
		String param = (String) parameterCombo.getSelectedItem();
		if (param==null || param.compareTo("")==0) {
			JOptionPane.showMessageDialog(null,
					TLanguage.getString("TAddRule.INFO_BLANK_PAR"), 
					TLanguage.getString("WARNING"),
					JOptionPane.WARNING_MESSAGE);
		}
		else
			for (int i=0; i<parameterList.size(); i++)
				if (parameterList.get(i).getName().compareTo(param)==0)
					rule.setParameter(new TAttribute(null, parameterList.get(i).getNameXML()));
		// Get text
		if (languageField.getText().compareTo("")==0) {
			JOptionPane.showMessageDialog(null,
					TLanguage.getString("TAddRule.INFO_BLANK_ES"), 
					TLanguage.getString("WARNING"),
					JOptionPane.WARNING_MESSAGE);
		}
		else {
			// rule already exists
			String s[] = rule.getNameXML().split("_");
			rule.setMessage(languageField.getText());
			rule.setMessageXML("MESSAGE_" + s[1]);
			if (TLanguage.getString("TShowResults." + rule.getMessageXML())
					.compareTo(languageField.getText())!=0)
				// if message is modified
				new TWriteLanguageFile(rule.getMessage(), rule.getMessageXML());
			// Get type
			String str = rule.getAttribute().getNameXML();
			if (str.contains("project"))
				rule.setType("project");
			if (str.contains("board"))
				rule.setType("board");
			if (str.contains("cell"))
				rule.setType("cell");

			TSaveRule save_r = new TSaveRule();
			int ret = save_r.saveRule(rule);
			if (ret!=-1) 
    			JOptionPane.showMessageDialog(null,
						TLanguage.getString("TRulesAdminDialog.EDIT_OK_MESSAGE"), 
						TLanguage.getString("INFO"),
						JOptionPane.INFORMATION_MESSAGE);
    		else 
    			JOptionPane.showMessageDialog(null,
						TLanguage.getString("TRulesAdminDialog.EDIT_ERROR_MESSAGE"), 
						TLanguage.getString("ERROR"),
						JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	private void updateRules() {
		ruleList.removeAllElements();
		getRuleElements();
		selectCombo.removeAllItems();
		selectCombo.addItem("");
		for (int i=0; i<ruleList.size(); i++) {
			if (ruleList.get(i).getFunctionXML().compareTo("lightness")!=0) {
				String[] fun = ruleList.get(i).getFunction().split("'");
				selectCombo.addItem(ruleList.get(i).getName() + ": " +
						ruleList.get(i).getAttribute().getName() + " " + fun[1] + " " +
						ruleList.get(i).getParameter().getName());
			} else {
				String[] fun = ruleList.get(i).getFunction().split("'");
				selectCombo.addItem(ruleList.get(i).getName() + ": " +
					ruleList.get(i).getAttribute().getName() + ", " + 
					ruleList.get(i).getAttribute2().getName() + " " + fun[1] + " " +
					ruleList.get(i).getParameter().getName());
			}
		}
	}
	
	
	void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
    
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
}