/*
 * File: TInterpreterOptionsDialog.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Antonio Rodríguez
 * 
 * Date: May 23, 2007
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

package tico.interpreter.dialogs;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tico.components.TButton;
import tico.components.TDialog;
import tico.components.TImageChooser;
import tico.configuration.TLanguage;
import tico.interpreter.TInterpreter;
import tico.interpreter.TInterpreterConstants;

public class TInterpreterOptionsDialog extends TDialog {

	TImageChooser cursorPanel;

	JPanel panelButtons;

	JPanel speedPanel;

	JPanel cellPanel;
	
	JPanel mouseModePanel;

	public static int value;

	JSpinner spinner;

	SpinnerNumberModel model;

	private void createCursorPanel() {
		cursorPanel = new TImageChooser(TLanguage.getString("TInterpreterOptionDialog.CURSOR_IMAGE"));
		if (TInterpreterConstants.interpreterCursor != null) {
			ImageIcon cursorIcon = new ImageIcon(
					TInterpreterConstants.interpreterCursor);
			cursorPanel.setIcon(cursorIcon);
		}

	}

	private void createButtonPanel() {
		panelButtons = new JPanel();
		TButton AcceptButton = new TButton(TLanguage.getString("TInterpreterOptionDialog.BUTTON_ACCEPT"));
		AcceptButton.setFocusable(true);
		TButton CancelButton = new TButton(TLanguage.getString("TInterpreterOptionDialog.BUTTON_CANCEL"));

		AcceptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Accept_ActionPerformed(e);
			}
		});

		CancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Cancel_ActionPerformed(e);
			}

		});

		panelButtons.add(AcceptButton);
		panelButtons.add(CancelButton);

	}

	private void createSpeedPanel() {

		speedPanel = new JPanel();
		speedPanel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)),
				TLanguage.getString("TInterpreterOptionDialog.WAIT_CURSOR")));
		JSlider gros = new JSlider(JSlider.HORIZONTAL, 1000, 20000,
				TInterpreterConstants.interpreterDelay);

		value = gros.getValue();

		gros.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				JSlider source = (JSlider) e.getSource();

				if (!source.getValueIsAdjusting()) {
					value = (int) source.getValue();

				}
			}

		});
		Dictionary<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(1000), new JLabel("1"));
		labelTable.put(new Integer(10000), new JLabel("10"));
		labelTable.put(new Integer(20000), new JLabel("20"));
		gros.setLabelTable(labelTable);
		gros.setMinorTickSpacing(1000);
		gros.setPaintTicks(true);
		gros.setPaintLabels(true);
		speedPanel.add(gros);
	}

	private void createNumCellsPanel() {
		cellPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		cellPanel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), ""));
		JLabel text = new JLabel(TLanguage.getString("TInterpreterOptionDialog.MAX_ACCUMULATED_CELLS"));
		cellPanel.add(text);

		model = new SpinnerNumberModel(
				TInterpreterConstants.accumulatedCells, 0, 
				TInterpreterConstants.maxAccumulatedCells, 1);
		spinner = new JSpinner(model);

		cellPanel.add(spinner);

	}
	
	private void createMouseModePanel() {
		mouseModePanel = new JPanel();
		mouseModePanel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)),
				TLanguage.getString("TInterpreterOptionDialog.MOUSE_MODE")));
		
		JRadioButton automaticScanningMode = new JRadioButton(TLanguage.getString("TInterpreterMouseMode.AUTOMATIC_SCANNING"));
		automaticScanningMode.setActionCommand(TInterpreterConstants.AUTOMATIC_SCANNING_MODE);
		automaticScanningMode.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				TInterpreterConstants.mouseModeSelected = TInterpreterConstants.AUTOMATIC_SCANNING_MODE;
			}});
		if (TInterpreterConstants.mouseModeSelected.equals(TInterpreterConstants.AUTOMATIC_SCANNING_MODE)){
			automaticScanningMode.setSelected(true);
		}
		
		JRadioButton directSelectionMode = new JRadioButton(TLanguage.getString("TInterpreterMouseMode.DIRECT_SELECTION"));
		directSelectionMode.setActionCommand(TInterpreterConstants.DIRECT_SELECTION_MODE);
		
		directSelectionMode.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				TInterpreterConstants.mouseModeSelected = TInterpreterConstants.DIRECT_SELECTION_MODE;
			}});
		if (TInterpreterConstants.mouseModeSelected.equals(TInterpreterConstants.DIRECT_SELECTION_MODE)){
			directSelectionMode.setSelected(true);
		}
		
		JRadioButton manualScanningMode = new JRadioButton(TLanguage.getString("TInterpreterMouseMode.MANUAL_SCANNING"));
		manualScanningMode.setActionCommand(TInterpreterConstants.MANUAL_SCANNING_MODE);
		
		manualScanningMode.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				TInterpreterConstants.mouseModeSelected = TInterpreterConstants.MANUAL_SCANNING_MODE;
			}});
		if (TInterpreterConstants.mouseModeSelected.equals(TInterpreterConstants.MANUAL_SCANNING_MODE)){
			manualScanningMode.setSelected(true);
		}
				
		ButtonGroup mouseModeOptions = new ButtonGroup();
		mouseModeOptions.add(directSelectionMode);
		mouseModeOptions.add(automaticScanningMode);
		mouseModeOptions.add(manualScanningMode);
		
		mouseModePanel.add(directSelectionMode);
		mouseModePanel.add(automaticScanningMode);
		mouseModePanel.add(manualScanningMode);
	}

	public TInterpreterOptionsDialog(TInterpreter interprete) {

		super(interprete, TLanguage.getString("TInterpreterOptionDialog.NAME"),	true);
		
		JPanel backPanel = new JPanel();
		createCursorPanel();
		createButtonPanel();
		createSpeedPanel();
		createNumCellsPanel();
		createMouseModePanel();
		
		GridBagConstraints c = new GridBagConstraints();
		backPanel.setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 20, 0, 20);
		c.gridx = 0;
		c.gridy = 0;
		backPanel.add(cursorPanel, c);

		c.gridx = 0;
		c.gridy = 1;
		backPanel.add(cellPanel, c);

		c.gridx = 0;
		c.gridy = 2;
		backPanel.add(speedPanel, c);

		c.gridx = 0;
		c.gridy = 3;
		backPanel.add(mouseModePanel, c);
		
		c.gridx = 0;
		c.gridy = 4;
		backPanel.add(panelButtons, c);

		this.setBounds(100, 100, 380, 380);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(true);
		this.setContentPane(backPanel);
		this.pack();
		this.setVisible(true);
	}

	public void Accept_ActionPerformed(ActionEvent e) {

		if (TImageChooser.path != null) {
			TInterpreterConstants.interpreterCursor = TImageChooser.path;
		}else if (cursorPanel.getImageGalleryButton().getPath()!=null){
			TInterpreterConstants.interpreterCursor = cursorPanel.getImageGalleryButton().getPath();
		}else {
			TInterpreterConstants.interpreterCursor = null;
		}
		TInterpreterConstants.interpreterDelay = value;
		TInterpreterConstants.accumulatedCells = model.getNumber().intValue();
		setVisible(false);
	}

	public void Cancel_ActionPerformed(ActionEvent e) {
		setVisible(false);
	}
}
