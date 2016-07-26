package araword.tts.strategy;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import araword.G;

/**
 * @author Maurizio Mazzotta
 * @author Sebastiano Pera
 */
public class LinuxTtsStrategy implements TtsStrategy {

	private String voiceName;
	
	@Override
	public void play(String textToSpeech) {
		class CommanderTTS implements Runnable {

			String textToSpeech;
			CommanderTTS(String s) { textToSpeech = s; }

			public void run() {
				String texto = textToSpeech.toLowerCase();
				String command = "pico2wave -l \"" + voiceName + "\" -w arawordttslinux.wav \"" + texto + "\" && aplay arawordttslinux.wav";
				
				System.out.println(command);
				
				Runtime r;
				Process p;
				r = Runtime.getRuntime();
				
				try {
					p = r.exec("/bin/bash");
					
					BufferedWriter bw = new BufferedWriter(
							  new OutputStreamWriter(p.getOutputStream(), "UTF-8"));
							bw.write(command+((char)10)+((char)13));
							bw.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		Thread t = new Thread(new CommanderTTS(textToSpeech));
		t.start();
	}
	
	@Override
	public void setCurrentVoice() {
		this.voiceName = translateLanguage();
	}

	private String translateLanguage() {
		String code="es";
		if (G.defaultDocumentLanguage.equals("Castellano")) code="es-ES";
		if (G.defaultDocumentLanguage.equals("Ingles")) code="en-GB";
		if (G.defaultDocumentLanguage.equals("Catalan")) code="es-ES";
		if (G.defaultDocumentLanguage.equals("Euskera")) code="es-ES";
		if (G.defaultDocumentLanguage.equals("Gallego")) code="es-ES";
		if (G.defaultDocumentLanguage.equals("Aleman")) code="de-DE";
		if (G.defaultDocumentLanguage.equals("Frances")) code="fr-FR";
		if (G.defaultDocumentLanguage.equals("Italiano")) code="it-IT";
		if (G.defaultDocumentLanguage.equals("Portugues")) code="pt-PT";
		if (G.defaultDocumentLanguage.equals("Portugues Brasis")) code="pt-BR";

		return code;
	}

	@Override
	public void setSpeechRate(final String speechRate) {
		// TODO to be implemented
		throw new UnsupportedOperationException();
	}

}
