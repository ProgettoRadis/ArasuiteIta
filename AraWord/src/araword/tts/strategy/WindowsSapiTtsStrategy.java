package araword.tts.strategy;

import java.io.IOException;

/**
 * @author Maurizio Mazzotta
 * @author Sebastiano Pera
 */
public class WindowsSapiTtsStrategy implements TtsStrategy {

    @Override
    public void play(String text) {

        class SapiReader implements Runnable {
            String text;

            SapiReader(String s) {
                this.text = s;
            }

            public void run() {
                try {
                    Runtime.getRuntime().exec(".\\resources\\tts\\tts.exe \"" + this.text + "\"");
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

    @Override
    public void setSpeechRate(final String speechRate) {
        // TODO to be implemented
        throw new UnsupportedOperationException();
    }

}
