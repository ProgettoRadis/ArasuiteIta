/*
 * File: TInterpreterVideo.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Carolina Palacio
 * 
 * Date: Nov, 2009
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

package tico.interpreter.threads;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.UIManager;

import tico.components.resources.TResourceManager;
import tico.interpreter.TInterpreter;
import tico.interpreter.listeners.TVideoListener;
import de.humatic.dsj.DSFiltergraph;
import de.humatic.dsj.DSJException;
import de.humatic.dsj.DSJUtils;

/**
 * The video thread (WMV, AVI, MPEG, MPG format)
 * 
 * @author Carolina Palacio
 * @version 1.0 Dec, 2009
 */
public class TInterpreterVideo extends JDialog implements java.beans.PropertyChangeListener{

	private DSFiltergraph moviePanel;
	private static boolean ini=false;	
	private boolean running = true;

	//Load video dll
	static{
		try{
        	if (!ini){
        		System.loadLibrary("libs/dsj");
        		System.out.println("Video DLL loaded correctly");
        		//La siguiente linea comentada devuelve el número de bits del SO
        		//System.out.println(System.getProperty("sun.arch.data.model"));
        		ini=true;
        	}
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            System.out.println("Video DLL not loaded");
        }	
	}	

	private JAudioPlayer audioPlayer;
	/**
	This code makes use of the dsj.xml setup file located next to it. If you copy the code or jar files to run them
	from some other place, copy the xml file along with it.
	**/
	private int xVideo, yVideo, widthVideo, heightVideo;
	
	public TInterpreterVideo(String videoLocation, int x, int y, int width, int height, TInterpreter interprete) {
		super(interprete);
		setUndecorated(true);
		xVideo=x;
		yVideo=y;
		widthVideo=width;
		heightVideo=height;
		this.setModal(true);

		try {  
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); 
			}catch (Exception e){}
			setLayout(new BorderLayout());
			
			pack();
			setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2), (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2));
			toFront();

			addWindowListener(new WindowAdapter () {
				public void windowClosing (WindowEvent e) {
					running = false;
					try{
						if (audioPlayer != null) {
							audioPlayer.close();
							audioPlayer=null;
							dispose();
							}
						if (moviePanel != null) {
							moviePanel.dispose();
							moviePanel=null;
							dispose();
						}
					}catch (Exception ex){ex.printStackTrace();}
				}
		});
		loadMovie(videoLocation, 0);	
	}

	private void loadMovie(String path, int flags) {
		running = false;
		if (moviePanel != null) {
			/** This cleans up the native structures and removes the display component from
			any container it was added to. It's not essential to do so anymore as
			dsj >= 0.8 can handle multiple graphs in one process space (see showStereoFrame())
			but we're replacing one with the other here. However all DSFiltergraph subclasses
			should finally be closed with the dispose() method.
			**/
			moviePanel.dispose();
			if (audioPlayer != null) {
				audioPlayer.close();
				audioPlayer = null;
			}
		}
		try{
			moviePanel = DSFiltergraph.createDSFiltergraph(path, flags, this);
			moviePanel.addMouseListener(new TVideoListener(this,running, audioPlayer, moviePanel));
			DSFiltergraph.DSAudioStream audioStream = moviePanel.getAudioStream();
			if (audioStream != null) {
				audioPlayer = new JAudioPlayer(audioStream);
			}

		} catch (DSJException e) {
			System.err.println("El video no se puede reproducir");
			System.out.println("\n"+e.toString()+"  "+e.getErrorCode());
			moviePanel = null;
			pack();
			return;
		}

		initDisplay();

		if (audioPlayer != null) audioPlayer.start();
		this.setVisible(true);
	}


	private void initDisplay() {

		try{ 
			add("Center", moviePanel.asComponent()); 
		
		}catch (NullPointerException ne){}

		pack();
		setLocation(xVideo+5,yVideo+54);
		setSize(widthVideo, heightVideo);
		running = true;		
		toFront();
		
		ImageIcon icon = TResourceManager.getImageIcon("transparent.png");
		Image imageCursor = icon.getImage();
		Cursor customCursor = getToolkit().createCustomCursor(imageCursor, new Point(), "MyCursor");
		this.setCursor(customCursor);
		
	}


	public void propertyChange(java.beans.PropertyChangeEvent pe) { 

		switch(DSJUtils.getEventType(pe)) {
			case DSFiltergraph.ACTIVATING: System.out.print("."); break;
			case DSFiltergraph.MOUSE_EVENT:
				System.out.println("Evento de ratón"); break;
			case DSFiltergraph.DONE:
			
				try{
					if (audioPlayer != null) {
						audioPlayer.close();
						audioPlayer=null;
						dispose();
						}
					if (moviePanel != null) {
						moviePanel.dispose();
						moviePanel=null;
						dispose();
					}
				}catch (Exception ex){ex.printStackTrace();}
				
				break;
		}
	}
	
	/**
	Java audio player
	================================================================================================
	Standard javasound code that will read from the filtergraph's DSAudioStream
	**/

	public class JAudioPlayer extends Thread {
		private AudioInputStream audioInputStrem;
		private AudioFormat format;

		private SourceDataLine sourceDataLine;

		private int bufferSize;

		private DataLine.Info info;

		private JAudioPlayer(DSFiltergraph.DSAudioStream dsAudio) {

			try{
				format = dsAudio.getFormat();
				bufferSize = dsAudio.getBufferSize();
				audioInputStrem = new AudioInputStream(dsAudio, format, -1);
				info = new DataLine.Info(SourceDataLine.class, format);
				if (!AudioSystem.isLineSupported(info)) {
					System.out.println("Line matching " + info + " not supported.");
					return;
				}
			}catch (Exception e){}
		}

		public void run() {
			try {
				sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
				sourceDataLine.open(format, bufferSize);
				sourceDataLine.start();
			} catch (LineUnavailableException ex) {
				System.out.println("Unable to open the line: " + ex);
				return;
			}
			try{
				byte[] data = new byte[bufferSize];
				int numBytesRead = 0;
				int written = 0;
				while (running) {
					try {
						if ((numBytesRead = audioInputStrem.read(data)) == -1) break;
						int numBytesRemaining = numBytesRead;
						while (numBytesRemaining > 0 ) {
							written = sourceDataLine.write(data, 0, numBytesRemaining);
							numBytesRemaining -= written;
						}
					} catch (ArrayIndexOutOfBoundsException ae) {
						/**
						Some capture devices eventually deliver larger buffers than they originally say they would.
						Catch that and reset the data buffer
						**/
						bufferSize = numBytesRead;
						data = new byte[bufferSize];
					} catch (Exception e) {e.printStackTrace();
						System.out.println("Error during playback: " + e);
						break;
					}

				}
				sourceDataLine.stop();
				sourceDataLine.flush();
				sourceDataLine.close();

			}catch (Exception e){}
		}

		public void close() {
			try{
				running = false;
				/**
				Give DirectSound based Javasound some time to shut down. Otherwise
				parallel shutdown of dsj's native structures tends to crash.
				**/
				sleep(500);
				audioInputStrem.close();
			}catch (Exception e){}
		}
	}
}