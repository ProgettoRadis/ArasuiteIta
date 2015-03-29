/*
 * File: TEditorMenuBar.java
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
 *     	along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 */

package tico.editor;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import tico.components.TMenuItem;
import tico.components.TToolBarContainer;
import tico.configuration.TLanguage;
import tico.editor.actions.THideToolBarAction;
import tico.imageGallery.TImageGalleryMenuBar;

/**
 * Editor's menu bar.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TEditorMenuBar extends JMenuBar {
	private TEditor editor;

	private TToolBarContainer toolBarContainer;

	private TActionSet actionSet;
	
	//public static final File pluginsDir = new File("plugins");
	
	//private PluginManager pluginManager;

	/**
	 * Creates a new <code>TEditorMenuBar</code> for the specified <code>editor</code>.
	 * 
	 * @param editor The specified <code>editor</code>
	 * @param toolBarContainer The specified <code>editor</code>'s tool bar container
	 */
	public TEditorMenuBar(TEditor editor, TToolBarContainer toolBarContainer) {
		super();

		this.editor = editor;
		this.toolBarContainer = toolBarContainer;
		this.actionSet = editor.getActionSet();

		createFileMenu();
		createEditMenu();
		createViewMenu();
		createToolsMenu();
		createBoardMenu();
		createProjectMenu();
		
		createDynamicToolsMenu();
		
		createHelpMenu();
	}

	private void createFileMenu() {
		// Create the menu
		JMenu menu = new JMenu(TLanguage.getString("TEditorMenuBar.FILE_MENU"));
		menu.setMnemonic(KeyEvent.VK_A);

		// Create the menu items
		TMenuItem menuItem;
		JMenu submenu;

		menuItem = new TMenuItem(actionSet
				.getAction(TActionSet.PROJECT_NEW_ACTION));
		menuItem.setMnemonic(KeyEvent.VK_N);//menuItem.setMnemonic(KeyEvent.VK_T);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.CTRL_MASK));
		menu.add(menuItem);
		
		//para android
		menuItem = new TMenuItem(actionSet
				.getAction(TActionSet.PROJECT_NEW_ANDROID_ACTION));
		menuItem.setMnemonic(KeyEvent.VK_D);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
		menu.add(menuItem);

		menu.add(new JSeparator());
		menuItem = new TMenuItem(actionSet
				.getAction(TActionSet.PROJECT_OPEN_ACTION));
		menuItem.setMnemonic(KeyEvent.VK_A);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.CTRL_MASK));
		menu.add(menuItem);

		menuItem = new TMenuItem(actionSet
				.getAction(TActionSet.PROJECT_SAVE_ACTION));
		menuItem.setMnemonic(KeyEvent.VK_G);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
				ActionEvent.CTRL_MASK));
		menuItem.setEnabled(false);
		menu.add(menuItem);

		menuItem = new TMenuItem(actionSet
				.getAction(TActionSet.PROJECT_SAVE_AS_ACTION));
		menuItem.setMnemonic(KeyEvent.VK_O);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
				ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
		menuItem.setEnabled(false);
		menu.add(menuItem);

		menu.add(new JSeparator());
		menuItem = new TMenuItem(actionSet
				.getAction(TActionSet.PROJECT_PRINT_ACTION));
		menuItem.setMnemonic(KeyEvent.VK_P);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				ActionEvent.CTRL_MASK));
		menuItem.setEnabled(false);
		menu.add(menuItem);

		menu.add(new JSeparator());
		menuItem = new TMenuItem(actionSet
				.getAction(TActionSet.PROJECT_IMPORT_ACTION));
		menuItem.setMnemonic(KeyEvent.VK_I);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
				ActionEvent.CTRL_MASK));
		menu.add(menuItem);

		submenu = new JMenu(TLanguage.getString("TEditorMenuBar.EXPORT_MENU"));
		submenu.setMnemonic(KeyEvent.VK_E);

		menuItem = new TMenuItem(actionSet
				.getAction(TActionSet.BOARD_EXPORT_ACTION));
		menuItem.setText(TLanguage.getString("TEditorMenuBar.BOARD"));
		menuItem.setMnemonic(KeyEvent.VK_E);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,
				ActionEvent.CTRL_MASK));
		submenu.add(menuItem);

		submenu.add(new JSeparator());
		menuItem = new TMenuItem(actionSet
				.getAction(TActionSet.BOARD_EXPORT_PNG_ACTION));
		menuItem.setText(TLanguage.getString("TEditorMenuBar.TO_PNG"));
		menuItem.setMnemonic(KeyEvent.VK_P);
		submenu.add(menuItem);
		menuItem = new TMenuItem(actionSet
				.getAction(TActionSet.BOARD_EXPORT_JPG_ACTION));
		menuItem.setText(TLanguage.getString("TEditorMenuBar.TO_JPG"));
		menuItem.setMnemonic(KeyEvent.VK_J);
		submenu.add(menuItem);

		menu.add(submenu);

		menu.add(new JSeparator());

		menuItem = new TMenuItem(actionSet
				.getAction(TActionSet.EDITOR_EXIT_ACTION));
		menuItem.setMnemonic(KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));
		menu.add(menuItem);

		// Add the menu
		add(menu);
	}

	private void createEditMenu() {
		// Create the menu
		JMenu menu = new JMenu(TLanguage.getString("TEditorMenuBar.EDIT_MENU"));
		menu.setMnemonic(KeyEvent.VK_E);

		// Create the menu items
		TMenuItem menuItem;

		menuItem = new TMenuItem(actionSet.getAction(TActionSet.UNDO_ACTION));
		menuItem.setMnemonic(KeyEvent.VK_D);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				ActionEvent.CTRL_MASK));
		menu.add(menuItem);

		menuItem = new TMenuItem(actionSet.getAction(TActionSet.REDO_ACTION));
		menuItem.setMnemonic(KeyEvent.VK_R);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,
				ActionEvent.CTRL_MASK));
		menu.add(menuItem);

		menu.add(new JSeparator());
		menuItem = new TMenuItem(actionSet.getAction(TActionSet.CUT_ACTION));
		menuItem.setMnemonic(KeyEvent.VK_T);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				ActionEvent.CTRL_MASK));
		menu.add(menuItem);

		menuItem = new TMenuItem(actionSet.getAction(TActionSet.COPY_ACTION));
		menuItem.setMnemonic(KeyEvent.VK_C);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				ActionEvent.CTRL_MASK));
		menu.add(menuItem);

		menuItem = new TMenuItem(actionSet.getAction(TActionSet.PASTE_ACTION));
		menuItem.setMnemonic(KeyEvent.VK_P);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
				ActionEvent.CTRL_MASK));
		menu.add(menuItem);

		menu.add(new JSeparator());
		
		menuItem = new TMenuItem(actionSet.getAction(TActionSet.DELETE_ACTION));
		menuItem.setMnemonic(KeyEvent.VK_B);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		menu.add(menuItem);

		menuItem = new TMenuItem(actionSet
				.getAction(TActionSet.SELECT_ALL_ACTION));
		menuItem.setMnemonic(KeyEvent.VK_C);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.CTRL_MASK));
		menu.add(menuItem);

		menu.add(new JSeparator());
		menuItem = new TMenuItem(actionSet
				.getAction(TActionSet.EDITOR_PREFERENCES));
		menuItem.setMnemonic(KeyEvent.VK_E);
		menu.add(menuItem);

		add(menu);
	}

	private void createViewMenu() {
		// Create the menu
		JMenu menu = new JMenu(TLanguage.getString("TEditorMenuBar.VIEW_MENU"));
		menu.setMnemonic(KeyEvent.VK_V);

		// Create the menu items
		JCheckBoxMenuItem cbMenuItem;

		cbMenuItem = new JCheckBoxMenuItem(new THideToolBarAction(editor,
				toolBarContainer.getToolBar(0)));
		cbMenuItem.setState(true);
		cbMenuItem.setMnemonic(KeyEvent.VK_I);
		cbMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
				ActionEvent.CTRL_MASK | ActionEvent.ALT_MASK));
		menu.add(cbMenuItem);

		cbMenuItem = new JCheckBoxMenuItem(new THideToolBarAction(editor,
				toolBarContainer.getToolBar(1)));
		cbMenuItem.setState(true);
		cbMenuItem.setMnemonic(KeyEvent.VK_E);
		cbMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				ActionEvent.CTRL_MASK | ActionEvent.ALT_MASK));
		menu.add(cbMenuItem);

		cbMenuItem = new JCheckBoxMenuItem(new THideToolBarAction(editor,
				toolBarContainer.getToolBar(2)));
		cbMenuItem.setState(true);
		cbMenuItem.setMnemonic(KeyEvent.VK_H);
		cbMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
				ActionEvent.CTRL_MASK | ActionEvent.ALT_MASK));
		menu.add(cbMenuItem);

		cbMenuItem = new JCheckBoxMenuItem(new THideToolBarAction(editor,
				toolBarContainer.getToolBar(3)));
		cbMenuItem.setState(true);
		cbMenuItem.setMnemonic(KeyEvent.VK_F);
		cbMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
				ActionEvent.CTRL_MASK | ActionEvent.ALT_MASK));
		menu.add(cbMenuItem);

		cbMenuItem = new JCheckBoxMenuItem(new THideToolBarAction(editor,
				toolBarContainer.getToolBar(4)));
		cbMenuItem.setState(true);
		cbMenuItem.setMnemonic(KeyEvent.VK_T);
		cbMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,
				ActionEvent.CTRL_MASK | ActionEvent.ALT_MASK));
		menu.add(cbMenuItem);

		cbMenuItem = new JCheckBoxMenuItem(new THideToolBarAction(editor,
				toolBarContainer.getToolBar(5)));
		cbMenuItem.setState(true);
		cbMenuItem.setMnemonic(KeyEvent.VK_A);
		cbMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.CTRL_MASK | ActionEvent.ALT_MASK));
		menu.add(cbMenuItem);

		add(menu);
	}

	private void createToolsMenu() {
		// Create the menu
		JMenu menu = new JMenu(TLanguage.getString("TEditorMenuBar.TOOLS_MENU"));
		menu.setMnemonic(KeyEvent.VK_H);

		// Create the menu items
		TMenuItem menuItem;

		menuItem = new TMenuItem(actionSet
				.getAction(TActionSet.SELECTION_HANDLER));
		menuItem.setMnemonic(KeyEvent.VK_E);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				ActionEvent.SHIFT_MASK));
		menu.add(menuItem);

		menu.add(new JSeparator());
		menuItem = new TMenuItem(actionSet.getAction(TActionSet.CELL_HANDLER));
		menuItem.setMnemonic(KeyEvent.VK_C);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				ActionEvent.SHIFT_MASK));
		menu.add(menuItem);
		
		menuItem = new TMenuItem(actionSet.getAction(TActionSet.CELL_CONTROLLER_HANDLER));
		menuItem.setMnemonic(KeyEvent.VK_N);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.SHIFT_MASK));
		menu.add(menuItem);

		/**/menuItem = new TMenuItem(actionSet.getAction(TActionSet.GRID_HANDLER));
		menuItem.setMnemonic(KeyEvent.VK_U);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U,
				ActionEvent.SHIFT_MASK));
		menu.add(menuItem);/**/

		menu.add(new JSeparator());
		menuItem = new TMenuItem(actionSet.getAction(TActionSet.LINE_HANDLER));
		menuItem.setMnemonic(KeyEvent.VK_L);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
				ActionEvent.SHIFT_MASK));
		menu.add(menuItem);

		menuItem = new TMenuItem(actionSet
				.getAction(TActionSet.RECTANGLE_HANDLER));
		menuItem.setMnemonic(KeyEvent.VK_R);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				ActionEvent.SHIFT_MASK));
		menu.add(menuItem);

		menuItem = new TMenuItem(actionSet
				.getAction(TActionSet.ROUND_RECT_HANDLER));
		menuItem.setMnemonic(KeyEvent.VK_D);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
				ActionEvent.SHIFT_MASK));
		menu.add(menuItem);

		menuItem = new TMenuItem(actionSet.getAction(TActionSet.OVAL_HANDLER));
		menuItem.setMnemonic(KeyEvent.VK_O);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				ActionEvent.SHIFT_MASK));
		menu.add(menuItem);

		menu.add(new JSeparator());
		menuItem = new TMenuItem(actionSet
				.getAction(TActionSet.TEXT_AREA_HANDLER));
		menuItem.setMnemonic(KeyEvent.VK_A);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.SHIFT_MASK));
		menu.add(menuItem);

		menuItem = new TMenuItem(actionSet.getAction(TActionSet.LABEL_HANDLER));
		menuItem.setMnemonic(KeyEvent.VK_T);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,
				ActionEvent.SHIFT_MASK));
		menu.add(menuItem);

		add(menu);
	}

	private void createBoardMenu() {
		// Create the menu
		JMenu menu = new JMenu(TLanguage.getString("TEditorMenuBar.BOARD_MENU"));
		menu.setMnemonic(KeyEvent.VK_T);

		// Create the menu items
		TMenuItem menuItem;

		menuItem = new TMenuItem(actionSet
				.getAction(TActionSet.BOARD_PROPERTIES_ACTION));
		menuItem.setMnemonic(KeyEvent.VK_R);
		menu.add(menuItem);

		add(menu);
	}

	private void createProjectMenu() {
		// Create the menu
		JMenu menu = new JMenu(TLanguage
				.getString("TEditorMenuBar.PROJECT_MENU"));
		menu.setMnemonic(KeyEvent.VK_P);

		// Create the menu items
		TMenuItem menuItem;

		menuItem = new TMenuItem(actionSet
				.getAction(TActionSet.PROJECT_PROPERTIES_ACTION));
		menuItem.setMnemonic(KeyEvent.VK_R);
		menu.add(menuItem);

		add(menu);
	}

	
	private JMenu createValidationMenu() {
		// Create the menu
		JMenu menu = new JMenu(TLanguage.getString("TEditorMenuBar.VALIDATION_MENU"));	

		// Create the menu items
		TMenuItem menuItem;
		JMenu submenu;
		
		// Create submenu
		submenu = new JMenu(TLanguage.getString("TEditorMenuBar.ADMIN_MENU"));
		// Create item
		menuItem = new TMenuItem(actionSet
				.getAction(TActionSet.USERS_ADMIN_ACTION));
		submenu.add(menuItem);
		submenu.add(new JSeparator());
		// Create item
		menuItem = new TMenuItem(actionSet
				.getAction(TActionSet.LIMITATIONS_ADMIN_ACTION));
		submenu.add(menuItem);
		submenu.add(new JSeparator());
		// Create item
		menuItem = new TMenuItem(actionSet
				.getAction(TActionSet.RULES_ADMIN_ACTION));
		submenu.add(menuItem);
		
		menu.add(submenu);
		menu.add(new JSeparator());
		
		// Create submenu
		submenu = new JMenu(TLanguage.getString("TEditorMenuBar.VALIDATE_MENU"));

		// Create item
		menuItem = new TMenuItem(actionSet
				.getAction(TActionSet.BOARD_VALIDATION_ACTION));
		submenu.add(menuItem);
		submenu.add(new JSeparator());
		
		menuItem = new TMenuItem(actionSet
				.getAction(TActionSet.PROJECT_VALIDATION_ACTION));
		menuItem.setMnemonic(KeyEvent.VK_F);
		submenu.add(menuItem);
		
		menu.add(submenu);
		
		//add(menu);
		return menu;
	}
	
	private void createDynamicToolsMenu(){
		//Define the extension point and add the existing plugins
		
		/*JMenu menuValidation = createValidationMenu();
		JMenu menu = new JMenu(TLanguage.getString("TEditorMenuBar.DYNAMIC_MENU"));
		
		menu.add(menuValidation);
		menu.add(new JSeparator());
		
		TImageGalleryMenuBar imageGalleryMenu = new TImageGalleryMenuBar();
        menu.add(imageGalleryMenu.createImageGalleryMenu(editor));	
		this.add(menu);*/
		
		// Create the menu
		JMenu menu = new JMenu(TLanguage
				.getString("TEditorMenuBar.DYNAMIC_MENU"));
		menu.setMnemonic(KeyEvent.VK_P);

		// Create the menu items
		TMenuItem menuItem;

		menuItem = new TMenuItem(actionSet
				.getAction(TActionSet.GALLERY_MANAGER_ACTION));
		menuItem.setMnemonic(KeyEvent.VK_R);
		menu.add(menuItem);

		add(menu);
	}

	private void createHelpMenu() {
		// Create the menu
		JMenu menu = new JMenu(TLanguage.getString("TEditorMenuBar.HELP_MENU"));
		menu.setMnemonic(KeyEvent.VK_Y);

		// Create the menu items
		TMenuItem menuItem;

		menuItem = new TMenuItem(actionSet.getAction(TActionSet.EDITOR_ABOUT));
		menuItem.setMnemonic(KeyEvent.VK_A);
		menu.add(menuItem);

		add(menu);

	}
}
