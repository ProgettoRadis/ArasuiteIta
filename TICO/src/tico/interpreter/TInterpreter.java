/*
 * File: TInterpreter.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Antonio Rodríguez y Carolina Palacio
 * 
 * Date: Nov 20, 2009
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

package tico.interpreter;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;

import tico.components.TMenuItem;
import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;
import tico.interpreter.actions.TInterpreterExitAction;
import tico.interpreter.listeners.TBoardListener;
import tico.interpreter.threads.TThreads;

/**
 * The main window of the Tico interpreter application.
 * 
 * @author Antonio Rodríguez y Carolina Palacio
 * @version e1.0 Nov 20, 2009
 */

public class TInterpreter extends JFrame {

	public static String DEFAULT_TITLE = TLanguage.getString("TInterpreter.INTERPRETER_WINDOW_TITLE");

	// Interpreting project
	private TInterpreterProject project;
	
	// Current board of the interpreting project
	private static TInterpreterBoard currentBoard = null;	
	
	// TUNE Should be gotten from setup class
	private Dimension initLocation = new Dimension(0,0);
	
	private Dimension initSize = new Dimension(1204,800);
	
	// List with the accumulated cells
	public static ArrayList accumulatedCellsList = null;
	
	// Indicates if the project interpretation  has started
	public static int run = 0;
	
	// Interpretation thread
	public static TThreads interpretationThread;
	
	// Performs mouse movements when browsing modes are used
	public Robot interpreterRobot;
	
	// Menu actions
	private static TMenuItem menuItemStart;
	
	private static TMenuItem menuItemStop;
	
	private static TMenuItem menuItemRead;
	
	private static TMenuItem menuItemUndo;
	
	private static TMenuItem menuItemUndoAll;
	
	// Panels in the main window of the Tico interpreter
	public JPanel backgroundPanel;
	
	public static TPanel interpretArea;
	
	public static JPanel interpretAreaBackground;
	
        public static JSplitPane splitPane;
	
	public static JPanel accumulatedCells;
	
	// Listener for the board in interpretation
	public static TBoardListener boardListener;
	
	// Actions defined for board interpretation
	private TInterpreterActionSet actionSet;

        // The interpreter is fullscreen or not
        public static boolean fullscreen = false;

