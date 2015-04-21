package araword.tts.strategy;

import java.io.IOException;

/**
 * Created by Davide Monfrecola on 17/04/15.
 */
public class AppleTtsStrategy implements TtsStrategy {

    // TODO get voice name from XML configuration file
    private String voiceName;

    @Override
    public void play(String text) {
        try {
            System.out.println("Apple TTS called");
            Runtime.getRuntime().exec("say " + text);
        } catch (IOException e) {
            // TODO log error message
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void setCurrentVoice(String voiceName) {
        this.voiceName = voiceName;
    }

    private void executeScript() {

    }
}
