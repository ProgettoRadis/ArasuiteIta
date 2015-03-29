/*
 * File: TBoardConstants.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Mar 29, 2006
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

package tico.board;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.Icon;

import org.jgraph.graph.GraphConstants;

import tico.board.components.TComponent;
import tico.editor.TEditor;


/**
 * A collection of attribute keys and methods to apply to a <code>Map</code>
 * to get/set the properties in a typesafe manner added to the base
 * <code>GrahConstants</code> attributes.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TBoardConstants extends GraphConstants {
	
	public static TBoard currentBoard = null;
	public static TEditor editor = null;
	
	


	
	/**
	 * Default background color.
	 */
	public final static Color DEFAULT_BACKGROUND = Color.WHITE;

	/**
	 * Default foreground color.
	 */
	public final static Color DEFAULT_FOREGROUND = Color.BLACK;
	
	/**
	 * Default alternative border color.
	 */
	public final static Color DEFAULT_ALTERNATIVE_BORDERCOLOR = Color.RED;
	
	/**
	 * Default alternative border SIZE.
	 */
	public final static int DEFAULT_ALTERNATIVE_LINEWIDTH = 4;

	/**
	 * Represents the fit imageResizeStyle.
	 * 
	 * @see #getImageResizeStyle(Map)
	 * @see #setImageResizeStyle(Map, int)
	 */
	public final static int IMAGE_FIT = 0;

	/**
	 * Represents the scale imageResizeStyle.
	 * 
	 * @see #getImageResizeStyle(Map)
	 * @see #setImageResizeStyle(Map, int)
	 */
	public final static int IMAGE_SCALE = 1;

	/**
	 * Represents the center imageResizeStyle.
	 * 
	 * @see #getImageResizeStyle(Map)
	 * @see #setImageResizeStyle(Map, int)
	 */
	public final static int IMAGE_CENTER = 2;
	
	/**
	 * Represents the begin corner of a TLine.
	 * 
	 * @see #getStartCorner(Map)
	 * @see #setStartCorner(Map, int)
	 */
	public final static int TOP_LEFT_CORNER = 0;

	/**
	 * Represents the begin corner of a TLine.
	 * 
	 * @see #getStartCorner(Map)
	 * @see #setStartCorner(Map, int)
	 */
	public final static int TOP_RIGHT_CORNER = 2;

	/**
	 * Represents the begin corner of a TLine.
	 * 
	 * @see #getStartCorner(Map)
	 * @see #setStartCorner(Map, int)
	 */
	public final static int BOTTOM_LEFT_CORNER = 5;

	/**
	 * Represents the begin corner of a TLine.
	 * 
	 * @see #getStartCorner(Map)
	 * @see #setStartCorner(Map, int)
	 */
	public final static int BOTTOM_RIGHT_CORNER = 7;
	
	/**
	 * Represents the size of the image controller in the controller cell menu.
	 */
	public final static int CONTROLLER_IMAGE_SIZE = 70;
	
	/**
	 * Represents the action of a controller cell.
	 * 
	 * @see #getActionCode(Map)
	 * @see #setActionCode(Map, int)
	 */
	public final static int EXIT_ACTION_CODE = 1;
	
	public final static int UNDO_ACTION_CODE = 2;
	
	public final static int UNDO_ALL_ACTION_CODE = 3;
	
	public final static int READ_ACTION_CODE = 4;
	
	public final static int RETURN_ACTION_CODE = 5;
	
	public final static int STOP_ACTION_CODE = 6;
	
	public final static int HOME_ACTION_CODE = 7;
        
        public final static int COPY_ACTION_CODE = 8;
        
         /**
         * Represents the google mode for <code>SynthesizerMode</code> attribute. 
         */
        public final static int GOOGLE_MODE = 0;
        
        /**
         * Represents the SAPI mode for <code>SynthesizerMode</code> attribute. 
         */
        public final static int SAPI_MODE = 1;
	
	/**
	 * Key for the <code>text</code> attribute. Use instances of String as
	 * values for this key.
	 */
	public final static String TEXT = "text";

	/**
	 * Key for the <code>id</code> attribute. Use instances of String as
	 * values for this key.
	 */
	public final static String ID = "id";

	/**
	 * Key for the <code>browseable</code> attribute. Use instances of Boolean as
	 * values for this key.
	 */
	public final static String BROWSEABLE = "browseable";

	/**
	 * Key for the <code>alternateIcon</code> attributes. Use instances of
	 * Icon as values for this key.
	 */
	public final static String ALTERNATIVE_ICON = "alternativeIcon";

	/**
	 * Key for the <code>soundFile</code> attributes. Use instances of
	 * String as values for this key.
	 */
	public final static String SOUND_FILE = "soundFile";
        
        /**
         * Key for the <code>synthMode</code> attributes. Use instances of
         * String as values for this key.
         */
        
        public final static String SYNTH_MODE = "synthMode";
        
        /**
         * Key for the <code>voiceName</code> attributes. Use instances of
         * String as values for this key.
         */
        public final static String VOICE_NAME = "voiceName";
        
        
        /**
         * Key for the <code>voiceText</code> attributes. Use instances of
         * String as values for this key.
         */
        public final static String VOICE_TEXT = "voiceText";
	
	/**
	 * Key for the <code>alternativeSoundFile</code> attributes. Use instances of
	 * String as values for this key.
	 */
	public final static String ALTERNATIVE_SOUND_FILE = "alternativeSoundFile";
	
	/**
	 * Key for the <code>browsingsoundFile</code> attributes. Use instances of
	 * String as values for this key.
	 */
	public final static String BROWSING_SOUND_FILE ="browsingSound";
	
	/**
	 * Key for the <code>videoFile</code> attributes. Use instances of
	 * String as values for this key.
	 */
	public final static String VIDEO_FILE = "videoFile";
	
	/**
	 * Key for the <code>videoURL</code> attributes. Use instances of
	 * String as values for this key.
	 */
	public final static String VIDEO_URL = "videoURL";
	
	/**
	 * Key for the <code>startCorner</code> attribute. Use instances of
	 * Integer as values for this key.
	 */
	public final static String START_CORNER = "startCorner";
	
	/**
	 * Key for the <code>actionCode</code> attribute. Use instances of
	 * Integer as values for this key.
	 */
	public final static String ACTION_CODE = "actionCode";

	/**
	 * Key for the <code>column</code> attribute. Use instances of Integer as
	 * values for this key.
	 */
	public final static String COLUMN = "column";

	/**
	 * Key for the <code>row</code> attribute. Use instances of Integer as
	 * values for this key.
	 */
	public final static String ROW = "row";

	/**
	 * Key for the <code>order</code> attribute. Use instances of Integer as
	 * values for this key.
	 */
	public final static String ORDER = "order";

	/**
	 * Key for the <code>orderedCellList</code> attribute. Use instances of ArrayList as
	 * values for this key.
	 */	
	public final static String ORDERED_CELL_LIST = "orderedCellList";

	/**
	 * Key for the <code>unorderedCellList</code> attribute. Use instances of ArrayList as
	 * values for this key.
	 */	
	public final static String UNORDERED_CELL_LIST = "unorderedCellList";
	
	/**
	 * Key for the <code>resizeToText</code> attribute. Use instances of Boolean as
	 * values for this key.
	 */
	public final static String RESIZE_TO_TEXT = "resizeToText";

	/**
	 * Key for the <code>imageResizeStyle</code> attribute. Use instances of Integer as
	 * values for this key.
	 */
	public final static String IMAGE_RESIZE_STYLE = "imageResizeStyle";

	/**
	 * Key for the <code>textSender</code> attributes. Use instances of Boolean as
	 * values for this key
	 */
	public final static String TEXT_SENDER = "textSender";
	
	/**
	 * Key for the <code>textReceiver</code> attribute. Use instances of Boolean as
	 * values for this key.
	 */
	public final static String TEXT_RECEIVER = "textReceiver";

	/**
	 * Key for the <code>sendText</code> attribute. Use instances of String as
	 * values for this key.
	 */
	public final static String SEND_TEXT = "sendText";

	/**
	 * Key for the <code>sendTextTerget</code> attribute. Use instances of TTextArea as
	 * values for this key.
	 */
	public final static String SEND_TEXT_TARGET = "sendTextTarget";

	/**
	 * Key for the <code>sendTextTimer</code> attribute. Use instances of Integer as
	 * values for this key.
	 */
	public final static String SEND_TEXT_TIMER = "sendTextTimer";

	/**
	 * Key for the <code>accumulated</code> attribute. Use instances of Boolean as
	 * values for this key.
	 */
	public final static String ACCUMULATED = "accumulated";

	/**
	 * Key for the <code>followingBoard</code> attribute. Use instances of TBoard as
	 * values for this key.
	 */	
	public final static String FOLLOWING_BOARD = "followingBoard";
	
	/**
	 * Key for the <code>changeLineWidth</code> attribute. Use instances of Integer as 
	 * values for this key.
	 */
	
	public final static String ALTERNATIVE_LINEWIDTH="alternativeLinewidth";
	
	/**
	 * Key for the <code>ChangeColor</code> attribute. Use instances of Color as 
	 * values for this key.
	 */
	public final static String ALTERNATIVE_BORDER_COLOR="alternativeBorderColor";
	
	/**
	 * Key for the <code>ChangeColorGrid</code> attribute. Use instances of Color as 
	 * values for this key.
	 */
	public final static String CHANGE_COLOR_GRID="ChangeColorGrid";

	/**
	 * Key for the <code>ChangeLineGrid</code> attribute. Use instances of Integer as 
	 * values for this key.
	 */
	public final static String CHANGE_LINE_GRID="ChangeLineGrid";
	
	/**
	 * Key for the <code>EnvironmentAction</code> attribute. Use instances of String as 
	 * values for this key.
	 */
	public final static String ENVIRONMENT_ACTION="environmentAction";
	
	/**
	 * Key for the <code>ActionPosition</code> attribute. Use instances of Integer as 
	 * values for this key.
	 */
	public final static String ACTION_POSITION="actionPosition";
	

	/**
	 * Sets the text attribute in the specified map to the specified value.
	 */
	public static final void setText(Map map, String value) {
		map.put(TEXT, value);
	}

	/**
	 * Returns the text attribute from the specified map.
	 */
	public static final String getText(Map map) {
		return (String)map.get(TEXT);
	}

	/**
	 * Sets the text attribute in the specified map to the specified value.
	 */
	public static final void setId(Map map, String value) {
		map.put(ID, value);
	}

	/**
	 * Returns the text attribute from the specified map.
	 */
	public static final String getId(Map map) {
		return (String)map.get(ID);
	}

	/**
	 * Sets the browseable attribute in the specified map to the specified value.
	 */
	public static final void setBrowseable(Map map, boolean flag) {
		map.put(BROWSEABLE, new Boolean(flag));
	}

	/**
	 * Returns the browseable attribute from the specified map.
	 */
	public static final boolean isBrowseable(Map map) {
		Boolean bool = (Boolean) map.get(BROWSEABLE);
		if (bool != null)
			return bool.booleanValue();
		return false;
	}
	
	/**
	 * Sets the alternateIcon attribute in the specified map to the specified
	 * value.
	 */
	public static final void setAlternativeIcon(Map map, Icon value) {
		map.put(ALTERNATIVE_ICON, value);
	}

	/**
	 * Returns the soundFile attribute from the specified map.
	 */
	public static final String getSoundFile(Map map) {
		File fichero=(File)map.get(SOUND_FILE);
		if (fichero!=null)
		return fichero.getPath();
		else return null;
	}

	/**
	 * Sets the soundFile attribute in the specified map to the specified
	 * value.
	 */
	public static final void setSoundFile(Map map, String value) {
	
		map.put(SOUND_FILE,new File(value));
	}
	
	
	/**
	 * Returns the alternativeSoundFile attribute from the specified map.
	 */
	public static final String getAlternativeSoundFile(Map map) {
		File fichero=(File)map.get(ALTERNATIVE_SOUND_FILE);
		if (fichero!=null)
		return fichero.getPath();
		else return null;
	}

	/**
	 * Sets the alternativeSoundFile attribute in the specified map to the specified
	 * value.
	 */
	public static final void setAlternativeSoundFile(Map map, String value) {
	
		map.put(ALTERNATIVE_SOUND_FILE,new File(value));
	}
        
        /**
	 * Sets the synthesizer mode attribute in the specified map to the specified
	 * value.
	 */
        public static final void setSynthMode(Map map, String value){
            map.put(SYNTH_MODE, value);
        }
        
        public static final String getSynthMode(Map map){
            return (String)map.get(SYNTH_MODE);
        }
        
        
        /**
	 * Sets the voice name attribute in the specified map to the specified
	 * value.
	 */
        public static final void setVoiceName(Map map, String value){
            map.put(VOICE_NAME, value);
        }
        
        public static final String getVoiceName(Map map){
            return (String)map.get(VOICE_NAME);
        }
        
        /**
	 * Sets the voiceText attribute in the specified map to the specified
	 * value.
	 */
        public static final void setVoiceText(Map map, String value){
            map.put(VOICE_TEXT, value);
        }
        
        public static final String getVoiceText(Map map){
            return (String)map.get(VOICE_TEXT);
        }
	
	
	public static final String getBrowsingSoundFile (Map map)
	{
		return (String)map.get(BROWSING_SOUND_FILE);
	}
	
	public static final void setBrowsingSoundFile(Map map, String value)
	{
		map.put(BROWSING_SOUND_FILE, value);
	}
	
	/**
     * Returns the videoFile attribute from the specified map.
     */
    public static final String getVideoFile(Map map) {
        File fichero=(File)map.get(VIDEO_FILE);
        if (fichero!=null)
        return fichero.getPath();
        else return null;
    }

    /**
     * Sets the videoFile attribute in the specified map to the specified
     * value.
     */
    public static final void setVideoFile(Map map, String value) {
        map.put(VIDEO_FILE,new File(value));
    }
    
    /**
     * Returns the videoFile attribute from the specified map.
     */
    public static final String getVideoURL(Map map) {
    	return (String)map.get(VIDEO_URL);
    }

    /**
     * Sets the videoFile attribute in the specified map to the specified
     * value.
     */
    public static final void setVideoURL(Map map, String value) {
        map.put(VIDEO_URL, value);
    }

    /**
	 * Returns the alternateIcon attribute from the specified map.
	 */
	public static final Icon getAlternativeIcon(Map map) {
		return (Icon)map.get(ALTERNATIVE_ICON);
	}

	/**
	 * Sets the startPoint attribute in the specified map to the specified
	 * value.
	 */
	public static final void setStartCorner(Map map, int value) {
		map.put(START_CORNER, new Integer(value));
	}

	/**
	 * Returns the StartPoint attribute from the specified map.
	 */
	public static final int getStartCorner(Map map) {
		Integer intObj = (Integer)map.get(START_CORNER);
		if (intObj != null)
			return intObj.intValue();
		return 0;
	}
	
	/**
	 * Sets the actionCode attribute in the specified map to the specified
	 * value.
	 */
	public static final void setActionCode(Map map, int value) {
		map.put(ACTION_CODE, new Integer(value));
	}

	/**
	 * Returns the actionCode attribute from the specified map.
	 */
	public static final int getActionCode(Map map) {
		Integer intObj = (Integer)map.get(ACTION_CODE);
		if (intObj != null)
			return intObj.intValue();
		return 0;
	}

	/**
	 * Sets the column attribute in the specified map to the specified value.
	 */
	public static final void setColumn(Map map, int value) {
		map.put(COLUMN, new Integer(value));

	}

	/**
	 * Returns the column attribute from the specified map.
	 */
	public static final int getColumn(Map map) {
		Integer intObj = (Integer)map.get(COLUMN);
		if (intObj != null)
			return intObj.intValue();
		return 0;
	}

	/**
	 * Sets the row attribute in the specified map to the specified value.
	 */
	public static final void setRow(Map map, int value) {
		map.put(ROW, new Integer(value));

	}

	/**
	 * Returns the row attribute from the specified map.
	 */
	public static final int getRow(Map map) {
		Integer intObj = (Integer)map.get(ROW);
		if (intObj != null)
			return intObj.intValue();
		return 0;
	}

	/**
	 * Sets the order attribute in the specified map to the specified value.
	 */
	public static final void setOrder(Map map, int value) {
		map.put(ORDER, new Integer(value));
	}

	/**
	 * Returns the order attribute from the specified map.
	 */
	public static final int getOrder(Map map) {
		Integer intObj = (Integer)map.get(ORDER);
		if (intObj != null)
			return intObj.intValue();
		return 0;
	}

	/**
	 * Sets the orderedCellList attribute in the specified map to the specified value.
	 */
	public static final void setOrderedCellList(Map map, ArrayList value) {
		map.put(ORDERED_CELL_LIST, value.toArray());
	}

	/**
	 * Returns the orderedCellList attribute from the specified map.
	 */
	public static final ArrayList getOrderedCellList(Map map) {
		Object[] array = (Object[]) map.get(ORDERED_CELL_LIST);
		ArrayList orderedCells = new ArrayList();
		if (array != null) for (int i = 0; i < array.length; i++)
			orderedCells.add(array[i]);
		
		return orderedCells;
	}

	/**
	 * Sets the unorderedCellList attribute in the specified map to the specified value.
	 */
	public static final void setUnorderedCellList(Map map, ArrayList value) {
		map.put(UNORDERED_CELL_LIST, value.toArray());
	}

	/**
	 * Returns the unorderedCellList attribute from the specified map.
	 */
	public static final ArrayList getUnorderedCellList(Map map) {
		Object[] array = (Object[]) map.get(UNORDERED_CELL_LIST);
		ArrayList orderedCells = new ArrayList();
		if (array != null) for (int i = 0; i < array.length; i++)
			orderedCells.add(array[i]);
		
		return orderedCells;
	}

	/**
	 * Sets the resizeToText attribute in the specified map to the specified value.
	 */
	public static final void setResizeToText(Map map, boolean flag) {
		map.put(RESIZE_TO_TEXT, new Boolean(flag));
	}

	/**
	 * Returns the resizeToText attribute from the specified map.
	 */
	public static final boolean isResizeToText(Map map) {
		Boolean bool = (Boolean) map.get(RESIZE_TO_TEXT);
		if (bool != null)
			return bool.booleanValue();
		return false;
	}

	/**
	 * Sets the imageResizeStyle attribute in the specified map to the specified value.
	 */
	public static final void setImageResizeStyle(Map map, int value) {
		map.put(IMAGE_RESIZE_STYLE, new Integer(value));
	}

	/**
	 * Returns the imageResizeStyle attribute from the specified map.
	 */
	public static final int getImageResizeStyle(Map map) {
		Integer intObj = (Integer)map.get(IMAGE_RESIZE_STYLE);
		if (intObj != null)
			return intObj.intValue();
		return IMAGE_FIT;	
	}

	/**
	 * Sets the textSender attribute in the specified map to the specified value.
	 */
	public static final void setTextSender(Map map, boolean flag) {
		map.put(TEXT_SENDER, new Boolean(flag));
	}

	/**
	 * Returns the textSender attribute from the specified map.
	 */
	public static final boolean isTextSender(Map map) {
		Boolean bool = (Boolean) map.get(TEXT_SENDER);
		if (bool != null)
			return bool.booleanValue();
		return false;
	}

	/**
	 * Sets the textReceiver attribute in the specified map to the specified value.
	 */
	public static final void setTextReceiver(Map map, boolean flag) {
		map.put(TEXT_RECEIVER, new Boolean(flag));
	}

	/**
	 * Returns the textReceiver attribute from the specified map.
	 */
	public static final boolean isTextReceiver(Map map) {
		Boolean bool = (Boolean) map.get(TEXT_RECEIVER);
		if (bool != null)
			return bool.booleanValue();
		return false;
	}

	/**
	 * Sets the sendTextTarget attribute in the specified map to the specified value.
	 */
	public static final void setSendTextTarget(Map map, TComponent value) {
		map.put(SEND_TEXT_TARGET, value);
	}

	/**
	 * Returns the sendTextTarget attribute from the specified map.
	 */
	public static final TComponent getSendTextTarget(Map map) {
		return (TComponent)map.get(SEND_TEXT_TARGET);
	}

	/**
	 * Sets the sendText attribute in the specified map to the specified value.
	 */
	public static final void setSendText(Map map, String value) {
		map.put(SEND_TEXT, value);
	}

	/**
	 * Returns the sendText attribute from the specified map.
	 */
	public static final String getSendText(Map map) {
		return (String)map.get(SEND_TEXT);
	}

	/**
	 * Sets the sendTextTimer attribute in the specified map to the specified value.
	 */
	public static final void setSendTextTimer(Map map, int value) {
		map.put(SEND_TEXT_TIMER, new Integer(value));
	}

	/**
	 * Returns the sendTextTimer attribute from the specified map.
	 */
	public static final int getSendTextTimer(Map map) {
		Integer intObj = (Integer)map.get(SEND_TEXT_TIMER);
		if (intObj != null)
			return intObj.intValue();
		return IMAGE_FIT;	
	}

	/**
	 * Sets the resizeToText attribute in the specified map to the specified value.
	 */
	public static final void setAccumulated(Map map, boolean flag) {
		map.put(ACCUMULATED, new Boolean(flag));
	}

	/**
	 * Returns the resizeToText attribute from the specified map.
	 */
	public static final boolean isAccumulated(Map map) {
		Boolean bool = (Boolean) map.get(ACCUMULATED);
		if (bool != null)
			return bool.booleanValue();
		return false;
	}

	/**
	 * Sets the followingBoard attribute in the specified map to the specified value.
	 */
	public static final void setFollowingBoard(Map map, String value) {
		map.put(FOLLOWING_BOARD, value);
	}

	/**
	 * Returns the followingBoard attribute from the specified map.
	 */
	/*public static final TBoard getFollowingBoard(Map map) {
		return (TBoard)map.get(FOLLOWING_BOARD);
	}*/
	
	public static final String getFollowingBoardName(Map map) {
		return (String)map.get(FOLLOWING_BOARD);
	}
	
	public static final void setAlternativeLinewidth(Map map, int value)
	{
		map.put(ALTERNATIVE_LINEWIDTH, new Integer(value));
	}
	
	public static final int getAlternativeLinewidth(Map map) {
		Integer intObj = (Integer)map.get(ALTERNATIVE_LINEWIDTH);
		
		if (intObj != null)
			return intObj.intValue();
		return 0;
	}

	public static final void setAlternativeBorderColor(Map map, Color value) {
		map.put(ALTERNATIVE_BORDER_COLOR, value);
	}
	
	public static final Color getAlternativeBorderColor(Map map) {
		Color colObj = (Color)map.get(ALTERNATIVE_BORDER_COLOR);
		
		if (colObj != null)
			return colObj;
		return Color.red;
	} 
	
	public static final int getChangeLineWidthGrid(Map map) {
		Integer intObj = (Integer)map.get(CHANGE_LINE_GRID);
		
		if (intObj != null)
			return intObj.intValue();
		return 0;
	}
	public static final void setChangeLineWidthGrid(Map map,int value) {
		map.put(CHANGE_LINE_GRID, new Integer(value));
	}
	
	
	public static final void setChangeColorGrid(Map map, Color value) {
		map.put(CHANGE_COLOR_GRID,value);
	}
	
	public static final Color getChangeColorGrid(Map map) {
		Color colObj = (Color)map.get(CHANGE_COLOR_GRID);
		
		if (colObj != null)
			return colObj;
		return Color.red;
	}	
	
	public static final void setEnvironmentAction(Map map, String value) {
		map.put(ENVIRONMENT_ACTION, value);
	}

	/**
	 * Returns the sendText attribute from the specified map.
	 */
	public static final String getEnvironmentAction(Map map) {
		return (String)map.get(ENVIRONMENT_ACTION);
	}
	
	public static final int getPositionAction(Map map) {
		Integer intObj = (Integer)map.get(ACTION_POSITION);
		
		if (intObj != null)
			return intObj.intValue();
		return -1;
	}
	
	public static final void setPositionAction(Map map,int value){
		map.put(ACTION_POSITION, new Integer(value));
	}
}
