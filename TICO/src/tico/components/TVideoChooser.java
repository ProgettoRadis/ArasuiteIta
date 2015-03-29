/*
 * File: TVideoChooser.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Carolina Palacio
 * 
 * Date: Oct 22, 2009
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

package tico.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import tico.components.resources.TFileUtils;
import tico.components.resources.VideoFilter;
import tico.configuration.TLanguage;
import tico.editor.TFileHandler;

/**
 * Components to a video file.
 * 
 * @author Carolina Palacio
 * @version 4.0 Oct 22, 2009
 */
public class TVideoChooser extends JPanel {
	
	// TVideoChooser default constructor parameters
	private final static String DEFAULT_TITLE = TLanguage.getString("TVideoChooser.TITLE");

	// Video Panel
	private JPanel videoFilePanel;
	
	private JPanel videoURLPanel;
	
	// Video file path
	private String videoFilePath;
	
	// Video URL
	private String videoFileURL;
	
	// Video file text field
	private TTextField videoFileTextField;
	
	// Video URL text field
	private TTextField videoURLTextField;
	
	// Choose and clear buttons panel container
	private JPanel buttonPanel;
	
	// Delete video file button
	private TButton clearVideoButton;
	
	// Open video chooser dialog button
	private TButton selectVideoFileButton;
	
	// Open video URL dialog button
	private TButton insertVideoURLButton;
	
	// Allows save the directory where you get the last video file
	private static File defaultDirectory = null;
	
	/**
	 * Creates a new <code>TVideoChooser</code> with <i>NO_OPTIONS_TYPE</i>
	 * <code>type</code>.
	 */
	public TVideoChooser() {
		this(DEFAULT_TITLE);
	}

