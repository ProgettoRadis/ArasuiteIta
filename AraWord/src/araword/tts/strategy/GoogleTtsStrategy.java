package araword.tts.strategy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import araword.G;
import araword.utils.TInterpreterGoogleTTS;
import araword.utils.TInterpreterMp3Sound;

public class GoogleTtsStrategy implements TtsStrategy {

	private String voiceName;
	//private String text;

	@Override
	public void setCurrentVoice(String voiceName) {
		this.voiceName = translateLanguage();
		//this.text = text;
	}

	private String translateLanguage() {
		String code="ES_ES";
		if (G.defaultDocumentLanguage.equals("Castellano")) code="ES_ES";
		if (G.defaultDocumentLanguage.equals("Ingles")) code="EN";
		if (G.defaultDocumentLanguage.equals("Catalan")) code="ES";
		if (G.defaultDocumentLanguage.equals("Euskera")) code="ES";
		if (G.defaultDocumentLanguage.equals("Gallego")) code="ES";
		if (G.defaultDocumentLanguage.equals("Aleman")) code="DE";
		if (G.defaultDocumentLanguage.equals("Frances")) code="FR";
		if (G.defaultDocumentLanguage.equals("Italiano")) code="IT";
		if (G.defaultDocumentLanguage.equals("Portugues")) code="PT";
		if (G.defaultDocumentLanguage.equals("Portugues Brasis")) code="PT_BR";

		return code;
	}


	public void play(String textToSpeech) {

		class CommanderTTS implements Runnable {

			String textToSpeech;
			CommanderTTS(String s) { textToSpeech = s; }

			public void run() {
				String texto = textToSpeech.toLowerCase();
				ArrayList<String> TTSList = new ArrayList<String>();

				while (texto.length()>100) {
					//split in 100 chars max

					int index=99;

					while (!((texto.charAt(index)==' ')
							|| (texto.charAt(index)=='.')
							|| (texto.charAt(index)==',')
							|| (texto.charAt(index)==';')
							|| (texto.charAt(index)==':'))) {

						index--;

					}

					String cacho = texto.substring(0, index);

					TTSList.add(cacho);
					texto=texto.substring(index, texto.length());
				}
				TTSList.add(texto);

				for (String TTSPiece: TTSList) {
					//generate file name

					File outputFile = new File( "sintesis_tmp" + ".mp3");

					//       if (!outputFile.exists()) { //to avoid re-synthesizing when the work was already done
					//removed to avoid "old" synthesized texts: generate speech every time
					try {
						//get document language
						//System.out.println("LANG="+ G.defaultDocumentLanguage);

						//System.out.println("TEXT="+ TTSPiece);
						String text = URLEncoder.encode(TTSPiece, "utf-8");
						String strUrl = "http://translate.google.com/translate_tts" + "?" +
								"tl=" + voiceName + "&q=" + text;
						URL url = new URL(strUrl);
						// Establish connection
						HttpURLConnection connection = (HttpURLConnection) url.openConnection();
						// Get method
						connection.setRequestMethod("GET");
						// Set User-Agent to "mimic" the behavior of a web browser. In this
						// example, I used my browser's info
						connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:11.0) " +
								"Gecko/20100101 Firefox/11.0");
						connection.connect();



						InputStream is = connection.getInputStream();

						FileOutputStream fos = new FileOutputStream(outputFile);
						byte[] buffer = new byte[1024];

						while (is.read(buffer) > -1) {
							fos.write(buffer);
						}

						fos.close();
					} catch (Exception ex) {
						Logger.getLogger(TInterpreterGoogleTTS.class.getName()).log(Level.SEVERE, null, ex);
					}
					//       }

					TInterpreterMp3Sound tims = new TInterpreterMp3Sound(outputFile.toString());

					outputFile.delete(); //to avoid lots of file in the synth-cache directory
					tims.TPlay();
					while (!tims.TIsFinished()) {
						//wait to finish
					}
				} // of for elements in TTSList
			}
		}

		Thread t = new Thread(new CommanderTTS(textToSpeech));
		t.start();
	}

}
