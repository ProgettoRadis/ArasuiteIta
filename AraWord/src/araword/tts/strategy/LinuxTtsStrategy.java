package araword.tts.strategy;

import java.io.DataOutputStream;
import java.io.IOException;

import araword.G;

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
					DataOutputStream dos = new DataOutputStream(p.getOutputStream());
					dos.writeBytes(command+((char)10)+((char)13));
					dos.flush();
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
		if (G.defaultDocumentLanguage.equals("Castellano")) code="es";
		if (G.defaultDocumentLanguage.equals("Ingles")) code="es";
		if (G.defaultDocumentLanguage.equals("Catalan")) code="es";
		if (G.defaultDocumentLanguage.equals("Euskera")) code="es";
		if (G.defaultDocumentLanguage.equals("Gallego")) code="es";
		if (G.defaultDocumentLanguage.equals("Aleman")) code="de";
		if (G.defaultDocumentLanguage.equals("Frances")) code="fr";
		if (G.defaultDocumentLanguage.equals("Italiano")) code="it-IT";
		if (G.defaultDocumentLanguage.equals("Portugues")) code="pt";
		if (G.defaultDocumentLanguage.equals("Portugues Brasis")) code="pt";

		return code;
	}

}
