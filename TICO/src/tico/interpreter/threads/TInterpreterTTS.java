/*
 * File: TInterpreterTTS.java
 * 		This file is part of Tico, an application
 * 		to create and perform interactive communication boards to be
 * 		used by people with severe motor disabilities.
 * 
 * Authors: Rodrigo Perez Fulloni
 * 
 * Date: Aug 15, 2012
 *  
 * Company: Fundación Teletón Uruguay
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

import javax.speech.*;
import javax.speech.synthesis.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TInterpreterTTS implements Runnable {

    private HashMap<String, Synthesizer> synths = new HashMap<String, Synthesizer>();
    private String voiceName, text;

    /**
     * Crea un runnable para sintetizar voz
     * @param voiceName el nombre de la voz, debe estar instalada en el sistema
     * @param voiceText el texto a sintetizar
     */
    private TInterpreterTTS() {
        
    }
    private static TInterpreterTTS instance = null;

    public static TInterpreterTTS getInstance() {
        if (instance == null) {
            instance = new TInterpreterTTS();
        }

        return instance;
    }

    public void addVoiceByName(String voice) {
        EngineList list = Central.availableSynthesizers(null);
        SynthesizerModeDesc desc = null;

        Voice v = null;
        SynthesizerModeDesc d = null;

        Boolean found = false;

        for (int i = 0; i < list.size() && !found; i++) {
            desc = (SynthesizerModeDesc) list.elementAt(i);
            Voice[] voices = desc.getVoices();

            for (int j = 0; j < voices.length && !found; j++) {
                if (voices[j].getName().indexOf(voice) > -1) {
                    v = voices[j];
                    d = desc;
                    found = true;
                }
            }
        }

        try {
            Synthesizer synth = Central.createSynthesizer(d);
            synth.allocate();
            synth.resume();
            synth.waitEngineState(Synthesizer.ALLOCATED);
            SynthesizerProperties props = synth.getSynthesizerProperties();
            props.setVoice(v);
            
            synths.put(voice, synth);
        } catch (Exception e) {
            
        }
    }
    
    public void setCurrentVoiceAndText(String voiceName, String text){
        this.voiceName = voiceName;
        this.text = text;
    }


    @Override
    public void run() {
        Synthesizer synth = synths.get(voiceName);

        try {
            synth.speak("", null);
            synth.waitEngineState(synth.QUEUE_EMPTY);
            synth.speak(text, null);
            synth.waitEngineState(synth.QUEUE_EMPTY);
        } catch (Exception ex) {
            Logger.getLogger(TInterpreterTTS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void unload(){
        for(Synthesizer synth : synths.values()){
            try{
                synth.deallocate();
                synth.waitEngineState(synth.DEALLOCATED);
            }catch (Exception ex){
                Logger.getLogger(TInterpreterTTS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
