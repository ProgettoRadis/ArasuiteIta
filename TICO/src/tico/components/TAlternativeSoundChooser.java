/*
 * File: TAlternativeSoundChooser.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Aug 22, 2006
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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import tico.components.resources.SoundFilter;
import tico.components.resources.TFileUtils;
import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;
import tico.editor.TFileHandler;

import tico.editor.dialogs.TRecordSound;
import tico.interpreter.threads.TInterpreterMp3Sound;

/**
 * Components to a alternative sound file.
 * 
 * @author Eduardo Ferrer
 * @version 1.0 May, 2012
 */
public class TAlternativeSoundChooser extends JPanel {
	// TImageChooser default constructor parameters
	private final static String DEFAULT_TITLE = TLanguage.getString("TAnotherSoundChosser.TITLE");

	// Sound file properties
	private String soundFilePath;

	// Sound file player needed variables
	private boolean stopPlayback;
	private SourceDataLine sourceDataLine;
	private AudioFormat audioFormat;
	private AudioInputStream audioInputStream;
	private AudioPlayThread audioPlayThread;
	private TInterpreterMp3Sound prueba=null;

	// Sound name panel
	private JPanel soundNamePane;
	// Sound file name text field
	private TTextField soundNameTextField;
	// Play and stop buttons
	private TButton playSoundButton;
	private TButton stopSoundButton;
	// Choose and clear buttons panel container
	private JPanel buttonPanel;
	// Clear sound button
	private TButton clearSoundButton;
	// Open image chooser dialog button
	private TButton selectSoundButton;
	//Record button;
	private TButton recordSoundButton;

	// Allows save the directory where you get the last sound file
	private static File defaultDirectory = null;

	/**
	 * Creates a new <code>TAlternativeSoundChooser</code> with <i>NO_OPTIONS_TYPE</i>
	 * <code>type</code>.
	 */
	public TAlternativeSoundChooser() {
		this(DEFAULT_TITLE);
	}

