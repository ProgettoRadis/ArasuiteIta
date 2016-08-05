package araword.tts.strategy;

import java.io.IOException;

/**
 * @author Maurizio Mazzotta
 * @author Sebastiano Pera
 */
public class WindowsSapiTtsStrategy implements TtsStrategy {

	
    @Override
    public void play(String text, int rate) {
    	//Windows speech rate goes from -10 to +10, 
    	//so the speech rate value has to be normalized between this values.
    	final int ttsRateResult;
    	
    	ttsRateResult = Math.round((((float)rate/100) * 20) - 10);

        class SapiReader implements Runnable {
            String text;

            SapiReader(String s) {
                this.text = s;
            }
            
            public void run() {
                try {
                    Runtime.getRuntime().exec(".\\resources\\tts\\tts.exe \"" + this.text + "\" \"" + Integer.toString(ttsRateResult) + "\"" );
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        Thread t = new Thread(new SapiReader(text));
        t.start();
    }
    
    
    
    @Override
    public void setCurrentVoice() {}



}
