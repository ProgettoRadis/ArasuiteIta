/*
 * File: TIntepreterMidSound.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Eduardo Ferrer
 * 
 * Date: Aug, 2012
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
import java.io.IOException;
import java.net.MalformedURLException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.text.Position;

/**
 * The audio thread (Midi format)
 * 
 * @author Eduardo Ferrer
 * @version 1.0 Aug, 2012
 */


public class TInterpreterMidSound extends Thread {

	private String filename;
	static private Sequencer sequencer;
	static private Sequence sequence;

//	private final int EXTERNAL_BUFFER_SIZE = 524288; // 128Kb

	public TInterpreterMidSound(String midfile) {
		filename = midfile;
	}

	public void run() {

		
		File soundFile = new File(filename);
		if (!soundFile.exists()) {
			System.err.println("Midi file not found: " + filename);
			return;
		}
		
		

//		AudioInputStream audioInputStream = null;
		try {
			/*Sequence*/ sequence = MidiSystem.getSequence(soundFile);
			
			/* Sequencer*/ sequencer = MidiSystem.getSequencer();
		     sequencer.open();
		     sequencer.setSequence(sequence);
		     
		     sequencer.start();
			
		     sleep((long) Math.ceil(sequence.getMicrosecondLength()/1000));
		     sequencer.close();
		     //audioInputStream = AudioSystem.getAudioInputStream(soundFile);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		return;
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		} catch (MidiUnavailableException e1) {
			e1.printStackTrace();
			return;
		} catch (InvalidMidiDataException e1) {
			e1.printStackTrace();
			return;
		}  catch (InterruptedException e1) {
			e1.printStackTrace();
			return;
		}
		

	}
	
	
	public void TStop(){
		if(sequencer.isRunning())
			sequencer.stop();
		super.stop();
		
	}
}