	/**
	 * Creates a new <code>TAlternativeSoundChooser</code> with the specified
	 * <code>title</code>.
	 * 
	 * @param title The specified <code>title</code>
	 */
	public TAlternativeSoundChooser(String title) {
		super();
		// Creates the border of the component
		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), title));
		// Creates the components
		createSoundNamePane();
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
		add(soundNamePane, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 0, 0);
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		add(buttonPanel, c);
	}

	// Creates the sound name pane
	private void createSoundNamePane() {
		soundNamePane = new JPanel();

		soundNamePane.setLayout(new FlowLayout());

		JLabel textNameLabel = new JLabel(TLanguage.getString("TSoundChooser.NAME"));

		soundNameTextField = new TTextField();
		soundNameTextField.setColumns(20);
		soundNameTextField.setEditable(false);

		playSoundButton = new TButton(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				playSoundFile();
			}
		});
		playSoundButton.setIcon(TResourceManager
				.getImageIcon("media-start-16.png"));
		playSoundButton.setMargin(new Insets(2, 2, 2, 2));
		playSoundButton.setToolTipText(TLanguage.getString("TSoundChooser.PLAY_TOOLTIP"));

		stopSoundButton = new TButton(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				stopSoundFile();
			}
		});
		stopSoundButton.setIcon(TResourceManager
				.getImageIcon("media-stop-16.png"));
		stopSoundButton.setMargin(new Insets(2, 2, 2, 2));
		stopSoundButton.setEnabled(false);
		stopSoundButton.setToolTipText(TLanguage.getString("TSoundChooser.STOP_TOOLTIP"));

		soundNamePane.add(textNameLabel);
		soundNamePane.add(soundNameTextField);
		soundNamePane.add(playSoundButton);
		soundNamePane.add(stopSoundButton);
	}

	// Creates the button panel
	private void createButtonPanel() {
		buttonPanel = new JPanel();

		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		selectSoundButton = new TButton(TLanguage.getString("TSoundChooser.BUTTON_SELECT"));
		selectSoundButton.addActionListener(new ChooseSoundButtonListener());
		recordSoundButton = new TButton(TLanguage.getString("TSoundChooser.RECORD"));
		recordSoundButton.addActionListener(new RecordSoundButtonListener());
		

		clearSoundButton = new TButton(new AbstractAction(TLanguage.getString("TSoundChooser.BUTTON_CLEAR")) {
			public void actionPerformed(ActionEvent e) {
				setSoundFilePath(null);
			}
		});
		
		
		buttonPanel.add(selectSoundButton);
		buttonPanel.add(recordSoundButton);
		buttonPanel.add(clearSoundButton);
	}

	/**
	 * Update all the components. Enables or disables the buttons.
	 */
	public void updateComponents() {
		
		if (soundFilePath != null) {
			
			playSoundButton.setEnabled(true);
			clearSoundButton.setEnabled(true);
			
			soundNameTextField.setText(TFileUtils.getFilename(soundFilePath));
			soundNameTextField.setCaretPosition(0);
		} else {
			playSoundButton.setEnabled(false);
			clearSoundButton.setEnabled(false);

			soundNameTextField.setText("");
		}
		
	}

	// Play the selected alternative sound file
	private void playSoundFile() {
		String extension=TFileUtils.getExtension(soundFilePath);
		if (extension.equals("mp3"))
		{
			 prueba= new TInterpreterMp3Sound(soundFilePath);
			 prueba.TPlay();
			 
			 stopSoundButton.setEnabled(true);
			 playSoundButton.setEnabled(false);
			 clearSoundButton.setEnabled(false);
			 selectSoundButton.setEnabled(false);
		}
		else
		{	
		try {
			int sampleSizeInBits = 16;
			int internalBufferSize = AudioSystem.NOT_SPECIFIED;

			File soundFile = new File(soundFilePath);
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);

			audioFormat = audioInputStream.getFormat();

			DataLine.Info dataLineInfo = new DataLine.Info(
					SourceDataLine.class, audioFormat, internalBufferSize);
			boolean isSuportedDirectly = AudioSystem
					.isLineSupported(dataLineInfo);

			if (!isSuportedDirectly) {
				AudioFormat sourceFormat = audioFormat;
				AudioFormat targetFormat = new AudioFormat(
						AudioFormat.Encoding.PCM_SIGNED, sourceFormat
								.getSampleRate(), sampleSizeInBits,
						sourceFormat.getChannels(), sourceFormat.getChannels()
								* (sampleSizeInBits / 8), sourceFormat
								.getSampleRate(), false);

				audioInputStream = AudioSystem.getAudioInputStream(
						targetFormat, audioInputStream);
				audioFormat = audioInputStream.getFormat();
			}

			dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat,
					internalBufferSize);
			sourceDataLine = (SourceDataLine)AudioSystem.getLine(dataLineInfo);
			sourceDataLine.open(audioFormat, internalBufferSize);

			stopPlayback = false;
			stopSoundButton.setEnabled(true);
			playSoundButton.setEnabled(false);
			clearSoundButton.setEnabled(false);
			selectSoundButton.setEnabled(false);

			audioPlayThread = new AudioPlayThread();
			audioPlayThread.start();
		} catch (UnsupportedAudioFileException e) {
			JOptionPane.showMessageDialog(null, TLanguage
					.getString("TSoundChooser.INVALID_FORMAT_ERROR"), TLanguage
					.getString("ERROR")
					+ "!", JOptionPane.ERROR_MESSAGE);
		} catch (LineUnavailableException e) {
			JOptionPane.showMessageDialog(null, TLanguage
					.getString("TSoundChooser.PLAY_FAILURE_ERROR"), TLanguage
					.getString("ERROR")
					+ "!", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, TLanguage
					.getString("TSoundChooser.OPEN_FILE_ERROR"), TLanguage
					.getString("ERROR")
					+ "!", JOptionPane.ERROR_MESSAGE);
		}
	 }
	}

	// Stop the current played sound file
	private void stopSoundFile() {
		if (prueba!=null)
		{	
			prueba.TStop();
		}
		
		else 
		{
		stopPlayback = true;
		
		}
		stopSoundButton.setEnabled(false);
		playSoundButton.setEnabled(true);
		clearSoundButton.setEnabled(true);
		selectSoundButton.setEnabled(true);
		
	}

	/**
	 * Returns the selected <code>soundFilePath</code>.
	 * 
	 * @return The selected <code>soundFilePath</code>
	 */
	public String getSoundFilePath() {
		
		return soundFilePath;
	}

	/**
	 * Set the <code>soundFilePath</code>.
	 * 
	 * @param soundFilePath The <code>soundFilePath</code> to set
	 */
	public void setSoundFilePath(String soundFilePath) {
		
		this.soundFilePath = soundFilePath;
		
		updateComponents();
	}
	
	//Action Listener for the recordSoundButton
	
	private class RecordSoundButtonListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			TRecordSound recordSoundWindow = new TRecordSound();
		        
		        if (TRecordSound.text!=null)		        	
		        {
		        	File selectedFile = TRecordSound.text;
					// Set its directory as next first JFileChooser directory
					defaultDirectory = selectedFile.getParentFile();

					try {
						// Import the file to the application directory
						selectedFile = TFileHandler.importFile(selectedFile);
						setSoundFilePath(selectedFile.getAbsolutePath());

						// TUNE Find a method to keep clean the application
						// directory
					} catch (Exception ex) {
						// If the import fails show an error message
						JOptionPane.showMessageDialog(null,
								TLanguage.getString("TSoundChooser.OPEN_FILE_ERROR"),
								TLanguage.getString("ERROR") + "!",
								JOptionPane.ERROR_MESSAGE);
					}
		        	
		        }
		}
		
	}
	// Action listener for the selectSoundButton
	private class ChooseSoundButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// Open a JFileChooser
			JFileChooser fileChooser = new JFileChooser();
			// Customoze JFileChooser
			fileChooser.setDialogTitle(TLanguage.getString("TSoundChooser.CHOOSE_SOUND"));
			fileChooser.setCurrentDirectory(defaultDirectory);
			fileChooser.addChoosableFileFilter(new SoundFilter());
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
					setSoundFilePath(selectedFile.getAbsolutePath());

					// TUNE Find a method to keep clean the application
					// directory
				} catch (Exception ex) {
					// If the import fails show an error message
					JOptionPane.showMessageDialog(null,
							TLanguage.getString("TSoundChooser.OPEN_FILE_ERROR"),
							TLanguage.getString("ERROR") + "!",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	// Audio play thread which plays the selected sound file
	public class AudioPlayThread extends Thread {
		private final static int EXTERNAL_BUFFER_SIZE = 4096;

		public void run() {
			byte tempBuffer[] = new byte[EXTERNAL_BUFFER_SIZE];

			try {
				sourceDataLine.start();

				int readBytes = 0;

				while ((readBytes != -1) && !stopPlayback) {
					readBytes = audioInputStream.read(tempBuffer, 0,
							tempBuffer.length);
					if (readBytes > 0)
						sourceDataLine.write(tempBuffer, 0, readBytes);
				}

				sourceDataLine.drain();
				sourceDataLine.close();

				stopSoundFile();

			} catch (Exception e) {
				stopSoundFile();
				JOptionPane.showMessageDialog(null,
						TLanguage.getString("TSoundChooser.OPEN_FILE_ERROR"),
						TLanguage.getString("ERROR") + "!",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
