package araword.tts.strategy;

/**
 * Created by Davide Monfrecola on 17/04/15.
 */
public class AppleTtsStrategy implements TtsStrategy {

    // TODO get voice name from XML configuration file
    private String voiceName;

    @Override
    public void play(String text) {
        System.out.println("Apple TTS called");
    }

    @Override
    public void setCurrentVoice(String voiceName) {
        this.voiceName = voiceName;
    }
}
