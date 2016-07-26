package araword.tts.strategy;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;

/**
 * @author Davide Monfrecola
 */
public class AppleTtsStrategy implements TtsStrategy {

    private String voiceName = "Alice";

    private String speechRate = "200";

    @Override
    public void play(final String text) {

        class CommanderTTS implements Runnable {

            private String textToSpeech;

            private String voiceName;

            private String speechRate;


            CommanderTTS(final String text, final String voiceName, final String speechRate) {
                this.textToSpeech = text;
                this.voiceName = voiceName;
                this.speechRate = speechRate;
            }

            public void run() {
                try {
                    StringBuilder command = new StringBuilder("say -v ");
                    command.append(voiceName);
                    command.append(" \"");
                    command.append(this.textToSpeech);
                    command.append("\" -r ");
                    command.append(speechRate);

                    Runtime.getRuntime().exec(command.toString());
                } catch (IOException e) {
                    // TODO log error message
                    System.out.println(e.getMessage());
                }
            }
        }

        Thread t = new Thread(new CommanderTTS(text, voiceName, speechRate));
        t.start();
    }

    @Override
    public void setCurrentVoice() {
        this.voiceName = "Alice";
    }

    @Override
    public void setSpeechRate(final String speechRate) {
        this.speechRate = speechRate;
    }

}
