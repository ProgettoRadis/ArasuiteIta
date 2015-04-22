// Requires JRE 1.6

// Creates the main window of the application, GUI, and the actions
// associated with options in the menu bar.

// SQLite 3.x uses UTF-8 by default. Check that Eclipse is using UTF-8 for text encoding (File->Project Properties)
package araword.gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.KeyStroke;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import org.jdesktop.application.FrameView;
import org.jdesktop.application.SingleFrameApplication;
import org.jdom.Element;

import say.swing.JFontChooser;
import araword.G;
import araword.classes.AWElement;
import araword.configuration.TLanguage;
import araword.configuration.TSetup;
import araword.db.DBManagement;
import araword.utils.TextUtils;
import dialogs.mainFrame;

public class GUI extends FrameView
{

    public GUI(SingleFrameApplication app) {
        super(app);
        initComponents();

        try {
            TSetup.load();
            TLanguage.initLanguage(G.applicationLanguage);
            // Menu bar and toolbar.
            setMenuBar(menuBar);
            String iconsPath = "classicIcons";
            if (G.classicIcons) iconsPath = "classicIcons";
            else iconsPath = "ARASAACIcons";

            // Ugly, but only quick way found to resize PNGs without losing transparency.
            toolBarButtonFileNew.setIcon(new ImageIcon(new ImageIcon(
                    "resources" + File.separator + "imgToolbar" + File.separator + iconsPath + File.separator + "file-new.png").getImage().getScaledInstance(G.iconsSize,G.iconsSize,java.awt.Image.SCALE_SMOOTH)));
            toolBarButtonFileOpen.setIcon(new ImageIcon(new ImageIcon(
                    "resources" + File.separator + "imgToolbar" + File.separator + iconsPath + File.separator + "file-open.png").getImage().getScaledInstance(G.iconsSize,G.iconsSize,java.awt.Image.SCALE_SMOOTH)));
            toolBarButtonFileSave.setIcon(new ImageIcon(new ImageIcon(
                    "resources" + File.separator + "imgToolbar" + File.separator + iconsPath + File.separator + "file-save-as.png").getImage().getScaledInstance(G.iconsSize,G.iconsSize,java.awt.Image.SCALE_SMOOTH)));
            toolBarButtonEditUndo.setIcon(new ImageIcon(new ImageIcon(
                    "resources" + File.separator + "imgToolbar" + File.separator + iconsPath + File.separator + "edit-undo.png").getImage().getScaledInstance(G.iconsSize,G.iconsSize,java.awt.Image.SCALE_SMOOTH)));
            toolBarButtonEditRedo.setIcon(new ImageIcon(new ImageIcon(
                    "resources" + File.separator + "imgToolbar" + File.separator + iconsPath + File.separator + "edit-redo.png").getImage().getScaledInstance(G.iconsSize,G.iconsSize,java.awt.Image.SCALE_SMOOTH)));
            toolBarButtonEditCut.setIcon(new ImageIcon(new ImageIcon(
                    "resources" + File.separator + "imgToolbar" + File.separator + iconsPath + File.separator + "edit-cut.png").getImage().getScaledInstance(G.iconsSize,G.iconsSize,java.awt.Image.SCALE_SMOOTH)));
            toolBarButtonEditCopy.setIcon(new ImageIcon(new ImageIcon(
                    "resources" + File.separator + "imgToolbar" + File.separator + iconsPath + File.separator + "edit-copy.png").getImage().getScaledInstance(G.iconsSize,G.iconsSize,java.awt.Image.SCALE_SMOOTH)));
            toolBarButtonEditPaste.setIcon(new ImageIcon(new ImageIcon(
                    "resources" + File.separator + "imgToolbar" + File.separator + iconsPath + File.separator + "edit-paste.png").getImage().getScaledInstance(G.iconsSize,G.iconsSize,java.awt.Image.SCALE_SMOOTH)));
            toolBarButtonPictogramsNextImage.setIcon(new ImageIcon(new ImageIcon(
                    "resources" + File.separator + "imgToolbar" + File.separator + iconsPath + File.separator + "pictograms-next-image.png").getImage().getScaledInstance(G.iconsSize,G.iconsSize,java.awt.Image.SCALE_SMOOTH)));
            toolBarButtonPictogramsCompoundSplitWord.setIcon(new ImageIcon(new ImageIcon(
                    "resources" + File.separator + "imgToolbar" + File.separator + iconsPath + File.separator + "pictograms-compound-split-word.png").getImage().getScaledInstance(G.iconsSize,G.iconsSize,java.awt.Image.SCALE_SMOOTH)));
            toolBarButtonPictogramsChangeName.setIcon(new ImageIcon(new ImageIcon(
                    "resources" + File.separator + "imgToolbar" + File.separator + iconsPath + File.separator + "pictograms-change-name.png").getImage().getScaledInstance(G.iconsSize,G.iconsSize,java.awt.Image.SCALE_SMOOTH)));
            toolBarButtonPictogramsInsertImage.setIcon(new ImageIcon(new ImageIcon(
                    "resources" + File.separator + "imgToolbar" + File.separator + iconsPath + File.separator + "pictograms-insert-image.png").getImage().getScaledInstance(G.iconsSize,G.iconsSize,java.awt.Image.SCALE_SMOOTH)));
            toolBarButtonVoiceSintesys.setIcon(new ImageIcon(new ImageIcon(
                    "resources" + File.separator + "imgToolbar" + File.separator + iconsPath + File.separator + "voice-sintesys.png").getImage().getScaledInstance(G.iconsSize,G.iconsSize,java.awt.Image.SCALE_SMOOTH)));
            setToolBar(toolBar);
        }
        catch (Exception e) { System.out.println(e); }

        textZone.setBackground(Color.WHITE); // Needed for Unix...
        aboutDialogImage.setIcon(new ImageIcon("resources/logo.png"));
        G.documentLanguage = G.defaultDocumentLanguage;
        G.imagesSize = G.defaultImagesSize;
        G.font = new Font(G.defaultFont.getName(),G.defaultFont.getStyle(),G.defaultFont.getSize());
        G.color = new Color(G.defaultColor.getRed(),G.defaultColor.getBlue(),G.defaultColor.getGreen());
        G.textBelowPictogram = G.defaultTextBelowPictogram;
        G.notFound = new ImageIcon(new ImageIcon("resources/404.jpg").getImage().getScaledInstance(-1,G.imagesSize,0));

        setApplicationLanguage();

        // Keyboard shortcuts for menu actions.
        menuFileNew.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N,java.awt.Event.CTRL_MASK));
        menuFileOpen.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O,java.awt.Event.CTRL_MASK));
        menuFileSave.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S,java.awt.Event.CTRL_MASK));
        menuFileExit.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q,java.awt.Event.CTRL_MASK));

        menuEditCut.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X,java.awt.Event.CTRL_MASK));
        menuEditCopy.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C,java.awt.Event.CTRL_MASK));
        menuEditPaste.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V,java.awt.Event.CTRL_MASK));
        menuEditUndo.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z,java.awt.Event.CTRL_MASK));
        menuEditRedo.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y,java.awt.Event.CTRL_MASK));
        menuEditFind.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F,java.awt.Event.CTRL_MASK));
        menuEditSelectAll.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E,java.awt.Event.CTRL_MASK));

        menuPictogramsNextImage.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3,0));
        menuPictogramsCompoundSplitWord.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4,0));
        menuPictogramsChangeName.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5,0));
        menuPictogramsInsertImage.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6,0));
        menuVoiceSintesys.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F7,0));

        menuHelpShowHelp.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1,0));

        this.getFrame().setTitle("AraWord");
        this.getFrame().pack();
        this.getFrame().setIconImage(Toolkit.getDefaultToolkit().getImage("resources/logo.png")); // Application logo
        // Little trick to allow modulation without breaking possibility of easy
        // GUI development and change.
        G.giveMePrivateVariables(textZone);
        try {
            DBManagement.connectDB();
            DBManagement.connectVerbsDB();
            DBManagement.createAraWordView(G.documentLanguage);
            TextUtils.newDocument();
        }
        catch (Exception exc) {System.out.println(exc);}

        // Catch event "window closing" from big red X on Windows XP.
        // This way, users don't lose their document accidentally.
        this.getFrame().setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        this.getFrame().addWindowListener( new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                //############### hacer que permitar guardar al salir ##################
                if (JOptionPane.showConfirmDialog(getFrame(),TLanguage.getString("FILE_MENU_EXIT_WARNING"),
                        TLanguage.getString("WARNING"),JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    System.exit(0);
                }
            }
        });
    }

    private void setApplicationLanguage() {
        menuFile.setText(TLanguage.getString("FILE_MENU"));
        menuFileNew.setText(TLanguage.getString("FILE_MENU_NEW"));
        menuFileOpen.setText(TLanguage.getString("FILE_MENU_LOAD"));
        menuFileSave.setText(TLanguage.getString("FILE_MENU_SAVE"));
        menuFileSaveAs.setText(TLanguage.getString("FILE_MENU_SAVE_AS"));
        menuFileExport.setText(TLanguage.getString("FILE_MENU_EXPORT"));
        menuFileExportImage.setText(TLanguage.getString("FILE_MENU_EXPORT_IMAGE"));
        menuFileExportPDF.setText(TLanguage.getString("FILE_MENU_EXPORT_PDF"));
        menuFileExit.setText(TLanguage.getString("FILE_MENU_EXIT"));

        menuEdit.setText(TLanguage.getString("EDIT_MENU"));
        menuEditCut.setText(TLanguage.getString("EDIT_MENU_CUT"));
        menuEditCopy.setText(TLanguage.getString("EDIT_MENU_COPY"));
        menuEditPaste.setText(TLanguage.getString("EDIT_MENU_PASTE"));
        menuEditUndo.setText(TLanguage.getString("EDIT_MENU_UNDO"));
        menuEditRedo.setText(TLanguage.getString("EDIT_MENU_REDO"));
        menuEditFind.setText(TLanguage.getString("EDIT_MENU_FIND"));
        menuEditSelectAll.setText(TLanguage.getString("EDIT_MENU_SELECT_ALL"));

        menuText.setText(TLanguage.getString("TEXT_MENU"));
        menuTextFont.setText(TLanguage.getString("TEXT_MENU_FONT"));
        menuTextColor.setText(TLanguage.getString("TEXT_MENU_COLOR"));
        menuTextPlacement.setText(TLanguage.getString("TEXT_MENU_PLACEMENT"));
        menuTextPlacementAbovePictogram.setText(TLanguage.getString("TEXT_MENU_PLACEMENT_ABOVE_PICTOGRAM"));
        menuTextPlacementBelowPictogram.setText(TLanguage.getString("TEXT_MENU_PLACEMENT_BELOW_PICTOGRAM"));
        menuTextToUpperCase.setText(TLanguage.getString("TEXT_MENU_TO_UPPER_CASE"));
        menuTextToUpperCaseActiveElement.setText(TLanguage.getString("TEXT_MENU_TO_UPPER_CASE_ACTIVE_ELEMENT"));
        menuTextToUpperCaseAllElements.setText(TLanguage.getString("TEXT_MENU_TO_UPPER_CASE_ALL_ELEMENTS"));
        menuTextToLowerCase.setText(TLanguage.getString("TEXT_MENU_TO_LOWER_CASE"));
        menuTextToLowerCaseActiveElement.setText(TLanguage.getString("TEXT_MENU_TO_LOWER_CASE_ACTIVE_ELEMENT"));
        menuTextToLowerCaseAllElements.setText(TLanguage.getString("TEXT_MENU_TO_LOWER_CASE_ALL_ELEMENTS"));
        menuTextDocumentLanguage.setText(TLanguage.getString("TEXT_MENU_DOCUMENT_LANGUAGE"));

        menuPictograms.setText(TLanguage.getString("PICTOGRAMS_MENU"));
        menuPictogramsSize.setText(TLanguage.getString("PICTOGRAMS_MENU_SIZE"));
        menuPictogramsNextImage.setText(TLanguage.getString("PICTOGRAMS_MENU_NEXT_IMAGE"));
        menuPictogramsCompoundSplitWord.setText(TLanguage.getString("PICTOGRAMS_MENU_COMPOUND_SPLIT_WORD"));
        menuPictogramsChangeName.setText(TLanguage.getString("PICTOGRAMS_MENU_CHANGE_NAME"));
        menuPictogramsInsertImage.setText(TLanguage.getString("TOOLS_MENU_RESOURCE_MANAGER"));
        menuVoiceSintesys.setText(TLanguage.getString("TOOLS_MENU_VOICE_SINTESYS"));
        menuPictogramsHide.setText(TLanguage.getString("PICTOGRAMS_MENU_HIDE"));
        menuPictogramsHideBorder.setText(TLanguage.getString("PICTOGRAMS_MENU_HIDE_BORDER"));
        menuPictogramsHideBorderActiveElement.setText(TLanguage.getString("PICTOGRAMS_MENU_HIDE_BORDER_ACTIVE_ELEMENT"));
        menuPictogramsHideBorderAllElements.setText(TLanguage.getString("PICTOGRAMS_MENU_HIDE_BORDER_ALL_ELEMENTS"));
        menuPictogramsHideImage.setText(TLanguage.getString("PICTOGRAMS_MENU_HIDE_IMAGE"));
        menuPictogramsHideImageActiveElement.setText(TLanguage.getString("PICTOGRAMS_MENU_HIDE_IMAGE_ACTIVE_ELEMENT"));
        menuPictogramsHideImageAllElements.setText(TLanguage.getString("PICTOGRAMS_MENU_HIDE_IMAGE_ALL_ELEMENTS"));
        menuPictogramsShow.setText(TLanguage.getString("PICTOGRAMS_MENU_SHOW"));
        menuPictogramsShowBorder.setText(TLanguage.getString("PICTOGRAMS_MENU_SHOW_BORDER"));
        menuPictogramsShowBorderActiveElement.setText(TLanguage.getString("PICTOGRAMS_MENU_SHOW_BORDER_ACTIVE_ELEMENT"));
        menuPictogramsShowBorderAllElements.setText(TLanguage.getString("PICTOGRAMS_MENU_SHOW_BORDER_ALL_ELEMENTS"));
        menuPictogramsShowImage.setText(TLanguage.getString("PICTOGRAMS_MENU_SHOW_IMAGE"));
        menuPictogramsShowImageActiveElement.setText(TLanguage.getString("PICTOGRAMS_MENU_SHOW_IMAGE_ACTIVE_ELEMENT"));
        menuPictogramsShowImageAllElements.setText(TLanguage.getString("PICTOGRAMS_MENU_SHOW_IMAGE_ALL_ELEMENTS"));

        menuTools.setText(TLanguage.getString("TOOLS_MENU"));
        menuToolsResourceManager.setText(TLanguage.getString("TOOLS_MENU_RESOURCE_MANAGER"));

        menuToolsGeneralPreferences.setText(TLanguage.getString("TOOLS_MENU_GENERAL_PREFERENCES"));

        menuHelp.setText(TLanguage.getString("HELP_MENU"));
        menuHelpShowHelp.setText(TLanguage.getString("HELP_MENU_SHOW_HELP"));
        menuHelpAbout.setText(TLanguage.getString("HELP_MENU_ABOUT"));

        generalPreferencesDialog.setTitle(TLanguage.getString("GENERAL_PREFERENCES_DIALOG_TITLE"));
        generalPreferencesDialogApplicationLanguageLabel.setText(TLanguage.getString("GENERAL_PREFERENCES_DIALOG_APPLICATION_LANGUAGE_LABEL"));
        generalPreferencesDialogDocumentLanguageLabel.setText(TLanguage.getString("GENERAL_PREFERENCES_DIALOG_DOCUMENT_LANGUAGE_LABEL"));
        generalPreferencesDialogImagesSizeLabel.setText(TLanguage.getString("GENERAL_PREFERENCES_DIALOG_IMAGES_SIZE_LABEL"));
//    	generalPreferencesDialogMaxLengthCompoundWordsLabel.setText(TLanguage.getString("GENERAL_PREFERENCES_DIALOG_MAX_LENGTH_COMPOUND_WORDS_LABEL"));
//    	generalPreferencesDialogMaxUndoLevelLabel.setText(TLanguage.getString("GENERAL_PREFERENCES_DIALOG_MAX_UNDO_LEVEL_LABEL"));
//    	generalPreferencesDialogPictogramsPathLabel.setText(TLanguage.getString("GENERAL_PREFERENCES_DIALOG_PICTOGRAMS_PATH_LABEL"));
//    	generalPreferencesDialogChoosePictogramsPathButton.setText(TLanguage.getString("GENERAL_PREFERENCES_DIALOG_PICTOGRAMS_PATH_CHOOSE_BUTTON"));
        generalPreferencesDialogTextFontLabel.setText(TLanguage.getString("GENERAL_PREFERENCES_DIALOG_TEXT_FONT_LABEL"));
        generalPreferencesDialogChooseTextFontButton.setText(TLanguage.getString("GENERAL_PREFERENCES_DIALOG_TEXT_FONT_CHOOSE_BUTTON"));
        generalPreferencesDialogTextColorLabel.setText(TLanguage.getString("GENERAL_PREFERENCES_DIALOG_TEXT_COLOR_LABEL"));
        generalPreferencesDialogChooseTextColorButton.setText(TLanguage.getString("GENERAL_PREFERENCES_DIALOG_TEXT_COLOR_CHOOSE_BUTTON"));
        generalPreferencesDialogTextPlacementLabel.setText(TLanguage.getString("GENERAL_PREFERENCES_DIALOG_TEXT_PLACEMENT_LABEL"));
        generalPreferencesDialogOKButton.setText(TLanguage.getString("OK"));
        generalPreferencesDialogCancelButton.setText(TLanguage.getString("CANCEL"));

        documentLanguageDialog.setTitle(TLanguage.getString("DOCUMENT_LANGUAGE_DIALOG_TITLE"));
        documentLanguageDialogDocumentLanguageLabel.setText(TLanguage.getString("DOCUMENT_LANGUAGE_DIALOG_DOCUMENT_LANGUAGE_LABEL"));
        documentLanguageDialogOKButton.setText(TLanguage.getString("OK"));
        documentLanguageDialogCancelButton.setText(TLanguage.getString("CANCEL"));

        imagesSizeDialog.setTitle(TLanguage.getString("IMAGES_SIZE_DIALOG_TITLE"));
        imagesSizeDialogImagesSizeLabel.setText(TLanguage.getString("IMAGES_SIZE_DIALOG_IMAGES_SIZE_LABEL"));
        imagesSizeDialogOKButton.setText(TLanguage.getString("OK"));
        imagesSizeDialogCancelButton.setText(TLanguage.getString("CANCEL"));

        exportPDFDialog.setTitle(TLanguage.getString("EXPORT_PDF_DIALOG_TITLE"));
        exportPDFDialogExportButton.setText(TLanguage.getString("EXPORT_PDF_BUTTON"));
        exportPDFDialogCancelButton.setText(TLanguage.getString("CANCEL"));
        exportPDFDialogPictosLineLabel.setText(TLanguage.getString("EXPORT_PDF_PICTOS_LINE"));
        exportPDFDialogArasaacLicenseCB.setText(TLanguage.getString("EXPORT_PDF_ARASAAC_LICENSE"));
        exportPDFDialogPagLabel.setText(TLanguage.getString("EXPORT_PDF_PAGE"));
        exportPDFDialogPrevLabel.setText(TLanguage.getString("EXPORT_PDF_PREVISUALIZATION"));
        //exportPDFDialogPrevArea.setText("AREA");

        aboutDialog.setTitle(TLanguage.getString("ABOUT_DIALOG_TITLE"));
        aboutDialogCloseButton.setText(TLanguage.getString("CLOSE"));
        String str = "";
        str = "******* AraWord 1.0.5 *******\n\n";
        str = str + "--- " + TLanguage.getString("TAboutDialog.DEVELOPERS") + " ---\n";
        str = str + "# Adrián Gómez\n";
        str = str + "# Joaquín Pérez\n";
        str = str + "--- " + TLanguage.getString("TAboutDialog.DIRECTOR") + " ---\n";
        str = str + "# Dr. Joaquín Ezpeleta (EINA)\n";
        str = str + "--- " + TLanguage.getString("TAboutDialog.COLLABORATORS") + " ---\n";
        str = str + "# José Manuel Marcos (CPEE Alborada)\n";
        str = str + "# David Romero (ARASAAC)\n";
        str = str + "--- " + TLanguage.getString("TAboutDialog.ORGANIZATIONS") + " ---\n";
        str = str + "# Escuela de Ingeniería y Arquitectura (EINA)\n";
        str = str + "# Universidad de Zaragoza (UZ)\n";
        str = str + "# Colegio Público de Educación Especial Alborada (CPEE Alborada)\n";
        str = str + "# Portal Aragonés de la Comunicación Aumentativa y Alternativa (ARASAAC)\n";
        str = str + "--- " + TLanguage.getString("TAboutDialog.YEAR") + " 2013 ---\n";
        str = str + "--- " + TLanguage.getString("TAboutDialog.LICENSE") + " GPL v3 ---\n\n";
        str = str + "******* Pictogramas ARASAAC (http://arasaac.org) *******\n\n";
        str = str + "# Autor: Sergio Palao\n";
        str = str + "# Licencia: Creative Commons (BY-NC-SA)";

        aboutDialogTextArea.setText(str);

        toolBarButtonFileNew.setToolTipText(menuFileNew.getText());
        toolBarButtonFileOpen.setToolTipText(menuFileOpen.getText());
        toolBarButtonFileSave.setToolTipText(menuFileSave.getText());
        toolBarButtonEditUndo.setToolTipText(menuEditUndo.getText());
        toolBarButtonEditRedo.setToolTipText(menuEditRedo.getText());
        toolBarButtonEditCut.setToolTipText(menuEditCut.getText());
        toolBarButtonEditCopy.setToolTipText(menuEditCopy.getText());
        toolBarButtonEditPaste.setToolTipText(menuEditPaste.getText());
        toolBarButtonPictogramsNextImage.setToolTipText(menuPictogramsNextImage.getText());
        toolBarButtonPictogramsCompoundSplitWord.setToolTipText(menuPictogramsCompoundSplitWord.getText());
        toolBarButtonPictogramsChangeName.setToolTipText(menuPictogramsChangeName.getText());
        toolBarButtonPictogramsInsertImage.setToolTipText(menuPictogramsInsertImage.getText());
        toolBarButtonVoiceSintesys.setToolTipText(menuVoiceSintesys.getText());

        // Forgot that!!

        // In future, it could not be "fixed" but instead depend of language chosen:
        // Castellano --> Castellano, Inglés, Francés...
        // English --> Spanish, English, French...
        SpinnerModel model = new SpinnerListModel(G.applicationLanguages);
        ((DefaultComboBoxModel)generalPreferencesDialogSpinnerApplicationLanguage.getModel()).removeAllElements();
        for (int i = 0; i < G.applicationLanguages.length; i++) {
            generalPreferencesDialogSpinnerApplicationLanguage.addItem(G.applicationLanguages[i]);
        }

        ((DefaultComboBoxModel)generalPreferencesDialogSpinnerDocumentLanguage.getModel()).removeAllElements();
        for (int i = 0; i < G.documentLanguages.length; i++) {
            generalPreferencesDialogSpinnerDocumentLanguage.addItem(G.documentLanguages[i]);
        }

//		SpinnerModel model3 = new SpinnerListModel(G.documentLanguages);
//		documentLanguageDialogSpinnerDocumentLanguage.setModel(model3);
		for (int i = 0; i < G.documentLanguages.length; i++) {
			documentLanguageDialogComboBoxDocumentLanguage.addItem(G.documentLanguages[i]);
		}
		
		String textPlacement[] = new String[]{TLanguage.getString("SPINNER_TEXT_BELOW_PICTOGRAM"),TLanguage.getString("SPINNER_TEXT_ABOVE_PICTOGRAM")};
		if(generalPreferencesDialogSpinnerTextPlacement.getItemCount() == 0)
			for (int i = 0; i < textPlacement.length; i++) {			
				generalPreferencesDialogSpinnerTextPlacement.addItem(textPlacement[i]);
			}
		
		generalPreferencesDialogSpinnerImagesSize.setValue(G.defaultImagesSize);
//    	generalPreferencesDialogSpinnerMaxLengthCompoundWords.setValue(G.maxLengthCompoundWords);
//    	generalPreferencesDialogSpinnerMaxUndoLevel.setValue(G.maxUndoLevel);
        generalPreferencesDialogSpinnerDocumentLanguage.setSelectedItem(G.defaultDocumentLanguage);
        generalPreferencesDialogSpinnerApplicationLanguage.setSelectedItem(G.applicationLanguage);
        if (G.textBelowPictogram) {
            generalPreferencesDialogSpinnerTextPlacement.setSelectedItem(TLanguage.getString("SPINNER_TEXT_BELOW_PICTOGRAM"));
        } else {
            generalPreferencesDialogSpinnerTextPlacement.setSelectedItem(TLanguage.getString("SPINNER_TEXT_ABOVE_PICTOGRAM"));
        }

        documentLanguageDialogComboBoxDocumentLanguage.setSelectedItem(G.documentLanguage);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        scrollTextZone = new javax.swing.JScrollPane();
        textZone = new javax.swing.JTextPane();
        menuBar = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuFileNew = new javax.swing.JMenuItem();
        menuFileOpen = new javax.swing.JMenuItem();
        menuFileSave = new javax.swing.JMenuItem();
        menuFileSaveAs = new javax.swing.JMenuItem();
        menuFileExport = new javax.swing.JMenu();
        menuFileExportImage = new javax.swing.JMenuItem();
        menuFileExportPDF = new javax.swing.JMenuItem();
        menuFileExit = new javax.swing.JMenuItem();
        menuEdit = new javax.swing.JMenu();
        menuEditUndo = new javax.swing.JMenuItem();
        menuEditRedo = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuEditCut = new javax.swing.JMenuItem();
        menuEditCopy = new javax.swing.JMenuItem();
        menuEditPaste = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        menuEditFind = new javax.swing.JMenuItem();
        menuEditSelectAll = new javax.swing.JMenuItem();
        menuText = new javax.swing.JMenu();
        menuTextFont = new javax.swing.JMenuItem();
        menuTextColor = new javax.swing.JMenuItem();
        menuTextPlacement = new javax.swing.JMenu();
        menuTextPlacementAbovePictogram = new javax.swing.JMenuItem();
        menuTextPlacementBelowPictogram = new javax.swing.JMenuItem();
        menuTextToUpperCase = new javax.swing.JMenu();
        menuTextToUpperCaseActiveElement = new javax.swing.JMenuItem();
        menuTextToUpperCaseAllElements = new javax.swing.JMenuItem();
        menuTextToLowerCase = new javax.swing.JMenu();
        menuTextToLowerCaseActiveElement = new javax.swing.JMenuItem();
        menuTextToLowerCaseAllElements = new javax.swing.JMenuItem();
        menuTextDocumentLanguage = new javax.swing.JMenuItem();
        menuPictograms = new javax.swing.JMenu();
        menuPictogramsSize = new javax.swing.JMenuItem();
        menuPictogramsNextImage = new javax.swing.JMenuItem();
        menuPictogramsCompoundSplitWord = new javax.swing.JMenuItem();
        menuPictogramsChangeName = new javax.swing.JMenuItem();
        menuPictogramsInsertImage = new javax.swing.JMenuItem();
        menuVoiceSintesys = new javax.swing.JMenuItem();
        menuPictogramsShow = new javax.swing.JMenu();
        menuPictogramsShowImage = new javax.swing.JMenu();
        menuPictogramsShowImageActiveElement = new javax.swing.JMenuItem();
        menuPictogramsShowImageAllElements = new javax.swing.JMenuItem();
        menuPictogramsShowBorder = new javax.swing.JMenu();
        menuPictogramsShowBorderActiveElement = new javax.swing.JMenuItem();
        menuPictogramsShowBorderAllElements = new javax.swing.JMenuItem();
        menuPictogramsHide = new javax.swing.JMenu();
        menuPictogramsHideImage = new javax.swing.JMenu();
        menuPictogramsHideImageActiveElement = new javax.swing.JMenuItem();
        menuPictogramsHideImageAllElements = new javax.swing.JMenuItem();
        menuPictogramsHideBorder = new javax.swing.JMenu();
        menuPictogramsHideBorderActiveElement = new javax.swing.JMenuItem();
        menuPictogramsHideBorderAllElements = new javax.swing.JMenuItem();
        menuTools = new javax.swing.JMenu();
        menuToolsResourceManager = new javax.swing.JMenuItem();

        menuToolsGeneralPreferences = new javax.swing.JMenuItem();
        menuHelp = new javax.swing.JMenu();
        menuHelpShowHelp = new javax.swing.JMenuItem();
        menuHelpAbout = new javax.swing.JMenuItem();
        aboutDialog = new javax.swing.JDialog();
        aboutDialogCloseButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        aboutDialogTextArea = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        aboutDialogImage = new javax.swing.JLabel();
        findDialog = new javax.swing.JDialog();
        findDialogFindTextLabel = new javax.swing.JLabel();
        findDialogTextField = new javax.swing.JTextField();
        findDialogFindButton = new javax.swing.JButton();
        findDialogExitButton = new javax.swing.JButton();
        generalPreferencesDialog = new javax.swing.JDialog();
        generalPreferencesDialogSpinnerImagesSize = new javax.swing.JSpinner();
        generalPreferencesDialogImagesSizeLabel = new javax.swing.JLabel();
//        generalPreferencesDialogMaxUndoLevelLabel = new javax.swing.JLabel();
//        generalPreferencesDialogSpinnerMaxUndoLevel = new javax.swing.JSpinner();
//        generalPreferencesDialogMaxLengthCompoundWordsLabel = new javax.swing.JLabel();
//        generalPreferencesDialogSpinnerMaxLengthCompoundWords = new javax.swing.JSpinner();
        generalPreferencesDialogApplicationLanguageLabel = new javax.swing.JLabel();
        generalPreferencesDialogSpinnerApplicationLanguage = new javax.swing.JComboBox();
        generalPreferencesDialogDocumentLanguageLabel = new javax.swing.JLabel();
        generalPreferencesDialogSpinnerDocumentLanguage = new javax.swing.JComboBox();
        generalPreferencesDialogOKButton = new javax.swing.JButton();
        generalPreferencesDialogCancelButton = new javax.swing.JButton();
//        generalPreferencesDialogPictogramsPathLabel = new javax.swing.JLabel();
//        generalPreferencesDialogChoosePictogramsPathButton = new javax.swing.JButton();
        generalPreferencesDialogTextFontLabel = new javax.swing.JLabel();
        generalPreferencesDialogChooseTextFontButton = new javax.swing.JButton();
        generalPreferencesDialogTextColorLabel = new javax.swing.JLabel();
        generalPreferencesDialogChooseTextColorButton = new javax.swing.JButton();
        generalPreferencesDialogTextPlacementLabel = new javax.swing.JLabel();
        generalPreferencesDialogSpinnerTextPlacement = new javax.swing.JComboBox();
        toolBar = new javax.swing.JToolBar();
        toolBarButtonFileNew = new javax.swing.JButton();
        toolBarButtonFileOpen = new javax.swing.JButton();
        toolBarButtonFileSave = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        toolBarButtonEditUndo = new javax.swing.JButton();
        toolBarButtonEditRedo = new javax.swing.JButton();
        toolBarButtonEditCut = new javax.swing.JButton();
        toolBarButtonEditCopy = new javax.swing.JButton();
        toolBarButtonEditPaste = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        toolBarButtonPictogramsNextImage = new javax.swing.JButton();
        toolBarButtonPictogramsCompoundSplitWord = new javax.swing.JButton();
        toolBarButtonPictogramsChangeName = new javax.swing.JButton();
        toolBarButtonPictogramsInsertImage = new javax.swing.JButton();
        toolBarButtonVoiceSintesys = new javax.swing.JButton();
        documentLanguageDialog = new javax.swing.JDialog();
        documentLanguageDialogDocumentLanguageLabel = new javax.swing.JLabel();
        documentLanguageDialogComboBoxDocumentLanguage = new JComboBox();
        documentLanguageDialogOKButton = new javax.swing.JButton();
        documentLanguageDialogCancelButton = new javax.swing.JButton();
        imagesSizeDialog = new javax.swing.JDialog();
        imagesSizeDialogImagesSizeLabel = new javax.swing.JLabel();
        imagesSizeDialogSpinnerImagesSize = new javax.swing.JSpinner();
        imagesSizeDialogCancelButton = new javax.swing.JButton();
        imagesSizeDialogOKButton = new javax.swing.JButton();

        //export PDF dialog window
        exportPDFDialog = new javax.swing.JDialog();
        exportPDFDialog.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                G.imagesSize=G.imagesSizePDF;
            }
        });
        exportPDFDialogCancelButton = new javax.swing.JButton();
        exportPDFDialogExportButton = new javax.swing.JButton();
        exportPDFDialogPictosLineLabel = new javax.swing.JLabel();
        exportPDFDialogSpinnerPictosLine = new javax.swing.JSpinner();
        exportPDFDialogPagSpinner = new javax.swing.JSpinner();
        exportPDFDialogArasaacLicenseCB = new javax.swing.JCheckBox();
        exportPDFDialogPagLabel = new javax.swing.JLabel();
        exportPDFDialogPrevLabel = new javax.swing.JLabel();
        exportPDFDialogPrevArea = new javax.swing.JTextPane();

        mainPanel.setName("mainPanel"); // NOI18N

        scrollTextZone.setName("scrollTextZone"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(araword.AraWordApp.class).getContext().getResourceMap(GUI.class);
        textZone.setContentType(resourceMap.getString("textZone.contentType")); // NOI18N
        textZone.setEditable(false);
        textZone.setFocusable(false);
        textZone.setHighlighter(null);
        textZone.setMargin(new java.awt.Insets(10, 10, 10, 10));
        textZone.setName("textZone"); // NOI18N
        textZone.setRequestFocusEnabled(false);
        scrollTextZone.setViewportView(textZone);

        org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(mainPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .add(scrollTextZone, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE)
                                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(mainPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .add(scrollTextZone, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
                                .addContainerGap())
        );

        menuBar.setName("menuBar"); // NOI18N

        menuFile.setText(resourceMap.getString("menuFile.text")); // NOI18N
        menuFile.setName("menuFile"); // NOI18N

        menuFileNew.setText(resourceMap.getString("menuFileNew.text")); // NOI18N
        menuFileNew.setName("menuFileNew"); // NOI18N
        menuFileNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFileNewActionPerformed(evt);
            }
        });
        menuFile.add(menuFileNew);

        menuFileOpen.setText(resourceMap.getString("menuFileOpen.text")); // NOI18N
        menuFileOpen.setName("menuFileOpen"); // NOI18N
        menuFileOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFileOpenActionPerformed(evt);
            }
        });
        menuFile.add(menuFileOpen);

        menuFileSave.setText(resourceMap.getString("menuFileSave.text")); // NOI18N
        menuFileSave.setName("menuFileSave"); // NOI18N
        menuFileSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFileSaveActionPerformed(evt);
            }
        });
        menuFile.add(menuFileSave);

        menuFileSaveAs.setText(resourceMap.getString("menuFileSaveAs.text")); // NOI18N
        menuFileSaveAs.setName("menuFileSaveAs"); // NOI18N
        menuFileSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFileSaveAsActionPerformed(evt);
            }
        });
        menuFile.add(menuFileSaveAs);

        menuFileExport.setText(resourceMap.getString("menuFileExport.text")); // NOI18N
        menuFileExport.setName("menuFileExport"); // NOI18N
        menuFileExportImage.setText(resourceMap.getString("menuFileExportImage.text")); // NOI18N
        menuFileExportImage.setName("menuFileExportImage"); // NOI18N
        menuFileExportImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFileExportActionPerformed(evt);
            }
        });
        menuFileExport.add(menuFileExportImage);

        menuFileExportPDF.setText(resourceMap.getString("menuFileExportPDF.text")); // NOI18N
        menuFileExportPDF.setName("menuFileExportPDF"); // NOI18N
        menuFileExportPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFileExportPDFActionPerformed(evt);
            }
        });
        menuFileExport.add(menuFileExportPDF);

        menuFile.add(menuFileExport);

        menuFileExit.setText(resourceMap.getString("menuFileExit.text")); // NOI18N
        menuFileExit.setName("menuFileExit"); // NOI18N




        menuFileExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFileExitActionPerformed(evt);
            }
        });
        menuFile.add(menuFileExit);

        menuBar.add(menuFile);

        menuEdit.setText(resourceMap.getString("menuEdit.text")); // NOI18N
        menuEdit.setName("menuEdit"); // NOI18N

        menuEditUndo.setText(resourceMap.getString("menuEditUndo.text")); // NOI18N
        menuEditUndo.setName("menuEditUndo"); // NOI18N
        menuEditUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEditUndoActionPerformed(evt);
            }
        });
        menuEdit.add(menuEditUndo);

        menuEditRedo.setText(resourceMap.getString("menuEditRedo.text")); // NOI18N
        menuEditRedo.setName("menuEditRedo"); // NOI18N
        menuEditRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEditRedoActionPerformed(evt);
            }
        });
        menuEdit.add(menuEditRedo);

        jSeparator1.setName("jSeparator1"); // NOI18N
        menuEdit.add(jSeparator1);

        menuEditCut.setText(resourceMap.getString("menuEditCut.text")); // NOI18N
        menuEditCut.setName("menuEditCut"); // NOI18N
        menuEditCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEditCutActionPerformed(evt);
            }
        });
        menuEdit.add(menuEditCut);

        menuEditCopy.setText(resourceMap.getString("menuEditCopy.text")); // NOI18N
        menuEditCopy.setName("menuEditCopy"); // NOI18N
        menuEditCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEditCopyActionPerformed(evt);
            }
        });
        menuEdit.add(menuEditCopy);

        menuEditPaste.setText(resourceMap.getString("menuEditPaste.text")); // NOI18N
        menuEditPaste.setName("menuEditPaste"); // NOI18N
        menuEditPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEditPasteActionPerformed(evt);
            }
        });
        menuEdit.add(menuEditPaste);

        jSeparator2.setName("jSeparator2"); // NOI18N
        menuEdit.add(jSeparator2);

        menuEditFind.setText(resourceMap.getString("menuEditFind.text")); // NOI18N
        menuEditFind.setName("menuEditFind"); // NOI18N
        menuEditFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEditFindActionPerformed(evt);
            }
        });
        menuEdit.add(menuEditFind);

        menuEditSelectAll.setText(resourceMap.getString("menuEditSelectAll.text")); // NOI18N
        menuEditSelectAll.setName("menuEditSelectAll"); // NOI18N
        menuEditSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEditSelectAllActionPerformed(evt);
            }
        });
        menuEdit.add(menuEditSelectAll);

        menuBar.add(menuEdit);

        menuText.setText(resourceMap.getString("menuText.text")); // NOI18N
        menuText.setName("menuText"); // NOI18N

        menuTextFont.setText(resourceMap.getString("menuTextFont.text")); // NOI18N
        menuTextFont.setName("menuTextFont"); // NOI18N
        menuTextFont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTextFontActionPerformed(evt);
            }
        });
        menuText.add(menuTextFont);

        menuTextColor.setText(resourceMap.getString("menuTextColor.text")); // NOI18N
        menuTextColor.setName("menuTextColor"); // NOI18N
        menuTextColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTextColorActionPerformed(evt);
            }
        });
        menuText.add(menuTextColor);

        menuTextPlacement.setText(resourceMap.getString("menuTextPlacement.text")); // NOI18N
        menuTextPlacement.setName("menuTextPlacement"); // NOI18N

        menuTextPlacementAbovePictogram.setText(resourceMap.getString("menuTextPlacementAbovePictogram.text")); // NOI18N
        menuTextPlacementAbovePictogram.setName("menuTextPlacementAbovePictogram"); // NOI18N
        menuTextPlacementAbovePictogram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTextPlacementAbovePictogramActionPerformed(evt);
            }
        });
        menuTextPlacement.add(menuTextPlacementAbovePictogram);

        menuTextPlacementBelowPictogram.setText(resourceMap.getString("menuTextPlacementBelowPictogram.text")); // NOI18N
        menuTextPlacementBelowPictogram.setName("menuTextPlacementBelowPictogram"); // NOI18N
        menuTextPlacementBelowPictogram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTextPlacementBelowPictogramActionPerformed(evt);
            }
        });
        menuTextPlacement.add(menuTextPlacementBelowPictogram);

        menuText.add(menuTextPlacement);

        menuTextToUpperCase.setText(resourceMap.getString("menuTextToUpperCase.text")); // NOI18N
        menuTextToUpperCase.setName("menuTextToUpperCase"); // NOI18N

        menuTextToUpperCaseActiveElement.setText(resourceMap.getString("menuTextToUpperCaseActiveElement.text")); // NOI18N
        menuTextToUpperCaseActiveElement.setName("menuTextToUpperCaseActiveElement"); // NOI18N
        menuTextToUpperCaseActiveElement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTextToUpperCaseActiveElementActionPerformed(evt);
            }
        });
        menuTextToUpperCase.add(menuTextToUpperCaseActiveElement);

        menuTextToUpperCaseAllElements.setText(resourceMap.getString("menuTextToUpperCaseAllElements.text")); // NOI18N
        menuTextToUpperCaseAllElements.setName("menuTextToUpperCaseAllElements"); // NOI18N
        menuTextToUpperCaseAllElements.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTextToUpperCaseAllElementsActionPerformed(evt);
            }
        });
        menuTextToUpperCase.add(menuTextToUpperCaseAllElements);

        menuText.add(menuTextToUpperCase);

        menuTextToLowerCase.setText(resourceMap.getString("menuTextToLowerCase.text")); // NOI18N
        menuTextToLowerCase.setName("menuTextToLowerCase"); // NOI18N

        menuTextToLowerCaseActiveElement.setText(resourceMap.getString("menuTextToLowerCaseActiveElement.text")); // NOI18N
        menuTextToLowerCaseActiveElement.setName("menuTextToLowerCaseActiveElement"); // NOI18N
        menuTextToLowerCaseActiveElement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTextToLowerCaseActiveElementActionPerformed(evt);
            }
        });
        menuTextToLowerCase.add(menuTextToLowerCaseActiveElement);

        menuTextToLowerCaseAllElements.setText(resourceMap.getString("menuTextToLowerCaseAllElements.text")); // NOI18N
        menuTextToLowerCaseAllElements.setName("menuTextToLowerCaseAllElements"); // NOI18N
        menuTextToLowerCaseAllElements.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTextToLowerCaseAllElementsActionPerformed(evt);
            }
        });
        menuTextToLowerCase.add(menuTextToLowerCaseAllElements);

        menuText.add(menuTextToLowerCase);

        menuTextDocumentLanguage.setText(resourceMap.getString("menuTextDocumentLanguage.text")); // NOI18N
        menuTextDocumentLanguage.setName("menuTextDocumentLanguage"); // NOI18N
        menuTextDocumentLanguage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTextDocumentLanguageActionPerformed(evt);
            }
        });
        menuText.add(menuTextDocumentLanguage);

        menuBar.add(menuText);

        menuPictograms.setText(resourceMap.getString("menuPictograms.text")); // NOI18N
        menuPictograms.setName("menuPictograms"); // NOI18N

        menuPictogramsSize.setText(resourceMap.getString("menuPictogramsSize.text")); // NOI18N
        menuPictogramsSize.setName("menuPictogramsSize"); // NOI18N
        menuPictogramsSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPictogramsSizeActionPerformed(evt);
            }
        });
        menuPictograms.add(menuPictogramsSize);

        menuPictogramsNextImage.setText(resourceMap.getString("menuPictogramsNextImage.text")); // NOI18N
        menuPictogramsNextImage.setName("menuPictogramsNextImage"); // NOI18N
        menuPictogramsNextImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPictogramsNextImageActionPerformed(evt);
            }
        });
        menuPictograms.add(menuPictogramsNextImage);

        menuPictogramsCompoundSplitWord.setText(resourceMap.getString("menuPictogramsCompoundSplitWord.text")); // NOI18N
        menuPictogramsCompoundSplitWord.setName("menuPictogramsCompoundSplitWord"); // NOI18N
        menuPictogramsCompoundSplitWord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPictogramsCompoundSplitWordActionPerformed(evt);
            }
        });
        menuPictograms.add(menuPictogramsCompoundSplitWord);

        menuPictogramsChangeName.setText(resourceMap.getString("menuPictogramsChangeName.text")); // NOI18N
        menuPictogramsChangeName.setName("menuPictogramsChangeName"); // NOI18N
        menuPictogramsChangeName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPictogramsChangeNameActionPerformed(evt);
            }
        });
        menuPictograms.add(menuPictogramsChangeName);

        menuPictogramsShow.setText(resourceMap.getString("menuPictogramsShow.text")); // NOI18N
        menuPictogramsShow.setName("menuPictogramsShow"); // NOI18N

        menuPictogramsShowImage.setText(resourceMap.getString("menuPictogramsShowImage.text")); // NOI18N
        menuPictogramsShowImage.setName("menuPictogramsShowImage"); // NOI18N

        menuPictogramsShowImageActiveElement.setText(resourceMap.getString("menuPictogramsShowImageActiveElement.text")); // NOI18N
        menuPictogramsShowImageActiveElement.setName("menuPictogramsShowImageActiveElement"); // NOI18N
        menuPictogramsShowImageActiveElement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPictogramsShowImageActiveElementActionPerformed(evt);
            }
        });
        menuPictogramsShowImage.add(menuPictogramsShowImageActiveElement);

        menuPictogramsShowImageAllElements.setText(resourceMap.getString("menuPictogramsShowImageAllElements.text")); // NOI18N
        menuPictogramsShowImageAllElements.setName("menuPictogramsShowImageAllElements"); // NOI18N
        menuPictogramsShowImageAllElements.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPictogramsShowImageAllElementsActionPerformed(evt);
            }
        });
        menuPictogramsShowImage.add(menuPictogramsShowImageAllElements);

        menuPictogramsShow.add(menuPictogramsShowImage);

        menuPictogramsShowBorder.setText(resourceMap.getString("menuPictogramsShowBorder.text")); // NOI18N
        menuPictogramsShowBorder.setName("menuPictogramsShowBorder"); // NOI18N

        menuPictogramsShowBorderActiveElement.setText(resourceMap.getString("menuPictogramsShowBorderActiveElement.text")); // NOI18N
        menuPictogramsShowBorderActiveElement.setName("menuPictogramsShowBorderActiveElement"); // NOI18N
        menuPictogramsShowBorderActiveElement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPictogramsShowBorderActiveElementActionPerformed(evt);
            }
        });
        menuPictogramsShowBorder.add(menuPictogramsShowBorderActiveElement);

        menuPictogramsShowBorderAllElements.setText(resourceMap.getString("menuPictogramsShowBorderAllElements.text")); // NOI18N
        menuPictogramsShowBorderAllElements.setName("menuPictogramsShowBorderAllElements"); // NOI18N
        menuPictogramsShowBorderAllElements.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPictogramsShowBorderAllElementsActionPerformed(evt);
            }
        });
        menuPictogramsShowBorder.add(menuPictogramsShowBorderAllElements);

        menuPictogramsShow.add(menuPictogramsShowBorder);

        menuPictograms.add(menuPictogramsShow);

        menuPictogramsHide.setText(resourceMap.getString("menuPictogramsHide.text")); // NOI18N
        menuPictogramsHide.setName("menuPictogramsHide"); // NOI18N

        menuPictogramsHideImage.setText(resourceMap.getString("menuPictogramsHideImage.text")); // NOI18N
        menuPictogramsHideImage.setName("menuPictogramsHideImage"); // NOI18N

        menuPictogramsHideImageActiveElement.setText(resourceMap.getString("menuPictogramsHideImageActiveElement.text")); // NOI18N
        menuPictogramsHideImageActiveElement.setName("menuPictogramsHideImageActiveElement"); // NOI18N
        menuPictogramsHideImageActiveElement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPictogramsHideImageActiveElementActionPerformed(evt);
            }
        });
        menuPictogramsHideImage.add(menuPictogramsHideImageActiveElement);

        menuPictogramsHideImageAllElements.setText(resourceMap.getString("menuPictogramsHideImageAllElements.text")); // NOI18N
        menuPictogramsHideImageAllElements.setName("menuPictogramsHideImageAllElements"); // NOI18N
        menuPictogramsHideImageAllElements.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPictogramsHideImageAllElementsActionPerformed(evt);
            }
        });
        menuPictogramsHideImage.add(menuPictogramsHideImageAllElements);

        menuPictogramsHide.add(menuPictogramsHideImage);

        menuPictogramsHideBorder.setText(resourceMap.getString("menuPictogramsHideBorder.text")); // NOI18N
        menuPictogramsHideBorder.setName("menuPictogramsHideBorder"); // NOI18N

        menuPictogramsHideBorderActiveElement.setText(resourceMap.getString("menuPictogramsHideBorderActiveElement.text")); // NOI18N
        menuPictogramsHideBorderActiveElement.setName("menuPictogramsHideBorderActiveElement"); // NOI18N
        menuPictogramsHideBorderActiveElement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPictogramsHideBorderActiveElementActionPerformed(evt);
            }
        });
        menuPictogramsHideBorder.add(menuPictogramsHideBorderActiveElement);

        menuPictogramsHideBorderAllElements.setText(resourceMap.getString("menuPictogramsHideBorderAllElements.text")); // NOI18N
        menuPictogramsHideBorderAllElements.setName("menuPictogramsHideBorderAllElements"); // NOI18N
        menuPictogramsHideBorderAllElements.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPictogramsHideBorderAllElementsActionPerformed(evt);
            }
        });
        menuPictogramsHideBorder.add(menuPictogramsHideBorderAllElements);

        menuPictogramsHide.add(menuPictogramsHideBorder);

        menuPictograms.add(menuPictogramsHide);



        menuBar.add(menuPictograms);

        menuTools.setText(resourceMap.getString("menuTools.text")); // NOI18N

        menuToolsResourceManager.setText(resourceMap.getString("menuToolsResourceManager.text")); // NOI18N
        menuToolsResourceManager.setName("menuToolsResourceManager"); // NOI18N
        menuToolsResourceManager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainFrame f = new mainFrame(G.applicationLanguage);
                f.setVisible(true);
                f.pack();
            }
        });

        //change: 02/02/12: just disable the option in order the data base be handled
        //                    from the ResourceMananger application. There are some
        //                    path problems when launching it from the menu
        menuTools.add(menuToolsResourceManager);

        menuToolsGeneralPreferences.setText(resourceMap.getString("menuToolsGeneralPreferences.text")); // NOI18N
        menuToolsGeneralPreferences.setName("menuToolsGeneralPreferences"); // NOI18N
        menuToolsGeneralPreferences.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuToolsGeneralPreferencesActionPerformed(evt);
            }
        });
        menuTools.add(menuToolsGeneralPreferences);

        menuVoiceSintesys.setText(resourceMap.getString("menuPictogramsNextImage.text")); // NOI18N
        menuVoiceSintesys.setName("menuVoiceSintesys"); // NOI18N
        menuVoiceSintesys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolBarButtonVoiceSintesysActionPerformed(evt);
            }
        });
        menuTools.add(menuVoiceSintesys);

        menuBar.add(menuTools);

        menuHelp.setText(resourceMap.getString("menuHelp.text")); // NOI18N
        menuHelp.setName("menuHelp"); // NOI18N

        menuHelpShowHelp.setText(resourceMap.getString("menuHelpShowHelp.text")); // NOI18N
        menuHelpShowHelp.setName("menuHelpShowHelp"); // NOI18N
        menuHelpShowHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuHelpShowHelpActionPerformed(evt);
            }
        });
        menuHelp.add(menuHelpShowHelp);

        menuHelpAbout.setText(resourceMap.getString("menuHelpAbout.text")); // NOI18N
        menuHelpAbout.setName("menuHelpAbout"); // NOI18N
        menuHelpAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuHelpAboutActionPerformed(evt);
            }
        });
        menuHelp.add(menuHelpAbout);

        menuBar.add(menuHelp);

        aboutDialog.setTitle(resourceMap.getString("aboutDialog.title")); // NOI18N
        aboutDialog.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        aboutDialog.setModal(true);
        aboutDialog.setName("aboutDialog"); // NOI18N
        aboutDialog.setResizable(false);

        aboutDialogCloseButton.setText(resourceMap.getString("aboutDialogCloseButton.text")); // NOI18N
        aboutDialogCloseButton.setName("aboutDialogCloseButton"); // NOI18N
        aboutDialogCloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutDialogCloseButtonActionPerformed(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        aboutDialogTextArea.setColumns(20);
        aboutDialogTextArea.setEditable(false);
        aboutDialogTextArea.setRows(5);
        aboutDialogTextArea.setFocusable(false);
        aboutDialogTextArea.setName("aboutDialogTextArea"); // NOI18N
        jScrollPane1.setViewportView(aboutDialogTextArea);

        jPanel1.setName("jPanel1"); // NOI18N

        aboutDialogImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        aboutDialogImage.setText(resourceMap.getString("aboutDialogImage.text")); // NOI18N
        aboutDialogImage.setName("aboutDialogImage"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(aboutDialogImage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, aboutDialogImage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout aboutDialogLayout = new org.jdesktop.layout.GroupLayout(aboutDialog.getContentPane());
        aboutDialog.getContentPane().setLayout(aboutDialogLayout);
        aboutDialogLayout.setHorizontalGroup(
                aboutDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(aboutDialogLayout.createSequentialGroup()
                                .addContainerGap()
                                .add(aboutDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, aboutDialogLayout.createSequentialGroup()
                                                .add(aboutDialogCloseButton)
                                                .addContainerGap())
                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, aboutDialogLayout.createSequentialGroup()
                                                .add(aboutDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE))
                                                .addContainerGap())))
        );
        aboutDialogLayout.setVerticalGroup(
                aboutDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(aboutDialogLayout.createSequentialGroup()
                                .addContainerGap()
                                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(12, 12, 12)
                                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 480, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(aboutDialogCloseButton)
                                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        findDialog.setTitle(resourceMap.getString("findDialog.title")); // NOI18N
        findDialog.setName("findDialog"); // NOI18N
        findDialog.setResizable(false);

        findDialogFindTextLabel.setText(resourceMap.getString("findDialogFindTextLabel.text")); // NOI18N
        findDialogFindTextLabel.setName("findDialogFindTextLabel"); // NOI18N

        findDialogTextField.setText(resourceMap.getString("findDialogTextField.text")); // NOI18N
        findDialogTextField.setName("findDialogTextField"); // NOI18N

        findDialogFindButton.setText(resourceMap.getString("findDialogFindButton.text")); // NOI18N
        findDialogFindButton.setName("findDialogFindButton"); // NOI18N
        findDialogFindButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findDialogFindButtonActionPerformed(evt);
            }
        });

        findDialogExitButton.setText(resourceMap.getString("findDialogExitButton.text")); // NOI18N
        findDialogExitButton.setName("findDialogExitButton"); // NOI18N
        findDialogExitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findDialogExitButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout findDialogLayout = new org.jdesktop.layout.GroupLayout(findDialog.getContentPane());
        findDialog.getContentPane().setLayout(findDialogLayout);
        findDialogLayout.setHorizontalGroup(
                findDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(findDialogLayout.createSequentialGroup()
                                .addContainerGap()
                                .add(findDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                        .add(findDialogFindTextLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 73, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(findDialogLayout.createSequentialGroup()
                                                .add(findDialogFindButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(findDialogExitButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .add(findDialogTextField))
                                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        findDialogLayout.setVerticalGroup(
                findDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(findDialogLayout.createSequentialGroup()
                                .addContainerGap()
                                .add(findDialogFindTextLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(findDialogTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(findDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(findDialogFindButton)
                                        .add(findDialogExitButton))
                                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        generalPreferencesDialog.setModal(true);
        generalPreferencesDialog.setName("generalPreferencesDialog"); // NOI18N
        generalPreferencesDialog.setResizable(false);

        generalPreferencesDialogSpinnerImagesSize.setModel(new javax.swing.SpinnerNumberModel(100, 25, 500, 5));
        generalPreferencesDialogSpinnerImagesSize.setName("generalPreferencesDialogSpinnerImagesSize"); // NOI18N

        generalPreferencesDialogImagesSizeLabel.setText(resourceMap.getString("generalPreferencesDialogImagesSizeLabel.text")); // NOI18N
        generalPreferencesDialogImagesSizeLabel.setName("generalPreferencesDialogImagesSizeLabel"); // NOI18N

//        generalPreferencesDialogMaxUndoLevelLabel.setText(resourceMap.getString("generalPreferencesDialogMaxUndoLevelLabel.text")); // NOI18N
//        generalPreferencesDialogMaxUndoLevelLabel.setName("generalPreferencesDialogMaxUndoLevelLabel"); // NOI18N

//        generalPreferencesDialogSpinnerMaxUndoLevel.setModel(new javax.swing.SpinnerNumberModel(5, 1, 20, 1));
//        generalPreferencesDialogSpinnerMaxUndoLevel.setName("generalPreferencesDialogSpinnerMaxUndoLevel"); // NOI18N

//        generalPreferencesDialogMaxLengthCompoundWordsLabel.setText(resourceMap.getString("generalPreferencesDialogMaxLengthCompoundWordsLabel.text")); // NOI18N
//        generalPreferencesDialogMaxLengthCompoundWordsLabel.setName("generalPreferencesDialogMaxLengthCompoundWordsLabel"); // NOI18N

//        generalPreferencesDialogSpinnerMaxLengthCompoundWords.setModel(new javax.swing.SpinnerNumberModel(3, 0, 5, 1));
//        generalPreferencesDialogSpinnerMaxLengthCompoundWords.setName("generalPreferencesDialogSpinnerMaxLengthCompoundWords"); // NOI18N

        generalPreferencesDialogApplicationLanguageLabel.setText(resourceMap.getString("generalPreferencesDialogApplicationLanguageLabel.text")); // NOI18N
        generalPreferencesDialogApplicationLanguageLabel.setName("generalPreferencesDialogApplicationLanguageLabel"); // NOI18N

        /*String languages[] = new String[] {"Español", "English", "Français", "Deutsch", "Català"};
        for (int i = 0; i < languages.length; i++) {
        	generalPreferencesDialogSpinnerApplicationLanguage.addItem(languages[i]);
		}
        generalPreferencesDialogSpinnerApplicationLanguage.setName("generalPreferencesDialogSpinnerApplicationLanguage"); // NOI18N*/

        generalPreferencesDialogDocumentLanguageLabel.setText(resourceMap.getString("generalPreferencesDialogDocumentLanguageLabel.text")); // NOI18N
        generalPreferencesDialogDocumentLanguageLabel.setName("generalPreferencesDialogDocumentLanguageLabel"); // NOI18N

        /*String documentLanguages[] = new String[] {"(todos)", "Español", "English", "Français", "Deutsch", "Català"};
        for (int i = 0; i < documentLanguages.length; i++) {			
        	generalPreferencesDialogSpinnerDocumentLanguage.addItem(documentLanguages[i]);
		}
        generalPreferencesDialogSpinnerDocumentLanguage.setName("generalPreferencesDialogSpinnerDocumentLanguage"); // NOI18N*/

        generalPreferencesDialogOKButton.setText(resourceMap.getString("generalPreferencesDialogOKButton.text")); // NOI18N
        generalPreferencesDialogOKButton.setName("generalPreferencesDialogOKButton"); // NOI18N
        generalPreferencesDialogOKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generalPreferencesDialogOKButtonActionPerformed(evt);
            }
        });

        generalPreferencesDialogCancelButton.setText(resourceMap.getString("generalPreferencesDialogCancelButton.text")); // NOI18N
        generalPreferencesDialogCancelButton.setName("generalPreferencesDialogCancelButton"); // NOI18N
        generalPreferencesDialogCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generalPreferencesDialogCancelButtonActionPerformed(evt);
            }
        });

//        generalPreferencesDialogPictogramsPathLabel.setText(resourceMap.getString("generalPreferencesDialogPictogramsPathLabel.text")); // NOI18N
//        generalPreferencesDialogPictogramsPathLabel.setName("generalPreferencesDialogPictogramsPathLabel"); // NOI18N

//        generalPreferencesDialogChoosePictogramsPathButton.setText(resourceMap.getString("generalPreferencesDialogChoosePictogramsPathButton.text")); // NOI18N
//        generalPreferencesDialogChoosePictogramsPathButton.setName("generalPreferencesDialogChoosePictogramsPathButton"); // NOI18N
//        generalPreferencesDialogChoosePictogramsPathButton.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                generalPreferencesDialogChoosePictogramsPathButtonActionPerformed(evt);
//            }
//        });

        generalPreferencesDialogTextFontLabel.setText(resourceMap.getString("generalPreferencesDialogTextFontLabel.text")); // NOI18N
        generalPreferencesDialogTextFontLabel.setName("generalPreferencesDialogTextFontLabel"); // NOI18N

        generalPreferencesDialogChooseTextFontButton.setText(resourceMap.getString("generalPreferencesDialogChooseTextFontButton.text")); // NOI18N
        generalPreferencesDialogChooseTextFontButton.setName("generalPreferencesDialogChooseTextFontButton"); // NOI18N
        generalPreferencesDialogChooseTextFontButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generalPreferencesDialogChooseTextFontButtonActionPerformed(evt);
            }
        });

        generalPreferencesDialogTextColorLabel.setText(resourceMap.getString("generalPreferencesDialogTextColorLabel.text")); // NOI18N
        generalPreferencesDialogTextColorLabel.setName("generalPreferencesDialogTextColorLabel"); // NOI18N

        generalPreferencesDialogChooseTextColorButton.setText(resourceMap.getString("generalPreferencesDialogChooseTextColorButton.text")); // NOI18N
        generalPreferencesDialogChooseTextColorButton.setName("generalPreferencesDialogChooseTextColorButton"); // NOI18N
        generalPreferencesDialogChooseTextColorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generalPreferencesDialogChooseTextColorButtonActionPerformed(evt);
            }
        });

        generalPreferencesDialogTextPlacementLabel.setText(resourceMap.getString("generalPreferencesDialogTextPlacementLabel.text")); // NOI18N
        generalPreferencesDialogTextPlacementLabel.setName("generalPreferencesDialogTextPlacementLabel"); // NOI18N

        /*String textPlacement[] = new String[] {"Encima del pictograma", "Debajo del pictograma"};
        for (int i = 0; i < textPlacement.length; i++) {
        	generalPreferencesDialogSpinnerTextPlacement.addItem(textPlacement[i]);
		}
        generalPreferencesDialogSpinnerTextPlacement.setName("generalPreferencesDialogSpinnerTextPlacement"); // NOI18N*/

        org.jdesktop.layout.GroupLayout generalPreferencesDialogLayout = new org.jdesktop.layout.GroupLayout(generalPreferencesDialog.getContentPane());
        generalPreferencesDialog.getContentPane().setLayout(generalPreferencesDialogLayout);
        generalPreferencesDialogLayout.setHorizontalGroup(
                generalPreferencesDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(generalPreferencesDialogLayout.createSequentialGroup()
                                .addContainerGap()
                                .add(generalPreferencesDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(generalPreferencesDialogLayout.createSequentialGroup()
                                                .add(generalPreferencesDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                .add(generalPreferencesDialogImagesSizeLabel)
//                            .add(generalPreferencesDialogMaxUndoLevelLabel)
                                                                .add(generalPreferencesDialogApplicationLanguageLabel)
//                            .add(generalPreferencesDialogPictogramsPathLabel)
                                                                .add(generalPreferencesDialogDocumentLanguageLabel)
//                            .add(generalPreferencesDialogMaxLengthCompoundWordsLabel)
                                                )
                                                .add(18, 18, 18)
                                                .add(generalPreferencesDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                .add(generalPreferencesDialogSpinnerImagesSize, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
//                            .add(generalPreferencesDialogChoosePictogramsPathButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                                                                .add(generalPreferencesDialogSpinnerDocumentLanguage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                                                                .add(generalPreferencesDialogSpinnerApplicationLanguage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
//                            .add(generalPreferencesDialogSpinnerMaxLengthCompoundWords, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
//                            .add(generalPreferencesDialogSpinnerMaxUndoLevel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                                                ))
                                        .add(generalPreferencesDialogTextFontLabel)
                                        .add(generalPreferencesDialogTextColorLabel)
                                        .add(generalPreferencesDialogLayout.createSequentialGroup()
                                                .add(generalPreferencesDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                        .add(generalPreferencesDialogOKButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 131, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                        .add(generalPreferencesDialogTextPlacementLabel))
                                                .add(18, 18, 18)
                                                .add(generalPreferencesDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, generalPreferencesDialogChooseTextColorButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, generalPreferencesDialogChooseTextFontButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, generalPreferencesDialogSpinnerTextPlacement, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                                                        .add(generalPreferencesDialogCancelButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE))))
                                .addContainerGap())
        );
        generalPreferencesDialogLayout.setVerticalGroup(
                generalPreferencesDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(generalPreferencesDialogLayout.createSequentialGroup()
                                .addContainerGap()
                                .add(generalPreferencesDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(generalPreferencesDialogImagesSizeLabel)
                                        .add(generalPreferencesDialogSpinnerImagesSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(5, 5, 5)
                                .add(generalPreferencesDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
//                    .add(generalPreferencesDialogMaxUndoLevelLabel)
//                    .add(generalPreferencesDialogSpinnerMaxUndoLevel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                )
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(generalPreferencesDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
//                    .add(generalPreferencesDialogMaxLengthCompoundWordsLabel)
//                    .add(generalPreferencesDialogSpinnerMaxLengthCompoundWords, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                )
                                .add(9, 9, 9)
                                .add(generalPreferencesDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(generalPreferencesDialogApplicationLanguageLabel)
                                        .add(generalPreferencesDialogSpinnerApplicationLanguage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(generalPreferencesDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(generalPreferencesDialogDocumentLanguageLabel)
                                        .add(generalPreferencesDialogSpinnerDocumentLanguage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(generalPreferencesDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
//                    .add(generalPreferencesDialogPictogramsPathLabel)
//                    .add(generalPreferencesDialogChoosePictogramsPathButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                )
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(generalPreferencesDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(generalPreferencesDialogTextFontLabel)
                                        .add(generalPreferencesDialogChooseTextFontButton))
                                .add(7, 7, 7)
                                .add(generalPreferencesDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(generalPreferencesDialogTextColorLabel)
                                        .add(generalPreferencesDialogChooseTextColorButton))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(generalPreferencesDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(generalPreferencesDialogTextPlacementLabel)
                                        .add(generalPreferencesDialogSpinnerTextPlacement, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(34, 34, 34)
                                .add(generalPreferencesDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(generalPreferencesDialogOKButton)
                                        .add(generalPreferencesDialogCancelButton))
                                .addContainerGap())
        );

        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        toolBar.setName("toolBar"); // NOI18N

        toolBarButtonFileNew.setText(resourceMap.getString("toolBarButtonFileNew.text")); // NOI18N
        toolBarButtonFileNew.setFocusable(false);
        toolBarButtonFileNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolBarButtonFileNew.setName("toolBarButtonFileNew"); // NOI18N
        toolBarButtonFileNew.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBarButtonFileNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolBarButtonFileNewActionPerformed(evt);
            }
        });
        toolBar.add(toolBarButtonFileNew);

        toolBarButtonFileOpen.setFocusable(false);
        toolBarButtonFileOpen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolBarButtonFileOpen.setName("toolBarButtonFileOpen"); // NOI18N
        toolBarButtonFileOpen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBarButtonFileOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolBarButtonFileOpenActionPerformed(evt);
            }
        });
        toolBar.add(toolBarButtonFileOpen);

        toolBarButtonFileSave.setFocusable(false);
        toolBarButtonFileSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolBarButtonFileSave.setName("toolBarButtonFileSave"); // NOI18N
        toolBarButtonFileSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBarButtonFileSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolBarButtonFileSaveActionPerformed(evt);
            }
        });
        toolBar.add(toolBarButtonFileSave);

        jSeparator3.setName("jSeparator3"); // NOI18N
        toolBar.add(jSeparator3);

        toolBarButtonEditUndo.setFocusable(false);
        toolBarButtonEditUndo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolBarButtonEditUndo.setName("toolBarButtonEditUndo"); // NOI18N
        toolBarButtonEditUndo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBarButtonEditUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolBarButtonEditUndoActionPerformed(evt);
            }
        });
        toolBar.add(toolBarButtonEditUndo);

        toolBarButtonEditRedo.setFocusable(false);
        toolBarButtonEditRedo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolBarButtonEditRedo.setName("toolBarButtonEditRedo"); // NOI18N
        toolBarButtonEditRedo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBarButtonEditRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolBarButtonEditRedoActionPerformed(evt);
            }
        });
        toolBar.add(toolBarButtonEditRedo);

        toolBarButtonEditCut.setFocusable(false);
        toolBarButtonEditCut.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolBarButtonEditCut.setName("toolBarButtonEditCut"); // NOI18N
        toolBarButtonEditCut.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBarButtonEditCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolBarButtonEditCutActionPerformed(evt);
            }
        });
        toolBar.add(toolBarButtonEditCut);

        toolBarButtonEditCopy.setFocusable(false);
        toolBarButtonEditCopy.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolBarButtonEditCopy.setName("toolBarButtonEditCopy"); // NOI18N
        toolBarButtonEditCopy.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBarButtonEditCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolBarButtonEditCopyActionPerformed(evt);
            }
        });
        toolBar.add(toolBarButtonEditCopy);

        toolBarButtonEditPaste.setFocusable(false);
        toolBarButtonEditPaste.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolBarButtonEditPaste.setName("toolBarButtonEditPaste"); // NOI18N
        toolBarButtonEditPaste.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBarButtonEditPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolBarButtonEditPasteActionPerformed(evt);
            }
        });
        toolBar.add(toolBarButtonEditPaste);

        jSeparator4.setName("jSeparator4"); // NOI18N
        toolBar.add(jSeparator4);

        toolBarButtonPictogramsNextImage.setText(resourceMap.getString("toolBarButtonPictogramsNextImage.text")); // NOI18N
        toolBarButtonPictogramsNextImage.setFocusable(false);
        toolBarButtonPictogramsNextImage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolBarButtonPictogramsNextImage.setName("toolBarButtonPictogramsNextImage"); // NOI18N
        toolBarButtonPictogramsNextImage.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBarButtonPictogramsNextImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolBarButtonPictogramsNextImageActionPerformed(evt);
            }
        });
        toolBar.add(toolBarButtonPictogramsNextImage);

        toolBarButtonPictogramsCompoundSplitWord.setText(resourceMap.getString("toolBarButtonPictogramsCompoundSplitWord.text")); // NOI18N
        toolBarButtonPictogramsCompoundSplitWord.setFocusable(false);
        toolBarButtonPictogramsCompoundSplitWord.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolBarButtonPictogramsCompoundSplitWord.setName("toolBarButtonPictogramsCompoundSplitWord"); // NOI18N
        toolBarButtonPictogramsCompoundSplitWord.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBarButtonPictogramsCompoundSplitWord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolBarButtonPictogramsCompoundSplitWordActionPerformed(evt);
            }
        });
        toolBar.add(toolBarButtonPictogramsCompoundSplitWord);

        toolBarButtonPictogramsChangeName.setText(resourceMap.getString("toolBarButtonPictogramsChangeName.text")); // NOI18N
        toolBarButtonPictogramsChangeName.setFocusable(false);
        toolBarButtonPictogramsChangeName.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolBarButtonPictogramsChangeName.setName("toolBarButtonPictogramsChangeName"); // NOI18N
        toolBarButtonPictogramsChangeName.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBarButtonPictogramsChangeName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolBarButtonPictogramsChangeNameActionPerformed(evt);
            }
        });
        toolBar.add(toolBarButtonPictogramsChangeName);

        toolBarButtonPictogramsInsertImage.setText(resourceMap.getString("toolBarButtonPictogramsChangeName.text")); // NOI18N
        toolBarButtonPictogramsInsertImage.setFocusable(false);
        toolBarButtonPictogramsInsertImage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolBarButtonPictogramsInsertImage.setName("toolBarButtonPictogramsChangeName"); // NOI18N
        toolBarButtonPictogramsInsertImage.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBarButtonPictogramsInsertImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolBarButtonPictogramsInsertImageActionPerformed(evt);
            }
        });
        toolBar.add(toolBarButtonPictogramsInsertImage);

        toolBarButtonVoiceSintesys.setText(resourceMap.getString("toolBarButtonVoiceSintesys.text")); // NOI18N
        toolBarButtonVoiceSintesys.setFocusable(false);
        toolBarButtonVoiceSintesys.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toolBarButtonVoiceSintesys.setName("toolBarButtonVoiceSintesys"); // NOI18N
        toolBarButtonVoiceSintesys.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBarButtonVoiceSintesys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolBarButtonVoiceSintesysActionPerformed(evt);
            }
        });
        toolBar.add(toolBarButtonVoiceSintesys);






        documentLanguageDialog.setModal(true);
        documentLanguageDialog.setName("documentLanguageDialog"); // NOI18N
        documentLanguageDialog.setResizable(false);

        documentLanguageDialogDocumentLanguageLabel.setText(resourceMap.getString("documentLanguageDialogDocumentLanguageLabel.text")); // NOI18N
        documentLanguageDialogDocumentLanguageLabel.setName("documentLanguageDialogDocumentLanguageLabel"); // NOI18N

