/*
 * File: TVideoListener.java
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

package tico.interpreter.listeners;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;

import tico.components.resources.TResourceManager;
import tico.interpreter.TInterpreterConstants;
import tico.interpreter.threads.TInterpreterVideo;
import tico.interpreter.threads.TInterpreterVideo.JAudioPlayer;
import de.humatic.dsj.DSFiltergraph;

public class TVideoListener implements MouseListener {
	
	private TInterpreterVideo videoWindowListener;
	Boolean running;
	private DSFiltergraph moviePanel;
	private JAudioPlayer audioPlayer;
	
	public TVideoListener(TInterpreterVideo videoWindow, Boolean runningVideo, JAudioPlayer audioPlayerVideo, DSFiltergraph moviePanelVideo){
		videoWindowListener = videoWindow;
		running = runningVideo;
		audioPlayer = audioPlayerVideo;
		moviePanel = moviePanelVideo;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		ImageIcon icon = TResourceManager.getImageIcon("transparent.png");
		Image imageCursor = icon.getImage();
		Cursor customCursor = videoWindowListener.getToolkit().createCustomCursor(imageCursor, new Point(), "MyCursor");
		videoWindowListener.setCursor(customCursor);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		ImageIcon icon = null;
		if (TInterpreterConstants.interpreterCursor==null)
			icon = TResourceManager.getImageIcon("flecha2.png");
		else{
			icon = new ImageIcon(TInterpreterConstants.interpreterCursor);
		}
		Image imageCursor = icon.getImage();
		Cursor customCursor = videoWindowListener.getToolkit().createCustomCursor(imageCursor, new Point(), "MyCursor");
		videoWindowListener.setCursor(customCursor);
	}

	@Override
	public void mousePressed(MouseEvent e) {
	
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		running = false;
		try{
			if (audioPlayer != null) {
				audioPlayer.close();
				audioPlayer=null;
				videoWindowListener.dispose();
				}
			if (moviePanel != null) {
				moviePanel.dispose();
				moviePanel=null;
				videoWindowListener.dispose();
			}
		}catch (Exception ex){ex.printStackTrace();}
	}

}
