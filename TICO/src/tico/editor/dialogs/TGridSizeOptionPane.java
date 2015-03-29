/*
 * File: TGridSizeOptionPane.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Sep 5, 2006
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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import tico.components.TButton;
import tico.components.TDialog;
import tico.configuration.TLanguage;
import tico.editor.TEditor;

/**
 * Creates a dialog that allows to select a grid size.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TGridSizeOptionPane extends TDialog {
	private static int default_rows = 2;

	private static int default_columns = 3;

	private JRadioButton size2x2RadioButton;

	private JRadioButton size3x3RadioButton;

	private JRadioButton size4x4RadioButton;

	private JRadioButton customRadioButton;

	private JSpinner rowsSpinner;

	private JSpinner columnsSpinner;
	
	private TButton acceptButton;

	/**
	 * Creates a new <code>TGridSizeOptionPane</code> in the specified
	 * <code>editor</code>.
	 * 
	 * @param editor The specified <code>editor</code>
	 */
	public TGridSizeOptionPane(TEditor editor) {
		this(editor, default_rows, default_columns);
	}

	/**
	 * Creates a new <code>TGridSizeOptionPane</code> in the specified
	 * <code>editor</code> and with the specified initial <code>rows</code>
	 * and <code>columns</code> values.
	 * 
	 * @param editor The specified <code>editor</code>
	 * @param rows The specified <code>rows</code> value
	 * @param columns The specified <code>columns</code> value
	 */
	public TGridSizeOptionPane(TEditor editor, int rows, int columns) {
		super(editor, TLanguage.getString("TGridSizeOptionPane.TITLE"), true);

		createDimensionChooser();

		setDimension(rows, columns);
		
		setResizable(false);
		pack();
		setLocationRelativeTo(editor);
		setVisible(true);
	}

	private void createDimensionChooser() {
		JPanel definedChooserPanel = createDefinedChooser();
		JPanel customChooserPanel = createCustomChooser();
		JPanel buttonPanel = createButtonPanel();

		getContentPane().setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 5, 10);
		c.gridx = 0;
		c.gridy = 0;
		getContentPane().add(definedChooserPanel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 0, 0);
		c.gridx = 1;
		c.gridy = 0;
		getContentPane().add(customChooserPanel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		getContentPane().add(buttonPanel, c);
	}

	private JPanel createDefinedChooser() {
		JPanel definedChooserPanel = new JPanel();

		ButtonGroup definedOptionsButtonGroup = new ButtonGroup();

		ActionListener listener = new GridSizeActionListener();

		size2x2RadioButton = new JRadioButton("2x2");
		size3x3RadioButton = new JRadioButton("3x3");
		size4x4RadioButton = new JRadioButton("4x4");
		customRadioButton = new JRadioButton("Custom");

		size2x2RadioButton.addActionListener(listener);
		size3x3RadioButton.addActionListener(listener);
		size4x4RadioButton.addActionListener(listener);
		customRadioButton.addActionListener(listener);

		definedOptionsButtonGroup.add(size2x2RadioButton);
		definedOptionsButtonGroup.add(size3x3RadioButton);
		definedOptionsButtonGroup.add(size4x4RadioButton);
		definedOptionsButtonGroup.add(customRadioButton);

		definedChooserPanel.setLayout(new GridLayout(4, 1));

		definedChooserPanel.add(size2x2RadioButton);
		definedChooserPanel.add(size3x3RadioButton);
		definedChooserPanel.add(size4x4RadioButton);
		definedChooserPanel.add(customRadioButton);

		return definedChooserPanel;
	}

	private JPanel createCustomChooser() {
		JPanel customChooserPanel = new JPanel();

		SpinnerModel rowsSpinnerModel = new SpinnerNumberModel(default_rows, 1,
				1000, 1);
		SpinnerModel columnsSpinnerModel = new SpinnerNumberModel(
				default_columns, 1, 1000, 1);

		JLabel rowsLabel = new JLabel(TLanguage.getString("TGridSizeOptionPane.ROWS"));
		rowsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		rowsSpinner = new JSpinner(rowsSpinnerModel);

		JLabel columnsLabel = new JLabel(TLanguage.getString("TGridSizeOptionPane.COLUMNS"));
		columnsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		columnsSpinner = new JSpinner(columnsSpinnerModel);

		customChooserPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 0, 0);
		c.gridx = 0;
		c.gridy = 0;
		customChooserPanel.add(rowsLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 5, 10, 10);
		c.gridx = 1;
		c.gridy = 0;
		customChooserPanel.add(rowsSpinner, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 10, 0);
		c.gridx = 0;
		c.gridy = 1;
		customChooserPanel.add(columnsLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 10, 10);
		c.gridx = 1;
		c.gridy = 1;
		customChooserPanel.add(columnsSpinner, c);

		return customChooserPanel;
	}

	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();

		acceptButton = new TButton(TLanguage.getString("TGridSizeOptionPane.BUTTON_ACCEPT"));
		acceptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		buttonPanel.add(acceptButton);
		
		return buttonPanel;
	}

	/**
	 * Returns the selected <code>dimension</code>.
	 * 
	 * @return The selected <code>dimension</code>
	 */
	public Dimension getDimension() {
		if (size2x2RadioButton.isSelected()) {
			default_rows = 2;
			default_columns = 2;
			return new Dimension(2, 2);
		}

		if (size3x3RadioButton.isSelected()) {
			default_rows = 3;
			default_columns = 3;
			return new Dimension(3, 3);
		}

		if (size4x4RadioButton.isSelected()) {
			default_rows = 4;
			default_columns = 4;
			return new Dimension(4, 4);
		}

		default_rows = ((Integer)rowsSpinner.getValue()).intValue();
		default_columns = ((Integer)columnsSpinner.getValue()).intValue();
		return new Dimension(default_columns, default_rows);
	}

	/**
	 * Sets the <code>rows</code> and <code>columns</code> values.
	 * 
	 * @param rows The specified <code>rows</code> value to set
	 * @param columns The specified <code>columns</code> value to set
	 */
	private void setDimension(int rows, int columns) {
		rowsSpinner.setEnabled(false);
		columnsSpinner.setEnabled(false);

		if ((rows == 2) && (columns == 2))
			size2x2RadioButton.setSelected(true);
		else if ((rows == 3) && (columns == 3))
			size3x3RadioButton.setSelected(true);
		else if ((rows == 4) && (columns == 4))
			size4x4RadioButton.setSelected(true);
		else {
			customRadioButton.setSelected(true);
			rowsSpinner.getModel().setValue(new Integer(rows));
			columnsSpinner.getModel().setValue(new Integer(columns));
			rowsSpinner.setEnabled(true);
			columnsSpinner.setEnabled(true);
		}
	}

	private class GridSizeActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (customRadioButton.isSelected()) {
				rowsSpinner.setEnabled(true);
				columnsSpinner.setEnabled(true);
			} else {
				rowsSpinner.setEnabled(false);
				columnsSpinner.setEnabled(false);
			}
		}

	}
}
