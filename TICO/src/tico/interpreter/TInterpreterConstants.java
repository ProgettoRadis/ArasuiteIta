/*
 * File: TInterpreterConstants.java
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

package tico.interpreter;

import java.awt.Color;
import java.util.ArrayList;

import tico.interpreter.threads.TInterpreterMp3Sound;
import tico.interpreter.threads.TInterpreterTTS;
import tico.interpreter.threads.TInterpreterWavSound;

import tico.interpreter.threads.TInterpreterMidSound;

/**
 * 
 * @author Carolina Palacio
 *
 */
public class TInterpreterConstants {
	
	public static TInterpreter interpreter;
		
	public final static Color BACKGROUND_COLOR = Color.WHITE;
	
	public final static Color BORDER_COLOR = Color.BLACK;
	
	public final static Color SELECTED_BORDER_COLOR = Color.RED;
	
	public final static int BORDER_SIZE = 1;
	
	public final static int SELECTED_BORDER_SIZE = 4;
	
	public final static String OS_WINDOWS = "windows";
	
	public final static String OS_LINUX = "linux";
	
	//OS
	
	public static String operatingSystem=""; 
	
	//Mouse modes
	
	public final static String AUTOMATIC_SCANNING_MODE = "automaticScanningMode";
	
	public final static String DIRECT_SELECTION_MODE = "directSelectionMode";
	
	public final static String MANUAL_SCANNING_MODE = "manualScanningMode";

	//Mouse mode selected
	
	public static String mouseModeSelected = TInterpreterConstants.DIRECT_SELECTION_MODE;
	
	//Semáforo que controla la sincronización entre el barrido y las acciones de las celdas
	
	public static TSemaphore semaforo = new TSemaphore(1);
	
	public static TInterpreterMp3Sound audioMp3 = null;
	
	
	public static TInterpreterMidSound audioMid = null;
	
	
	public static TInterpreterWavSound audio = null;
        
        public static TInterpreterTTS tts = null;
        
        public static tico.interpreter.components.TInterpreterCell audioOrigin = null;
	//Interpreter configuration
		
	public static String interpreterCursor = null;
	
	public static int interpreterDelay = 1000;
	
	public static int accumulatedCells = 5;
	
	public static int maxAccumulatedCells = 30;
	
	public static TInterpreterBoard currentBoard;
	
	public static int countRun = 0;
	
	public static ArrayList<String> boardOrderedCells;
	
	public static int x,y;
		
}
