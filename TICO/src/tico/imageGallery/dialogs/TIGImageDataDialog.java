/*
 * File: TIGImageDataDialog.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Patricia M. Jaray
 * 
 * Date: Mar 31, 2008
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
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


import tico.components.TButton;
import tico.components.TDialog;
import tico.configuration.TLanguage;
import tico.editor.TEditor;
import tico.imageGallery.actions.TIGModifyImageAction;
import tico.imageGallery.components.TIGImageInformation;
import tico.imageGallery.components.TIGInsertKeyWord;
import tico.imageGallery.dataBase.TIGDataBase;

/*
 * This class displays the dialog that contains all the data of an image
 * and allows to modify all that information 
 */
public class TIGImageDataDialog extends TDialog{
	
	private TEditor myEditor;
	
	private String imageName;
	
	private String imagePath;
	
	private TIGDataBase myDataBase;
	
	private TIGInsertKeyWord keyWordPanel;
	
	private TIGImageInformation imagePanel;
	
	public TIGImageDataDialog(TEditor editor, TIGDataBase dataBase,ImageIcon icon, String path) {
		super(editor, TLanguage.getString("TIGImageDataDialog.NAME"),true);	
		this.myEditor = editor;
		myDataBase = dataBase;
		TIGDataBase.conectDB();
		this.imageName = TIGDataBase.imageNameSearch(path);
		this.imagePath = path;
		
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
		
		// Create components
		// First, create component that shows the name of the image
		imagePanel = new TIGImageInformation(
				TLanguage.getString("TIGImageDataDialog.DATANAME"),icon,imageName);
		
		// Second, create the key word component that shows the concepts asociated
		//or not to the image
		keyWordPanel = new TIGInsertKeyWord(path);
				
		// Third, create two buttons, the first for modifying the image, and the second
		//for closing without changes
		JPanel buttons = new JPanel();
		TButton acceptButton = new TButton(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				Vector concepts = new Vector();
				concepts = keyWordPanel.returnKeyWords();
				TIGModifyImageAction action = new TIGModifyImageAction(myEditor,myDataBase,imagePath,imageName,concepts);
				action.actionPerformed(e);	
				dispose();
			}
		});
		acceptButton.setText(TLanguage.getString("TIGImageDataDialog.END"));
		
		TButton cancelButton = new TButton(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancelButton.setText(TLanguage.getString("TIGImageDataDialog.CANCEL"));
		
		// Place buttons
		GridBagConstraints but = new GridBagConstraints();
		buttons.setLayout(new GridBagLayout());
		
		but.fill = GridBagConstraints.CENTER;
		but.insets = new Insets(10, 5, 10, 5);
		but.gridx = 1;
		but.gridy = 0;
		buttons.add(acceptButton, but);		
		
		but.fill = GridBagConstraints.CENTER;
		but.insets = new Insets(10, 5, 10, 5);
		but.gridx = 2;
		but.gridy = 0;
		buttons.add(cancelButton, but);
					
		// Place components
		GridBagConstraints c = new GridBagConstraints();
		getContentPane().setLayout(new GridBagLayout());
		
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 0;
		getContentPane().add(imagePanel, c);

		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 1;
		getContentPane().add(keyWordPanel, c);
		
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(10, 5, 10, 5);
		c.gridx = 0;
		c.gridy = 3;
		getContentPane().add(buttons, c);		
		
		// Display the dialog
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		setResizable(false);
		pack();

		setLocationRelativeTo(editor);
		setVisible(true);
	}
	
}
