/*
 * File: TProjectPrintAction.java
 * 		This file is part of Tico, an application
 * 		to create and perform interactive communication boards to be
 * 		used by people with severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Jan 30, 2006
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

package tico.editor.actions;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;

import javax.swing.JOptionPane;

import tico.board.TBoardConstants;
import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;
import tico.editor.TBoardContainer;
import tico.editor.TEditor;

/**
 * Action wich prints the current editor project. Each board of the editor
 * is printed in a different page.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TProjectPrintAction extends TEditorAbstractAction {
	/**
	 * Constructor for TProjectPrintAction.
	 * 
	 * @param editor The boards' editor
	 */
	public TProjectPrintAction(TEditor editor) {
		super(editor, TLanguage.getString("TProjectPrintAction.NAME"),
				TResourceManager.getImageIcon("archive-print-22.png"));
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		
		// Get the print manager
		PrinterJob printJob = PrinterJob.getPrinterJob();
		Book book = new Book();
		int pages = getEditor().getBoardContainerCount();
		for (int i = 0; i < pages; i++) {
			TBoardContainer boardContainer = getEditor().getBoardContainer(i);
			// Clear selection
			boardContainer.getBoard().clearSelection();
			Dimension boardSize = TBoardConstants.getSize(boardContainer.getBoard().getAttributes(null));
			PageFormat page = printJob.defaultPage();
			if (boardSize.getWidth() > boardSize.getHeight())
				page.setOrientation(PageFormat.LANDSCAPE);
			else
				page.setOrientation(PageFormat.PORTRAIT);
			book.append(boardContainer, page);
		}
		// Set the component to print
		printJob.setPageable(book);
		if (printJob.printDialog()) {
			try {
				printJob.print();
			} catch (Exception ex) {
				// If the print fails show an error message
				JOptionPane.showMessageDialog(null,
						TLanguage.getString("TProjectPrintAction.PRINT_ERROR"),
						TLanguage.getString("TProjectPrintAction.ERROR") + "!",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
