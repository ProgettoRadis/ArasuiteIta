package araword.tts.strategy;

import java.io.IOException;

/**
 * Created by Davide Monfrecola on 17/04/15.
 */
public class AppleTtsStrategy implements TtsStrategy {

    // TODO get voice name from XML configuration file
    private String voiceName = "Alice";

    @Override
    public void play(final String text) {

        class CommanderTTS implements Runnable {

            String textToSpeech;

            CommanderTTS(String t) { this.textToSpeech = t; }

            public void run() {
                try {
                    StringBuilder command = new StringBuilder("say -v ");
                    command.append(voiceName);
                    command.append(" ");
                    command.append(this.textToSpeech);

                    Runtime.getRuntime().exec(command.toString());
                } catch (IOException e) {
                    // TODO log error message
                    System.out.println(e.getMessage());
                }
            }
        }

        Thread t = new Thread(new CommanderTTS(text));
        t.start();
    }

    @Override
    public void setCurrentVoice(String voiceName) {
        this.voiceName = voiceName;
    }

}
