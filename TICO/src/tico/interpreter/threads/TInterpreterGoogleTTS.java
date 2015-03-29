/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tico.interpreter.threads;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import tico.interpreter.TInterpreterConstants;
import tico.interpreter.TInterpreterProject;

/**
 *
 * @author rodripf
 */
public class TInterpreterGoogleTTS implements Runnable {

    private String voiceName;
    private String text;

    public void setCurrentVoiceAndText(String voiceName, String text) {
        this.voiceName = voiceName;
        this.text = text;
    }

    @Override
    public void run() {
        String texto = text.replace(" ", "+").toLowerCase();
        
        File outputFile = new File("synth-cache/" + texto + ".mp3");

 //       if (!outputFile.exists()) { //to avoid re-synthesizing when the work was already done
        							  //removed to avoid "old" synthesized texts: generate speech every time
            try {
                URL website = new URL("http://translate.google.com/translate_tts?tl=" + voiceName + "&q=" + texto);
                HttpURLConnection con = (HttpURLConnection) website.openConnection();
                con.addRequestProperty("User-Agent", "Mozilla/4.76");

                InputStream is = con.getInputStream();
                
                FileOutputStream fos = new FileOutputStream(outputFile);
                byte[] buffer = new byte[1024];

                while (is.read(buffer) > -1) {
                    fos.write(buffer);
                }

                fos.close();
            } catch (Exception ex) {
                Logger.getLogger(TInterpreterGoogleTTS.class.getName()).log(Level.SEVERE, null, ex);
            }
 //       }
        
        TInterpreterMp3Sound tims = new TInterpreterMp3Sound(outputFile.toString());
                
        outputFile.delete(); //to avoid lots of file in the synth-cache directory
        tims.TPlay();
    }

    public static void main(String[] args) {
        TInterpreterGoogleTTS tg = new TInterpreterGoogleTTS();
        tg.setCurrentVoiceAndText("es", "Esto es una prueba");
        tg.run();
    }
}
