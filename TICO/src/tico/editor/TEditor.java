/*
 * File: TEditor.java
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

package tico.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jgraph.graph.AttributeMap;

import tico.board.TBoard;
import tico.board.TBoardConstants;
import tico.board.TBoardModel;
import tico.board.TProject;
import tico.board.events.ProjectChangeEvent;
import tico.board.events.ProjectChangeListener;
import tico.components.TButton;
import tico.components.TOrderList;
import tico.components.TToolBarContainer;
import tico.components.TUnorderList;
import tico.components.events.OrderChangeEvent;
import tico.components.events.OrderChangeListener;
import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;
import tico.editor.actions.TEditorExitAction;
import tico.editor.toolbars.TAlignToolBar;
import tico.editor.toolbars.TEditionToolBar;
import tico.editor.toolbars.TFileToolBar;
import tico.editor.toolbars.TFormatToolBar;
import tico.editor.toolbars.THandlersToolBar;
import tico.editor.toolbars.TTextToolBar;

/**
 * The main window of the Tico editor application.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */

public class TEditor extends JFrame {
	
	private static boolean android_mode=false;
	
	private static int board_width=800;
	private static int board_height=600;
	private static Color board_background_color=Color.WHITE;
	private static Color board_gradient_color=null;
	//anyadido orientacion android
	private static String android_orientation="free";//"portrait";
	
	private static String DEFAULT_TITLE = TLanguage.getString("TEditor.EDITOR_WINDOW_TITLE");

	// Editing project
	private TProject project;

	private boolean modified = false;

	private File projectFile = null;

	// Board modification elements
	private TOrderList boardList;

	private TOrderList cellOrderList;

	private JPanel boardPanel;

	// Tool bars
	private TToolBarContainer toolBarContainer;

	private TEditionToolBar editionToolBar;

	private TAlignToolBar alignToolBar;

	private TFileToolBar fileToolBar;

	private TFormatToolBar formatToolBar;

	private THandlersToolBar handlersToolBar;

	private TTextToolBar textToolBar;

	// Actions defined for board management
	private TActionSet actionSet;

	// TUNE Should be gotten from setup class
	private Dimension initLocation = new Dimension(100, 100);

	private Dimension initSize = new Dimension(800, 600);

	/**
	 * Creates a new <code>TEditor</code> main application window.
	 */
	public TEditor() {
		this(null);
	}

	/**
	 * Creates a new <code>TEditor</code> main application window with the
	 * specified initial <code>project</code>.
	 * 
	 * @param project The specified initial <code>project</code>
	 */
	public TEditor(TProject project) {
		super(DEFAULT_TITLE);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setWindowAtributes();

		getContentPane().setLayout(new BorderLayout());

		createActions();
		createToolBars();
		createMenu();
		createBoardEditor();
		createWindowListener();

		updateProjectButtons();

		if (project != null)
			setProject(project);

		setVisible(true);
		TBoardConstants.editor = this;
	}

	// Sets the window attributes
	protected void setWindowAtributes() {
		setIconImage(TResourceManager.getImage("editor-icon-24.png"));
		setLocation(initLocation.width, initLocation.height);
		setSize(initSize.width, initSize.height);
	}

	// Creates the editor actions
	private void createActions() {
		actionSet = new TActionSet(this);
	}

	// Creates the editor tool bars
	protected void createToolBars() {
		toolBarContainer = new TToolBarContainer();

		fileToolBar = new TFileToolBar(this);
		editionToolBar = new TEditionToolBar(this);
		handlersToolBar = new THandlersToolBar(this);
		formatToolBar = new TFormatToolBar(this);
		textToolBar = new TTextToolBar(this);
		alignToolBar = new TAlignToolBar(this);

		toolBarContainer.addToolBar(fileToolBar);
		toolBarContainer.addToolBar(editionToolBar);
		toolBarContainer.addToolBar(handlersToolBar);
		toolBarContainer.addToolBar(formatToolBar);
		toolBarContainer.addToolBar(textToolBar);
		toolBarContainer.addToolBar(alignToolBar);

		getContentPane().add(toolBarContainer, BorderLayout.NORTH);
	}

	// Creates the editor main menu
	private void createMenu() {
		setJMenuBar(new TEditorMenuBar(this, toolBarContainer));
	}

