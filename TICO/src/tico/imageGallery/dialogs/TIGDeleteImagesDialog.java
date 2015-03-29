/*
 * File: TIGDeleteImagesDialog.java
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ButtonGroup;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;


import tico.components.TButton;
import tico.components.TDialog;
import tico.configuration.TLanguage;
import tico.editor.TEditor;
import tico.imageGallery.components.TIGSearchKeyWord;
import tico.imageGallery.components.TIGSearchName;
import tico.imageGallery.components.TIGThumbnails;
import tico.imageGallery.dataBase.TIGDataBase;
import tico.imageGallery.dialogs.TIGImportDBDialog.TimerListener;
import tico.imageGallery.tasks.TIGDeleteTask;
import tico.imageGallery.tasks.TIGExportTask;

/*
 * This class displays the window for searching an image to modify any 
 * of its characteristics or to delete it from the Data Base
 */

public class TIGDeleteImagesDialog extends TDialog{
		
	private ImageIcon icon;
	
	private TEditor myEditor;
	
	private TButton deleteButton;
	
	private String imagePath;
	
	private Vector<Vector<String>> images;
	
	public Vector myResults;
	
	private JTextField text;
	
	private JProgressBar progressBar;
	
	private JPanel contentPane;
	
	private Timer timer;
	    
    private TIGDeleteTask task;
    
    private boolean stop = false;
	
    private boolean cancel = false;
    
	private TIGThumbnails thumbnailsDialog;
	
	private JPanel thumbnailsPanel;

	private TIGDataBase myDataBase;
	
	private GridBagConstraints c = new GridBagConstraints();
	
	public TIGDeleteImagesDialog(TEditor editor,TIGDataBase dataBase) {
		super(editor, true);
		myEditor = editor;
		this.myDataBase = dataBase;
		
		TIGDataBase.conectDB();
		
		addWindowListener(new java.awt.event.WindowListener(){
			public void windowClosing(java.awt.event.WindowEvent e){
				if (task.isRunning()){
					stop = true;
					cancel = true;
					JOptionPane.showConfirmDialog(null,
							TLanguage.getString("TIGOperationDB.CANCELED"),
							"",
							JOptionPane.CLOSED_OPTION , JOptionPane.INFORMATION_MESSAGE);
				}
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
		setTitle(TLanguage.getString("TEditorMenuBar.IMAGES_DELETE"));
				
		// Create components
		
		//All the images in the dataBase are shown 
		//when the window is displayed 

		images = TIGDataBase.imageSearchByName("*");
		myResults = null;
		
		// First, create the component that shows all the images
		thumbnailsPanel = new JPanel();		
		thumbnailsDialog = new TIGThumbnails(true);
		// Create thumbnails panel with no selection of images
		thumbnailsPanel = thumbnailsDialog.createThumbnailsPanel(images, false);
		
		// Second, create the component that search the names of the images
		//JPanel searchNamePanel = new JPanel();	
		TIGSearchName searchNamePanel = new TIGSearchName(thumbnailsDialog, this);
		//searchNamePanel = searchNameDialog.createSearchNamePanelDelete(this);
		
		// Third, create the component that search the images from its associations
		TIGSearchKeyWord keyWordSearchPanel = new TIGSearchKeyWord(thumbnailsDialog, this);
		//TIGSearchKeyWord keyWordSearchDialog = new TIGSearchKeyWord(this.myEditor,this.myDataBase);
		
				
		// Fourth, create three buttons, the first one to modify the image, the second one to
		//delete it, and the last one to exit the window
		JPanel buttons = new JPanel();
		
		// Fifth, create the progressBar dialog
		task = new TIGDeleteTask();
		contentPane = createProgressDialog();
		
		deleteButton = new TButton(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				
				File directory;
				File[] directoryFiles;
				
				imagePath = thumbnailsDialog.pathImageSelected();
				
				if (myResults == null){
					JOptionPane.showConfirmDialog(null,
							TLanguage.getString("TIGSearchImageDialog.MESSAGE_NO_SEARCH"),
							TLanguage.getString("TIGSearchImageDialog.DELETE_ALL"),
							JOptionPane.CLOSED_OPTION,JOptionPane.WARNING_MESSAGE);
				}else if(myResults.size()==0){
					JOptionPane.showConfirmDialog(null,
							TLanguage.getString("TIGSearchImageDialog.MESSAGE_NO_IMAGES_FOUND"),
							TLanguage.getString("TIGSearchImageDialog.DELETE_ALL"),
							JOptionPane.CLOSED_OPTION,JOptionPane.WARNING_MESSAGE);
				}
				else{
					int choosenOption = JOptionPane.showConfirmDialog(null,
							TLanguage.getString("TIGSearchImageDialog.DELETE_MESSAGE") + " " + myResults.size() + " " +
							TLanguage.getString("TIGSearchImageDialog.ENSURE_DELETE"),
							TLanguage.getString("TIGSearchImageDialog.DELETE_ALL"),
							JOptionPane.YES_NO_CANCEL_OPTION);
					if (choosenOption == JOptionPane.YES_OPTION) {
						
						text.setText(TLanguage.getString("TIGImportDBDialog.PROGRESS_TASK"));
						deleteButton.setEnabled(false);
						progressBar.setIndeterminate(false);
						
						progressBar.setMaximum(myResults.size());
						progressBar.setValue(0);

						task.go(myEditor,myDataBase,myResults);	
						timer.start();
					}
				}
			}
			
		});
		deleteButton.setText(TLanguage.getString("TIGSearchImageDialog.DELETE_ALL"));

		TButton cancelButton = new TButton(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				stop = true;
				cancel = true;
				JOptionPane.showConfirmDialog(null,
						TLanguage.getString("TIGOperationDB.CANCELED"),
						"",
						JOptionPane.CLOSED_OPTION , JOptionPane.INFORMATION_MESSAGE);
				dispose();
			}
		});
		cancelButton.setText(TLanguage.getString("TIGSearchImageDialog.CANCEL"));
		
