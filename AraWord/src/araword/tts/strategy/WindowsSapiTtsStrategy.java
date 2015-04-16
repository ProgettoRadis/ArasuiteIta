package araword.tts.strategy;

import java.io.IOException;

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
					Runtime.getRuntime().exec(".\\bin\\tts.exe \"" + this.text + "\"");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		Thread t = new Thread(new SapiReader(text));
		t.start();
	}

}
