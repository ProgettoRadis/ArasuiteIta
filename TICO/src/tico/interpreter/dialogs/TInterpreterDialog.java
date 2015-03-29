/*
 * File: TInterpreterDialog.java
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
import tico.interpreter.TInterpreter;

public class TInterpreterDialog extends TProjectInterpreterDialog {

		private JPanel languageChooserPanel;

		private TLanguageComboBox languageComboBox;

		/**
		 * Creates a new <code>TInterpreterDialog</code> to edit the
		 * <code>interpreter</code> preferences.
		 * 
		 * @param interpreter
		 *            The <code>interpreter</code> to edit
		 */
		public TInterpreterDialog (TInterpreter interpreter) {
			super(interpreter);
			setTitle(TLanguage.getString("TEditorDialog.TITLE"));

			createTabbedPane();

			setVisible(true);
		}

		// Create the main dialog tabbed pane
		private void createTabbedPane() {
			getPropertiesPane().setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();

			JTabbedPane tabbedPane = new JTabbedPane();
			/*Creates one Tab of preferences*/
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

		private void createLanguageChooser() {
			languageChooserPanel = new JPanel();

			languageChooserPanel.setBorder(new TitledBorder(BorderFactory
					.createEtchedBorder(Color.black, new Color(165, 163, 151)),
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

		/*
		 * (non-Javadoc)
		 * 
		 * @see tico.board.dialogs.TPropertiesDialog#applyValues()
		 * Set actual language
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


