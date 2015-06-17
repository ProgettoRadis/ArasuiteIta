// A simple way to have global variables, accessed from different modules.

package araword;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import javax.swing.border.Border;

import araword.classes.AWElement;
import araword.listeners.FocusListenerImpl;
import araword.listeners.KeyListenerImpl;
import araword.listeners.MouseListenerImpl;
import araword.listeners.MouseMotionListenerImpl;
import araword.tts.strategy.TtsStrategy;

public abstract class G { // Can't be instantiated

    // Document variables
    public static String documentLanguage;
    public static int imagesSize; //Picto size
    public static int imagesSizePDF;  //save the picto size while prev for PDF export
    public static int numPictosLinePDF=5;
    public static boolean licensePDF=true; //add arasaac license on pdf docs

    public static Font font;
    public static Font tempFont; // Useful for saving it while in dialogs...
    public static Color color;
    public static Color tempColor;
    public static boolean textBelowPictogram;

    // User default variables  
    public static String defaultDocumentLanguage;
    public static int defaultImagesSize;
    public static Font defaultFont;
    public static Font tempDefaultFont; // Useful for saving it while in dialogs...
    public static Color defaultColor;
    public static Color tempDefaultColor;
    public static boolean defaultTextBelowPictogram;

    // Config variables
    public static String applicationLanguage;
    public static int maxLengthCompoundWords;
    public static int iconsSize; // Not to be confused with imagesSize. TOOLBAR icons.
    public static int iconsBorderSize;
    public static boolean classicIcons; // 1, classic icons. 0, ARASAAC icons.
    // 3 = "cepillo de dientes" 1 = only simple words allowed.
    // WARNING: Increasing this number will decrease performance dramatically.
    public static String pictogramsPath; // Directory, i.e. ../pictograms
    public static String tempPictogramsPath = ""; // For import DB dialog.
    public static ImageIcon notFound; // Not exactly a config variable,
    // but its size depends on imagesSize, and must be
    // reloaded if that variable changes, so it's fine to keep it here.

    // Global variables for easy use.
    public static ArrayList<AWElement> elementList = new ArrayList<AWElement>();
    public static AWElement activeElement = null;
    public static AWElement previousElement = null;
    public static AWElement previous2Element = null;
    public static AWElement nextElement = null;
    public static AWElement next2Element = null;
    public static int activeElementPosition;
    public static Component activeComponent;
    //public static DB conn;
    public static Connection connVerbsDB;
    public static String[] documentLanguages;
    public static String[] applicationLanguages;
    public static String databaseName;
    public static HashMap<String,Border> borders = new HashMap<String,Border>();
    public static final String separatorSpace = "  ";
    public static final Border myTextFieldBorder = BorderFactory.createEmptyBorder(5,5,5,5);
    // Useful for automatic selection of last pictogram used for certain word.
    // Only store words with numPictogram != 0 (in normal documents, will be no more than five or six words).
    public static Hashtable lastPictogramWordAssociation = new Hashtable();
    public static String activeDocumentFileName = "";

    // Selection variables.
    public static int selectionState = 0; // State-machine of selection...
    public static int indexSelectionFrom = -30;
    public static int indexSelectionTo = -20;
    public static Point lastPointDragged = new Point();
    public static boolean wereDrag = false;
    public static Point auxPointForDrag = new Point();


    // Event listeners, to add and remove if needed.
    public static MouseListener mouseListener = new MouseListenerImpl();
    public static MouseMotionListener mouseMotionListener = new MouseMotionListenerImpl();
    public static KeyListener keyListener = new KeyListenerImpl();
    public static FocusListener focusListener = new FocusListenerImpl();

    // Find/replace variables.
    public static int lastPositionFound = -1;
    public static String lastStringAsked = "";

    // Undo/redo variables.
    public static int maxUndoLevel = 5; // Increase this needs more CPU and RAM.
    public static ArrayList<String> undoList = new ArrayList<String>();
    public static ArrayList<String> redoList = new ArrayList<String>();

    // GUI object variables needed here for easy modularization of source code.
    public static JTextPane textZone;
    public static araword.gui.GUI appWindow;

    //arasuite ita - tts
    public static TtsStrategy ttsStrategy;
    public static String[] ttsOptions;
    public static String defaultTTS;

    // arasuite ita - show/hide borders
    public static boolean showBorders;

    public static void giveMePrivateVariables(JTextPane one) {
        // A trick to avoid problems with easy GUI editing.
        textZone = one;
    }

}
