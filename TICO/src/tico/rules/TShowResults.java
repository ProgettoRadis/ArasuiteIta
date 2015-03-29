/*
 * File: TShowResults.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Beatriz Mateo
 * 
 * Date: Jun 2, 2007
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

package tico.rules;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JPanel;

import tico.configuration.TLanguage;
import tico.editor.TEditor;
import tico.interpreter.TInterpreter;
import tico.rules.database.TLoadRule;

/**
 * The results dialog class.
 * 
 * @author Beatriz Mateo
 * @version 0.1 Jun 2, 2007
 */

public class TShowResults extends TLanguage{
	/**
	 * Creates a new <code>TShowResults</code> dialog for the specified
	 * <code>editor</code>.
	 * 
	 * @param editor The specified <code>editor</code>
	 */

	private JDialog textAreaDialog;
	private final static String newline = "\n";
	private final static String tab = "\t";
	private static String text = "";
	
	
	public TShowResults() {
	}
	
	
	/**
	 * Creates the dialog
	 * @param editor The specified editor
	 * @param type Display de results of validation or the advices
	 */
	public void createDialog(TEditor editor, String type) {
		if (type=="VALIDATION")
			textAreaDialog = new JDialog(editor, TLanguage.getString("TShowResults.TEXTAREA_VALIDATION_NAME"), false);
		else textAreaDialog = new JDialog(editor, TLanguage.getString("TShowResults.TEXTAREA_DESIGNRULES_NAME"), false);
		
		// Create text area panel
		createTextAreaPanel();
		textAreaDialog.getContentPane().setLayout(new BorderLayout());
		textAreaDialog.getContentPane().add(createTextAreaPanel(), BorderLayout.NORTH);
		
		// Display the dialog
		textAreaDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		textAreaDialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				removeText();
			}
		});
		
		textAreaDialog.setResizable(false);
		textAreaDialog.pack();

		textAreaDialog.setLocationRelativeTo(editor);
		textAreaDialog.setVisible(true);
		
		removeText();
	}
	
	
	/**
	 * Creates the dialog
	 * @param interpreter The specified interpreter
	 * @param type Display de results of validation or the advices
	 */
	public void createDialog(TInterpreter interpreter, String type) {
		if (type=="VALIDATION")
			textAreaDialog = new JDialog(interpreter, TLanguage.getString("TShowResults.TEXTAREA_VALIDATION_NAME"), false);
		else textAreaDialog = new JDialog(interpreter, TLanguage.getString("TShowResults.TEXTAREA_DESIGNRULES_NAME"), false);
		
		// Create text area panel
		createTextAreaPanel();
		textAreaDialog.getContentPane().setLayout(new BorderLayout());
		textAreaDialog.getContentPane().add(createTextAreaPanel(), BorderLayout.NORTH);
		
		// Display the dialog
		textAreaDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		textAreaDialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				removeText();
			}
		});
		textAreaDialog.setResizable(false);
		textAreaDialog.pack();

		textAreaDialog.setLocationRelativeTo(interpreter);
		textAreaDialog.setVisible(true);
		
		removeText();
	}
	
	private JPanel createTextAreaPanel() {
		JPanel textAreaPanel = new JPanel();
		JTextArea textArea = new JTextArea(15, 85);
        textArea.setEditable(false);
        textArea.setFont(new Font("Courier", Font.PLAIN, 12));
        textArea.append(text);
        JScrollPane scrollPane = new JScrollPane(textArea,
                                       JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                       JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        // Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        textAreaPanel.add(scrollPane, c);
       
        textArea.setCaretPosition(0);
        
        if (text.compareTo("")==0)
        	text=TLanguage.getString("TShowResults.NO_ADVICES");
        
        return textAreaPanel;
	}
	
	/**
	 * Writes in text area the results of validation
	 * @param str The string to write
	 */
	public void addToValidationResults(String str) {
		if (str!="") {
			String[] strVector = str.split("-");
			try{
			int n_rule = Integer.parseInt(strVector[0]);

			switch (n_rule) {
				case 101:
					// element-exist
					text += strVector[2] + newline
						+ tab + TLanguage.getString("TShowResults.PROPERTY") + " "
						+ TLanguage.getString("TShowResults."+strVector[1]) + newline + newline;
					break;
				case 102:
					// element-type
					text += strVector[2] + newline
						+ tab + TLanguage.getString("TShowResults.PROPERTY") + " " 
						+ TLanguage.getString("TShowResults."+strVector[1]) + newline				
						+ tab + TLanguage.getString("TShowResults.ADVICE") + " ";
					if (strVector[1].compareTo("MESSAGE_16")==0) // text type
						text += TLanguage.getString("TShowResults.UPPERCASE") + newline + newline;
					else if (strVector[1].compareTo("MESSAGE_18")==0) // text font
						text += TLanguage.getString("TShowResults.FONT") + newline + newline;
					else if (strVector[1].compareTo("MESSAGE_19")==0) // text style
						text += TLanguage.getString("TShowResults.STYLE") + newline + newline;
					else if (strVector[1].compareTo("MESSAGE_23")==0) // text style
						text += TLanguage.getString("TShowResults.MOUSEBAR") + newline + newline;
					break;
				case 103:
					// element-comparationMin, element-comparationMan
					text += strVector[2] + newline
						+ tab + TLanguage.getString("TShowResults.PROPERTY") + " " 
						+ TLanguage.getString("TShowResults."+strVector[1]) + newline
						+ tab + TLanguage.getString("TShowResults.VALUE") + " "
						+ strVector[3] + newline;
					text += tab + TLanguage.getString("TShowResults.ADVICE") + " "
						+ strVector[4] + newline + newline;
					break;
				case 104:
					// element-lightness
					text += strVector[2] + newline
						+ tab + TLanguage.getString("TShowResults.PROPERTY") + " "
						+ TLanguage.getString("TShowResults."+strVector[1]) + newline
						+ tab + TLanguage.getString("TShowResults.VALUE") + " "
						+ strVector[3] + newline 
						+ tab + TLanguage.getString("TShowResults.ADVICE") + " " 
						+ strVector[4] + newline + newline;
					break;
				default:
					
			}
			} catch (Exception e){
				text += newline + strVector[0] + newline + newline;						
			}
		}
	}
	
	
	private void removeText() {
		text = "";
	}
	
	/**
	 * Writes in text area the advice 
	 * @param type The type of elements to take advice
	 */
	public void designRules(String type) {	
		Vector<TAttribute> attributeList = new Vector<TAttribute>();
		Vector<TRule> ruleList = new Vector<TRule>();
		
		TLoadRule load_r = new TLoadRule();
		
		if (type.compareTo("project")==0) {
			// get rule parameters of type "project"
			ruleList = load_r.loadRulesByType("project", attributeList);
			text += newline + TLanguage.getString("TShowResults.ATTRIB_PROJECT") + newline + newline;
			
			designRulesForType(ruleList);
			
			colorTable();
		}
		else if (type.compareTo("board")==0) {
			// get rule parameters of type "board"
			ruleList = load_r.loadRulesByType("board", attributeList);
			text += newline + TLanguage.getString("TShowResults.ATTRIB_BOARD") + newline + newline;
			
			designRulesForType(ruleList);
			
			// get rule parameters of type "cell"
			ruleList = load_r.loadRulesByType("cell", attributeList);
			text += newline + TLanguage.getString("TShowResults.ATTRIB_CELL") + newline + newline;
			
			designRulesForType(ruleList);
				
			colorTable();
		}
		else if (type.compareTo("interpreter")==0) {
			// get rule parameters of type "interpreter"
			ruleList = load_r.loadRulesByType("interpreter", attributeList);
			text += newline + TLanguage.getString("TShowResults.ATTRIB_INTERPRETER") + newline + newline;
			
			designRulesForType(ruleList);			
		}
	}
	
	private void designRulesForType(Vector<TRule> ruleList) {
		Vector<TRule> ruleAux = new Vector<TRule>();
		
		ruleAux.clear();
		if (ruleList.size()>0)
			for(int i=0; i<ruleList.size(); i++) {
				if (ruleAux.size()==0) {
					ruleAux.add(ruleList.get(i));
					// Find limitation type						
					String typeLim = "";
					if (TLanguage.getString("Rules." + ruleList.get(i).getParameter().getLimitType().toUpperCase()).startsWith("!"))
						typeLim = ruleList.get(i).getParameter().getLimitType();
					else 
						typeLim = TLanguage.getString("Rules." + ruleList.get(i).getParameter().getLimitType().toUpperCase());
					
					if (typeLim.compareTo("")!=0) 
						text += "(" + typeLim + ") ";
					 
					text +=	ruleList.get(i).getParameter().getName() + " --> ";
					if (ruleList.get(i).getParameter().getValue().equals((Object)false))
						text += TLanguage.getString("TShowResults.NO");
					else if (ruleList.get(i).getParameter().getValue().equals((Object)true))
						text += TLanguage.getString("TShowResults.YES");
					else text += ruleList.get(i).getParameter().getValue();
					text += newline;
				}			
				else { 
					Boolean exist = false;
					for (int j=0; j<ruleAux.size(); j++){
						if (ruleAux.get(j).getParameter().getNameXML().compareTo(ruleList.get(i).getParameter().getNameXML())==0)
							exist = true;
					}
					if (!exist) {
						ruleAux.add(ruleList.get(i));
						
						// Find limitation type
						String typeLim = "";
						if (TLanguage.getString("Rules." + ruleList.get(i).getParameter().getLimitType().toUpperCase()).startsWith("!"))
							typeLim = ruleList.get(i).getParameter().getLimitType();
						else 
							typeLim = TLanguage.getString("Rules." + ruleList.get(i).getParameter().getLimitType().toUpperCase());
						
						if (typeLim.compareTo("")!=0) 
							text += "(" + typeLim + ") ";
						
						text += ruleList.get(i).getParameter().getName() + " --> ";
						if (ruleList.get(i).getParameter().getValue().equals((Object)false))
							text += TLanguage.getString("TShowResults.NO");
						else if (ruleList.get(i).getParameter().getValue().equals((Object)true))
							text += TLanguage.getString("TShowResults.YES");
						else text += ruleList.get(i).getParameter().getValue();
						text += newline;
					}
				}
			}	
	}
	private void colorTable () {
		String language = currentLanguage;
		if (language.compareTo("Español")==0) {
			text += newline +  TLanguage.getString("TShowResults.COLORTABLE") + newline;
			text += "|-----------------------------------------------------------------------------|" + newline +
					"|  FONDO   |                               TEXTO                              |" + newline +
					"|          | negro | blanco | magenta | cian | amarillo | verde | rojo | azul |" + newline +
					"|----------|-------|--------|---------|------|----------|-------|------|------|" + newline +
					"| negro    |       |   x    |    x    |  x   |    x     |   x   |  x   |  x   |" + newline +
					"| blanco   |   x   |        |    x    |      |          |       |  x   |  x   |" + newline +
					"| magenta  |   x   |   x    |         |  x   |    x     |   x   |      |      |" + newline +
					"| cian     |   x   |        |    x    |      |          |       |  x   |  x   |" + newline +
					"| amarillo |   x   |        |    x    |      |          |       |  x   |  x   |" + newline +
					"| verde    |   x   |        |    x    |      |          |       |      |  x   |" + newline +
					"| rojo     |       |   x    |         |  x   |    x     |       |      |      |" + newline +
					"| azul     |       |   x    |         |  x   |    x     |   x   |      |      |" + newline +
					"|-----------------------------------------------------------------------------|" + newline;
		} else {
			text += newline +  TLanguage.getString("TShowResults.COLORTABLE") + newline;
			text += "|-----------------------------------------------------------------------------|" + newline +
					"|  BACKGROUND  |                               TEXT                           |" + newline +
					"|              | black | white | magenta | cyan | yellow | green | red | blue |" + newline +
					"|--------------|-------|-------|---------|------|--------|-------|-----|------|" + newline +
					"| black        |       |   x   |    x    |  x   |   x    |   x   |  x  |  x   |" + newline +
					"| white        |   x   |       |    x    |      |        |       |  x  |  x   |" + newline +
					"| magenta      |   x   |   x   |         |  x   |   x    |   x   |     |      |" + newline +
					"| cyan         |   x   |       |    x    |      |        |       |  x  |  x   |" + newline +
					"| yellow       |   x   |       |    x    |      |        |       |  x  |  x   |" + newline +
					"| green        |   x   |       |    x    |      |        |       |     |  x   |" + newline +
					"| red          |       |   x   |         |  x   |   x    |       |     |      |" + newline +
					"| blue         |       |   x   |         |  x   |   x    |   x   |     |      |" + newline +
					"|-----------------------------------------------------------------------------|" + newline;
		}
	}
}