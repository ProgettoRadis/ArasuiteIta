/*
 * File: TCellDialog.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Mar 6, 2006
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

package tico.editor.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import tico.board.TBoard;
import tico.board.TBoardConstants;
import tico.board.components.TComponent;
import tico.components.TAlternativeBorderSelectionPanel;
import tico.components.TAlternativeSoundChooser;
import tico.components.TBackgroundSelectionPanel;
import tico.components.TBorderSelectionPanel;
import tico.components.TClickCellActionsPanel;
import tico.components.TFontModelChooser;
import tico.components.TIdTextField;
import tico.components.TImageChooser;
import tico.components.TSendTextChooser;
import tico.components.TSoundChooser;
import tico.components.TSynthesizerConfig;
import tico.components.TTextField;
import tico.components.TVideoChooser;
import tico.configuration.TLanguage;
import tico.editor.TBoardContainer;
import tico.editor.TEditor;
import tico.environment.TEnvironment;
/*anyadido sonido alternativo*/
/**
 * Dialog to change <code>TCellDialog</code> attributes.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TCellDialog extends TComponentDialog {

	private static String DEFAULT_TITLE = TLanguage.getString("TCellDialog.TITLE");

	// Tabbed pane which contains all the other cell properties panes
	protected JTabbedPane tabbedPane;

	// Text properties panel
	private JPanel textPropertiesPanel;

	private JPanel textFieldPanel;

	private TTextField textField;

	protected JPanel idFieldPanel;

	protected TIdTextField idTextField;

	private TFontModelChooser fontModel;

	// Component properties panel
	private JPanel componentPropertiesPanel;
	
	private JPanel componentMultimediaPanel;

	private TBorderSelectionPanel borderSelectionPanel;
	
	private TAlternativeBorderSelectionPanel alternativeBorderSelectionPanel;

	private TBackgroundSelectionPanel backgroundSelectionPanel;

	private TImageChooser iconChooser;

	private TImageChooser alternativeIconChooser;

	// Actions panel
	private JPanel componentActionsPanel;
	
	private TClickCellActionsPanel clickCellActionPanel;

	private TSoundChooser soundChooser;
	
	private TAlternativeSoundChooser alternativeSoundChooser;
	
	private TSendTextChooser sendTextChooser;
	
	private TVideoChooser videoChooser;
	
	private JPanel environmentPanel;
	
	private JScrollPane listScroll;
	
	private JList orderList;
	
	private TEditor myEditor;
        
    
        private TSynthesizerConfig synthesizerPanel;
	
	/**
	 * Creates a new <code>TCellDialog</code> to edit the <code>cell</code>
	 * properties.
	 * 
	 * @param boardContainer The <code>boardContainer</code> which contains the
	 * cell to be edited
	 * @param cell The <code>cell</code> to be edited
	 */
	public TCellDialog(TBoardContainer boardContainer, TComponent cell) {
		this(boardContainer, DEFAULT_TITLE, cell);
		myEditor = boardContainer.getEditor();	
	}
	
	/**
	 * Creates a new <code>TCellDialog</code> to edit the <code>cell</code>
	 * properties.
	 * 
	 * @param boardContainer The <code>boardContainer</code> which contains the
	 * cell to be edited
	 * @param title The <code>title</code> of the dialog
	 * @param cell The <code>cell</code> to be edited
	 */
	public TCellDialog(TBoardContainer boardContainer, String title,
			TComponent cell) {
		super(boardContainer, title, cell);
		myEditor = boardContainer.getEditor();
	}

	// Creates the main dialog pane
	protected JPanel setComponentPane(TEditor editor) {
		JPanel componentPane = new JPanel();
		myEditor = editor;

		GridBagConstraints c = new GridBagConstraints();

		componentPane.setLayout(new GridBagLayout());
		
		createTabbedPane();

		createIdField();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 0;
		componentPane.add(idFieldPanel, c);

		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 1;
		componentPane.add(tabbedPane, c);

		return componentPane;
	}

	// Creates the cell id field
	protected void createIdField() {
		idFieldPanel = new JPanel();

		idFieldPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		idFieldPanel.add(new JLabel(TLanguage.getString("TCellDialog.ID")));

		idTextField = new TIdTextField();
		
		idFieldPanel.add(idTextField);

		idTextField.setText(TBoardConstants.getId(getAttributeMap()));
	}

	// Creates the main dialog tabbed pane
	protected void createTabbedPane() {
		tabbedPane = new JTabbedPane();

		// Create properties panels
		createTextPropertiesPanel();
		createComponentPropertiesPanel();
		createComponentMultimediaPanel();
		createActionsPanel();
		createEnvironmentPanel();
		// Add properties panels to the tabbed pane
		tabbedPane.addTab(TLanguage.getString("TCellDialog.TAB_TEXT"), textPropertiesPanel);
		tabbedPane.addTab(TLanguage.getString("TCellDialog.TAB_PROPERTIES"),
				componentPropertiesPanel);
		tabbedPane.addTab(TLanguage.getString("TCellDialog.TAB_MULTIMEDIA"),
				componentMultimediaPanel);
		tabbedPane.addTab(TLanguage.getString("TCellDialog.TAB_ACTIONS"),
				componentActionsPanel);
		
		tabbedPane.addTab(TLanguage.getString("TCellDialog.ENVIRONMENT"),environmentPanel);
	}
	public void createEnvironmentPanel()
	{
		environmentPanel = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		environmentPanel.setLayout(new GridBagLayout());
		createOrderList();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 0;
		environmentPanel.add(listScroll, c);
	}
	
	
	private void createOrderList() {
		Map map = getAttributeMap();
		 listScroll = new JScrollPane();
		// Set minimum scroll pane size
		listScroll.setPreferredSize(new Dimension(300,
				300));
		listScroll
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		listScroll
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// Create the list
		Vector environmentAction=TEnvironment.getAllKeys();
		
		orderList = new JList(environmentAction);
		orderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		orderList.setMinimumSize(new Dimension(300, 500));
		orderList.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent arg0) {
				orderList.repaint();
			}
		});

		int position = TBoardConstants.getPositionAction(map);
		int tam = environmentAction.size();
		if (position!=-1){
			if(position<tam){
				orderList.setSelectedIndex(position);
			}
		}
		
		listScroll.setViewportView(orderList);
		
	}

	// Creates the text properties panel for the tabbed pane
	private void createTextPropertiesPanel() {
		textPropertiesPanel = new JPanel();

		GridBagConstraints c = new GridBagConstraints();

		textPropertiesPanel.setLayout(new GridBagLayout());

		createTextField();
		createFontModel();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 0;
		textPropertiesPanel.add(textFieldPanel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 1;
		textPropertiesPanel.add(fontModel, c);
	}

	// Creates the cell properties panel for the tabbed pane
	private void createComponentPropertiesPanel() {
		componentPropertiesPanel = new JPanel();

		GridBagConstraints c = new GridBagConstraints();

		componentPropertiesPanel.setLayout(new GridBagLayout());
		createAlternativeBorderSelectionPanel();
		createBorderSelectionPanel();
		
		createBackgroundSelectionPanel();
		createIconChooser();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 0;
		componentPropertiesPanel.add(borderSelectionPanel, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 1;
		componentPropertiesPanel.add(alternativeBorderSelectionPanel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 2;
		componentPropertiesPanel.add(backgroundSelectionPanel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 3;
		componentPropertiesPanel.add(iconChooser, c);
	}

	// Creates the actions panel for the tabbed pane
	private void createActionsPanel() {
		componentActionsPanel = new JPanel();

		GridBagConstraints c = new GridBagConstraints();

		componentActionsPanel.setLayout(new GridBagLayout());

		createFollowingBoardPanel();
		createAlterntativeIconChooser();
                createSinthesizerConfig();
//		createSoundChooser();
//		createAlternativeSoundChooser();
		createSendTextChooser();
		
		/*if(TEditor.get_android_mode())*/ sendTextChooser.setEnabled(false);
		
//		createVideoChooser();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 0;
		componentActionsPanel.add(clickCellActionPanel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 1;
		componentActionsPanel.add(alternativeIconChooser, c);
		
//		if(!TEditor.get_android_mode()){
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 3;
		componentActionsPanel.add(sendTextChooser, c);
//		}
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 6;
		componentActionsPanel.add(synthesizerPanel, c);
	}
	
	// Creates the cell properties panel for the tabbed pane
	private void createComponentMultimediaPanel() {
		componentMultimediaPanel = new JPanel();

		GridBagConstraints c = new GridBagConstraints();

		componentMultimediaPanel.setLayout(new GridBagLayout());

		createSoundChooser();
		createAlternativeSoundChooser();
		createVideoChooser();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 0;
		componentMultimediaPanel.add(soundChooser, c);
		
		//anyadido alternativesound
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 1;
		componentMultimediaPanel.add(alternativeSoundChooser, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 3;
		componentMultimediaPanel.add(videoChooser, c);
	}
	
	
	// Creates the cell text field
	private void createTextField() {
		Map map = getAttributeMap();

		textFieldPanel = new JPanel();

		textFieldPanel.setBorder(new TitledBorder(BorderFactory
				.createEtchedBorder(Color.WHITE, new Color(165, 163, 151)),
				TLanguage.getString("TCellDialog.TEXT_FILED")));

		textField = new TTextField(TBoardConstants.getText(map));
		textField.setColumns(30);

		textFieldPanel.add(textField);
	}
	
	// Creates the font model selection panel
	private void createFontModel() {
		Map map = getAttributeMap();

		fontModel = new TFontModelChooser(TBoardConstants.getFont(map)
				.getName(), TBoardConstants.getForeground(map), TBoardConstants
				.getFont(map).getSize(), TBoardConstants.getFont(map)
				.getStyle());
	}
	
	// Creates the border selection panel
	private void createBorderSelectionPanel() {
		Map map = getAttributeMap();

		borderSelectionPanel = new TBorderSelectionPanel();
	
		borderSelectionPanel
				.setBorderColor(TBoardConstants.getBorderColor(map));
		borderSelectionPanel.setBorderSize(Math.max(1, Math
				.round(TBoardConstants.getLineWidth(map))));
	}
	
	//Creates the alternative border selection panel
	private void createAlternativeBorderSelectionPanel() {
		Map map = getAttributeMap();

		alternativeBorderSelectionPanel = new TAlternativeBorderSelectionPanel();

		alternativeBorderSelectionPanel
				.setBorderColor(TBoardConstants.getAlternativeBorderColor(map));
		
		alternativeBorderSelectionPanel.setBorderSize(Math.max(1,Math
				.round(TBoardConstants.getAlternativeLinewidth(map))));
	}	

	// Creates the background selection panel
	private void createBackgroundSelectionPanel() {
		Map map = getAttributeMap();

		backgroundSelectionPanel = new TBackgroundSelectionPanel();

		backgroundSelectionPanel.setBackgroundColor(TBoardConstants
				.getBackground(map));
		backgroundSelectionPanel.setGradientColor(TBoardConstants
				.getGradientColor(map));
	}

	// Creates the icon chooser panel
	private void createIconChooser() {
		Map map = getAttributeMap();

		iconChooser = new TImageChooser(TImageChooser.TEXT_POSITION_TYPE, myEditor);

		iconChooser.setIcon((ImageIcon)TBoardConstants.getIcon(map));
		iconChooser.setVerticalTextPosition(TBoardConstants
				.getVerticalTextPosition(map));
	}

	// Creates the alternative icon chooser panel
	private void createAlterntativeIconChooser() {
		Map map = getAttributeMap();

		alternativeIconChooser = new TImageChooser(
				TLanguage.getString("TCellDialog.ALTERNATIVE_IMAGE"),myEditor);
		
		alternativeIconChooser.setIcon((ImageIcon)TBoardConstants
				.getAlternativeIcon(map));
	}

	// Creates the following chooser panel
	private void createFollowingBoardPanel() {
		Map map = getAttributeMap();

		// Get the board list from the editor
		ArrayList boardList = (ArrayList)getBoardContainer().getEditor()
				.getProject().getBoardList().clone();
		ArrayList<String> boardListName = new ArrayList<String>();
		for (int i=0; i<boardList.size(); i++){
			boardListName.add(((TBoard)boardList.get(i)).getBoardName());
		}
		// Delete the current board
		boardListName.remove(getBoard().getBoardName());

		clickCellActionPanel = new TClickCellActionsPanel(boardListName);

		clickCellActionPanel.setFollowingBoard(TBoardConstants
				.getFollowingBoardName(map));
		clickCellActionPanel.setAccumulated(TBoardConstants.isAccumulated(map));
	}
        
        private void createSinthesizerConfig(){
            Map map = getAttributeMap();
            
            synthesizerPanel = new TSynthesizerConfig(false);            
        String synthMode = TBoardConstants.getSynthMode(map);
        if(synthMode != null){
            synthesizerPanel.setMode(Integer.parseInt(synthMode));
        }else{
            synthesizerPanel.setMode(0);
        }
            synthesizerPanel.setTexto(TBoardConstants.getVoiceText(map));
            synthesizerPanel.setCopyFrom(textField);            
        }

	// Creates the sound chooser panel
	private void createSoundChooser() {
		Map map = getAttributeMap();

		soundChooser = new TSoundChooser();

		soundChooser.setSoundFilePath(TBoardConstants.getSoundFile(map));
	}
	
	// Creates the alternative sound chooser panel
	private void createAlternativeSoundChooser() {
		Map map = getAttributeMap();

		alternativeSoundChooser = new TAlternativeSoundChooser();

		alternativeSoundChooser.setSoundFilePath(TBoardConstants.getAlternativeSoundFile(map));
	}
	
	// Creates the video chooser panel
	private void createVideoChooser() {
		Map map = getAttributeMap();

		videoChooser = new TVideoChooser();

		videoChooser.setVideoFilePath(TBoardConstants.getVideoFile(map));
		videoChooser.setVideoFileURL(TBoardConstants.getVideoURL(map));
	}
	
	// Creates the send text chooser panel
	private void createSendTextChooser() {
		Map map = getAttributeMap();

		sendTextChooser = new TSendTextChooser(getBoard());

		sendTextChooser.setText(TBoardConstants.getSendText(map));
		sendTextChooser.setTextReceiver(TBoardConstants.getSendTextTarget(map));
		sendTextChooser.setTimer(TBoardConstants.getSendTextTimer(map));
	}

	/* (non-Javadoc)
	 * @see tico.editor.dialogs.TComponentDialog#newComponentsAttributeMap()
	 */
	protected Map newComponentsAttributeMap() {
		// Create used variables
		Map nested = new Hashtable();
		Map attributeMap = new Hashtable();
		Vector removalAttributes = new Vector();

		// Set cell text and format
		TBoardConstants.setText(attributeMap, textField.getText());

		TBoardConstants.setForeground(attributeMap, fontModel.getFontColor());
		TBoardConstants.setFont(attributeMap, new Font(fontModel.getFontFace(),
				fontModel.getFontStyle(), fontModel.getFontSize()));

		// Set cell background and border
		Color color = borderSelectionPanel.getBorderColor();
		if (color != null)
			TBoardConstants.setBorderColor(attributeMap, color);
		else
			removalAttributes.add(TBoardConstants.BORDERCOLOR);
		
		Color alternativeBorderColor = alternativeBorderSelectionPanel.getBorderColor();
		
		if (alternativeBorderColor!=null){			
			TBoardConstants.setAlternativeBorderColor(attributeMap,alternativeBorderColor);
		}
		else
			removalAttributes.add(TBoardConstants.ALTERNATIVE_BORDER_COLOR);

		TBoardConstants.setLineWidth(attributeMap, borderSelectionPanel
				.getBorderSize());
		
		int alternativeBorderLinewidth = alternativeBorderSelectionPanel.getBorderSize();
		
		TBoardConstants.setAlternativeLinewidth(attributeMap,alternativeBorderLinewidth);
	
		Color background = backgroundSelectionPanel.getBackgroundColor();
		if (background != null)
			TBoardConstants.setBackground(attributeMap, background);
		else
			removalAttributes.add(TBoardConstants.BACKGROUND);

		Color gradient = backgroundSelectionPanel.getGradientColor();
		if (gradient != null)
			TBoardConstants.setGradientColor(attributeMap, gradient);
		else
			removalAttributes.add(TBoardConstants.GRADIENTCOLOR);

		// Set cell static image and other image properties
		ImageIcon icon = iconChooser.getIcon();
		if (icon != null){
			TBoardConstants.setIcon(attributeMap, icon);
		}
		else
			removalAttributes.add(TBoardConstants.ICON);

		TBoardConstants.setVerticalTextPosition(attributeMap, iconChooser
				.getVerticalTextPosition());

		// Set cell alternative image
		ImageIcon alternativeIcon = alternativeIconChooser.getIcon();
		if (alternativeIcon != null)
			TBoardConstants.setAlternativeIcon(attributeMap, alternativeIcon);
		else
			removalAttributes.add(TBoardConstants.ALTERNATIVE_ICON);

		// Set cell sound file
		String soundFile = soundChooser.getSoundFilePath();
		if (soundFile != null)
			TBoardConstants.setSoundFile(attributeMap, soundFile);
		else
			removalAttributes.add(TBoardConstants.SOUND_FILE);
		
		// Set cell alternative sound file
		String alternativeSoundFile = alternativeSoundChooser.getSoundFilePath();
		if (alternativeSoundFile != null)
			TBoardConstants.setAlternativeSoundFile(attributeMap, alternativeSoundFile);
		else
			removalAttributes.add(TBoardConstants.ALTERNATIVE_SOUND_FILE);
                
                
                //Set cell voice parameters
                String voiceName = synthesizerPanel.getNombreVoz();
                if(!voiceName.equals(""))
                    TBoardConstants.setVoiceName(attributeMap, voiceName);
                else
                    removalAttributes.add(TBoardConstants.VOICE_NAME);
                
                String voiceText = synthesizerPanel.getTexto();
                if(voiceText != null){
                    TBoardConstants.setVoiceText(attributeMap, voiceText);
                }else{
                    removalAttributes.add(TBoardConstants.VOICE_TEXT);
                }
                
                int mode = synthesizerPanel.getMode();
                TBoardConstants.setSynthMode(attributeMap, String.valueOf(mode));
                          
		
		// Set cell video file
		String videoFile = videoChooser.getVideoFilePath();
		if (videoFile != null)
			TBoardConstants.setVideoFile(attributeMap, videoFile);
		else
			removalAttributes.add(TBoardConstants.VIDEO_FILE);
		
		// Set cell video URL
		String videoURL = videoChooser.getVideoFileURL();
		if (videoURL != null)
			TBoardConstants.setVideoURL(attributeMap, videoURL);
		else
			removalAttributes.add(TBoardConstants.VIDEO_URL);
		
		//Set Environment Action
		Object action = orderList.getSelectedValue();

		if (action!=null && !action.toString().trim().equals(""))
		{	int pos=orderList.getSelectedIndex();
			TBoardConstants.setEnvironmentAction(attributeMap,TEnvironment.getCode(action.toString()));
			TBoardConstants.setPositionAction(attributeMap, pos);
		}
		else 
		{	removalAttributes.add(TBoardConstants.ENVIRONMENT_ACTION);
			removalAttributes.add(TBoardConstants.ACTION_POSITION);
		}
		// Set cell send text properties
		TComponent component = sendTextChooser.getTextReceiver();
		if (component != null) {
			TBoardConstants.setSendTextTarget(attributeMap, component);
			TBoardConstants
					.setSendText(attributeMap, sendTextChooser.getText());
			TBoardConstants.setSendTextTimer(attributeMap, sendTextChooser
					.getTimer());
		} else {
			removalAttributes.add(TBoardConstants.SEND_TEXT_TARGET);
			removalAttributes.add(TBoardConstants.SEND_TEXT);
			removalAttributes.add(TBoardConstants.SEND_TEXT_TIMER);
		}

		// Set click action cell attributes
		TBoardConstants.setAccumulated(attributeMap, clickCellActionPanel
				.getAccumulated());
		String followingBoard = clickCellActionPanel.getFollowingBoard();

		if (followingBoard != null)
			TBoardConstants.setFollowingBoard(attributeMap, followingBoard);
		else
			removalAttributes.add(TBoardConstants.FOLLOWING_BOARD);

		// Apply removal attributes
		TBoardConstants.setRemoveAttributes(attributeMap, removalAttributes
				.toArray());

		// Set cell id
		TBoardConstants.setId(attributeMap, idTextField.getText());

		nested.put(getComponent(), attributeMap);

		return nested;
	}

	
}