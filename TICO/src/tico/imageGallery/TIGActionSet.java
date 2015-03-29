/*
 * File: TIGActionSet.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Patricia M. Jaray
 * 
 * Date: May 20, 2008
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

package tico.imageGallery;

import java.util.Hashtable;
import java.util.Map;

import javax.swing.Action;

import tico.editor.TEditor;
import tico.imageGallery.actions.*;
import tico.imageGallery.dataBase.TIGDataBase;

/**
 * Map of actions that can be done to the image gallery
 * 
 * @author Patricia M. Jaray
 * @version 1.0 May 20, 2008
 */
public class TIGActionSet {
	
	// Action map codes
	
	/**
	 * The <code>TIGKeyWordGalleryAction</code> id
	 */
	public final static String GALLERY_KEY_ACTION = "galleryOpenAction";
	
	/**
	 * The <code>TIGManageGallery</code> id
	 */
	public final static String GALLERY_NEW_IMAGE_ACTION = "galleryNewImageAction";
	
	/**
	 * The <code>TIGManageImageAction</code> id
	 */
	public final static String GALLERY_MANAGE_ACTION = "galleryManageAction";
	
	/**
	 * The <code>TIGDeleteImageAction</code> id
	 */
	public final static String  GALLERY_DELETE_ACTION= "galleryDeleteAction";
	
	/**
	 * The <code>TIGImportDBAction</code> id
	 */
	public final static String GALLERY_IMPORT_DB_ACTION = "galleryLoadDBAction";
	
	/**
	 * The <code>TIGExportDBAction</code> id
	 */
	public final static String GALLERY_EXPORT_DB_ACTION = "galleryExportDBAction";

	// Map which contains the actionName-action pairs
	private Map actionSet;

	/**
	 * Creates a new <code>TIGActionSet</code> for the specified <code>editor</code>
	 * with all the possible actions.
	 * 
	 * @param editor The specified <code>editor</code>
	 */
	public TIGActionSet(TEditor editor, TIGDataBase dataBase) {
		actionSet = new Hashtable();

		//Image Gallery actions
		actionSet.put(GALLERY_KEY_ACTION, new TIGKeyWordGalleryAction(editor, dataBase));
		actionSet.put(GALLERY_NEW_IMAGE_ACTION, new TIGManageGallery(editor, dataBase));
		actionSet.put(GALLERY_MANAGE_ACTION, new TIGManageImageAction(editor, dataBase));
		actionSet.put(GALLERY_DELETE_ACTION, new TIGDeleteImageAction(editor, dataBase));
		actionSet.put(GALLERY_IMPORT_DB_ACTION, new TIGImportDBAction(editor, dataBase));
		actionSet.put(GALLERY_EXPORT_DB_ACTION, new TIGExportDBAction(editor, dataBase));
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