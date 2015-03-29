package components;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.swing.JProgressBar;

import configuration.TConfiguration;
import configuration.TLanguage;

public class Updater {
	
	private File temp;
	private static final String INFORMATION_FILE_NAME = "info";
	private static String informationFilePath;
	private Properties p;
	private String DBToInstallZip, DBToInstallDestination;

	public static final String KEY_CHANGELOG = "CHANGELOG";
	public static final String KEY_RELEASE_DATE = "RELEASE_DATE";
	public static final String KEY_DOWNLOAD_URL = "DOWNLOAD_URL";
	public static final String KEY_MD5 = "MD5";

	private JProgressBar progressBar = null;

	public Updater() {

	}
	
	public void launchUpdate() throws Exception {
		this.createTempFolder();
		this.downloadInformationFile();
		this.loadUpdateSteps();
	}
	
	public String getProperty(String key) {
		return p.getProperty(key);
	}

	public String performUpdate(JProgressBar progressBar) throws Exception {
		this.progressBar = progressBar;
		String downloadURL = this.getProperty(Updater.KEY_DOWNLOAD_URL);
		DBToInstallZip = temp.getAbsolutePath() + File.separator + "newBD.zip";
		DBToInstallDestination = temp.getAbsolutePath() + File.separator
				+ "newBD";
		this.downloadFile(downloadURL, DBToInstallZip);

		TLanguage.initLanguage(TConfiguration.getLanguage());

		progressBar.setString(TLanguage
				.getString("UPDATER_FRAME.CHECKING_FILE_INTEGRITY"));

		System.out.println("Correct MD5: " + this.getProperty(KEY_MD5));
		if (this.checkMD5(DBToInstallZip, this.getProperty(KEY_MD5))) {

			progressBar.setString(TLanguage
					.getString("UPDATER_FRAME.UNZIPPING"));

			File UnzipFolder = new ZipUtils().UnzipIt(DBToInstallZip,
					DBToInstallDestination);
		} else {
			// Error checking MD5
			throw new Exception();
		}
		
		return DBToInstallDestination;
	}

	private boolean checkMD5(String DBToInstallZip, String correctMD5)
			throws Exception {
		System.out.println("File MD5: "
				+ MD5Checksum.getMD5Checksum(DBToInstallZip));
		return correctMD5.compareTo(MD5Checksum.getMD5Checksum(DBToInstallZip)) == 0;
	}

	private void loadUpdateSteps() throws IOException {
		File f = new File(temp.getAbsolutePath() + File.separator + INFORMATION_FILE_NAME + ".properties");
		if (f.exists()) {
			p = new Properties();
			p.load(new FileInputStream(f));
		}
	}
	
	private void createTempFolder() throws IOException {
		temp = new File(TConfiguration.getAbsoultePath() + File.separator + "temp");
		temp.mkdirs();
		informationFilePath = temp.getAbsolutePath() + File.separator + INFORMATION_FILE_NAME + ".properties";
	}
	
	private void downloadInformationFile() throws IOException {
		downloadFile(
				TConfiguration
						.getProperty(TConfiguration.KEY_DATABASE_UPDATE_URL),
				informationFilePath);
	}

	private void downloadFile(String source, String destination)
			throws IOException {
		URL downloadURL = new URL(source);
		BufferedInputStream in = new BufferedInputStream(downloadURL.openStream());
		FileOutputStream fout = new FileOutputStream(destination);

		int totalLength = downloadURL.openConnection().getContentLength();

		byte data[] = new byte[1024];
		int count;
		int totalDownloaded = 0;
		int progressValue = 0;
		while ((count = in.read(data, 0, 1024)) != -1){
			totalDownloaded += count;
			fout.write(data, 0, count);
			if (this.progressBar != null) {
				progressValue = (totalDownloaded / 100) * 100
						/ (totalLength / 100);
				this.progressBar.setValue(progressValue);
				this.progressBar.setString(progressValue + "%");
			}
		}
		
		in.close();
		fout.close();
	}

	public void removeTempFolder() {
		this.delete(temp.getAbsolutePath(), true);
	}

	private boolean delete(String filePath, boolean recursive) {
		File file = new File(filePath);
		if (!file.exists()) {
			return true;
		}

		if (!recursive || !file.isDirectory())
			return file.delete();

		String[] list = file.list();
		for (int i = 0; i < list.length; i++) {
			if (!delete(filePath + File.separator + list[i], true))
				return false;
		}

		return file.delete();
	}
}
