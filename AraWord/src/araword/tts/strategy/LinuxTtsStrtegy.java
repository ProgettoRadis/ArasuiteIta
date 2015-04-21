package araword.tts.strategy;

import java.io.IOException;

import araword.G;

public class LinuxTtsStrtegy implements TtsStrategy {

	private String voiceName;
	//private String text;
	
	@Override
	public void play(String textToSpeech) {
		class CommanderTTS implements Runnable {

			String textToSpeech;
			CommanderTTS(String s) { textToSpeech = s; }

			public void run() {
				String texto = textToSpeech.toLowerCase();
				String command = "espeak -v " + voiceName + " \"" + texto + "\"";
				System.out.println(command);
				Runtime rt = Runtime.getRuntime();
				String[] commands = {"espeak","-v", voiceName, texto};
				try {
					rt.exec(commands);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		Thread t = new Thread(new CommanderTTS(textToSpeech));
		t.start();
	}
	
	@Override
	public void setCurrentVoice(String voiceName) {
		this.voiceName = translateLanguage();
		//this.text = text;
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
		if (G.defaultDocumentLanguage.equals("Italiano")) code="it";
		if (G.defaultDocumentLanguage.equals("Portugues")) code="pt";
		if (G.defaultDocumentLanguage.equals("Portugues Brasis")) code="pt";

		return code;
	}

}