	/**
	 * Creates a new <code>TVideoChooser</code> with the specified
	 * <code>title</code>.
	 * 
	 * @param title The specified <code>title</code>
	 */
	public TVideoChooser(String title) {
		super();
		// Creates the border of the component
		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), title));
		// Creates the components
		createVideoFilePanel();
		createVideoURLPanel();
		createButtonPanel();
		// Update the components
		updateComponents();

		// Places the components
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 0, 0);
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 0;
		add(videoFilePanel, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 0, 0);
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		add(videoURLPanel, c);		

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 0, 0);
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 2;
		add(buttonPanel, c);
	}

	// Creates the video panel
	private void createVideoFilePanel() {
		videoFilePanel = new JPanel();
		
		videoFilePanel.setLayout(new FlowLayout());
		
		JLabel textFileLabel = new JLabel(TLanguage.getString("TVideoChooser.NAME"));
		videoFilePanel.add(textFileLabel);

		videoFileTextField = new TTextField();
		videoFileTextField.setColumns(20);
		videoFileTextField.setEditable(false);
		videoFilePanel.add(videoFileTextField);
		
		selectVideoFileButton = new TButton(TLanguage.getString("TVideoChooser.BUTTON_SELECT"));
		selectVideoFileButton.addActionListener(new ChooseVideoButtonListener());
		videoFilePanel.add(selectVideoFileButton);
		
	}
	
	private void createVideoURLPanel(){
		videoURLPanel = new JPanel();
		
		videoURLPanel.setLayout(new FlowLayout());
		
		JLabel textURLLabel = new JLabel(TLanguage.getString("TVideoChooser.URL"));
		videoURLPanel.add(textURLLabel);
		
		videoURLTextField = new TTextField();
		videoURLTextField.setColumns(20);
		videoURLTextField.setEditable(false);
		videoURLPanel.add(videoURLTextField);
		
		insertVideoURLButton = new TButton(TLanguage.getString("TVideoChooser.BUTTON_INSERT_URL"));
		insertVideoURLButton.addActionListener(new ChooseURLVideoButtonListener());
		videoURLPanel.add(insertVideoURLButton);
	}
	
	// Creates the button panel
	private void createButtonPanel() {
		buttonPanel = new JPanel();

		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));		

		clearVideoButton = new TButton(new AbstractAction(TLanguage.getString("TVideoChooser.BUTTON_CLEAR")) {
			public void actionPerformed(ActionEvent e) {
				setVideoFilePath(null);
				setVideoFileURL(null);
				selectVideoFileButton.setEnabled(true);
				insertVideoURLButton.setEnabled(true);
			}
		});
				
		buttonPanel.add(clearVideoButton);
	}

	/**
	 * Update all the components. Enables or disables the buttons.
	 */
	public void updateComponents() {
		
		if (videoFilePath != null) {
			clearVideoButton.setEnabled(true);
			videoFileTextField.setText(TFileUtils.getFilename(videoFilePath.toString()) + "." + 
					TFileUtils.getExtension(videoFilePath.toString()));
			videoFileTextField.setCaretPosition(0);
			insertVideoURLButton.setEnabled(false);
		} else {
			videoFileTextField.setText("");
		}		
		
		if (videoFileURL != null){
			clearVideoButton.setEnabled(true);
			videoURLTextField.setText(videoFileURL);
			videoURLTextField.setCaretPosition(0);
			selectVideoFileButton.setEnabled(false);
		} else {
			videoURLTextField.setText("");
		}
		
		if (videoFilePath == null && videoFileURL == null){
			clearVideoButton.setEnabled(false);
		}

	}

	/**
	 * Returns the selected <code>videoFilePath</code>.
	 * 
	 * @return The selected <code>videoFilePath</code>
	 */
	public String getVideoFilePath() {
		
		return videoFilePath;
	}

	/**
	 * Set the <code>videoFilePath</code>.
	 * 
	 * @param videoFilePath The <code>videoFilePath</code> to set
	 */
	public void setVideoFilePath(String videoFilePath) {
		
		this.videoFilePath = videoFilePath;
		
		updateComponents();
	}
	
	/**
	 * Returns the selected <code>videoFileURL</code>.
	 * 
	 * @return The selected <code>videoFileURL</code>
	 */
	public String getVideoFileURL() {
		
		return videoFileURL;
	}

	/**
	 * Set the <code>videoFileURL</code>.
	 * 
	 * @param videoFileURL The <code>videoFileURL</code> to set
	 */
	public void setVideoFileURL(String videoFileURL) {
		
		this.videoFileURL = videoFileURL;
		
		updateComponents();
	}
	
	
	// Action listener for the selectVideoButton
	private class ChooseVideoButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			// Open a JFileChooser
			JFileChooser fileChooser = new JFileChooser();
			// Customize JFileChooser
			fileChooser.setDialogTitle(TLanguage.getString("TVideoChooser.CHOOSE_VIDEO"));
			fileChooser.setCurrentDirectory(defaultDirectory);
			fileChooser.addChoosableFileFilter(new VideoFilter());
			fileChooser.setAcceptAllFileFilterUsed(false);

			// Checks if the user has chosen a file
			int returnValue = fileChooser.showOpenDialog((Component)null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				// Get the chosen file
				File selectedFile = fileChooser.getSelectedFile();
				// Set its directory as next first JFileChooser directory
				defaultDirectory = selectedFile.getParentFile();
					
				try {
					// Import the file to the application directory
					selectedFile = TFileHandler.importFile(selectedFile);
					setVideoFilePath(selectedFile.getAbsolutePath());				
						
					// TUNE Find a method to keep clean the application
					// directory
				} catch (Exception ex) {
					// If the import fails show an error message
					JOptionPane.showMessageDialog(null,
							TLanguage.getString("TVideoChooser.OPEN_FILE_ERROR"),
							TLanguage.getString("ERROR") + "!",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	// Action listener for the selectVideoButton
	private class ChooseURLVideoButtonListener implements ActionListener {
		
		JDialog insertURL;
		JTextField textURL;
	
		public void actionPerformed(ActionEvent e) {
			insertURL = new JDialog();
			insertURL.setTitle(TLanguage.getString("TVideoChooser.INSERT_URL_VIDEO"));
			insertURL.setLocationRelativeTo(((Component) e.getSource()).getParent());
			insertURL.setModal(true);
			
			insertURL.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			
			JLabel labelURL = new JLabel(TLanguage.getString("TVideoChooser.URL"));
			textURL = new JTextField(40);
			textURL.setText("http://");
			
			TButton acceptButton = new TButton(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {		
					insertURL.dispose();
					String url = textURL.getText();
					if (url != null && !url.equals("") && !url.equals("http://"))
						setVideoFileURL(textURL.getText());
				}
			});
			
			acceptButton.setText((TLanguage.getString("TVideoChooser.INSERT_URL_ACCEPT")));
			
			c.anchor = GridBagConstraints.CENTER;
			c.insets = new Insets(10, 10, 10, 10);
			c.gridx = 0;
			c.gridy = 0;
			insertURL.add(labelURL, c);
			
			c.gridx = 1;
			c.gridy = 0;
			insertURL.add(textURL, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = 2;
			insertURL.add(acceptButton, c);
			
			insertURL.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			insertURL.pack();
			insertURL.setVisible(true);
		}
	}
}
