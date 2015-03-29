/*
 * File: TIGInsertKeyWord.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Patricia M. Jaray
 * 
 * Date: Mar 29, 2008
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

package tico.imageGallery.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import tico.components.TButton;
import tico.configuration.TLanguage;
import tico.imageGallery.dataBase.TIGDataBase;
import tico.imageGallery.tasks.TIGTableModel;

/*
 * This class manages the association between one image and the words in the Data Base.
 * It allows to add new words to the Data Base if necessary
 */
public class TIGInsertKeyWord extends JPanel{

	private GridBagConstraints c;
	
	private JPanel buttonPanel;

	private TButton addButton;	
	
	private JTextField text;
	
	private JTable table;
	
	private TIGTableModel myModel;
	
	private JScrollPane scrollPaneList;
	
	protected Vector keyWordList;
	
	/*
	 * This constructor displays a panel in the window that allows to introduce a new image 
	 * into the Data Base. This panel allows to associate the new image to the words in 
	 * the Data Base and to insert new words if necessary.
	 */
	
	public TIGInsertKeyWord(){
		super();
		// Creates the border of the component
		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), 
				TLanguage.getString("TIGKeyWordInsertDialog.NAME")));
		
		keyWordList = new Vector();
		
		keyWordList = (Vector)(TIGDataBase.getKeyWords()).clone();
				
		
		//Create components
		//Create list of key words	
		createKeyList(keyWordList);    

		JLabel label = new JLabel(TLanguage.getString("TIGKeyWordInsertDialog.LABEL"));
		
		//Create button
		createButtonsPanel();
		//Create text field
		text = new JTextField();
		text.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e){
				if (e.getActionCommand().compareTo("comboBoxEdited") == 0) 
				{
				}
			}
		});
		text.setEditable(true);
		text.setPreferredSize(new Dimension (274, 25));
		
		//Place components
		c = new GridBagConstraints();
		setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 20, 5, 20);
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		add(scrollPaneList, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 10, 5, 140);
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 1;
		add(label, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 10, 5, 10);
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 2;
		add(text, c);	
		
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 30, 5, 30);
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 3;
		add(buttonPanel, c);

	}
	
	/*
	 * This constructor displays a panel in the window that allows to manage the 
	 * associations between an image from the Data Base and the words associated to it. 
	 */
	public TIGInsertKeyWord(String path) {
		super();
		// Creates the border of the component
		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
			Color.WHITE, new Color(165, 163, 151)), 
			TLanguage.getString("TIGKeyWordInsertDialog.NAME")));
	
		keyWordList = new Vector();
		
		keyWordList = (Vector)(TIGDataBase.getKeyWords()).clone();
		Vector keyWordAsociated = TIGDataBase.asociatedConceptSearch(path);
		
		
		//Create components
		//Create list of key words	
		createKeyList(keyWordList,keyWordAsociated);    

		JLabel label = new JLabel(TLanguage.getString("TIGKeyWordInsertDialog.LABEL"));
	
		//Create buttons
		createButtonsPanel();
		//Create text field
		text = new JTextField();	
		text.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e){
				if (e.getActionCommand().compareTo("comboBoxEdited") == 0) 
				{
				}
			}
		});
		text.setEditable(true);
		text.setPreferredSize(new Dimension (274, 25));
	
		//Place the components
		c = new GridBagConstraints();
		setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 20, 5, 20);
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		add(scrollPaneList, c);
	
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 10, 5, 140);
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 1;
		add(label, c);
	
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 10, 5, 10);
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 2;
		add(text, c);	
	
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 30, 5, 30);
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 3;
		add(buttonPanel, c);
	
	}	
	
	private void createKeyList(Vector keyWordList, Vector keyWordAsociated){		
		
		//Creates the table with the words in the Data Base and shows selected
		//the words that are associated to the image
		myModel = new TIGTableModel(keyWordList);
		for (int i = 0; i<keyWordAsociated.size();i++)
			myModel.addElement((String)keyWordAsociated.elementAt(i));
        //Create the scroll pane and add the table to it. 
        table = new JTable(myModel);
        table.addMouseListener(new java.awt.event.MouseAdapter(){
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e){
				//text.setText((String)myModel.getValueAt(table.getSelectedRow(),0));
			}
		});
        table.setPreferredScrollableViewportSize(new Dimension(268, 125));
        scrollPaneList = new JScrollPane(table);
	}
	
	private void createKeyList(Vector keyWordList){		
		
		//Creates the table with the words in the Data Base 
		myModel = new TIGTableModel(keyWordList);
		
        //Create the scroll pane and add the table to it. 
        table = new JTable(myModel);
        table.addMouseListener(new java.awt.event.MouseAdapter(){
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e){
				//text.setText((String)myModel.getValueAt(table.getSelectedRow(),0));
			}
		});
        table.setPreferredScrollableViewportSize(new Dimension(268, 125));
        scrollPaneList = new JScrollPane(table);
	}
	
	private void createButtonsPanel() {
		buttonPanel = new JPanel();

		//This button adds a new word to the Data Base and associates the word to the image
		addButton = new TButton(new AbstractAction(TLanguage.getString("TIGKeyWordInsertDialog.ADD")) {
			public void actionPerformed(ActionEvent e) {
				String texto = text.getText();
				int i;
				if (texto.contains(",")){
					String [] temp = null;
				    temp = texto.split(",");
				    for (int k = 0 ; k < temp.length ; k++) {
				    	String item = temp[k].trim();
				    	if (myModel.isElement(item)){
							i = myModel.whereIsElement(item);
							myModel.setValueAt(new Boolean(true),i,1);
							myModel.updateTable(i);
							table.setRowSelectionInterval(i,i);
							Rectangle r = table.getCellRect(i, 1, true);
							scrollPaneList.getViewport().scrollRectToVisible(r);
						}else{
							if (item.compareToIgnoreCase("")!=0){
								myModel.addElement(item);
								i = myModel.whereIsElement(item);
								table.setRowSelectionInterval(i,i);
								Rectangle r = table.getCellRect(i, 1, true);
								scrollPaneList.getViewport().scrollRectToVisible(r);
							}
						}
				    }
				}else{
					if (myModel.isElement(texto)){
						i = myModel.whereIsElement(texto);
						myModel.setValueAt(new Boolean(true),i,1);
						myModel.updateTable(i);
						table.setRowSelectionInterval(i,i);
						Rectangle r = table.getCellRect(i, 1, true);
						scrollPaneList.getViewport().scrollRectToVisible(r);
					}else{
						if (texto.trim().compareToIgnoreCase("")!=0){
							myModel.addElement(texto);
							i = myModel.whereIsElement(texto);
							table.setRowSelectionInterval(i,i);
							Rectangle r = table.getCellRect(i, 1, true);
							scrollPaneList.getViewport().scrollRectToVisible(r);
						}
					}
				}				
				text.setText("");
			}
		});
				
		buttonPanel.add(addButton);
	}
	
	/*
	 * Returns the key words selected in the table
	 */
	
	public Vector returnKeyWords(){
		Vector keyWords = new Vector();
		Vector data = myModel.returnData();
		
		for (int i=0; i < data.size(); i++){
			if (((Boolean)myModel.getValueAt(i,1)).booleanValue()){
				keyWords.add(myModel.getValueAt(i,0));
			}
		}		
		return keyWords;
	}
}