	// Creates the board edition area
	private void createBoardEditor() {
		// Create board list
		JPanel boardListPanel = new JPanel();
		boardListPanel.setLayout(new BorderLayout());

		boardList = new TOrderList();
		boardList.addOrderChangeListener(new OrderChangeListener() {
			public void orderChanged(OrderChangeEvent e) {
				AttributeMap map = new AttributeMap();
				getProject().setBoardList(boardList.getList());
				/*TBoardConstants
						.setOrderedCellList(map, boardList.getList());*/
				Map nested = new Hashtable();
				nested.put(getCurrentBoard().getModel(), map);
				getCurrentBoard().getGraphLayoutCache().edit(nested);
			}
		});
		boardList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (getCurrentBoardContainer() != null)
					updateVisibleBoard();
				updateBoardButtons();
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		TButton newBoardButton = new TButton(actionSet
				.getAction(TActionSet.BOARD_NEW_ACTION));
		newBoardButton.setMargin(new Insets(2, 2, 2, 2));
		newBoardButton.setText("");
		newBoardButton.setIcon(TResourceManager
				.getImageIcon("board-new-16.png"));

		TButton deleteBoardButton = new TButton(actionSet
				.getAction(TActionSet.BOARD_DELETE_ACTION));
		deleteBoardButton.setMargin(new Insets(2, 2, 2, 2));
		deleteBoardButton.setText("");
		deleteBoardButton.setIcon(TResourceManager
				.getImageIcon("board-delete-16.png"));

		buttonPanel.add(newBoardButton);
		buttonPanel.add(deleteBoardButton);

		boardListPanel.add(buttonPanel, BorderLayout.NORTH);
		boardListPanel.add(boardList, BorderLayout.CENTER);

		// Create cell order list
		JPanel cellOrderListPane = new JPanel();
		cellOrderListPane.setLayout(new BorderLayout());

		JLabel cellOrderListLabel = new JLabel(TLanguage.getString("TEditor.CELL_ORDER"));

		cellOrderList = new TOrderList();
		cellOrderList.addOrderChangeListener(new OrderChangeListener() {
			public void orderChanged(OrderChangeEvent e) {
				AttributeMap map = new AttributeMap();
				TBoardConstants
						.setOrderedCellList(map, cellOrderList.getList());
				Map nested = new Hashtable();
				nested.put(getCurrentBoard().getModel(), map);
				getCurrentBoard().getGraphLayoutCache().edit(nested);
			}
		});
		cellOrderList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				getCurrentBoard().setSelectionCell(
						cellOrderList.getSelectedValue());
			}
		});

		cellOrderListPane.add(cellOrderListLabel, BorderLayout.NORTH);
		cellOrderListPane.add(cellOrderList, BorderLayout.CENTER);

		// Create board container
		boardPanel = new JPanel();
		boardPanel.setLayout(new BorderLayout());
		JScrollPane boardScrollPane = new JScrollPane(boardPanel);

		// Create split panes
		JSplitPane verticalSplitPane = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT, boardListPanel, cellOrderListPane);
		verticalSplitPane.setResizeWeight(0.8);
		verticalSplitPane.setDividerSize(7);
		verticalSplitPane.setDividerLocation(0.8);

		JSplitPane horizontalSplitPane = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT, verticalSplitPane, boardScrollPane);
		horizontalSplitPane.setOneTouchExpandable(true);
		horizontalSplitPane.setDividerLocation(200);
		horizontalSplitPane.setDividerSize(7);

		getContentPane().add(horizontalSplitPane, BorderLayout.CENTER);
	}
	
	// Creates the window listener
	private void createWindowListener() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				new TEditorExitAction(getEditor()).actionPerformed(null);
			}
		});
	}
	
	/**
	 * Returns itself.
	 * 
	 * @return Itself
	 */
	public TEditor getEditor() {
		return this;
	}
	
	/**
	 * Returns the current editing <code>project</code>.
	 * 
	 * @return The current editing <code>project</code>
	 */
	public TProject getProject() {
		return project;
	}

	/**
	 * Sets a new <code>project</code> to begin its edition
	 * 
	 * @param project The new <code>project</code>
	 */
	public void setProject(TProject project) {
		if (getProject() != null)
			deleteProject();
		// Set a new name if needed
		if (project.getName() == null)
			project.setName(TProject.newName());
		// Asign the project
		this.project = project;
		project.addProjectChangeListener(new ProjectChangeListener() {
			public void projectChanged(ProjectChangeEvent e) {
				if (e.getChange() == ProjectChangeEvent.BOARD_ADDED) {
					// If a board has been added, add it to the editor
					addBoard(e.getChangedBoard());
				} else if (e.getChange() == ProjectChangeEvent.BOARD_REMOVED) {
					// If a board has been removed, remove it from the editor
					removeBoard(e.getChangedBoard());
				} else if (e.getChange() == ProjectChangeEvent.NAME_CHANGED)
					// If the name has been changed, change it in the editor
					updateTitle();
				// Set the project modified
				setModified(true);
			}
		});
		// Add new editor component elements
		for (int i = 0; i < project.getBoardList().size(); i++)
			addBoard((TBoard)project.getBoardList().get(i));
		// The new project has not been modified
		setModified(false);
		// Update buttons
		updateProjectButtons();
	}

	/**
	 * Deletes from the application the current editing <code>project</code>.
	 */
	public void deleteProject() {
		// Need to be cloned
		if (project == null)
			return;
		
		ArrayList list = (ArrayList)project.getBoardList().clone();
		// Delete current editor component elements
		for (int i = 0; i < list.size(); i++)
			project.removeBoard((TBoard)list.get(i));
		// Delete project files
		TFileHandler.cleanCurrentDirectory();
		// Set the project to null
		project = null;
		updateTitle();
		updateProjectButtons();
	}

	/**
	 * Returns the current editing project associated <code>projectFile</code>.
	 * 
	 * @return The current editing project associated <code>projectFile</code>.
	 */
	public File getProjectFile() {
		return projectFile;
	}

	/**
	 * Sets the current editing project associated <code>projectFile</code>.
	 * 
	 * @param projectFile The current editing project associated
	 * <code>projectFile</code> to set
	 */
	public void setProjectFile(File projectFile) {
		this.projectFile = projectFile;
	}
	public void setModified(boolean modified) {
		this.modified = modified;
		updateTitle();
	}

	public boolean isModified() {
		return modified;
	}

	/**
	 * Returns the current editing <code>boardContainer</code>.
	 * 
	 * @return The current editing <code>boardContainer</code>
	 */
	public TBoardContainer getCurrentBoardContainer() {
		return (TBoardContainer)boardList.getSelectedValue();
	}

	/**
	 * Returns the current editing <code>board</code>.
	 * 
	 * @return The current editing <code>board</code>
	 */
	public TBoard getCurrentBoard() {
		if (getCurrentBoardContainer() == null)
			return null;

		return getCurrentBoardContainer().getBoard();
	}

	/**
	 * Removes the current <code>board</code> from the editing project.
	 */
	public void removeCurrentBoard() {
		if (getCurrentBoard() != null)
			project.removeBoard(getCurrentBoard());
	}
	
	// Adds a new board to the application interface
	private void addBoard(TBoard board) {
		setModified(true);
		// Set the board to the board container
		TBoardContainer newBoardContainer = new TBoardContainer(this, board);
		// Add board container to the editor
		boardList.addElement(newBoardContainer);
		// Update the visible board
		updateVisibleBoard();
	}

	// Removes the specified board to the application interface
	private void removeBoard(TBoard board) {
		setModified(true);

		int boardContainerIndex = -1;

		for (int i = 0; (i < boardList.getList().size())
				&& (boardContainerIndex == -1); i++) {
			if (((TBoardContainer)boardList.getList().get(i)).getBoard()
					.equals(board))
				boardContainerIndex = i;
		}

		// If the board is in the list, delete it
		if (boardContainerIndex != -1) {
			// Remove from the board container JPanel
			boardPanel.remove((Component)boardList.getList().get(
					boardContainerIndex));
			// Remove from the list
			boardList.removeElement((Component)boardList.getList().get(
					boardContainerIndex));
			// Set the previous board selected
			if (boardContainerIndex < boardList.getList().size())
				boardList.setSelectedIndex(boardContainerIndex);
			else if (boardContainerIndex > 0)
				boardList.setSelectedIndex(boardContainerIndex - 1);
			// Update the visible board
			updateVisibleBoard();
		}
	}
	
	/**
	 * Returns the <code>index</code> position <code>boardContainer</code>.
	 * 
	 * @return The <code>index</code> position <code>boardContainer</code>
	 */
	public TBoardContainer getBoardContainer(int index) {
		return (TBoardContainer)boardList.getList().get(index);
	}

	/**
	 * Returns the number of <code>boardContainer</code> in the application.
	 * 
	 * @return The number of <code>boardContainer</code> in the application
	 */
	public int getBoardContainerCount() {
		return boardList.getList().size();
	}

	// Set the current editing board in the board edition area
	private void updateVisibleBoard() {
		ArrayList list = boardList.getList();
		
		for (int i = 0; i < list.size(); i++)
			boardPanel.remove((TBoardContainer)list.get(i));
		
		if (getCurrentBoardContainer() != null) {
			boardPanel.add(getCurrentBoardContainer(), BorderLayout.CENTER);
		}
		TBoardConstants.currentBoard = getCurrentBoard();
		
		boardPanel.updateUI();
		updateCellOrderList();
	}

	/**
	 * Returns the editor's <code>actionSet</code>.
	 * 
	 * @return The editor's <code>actionSet</code>
	 */
	public TActionSet getActionSet() {
		return actionSet;
	}

	/**
	 * Returns the attributes specified by the editor toolbars.
	 * 
	 * @return The attributes specified by the editor toolbars
	 */
	public AttributeMap getCurrentAttributes() {
		AttributeMap map = new AttributeMap();

		map.applyMap(formatToolBar.getAttributes());
		map.applyMap(textToolBar.getAttributes());

		return map;
	}

	// Editor component updaters
	/**
	 * Updates editor window title.
	 */
	public void updateTitle() {
		String title = DEFAULT_TITLE;
		
		if(get_android_mode()) title=title + " ANDROID"; //anyadimos al titulo que estamos en modo android
		
		if (getProject() != null) {
			title += " - " + getProject().getName();
			if (isModified())
				title += " *";
		}
		
		setTitle(title);
	}
	
	/**
	 * Updates toolbars.
	 */
	public void updateToolBar() {
		handlersToolBar.updateHandlers();
	}

	/**
	 * Updates components UIs.
	 */
	public void updateUI() {
		boardList.updateUI();
		boardPanel.updateUI();
	}

	/**
	 * Enables and disables the project actions.
	 */
	public void updateProjectButtons() {
		boolean projectExists = (project != null);

		actionSet.getAction(TActionSet.BOARD_NEW_ACTION).setEnabled(
				projectExists);
		actionSet.getAction(TActionSet.PROJECT_SAVE_ACTION).setEnabled(
				projectExists);
		actionSet.getAction(TActionSet.PROJECT_SAVE_AS_ACTION).setEnabled(
				projectExists);
		actionSet.getAction(TActionSet.PROJECT_IMPORT_ACTION).setEnabled(
				projectExists);
		/*actionSet.getAction(TActionSet.PROJECT_INTERPRET_ACTION).setEnabled(
				projectExists);*/
		actionSet.getAction(TActionSet.PROJECT_PROPERTIES_ACTION).setEnabled(
				projectExists);
		actionSet.getAction(TActionSet.PROJECT_VALIDATION_ACTION).setEnabled(
				projectExists);
		boardList.setEnabled(projectExists);
		cellOrderList.setEnabled(projectExists);
		
		updateBoardButtons();
	}

	/**
	 * Enables and disables the board actions.
	 */
	public void updateBoardButtons() {
		boolean boardExists = (getCurrentBoard() != null);

		// Action handlers
		actionSet.getAction(TActionSet.SELECTION_HANDLER).setEnabled(
				boardExists);
		actionSet.getAction(TActionSet.CELL_HANDLER).setEnabled(boardExists);
		/*if(!get_android_mode())*/actionSet.getAction(TActionSet.CELL_CONTROLLER_HANDLER).setEnabled(boardExists);
		/*else actionSet.getAction(TActionSet.CELL_CONTROLLER_HANDLER).setEnabled(false);*/
		actionSet.getAction(TActionSet.GRID_HANDLER).setEnabled(boardExists);
		actionSet.getAction(TActionSet.TEXT_AREA_HANDLER).setEnabled(
				boardExists);
		actionSet.getAction(TActionSet.LABEL_HANDLER).setEnabled(boardExists);
		//añadido if y la parte del else para deshabilitar en caso de edición android
		/*if(!get_android_mode())*/ 
		actionSet.getAction(TActionSet.RECTANGLE_HANDLER).setEnabled(boardExists);
		//else actionSet.getAction(TActionSet.RECTANGLE_HANDLER).setEnabled(false);
		/*if(!get_android_mode())*/ 
		actionSet.getAction(TActionSet.ROUND_RECT_HANDLER).setEnabled(boardExists);
		//else actionSet.getAction(TActionSet.ROUND_RECT_HANDLER).setEnabled(false);
		/*if(!get_android_mode())*/ 
		actionSet.getAction(TActionSet.OVAL_HANDLER).setEnabled(boardExists);
		//else actionSet.getAction(TActionSet.OVAL_HANDLER).setEnabled(false);
		/*if(!get_android_mode())*/ 
		actionSet.getAction(TActionSet.LINE_HANDLER).setEnabled(boardExists);
		//else actionSet.getAction(TActionSet.LINE_HANDLER).setEnabled(false);
		// Tool bar handlers
		formatToolBar.updateComponents();
		textToolBar.updateComponents();
		// Board actions
		actionSet.getAction(TActionSet.SELECT_ALL_ACTION).setEnabled(boardExists);
		actionSet.getAction(TActionSet.PASTE_ACTION).setEnabled(boardExists);
		actionSet.getAction(TActionSet.PROJECT_PRINT_ACTION).setEnabled(
				boardExists);
		actionSet.getAction(TActionSet.BOARD_PROPERTIES_ACTION).setEnabled(
				boardExists);
		actionSet.getAction(TActionSet.BOARD_DELETE_ACTION).setEnabled(
				boardExists);
		actionSet.getAction(TActionSet.BOARD_EXPORT_ACTION).setEnabled(
				boardExists);
		actionSet.getAction(TActionSet.BOARD_EXPORT_PNG_ACTION).setEnabled(
				boardExists);
		actionSet.getAction(TActionSet.BOARD_EXPORT_JPG_ACTION).setEnabled(
				boardExists);
		actionSet.getAction(TActionSet.BOARD_VALIDATION_ACTION).setEnabled(
				boardExists);

		updateSelectionButtons();
		updateHistoryButtons();
	}

	/**
	 * Enables and disables the history actions.
	 */
	public void updateHistoryButtons() {
		if (getCurrentBoard() != null) {
			// FIXME Change method to allow first modification edition
			actionSet.getAction(TActionSet.UNDO_ACTION).setEnabled(
					getCurrentBoardContainer().getUndoManager().canUndo(
							getCurrentBoard().getGraphLayoutCache()));
			actionSet.getAction(TActionSet.REDO_ACTION).setEnabled(
					getCurrentBoardContainer().getUndoManager().canRedo(
							getCurrentBoard().getGraphLayoutCache()));

			formatToolBar.updateComponents();
			textToolBar.updateComponents();
		} else {
			actionSet.getAction(TActionSet.UNDO_ACTION).setEnabled(false);
			actionSet.getAction(TActionSet.REDO_ACTION).setEnabled(false);
		}
	}
	
	/**
	 * Updates de editor's cell list component
	 */
	public void updateCellOrderList() {
		TBoard board = getCurrentBoard();

		if (board != null) {
			Object selectedObject = cellOrderList.getSelectedValue();
			cellOrderList.setList(TBoardConstants
					.getOrderedCellList(((TBoardModel)getCurrentBoard()
							.getModel()).getAttributes()));
			cellOrderList.setSelectedValue(selectedObject);
		}
	}
	
	/**
	 * Change Editor's Cursor into a waiting clock
	 * 
	 */
	
	public void changeToWaitingCursor(){			
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
  	}
	
	/**
	 * Restore Default Cursor. 
	 * 
	 */
	
	public void restoreCursor(){
		Cursor cursorBar = new Cursor(Cursor.DEFAULT_CURSOR);
		this.setCursor(cursorBar);
	}
	

	/**
	 * Enables and disables the selection depending actions.
	 */
	public void updateSelectionButtons() {
		boolean cutCopyActionsEnabled = false;
		boolean alignActionsEnabled = false;
		boolean gapActionsEnabled = false;

		// If the current board is null, disable everything
		if (getCurrentBoard() != null) {
			int selectionCount = getCurrentBoard().getSelectionCount();

			cutCopyActionsEnabled = (selectionCount > 0);
			alignActionsEnabled = (selectionCount > 1);
			gapActionsEnabled = (selectionCount > 2);
		}

		actionSet.getAction(TActionSet.ALIGN_BOTTOM_ACTION).setEnabled(
				alignActionsEnabled);
		actionSet.getAction(TActionSet.ALIGN_TOP_ACTION).setEnabled(
				alignActionsEnabled);
		actionSet.getAction(TActionSet.ALIGN_LEFT_ACTION).setEnabled(
				alignActionsEnabled);
		actionSet.getAction(TActionSet.ALIGN_RIGHT_ACTION).setEnabled(
				alignActionsEnabled);
		actionSet.getAction(TActionSet.ALIGN_HORIZONTAL_ACTION).setEnabled(
				alignActionsEnabled);
		actionSet.getAction(TActionSet.ALIGN_VERTICAL_ACTION).setEnabled(
				alignActionsEnabled);
		actionSet.getAction(TActionSet.FIT_WIDTH_ACTION).setEnabled(
				alignActionsEnabled);
		actionSet.getAction(TActionSet.FIT_HEIGHT_ACTION).setEnabled(
				alignActionsEnabled);

		actionSet.getAction(TActionSet.HORIZONTAL_GAP_ACTION).setEnabled(
				gapActionsEnabled);
		actionSet.getAction(TActionSet.VERTICAL_GAP_ACTION).setEnabled(
				gapActionsEnabled);

		actionSet.getAction(TActionSet.FRONT_ACTION).setEnabled(
				cutCopyActionsEnabled);
		actionSet.getAction(TActionSet.BACK_ACTION).setEnabled(
				cutCopyActionsEnabled);

		actionSet.getAction(TActionSet.CUT_ACTION).setEnabled(
				cutCopyActionsEnabled);
		actionSet.getAction(TActionSet.COPY_ACTION).setEnabled(
				cutCopyActionsEnabled);
		actionSet.getAction(TActionSet.DELETE_ACTION).setEnabled(
				cutCopyActionsEnabled);

		formatToolBar.updateComponents();
		textToolBar.updateComponents();
	}
	
	/** Sets the last board width selected to be used by default in a new board.
	 * 
	 * @param width <code>int</code> with  the last board width selected.
	 */
	public static void set_board_width(int width){
		board_width=width;
	}
	

	/** Gets the last board width selected to be used by default in a new board.
	 * 
	 * @return <code>int</code> with  the last board width selected
	 */
	public static int get_board_width(){
		return board_width;
	}
	
	/** Sets the last board height selected to be used by default in a new board.
	 * 
	 * @param height <code>int</code> with  the last board height selected.
	 */
	public static void set_board_height(int height){
		board_height=height;
	}
	
	/** Gets the last board height selected to be used by default in a new board.
	 * 
	 * @return <code>int</code> with  the last board height selected
	 */
	public static int get_board_height(){
		return board_height;
	}
	
	/** Sets the last board background color selected to be used by default in a new board.
	 * 
	 * @param color <code>Color</code> with  the last board background color selected.
	 */
	public static void set_board_background_color(Color color){
		board_background_color=color;
	}
	

	/** Gets the last board background color selected to be used by default in a new board.
	 * 
	 * @return <code>Color</code> with  the last board background color selected
	 */
	public static Color get_board_background_color(){
		return board_background_color;
	}
	
	/** Sets the last board gradient color selected to be used by default in a new board.
	 * 
	 * @param color <code>Color</code> with  the last board gradient color selected.
	 */
	public static void set_board_gradient_color(Color color){
		board_gradient_color=color;
	}
	/** Gets the last board gradient color selected to be used by default in a new board.
	 * 
	 * @return <code>Color</code> with  the last board gradient color selected
	 */
	public static Color get_board_gradient_color(){
		return board_gradient_color;
	}
	
	
	/** Sets the mode of the editor.
	 * 
	 * @param mode <code>boolean</code> with the editor's mode.<code>true</code> if it's android mode. <code>false</code> if it's not android mode.
	 * 
	 */
	public static void set_android_mode(boolean mode){
		android_mode=mode;
	}
	
	
	/** Returns the current mode of the editor.
	 * 
	 * @return <code>boolean</code> with the editor's mode.<code>true</code> if it's android mode. <code>false</code> if it's not android mode.
	 * 
	 */
	public static boolean get_android_mode(){
		return android_mode;
	}
	
	/** Sets the orientation of the panel for Android mode.
	 * "portrait" for vertical orientation. "landscape" for horizontal orientation.
	 * "free" if the project changes automatically to the device orientation
	 * 
	 * @param orientation <code>String</code> with the desired orientation
	 * 
	 */
	public static void set_android_orientation(String orientation){
		android_orientation=orientation;
	}
	/** Returns the orientation of the panel for Android mode.
	 * "portrait" for vertical orientation. "landscape" for horizontal orientation.
	 * "free" if the project changes automatically to the device orientation
	 * 
	 * @return <code>String</code> with the selected orientation
	 * 
	 */
	public static String get_android_orientation(){
		return android_orientation;
	}
}