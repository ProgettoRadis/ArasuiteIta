/*
 * File: TIGModifyImageDialog.java
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
import java.io.File;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import tico.components.TButton;
import tico.components.TDialog;
import tico.configuration.TLanguage;
import tico.editor.TEditor;
import tico.imageGallery.components.TIGSearchKeyWord;
import tico.imageGallery.components.TIGSearchName;
import tico.imageGallery.components.TIGThumbnails;
import tico.imageGallery.dataBase.TIGDataBase;

/*
 * This class displays the window for searching an image to modify any 
 * of its characteristics or to delete it from the Data Base
 */
public class TIGModifyImageDialog extends TDialog{
		
	private ImageIcon icon;
	
	private TEditor myEditor;
	
	private String imagePath;
	
	private Vector<Vector<String>> images;
	
	private TIGThumbnails thumbnailsDialog;
	
	private JPanel thumbnailsPanel;

	private TIGDataBase myDataBase;
	
	private GridBagConstraints c = new GridBagConstraints();
	
	public TIGModifyImageDialog(TEditor editor,TIGDataBase dataBase) {
		super(editor, true);
		myEditor = editor;
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
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(TLanguage.getString("TIGSearchImageDialog.NAME"));
				
		// Create components
		//All the images in the dataBase are shown 
		//when the window is displayed  
		images = TIGDataBase.imageSearchByName("*");
		
		// First, create the component that shows all the images
		thumbnailsPanel = new JPanel();		
		thumbnailsDialog = new TIGThumbnails(true);
		// Create thumbnails panel with selection of images
		thumbnailsPanel = thumbnailsDialog.createThumbnailsPanel(images, true);
		
		// Second, create the component that search the names of the images
		//JPanel searchNamePanel = new JPanel();	
		TIGSearchName searchNamePanel = new TIGSearchName(thumbnailsDialog);
		//searchNamePanel = searchNameDialog.createSearchNamePanel(this);
		
		// Third, create the component that search the images from its associations
		TIGSearchKeyWord keyWordSearchPanel = new TIGSearchKeyWord(thumbnailsDialog);
		//TIGSearchKeyWord keyWordSearchDialog = new TIGSearchKeyWord(this.myEditor,this.myDataBase);
		images = new Vector();
		
		//Fourth, create three buttons, the first one to modify the image, the second one to
		//delete it, and the last one to exit the window
		JPanel buttons = new JPanel();
		TButton modifyButton = new TButton(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				icon = thumbnailsDialog.imageSelected();
				imagePath = thumbnailsDialog.pathImageSelected();
				if (icon == null)
					JOptionPane.showConfirmDialog(null,
							TLanguage.getString("TIGSearchImageDialog.MESSAGE"),
							TLanguage.getString("TIGSearchImageDialog.ERROR"),
							JOptionPane.CLOSED_OPTION,JOptionPane.WARNING_MESSAGE );
				else{
					TIGImageDataDialog imageData = new TIGImageDataDialog(myEditor,myDataBase,icon,imagePath);
					thumbnailsPanel = thumbnailsDialog.updateThumbnailsPanel();
				}
			}
		});
		modifyButton.setText(TLanguage.getString("TIGSearchImageDialog.MODIFY"));
		
		TButton deleteButton = new TButton(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				
				icon = thumbnailsDialog.imageSelected();
				imagePath = thumbnailsDialog.pathImageSelected();
				if (icon == null)
					JOptionPane.showConfirmDialog(null,
							TLanguage.getString("TIGSearchImageDialog.MESSAGE"),
							TLanguage.getString("TIGSearchImageDialog.ERROR"),
							JOptionPane.CLOSED_OPTION,JOptionPane.WARNING_MESSAGE);
				else{
					int choosenOption = JOptionPane.showConfirmDialog(null,
							TLanguage.getString("TIGSearchImageDialog.ASK"),
							TLanguage.getString("TIGSearchImageDialog.DELETE"),
							JOptionPane.YES_NO_CANCEL_OPTION);
					if (choosenOption == JOptionPane.YES_OPTION) {
						int key = TIGDataBase.imageKeySearch(imagePath);
						TIGDataBase.deleteImageDB(key);		
						//Delete from the directory the image and its thumbnail
						File image = new File("images" + File.separator + imagePath.substring(0,1).toUpperCase() + File.separator + imagePath);
						image.delete();
						File imageTh = new File("images" + File.separator + imagePath.substring(0,1).toUpperCase() + File.separator + imagePath.substring(0,imagePath.lastIndexOf('.')) + "_th.jpg");
						imageTh.delete();
						thumbnailsPanel = thumbnailsDialog.deleteImage();
					}
					
				}
			}
		});
		deleteButton.setText(TLanguage.getString("TIGSearchImageDialog.DELETE"));

		TButton cancelButton = new TButton(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancelButton.setText(TLanguage.getString("TIGSearchImageDialog.END"));
		
		ButtonGroup actionGroup = new ButtonGroup();	    
	    actionGroup.add(modifyButton);
	    actionGroup.add(deleteButton);
	    actionGroup.add(cancelButton);
		
				
		// Place buttons
		GridBagConstraints but = new GridBagConstraints();
		buttons.setLayout(new GridBagLayout());
		
		but.fill = GridBagConstraints.BOTH;
		but.insets = new Insets(10, 10, 10, 10);
		but.gridx = 0;
		but.gridy = 0;
		buttons.add(modifyButton, but);
		
		but.fill = GridBagConstraints.BOTH;
		but.insets = new Insets(10, 10, 10, 10);
		but.gridx = 1;
		but.gridy = 0;
		buttons.add(deleteButton, but);
		
		but.fill = GridBagConstraints.BOTH;
		but.insets = new Insets(10, 10, 10, 10);
		but.gridx = 2;
		but.gridy = 0;
		buttons.add(cancelButton, but);
					
		// Place components
		getContentPane().setLayout(new GridBagLayout());
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 0;
		getContentPane().add(keyWordSearchPanel, c);
	
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 1;
		getContentPane().add(searchNamePanel, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 2;
		getContentPane().add(thumbnailsPanel, c);
		
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(10, 10, 10, 10);
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
	
	/**
	 * Returns the selected <code>icon</code>.
	 * 
	 * @return The selected <code>icon</code>
	 */
	public ImageIcon getIcon() {
		return icon;
	}
	
	/*
	 * Update the thumbnails panel when a search has been made
	 */
	public void update(Vector result){
		thumbnailsPanel = thumbnailsDialog.updateThumbnailsPanel(result,0);
	}
	
	
}