//        documentLanguageDialogSpinnerDocumentLanguage.setModel(new javax.swing.SpinnerListModel(new String[] {"(todos)", "Español", "English", "Français", "Deutsch", "Català"}));
//        documentLanguageDialogSpinnerDocumentLanguage.setName("documentLanguageDialogSpinnerDocumentLanguage"); // NOI18N

//        String[] languages = new String[] {"(todos)", "Español", "English", "Français", "Deutsch", "Català"};
//        for (int i = 0; i < languages.length; i++) {
//			documentLanguageDialogComboBoxDocumentLanguage.addItem(languages[i]);
//		}

        documentLanguageDialogOKButton.setText(resourceMap.getString("documentLanguageDialogOKButton.text")); // NOI18N
        documentLanguageDialogOKButton.setName("documentLanguageDialogOKButton"); // NOI18N
        documentLanguageDialogOKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                documentLanguageDialogOKButtonActionPerformed(evt);
            }
        });

        documentLanguageDialogCancelButton.setText(resourceMap.getString("documentLanguageDialogCancelButton.text")); // NOI18N
        documentLanguageDialogCancelButton.setName("documentLanguageDialogCancelButton"); // NOI18N
        documentLanguageDialogCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                documentLanguageDialogCancelButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout documentLanguageDialogLayout = new org.jdesktop.layout.GroupLayout(documentLanguageDialog.getContentPane());
        documentLanguageDialog.getContentPane().setLayout(documentLanguageDialogLayout);
        documentLanguageDialogLayout.setHorizontalGroup(
                documentLanguageDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(documentLanguageDialogLayout.createSequentialGroup()
                                .addContainerGap()
                                .add(documentLanguageDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(documentLanguageDialogLayout.createSequentialGroup()
                                                .add(documentLanguageDialogDocumentLanguageLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .add(35, 35, 35)
                                                .add(documentLanguageDialogComboBoxDocumentLanguage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))
                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, documentLanguageDialogLayout.createSequentialGroup()
                                                .add(documentLanguageDialogOKButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 138, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(documentLanguageDialogCancelButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        documentLanguageDialogLayout.setVerticalGroup(
                documentLanguageDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(documentLanguageDialogLayout.createSequentialGroup()
                                .addContainerGap()
                                .add(documentLanguageDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(documentLanguageDialogComboBoxDocumentLanguage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(documentLanguageDialogDocumentLanguageLabel))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(documentLanguageDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(documentLanguageDialogCancelButton)
                                        .add(documentLanguageDialogOKButton))
                                .addContainerGap())
        );

        imagesSizeDialog.setModal(true);
        imagesSizeDialog.setName("imagesSizeDialog"); // NOI18N
        imagesSizeDialog.setResizable(false);

        imagesSizeDialogImagesSizeLabel.setText(resourceMap.getString("imagesSizeDialogImagesSizeLabel.text")); // NOI18N
        imagesSizeDialogImagesSizeLabel.setName("imagesSizeDialogImagesSizeLabel"); // NOI18N

        imagesSizeDialogSpinnerImagesSize.setModel(new javax.swing.SpinnerNumberModel(100, 25, 500, 5));
        imagesSizeDialogSpinnerImagesSize.setName("imagesSizeDialogSpinnerImagesSize"); // NOI18N

        imagesSizeDialogCancelButton.setText(resourceMap.getString("imagesSizeDialogCancelButton.text")); // NOI18N
        imagesSizeDialogCancelButton.setName("imagesSizeDialogCancelButton"); // NOI18N
        imagesSizeDialogCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imagesSizeDialogCancelButtonActionPerformed(evt);
            }
        });

        imagesSizeDialogOKButton.setText(resourceMap.getString("imagesSizeDialogOKButton.text")); // NOI18N
        imagesSizeDialogOKButton.setName("imagesSizeDialogOKButton"); // NOI18N
        imagesSizeDialogOKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imagesSizeDialogOKButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout imagesSizeDialogLayout = new org.jdesktop.layout.GroupLayout(imagesSizeDialog.getContentPane());
        imagesSizeDialog.getContentPane().setLayout(imagesSizeDialogLayout);
        imagesSizeDialogLayout.setHorizontalGroup(
                imagesSizeDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(imagesSizeDialogLayout.createSequentialGroup()
                                .addContainerGap()
                                .add(imagesSizeDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                        .add(imagesSizeDialogOKButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .add(imagesSizeDialogImagesSizeLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(imagesSizeDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(imagesSizeDialogSpinnerImagesSize, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                                        .add(imagesSizeDialogCancelButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
                                .addContainerGap())
        );
        imagesSizeDialogLayout.setVerticalGroup(
                imagesSizeDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(imagesSizeDialogLayout.createSequentialGroup()
                                .addContainerGap()
                                .add(imagesSizeDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(imagesSizeDialogImagesSizeLabel)
                                        .add(imagesSizeDialogSpinnerImagesSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(imagesSizeDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(imagesSizeDialogOKButton)
                                        .add(imagesSizeDialogCancelButton))
                                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );


        // ################################# INIT COMPONENTS EXPORT PDF DIALOG
        exportPDFDialog.setModal(true);
        exportPDFDialog.setName("exportPDFDialog"); // NOI18N
        exportPDFDialog.setResizable(false);


        exportPDFDialogPictosLineLabel.setName("exportPDFDialogPictosLineLabel"); // NOI18N

        exportPDFDialogSpinnerPictosLine.setModel(new javax.swing.SpinnerNumberModel(5, 1, 10, 1));
        exportPDFDialogSpinnerPictosLine.setName("exportPDFDialogSpinnerPictosLine"); // NOI18N
        exportPDFDialogSpinnerPictosLine.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent arg0) {
                G.numPictosLinePDF=((Integer) exportPDFDialogSpinnerPictosLine.getValue()).intValue();
                G.imagesSize=200/G.numPictosLinePDF;

                araword.utils.TextUtils.regeneratePDFPrevZone(exportPDFDialogPrevArea,((Integer) exportPDFDialogPagSpinner.getValue()).intValue());
                //System.out.println("Picto linea: " + ((Integer) exportPDFDialogSpinnerPictosLine.getValue()).intValue());
            }
        });

        exportPDFDialogPagSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, 50, 1));
        exportPDFDialogPagSpinner.setName("exportPDFDialogPagSpinner"); // NOI18N
        exportPDFDialogPagSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent arg0) {
                int page= ((Integer) exportPDFDialogPagSpinner.getValue()).intValue();
                araword.utils.TextUtils.regeneratePDFPrevZone(exportPDFDialogPrevArea,page);

            }
        });

        exportPDFDialogArasaacLicenseCB.setSelected(G.licensePDF);
        exportPDFDialogArasaacLicenseCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                G.licensePDF=exportPDFDialogArasaacLicenseCB.isSelected();
            }
        });

        exportPDFDialogCancelButton.setName("exportPDFDialogCancelButton"); // NOI18N
        exportPDFDialogCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportPDFDialogCancelButtonActionPerformed(evt);
            }
        });


        exportPDFDialogExportButton.setName("exportPDFDialogExportButton"); // NOI18N
        exportPDFDialogExportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportPDFDialogExportButtonActionPerformed(evt);
                System.out.println("##### pulsado export");


            }
        });

        org.jdesktop.layout.GroupLayout exportPDFDialogLayout = new org.jdesktop.layout.GroupLayout(exportPDFDialog.getContentPane());
        exportPDFDialog.getContentPane().setLayout(exportPDFDialogLayout);

        //pruebas de añadir componentes
        exportPDFDialogLayout.setHorizontalGroup(
                exportPDFDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, exportPDFDialogLayout.createSequentialGroup()
                                .addContainerGap()
                                .add(exportPDFDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(exportPDFDialogLayout.createSequentialGroup()
                                                .add(exportPDFDialogPictosLineLabel)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                                .add(exportPDFDialogSpinnerPictosLine, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .add(exportPDFDialogArasaacLicenseCB)
                                        .add(exportPDFDialogLayout.createSequentialGroup()
                                                .add(exportPDFDialogCancelButton)
                                                .add(76, 76, 76)
                                                .add(exportPDFDialogExportButton)))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 57, Short.MAX_VALUE)
                                .add(exportPDFDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(exportPDFDialogPrevArea, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 275, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(exportPDFDialogLayout.createSequentialGroup()
                                                .add(exportPDFDialogPrevLabel)
                                                .add(67, 67, 67)
                                                .add(exportPDFDialogPagLabel)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(exportPDFDialogPagSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        exportPDFDialogLayout.setVerticalGroup(
                exportPDFDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(exportPDFDialogLayout.createSequentialGroup()
                                .addContainerGap()
                                .add(exportPDFDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(exportPDFDialogPrevLabel)
                                        .add(exportPDFDialogPagLabel)
                                        .add(exportPDFDialogPagSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(exportPDFDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(exportPDFDialogLayout.createSequentialGroup()
                                                .add(21, 21, 21)
                                                .add(exportPDFDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                                        .add(exportPDFDialogPictosLineLabel)
                                                        .add(exportPDFDialogSpinnerPictosLine, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                .add(18, 18, 18)
                                                .add(exportPDFDialogArasaacLicenseCB)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 236, Short.MAX_VALUE)
                                                .add(exportPDFDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                                        .add(exportPDFDialogCancelButton)
                                                        .add(exportPDFDialogExportButton)))
                                        .add(exportPDFDialogLayout.createSequentialGroup()
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(exportPDFDialogPrevArea, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)))
                                .add(28, 28, 28))
        );
        
        
        
       /*
        exportPDFDialogLayout.setHorizontalGroup(
        		 exportPDFDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(exportPDFDialogLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(exportPDFDialogPictosLineLabel)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                    .add(exportPDFDialogSpinnerPictosLine, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(449, Short.MAX_VALUE))
            );
        exportPDFDialogLayout.setVerticalGroup(
        		exportPDFDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(exportPDFDialogLayout.createSequentialGroup()
                    .add(49, 49, 49)
                    .add(exportPDFDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(exportPDFDialogPictosLineLabel)
                        .add(exportPDFDialogSpinnerPictosLine, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(331, Short.MAX_VALUE))
            );
        */
        
        /*
        exportPDFDialogLayout.setHorizontalGroup(
        		exportPDFDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(exportPDFDialogLayout.createSequentialGroup()
                .addContainerGap()
                .add(exportPDFDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(exportPDFDialogExportButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(exportPDFDialogPictosLineLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(exportPDFDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(exportPDFDialogSpinnerPictosLine, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                    .add(exportPDFDialogCancelButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
                .addContainerGap())
        );
        
        exportPDFDialogLayout.setVerticalGroup(
        		exportPDFDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(exportPDFDialogLayout.createSequentialGroup()
                .addContainerGap()
                .add(exportPDFDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(exportPDFDialogPictosLineLabel)
                    .add(exportPDFDialogSpinnerPictosLine, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(imagesSizeDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(exportPDFDialogExportButton)
                    .add(exportPDFDialogCancelButton))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
		*/
        setComponent(mainPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void menuFileNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileNewActionPerformed
    	if (JOptionPane.showConfirmDialog(getFrame(),TLanguage.getString("FILE_MENU_WARNING_DISCARD"),
    			TLanguage.getString("WARNING"),JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			TextUtils.newDocument();
		}
    }//GEN-LAST:event_menuFileNewActionPerformed

    private void menuFileOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileOpenActionPerformed
    	if (JOptionPane.showConfirmDialog(getFrame(),TLanguage.getString("FILE_MENU_WARNING_DISCARD"),
    			TLanguage.getString("WARNING"),JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
    		MenuFunctions.fileOpen();
		}
    	
    }//GEN-LAST:event_menuFileOpenActionPerformed

    private void menuFileSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileSaveActionPerformed
        MenuFunctions.fileSave();
    }//GEN-LAST:event_menuFileSaveActionPerformed

    private void menuFileSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileSaveAsActionPerformed
        MenuFunctions.fileSaveAs();
    }//GEN-LAST:event_menuFileSaveAsActionPerformed

    private void menuFileExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileExportActionPerformed
        MenuFunctions.fileExport();
    }//GEN-LAST:event_menuFileExportActionPerformed


    private void menuFileExportPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileExportActionPerformed
        //generate previsualization area
        System.out.println("######## export PDF ##########");

        //imagesSizeDialog
        exportPDFDialog.pack();
        G.imagesSizePDF=G.imagesSize;
        G.numPictosLinePDF=((Integer) exportPDFDialogSpinnerPictosLine.getValue()).intValue();
        G.imagesSize=200/G.numPictosLinePDF;

        exportPDFDialogPagSpinner.setValue(1);
        araword.utils.TextUtils.regeneratePDFPrevZone(exportPDFDialogPrevArea,1);
        exportPDFDialog.setModal(true);
        exportPDFDialog.setLocationRelativeTo(null); // Center window on the middle of the screen.
        exportPDFDialog.setVisible(true);

    }//GEN-LAST:event_menuFileExportActionPerformed

    private void menuFileExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileExitActionPerformed
        if (JOptionPane.showConfirmDialog(getFrame(),TLanguage.getString("FILE_MENU_EXIT_WARNING"),
                TLanguage.getString("WARNING"),JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            System.exit(0);
    }//GEN-LAST:event_menuFileExitActionPerformed

    private void menuEditCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEditCutActionPerformed
        MenuFunctions.editCut();
    }//GEN-LAST:event_menuEditCutActionPerformed

    private void menuEditCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEditCopyActionPerformed
        MenuFunctions.editCopy();
    }//GEN-LAST:event_menuEditCopyActionPerformed

    private void menuEditPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEditPasteActionPerformed
        MenuFunctions.editPaste(G.activeElementPosition+1);
    }//GEN-LAST:event_menuEditPasteActionPerformed

    private void menuEditUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEditUndoActionPerformed
        MenuFunctions.editUndo();
    }//GEN-LAST:event_menuEditUndoActionPerformed

    private void menuEditRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEditRedoActionPerformed
        MenuFunctions.editRedo();
    }//GEN-LAST:event_menuEditRedoActionPerformed

    private void menuEditFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEditFindActionPerformed
        findDialog.pack();
        G.lastPositionFound = -1;
        findDialog.setModal(true);
        findDialog.setLocationRelativeTo(null); // Center window on the middle of the screen.
        findDialog.setVisible(true);
    }//GEN-LAST:event_menuEditFindActionPerformed

    private void findDialogFindButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findDialogFindButtonActionPerformed

        String strToFind = findDialogTextField.getText();
        boolean somethingFound = false;

        if (strToFind.equals("")) {
            // Don't allow search of empty strings.
            JOptionPane.showMessageDialog(null,TLanguage.getString("EDIT_MENU_FIND_UNABLE_EMPTY_STRING_SEARCH"),
                    TLanguage.getString("WARNING"),JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (!strToFind.equals(G.lastStringAsked)) {
            G.lastPositionFound = -1; // New search.
            G.lastStringAsked = strToFind;
        }
        String wholeText = TextUtils.ElementList2String(G.elementList);
        if (wholeText.indexOf(strToFind)==-1) return; // String to find is not on text.

        searchLoop: // Useful to break outer loop.
        for (int i=G.lastPositionFound+1; i<G.elementList.size(); i++) {
            AWElement elem = G.elementList.get(i);
            if (elem.getType()==0) {
                String wordText = elem.getTextField().getText();
                if (wordText.length()>=strToFind.length()) {
                    // Search "abc" in a word like "ddabcddd"
                    if (wordText.indexOf(strToFind)!=-1) { // Found word!!
                        G.lastPositionFound = i;
                        somethingFound = true;
                        break searchLoop;
                    }
                }
                else {
                    // Search "abc ddddd" in a word like "abc".
                    // If we use startWith instead of indexOf, we'll miss search hits like
                    // search "abc ddddd" in a sequence of words: "fffabc dddddwer".
                    // However, that's a lot easier.
                    if (strToFind.startsWith(wordText)) {
                        boolean stop = false;
                        int tmpPos = i;
                        String words = "";
                        ArrayList<AWElement> eL = new ArrayList<AWElement>();
                        eL.add(elem);
                        while (!stop) {
                            if (tmpPos<G.elementList.size()) {
                                eL.add(G.elementList.get(tmpPos));
                                tmpPos++;
                                words = TextUtils.ElementList2String(eL);
                                if (words.length()>=strToFind.length()) {
                                    // Search "abc de f" in a sequence of words like "abc de f ghij"
                                    if (words.indexOf(strToFind)!=-1) { // Found word!!
                                        G.lastPositionFound = i;
                                        somethingFound = true;
                                        break searchLoop;
                                    }
                                    else stop = true;
                                }
                            }
                            else stop = true;
                        }
                    }
                }
            }
        }

        findDialog.setVisible(false);
        if (somethingFound) {
            G.elementList.get(G.lastPositionFound).getTextField().requestFocusInWindow();
        }
        else {
            JOptionPane.showMessageDialog(null,TLanguage.getString("EDIT_MENU_FIND_NOT_FOUND_TEXT"),
                    TLanguage.getString("WARNING"),JOptionPane.INFORMATION_MESSAGE);
            G.lastPositionFound = -1;
        }
        findDialog.setVisible(true);

    }//GEN-LAST:event_findDialogFindButtonActionPerformed

    private void findDialogExitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findDialogExitButtonActionPerformed
        findDialog.setVisible(false);
    }//GEN-LAST:event_findDialogExitButtonActionPerformed

    private void menuEditSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEditSelectAllActionPerformed
        G.indexSelectionFrom = 0;
        G.indexSelectionTo = G.elementList.size()-1;
        for (int i=G.indexSelectionFrom; i<=G.indexSelectionTo; i++) {
            G.elementList.get(i).setBackground(Color.BLUE);
        }
        G.wereDrag = false;
        G.selectionState = 2;
    }//GEN-LAST:event_menuEditSelectAllActionPerformed

    private void menuTextFontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTextFontActionPerformed
        JFontChooser fontChooser = new JFontChooser();
        if (fontChooser.showDialog(null) == JFontChooser.OK_OPTION) {
            Font font = fontChooser.getSelectedFont();
            G.font = font;
        }
        TextUtils.regenerateDocument();
    }//GEN-LAST:event_menuTextFontActionPerformed

    private void menuTextColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTextColorActionPerformed
        Color tmpColor = JColorChooser.showDialog(null,"",Color.BLACK);
        if (tmpColor!=null) G.color = tmpColor;
        TextUtils.regenerateDocument();
    }//GEN-LAST:event_menuTextColorActionPerformed

    private void menuTextPlacementAbovePictogramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTextPlacementAbovePictogramActionPerformed
        G.textBelowPictogram = false;
        TextUtils.regenerateDocument();
    }//GEN-LAST:event_menuTextPlacementAbovePictogramActionPerformed

    private void menuTextPlacementBelowPictogramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTextPlacementBelowPictogramActionPerformed
        G.textBelowPictogram = true;
        TextUtils.regenerateDocument();
    }//GEN-LAST:event_menuTextPlacementBelowPictogramActionPerformed

    private void menuTextToUpperCaseActiveElementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTextToUpperCaseActiveElementActionPerformed
        MenuFunctions.pictogramToUpperCaseActiveElement();
    }//GEN-LAST:event_menuTextToUpperCaseActiveElementActionPerformed

    private void menuTextToUpperCaseAllElementsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTextToUpperCaseAllElementsActionPerformed
        MenuFunctions.pictogramToUpperCaseAllElements();
    }//GEN-LAST:event_menuTextToUpperCaseAllElementsActionPerformed

    private void menuTextToLowerCaseActiveElementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTextToLowerCaseActiveElementActionPerformed
        MenuFunctions.pictogramToLowerCaseActiveElement();
    }//GEN-LAST:event_menuTextToLowerCaseActiveElementActionPerformed

    private void menuTextToLowerCaseAllElementsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTextToLowerCaseAllElementsActionPerformed
        MenuFunctions.pictogramToLowerCaseAllElements();
    }//GEN-LAST:event_menuTextToLowerCaseAllElementsActionPerformed

    private void menuTextDocumentLanguageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTextDocumentLanguageActionPerformed
        documentLanguageDialog.pack();
        documentLanguageDialog.setModal(true);
        documentLanguageDialog.setLocationRelativeTo(null); // Center window on the middle of the screen.
        documentLanguageDialog.setVisible(true);
    }//GEN-LAST:event_menuTextDocumentLanguageActionPerformed

    private void documentLanguageDialogCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_documentLanguageDialogCancelButtonActionPerformed
        documentLanguageDialog.setVisible(false);
    }//GEN-LAST:event_documentLanguageDialogCancelButtonActionPerformed

    private void documentLanguageDialogOKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_documentLanguageDialogOKButtonActionPerformed
        G.documentLanguage = (String)documentLanguageDialogComboBoxDocumentLanguage.getSelectedItem();
        DBManagement.connectVerbsDB();
        DBManagement.createAraWordView(G.documentLanguage);
        TextUtils.regenerateDocument();
        documentLanguageDialog.setVisible(false);
    }//GEN-LAST:event_documentLanguageDialogOKButtonActionPerformed

    private void menuPictogramsSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPictogramsSizeActionPerformed
        imagesSizeDialog.pack();
        imagesSizeDialog.setModal(true);
        imagesSizeDialog.setLocationRelativeTo(null); // Center window on the middle of the screen.
        imagesSizeDialog.setVisible(true);
    }//GEN-LAST:event_menuPictogramsSizeActionPerformed

    private void imagesSizeDialogCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imagesSizeDialogCancelButtonActionPerformed
        imagesSizeDialog.setVisible(false);
    }//GEN-LAST:event_imagesSizeDialogCancelButtonActionPerformed

    private void imagesSizeDialogOKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imagesSizeDialogOKButtonActionPerformed
        System.out.println("##### CAMBIADO TAMAÑO #####");
        G.imagesSize = ((Integer)imagesSizeDialogSpinnerImagesSize.getValue()).intValue();
        ImageIcon image = new ImageIcon("resources/404.jpg");
        G.notFound = new ImageIcon(image.getImage().getScaledInstance(-1,G.imagesSize,0));
        TextUtils.regenerateDocument();
        imagesSizeDialog.setVisible(false);
    }//GEN-LAST:event_imagesSizeDialogOKButtonActionPerformed

    private void exportPDFDialogCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imagesSizeDialogCancelButtonActionPerformed
        G.imagesSize=G.imagesSizePDF;
        exportPDFDialog.setVisible(false);
    }//GEN-LAST:event_imagesSizeDialogCancelButtonActionPerformed

    private void exportPDFDialogExportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imagesSizeDialogCancelButtonActionPerformed
        //G.imagesSize=G.imagesSizePDF;
        MenuFunctions.filePDFExport();
        G.imagesSize=G.imagesSizePDF;
        exportPDFDialogPrevArea.setText("");
        exportPDFDialog.setVisible(false);
    }//GEN-LAST:event_exportPDFDialogExportButtonActionPerformed    


    private void menuPictogramsNextImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPictogramsNextImageActionPerformed
        MenuFunctions.pictogramNextImage();
    }//GEN-LAST:event_menuPictogramsNextImageActionPerformed

    private void menuPictogramsCompoundSplitWordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPictogramsCompoundSplitWordActionPerformed
        MenuFunctions.pictogramCompoundSplit();
    }//GEN-LAST:event_menuPictogramsCompoundSplitWordActionPerformed

    private void menuPictogramsChangeNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPictogramsChangeNameActionPerformed
        MenuFunctions.pictogramChangeName();
    }//GEN-LAST:event_menuPictogramsChangeNameActionPerformed

    private void menuPictogramsShowBorderAllElementsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPictogramsShowBorderAllElementsActionPerformed
        MenuFunctions.pictogramShowBorderAllElements();
    }//GEN-LAST:event_menuPictogramsShowBorderAllElementsActionPerformed

    private void menuPictogramsHideBorderAllElementsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPictogramsHideBorderAllElementsActionPerformed
        MenuFunctions.pictogramHideBorderAllElements();
    }//GEN-LAST:event_menuPictogramsHideBorderAllElementsActionPerformed

    private void menuPictogramsShowImageActiveElementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPictogramsShowImageActiveElementActionPerformed
        MenuFunctions.pictogramShowImageActiveElement();
    }//GEN-LAST:event_menuPictogramsShowImageActiveElementActionPerformed

    private void menuPictogramsShowImageAllElementsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPictogramsShowImageAllElementsActionPerformed
        MenuFunctions.pictogramShowImageAllElements();
    }//GEN-LAST:event_menuPictogramsShowImageAllElementsActionPerformed

    private void menuPictogramsShowBorderActiveElementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPictogramsShowBorderActiveElementActionPerformed
        MenuFunctions.pictogramShowBorderActiveElement();
    }//GEN-LAST:event_menuPictogramsShowBorderActiveElementActionPerformed

    private void menuPictogramsHideImageActiveElementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPictogramsHideImageActiveElementActionPerformed
        MenuFunctions.pictogramHideImageActiveElement();
    }//GEN-LAST:event_menuPictogramsHideImageActiveElementActionPerformed

    private void menuPictogramsHideImageAllElementsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPictogramsHideImageAllElementsActionPerformed
        MenuFunctions.pictogramHideImageAllElements();
    }//GEN-LAST:event_menuPictogramsHideImageAllElementsActionPerformed

    private void menuPictogramsHideBorderActiveElementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPictogramsHideBorderActiveElementActionPerformed
        MenuFunctions.pictogramHideBorderActiveElement();
    }//GEN-LAST:event_menuPictogramsHideBorderActiveElementActionPerformed

    private void menuToolsResourceManagerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuToolsResourceManagerActionPerformed
        try {
            Runtime.getRuntime().exec("java -jar ."+File.separator+".."+
                    File.separator+"ResourceManager"+File.separator+"ResourceManager.jar");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }//GEN-LAST:event_menuToolsResourceManagerActionPerformed

    private void menuToolsGeneralPreferencesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuToolsGeneralPreferencesActionPerformed
        generalPreferencesDialog.pack();
        generalPreferencesDialog.setLocationRelativeTo(null); // Center window on the middle of the screen.
        G.tempDefaultFont = G.defaultFont;
        G.tempDefaultColor = G.defaultColor;
        G.tempPictogramsPath = G.pictogramsPath;
        generalPreferencesDialog.setVisible(true);
    }//GEN-LAST:event_menuToolsGeneralPreferencesActionPerformed

    private void generalPreferencesDialogOKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generalPreferencesDialogOKButtonActionPerformed
        G.defaultTextBelowPictogram = ((String)generalPreferencesDialogSpinnerTextPlacement.getSelectedItem()).equals(TLanguage.getString("SPINNER_TEXT_BELOW_PICTOGRAM"));
        G.defaultImagesSize = ((Integer)generalPreferencesDialogSpinnerImagesSize.getValue()).intValue();
