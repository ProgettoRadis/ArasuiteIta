/*
 * File: TIGImportDBDialog.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Patricia M. Jaray
 * 
 * Date: May 09, 2008
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
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
//import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import tico.components.TButton;
import tico.components.TDialog;
import tico.configuration.TLanguage;
import tico.editor.TEditor;
import tico.imageGallery.dataBase.TIGDataBase;
import tico.imageGallery.tasks.TIGImportTask;

/*
 * This class load images from a directory and associations from a text file
 * into the Data Base. It copies the new images into the Images directory and
 * creates all the thumbnails of the images. 
 */
public class TIGImportDBDialog extends TDialog {
	
	private GridBagConstraints c;
	
	private TButton importButton;
	
	private TButton openButton;
	
	private TButton exitButton;
	
	private ButtonGroup imagesBehaviour;
	
	private JTextField directory;
	
	private JTextField text;
	
	private TIGDataBase myDataBase;
	
	private String pathImages;
	
	private static File defaultDirectory = null;	
	
	private TEditor myEditor;

	public final static int ONE_SECOND = 1000;

    private Timer timer;
	
    private JProgressBar progressBar;
    
    private TIGImportTask task;

    private JPanel contentPane;
    
    private boolean stop = false;
    
    private boolean cancel = false;
    	
