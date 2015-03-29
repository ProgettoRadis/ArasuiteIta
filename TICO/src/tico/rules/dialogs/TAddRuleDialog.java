/*
 * File: TRulesAdminDialog.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Beatriz Mateo
 * 
 * Date: Oct 22, 2007
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
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
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
import tico.rules.database.TLoadParameter;
import tico.rules.database.TLoadRule;
import tico.rules.database.TSaveRule;
import tico.rules.database.TWriteLanguageFile;

/**
 * The add rule dialog class.
 * 
 * @author Beatriz Mateo
 * @version 0.1 Oct 22, 2007
 */
public class TAddRuleDialog extends TDialog implements ActionListener {
	
	private TComboBox functionCombo;
	private TComboBox attributeCombo;
	private TComboBox attributeCombo2;
	private TComboBox parameterCombo;
	private JTextField languageField;

	public static Vector<Integer> userValue = new Vector<Integer>();
	private Vector<String> functionTypes = new Vector<String>();
	private Vector<String> functionTypesXML = new Vector<String>();
	private Vector<TAttribute> attributeList = new Vector<TAttribute>();
	private Vector<TAttribute> parameterList = new Vector<TAttribute>();
	
	/**
	 * Creates a new <code>TValidationDialog</code> for the specified
	 * <code>editor</code>.
	 * 
	 * @param editor The specified <code>editor</code>
	 */
	public TAddRuleDialog(TEditor editor) {
		super(editor, TLanguage.getString("TAddRule.TITLE"), true);
		createComponents(editor);
	}

	
	// Creates and places components
	private void createComponents(TEditor editor) {		
		JTabbedPane tabbedPane = new JTabbedPane();
		// Create entry user panel
		tabbedPane.addTab(TLanguage.getString("TAddRule.TAB"), 
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
	
	
	private JPanel createEntryPanel() {	
		JPanel entryPanel = new JPanel();
		entryPanel.setBorder(new TitledBorder(BorderFactory
				.createEtchedBorder(Color.WHITE, new Color(165, 163, 151)),
				TLanguage.getString("TAddRule.TEXT_TITLE")));
		
		// Create components
		createFunctionCombo();
		createAttributeCombo();
		createParameterCombo();
		
		JLabel functionLabel = new JLabel(TLanguage.getString("TAddRule.FUNCTION"));
		JLabel attributeLabel = new JLabel(TLanguage.getString("TAddRule.ATTRIBUTE"));
		JLabel parameterLabel = new JLabel(TLanguage.getString("TAddRule.PARAMETER"));
		JLabel languageLabel = new JLabel(TLanguage.getString("TAddRule.LANGUAGE")
				+ TSetup.getLanguage().toLowerCase());
		JLabel infoLabel = new JLabel(TLanguage.getString("TAddRule.INFOLABEL"));
		languageField = new JTextField(80);
		languageField.setColumns(20);

		// Place the components
		GridBagConstraints c = new GridBagConstraints();
		entryPanel.setLayout(new GridBagLayout());

		functionLabel.setHorizontalAlignment(SwingConstants.LEFT);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 10, 5, 15);
		c.gridx = 0;
		c.gridy = 0;
		entryPanel.add(functionLabel, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 1, 10);
		c.gridx = 0; 
		c.gridy = 1; 
		entryPanel.add(functionCombo, c);
		
		attributeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 10, 5, 15);
		c.gridx = 1;
		c.gridy = 0;
		entryPanel.add(attributeLabel, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 1, 10);
		c.gridx = 1; 
		c.gridy = 1; 
		entryPanel.add(attributeCombo, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 1, 10);
		c.gridx = 1; 
		c.gridy = 2; 
		entryPanel.add(attributeCombo2, c);
		
		parameterLabel.setHorizontalAlignment(SwingConstants.LEFT);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 10, 5, 15);
		c.gridx = 1;
		c.gridy = 3;
		entryPanel.add(parameterLabel, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 1, 10);
		c.gridx = 1;
		c.gridy = 4;
		entryPanel.add(parameterCombo, c);
		
