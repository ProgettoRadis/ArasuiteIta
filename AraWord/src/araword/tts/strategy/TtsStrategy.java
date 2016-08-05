package araword.tts.strategy;

/**
 * @author Davide Monfrecola
 * @author Maurizio Mazzotta
 * @author Sebastiano Pera
 * @author Luca Quaglia
 */
public interface TtsStrategy {

	void play(String text, int rate);
	
	void setCurrentVoice();
}
