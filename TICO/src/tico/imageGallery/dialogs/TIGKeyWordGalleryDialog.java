/*
 * File: TIGKeyWordGalleryDialog.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Patricia M. Jaray
 * 
 * Date: Mar 12, 2008
 * 
 * Company: Universidad de Zaragoza, CPS, DIIS
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

package tico.imageGallery.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Rectangle;

import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.ListSelectionModel;



import tico.components.TButton;
import tico.components.TDialog;
import tico.configuration.TLanguage;
import tico.editor.TEditor;
import tico.imageGallery.actions.TIGAddKeyWordAction;
import tico.imageGallery.actions.TIGDeleteKeyWordAction;
import tico.imageGallery.actions.TIGUpdateKeyWordAction;
import tico.imageGallery.dataBase.TIGDataBase;
import tico.imageGallery.tasks.TIGTableModelOneColumn;

public class TIGKeyWordGalleryDialog extends TDialog {
	
	private GridBagConstraints c;
	
	private JPanel keyWordsPanel;
	
	private JPanel buttonPanel;

	private TButton addButton;
	
	private TButton modifyButton;
	
	private TButton deleteButton;
	
	private TButton acceptButton;
	
	private JTextField text;
	
	private JTable table;
	
	private TIGTableModelOneColumn myModel;
	
	private JScrollPane scrollPaneList;
	
	protected Vector keyWordList;
	
	private TEditor editor;
	
	private TIGDataBase myDataBase;
	
	/*
	 * This class displays the window that shows all the key words that
	 * exists in the Data Base and allows to modify or delete the words
	 * and add new words 
	 */
	
	public TIGKeyWordGalleryDialog(TEditor editor,TIGDataBase dataBase) {
		super(editor, TLanguage.getString("TIGKeyWordGalleryDialog.NAME"),true);
		
		this.editor = editor;
		
		this.myDataBase = dataBase;
		TIGDataBase.conectDB();
		
		addWindowListener(new java.awt.event.WindowListener(){
			public void windowClosing(java.awt.event.WindowEvent e){
				dispose();
			}
			
			public void windowActivated(java.awt.event.WindowEvent e){}	  
			public void windowDeactivated(java.awt.event.WindowEvent e){}
			public void windowDeiconified(java.awt.event.WindowEvent e){}
			public void windowIconified(java.awt.event.WindowEvent e){}
			public void windowOpened(java.awt.event.WindowEvent e){}
			public void windowClosed(java.awt.event.WindowEvent e){}

		});
		
		//Create components
		//First, create the panel that contains the list, the text field and
		//the buttons to manage the key words
		createWordPanel();	
	
		//Create the main button
		acceptButton = new TButton(new AbstractAction(TLanguage.getString("TIGKeyWordGalleryDialog.END")) {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		//Place the components
		c = new GridBagConstraints();
		setLayout(new GridBagLayout());
	
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 30, 5, 30);
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		add(keyWordsPanel, c);
	
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 30, 5, 30);
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 2;
		add(acceptButton, c);
	
		// Display the dialog
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		setResizable(false);
		pack();

		setLocationRelativeTo(editor);
		setVisible(true);
	
	}
	
	private void createWordPanel(){
		
		keyWordsPanel = new JPanel();
		
		keyWordsPanel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151))));
			
		keyWordList = new Vector();
		
		keyWordList = TIGDataBase.getKeyWords();
		
		//Create components
		//Create list of key words	
		myModel = new TIGTableModelOneColumn(keyWordList);
	    //Create the scroll pane and add the table to it. 
	    table = new JTable(myModel);
	    
	    ListSelectionModel selection = table.getSelectionModel();
	    selection.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    table.setSelectionModel(selection);
	    
	    table.setPreferredScrollableViewportSize(new Dimension(168, 302));
	    scrollPaneList = new JScrollPane(table);  
	    table.addMouseListener(new java.awt.event.MouseAdapter(){
			public void mouseClicked(java.awt.event.MouseEvent e){
				text.setText((String)myModel.getValueAt(table.getSelectedRow(),0));
			}
		});
	    table.addKeyListener(new java.awt.event.KeyAdapter(){
			@Override
			public void keyReleased(java.awt.event.KeyEvent e){
				char letter = e.getKeyChar();
				int i = 0;
				if (('A' < letter) && (letter < 'z')){
					i = getVectorIndex(letter);
					table.setRowSelectionInterval(i,i);
					// To the position in pixels of the selected row
					Rectangle r = table.getCellRect(i, 0, false);
					// Moves the scroll so that the selected cell is visible
					scrollPaneList.getViewport().scrollRectToVisible(r);
				}
				text.setText((String)myModel.getValueAt(table.getSelectedRow(),0));
			}
		});

	  //Create text field
		text = new JTextField();	
		text.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e){
				
			}
		});
		text.setEditable(true);
		text.setPreferredSize(new Dimension (174, 25));
	    
		//Create three buttons: The first one will add a new word to the list and to the Data Base.
		//The second modifies the selected word, and the last deletes the selected word 
		//from the list and from the Data Base
		buttonPanel = new JPanel();

		addButton = new TButton(new AbstractAction(TLanguage.getString("TIGKeyWordGalleryDialog.ADD")) {
			public void actionPerformed(ActionEvent e) {
				String theText = text.getText();
				if (theText.contains(",")){
					String [] temp = null;
				    temp = theText.split(",");
				    for (int i = 0 ; i < temp.length ; i++) {
				    	String item= temp[i].trim();
				    	if((!myModel.isElement(item)) &&(item.compareToIgnoreCase("")!=0)){
							myModel.addElement(item);
							TIGAddKeyWordAction action = new TIGAddKeyWordAction(editor,item,myDataBase);					
							action.actionPerformed(e);
						}
				    }
				}
				else if((!myModel.isElement(theText)) &&(theText.compareToIgnoreCase("")!=0)){
					myModel.addElement(theText);
					TIGAddKeyWordAction action = new TIGAddKeyWordAction(editor,theText,myDataBase);					
					action.actionPerformed(e);
				}
				text.setText("");
			}
		});
		
		modifyButton = new TButton(new AbstractAction(TLanguage.getString("TIGKeyWordGalleryDialog.MODIFY")) {
			public void actionPerformed(ActionEvent e) {				
				if ((text.getText().compareTo("") !=0) &&  (text.getText().compareTo("Todas") !=0)){			
					if (table.getSelectedRow() > 0){
						String oldConcept = (String)myModel.getValueAt(table.getSelectedRow(),0);
						String newConcept = text.getText();
						if (oldConcept.compareTo(newConcept) != 0){
							TIGUpdateKeyWordAction action = new TIGUpdateKeyWordAction(editor,myDataBase,oldConcept,newConcept);					
							action.actionPerformed(e);		
							myModel.setValueAt(newConcept,table.getSelectedRow());
						}
					}
				}
				text.setText("");
			}
		});
		
		deleteButton = new TButton(new AbstractAction(TLanguage.getString("TIGKeyWordGalleryDialog.DELETE")) {
			public void actionPerformed(ActionEvent e) {
				if ((text.getText().compareTo("") != 0) &&  (text.getText().compareTo("Todas") != 0))
				{					
					if (myModel.isElement(text.getText())){
						String theText= text.getText();
							TIGDeleteKeyWordAction action = new TIGDeleteKeyWordAction(editor,theText,myDataBase);					
							action.actionPerformed(e);					
							if (action.delete()!= 0){
								myModel.deleteKeyWord2(theText);
							}
						
					}
				}
				text.setText("");
			}
		});
			
		buttonPanel.add(addButton);
		buttonPanel.add(modifyButton);
		buttonPanel.add(deleteButton);
		
		//Place the components
		c = new GridBagConstraints();
		keyWordsPanel.setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 30, 5, 30);
		c.gridx = 0;
		c.gridy = 0;
		keyWordsPanel.add(scrollPaneList, c);
			
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 30, 5, 30);
		c.gridx = 0;
		c.gridy = 1;
		keyWordsPanel.add(text, c);	
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 10, 5, 10);
		c.gridx = 0;
		c.gridy = 2;
		keyWordsPanel.add(buttonPanel, c);
		
	}		   
	
	protected int getVectorIndex(char letter){
		int i = 0;
		boolean founded = false;
		while ((i < keyWordList.size()) && (!founded)){
			String name = order((String)keyWordList.elementAt(i));
			if (name.charAt(0) >= letter){
				founded = true;
			}
			else i++;
		}
		if (!founded) i--;
		return i;
	}
	
	private static String order(String word){
		String result = word.replace(' ', '_').replace(',', '-').replace('á', 'a').replace('é', 'e').replace('í', 'i').replace('ó', 'o').replace('ú', 'u').
		replace('Á', 'A').replace('É', 'E').replace('Í', 'I').replace('Ó', 'O').replace('Ú', 'U').toLowerCase();
		return result;
	}
}	