package araword.tts.strategy;


public interface TtsStrategy {

	void play(String text);

	void setCurrentVoice();

}