		ButtonGroup actionGroup = new ButtonGroup();	    
	    
	    actionGroup.add(deleteButton);
	    actionGroup.add(cancelButton);

	    // Place buttons
		GridBagConstraints but = new GridBagConstraints();
		buttons.setLayout(new GridBagLayout());
		
		but.fill = GridBagConstraints.BOTH;
		but.insets = new Insets(10, 10, 10, 10);
		but.gridx = 0;
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
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 4;
		getContentPane().add(contentPane, c);
		
		// Display the dialog
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		setResizable(false);
		pack();

		setLocationRelativeTo(editor);
		setVisible(true);
	}
	
	public JPanel createProgressDialog(){
	       
		progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        //progressBar.setIndeterminate(true);
        progressBar.setMaximum(task.getLengthOfTask());
        
        JPanel panel = new JPanel();
        text = new JTextField("",20);
        text.setEditable(false);
       
        c = new GridBagConstraints();
		setLayout(new GridBagLayout());
	
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 10, 5, 10);
		c.gridx = 0;
		c.gridy = 0;
		panel.add(text, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 10, 5, 10);
		c.gridx = 0;
		c.gridy = 1;
		panel.add(progressBar, c);

        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(panel, BorderLayout.NORTH);
        //contentPane.add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        contentPane.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), 
				TLanguage.getString("TIGImportDBDialog.PROGRESS")));

        //create a timer
        timer = new Timer(100, new TimerListener());
        
        return contentPane;
		
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
		this.myResults= (Vector) result;
	}
	
	 class TimerListener implements ActionListener {
	        public void actionPerformed(ActionEvent evt) {
	            progressBar.setValue(task.getCurrent());
	            if (stop){
	            	task.stop();
	            }
	            if (cancel){
	            	task.cancel();            	
	            }
	            if (task.done()) {
	                Toolkit.getDefaultToolkit().beep();
	                timer.stop();
	                myResults.clear();
					update(myResults);
	                deleteButton.setEnabled(true);
	                progressBar.setValue(progressBar.getMinimum());               
	                if (!cancel){
	                	JOptionPane.showConfirmDialog(null,
								TLanguage.getString("TIGDeleteImagesDialog.DELETE_COMPLETED"),
								"",
								JOptionPane.CLOSED_OPTION ,JOptionPane.INFORMATION_MESSAGE);
	                }
	                dispose();
	            }
	        }
	    }
	
}