	/**
	 * Creates a new <code>TInterpreter</code> main application window.
	 */
	public TInterpreter() {
		
		super(DEFAULT_TITLE);
		
		initSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		
		accumulatedCellsList = new ArrayList();
		
		try {
			interpreterRobot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setWindowAtributes();

		createActions();
		  
		createMenu();
		createInterpreterArea();
	    createWindowListener();
	    
	    updateMenuButtons();	    
			
		setResizable(true);
		setVisible(true);
		TInterpreterConstants.interpreter = this;
		
		String os = System.getProperty("os.name");
		if(os.startsWith("Windows")){
			TInterpreterConstants.operatingSystem = TInterpreterConstants.OS_WINDOWS;
		}
		if(os.startsWith("Linux")){
			TInterpreterConstants.operatingSystem = TInterpreterConstants.OS_LINUX;
		}
	}	
	
	/**
	 * Creates a new <code>TInterpreter</code> main application window with the
	 * specified initial <code>project</code>.
	 * 
	 * @param project The specified initial <code>project</code>
	 */		
	public TInterpreter(TInterpreterProject project) {	
		this();
		setProject(project);
		
		this.setTitle(TInterpreter.DEFAULT_TITLE + " - " + project.getName());		
	}
	
	/**
	 * Returns the current interpreting <code>board</code>.
	 * 
	 * @return The current interpreting <code>board</code>
	 */	
	public static TInterpreterBoard getCurrentBoard() {
		return currentBoard;
	}

	/**
	 * Sets a new <code>board</code> to begin its interpretation
	 * 
	 * @param project The <code>board</code>
	 */
	public static void setCurrentBoard(TInterpreterBoard currentBoard) {
		TInterpreter.currentBoard = currentBoard;
	}
	
	/**
	 * Changes the board displayed on the interpreter window with the
	 * specified <code>boardName</code>.
	 * 
	 * @param boardName The name of the specified <code>board</code>
	 */
	public void changeBoard(String boardName){
		  TInterpreterBoard board = project.getBoard(boardName);
		  interpretArea.removeAll();
		  this.setVisible(true);
		  board.paintBoard(interpretArea, true);
		  interpretArea.repaint();
	  }	
	
	public void repaintCurrentBoard(boolean playBoardSound){
		interpretArea.removeAll();
		getCurrentBoard().goToInitialState();
		getCurrentBoard().paintBoard(interpretArea, playBoardSound);
		interpretArea.repaint();
	}
	
	public TInterpreterProject getIntepreterProject() {
		return project;
	}

	// Sets the window attributes
	protected void setWindowAtributes() {
		setIconImage(TResourceManager.getImage("interpreter-icon-24.png"));
		setLocation(initLocation.width, initLocation.height);
		setSize(initSize.width, initSize.height);
	}

	// Creates the interpreter actions
	private void createActions() {
		actionSet = new TInterpreterActionSet(this);
	}
	
	// Creates the interpreter main menu
	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();	
		
		// Menu File		
		JMenu menuFile = new JMenu(TLanguage.getString("TInterpreter.FILE_MENU"));
		menuFile.setMnemonic(KeyEvent.VK_A);

		// Create the menu items
		TMenuItem menuItem;
		menuItem = new TMenuItem(actionSet
				.getAction(TInterpreterActionSet.PROJECT_OPEN_ACTION));
		menuItem.setMnemonic(KeyEvent.VK_A);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.CTRL_MASK));
		
		menuFile.add(menuItem);
		
		menuFile.add(new JSeparator());
		menuItem = new TMenuItem(actionSet
				.getAction(TInterpreterActionSet.INTERPRETER_EXIT_ACTION));
		menuItem.setMnemonic(KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));
		menuFile.add(menuItem);
		
		// Menu Help
		menuBar.add(menuFile);
		JMenu menuHelp = new JMenu(TLanguage.getString("TEditorMenuBar.HELP_MENU"));
		
		menuHelp.setMnemonic(KeyEvent.VK_Y);
		menuItem = new TMenuItem(actionSet.getAction(TInterpreterActionSet.INTERPRETER_ABOUT));
		menuItem.setMnemonic(KeyEvent.VK_F1);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,
				ActionEvent.CTRL_MASK));
		menuHelp.add(menuItem);
		
		// Menu Actions
		JMenu menuActions = new JMenu(TLanguage.getString("TInterpreterMenuBar.ACTION_MENU"));
		
		menuItemStart = new TMenuItem(actionSet.getAction(TInterpreterActionSet.INTERPRETER_RUN));		
		menuItemStart.setMnemonic(KeyEvent.VK_F5);
		menuItemStart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, ActionEvent.CTRL_MASK));		
		menuActions.add(menuItemStart);
		
		menuItemStop = new TMenuItem(actionSet.getAction(TInterpreterActionSet.INTERPRETER_STOP));		
		menuItemStop.setMnemonic(KeyEvent.VK_ESCAPE);
		menuItemStop.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
		menuActions.add(menuItemStop);
				
		menuItemRead = new TMenuItem(actionSet.getAction(TInterpreterActionSet.INTERPRETER_READ));
		menuItemRead.setMnemonic(KeyEvent.VK_F7);
		menuItemRead.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, ActionEvent.CTRL_MASK));		
		menuActions.add(menuItemRead);
		
		menuItemUndo = new TMenuItem(actionSet.getAction(TInterpreterActionSet.INTERPRETER_UNDO));
		menuItemUndo.setMnemonic(KeyEvent.VK_Z);
		menuItemUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		menuActions.add(menuItemUndo);
		
		menuItemUndoAll = new TMenuItem(actionSet.getAction(TInterpreterActionSet.INTERPRETER_UNDO_ALL));
		menuItemUndoAll.setMnemonic(KeyEvent.VK_F8);
		menuItemUndoAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F8, ActionEvent.CTRL_MASK));
		menuActions.add(menuItemUndoAll);
        
        //Menu View        
		JMenu menuView = new JMenu(TLanguage.getString("TInterpreterMenuBar.VIEW_MENU"));
		menuItem = new TMenuItem(actionSet.getAction(TInterpreterActionSet.INTERPRETER_OPTIONS));
		menuView.add(menuItem);
		menuItem = new TMenuItem(actionSet.getAction(TInterpreterActionSet.INTERPRETER_LANGUAJES));
		menuView.add(menuItem);
                menuItem = new TMenuItem(actionSet.getAction(TInterpreterActionSet.INTERPRETER_FULLSCREEN));
                menuView.add(menuItem);
		
		menuBar.add(menuView);
		menuBar.add(menuActions);
		menuBar.add(createValidationMenu());
		menuBar.add(menuHelp);
		
		setJMenuBar(menuBar);
		setEnabledActions(false);
	}
	
	public static void setEnabledActions(boolean enabled){
        menuItemStart.setEnabled(enabled);
        menuItemUndo.setEnabled(enabled);
        menuItemUndoAll.setEnabled(enabled);
        menuItemRead.setEnabled(enabled);
        menuItemStop.setEnabled(enabled);
	}
	
	public TInterpreter getInterpreter() {
		return this;
	}
	
	// Creates the board interpreting area
	private void createInterpreterArea() {
		backgroundPanel= new JPanel(new BorderLayout());
		
		interpretArea = new TPanel();
		interpretArea.setLayout(null);

		Dimension d = new Dimension(800,600);
		interpretArea.setPreferredSize(d);
		interpretArea.setBackground(Color.white);
		interpretArea.setVisible(true);
		interpretArea.setBorder(new LineBorder(Color.black,1));
		
		interpretAreaBackground = new JPanel();
		interpretAreaBackground.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor= GridBagConstraints.CENTER;
		interpretAreaBackground.add(interpretArea,c);
		
		accumulatedCells = new JPanel();
		accumulatedCells.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				new JScrollPane(interpretAreaBackground), accumulatedCells);
		
		splitPane.setResizeWeight(0.5);
		splitPane.setEnabled(false);
		splitPane.setDividerSize(10);
		splitPane.setDividerLocation(java.awt.Toolkit.getDefaultToolkit().getScreenSize().height-157);
		
		backgroundPanel.add(splitPane, BorderLayout.CENTER);				
			
		this.add(backgroundPanel);
	}

	// Creates the window listener
	private void createWindowListener() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				new TInterpreterExitAction(getInterpreter()).actionPerformed(null);
			}
		});
	}
	
	/**
	 * Returns the current <code>project</code>.
	 * 
	 * @return The current <code>project</code>
	 */
	public TInterpreterProject getProject() {
		return project;
	}

	/**
	 * Sets a new <code>project</code> to begin its interpretation
	 * 
	 * @param project The new <code>project</code>
	 */
	public void setProject(TInterpreterProject myproject) {
		if (getProject() != null)
			deleteProject();
		this.project = myproject;
		this.setTitle(TInterpreter.DEFAULT_TITLE + " - " + myproject.getName());
		changeBoard(project.getInitialBoardname());
		updateMenuButtons();
	}
	
	/**
	 * Deletes from the application the current <code>project</code>.
	 */	
	public void deleteProject() {
		project = null;
		accumulatedCellsList.removeAll(accumulatedCellsList);
		accumulatedCells.removeAll();
		updateMenuButtons();
	}

	/**
	 * Returns the editor's <code>actionSet</code>.
	 * 
	 * @return The editor's <code>actionSet</code>
	 */
	public TInterpreterActionSet getActionSet() {
		return actionSet;
	}
	
	/**
	 * Change Interpreter's Cursor into a predefined image 
	 * 
	 */
	
	public void TIntepreterChangeCursor(){			
		
		if (TInterpreterConstants.interpreterCursor==null)
		{
		ImageIcon ii=TResourceManager.getImageIcon("flecha2.png");
	
		if (ii==null){
			ii=TResourceManager.getImageIcon("flecha2.png");
		}
		Image imageCursor=ii.getImage();
		Cursor customCursor=getToolkit().createCustomCursor(imageCursor,new Point(),"MyCursor");
		this.setCursor(customCursor);
		}
		else 
		{
			ImageIcon ii= new ImageIcon(TInterpreterConstants.interpreterCursor);
			
			if (ii==null){
				ii=TResourceManager.getImageIcon("flecha2.png");
			}
			
			Image imageCursor=ii.getImage();
			Cursor customCursor=getToolkit().createCustomCursor(imageCursor,new Point(),"MyCursor");
			this.setCursor(customCursor);
		}
  	}
	
	/**
	 * Change Interpreter's Cursor into a waiting clock
	 * 
	 */
	
	public void TIntepreterWaitingCursor(){			
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
  	}
	
	
	/**
	 * Restore Default Cursor. 
	 * 
	 */
	
	public void TInterpreterRestoreCursor(){
		Cursor cursorBar = new Cursor(Cursor.DEFAULT_CURSOR);
		this.setCursor(cursorBar);
	}
	
	
	private JMenu createValidationMenu() {
		// Create the menu
		JMenu menu = new JMenu(TLanguage.getString("TInterpreter.VALIDATION_MENU"));
		menu.setMnemonic(KeyEvent.VK_L);		

		// Create submenu
		TMenuItem menuItem = new TMenuItem(actionSet.getAction(TInterpreterActionSet.INTERPRETER_VALIDATION));
		
		menu.add(menuItem);
		add(menu);
		
		return menu;
	}
	
	public void updateMenuButtons() {
		boolean projectExists = (project != null);
		// Action handlers
		actionSet.getAction(TInterpreterActionSet.INTERPRETER_VALIDATION).setEnabled(projectExists);
	}
	
	public static String returnMouseMode(){
		return TInterpreterConstants.mouseModeSelected;
	}
	 
	
}

