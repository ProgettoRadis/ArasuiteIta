/*
 * File: TAlignToolBar.java
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

import tico.components.TToolBar;
import tico.components.TToolBarButton;
import tico.configuration.TLanguage;
import tico.editor.TActionSet;
import tico.editor.TEditor;

/**
 * Tool bar with the align action buttons.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TAlignToolBar extends TToolBar {
	// Horizontal gap buttons
	private TToolBarButton horizontalGapButton;
	private TToolBarButton verticalGapButton;
	// Size buttons
	private TToolBarButton widthButton;
	private TToolBarButton heightButton;
	// Align buttons
	private TToolBarButton topAlignButton;
	private TToolBarButton bottomAlignButton;
	private TToolBarButton leftAlignButton;
	private TToolBarButton rightAlignButton;
	private TToolBarButton horizontalCenterButton;
	private TToolBarButton verticalCenterButton;
	// Depth buttons
	private TToolBarButton toFrontButton;
	private TToolBarButton toBackButton;
	
	/**
	 * Creates a new <code>TAlignToolBar</code>.
	 * 
	 * @param editor The <code>editor</code> actions receiver
	 */
	public TAlignToolBar(TEditor editor) {
		super(TLanguage.getString("TAlignToolBar.NAME"));

		horizontalGapButton = new TToolBarButton(editor.getActionSet()
				.getAction(TActionSet.HORIZONTAL_GAP_ACTION));
		verticalGapButton = new TToolBarButton(editor.getActionSet().getAction(
				TActionSet.VERTICAL_GAP_ACTION));
		widthButton = new TToolBarButton(editor.getActionSet().getAction(
				TActionSet.FIT_WIDTH_ACTION));
		heightButton = new TToolBarButton(editor.getActionSet().getAction(
				TActionSet.FIT_HEIGHT_ACTION));
		topAlignButton = new TToolBarButton(editor.getActionSet().getAction(
				TActionSet.ALIGN_TOP_ACTION));
		bottomAlignButton = new TToolBarButton(editor.getActionSet().getAction(
				TActionSet.ALIGN_BOTTOM_ACTION));
		leftAlignButton = new TToolBarButton(editor.getActionSet().getAction(
				TActionSet.ALIGN_LEFT_ACTION));
		rightAlignButton = new TToolBarButton(editor.getActionSet().getAction(
				TActionSet.ALIGN_RIGHT_ACTION));
		horizontalCenterButton = new TToolBarButton(editor.getActionSet()
				.getAction(TActionSet.ALIGN_HORIZONTAL_ACTION));
		verticalCenterButton = new TToolBarButton(editor.getActionSet()
				.getAction(TActionSet.ALIGN_VERTICAL_ACTION));
		toFrontButton = new TToolBarButton(editor.getActionSet().getAction(
				TActionSet.FRONT_ACTION));
		toBackButton = new TToolBarButton(editor.getActionSet().getAction(
				TActionSet.BACK_ACTION));

		add(horizontalGapButton);
		add(verticalGapButton);
		addSeparator();
		add(widthButton);
		add(heightButton);
		addSeparator();
		add(topAlignButton);
		add(leftAlignButton);
		add(bottomAlignButton);
		add(rightAlignButton);
		add(horizontalCenterButton);
		add(verticalCenterButton);
		addSeparator();
		add(toFrontButton);
		add(toBackButton);
	}
}