//    	G.maxLengthCompoundWords = ((Integer)generalPreferencesDialogSpinnerMaxLengthCompoundWords.getValue()).intValue();
//    	G.maxUndoLevel = ((Integer)generalPreferencesDialogSpinnerMaxUndoLevel.getValue()).intValue();
        G.applicationLanguage = ((String)generalPreferencesDialogSpinnerApplicationLanguage.getSelectedItem());
        G.defaultDocumentLanguage = ((String)generalPreferencesDialogSpinnerDocumentLanguage.getSelectedItem());
        G.documentLanguage = ((String)generalPreferencesDialogSpinnerDocumentLanguage.getSelectedItem());
        G.defaultFont = G.tempDefaultFont;
        G.defaultColor = G.tempDefaultColor;
        G.pictogramsPath = G.tempPictogramsPath;
        try {
            TSetup.save();
            TLanguage.initLanguage(G.applicationLanguage);
            setApplicationLanguage();
            DBManagement.createAraWordView(G.documentLanguage);
            TextUtils.regenerateDocument();
        }
        catch (Exception e) { System.out.println(e); }
//    	ImageIcon image = new ImageIcon("resources/404.jpg");
//    	G.notFound = new ImageIcon(image.getImage().getScaledInstance(-1,G.imagesSize,0));
//    	TextUtils.regenerateDocument();
        generalPreferencesDialog.setVisible(false);

    }//GEN-LAST:event_generalPreferencesDialogOKButtonActionPerformed

    private void generalPreferencesDialogCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generalPreferencesDialogCancelButtonActionPerformed
        generalPreferencesDialog.setVisible(false);
    }//GEN-LAST:event_generalPreferencesDialogCancelButtonActionPerformed

    private void generalPreferencesDialogChooseTextFontButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generalPreferencesDialogChooseTextFontButtonActionPerformed
        JFontChooser fontChooser = new JFontChooser();
        if (fontChooser.showDialog(null) == JFontChooser.OK_OPTION) {
            Font font = fontChooser.getSelectedFont();
            G.tempDefaultFont = font;
        }
    }//GEN-LAST:event_generalPreferencesDialogChooseTextFontButtonActionPerformed

