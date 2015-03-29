/*
 * File: TEditorDialog.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Sep 4, 2006
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

import tico.components.TLanguageComboBox;
import tico.configuration.TLanguage;
import tico.configuration.TSetup;
import tico.editor.TEditor;

/**
 * Dialog to change <code>TEditor</code> preferences.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TEditorDialog extends TPropertiesDialog {

	private JPanel languageChooserPanel;

	private TLanguageComboBox languageComboBox;

	/**
	 * Creates a new <code>TEditorDialog</code> to edit the specified
	 * <code>editor</code> preferences.
	 * 
	 * @param editor The <code>editor</code> to edit
	 */
	public TEditorDialog(TEditor editor) {
		super(editor);
		setTitle(TLanguage.getString("TEditorDialog.TITLE"));

		createTabbedPane();

		setVisible(true);
	}

	// Create the main dialog tabbed pane
	private void createTabbedPane() {
		getPropertiesPane().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		JTabbedPane tabbedPane = new JTabbedPane();

		tabbedPane.addTab(TLanguage.getString("TEditorDialog.TAB_PREFERENCES"),
				createPreferencesPanel());

		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 1;
		getPropertiesPane().add(tabbedPane, c);
	}

	// Create the editor preferences panel
	private JPanel createPreferencesPanel() {
		JPanel preferencesPanel = new JPanel();

		GridBagConstraints c = new GridBagConstraints();
		preferencesPanel.setLayout(new GridBagLayout());

		createLanguageChooser();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 0;
		preferencesPanel.add(languageChooserPanel, c);

		return preferencesPanel;
	}

	// Creates the language chooser panel
	private void createLanguageChooser() {
		languageChooserPanel = new JPanel();

		languageChooserPanel.setBorder(new TitledBorder(BorderFactory
				.createEtchedBorder(Color.WHITE, new Color(165, 163, 151)),
				TLanguage.getString("TEditorDialog.LANGUAGE")));

		JLabel languageLabel = new JLabel(TLanguage
				.getString("TEditorDialog.LANGUAGE"));
		languageComboBox = new TLanguageComboBox(TSetup.getLanguage());

		GridBagConstraints c = new GridBagConstraints();
		languageChooserPanel.setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 15, 10, 0);
		c.gridx = 0;
		c.gridy = 0;
		languageChooserPanel.add(languageLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 15, 10, 15);
		c.gridx = 1;
		c.gridy = 0;
		languageChooserPanel.add(languageComboBox, c);
	}

	/* (non-Javadoc)
	 * @see tico.editor.dialogs.TPropertiesDialog#applyValues()
	 */
	protected boolean applyValues() {
		if (!TSetup.getLanguage().equals(languageComboBox.getLanguage())) {
			TSetup.setLanguage(languageComboBox.getLanguage());
			
			String language = languageComboBox.getLanguage();
			String languageChangeAdvice = TLanguage.getString(language, "TEditorDialog.LANGUAGE_CHANGE_ADVICE");
			String languageChange = TLanguage.getString(language, "TEditorDialog.LANGUAGE_CHANGE");
			
			JOptionPane.showMessageDialog(null, languageChangeAdvice,
			languageChange,	JOptionPane.INFORMATION_MESSAGE);
		}
		return true;
	}
}
