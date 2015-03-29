/*
 * File: THandlersToolBar.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Aug 17, 2006
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

package tico.editor.toolbars;

import javax.swing.ButtonGroup;

import org.jgraph.graph.BasicMarqueeHandler;

import tico.board.TBoard;
import tico.components.TToolBar;
import tico.components.TToolBarToggleButton;
import tico.configuration.TLanguage;
import tico.editor.TActionSet;
import tico.editor.TEditor;
import tico.editor.handler.TCellMarqueeHandler;
import tico.editor.handler.TControllerCellMarqueeHandler;
import tico.editor.handler.TGridMarqueeHandler;
import tico.editor.handler.TLabelMarqueeHandler;
import tico.editor.handler.TLineMarqueeHandler;
import tico.editor.handler.TOvalMarqueeHandler;
import tico.editor.handler.TRectangleMarqueeHandler;
import tico.editor.handler.TRoundRectMarqueeHandler;
import tico.editor.handler.TTextAreaMarqueeHandler;

/**
 * Tool bar with the handlers selection buttons. It allows to change the editors
 * current board handler.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class THandlersToolBar extends TToolBar {
	// The editor whom selected components will receive the text format changes
	private TEditor editor;

	// Handler buttons
	private TToolBarToggleButton selectButton;
	private TToolBarToggleButton cellButton;
	private TToolBarToggleButton controllerCellButton;
	private TToolBarToggleButton gridButton;
	private TToolBarToggleButton textAreaButton;
	private TToolBarToggleButton labelButton;
	private TToolBarToggleButton lineButton;
	private TToolBarToggleButton ovalButton;
	private TToolBarToggleButton rectangleButton;
	private TToolBarToggleButton roundRectangleButton;
	
	/**
	 * Creates a new <code>THandlersToolBar</code>.
	 * 
	 * @param editor The <code>editor</code> handlers change receiver
	 */
	public THandlersToolBar(TEditor editor) {
		super(TLanguage.getString("THandlersToolBar.NAME"));

		this.editor = editor;

		ButtonGroup handlersButtonGroup = new ButtonGroup();

		selectButton = new TToolBarToggleButton(editor.getActionSet()
				.getAction(TActionSet.SELECTION_HANDLER));
		cellButton = new TToolBarToggleButton(editor.getActionSet().getAction(
				TActionSet.CELL_HANDLER));
		controllerCellButton = new TToolBarToggleButton(editor.getActionSet().getAction(
				TActionSet.CELL_CONTROLLER_HANDLER));
		gridButton = new TToolBarToggleButton(editor.getActionSet().getAction(
				TActionSet.GRID_HANDLER));
		textAreaButton = new TToolBarToggleButton(editor.getActionSet()
				.getAction(TActionSet.TEXT_AREA_HANDLER));
		labelButton = new TToolBarToggleButton(editor.getActionSet().getAction(
				TActionSet.LABEL_HANDLER));
		lineButton = new TToolBarToggleButton(editor.getActionSet().getAction(
				TActionSet.LINE_HANDLER));
		ovalButton = new TToolBarToggleButton(editor.getActionSet().getAction(
				TActionSet.OVAL_HANDLER));
		rectangleButton = new TToolBarToggleButton(editor.getActionSet()
				.getAction(TActionSet.RECTANGLE_HANDLER));
		roundRectangleButton = new TToolBarToggleButton(editor.getActionSet()
				.getAction(TActionSet.ROUND_RECT_HANDLER));

		handlersButtonGroup.add(selectButton);
		handlersButtonGroup.add(cellButton);
		handlersButtonGroup.add(controllerCellButton);
		handlersButtonGroup.add(gridButton);
		handlersButtonGroup.add(textAreaButton);
		handlersButtonGroup.add(labelButton);
		handlersButtonGroup.add(lineButton);
		handlersButtonGroup.add(ovalButton);
		handlersButtonGroup.add(rectangleButton);
		handlersButtonGroup.add(roundRectangleButton);

		add(selectButton);
		addSeparator();
		add(cellButton);
		add(controllerCellButton);
		add(gridButton);
		addSeparator();
		add(textAreaButton);
		add(labelButton);
		addSeparator();
		add(lineButton);
		add(ovalButton);
		add(rectangleButton);
		add(roundRectangleButton);

		selectButton.setSelected(true);
	}

	/**
	 * Updates the handlers toggle buttons to the current editors board handler.
	 */
	public void updateHandlers() {
		TBoard board = editor.getCurrentBoard();
		BasicMarqueeHandler handler = null;

		if (board != null)
			handler = board.getMarqueeHandler();

		if (handler != null) {

			if (handler instanceof TCellMarqueeHandler)
				cellButton.setSelected(true);
			else if (handler instanceof TControllerCellMarqueeHandler)
				controllerCellButton.setSelected(true);
		/**/	else if (handler instanceof TGridMarqueeHandler)
				gridButton.setSelected(true);/**/
			else if (handler instanceof TTextAreaMarqueeHandler)
				textAreaButton.setSelected(true);
			else if (handler instanceof TLabelMarqueeHandler)
				labelButton.setSelected(true);
			else if (handler instanceof TLineMarqueeHandler)
				lineButton.setSelected(true);
			else if (handler instanceof TOvalMarqueeHandler)
				ovalButton.setSelected(true);
			else if (handler instanceof TRectangleMarqueeHandler)
				rectangleButton.setSelected(true);
			else if (handler instanceof TRoundRectMarqueeHandler)
				roundRectangleButton.setSelected(true);
			else
				selectButton.setSelected(true);
		}
	}
}