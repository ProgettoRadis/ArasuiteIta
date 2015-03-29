/*
 * File: TInterpreterReadAction.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * * Authors: Antonio Rodríguez
 * 
 * Date:	May-2006 
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

package tico.interpreter.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import tico.board.TBoardConstants;
import tico.components.resources.TFileUtils;
import tico.configuration.TLanguage;
import tico.configuration.TSetup;
import tico.interpreter.TInterpreter;
import tico.interpreter.components.TInterpreterAccumulatedCell;
import tico.interpreter.threads.TInterpreterMidSound;
import tico.interpreter.threads.TInterpreterMp3Sound;
import tico.interpreter.threads.TInterpreterWavSound;
import tico.interpreter.threads.TInterpreterGoogleTTS;
import tico.interpreter.threads.TInterpreterTTS;

/**
 * 
 * @author Antonio Rodríguez
 *
 */

public class TInterpreterReadAction extends TInterpreterAbstractAction
{
	TInterpreterWavSound nAudio=null;
	TInterpreterMp3Sound nMp3Audio=null;
	
	TInterpreterMidSound nMidAudio=null;


	public TInterpreterReadAction(TInterpreter interpreter) {
		super(interpreter, TLanguage.getString("TInterpreterRead.NAME"));		
	}
	
	public void actionPerformed(ActionEvent e) {
	    
	    if (!System.getProperty("os.name").startsWith("Windows")) {
	        return;
	    }

		interpreter = getInterpreter();
			
			if (nMp3Audio!=null){					
				nMp3Audio.TJoin();
			}	
			if (nAudio!=null){					
				try {
				 nAudio.join();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			
			if (nMidAudio!=null){					
				try {
				 nMidAudio.join();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			
			int index=0;
                        //por cada celda acumulada reproduzco los mp3 y sintetizo la voz si tiene
			String accumulatedVoiceText = ""; //to be send to the voice synthesizer
			
			for (index=0;index < TInterpreter.accumulatedCellsList.size();index++)
			{			
                TInterpreterAccumulatedCell ac = (TInterpreterAccumulatedCell) (TInterpreter.accumulatedCellsList.get(index));
				String nameSound = ac.getSound();
				
				if (nameSound!=null)
				{
					String extension = TFileUtils.getExtension(nameSound);
					
					if (extension.equals("mp3"))
					{ nMp3Audio=new TInterpreterMp3Sound(nameSound);
					  nMp3Audio.TPlay();
					  nMp3Audio.TJoin();
					}
					else if(extension.equals("mid") || extension.equals("midi")) 
					{ nMidAudio=new TInterpreterMidSound(nameSound);
					  nMidAudio.start();
					  try {
						nMidAudio.join();
					  } catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						  e1.printStackTrace();
					  }
					}
					else 
					{ nAudio=new TInterpreterWavSound(nameSound);
					  nAudio.start();
					  try {
						nAudio.join();
					  } catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					  }
					}
				}                                
                
				String voiceText = ac.getVoiceText();
                int mode = ac.getSynthMode();
                                                               
                if(voiceText != null){
                	accumulatedVoiceText = accumulatedVoiceText + voiceText + " ";                                        
                }
			}
				//accumulated text is not empty, and must be synthesized
				if(!accumulatedVoiceText.equals("")){
								//take some parameters from the first accumulated cell
								//and apply them for the rest of cells
					TInterpreterAccumulatedCell ac = (TInterpreterAccumulatedCell) (TInterpreter.accumulatedCellsList.get(0));
	                int mode = ac.getSynthMode();
	                
	                switch(mode){
	                	case TBoardConstants.SAPI_MODE:                                    
	                		String voiceName = ac.getVoiceName();
	                		if(voiceName == null){
	                			voiceName = interpreter.getIntepreterProject().getDefaultVoiceName();
	                		}
	                		TInterpreterTTS tts = TInterpreterTTS.getInstance();
	                		tts.setCurrentVoiceAndText(voiceName, accumulatedVoiceText);
	                		tts.run();
	                		break;
		                case TBoardConstants.GOOGLE_MODE:
		                	String voice = TSetup.getLanguageCode();
		                	TInterpreterGoogleTTS ttsg = new TInterpreterGoogleTTS();
		                    ttsg.setCurrentVoiceAndText(voice, accumulatedVoiceText);          
		                    ttsg.run();
		                    break;
	            }
				
			}
                       
                       if (index == 0) { //no habia ninguna acumulada => leo las textareas
                    	  
                            String aLeer = "";
                            for (Component c : TInterpreter.interpretArea.getComponents()) {
                                if (c instanceof JLabel) {
                                    aLeer += " " + ((JLabel) c).getText();
                                }
                            }
                            
                            String voiceName = interpreter.getIntepreterProject().getDefaultVoiceName();
                            if(voiceName != null){
                            	TInterpreterTTS tts = TInterpreterTTS.getInstance();
                            	tts.setCurrentVoiceAndText(voiceName, aLeer);
                            	tts.run();
                            }
                        }
	}
		
}

