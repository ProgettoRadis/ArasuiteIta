/*
 * File: TCellListener.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Isabel Gonz�lez y Carolina Palacio
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

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import tico.board.TBoardConstants;
import tico.components.resources.TFileUtils;
import tico.interpreter.TInterpreter;
import tico.interpreter.TInterpreterConstants;
import tico.interpreter.components.TInterpreterAccumulatedCell;
import tico.interpreter.components.TInterpreterCell;
import tico.interpreter.components.TInterpreterTextArea;
import tico.interpreter.threads.TInterpreterGoogleTTS;
import tico.interpreter.threads.TInterpreterMidSound;
import tico.interpreter.threads.TInterpreterMp3Sound;
import tico.interpreter.threads.TInterpreterTTS;
import tico.interpreter.threads.TInterpreterVideo;
import tico.interpreter.threads.TInterpreterWavSound;

public class TCellListener implements MouseListener {

    TInterpreterCell cell;
    TInterpreter interpreter;

    public TCellListener(TInterpreterCell c) {
        cell = c;
        interpreter = TInterpreterConstants.interpreter;
    }

    @Override
    public void mousePressed(MouseEvent arg0) {

        if ((TInterpreter.run == 1)) {

            if (arg0.getButton() == MouseEvent.BUTTON3) { //Right button
                if (TInterpreter.returnMouseMode().equals(TInterpreterConstants.MANUAL_SCANNING_MODE)) {
                    TInterpreter.boardListener.click();
                }
            } else {

                TInterpreterCell cell = (TInterpreterCell) arg0.getSource();

                if (TInterpreter.returnMouseMode().equals(TInterpreterConstants.AUTOMATIC_SCANNING_MODE)) { // barrido automatico

                    try {
                        TInterpreterConstants.semaforo.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                if (cell.isAccumulated()) {

                    if (TInterpreter.accumulatedCellsList.size() < TInterpreterConstants.accumulatedCells) {
                        TInterpreterAccumulatedCell accumulatedCell = new TInterpreterAccumulatedCell();
                        accumulatedCell.setAttributes(cell);
                        TInterpreter.accumulatedCellsList.add(accumulatedCell);
                        TInterpreter.accumulatedCells.add(accumulatedCell);

                        TInterpreter.accumulatedCells.updateUI();
                    }
                }

                //si se esta reproduciendo mp3 detengo y borro referncias previas
                if (TInterpreterConstants.audioMp3 != null && !TInterpreterConstants.audioMp3.TIsFinished()) {
                    TInterpreterConstants.audioMp3.TStop();
                    TInterpreterConstants.audioMp3 = null;

                    //si se esta reproduciendo wav detengo y borro referncias previas
                } else if (TInterpreterConstants.audio != null && !TInterpreterConstants.audio.TIsFinished()) {
                    TInterpreterConstants.audio.TStop();
                    TInterpreterConstants.audio = null;
                }

                if ((TInterpreterConstants.audio != null && TInterpreterConstants.audio.TIsFinished())
                        || (TInterpreterConstants.audioMp3 != null && TInterpreterConstants.audioMp3.TIsFinished())) {
                    TInterpreterConstants.audioOrigin = null;
                    TInterpreterConstants.audio = null;
                    TInterpreterConstants.audioMp3 = null;
                }



                //si el origen del audio no es esta celda, reproduzco
                if (TInterpreterConstants.audioOrigin != this.cell) {

                    if (cell.getSoundPath() != null) {
                        String extension = TFileUtils.getExtension(cell.getSoundPath());
                        if (extension.equals("mp3")) {
                            if (TInterpreterConstants.audioMp3 == null || TInterpreterConstants.audioMp3.TIsFinished()) {
                                TInterpreterConstants.audioMp3 = new TInterpreterMp3Sound(cell.getSoundPath());
                                TInterpreterConstants.audioMp3.TPlay();
                            }
                        } else if (extension.equals("mid") || extension.equals("midi")) {
                            TInterpreterConstants.audioMid = new TInterpreterMidSound(cell.getSoundPath());
                            TInterpreterConstants.audioMid.start();
                            //TODO: check if eliminating this affects the functionality
					try {
                             TInterpreterConstants.audioMid.join();
                             } catch (InterruptedException e) {
                             e.printStackTrace();
                             System.out.println("Error al reproducir el sonido de la celda");
                             }
                        } else {
                            if (TInterpreterConstants.audio == null || TInterpreterConstants.audio.TIsFinished()) {
                            TInterpreterConstants.audio = new TInterpreterWavSound(cell.getSoundPath());
                            TInterpreterConstants.audio.start();

//                      Por qu� se utiliz� el join? Propongo descartarlo para que el thread de audio no tranque 
//                         el tread principal
                            try {
                                TInterpreterConstants.audio.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                               System.out.println("Error al reproducir el sonido de la celda");
                            }
                            }
                        }
                        TInterpreterConstants.audioOrigin = this.cell;
                    }
                }
                if (TInterpreterConstants.audioMp3 == null && TInterpreterConstants.audio == null) {
                    TInterpreterConstants.audioOrigin = null;
                }

                if (TInterpreterConstants.operatingSystem == TInterpreterConstants.OS_WINDOWS) {

                    if (cell.getVideoPath() != null) {
                        Point point = cell.getLocationOnScreen();
                        cell.setXVideo(point.x - 5);
                        cell.setYVideo(point.y - 54);
                        try {
                            TInterpreterVideo video = new TInterpreterVideo(cell.getVideoPath(), cell.getXVideo(),
                                    cell.getYVideo(), cell.getWidth(), cell.getHeight(), interpreter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (cell.getVideoURL() != null) {
                        Point point = cell.getLocationOnScreen();
                        cell.setXVideo(point.x - 5);
                        cell.setYVideo(point.y - 54);
                        try {
                            TInterpreterVideo video = new TInterpreterVideo(cell.getVideoURL(), cell.getXVideo(),
                                    cell.getYVideo(), cell.getWidth(), cell.getHeight(), interpreter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (cell.getTextAreaToSend() != null) {
                    TInterpreterTextArea textArea = TInterpreter.getCurrentBoard().getTextAreaByName(cell.getTextAreaToSend());

                    if (textArea != null) {
                        String originalText = textArea.getText();

                        textArea.setText(originalText + cell.getTextToSend());

                        interpreter.interpretArea.paintImmediately(0, 0, interpreter.interpretArea.getWidth(), interpreter.interpretArea.getHeight());

                        waiting w = new waiting(cell.getTimeSending());
                        w.start();
                        try {
                            w.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        
                        textArea.setText(originalText);
                        
                        TInterpreter.interpretArea.removeAll();
                        //No vuelvo al estado inicial para que la celda que env�a texto permanezca seleccionada
                        TInterpreter.getCurrentBoard().paintBoard(TInterpreter.interpretArea, false);
                        TInterpreter.interpretArea.repaint();
                    }
                }

                if (cell.getBoardToGo() != null) {

                    TInterpreterConstants.currentBoard = interpreter.getProject().getBoard(cell.getBoardToGo());
                    TInterpreterConstants.countRun = 0;
                    TInterpreterConstants.boardOrderedCells = TInterpreterConstants.currentBoard.getOrderedCellListNames();
                    interpreter.getProject().setPositionCellToReturn(0);

                    interpreter.getProject().setBoardToReturn(interpreter.getProject().getCurrentBoard());
                    interpreter.getProject().setCellToReturn((((TInterpreterCell) arg0.getSource()).getName()));

                    interpreter.getProject().getPositionCellToReturnByName(interpreter.getProject().getBoardToReturn(), interpreter.getProject().getCellToReturn());

                    //Repinta el tablero, para que se quede en el estado inicial
                    interpreter.repaintCurrentBoard(false);

                    interpreter.changeBoard(cell.getBoardToGo());
                    interpreter.getProject().setBoardChanged(true);
                    interpreter.getProject().setBoardToGo(cell.getBoardToGo());

                    interpreter.getProject().setCurrentBoard(cell.getBoardToGo());

                    if (TInterpreter.returnMouseMode().equals(TInterpreterConstants.MANUAL_SCANNING_MODE)) {
                        TInterpreter.boardListener.click();
                    }
                }

                if (cell.getCommand() != null) {
                    try {
                        Process p = Runtime.getRuntime().exec(cell.getCommand());
                        try {
                            // Waiting for the end of the call to environment control
                            p.waitFor();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        System.out.println(e.getCause());
                    }
                }
                                    
                if (cell.getVoiceText() != null) {
                	String OS = System.getProperty("os.name");
                	if(cell.getSynthMode() == TBoardConstants.GOOGLE_MODE){ //for every OS
                		String voiceLang = tico.configuration.TSetup.getLanguageCode();
                        TInterpreterGoogleTTS ttsg = new TInterpreterGoogleTTS();
                        ttsg.setCurrentVoiceAndText(voiceLang, cell.getVoiceText());
                        ttsg.run();
                	}else if((cell.getSynthMode() == TBoardConstants.SAPI_MODE) && (OS.startsWith("Windows"))){ //SAPI: only for MS-Windows
                		String voice = cell.getVoiceName();
                        if (voice == null) {
                            voice = interpreter.getIntepreterProject().getDefaultVoiceName();
                        }
                        TInterpreterTTS tts = TInterpreterTTS.getInstance();
                        //tts.addVoiceByName(voice);
                        tts.setCurrentVoiceAndText(voice, cell.getVoiceText());
                        tts.run();
                	}
                }

                if (TInterpreter.returnMouseMode().equals(TInterpreterConstants.AUTOMATIC_SCANNING_MODE)) { // barrido automatico
                    try {
                        TInterpreterConstants.semaforo.release();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {

        if ((TInterpreter.run == 1)) {

            TInterpreterCell cell = (TInterpreterCell) arg0.getSource();
            //Default border attributes
            Color colorLine = Color.red;
            int widthLine = 1;

            if (cell.getAlternativeIcon() != null) {
                cell.setIcon(cell.getAlternativeIcon());
            }

            if (cell.getAlternativeBorderSize() != 0) {
                cell.setBorderPainted(true);
                colorLine = cell.getAlternativeBorderColor();
                widthLine = cell.getAlternativeBorderSize();
            }





            Border thickBorder = new LineBorder(colorLine, widthLine);
            cell.setBorder(thickBorder);



            //if anyadido sonido alternativo suena en modos de barrido
            if (cell.getAlternativeSoundPath() != null && !TInterpreter.returnMouseMode().equals(TInterpreterConstants.DIRECT_SELECTION_MODE)) {
                String extension = TFileUtils.getExtension(cell.getAlternativeSoundPath());
                if (extension.equals("mp3")) {
                    TInterpreterConstants.audioMp3 = new TInterpreterMp3Sound(cell.getAlternativeSoundPath());
                    TInterpreterConstants.audioMp3.TPlay();
                    TInterpreterConstants.audioMp3.TJoin();
                } else if (extension.equals("mid") || extension.equals("midi")) {
                    TInterpreterConstants.audioMid = new TInterpreterMidSound(cell.getAlternativeSoundPath());
                    TInterpreterConstants.audioMid.start();

                } else {
                    TInterpreterConstants.audio = new TInterpreterWavSound(cell.getAlternativeSoundPath());
                    TInterpreterConstants.audio.start();
                    try {
                     TInterpreterConstants.audio.join();
                     } catch (InterruptedException e) {
                     e.printStackTrace();
                     System.out.println("Error al reproducir el sonido de la celda");
                     }
                }
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent arg0) {

        if ((TInterpreter.run == 1)) {

            TInterpreterCell cell = (TInterpreterCell) arg0.getSource();

            // Sets the original icon
            cell.setIcon(cell.getDefaultIcon());


            if (cell.isTransparentBorder()) {
                cell.setBorderPainted(false);
            } else {
                cell.setBorder(new LineBorder(cell.getBorderColor(), (int) cell.getBorderSize()));
            }

            if (cell.isTransparentBackground()) {
                cell.setFocusPainted(false);
                cell.setContentAreaFilled(false);
            } else if (cell.getBackground() != null) {
                cell.setFocusPainted(true);
                cell.setBackground(cell.getBackground());
            }
        }
    }

    public void mouseClicked(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }

    class waiting extends Thread {

        private int secs;

        public waiting(int segundos) {
            secs = segundos;
        }

        public void run() {
            try {
                Thread.sleep(secs * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
