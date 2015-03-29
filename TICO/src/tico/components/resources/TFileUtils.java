/*
 * File: TFileUtils.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Aug 27, 2006
 * 
 * Company: Universidad de Zaragoza, CPS, DIIS
 * 
 * License:
 * 		This program is free software: you can redistribute it and/or 
 * 		modify it under the terms of the GNU General Public License 
 * 		as published by the Free Software Foundation, either version 3
 * 		of the License, or (at your option) any later version.
 * 
 * 		This program is distributed in the hope that it will be useful,
 * 		but WITHOUT ANY WARRANTY; without even the implied warranty of
 * 		MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * 		GNU General Public License for more details.
 * 
 * 		You should have received a copy of the GNU General Public License
 *     	along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 */

package tico.components.resources;

import java.io.File;

/**
 * Static class that contains attributes and methods to manage application files.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TFileUtils {
	// Image extensions
	/**
	 * JPEG image files extension.
	 */
	public final static String JPEG = "jpeg";

	/**
	 * JPG image files extension.
	 */
	public final static String JPG = "jpg";

	/**
	 * GIF image files extension.
	 */
	public final static String GIF = "gif";

	/**
	 * TIFF image files extension.
	 */
	//public final static String TIFF = "tiff";

	/**
	 * TIF image files extension.
	 */
	//public final static String TIF = "tif";

	/**
	 * PNG image files extension.
	 */
	public final static String PNG = "png";

	/**
	 * BMP image files extension.
	 */
	//public final static String BMP = "bmp";

	// Sound extensions
	/**
	 * MP3 sound files extension.
	 */
	public final static String MP3 = "mp3";

	/**
	 * WAV sound files extension.
	 */
	public final static String WAV = "wav";
	
	/**
	 * MID sound files extension.
	 */
	public final static String MID = "mid";
	
	/**
	 * MIDI sound files extension.
	 */
	public final static String MIDI = "midi";
	
	// Video extensions
	/**
	 * AVI video files extension.
	 */
	public final static String AVI = "avi";
	
	/**
	 * MPG video files extension.
	 */
	public final static String MPG = "mpg";
	
	/**
	 * MPEG video files extension.
	 */
	public final static String MPEG = "mpeg";
	
	/**
	 * WMV video files extension.
	 */
	public final static String WMV = "wmv";
	
	/**
	 * FLV video files extension.
	 */
	public final static String FLV = "flv";

	// Tico extensions
	/**
	 * Tico project files extension.
	 */
	public final static String TCO = "tco";
	
	/**
	 * Tico project for Android files extension.
	 */
	public final static String TCOA = "tcoa";

	/**
	 * Tico board files extension.
	 */
	public final static String BRD = "brd";
	
	// Other extensions
	/**
	 * XML files extension.
	 */
	public final static String XML = "xml";
	
	//Anyadido video 3gp para android
	public final static String GP3 = "3gp";
	
	//Anyadido video mp4 para android
	public final static String MP4 = "mp4";

	/**
	 * Returns the fileName, without the extension, of the specified
	 * <code>filePath</code>.
	 * 
	 * @param filePath The specified <code>filePath</code>
	 * @return The file name, without the extesion, of the specified 
	 * <code>filePath</code>
	 */
	public static String getFilename(String filePath) {
		String fileName = null;
		
		int ini = filePath.lastIndexOf(File.separator);
		int end = filePath.lastIndexOf('.');

		if ((ini < end) && (ini >= -1 && ini < filePath.length() - 1)
				&& (end >= 0 && end < filePath.length()))
			fileName = filePath.substring(ini + 1, end);
		
		return fileName;
	}

	/**
	 * Returns the fileName, without the extension, of the specified
	 * <code>file</code>.
	 * 
	 * @param file The specified <code>file</code>
	 * @return The file name, without the extesion, of the specified 
	 * <code>file</code>
	 */
	public static String getFilename(File file) {
		String filePath = file.getAbsolutePath();

		return TFileUtils.getFilename(filePath);
	}

	/**
	 * Returns the extension of the specified <code>filePath</code>.
	 * 
	 * @param filePath The specified <code>filePath</code>
	 * @return The extension of the specified <code>filePath</code>
	 */
	public static String getExtension(String filePath) {
		String extension = null;
		int i = filePath.lastIndexOf('.');

		if (i > 0 && i < filePath.length() - 1)
			extension = filePath.substring(i + 1).toLowerCase();

		return extension;

	}

	/**
	 * Returns the extension of the specified <code>file</code>.
	 * 
	 * @param file The specified <code>file</code>
	 * @return The extension of the specified <code>file</code>
	 */
	public static String getExtension(File file) {
		String filePath = file.getAbsolutePath();

		return TFileUtils.getExtension(filePath);
	}
	
	/**
	 * Returns a string that contains the specified <code>filePath</code> replacing
	 * its extension with the specified <code>extension</code>.
	 * 
	 * @param filePath The specified <code>filePath</code>
	 * @param extension The specified <code>extension</code>
	 * @return A new file path created replacing the original <code>filePath</code>'s
	 * extension with the specified <code>extension</code>
	 */
	public static String setExtension(String filePath, String extension) {
		// Get the file extension
		String fileExtension = getExtension(filePath);
		
		if (fileExtension == null)
			// If do not have extension, add it
			return filePath + "." + extension.toLowerCase();
		else 
			// If has different extension, replace it
			if (!fileExtension.equals(extension)) {
				int end = filePath.lastIndexOf('.');
				if (end >= 0 && end < filePath.length())
					return filePath.substring(0, end) + "." + extension.toLowerCase();
			}
		
		return filePath;
		
	}

	/**
	 * Returns a string that contains the specified <code>file</code> replacing
	 * its extension with the specified <code>extension</code>.
	 * 
	 * @param file The specified <code>file</code>
	 * @param extension The specified <code>extension</code>
	 * @return A new file path created replacing the original <code>file</code>'s
	 * extension with the specified <code>extension</code>
	 */
	public static String setExtension(File file, String extension) {
		String filePath = file.getAbsolutePath();

		return TFileUtils.setExtension(filePath, extension);
	}

	/**
	 * Determines if the specified <code>filePath</code> extension is an image
	 * file extension.
	 * 
	 * @param filePath The specified <code>filePath</code>
	 * @return <i>true</i> if the specified <code>filePath</code> extension is
	 * an image file extension
	 */
	public static boolean isImageFile(String filePath) {
		String extension = TFileUtils.getExtension(filePath);

		if (extension != null) {
			if (extension.equals(TFileUtils.GIF)
					//|| extension.equals(TFileUtils.TIF)
					//|| extension.equals(TFileUtils.TIFF)
					|| extension.equals(TFileUtils.JPEG)
					|| extension.equals(TFileUtils.JPG)
					|| extension.equals(TFileUtils.PNG)){
					//|| extension.equals(TFileUtils.BMP)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Determines if the specified <code>file</code> extension is an image
	 * file extension.
	 * 
	 * @param file The specified <code>file</code>
	 * @return <i>true</i> if the specified <code>file</code> extension is
	 * an image file extension
	 */
	public static boolean isImageFile(File file) {
		String filePath = file.getAbsolutePath();

		return TFileUtils.isImageFile(filePath);
	}

	/**
	 * Determines if the specified <code>filePath</code> type needs JAI (<i>Java
	 * Advanced Imaging API</i>) to be displayed.
	 * 
	 * @param filePath The specified <code>filePath</code>
	 * @return <i>true</i> if the specified <code>filePath</code> type needs JAI
	 * to be displayed
	 */
	public static boolean isJAIRequired(String filePath) {
		String ext = getExtension(filePath);
		if (ext.equals(GIF) || ext.equals(JPG) || ext.equals(JPEG)
				|| ext.equals(PNG))
			return false;

		return true;
	}

	/**
	 * Determines if the specified <code>file</code> type needs JAI (<i>Java
	 * Advanced Imaging API</i>) to be displayed.
	 * 
	 * @param file The specified <code>file</code>
	 * @return <i>true</i> if the specified <code>file</code> type needs JAI
	 * to be displayed
	 */
	public static boolean isJAIRequired(File file) {
		String filePath = file.getAbsolutePath();

		return TFileUtils.isJAIRequired(filePath);
	}


	/**
	 * Determines if the specified <code>filePath</code> extension is a sound
	 * file extension.
	 * 
	 * @param filePath The specified <code>filePath</code>
	 * @return <i>true</i> if the specified <code>filePath</code> extension is
	 * a sound file extension
	 */
	public static boolean isSoundFile(String filePath) {
		String extension = TFileUtils.getExtension(filePath);

		if (extension != null) {
			if (extension.equals(TFileUtils.MP3)
					|| extension.equals(TFileUtils.WAV)
					|| extension.equals(TFileUtils.MID)
					|| extension.equals(TFileUtils.MIDI)) {
				return true;
			}
		}

		return false;
	}


	/**
	 * Determines if the specified <code>file</code> extension is a sound
	 * file extension.
	 * 
	 * @param file The specified <code>file</code>
	 * @return <i>true</i> if the specified <code>file</code> extension is
	 * a sound file extension
	 */
	public static boolean isSoundFile(File file) {
		String filePath = file.getAbsolutePath();

		return TFileUtils.isSoundFile(filePath);
	}
	
	/**
	 * Determines if the specified <code>filePath</code> extension is a video
	 * file extension.
	 * 
	 * @param filePath The specified <code>filePath</code>
	 * @return <i>true</i> if the specified <code>filePath</code> extension is
	 * a video file extension
	 */
	public static boolean isVideoFile(String filePath) {
		String extension = TFileUtils.getExtension(filePath);

		if (extension != null) {
			if (extension.equals(TFileUtils.AVI)
					|| extension.equals(TFileUtils.MPG)
					|| extension.equals(TFileUtils.MPEG)
					|| extension.equals(TFileUtils.WMV)
					|| extension.equals(TFileUtils.FLV)
					|| extension.equals(TFileUtils.MP4)
					|| extension.equals(TFileUtils.GP3)) {
				return true;
			}
		}

		return false;
	}
	
	/**
	 * Determines if the specified <code>file</code> extension is a video
	 * file extension.
	 * 
	 * @param file The specified <code>file</code>
	 * @return <i>true</i> if the specified <code>file</code> extension is
	 * a video file extension
	 */
	public static boolean isVideoFile(File file) {
		String filePath = file.getAbsolutePath();

		return TFileUtils.isVideoFile(filePath);
	}

	/**
	 * Determines if the specified <code>filePath</code> extension is a Tico
	 * project file extension.
	 * 
	 * @param filePath The specified <code>filePath</code>
	 * @return <i>true</i> if the specified <code>filePath</code> extension is
	 * a Tico project file extension
	 */
	public static boolean isProjectFile(String filePath) {
		String extension = TFileUtils.getExtension(filePath);

		if (extension != null) {
			if (extension.equals(TFileUtils.TCO)) {
				return true;
			}
		}

		return false;
	}
	
	/**
	 * Determines if the specified <code>filePath</code> extension is a Tico
	 * for Android project file extension.
	 * 
	 * @param filePath The specified <code>filePath</code>
	 * @return <i>true</i> if the specified <code>filePath</code> extension is
	 * a Tico for Android project file extension
	 */
	public static boolean isProjectAndroidFile(String filePath) {
		String extension = TFileUtils.getExtension(filePath); 

		if (extension != null) {
			if (extension.equals(TFileUtils.TCOA)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Determines if the specified <code>file</code> extension is a Tico
	 * project file extension.
	 * 
	 * @param file The specified <code>file</code>
	 * @return <i>true</i> if the specified <code>file</code> extension is
	 * a Tico project file extension
	 */
	public static boolean isProjectFile(File file) {
		String filePath = file.getAbsolutePath();

		return TFileUtils.isProjectFile(filePath);
	}
	
	/**
	 * Determines if the specified <code>file</code> extension is a Tico
	 * for Android project file extension.
	 * 
	 * @param file The specified <code>file</code>
	 * @return <i>true</i> if the specified <code>file</code> extension is
	 * a Tico for Android project file extension
	 */
	public static boolean isProjectAndroidFile(File file) {
		String filePath = file.getAbsolutePath();

		return TFileUtils.isProjectAndroidFile(filePath);
	}

	/**
	 * Determines if the specified <code>filePath</code> extension is a Tico
	 * board file extension.
	 * 
	 * @param filePath The specified <code>filePath</code>
	 * @return <i>true</i> if the specified <code>filePath</code> extension is
	 * a Tico board file extension
	 */
	public static boolean isBoardFile(String filePath) {
		String extension = TFileUtils.getExtension(filePath);

		if (extension != null) {
			if (extension.equals(TFileUtils.BRD)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Determines if the specified <code>file</code> extension is a Tico
	 * board file extension.
	 * 
	 * @param file The specified <code>file</code>
	 * @return <i>true</i> if the specified <code>file</code> extension is
	 * a Tico board file extension
	 */
	public static boolean isBoardFile(File file) {
		String filePath = file.getAbsolutePath();

		return TFileUtils.isBoardFile(filePath);
	}


	/**
	 * Determines if the specified <code>filePath</code> extension is a Tico
	 * file extension.
	 * 
	 * @param filePath The specified <code>filePath</code>
	 * @return <i>true</i> if the specified <code>filePath</code> extension is
	 * a Tico file extension
	 */
	public static boolean isTicoFile(String filePath) {
		String extension = TFileUtils.getExtension(filePath);

		if (extension != null) {
			if (extension.equals(TFileUtils.TCO)
					|| extension.equals(TFileUtils.BRD)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Determines if the specified <code>file</code> extension is a Tico
	 * file extension.
	 * 
	 * @param file The specified <code>file</code>
	 * @return <i>true</i> if the specified <code>file</code> extension is
	 * a Tico file extension
	 */
	public static boolean isTicoFile(File file) {
		String filePath = file.getAbsolutePath();
		return TFileUtils.isTicoFile(filePath);
	}

	
}
