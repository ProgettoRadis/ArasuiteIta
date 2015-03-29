/*
 * File: TIntepreterBoardSound.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Carolina Palacio
 * 
 * Date: Dec, 2009
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

import java.io.File;

import tico.components.resources.TFileUtils;
import tico.editor.TFileHandler;
import tico.interpreter.TInterpreterConstants;

/**
 * The audio thread for a board sound
 * 
 * @author Carolina Palacio
 * @version 1.0 Dec, 2009
 */
public class TInterpreterBoardSound implements Runnable {

	private String filePath;

	public TInterpreterBoardSound(String soundFile) {
		filePath = soundFile;
	}

	public void run() { 
		
		// Catches the semaphore
		try {
			TInterpreterConstants.semaforo.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		String extension= TFileUtils.getExtension(filePath);
		if (extension.equals("mp3")){
			TInterpreterConstants.audioMp3 = new TInterpreterMp3Sound(TFileHandler.getCurrentDirectoryPath()+File.separator+filePath);
			TInterpreterConstants.audioMp3.TPlay();			 
			TInterpreterConstants.audioMp3.TJoin();
		}
		
		else if(extension.equals("mid") || extension.equals("midi")){
			TInterpreterConstants.audioMid=new TInterpreterMidSound(TFileHandler.getCurrentDirectoryPath()+File.separator+filePath);	
			TInterpreterConstants.audioMid.start();
			try {
				TInterpreterConstants.audioMid.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.err.println("Error al reproducir el sonido del tablero");
				}
		}
		
		else {
			TInterpreterConstants.audio=new TInterpreterWavSound(TFileHandler.getCurrentDirectoryPath()+File.separator+filePath);	
			TInterpreterConstants.audio.start();
			try {
				TInterpreterConstants.audio.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.err.println("Error al reproducir el sonido del tablero");
				}
		}
		
		//Release the semaphore
		try {
			TInterpreterConstants.semaforo.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
