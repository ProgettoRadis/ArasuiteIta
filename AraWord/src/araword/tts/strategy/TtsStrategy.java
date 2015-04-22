package araword.tts.strategy;

import java.io.IOException;

public interface TtsStrategy {

	void play(String text);

	void setCurrentVoice(String voiceName);

}
