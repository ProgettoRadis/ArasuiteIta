/*
 * File: TIGManageGalleryDialog.java
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

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;



import tico.components.TButton;
import tico.components.TDialog;
import tico.configuration.TLanguage;
import tico.editor.TEditor;
import tico.imageGallery.actions.TIGInsertImageAction;
import tico.imageGallery.components.TIGInsertKeyWord;
import tico.imageGallery.components.TIGSelectNewImage;
import tico.imageGallery.dataBase.TIGDataBase;

/*
 * This class displays the window for inserting new images to the Data Base
 */
public class TIGManageGalleryDialog extends TDialog{
	
	protected TEditor myEditor;
	
	protected String image;
	
	protected String name;
	
	protected Vector concepts;
	
	protected TIGSelectNewImage imagePanel;
	
	protected JPanel repeatedImages;
	
	protected ButtonGroup imagesBehaviour;
	
	protected TIGInsertKeyWord keyWordPanel;
	
	protected TIGDataBase myDataBase;
	
	public TIGManageGalleryDialog(TEditor editor, TIGDataBase dataBase) {
		super(editor, TLanguage.getString("TIGManageGalleryDialog.NAME"),true);

		this.myEditor = editor;
		
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
		
		// Create components
		// First, create the File chooser that selects an image
		imagePanel = new TIGSelectNewImage(TLanguage.getString("TIGManageGalleryDialog.IMAGE"));
		
		//Second, create repeated images panel
		repeatedImages = new JPanel(new GridLayout(1, 0));
		repeatedImages.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165,163,151)),
				TLanguage.getString("TIGManageGalleryDialog.REPEATED_IMAGES_TITLE")));
		
		imagesBehaviour = new ButtonGroup();
		JRadioButton replaceImages = new JRadioButton(TLanguage.getString("TIGManageGalleryDialog.REPLACE_IMAGE"),true);
		replaceImages.setActionCommand(TLanguage.getString("TIGManageGalleryDialog.REPLACE_IMAGE"));
		JRadioButton renameImages = new JRadioButton(TLanguage.getString("TIGManageGalleryDialog.ADD_IMAGE"),false);
		renameImages.setActionCommand(TLanguage.getString("TIGManageGalleryDialog.ADD_IMAGE"));
		imagesBehaviour.add(replaceImages);
		imagesBehaviour.add(renameImages);
		
		repeatedImages.add(replaceImages);
		repeatedImages.add(renameImages);
		
		// Third, create key word component
		keyWordPanel = new TIGInsertKeyWord();
		
				
		// Fourth, create buttons
		JPanel buttons = new JPanel();
		TButton insertButton = new TButton(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				image = imagePanel.returnImage();
				if (image != null){
					concepts = keyWordPanel.returnKeyWords();
					String behaviour = imagesBehaviour.getSelection().getActionCommand();
					TIGInsertImageAction action = new TIGInsertImageAction(myEditor,concepts,image,myDataBase,behaviour);
					action.actionPerformed(e);
				}
				dispose();
			}
		});
		insertButton.setText(TLanguage.getString("TIGManageGalleryDialog.END"));
		
		TButton cancelButton = new TButton(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancelButton.setText(TLanguage.getString("TIGManageGalleryDialog.CANCEL"));
		
		// Place buttons
		GridBagConstraints but = new GridBagConstraints();
		buttons.setLayout(new GridBagLayout());
		
		but.fill = GridBagConstraints.CENTER;
		but.insets = new Insets(5, 5, 5, 5);
		but.gridx = 1;
		but.gridy = 0;
		buttons.add(insertButton, but);
		
		
		but.fill = GridBagConstraints.CENTER;
		but.insets = new Insets(5, 5, 5, 5);
		but.gridx = 2;
		but.gridy = 0;
		buttons.add(cancelButton, but);
					
		// Place components
		GridBagConstraints c = new GridBagConstraints();
		getContentPane().setLayout(new GridBagLayout());
		
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 0;
		getContentPane().add(imagePanel, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 1;
		getContentPane().add(repeatedImages, c);

		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 2;
		getContentPane().add(keyWordPanel, c);
		
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(5, 5, 5, 5);
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
