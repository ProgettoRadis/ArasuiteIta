/*
 * File: TSendTextChooser.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Aug 22, 2006
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

package tico.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import org.jgraph.graph.AttributeMap;

import tico.board.TBoard;
import tico.board.TBoardConstants;
import tico.board.components.TComponent;
import tico.configuration.TLanguage;

/**
 * Component to choose between all the text receiver components from a specified
 * board. It allows to choose the destiny component, the text to send and the
 * timer which determines the time the send text will be shown on the destiny
 * component.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TSendTextChooser extends JPanel {
	// The default component border title value
	private final static String DEFAULT_TITLE = TLanguage.getString("TSendTextChooser.TITLE");
	// Default initial timer
	private final static int DEFATULT_TIMER = 5;

	// Text components panel (top panel
	private JPanel textPanel;
	// Send text field
	private TTextField sendText;
	// Text receiver and time spiner panel (bottom panel)
	private JPanel propertiesPanel;
	// Destiny text area selector
	private JComboBox textReceiverComboBox;
	// Timer spinner selection
	private JSpinner timerSpinner;

	/**
	 * Creates a new <code>TSendTextChooser</code> for the specified
	 * <code>board</code>.
	 * 
	 * @param board The <code>board</code> which contains the text receiver
	 * components we want to choose
	 */
	public TSendTextChooser(TBoard board) {
		this(board, DEFAULT_TITLE);
	}

	/**
	 * Creates a new <code>TSendTextChooser</code> for the specified
	 * <code>board</code>.
	 * 
	 * @param board The <code>TBoard</code> which contains the text receiver
	 * components we want to choose
	 * @param title The selected component border <code>title</title>
	 */
	public TSendTextChooser(TBoard board, String title) {
		super();
		// Creates the border of the component
		setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151)), title));
		// Creates the components
		createTextPanel();
		createPropertiesPanel(board);
		// Updates the components
		updateComponents();
		
		// Places the components
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 0, 0);
		c.weightx = 1.0;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		add(textPanel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 0, 0);
		c.weightx = 1.0;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 1;
		add(propertiesPanel, c);
	}
	
	// Creates the text panel (top panel)
	private void createTextPanel() {
		textPanel = new JPanel();
		textPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel sendTextLabel = new JLabel(TLanguage.getString("TSendTextChooser.SEND") + " ");
		sendText = new TTextField();
		sendText.setColumns(25);

		textPanel.add(sendTextLabel);
		textPanel.add(sendText);
	}

	// Creates the properties panel (bottom panel)
	private void createPropertiesPanel(TBoard board) {
		propertiesPanel = new JPanel();
		propertiesPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		JLabel textAreaLabel = new JLabel(TLanguage.getString("TSendTextChooser.TO") + " ");
		createTextComponentComboBox(board);

		JLabel timerLabel = new JLabel(" " + TLanguage.getString("TSendTextChooser.DURING") + " ");
		timerSpinner = new JSpinner(new SpinnerNumberModel(DEFATULT_TIMER, 1,
				1000, 1));
		JLabel secondsLabel = new JLabel(TLanguage.getString("TSendTextChooser.SECONDS"));

		propertiesPanel.add(textAreaLabel);
		propertiesPanel.add(textReceiverComboBox);
		propertiesPanel.add(timerLabel);
		propertiesPanel.add(timerSpinner);
		propertiesPanel.add(secondsLabel);
	}
	
	// Create text component chooser
	private void createTextComponentComboBox(TBoard board) {
		textReceiverComboBox = new JComboBox();
		textReceiverComboBox.setPreferredSize(new Dimension(120,20));

		// Get the list of the text areas in the board
		Vector componentList = new Vector();
		componentList.add(null);

		Object[] components = board.getGraphLayoutCache().getCells(
				board.getGraphLayoutCache().getCellViews());
		for (int i = 0; i < components.length; i++) {
			AttributeMap map = ((TComponent)components[i]).getAttributes();
			if (map != null)
				if (TBoardConstants.isTextReceiver(map))
					componentList.add(components[i]);
		}

		textReceiverComboBox.setModel(new DefaultComboBoxModel(componentList));
		textReceiverComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateComponents();
			}
		});
	}
	
	// Update chooser components disabling or enabling them when its needed
	private void updateComponents() {
		if (textReceiverComboBox.getModel().getSize() < 2) {
			// If there are no text areas in the board disable everything
			sendText.setEnabled(false);
			textReceiverComboBox.setEnabled(false);
			timerSpinner.setEnabled(false);
		} else if (textReceiverComboBox.getSelectedItem() == null) {
			// If there are no selected text area, disable text and timer fields
			sendText.setEnabled(false);
			timerSpinner.setEnabled(false);
		} else {
			// Enable everything
			sendText.setEnabled(true);
			timerSpinner.setEnabled(true);			
		}
	}

	/**
	 * Returns the selected <code>text</code>.
	 * 
	 * @return The selected <code>text</code>
	 */	
	public String getText() {
		return sendText.getText();
	}

	/**
	 * Set the <code>text</code>. 
	 * 
	 * @param text The <code>text</code> to set
	 */
	public void setText(String text) {
		sendText.setText(text);
	}

	/**
	 * Returns the selected <code>timer</code>.
	 * 
	 * @return The selected <code>timer</code>
	 */		
	public int getTimer() {
		return ((Integer)timerSpinner.getValue()).intValue();
	}

	/**
	 * Set the <code>timer</code>. 
	 * 
	 * @param timer The <code>timer</code> to set
	 */	
	public void setTimer(int timer) {
		if (timer == 0)
			timer = DEFATULT_TIMER;
		
		timerSpinner.setValue(new Integer(timer));
	}

	/**
	 * Returns the selected <code>textReceiver</code>.
	 * 
	 * @return The selected <code>textReceiver</code>
	 */		
	public TComponent getTextReceiver() {
		if (textReceiverComboBox.getSelectedItem() instanceof TComponent)
			return (TComponent)textReceiverComboBox.getSelectedItem();
		
		return null;
	}

	/**
	 * Set the <code>textReceiver</code>. 
	 * 
	 * @param textReceiver The <code>textReceiver</code> to set
	 */	
	public void setTextReceiver(TComponent textReceiver) {
		textReceiverComboBox.setSelectedItem(textReceiver);
	}
}

