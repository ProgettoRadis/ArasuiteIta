/*
 * File: TActionSet.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Sep 13, 2006
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

package tico.editor;

import java.util.Hashtable;
import java.util.Map;

import javax.swing.Action;

import tico.components.resources.TFileUtils;
import tico.editor.actions.TAdjustHorizontalGapAction;
import tico.editor.actions.TAdjustVerticalGapAction;
import tico.editor.actions.TAlignBottomAction;
import tico.editor.actions.TAlignCenterHorizontalAction;
import tico.editor.actions.TAlignCenterVerticalAction;
import tico.editor.actions.TAlignLeftAction;
import tico.editor.actions.TAlignRightAction;
import tico.editor.actions.TAlignTopAction;
import tico.editor.actions.TBackAction;
import tico.editor.actions.TBoardDeleteAction;
import tico.editor.actions.TBoardExportAction;
import tico.editor.actions.TBoardExportImageAction;
import tico.editor.actions.TBoardNewAction;
import tico.editor.actions.TBoardPropertiesAction;
import tico.editor.actions.TCopyAction;
import tico.editor.actions.TCutAction;
import tico.editor.actions.TDeleteAction;
import tico.editor.actions.TEditorAboutAction;
import tico.editor.actions.TEditorExitAction;
import tico.editor.actions.TEditorPreferencesAction;
import tico.editor.actions.TFitHeightAction;
import tico.editor.actions.TFitWidthAction;
import tico.editor.actions.TFrontAction;
import tico.editor.actions.TGalleryManagerAction;
import tico.editor.actions.THandlerCellAction;
import tico.editor.actions.THandlerControllerCellAction;
import tico.editor.actions.THandlerGridAction;
import tico.editor.actions.THandlerLabelAction;
import tico.editor.actions.THandlerLineAction;
import tico.editor.actions.THandlerOvalAction;
import tico.editor.actions.THandlerRectangleAction;
import tico.editor.actions.THandlerRoundRectAction;
import tico.editor.actions.THandlerSelectionAction;
import tico.editor.actions.THandlerTextAreaAction;
import tico.editor.actions.TPasteAction;
import tico.editor.actions.TProjectImportAction;
import tico.editor.actions.TProjectNewAction;
import tico.editor.actions.TProjectNewAndroidAction;
import tico.editor.actions.TProjectOpenAction;
import tico.editor.actions.TProjectPrintAction;
import tico.editor.actions.TProjectPropertiesAction;
import tico.editor.actions.TProjectSaveAction;
import tico.editor.actions.TProjectSaveAsAction;
import tico.editor.actions.TRedoAction;
import tico.editor.actions.TSelectAllAction;
import tico.editor.actions.TUndoAction;
import tico.rules.actions.TBoardValidationAction;
import tico.rules.actions.TLimitationsAdminAction;
import tico.rules.actions.TProjectValidationAction;
import tico.rules.actions.TRulesAdminAction;
import tico.rules.actions.TUsersAdminAction;

/**
 * Map of actions that can be done to an editor.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TActionSet {
	// Action map codes
	/**
	 * The <code>TEditorPreferencesAction</code> id
	 */
	public final static String EDITOR_PREFERENCES = "editorPreferences";

	/**
	 * The <code>TEditorAbout</code> id
	 */
	public final static String EDITOR_ABOUT = "editorAbout";

	/**
	 * The <code>TEditorExitAction</code> id
	 */
	public final static String EDITOR_EXIT_ACTION = "editorExitAction";

	/**
	 * The <code>TProyectNewAction</code> id
	 */
	public final static String PROJECT_NEW_ACTION = "proyectNewAction";
	
	
	/**
	 * The <code>TProyectNewAndroidAction</code> id
	 */
	public final static String PROJECT_NEW_ANDROID_ACTION = "proyectNewAndroidAction";
	
	

	/**
	 * The <code>TProyectOpenAction</code> id
	 */
	public final static String PROJECT_OPEN_ACTION = "proyectOpenAction";

	/**
	 * The <code>TProyectSaveAction</code> id
	 */
	public final static String PROJECT_SAVE_ACTION = "proyectSaveAction";

	/**
	 * The <code>TProyectSaveAsAction</code> id
	 */
	public final static String PROJECT_SAVE_AS_ACTION = "proyectSaveAsAction";

	/**
	 * The <code>TProyectImportAction</code> id
	 */
	public final static String PROJECT_IMPORT_ACTION = "proyectImportAction";

	/**
	 * The <code>TProjectInterpretAction</code> id
	 */
	public final static String PROJECT_INTERPRET_ACTION = "projectInterpretAction";

	/**
	 * The <code>TProjectPropertiesAction</code> id
	 */
	public final static String PROJECT_PROPERTIES_ACTION = "projectPropertiesAction";

	/**
	 * The <code>TProjectPrintAction</code> id
	 */
	public final static String PROJECT_PRINT_ACTION = "projectPrintAction";
	
	/**
	 * The <code>TProyectValidationAction</code> id
	 */
	public final static String PROJECT_VALIDATION_ACTION = "projectValidationAction";
	
	/**
	 * The <code>TBoardNewAction</code> id
	 */
	public final static String BOARD_NEW_ACTION = "boardNewAction";

	/**
	 * The <code>TBoardDeleteAction</code> id
	 */
	public final static String BOARD_DELETE_ACTION = "boardDeleteAction";

	/**
	 * The <code>TBoardPropertiesAction</code> id
	 */
	public final static String BOARD_PROPERTIES_ACTION = "boardPropertiesAction";

	/**
	 * The <code>TBoardExportAction</code> id
	 */
	public final static String BOARD_EXPORT_ACTION = "boardExportAction";

	/**
	 * The <code>TBoardInterpretAction</code> id
	 */
	public final static String BOARD_INTERPRET_ACTION = "boardInterpretAction";

	/**
	 * The <code>TBoardExportJPGAction</code> id
	 */
	public final static String BOARD_EXPORT_JPG_ACTION = "boardExportJPGAction";

	/**
	 * The <code>TBoardExportPNGAction</code> id
	 */
	public final static String BOARD_EXPORT_PNG_ACTION = "boardExportPNGAction";
	
	/**
	 * The <code>TBoardValidationAction</code> id
	 */
	public final static String BOARD_VALIDATION_ACTION = "boardValidationAction";
	
	/**
	 * The <code>THorizontalGapAction</code> id
	 */
	public final static String HORIZONTAL_GAP_ACTION = "horizontalGapAction";

	/**
	 * The <code>TVerticalGapAction</code> id
	 */
	public final static String VERTICAL_GAP_ACTION = "verticalGapAction";

	/**
	 * The <code>TAlignBottomAction</code> id
	 */
	public final static String ALIGN_BOTTOM_ACTION = "alignBottomAction";

	/**
	 * The <code>TAlignTopAction</code> id
	 */
	public final static String ALIGN_TOP_ACTION = "alignTopAction";

	/**
	 * The <code>TAlignLeftAction</code> id
	 */
	public final static String ALIGN_LEFT_ACTION = "alignLeftAction";

	/**
	 * The <code>TAlignRightAction</code> id
	 */
	public final static String ALIGN_RIGHT_ACTION = "alignRightAction";

	/**
	 * The <code>TAlignHorizontalAction</code> id
	 */
	public final static String ALIGN_HORIZONTAL_ACTION = "alignHorizontalAction";

	/**
	 * The <code>TAlignVerticalAction</code> id
	 */
	public final static String ALIGN_VERTICAL_ACTION = "alignVerticalAction";

	/**
	 * The <code>TFitWidthAction</code> id
	 */
	public final static String FIT_WIDTH_ACTION = "fitWidthAction";

	/**
	 * The <code>TFitHeightAction</code> id
	 */
	public final static String FIT_HEIGHT_ACTION = "fitHeightAction";

	/**
	 * The <code>TPasteAction</code> id
	 */
	public final static String PASTE_ACTION = "pasteAction";

	/**
	 * The <code>TCopyAction</code> id
	 */
	public final static String COPY_ACTION = "copyAction";

	/**
	 * The <code>TCutAction</code> id
	 */
	public final static String CUT_ACTION = "cutAction";

	/**
	 * The <code>TDeleteAction</code> id
	 */
	public final static String DELETE_ACTION = "deleteAction";

	/**
	 * The <code>TSelectAllAction</code> id
	 */
	public final static String SELECT_ALL_ACTION = "selectAllAction";

	/**
	 * The <code>TUndoAction</code> id
	 */
	public final static String UNDO_ACTION = "undoAction";

	/**
	 * The <code>TRedoAction</code> id
	 */
	public final static String REDO_ACTION = "redoAction";

	/**
	 * The <code>TBackAction</code> id
	 */
	public final static String BACK_ACTION = "backAction";

	/**
	 * The <code>TFrontAction</code> id
	 */
	public final static String FRONT_ACTION = "frontAction";

	/**
	 * The <code>TCellHandlerAction</code> id
	 */
	public final static String CELL_HANDLER = "cellHandlerAction";
	
	/**
	 * The <code>TControllerCellHandlerAction</code> id
	 */
	public final static String CELL_CONTROLLER_HANDLER = "controllerCellHandlerAction";

	/**
	 * The <code>TGridHandlerAction</code> id
	 */
	public final static String GRID_HANDLER = "gridHandlerAction";

	/**
	 * The <code>TLabelHandlerAction</code> id
	 */
	public final static String LABEL_HANDLER = "labelHandlerAction";

	/**
	 * The <code>TLineHandlerAction</code> id
	 */
	public final static String LINE_HANDLER = "lineHandlerAction";

	/**
	 * The <code>TRectangleHandlerAction</code> id
	 */
	public final static String RECTANGLE_HANDLER = "rectangleHandlerAction";

	/**
	 * The <code>TRoundRectHandlerAction</code> id
	 */
	public final static String ROUND_RECT_HANDLER = "roundRectHandlerAction";

	/**
	 * The <code>TSelectionHandlerAction</code> id
	 */
	public final static String SELECTION_HANDLER = "selectionHandlerAction";

	/**
	 * The <code>TOvalHandlerAction</code> id
	 */
	public final static String OVAL_HANDLER = "ovalHandlerAction";

	/**
	 * The <code>TTextAreaHandlerAction</code> id
	 */
	public final static String TEXT_AREA_HANDLER = "textAreaHandlerAction";
	
	/**
	 * The <code>TUsersAdminAction</code> id
	 */
	public final static String USERS_ADMIN_ACTION = "usersAdminAction";
	
	/**
	 * The <code>TAtributtesAdminAction</code> id
	 */
	public final static String LIMITATIONS_ADMIN_ACTION = "limitationsAdminAction";
	
	/**
	 * The <code>TUsersAdminAction</code> id
	 */
	public final static String RULES_ADMIN_ACTION = "rulesAdminAction";
	
	/**
	 * The <code>TGalleryManagerAction</code> id
	 */
	public final static String GALLERY_MANAGER_ACTION = "galleryManagerAction";

	// Map which contains the actionName-action pairs
	private Map actionSet;

	/**
	 * Creates a new <code>TActionSet</code> for the specified <code>editor</code>
	 * with all the possible actions.
	 * 
	 * @param editor The specified <code>editor</code>
	 */
	public TActionSet(TEditor editor) {
		actionSet = new Hashtable();

		// Editor actions
		actionSet.put(EDITOR_PREFERENCES, new TEditorPreferencesAction(editor));
		actionSet.put(EDITOR_ABOUT, new TEditorAboutAction(editor));
		
		// Project actions
		actionSet.put(PROJECT_NEW_ACTION, new TProjectNewAction(editor));
		
		actionSet.put(PROJECT_NEW_ANDROID_ACTION, new TProjectNewAndroidAction(editor));
		
		actionSet.put(PROJECT_OPEN_ACTION, new TProjectOpenAction(editor));
		actionSet.put(PROJECT_SAVE_ACTION, new TProjectSaveAction(editor));
		actionSet.put(PROJECT_SAVE_AS_ACTION, new TProjectSaveAsAction(editor));
		actionSet.put(PROJECT_IMPORT_ACTION, new TProjectImportAction(editor));
		actionSet.put(PROJECT_PROPERTIES_ACTION, new TProjectPropertiesAction(
				editor));
		actionSet.put(PROJECT_PRINT_ACTION, new TProjectPrintAction(editor));
		actionSet.put(PROJECT_VALIDATION_ACTION, new TProjectValidationAction(editor));
		actionSet.put(EDITOR_EXIT_ACTION, new TEditorExitAction(editor));

		// Board actions
		actionSet.put(BOARD_NEW_ACTION, new TBoardNewAction(editor));
		actionSet.put(BOARD_DELETE_ACTION, new TBoardDeleteAction(editor));
		actionSet.put(BOARD_PROPERTIES_ACTION, new TBoardPropertiesAction(
				editor));
		actionSet.put(BOARD_EXPORT_ACTION, new TBoardExportAction(editor));
		actionSet.put(BOARD_EXPORT_JPG_ACTION, new TBoardExportImageAction(
				editor, TFileUtils.JPG));
		actionSet.put(BOARD_EXPORT_PNG_ACTION, new TBoardExportImageAction(
				editor, TFileUtils.PNG));
		actionSet.put(BOARD_VALIDATION_ACTION, new TBoardValidationAction(editor));
		
		// Adjust, align and fit actions
		actionSet.put(HORIZONTAL_GAP_ACTION, new TAdjustHorizontalGapAction(
				editor));
		actionSet
				.put(VERTICAL_GAP_ACTION, new TAdjustVerticalGapAction(editor));
		actionSet.put(ALIGN_BOTTOM_ACTION, new TAlignBottomAction(editor));
		actionSet.put(ALIGN_TOP_ACTION, new TAlignTopAction(editor));
		actionSet.put(ALIGN_LEFT_ACTION, new TAlignLeftAction(editor));
		actionSet.put(ALIGN_RIGHT_ACTION, new TAlignRightAction(editor));
		actionSet.put(ALIGN_HORIZONTAL_ACTION,
				new TAlignCenterHorizontalAction(editor));
		actionSet.put(ALIGN_VERTICAL_ACTION, new TAlignCenterVerticalAction(
				editor));
		actionSet.put(FIT_WIDTH_ACTION, new TFitWidthAction(editor));
		actionSet.put(FIT_HEIGHT_ACTION, new TFitHeightAction(editor));
		// Copy-paste actions
		actionSet.put(PASTE_ACTION, new TPasteAction(editor));
		actionSet.put(COPY_ACTION, new TCopyAction(editor));
		actionSet.put(CUT_ACTION, new TCutAction(editor));
		actionSet.put(DELETE_ACTION, new TDeleteAction(editor));
		actionSet.put(SELECT_ALL_ACTION, new TSelectAllAction(editor));
		// Undo manager actions
		actionSet.put(UNDO_ACTION, new TUndoAction(editor));
		actionSet.put(REDO_ACTION, new TRedoAction(editor));
		// Elements order actions
		actionSet.put(BACK_ACTION, new TBackAction(editor));
		actionSet.put(FRONT_ACTION, new TFrontAction(editor));
		// Painter actions
		actionSet.put(CELL_HANDLER, new THandlerCellAction(editor));
		actionSet.put(CELL_CONTROLLER_HANDLER, new THandlerControllerCellAction(editor));
		actionSet.put(GRID_HANDLER, new THandlerGridAction(editor));
		actionSet.put(LABEL_HANDLER, new THandlerLabelAction(editor));
		actionSet.put(LINE_HANDLER, new THandlerLineAction(editor));
		actionSet.put(RECTANGLE_HANDLER, new THandlerRectangleAction(editor));
		actionSet.put(ROUND_RECT_HANDLER, new THandlerRoundRectAction(editor));
		actionSet.put(SELECTION_HANDLER, new THandlerSelectionAction(editor));
		actionSet.put(OVAL_HANDLER, new THandlerOvalAction(editor));
		actionSet.put(TEXT_AREA_HANDLER, new THandlerTextAreaAction(editor));
		
		// Administration actions
		actionSet.put(USERS_ADMIN_ACTION, new TUsersAdminAction(editor));
		actionSet.put(LIMITATIONS_ADMIN_ACTION, new TLimitationsAdminAction(editor));
		actionSet.put(RULES_ADMIN_ACTION, new TRulesAdminAction(editor));
		
		// Gallery Manager
		actionSet.put(GALLERY_MANAGER_ACTION, new TGalleryManagerAction(
				editor));
	}

	/**
	 * Returns the <code>action</code> with the specified <code>actionId</code>.
	 * 
	 * @param actionId The specified <code>actionId</code>
	 * @return The <code>action</code> with the specified <code>actionId</code>
	 */
	public Action getAction(String actionId) {
		return (Action)actionSet.get(actionId);
	}

}