		languageLabel.setHorizontalAlignment(SwingConstants.LEFT);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 10, 5, 15);
		c.gridx = 1;
		c.gridy = 5;
		entryPanel.add(languageLabel, c);
				
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 10, 1, 10);
		c.gridx = 1; 
		c.gridy = 6; 
		entryPanel.add(languageField, c);
		
		infoLabel.setHorizontalAlignment(SwingConstants.LEFT);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 10, 5, 15);
		c.gridx = 1;
		c.gridy = 7;
		entryPanel.add(infoLabel, c);
		
        return entryPanel;
	}
	
	
	private void createFunctionCombo() {
		functionTypes.add("");
		functionTypesXML.add("");
		
		TLoadRule load_r = new TLoadRule();
		Vector<TRule> rules = load_r.loadAllRules();
		
		for (int i=0; i<rules.size(); i++) {
			if (!functionTypes.contains(rules.get(i).getFunction()))
				functionTypes.add(rules.get(i).getFunction());
			if (!functionTypesXML.contains(rules.get(i).getFunctionXML()))
				functionTypesXML.add(rules.get(i).getFunctionXML());
		}
		functionCombo = new TComboBox(functionTypes);
		functionCombo.setMinimumSize(new Dimension(310, 25));
		functionCombo.setPreferredSize(new Dimension(310, 25));
		functionCombo.addActionListener(this);
		functionCombo.setSelectedIndex(0);
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
	
	
	public void actionPerformed (ActionEvent e) {
		Vector<String> auxAttrib = new Vector<String>();
		Vector<String> auxParam = new Vector<String>();
		JComboBox cb = (JComboBox)e.getSource();
        String funName = (String)cb.getSelectedItem();
        String funNameXML = null;
        // obtain the XML name
        if (funName!=null && funName.compareTo("")!=0) {
        	for (int i=0; i<functionTypes.size(); i++)
            	if (funName.compareTo(functionTypes.get(i))==0)
            		funNameXML = functionTypesXML.get(i);
        	parameterCombo.removeAllItems();
        	attributeCombo.removeAllItems();
        	// compare type of function
        	if (funNameXML.compareTo("exist")==0 || funNameXML.compareTo("type")==0) {
        		// select only the correct parameter type
        		for (int i=0; i<parameterList.size(); i++)
        			if (parameterList.get(i).getValue().getClass()==(Object)new Boolean(false).getClass())
        				auxParam.add(parameterList.get(i).getName());
        		// select only the correct attribute type
        		for (int i=0; i<attributeList.size(); i++)
        			if (attributeList.get(i).getValue().getClass()==(Object)new Boolean(false).getClass())
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
	
	
	private JPanel createButtonPanel(final TEditor editor) {		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		// button to save values
		TButton saveButton = new TButton(TLanguage.
				getString("TAddRule.SAVE_BUTTON"));
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// save rule in XML file
				TRule rule = new TRule();
				int ret = saveRule(rule);
				if (ret!=-1) {
					String funXML = null;
					for (int i=0; i<functionTypes.size(); i++)
						if (((String) functionCombo.getSelectedItem()).compareTo(functionTypes.get(i))==0)
							funXML = functionTypesXML.get(i);
					if (funXML.compareTo("lightness")==0)
						attributeCombo2.setSelectedIndex(0);
					
					functionCombo.setSelectedIndex(0);
					attributeCombo.setSelectedIndex(0);
					parameterCombo.setSelectedIndex(0);					
					languageField.setText("");
				}
			}
		});
		buttonPanel.add(saveButton);
		
		// button to close dialog
		TButton closeButton = new TButton(TLanguage.
				getString("TAddRule.CLOSE_BUTTON"));
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPanel.add(closeButton);
		
		return buttonPanel;
	}
	
	
	private int saveRule(TRule rule) {
		// Get function name
		if (functionCombo.getSelectedIndex()!=0) {
			for (int i=0; i<functionTypes.size(); i++)
				if (((String) functionCombo.getSelectedItem()).compareTo(functionTypes.get(i))==0)
					rule.setFunctionXML(functionTypesXML.get(i));
		} else {
			JOptionPane.showMessageDialog(null,
					TLanguage.getString("TAddRule.INFO_BLANK_FUN"), 
					TLanguage.getString("WARNING"),
					JOptionPane.WARNING_MESSAGE);
			return -1;
		}
		// Get attribute
		String atr = (String) attributeCombo.getSelectedItem();
		if (atr==null || atr.compareTo("")==0) {
			JOptionPane.showMessageDialog(null,
					TLanguage.getString("TAddRule.INFO_BLANK_ATR"), 
					TLanguage.getString("WARNING"),
					JOptionPane.WARNING_MESSAGE);
			return -1;
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
				return -1;
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
			return -1;
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
			return -1;
		}
		else {			
			// is a new rule
			TLoadRule load_r = new TLoadRule();
			Vector<TRule> ruleList = new Vector<TRule>();
			ruleList = load_r.loadAllRules();
			// variable message is the number message of the last rule
			String s[] = ruleList.lastElement().getNameXML().split("_");
			// text[1] is the number of the rule
			int num = Integer.valueOf(s[1]).intValue()+1;
			rule.setNameXML("RULE_" + num);
			rule.setMessage(languageField.getText());
			rule.setMessageXML("MESSAGE_" + num);
			new TWriteLanguageFile(rule.getMessage(), rule.getMessageXML());
			// Get type
			String str = rule.getAttribute().getNameXML();
			if (str.contains("project"))
				rule.setType("project");
			else if (str.contains("board"))
				rule.setType("board");
			else if (str.contains("cell"))
				rule.setType("cell");
			else if (str.contains("int"))
				rule.setType("interpreter");

			TSaveRule save_r = new TSaveRule();
			int ret = save_r.saveRule(rule);
			if (ret!=-1) 
    			JOptionPane.showMessageDialog(null,
						TLanguage.getString("TAddRule.SAVE_OK_MESSAGE"), 
						TLanguage.getString("INFO"),
						JOptionPane.INFORMATION_MESSAGE);
    		else 
    			JOptionPane.showMessageDialog(null,
						TLanguage.getString("TAddRule.SAVE_ERROR_MESSAGE"), 
						TLanguage.getString("ERROR"),
						JOptionPane.ERROR_MESSAGE);
		}
		return 0;
	}
}