	public TIGImportDBDialog(TEditor editor, TIGDataBase dataBase) {
		
		super(editor, TLanguage.getString("TIGImportDBDialog.NAME"),true);
		this.myEditor = editor;

		myDataBase = dataBase;
		
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
		
		task = new TIGImportTask();
		contentPane = createProgressDialog();
		
		JPanel setDirectory = new JPanel();
		setDirectory.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), 
				TLanguage.getString("TIGImportDBDialog.IMAGES_SEL")));
				
		JPanel buttons = new JPanel();
		
		JPanel repeatedImages = new JPanel(new GridLayout(1, 0));
		repeatedImages.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165,163,151)),
				TLanguage.getString("TIGImportDBDialog.REPEATED_IMAGES_TITLE")));
		
		imagesBehaviour = new ButtonGroup();
		JRadioButton replaceImages = new JRadioButton(TLanguage.getString("TIGImportDBDialog.REPLACE_IMAGES"),true);
		replaceImages.setActionCommand(TLanguage.getString("TIGImportDBDialog.REPLACE_IMAGES"));
		JRadioButton renameImages = new JRadioButton(TLanguage.getString("TIGImportDBDialog.ADD_IMAGES"),false);
		renameImages.setActionCommand(TLanguage.getString("TIGImportDBDialog.ADD_IMAGES"));
		imagesBehaviour.add(replaceImages);
		imagesBehaviour.add(renameImages);
		
		repeatedImages.add(replaceImages);
		repeatedImages.add(renameImages);
		
		//Create components
		//First, create the panel that contains the file chooser of the directory
		//that contains the images
		
		directory = new JTextField(40);
		directory.setEditable(false);
		
		openButton = new TButton(new AbstractAction(TLanguage.getString("TIGImportDBDialog.IMAGES")) {
			public void actionPerformed(ActionEvent e) {
				pathImages = createFileChooser();
				if (pathImages.compareTo("") != 0){
					directory.setText(pathImages);
					File myDirectory = new File(pathImages);
					String[] list = myDirectory.list(); 
					task.setLengthOfTask(list.length);
	    			progressBar.setMaximum(list.length);						
				}
			}
		});	
		
		exitButton = new TButton(new AbstractAction(TLanguage.getString("TIGImportDBDialog.EXIT")) {
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

		importButton = new TButton(new AbstractAction(TLanguage.getString("TIGImportDBDialog.IMPORT_BUTTON")) {
			public void actionPerformed(ActionEvent e) {
				// Importa Imagenes
				if (directory.getText().trim().compareTo("") == 0){
						JOptionPane.showConfirmDialog(null,
							TLanguage.getString("TIGImportDBDialog.ERROR_EMPTY_DIRECTORY"),
							TLanguage.getString("TIGImportDBDialog.ERROR"),
							JOptionPane.CLOSED_OPTION ,JOptionPane.WARNING_MESSAGE );
				}else{ //Search for images.xml file
					File myDirectory = new File(pathImages);
					String[] list = myDirectory.list(); 
					
					boolean findXML = false;
					int i = 0;
					
					while (!findXML && i<list.length){
						findXML = list[i].equals("images.xml");
						i++;
					}

					if (findXML){
						
						TIGDataBase.conectDB();
						
						pathImages = directory.getText();							
						text.setText(TLanguage.getString("TIGImportDBDialog.PROGRESS_TASK"));
						importButton.setEnabled(false);
						
						progressBar.setIndeterminate(false);						
						progressBar.setValue(0);
				
						String behaviour = imagesBehaviour.getSelection().getActionCommand();
						task.go(myEditor, myDataBase, pathImages, behaviour);	
						timer.start();
					}else{
						JOptionPane.showConfirmDialog(null,
								TLanguage.getString("TIGImportDBDialog.ERROR_FORMAT_DATABASE"),
								TLanguage.getString("TIGImportDBDialog.ERROR"),
								JOptionPane.CLOSED_OPTION ,JOptionPane.WARNING_MESSAGE );
					}
				}
			}
		});		
		
		
		//Place components
		c = new GridBagConstraints();
		setLayout(new GridBagLayout());
	
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 10, 5, 10);
		c.gridx = 0;
		c.gridy = 0;
		setDirectory.add(openButton, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 10, 5, 10);
		c.gridx = 1;
		c.gridy = 0;
		setDirectory.add(directory, c);
		
		buttons.add(importButton);
		buttons.add(exitButton);
		
		c = new GridBagConstraints();
		setLayout(new GridBagLayout());
		
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 10, 5, 10);
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 0;
		add(setDirectory,c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 10, 5, 10);
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 2;
		add(repeatedImages, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 10, 5, 10);
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 3;
		add(buttons, c);
				
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 4;
		add(contentPane, c);
	
		// Display the dialog
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		setResizable(false);
		pack();

		setLocationRelativeTo(editor);
		setVisible(true);
	
	}
	
	
	private String createFileChooser(){	
		
		//Displays the file chooser to select the directory where the images are.
		String path = "";
		
		// Open a JFileChooser
		JFileChooser fileChooser = new JFileChooser();
		// Customize JFileChooser
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setDialogTitle(TLanguage.getString("TIGImportDBDialog.IMAGES_DIRECTORY"));
		fileChooser.setCurrentDirectory(defaultDirectory);

		// Checks if the user has chosen a file
		int returnValue = fileChooser.showOpenDialog((Component)null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			// Get the chosen file
			File selectedFile = fileChooser.getSelectedFile();
			// Set its directory as next first JFileChooser directory
			defaultDirectory = selectedFile.getParentFile();
			path = selectedFile.getPath();
			path = path + selectedFile.separator;
		}
		
		return path;		
	}
	
	public JPanel createProgressDialog(){
       
		progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        //progressBar.setIndeterminate(true);

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

    //the actionPerformed method in this class
    //is called each time the Timer "goes off"
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
                importButton.setEnabled(true);
                progressBar.setValue(progressBar.getMinimum());
                
                String errors = task.getErrorImages();
                if (errors.equals("")){
                	if (!cancel){
                		JOptionPane.showConfirmDialog(null,
        						TLanguage.getString("TIGImportDBDialog.IMPORT_COMPLETED"),
        						"",
        						JOptionPane.CLOSED_OPTION ,JOptionPane.INFORMATION_MESSAGE);
                		
                	}
                }else{
                	String pathFile = System.getProperty("user.dir")+File.separator+"import_failed.txt";
                	File importFailed = new File(pathFile);
                	Calendar calendar = Calendar.getInstance();
                    Date dateNow = calendar.getTime();
                    SimpleDateFormat formatDateHour = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            		try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(importFailed, false));
						bw.write(formatDateHour.format(dateNow) + System.getProperty("line.separator"));
						bw.write(TLanguage.getString("TIGImportDBDialog.IMPORT_WITH_ERRORS_MESSAGE"));
						bw.write(System.getProperty("line.separator"));
						bw.write(errors);
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (!cancel){
						JOptionPane.showConfirmDialog(null,
	    						TLanguage.getString("TIGImportDBDialog.IMPORT_INFO")+ 
	    						System.getProperty("line.separator") + pathFile,
	    						TLanguage.getString("TIGImportDBDialog.IMPORT_WITH_ERRORS"),
	    						JOptionPane.CLOSED_OPTION ,JOptionPane.WARNING_MESSAGE);
					}                	
                }
                dispose();
            }
        }
    }
	
}