//    private void generalPreferencesDialogChoosePictogramsPathButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generalPreferencesDialogChoosePictogramsPathButtonActionPerformed
//       try {
//          JFileChooser fc = new JFileChooser(".");
//          fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//          if (fc.showOpenDialog(G.textZone) == JFileChooser.APPROVE_OPTION) {
//             File file = fc.getSelectedFile();
//             // Use external class RelativePath to obtain relative path (i.e ..\pictograms).
//             // Easier than messing with file, URI, normalize, relativize...
//             G.tempPictogramsPath = RelativePath.getRelativePath(new File("."),file);
//          }
//       } catch (Exception exc) {System.out.println(exc);}
//    }//GEN-LAST:event_generalPreferencesDialogChoosePictogramsPathButtonActionPerformed




    private void toolBarButtonFileNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolBarButtonFileNewActionPerformed
    	if (JOptionPane.showConfirmDialog(getFrame(),TLanguage.getString("FILE_MENU_WARNING_DISCARD"),
        		TLanguage.getString("WARNING"),JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			TextUtils.newDocument();
		}
    }//GEN-LAST:event_toolBarButtonFileNewActionPerformed

    private void toolBarButtonFileOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolBarButtonFileOpenActionPerformed
    	if (JOptionPane.showConfirmDialog(getFrame(),TLanguage.getString("FILE_MENU_WARNING_DISCARD"),
        		TLanguage.getString("WARNING"),JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
    		MenuFunctions.fileOpen();
		}
    }//GEN-LAST:event_toolBarButtonFileOpenActionPerformed

    private void toolBarButtonFileSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolBarButtonFileSaveActionPerformed
        MenuFunctions.fileSave();
    }//GEN-LAST:event_toolBarButtonFileSaveActionPerformed

    private void toolBarButtonEditUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolBarButtonEditUndoActionPerformed
        MenuFunctions.editUndo();
    }//GEN-LAST:event_toolBarButtonEditUndoActionPerformed

    private void toolBarButtonEditRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolBarButtonEditRedoActionPerformed
        MenuFunctions.editRedo();
    }//GEN-LAST:event_toolBarButtonEditRedoActionPerformed

    private void toolBarButtonEditCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolBarButtonEditCutActionPerformed
        MenuFunctions.editCut();
    }//GEN-LAST:event_toolBarButtonEditCutActionPerformed

    private void toolBarButtonEditCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolBarButtonEditCopyActionPerformed
        MenuFunctions.editCopy();
    }//GEN-LAST:event_toolBarButtonEditCopyActionPerformed

    private void toolBarButtonEditPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolBarButtonEditPasteActionPerformed
        MenuFunctions.editPaste(G.activeElementPosition+1);
    }//GEN-LAST:event_toolBarButtonEditPasteActionPerformed

    private void toolBarButtonPictogramsNextImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolBarButtonPictogramsNextImageActionPerformed
        MenuFunctions.pictogramNextImage();
    }//GEN-LAST:event_toolBarButtonPictogramsNextImageActionPerformed

    private void toolBarButtonPictogramsCompoundSplitWordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolBarButtonPictogramsCompoundSplitWordActionPerformed
        MenuFunctions.pictogramCompoundSplit();
    }//GEN-LAST:event_toolBarButtonPictogramsCompoundSplitWordActionPerformed

    private void toolBarButtonPictogramsChangeNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolBarButtonPictogramsChangeNameActionPerformed
        MenuFunctions.pictogramChangeName();
    }//GEN-LAST:event_toolBarButtonPictogramsChangeNameActionPerformed

    private void toolBarButtonPictogramsInsertImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolBarButtonPictogramsInsertImageActionPerformed
        MenuFunctions.pictogramInsertImage();
    }//GEN-LAST:event_toolBarButtonPictogramsInsertImageActionPerformed

    private void toolBarButtonVoiceSintesysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolBarButtonPictogramsInsertImageActionPerformed
        MenuFunctions.VoiceSintesys();
    }//GEN-LAST:event_toolBarButtonPictogramsInsertImageActionPerformed




    private void generalPreferencesDialogChooseTextColorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generalPreferencesDialogChooseTextColorButtonActionPerformed
        G.tempDefaultColor = JColorChooser.showDialog(null,"",Color.BLACK);
    }//GEN-LAST:event_generalPreferencesDialogChooseTextColorButtonActionPerformed

    private void menuHelpShowHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuHelpShowHelpActionPerformed
        // Opens help document
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(new File("resources/ayuda.txt"));
            }
            catch (Exception exc) {System.out.println(exc);}
        }
    }//GEN-LAST:event_menuHelpShowHelpActionPerformed

    private void menuHelpAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuHelpAboutActionPerformed
        aboutDialog.pack();
        aboutDialog.setVisible(true);
    }//GEN-LAST:event_menuHelpAboutActionPerformed

    private void aboutDialogCloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutDialogCloseButtonActionPerformed
        aboutDialog.setVisible(false);
    }//GEN-LAST:event_aboutDialogCloseButtonActionPerformed


    //cambiar teto de la barra del titulo
    public  void setBarTitle(String name) {

        this.getFrame().setTitle("kk");

    }



    // **********************************
    // CREATE PROGRESS DIALOG
    // **********************************

    private Timer timer;
    public static JProgressBar  progressBar;
    public JPanel createProgressDialog(){

        progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        //progressBar.setIndeterminate(true);

        javax.swing.JLabel label = new javax.swing.JLabel("Progress: ");
        javax.swing.JPanel center_panel = new javax.swing.JPanel();
        center_panel.add(label);
        center_panel.add(progressBar);
        center_panel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
                Color.WHITE, new Color(165, 163, 151)),
                TLanguage.getString("TIGImportDBDialog.PROGRESS")));

        //create a timer
        timer = new Timer(100, new TimerListener());

        return center_panel;

    }

    //the actionPerformed method in this class
    //is called each time the Timer "goes off"
    class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            progressBar.setValue(25);
        }
    }



    //*****************************
    // import images in awz file custom dialog
    //*****************************

    private static boolean result = false;
    private static javax.swing.JDialog customDialog;


    public static boolean createCustomDialog(String imageName, List l) {
        /*******************************
         * Dialog asking for permission to import a custom picto to the ddbb
         *******************************/
        javax.swing.JLabel customWords;
        javax.swing.JButton importButton;
        javax.swing.JButton rejectButton;
        javax.swing.JTextPane prevImage;

        // recover words
        String wordResult = "";
        Iterator k = l.iterator();
        while (k.hasNext()) {
            Element languageElement = (Element) k.next();
            String language = languageElement.getAttributeValue("id");
            wordResult = wordResult + language + ": ";

            List words = languageElement.getChildren("word");
            Iterator w = words.iterator();
            while (w.hasNext()) {
                Element wordElement = (Element) w.next();
                String word = wordElement.getText();
                wordResult = wordResult + word + ", ";

            }
            wordResult = wordResult + "\n";
        }

        // recover image
        ImageIcon iconLoad = new ImageIcon(imageName);
        Image imgIcon = iconLoad.getImage();
        Image resizedImage = imgIcon.getScaledInstance(130, 130,
                Image.SCALE_DEFAULT);
        ImageIcon image = new ImageIcon(resizedImage);


        customDialog = new javax.swing.JDialog();
        customDialog.setModal(true);
        customDialog.setName("addCustomImageDialog"); // NOI18N
        customDialog.setResizable(false);
        customDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        customDialog.setTitle(TLanguage.getString("CUSTOM.DESEA_ADD_PICTO"));

        // interface netbeans
        customWords = new javax.swing.JLabel();
        importButton = new javax.swing.JButton();
        rejectButton = new javax.swing.JButton();
        prevImage = new javax.swing.JTextPane();

        customWords.setText(wordResult);


        importButton.setText(TLanguage.getString("CUSTOM.IMPORTAR"));

        importButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                result = true;
                customDialog.dispose();

            }
        });

        rejectButton.setText(TLanguage.getString("CUSTOM.RECHAZAR"));

        rejectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                result = false;
                customDialog.dispose();
            }
        });

        // prevImage.setText(imageName);
        prevImage.insertIcon(image);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
                customDialog.getContentPane());
        customDialog.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                        javax.swing.GroupLayout.Alignment.TRAILING,
                        layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(
                                        layout.createParallelGroup(
                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(
                                                        customWords,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        207,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(
                                                        layout.createSequentialGroup()
                                                                .addComponent(
                                                                        importButton,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                        84,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                        36,
                                                                        Short.MAX_VALUE)
                                                                .addComponent(
                                                                        rejectButton,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                        89,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(prevImage,
                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                        146,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap()));
        layout.setVerticalGroup(layout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                        layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(
                                        layout.createParallelGroup(
                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                false)
                                                .addGroup(
                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                        layout.createSequentialGroup()
                                                                .addComponent(
                                                                        customWords,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                        88,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE)
                                                                .addGroup(
                                                                        layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(
                                                                                        importButton)
                                                                                .addComponent(
                                                                                        rejectButton)))
                                                .addComponent(
                                                        prevImage,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        137,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE)));

        customDialog.setLocation(100, 100);
        customDialog.pack();
        customDialog.setVisible(true);

        return result;
    }






    //*****************************





    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog aboutDialog;
    private javax.swing.JButton aboutDialogCloseButton;
    private javax.swing.JLabel aboutDialogImage;
    private javax.swing.JTextArea aboutDialogTextArea;
    private javax.swing.JDialog documentLanguageDialog;
    private javax.swing.JButton documentLanguageDialogCancelButton;
    private javax.swing.JLabel documentLanguageDialogDocumentLanguageLabel;
    private javax.swing.JButton documentLanguageDialogOKButton;
    private javax.swing.JComboBox documentLanguageDialogComboBoxDocumentLanguage;
    private javax.swing.JDialog findDialog;
    private javax.swing.JButton findDialogExitButton;
    private javax.swing.JButton findDialogFindButton;
    private javax.swing.JLabel findDialogFindTextLabel;
    private javax.swing.JTextField findDialogTextField;
    private javax.swing.JDialog generalPreferencesDialog;
    private javax.swing.JLabel generalPreferencesDialogApplicationLanguageLabel;
    private javax.swing.JButton generalPreferencesDialogCancelButton;
    //    private javax.swing.JButton generalPreferencesDialogChoosePictogramsPathButton;
    private javax.swing.JButton generalPreferencesDialogChooseTextColorButton;
    private javax.swing.JButton generalPreferencesDialogChooseTextFontButton;
    private javax.swing.JLabel generalPreferencesDialogDocumentLanguageLabel;
    private javax.swing.JLabel generalPreferencesDialogImagesSizeLabel;
    //    private javax.swing.JLabel generalPreferencesDialogMaxLengthCompoundWordsLabel;
//    private javax.swing.JLabel generalPreferencesDialogMaxUndoLevelLabel;
    private javax.swing.JButton generalPreferencesDialogOKButton;
    //    private javax.swing.JLabel generalPreferencesDialogPictogramsPathLabel;
    private javax.swing.JComboBox generalPreferencesDialogSpinnerApplicationLanguage;
    private javax.swing.JComboBox generalPreferencesDialogSpinnerDocumentLanguage;
    private javax.swing.JSpinner generalPreferencesDialogSpinnerImagesSize;
    //    private javax.swing.JSpinner generalPreferencesDialogSpinnerMaxLengthCompoundWords;
//    private javax.swing.JSpinner generalPreferencesDialogSpinnerMaxUndoLevel;
    private javax.swing.JComboBox generalPreferencesDialogSpinnerTextPlacement;
    private javax.swing.JLabel generalPreferencesDialogTextColorLabel;
    private javax.swing.JLabel generalPreferencesDialogTextFontLabel;
    private javax.swing.JLabel generalPreferencesDialogTextPlacementLabel;
    private javax.swing.JDialog imagesSizeDialog;
    private javax.swing.JButton imagesSizeDialogCancelButton;
    private javax.swing.JLabel imagesSizeDialogImagesSizeLabel;
    private javax.swing.JButton imagesSizeDialogOKButton;
    private javax.swing.JSpinner imagesSizeDialogSpinnerImagesSize;

    //export PDF dialog
    private javax.swing.JDialog exportPDFDialog;
    private javax.swing.JButton exportPDFDialogCancelButton;
    private javax.swing.JButton exportPDFDialogExportButton;
    private javax.swing.JLabel exportPDFDialogPictosLineLabel;
    private javax.swing.JSpinner exportPDFDialogSpinnerPictosLine;
    private javax.swing.JSpinner exportPDFDialogPagSpinner;
    private javax.swing.JCheckBox exportPDFDialogArasaacLicenseCB;
    private javax.swing.JLabel exportPDFDialogPagLabel;
    private javax.swing.JLabel exportPDFDialogPrevLabel;
    private javax.swing.JTextPane exportPDFDialogPrevArea;

    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuEdit;
    private javax.swing.JMenuItem menuEditCopy;
    private javax.swing.JMenuItem menuEditCut;
    private javax.swing.JMenuItem menuEditFind;
    private javax.swing.JMenuItem menuEditPaste;
    private javax.swing.JMenuItem menuEditRedo;
    private javax.swing.JMenuItem menuEditSelectAll;
    private javax.swing.JMenuItem menuEditUndo;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenuItem menuFileExit;
    private javax.swing.JMenu menuFileExport;
    private javax.swing.JMenuItem menuFileExportImage;
    private javax.swing.JMenuItem menuFileExportPDF;
    private javax.swing.JMenuItem menuFileNew;
    private javax.swing.JMenuItem menuFileOpen;
    private javax.swing.JMenuItem menuFileSave;
    private javax.swing.JMenuItem menuFileSaveAs;
    private javax.swing.JMenu menuHelp;
    private javax.swing.JMenuItem menuHelpAbout;
    private javax.swing.JMenuItem menuHelpShowHelp;
    private javax.swing.JMenu menuPictograms;
    private javax.swing.JMenuItem menuPictogramsChangeName;
    private javax.swing.JMenuItem menuPictogramsCompoundSplitWord;
    private javax.swing.JMenuItem menuPictogramsInsertImage;
    private javax.swing.JMenuItem menuVoiceSintesys;
    private javax.swing.JMenu menuPictogramsHide;
    private javax.swing.JMenu menuPictogramsHideBorder;
    private javax.swing.JMenuItem menuPictogramsHideBorderActiveElement;
    private javax.swing.JMenuItem menuPictogramsHideBorderAllElements;
    private javax.swing.JMenu menuPictogramsHideImage;
    private javax.swing.JMenuItem menuPictogramsHideImageActiveElement;
    private javax.swing.JMenuItem menuPictogramsHideImageAllElements;
    private javax.swing.JMenuItem menuPictogramsNextImage;
    private javax.swing.JMenu menuPictogramsShow;
    private javax.swing.JMenu menuPictogramsShowBorder;
    private javax.swing.JMenuItem menuPictogramsShowBorderActiveElement;
    private javax.swing.JMenuItem menuPictogramsShowBorderAllElements;
    private javax.swing.JMenu menuPictogramsShowImage;
    private javax.swing.JMenuItem menuPictogramsShowImageActiveElement;
    private javax.swing.JMenuItem menuPictogramsShowImageAllElements;
    private javax.swing.JMenuItem menuPictogramsSize;
    private javax.swing.JMenu menuText;
    private javax.swing.JMenuItem menuTextColor;
    private javax.swing.JMenuItem menuTextDocumentLanguage;
    private javax.swing.JMenuItem menuTextFont;
    private javax.swing.JMenu menuTextPlacement;
    private javax.swing.JMenuItem menuTextPlacementAbovePictogram;
    private javax.swing.JMenuItem menuTextPlacementBelowPictogram;
    private javax.swing.JMenu menuTextToLowerCase;
    private javax.swing.JMenuItem menuTextToLowerCaseActiveElement;
    private javax.swing.JMenuItem menuTextToLowerCaseAllElements;
    private javax.swing.JMenu menuTextToUpperCase;
    private javax.swing.JMenuItem menuTextToUpperCaseActiveElement;
    private javax.swing.JMenuItem menuTextToUpperCaseAllElements;
    private javax.swing.JMenu menuTools;
    private javax.swing.JMenuItem menuToolsGeneralPreferences;
    private javax.swing.JMenuItem menuToolsResourceManager;
    private javax.swing.JScrollPane scrollTextZone;
    private javax.swing.JTextPane textZone;
    private javax.swing.JToolBar toolBar;
    private javax.swing.JButton toolBarButtonEditCopy;
    private javax.swing.JButton toolBarButtonEditCut;
    private javax.swing.JButton toolBarButtonEditPaste;
    private javax.swing.JButton toolBarButtonEditRedo;
    private javax.swing.JButton toolBarButtonEditUndo;
    private javax.swing.JButton toolBarButtonFileNew;
    private javax.swing.JButton toolBarButtonFileOpen;
    private javax.swing.JButton toolBarButtonFileSave;
    private javax.swing.JButton toolBarButtonPictogramsChangeName;
    private javax.swing.JButton toolBarButtonPictogramsInsertImage;
    private javax.swing.JButton toolBarButtonPictogramsCompoundSplitWord;
    private javax.swing.JButton toolBarButtonPictogramsNextImage;
    private javax.swing.JButton toolBarButtonVoiceSintesys;

    // End of variables declaration//GEN-END:variables



}
