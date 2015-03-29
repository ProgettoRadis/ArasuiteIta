/*
 * File: TLauncher.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Oct 5, 2006
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
 *     	along with this program. If not, see <http://www.gnu.org/licenses/>. 
 */

package tico;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import tico.board.TProject;
import tico.board.encoding.InvalidFormatException;
import tico.components.resources.TFileUtils;
import tico.configuration.TLanguage;
import tico.configuration.TSetup;
import tico.editor.TEditor;

import tico.editor.TProjectHandler;
import tico.environment.TEnvironment;
import tico.interpreter.TInterpreter;
import tico.interpreter.TInterpreterProject;

/**
 * TEditor launcher class. It shows the <code>TEditor</code> window. If the
 * path of a Tico project file is set as the first argument, it loads the
 * project.
 * 
 * @author Pablo Mu�oz
 * @version 1.0 Nov 20, 2006
 */
public class TLauncher {
	/**
	 * Main function of the editor launcher. It car receive one argument that
	 * could be the path of a <i>Tico</i> project file. The usage is the
	 * following <code>tico [-e|-i] [<project_file>]</code>.
	 * 
	 * @param args TEditor arguments
	 */
	static File  selectedFile;
	public static void main(String[] args) {
		// Loads application setup
		try {
			TSetup.load();
		} catch (Exception e) {
			// If the configuration file fails show the message error
			JOptionPane.showMessageDialog(null, "Se ha producido un error " +
					"cargando el fichero de configuración. Se utilizará una " +
					"por defecto.", "Error!",
					JOptionPane.ERROR_MESSAGE);
		}
		
		// Loads language file
		try {
			TLanguage.initLanguage(TSetup.getLanguage());
		} catch (Exception e) {
			e.printStackTrace();
			// If the import fails show an error message
			JOptionPane.showMessageDialog(null, "Se ha producido un error " +
					"cargando el fichero de idioma. No se puede " +
					"ejecutar la aplicación.", "Error!",
					JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}

		// Loads environment file
		try {
			TEnvironment.initEnvironment("environment");
		} catch (IOException e) {
			// If the import fails show an error message
			JOptionPane.showMessageDialog(null, "Se ha producido un error " +
					"cargando el fichero de control de entorno. Esta " +
					"funcionalidad no estará disponible.", "Error!",
					JOptionPane.ERROR_MESSAGE);
		}

		switch (args.length) {
		case 0:
			new TEditor();
			new TInterpreter();
			//new TEditorAndroid();///////////////////////////////////////////////////////////////
			break;
		case 1:
			if (args[0].equals("-i"))
				new TInterpreter();
			else if (args[0].equals("-e"))
				new TEditor();
			
			else {
				displayUsage();
				System.exit(-1);
			}
			break;
		case 2:
			TProject initialProject = null;
			TInterpreterProject initialInterpreterProject = null;
			try {
				// Get the initial project file
				selectedFile = new File(args[1]);
				// If selectedFile do not exists
				if (!selectedFile.exists())
					JOptionPane.showMessageDialog(null,
							TLanguage.getString("TLauncher.NOT_EXIST_FILE_ERROR"),
							TLanguage.getString("ERROR") + "!",
							JOptionPane.ERROR_MESSAGE);
				// Check the file extension
				if (!TFileUtils.isProjectFile(selectedFile))
					JOptionPane.showMessageDialog(null,
							TLanguage.getString("TLauncher.INVALID_FILE_ERROR"),
							TLanguage.getString("ERROR") + "!",
							JOptionPane.ERROR_MESSAGE);
				// Load the project
				
				
			} catch (Exception e) {
			}
			// Run the corresponding application
			if (args[0].equals("-i")){
				try {
					initialInterpreterProject= TProjectHandler.loadProjectInterpreter(selectedFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				new TInterpreter(initialInterpreterProject);
			}
			else if (args[0].equals("-e")){
				try {
					initialProject = TProjectHandler.loadProject(selectedFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				new TEditor(initialProject);
			}
			else {
				displayUsage();
				System.exit(-1);
			}
			break;
		default:
			displayUsage();
			System.exit(-1);
		}
	}

	// Diplay the usage of the application
	private static void displayUsage() {
		System.out.println("Usage: tico [-e|-i] [<project_file>]");
	}
}
