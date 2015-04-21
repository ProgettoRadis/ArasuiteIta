package araword.tts.strategy;

import java.io.IOException;

/**
 * Created by Davide Monfrecola on 17/04/15.
 */
public class AppleTtsStrategy implements TtsStrategy {

    // TODO get voice name from XML configuration file
    private String voiceName = "Alice";

    @Override
    public void play(String text) {
        try {
            StringBuilder command = new StringBuilder("say -v ");
            command.append(voiceName);
            command.append(" ");
            command.append(text);

            Runtime.getRuntime().exec(command.toString());
        } catch (IOException e) {
            // TODO log error message
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void setCurrentVoice(String voiceName) {
        this.voiceName = voiceName;
    }

}