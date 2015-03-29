/*
 * File: TProject.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: 04-Oct-2006
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
package tico.board;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.event.EventListenerList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import tico.board.components.TComponent;
import tico.board.encoding.InvalidFormatException;
import tico.board.events.BoardChangeEvent;
import tico.board.events.BoardChangeListener;
import tico.board.events.ProjectChangeEvent;
import tico.board.events.ProjectChangeListener;
import tico.editor.TBoardContainer;
import tico.editor.TEditor;

/**
 * Set of
 * <code>TBoards</code> wich can be navigated from an specified initial one.
 *
 * @author Pablo Mu�oz
 * @version 1.0 Nov 20, 2006
 */
public class TProject {

    private static int projectCount = 0;
    private ArrayList boardList;
    private Hashtable boardTable;
    private TBoard initialBoard;
    private String name = null;
    private String defaultVoiceName = "";
    private int synthMode = TBoardConstants.GOOGLE_MODE;
    protected transient EventListenerList listenerList = new EventListenerList();

    /**
     * Creates a new empty
     * <code>TProject</code> with no initial
     * <code>name</code>.
     */
    public TProject() {
        // Initialized board container structures
        boardList = new ArrayList();
        boardTable = new Hashtable();
    }

    /**
     * Creates a new empty
     * <code>TProject</code> with the specified initial
     * <code>name</code>.
     *
     * @param name The specified initial <code>name</code>
     */
    public TProject(String name) {
        this();
        this.name = name;
    }

    /**
     * Returns the project
     * <code>name</code>.
     *
     * @return The project <code>name</code>
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the project
     * <code>name</code>.
     *
     * @param name The project <code>name</code> to set
     */
    public void setName(String name) {
        this.name = name;
        fireProjectChange(null, ProjectChangeEvent.NAME_CHANGED);
    }

    /**
     * Creates a new project
     * <code>name</code> different from any other project
     * <code>name</code> created with this function.
     *
     * @return The new project <code>name</code>
     */
    public static String newName() {
        return "project_" + projectCount++;
    }

    /**
     * Add a
     * <code>board</code> to the project. The board is renamed if its name is
     * already used by other board of the project. If is the first
     * <code>board</code> of the project, its automatically set as the
     * <code>initialBoard</code>.
     *
     * @param board The <code>board</code> to add
     */
    public void addBoard(TBoard board) {
        // Check if the board has a correct name
        String boardName = board.getBoardName();
        // If it do not have name, create a new one for it
        if (boardName == null) {
            board.setBoardName(TBoard.newBoardName());
            boardName = board.getBoardName();
        }
        // Rename the board if its name is already used
        int boardCounter = 1;
        while (isUsedName(board.getBoardName())) {
            if (board.getBoardName().startsWith("board_")) {
                board.setBoardName(TBoard.newBoardName());
            } else {
                board.setBoardName(boardName + "_" + boardCounter++);
            }
        }
        // Add board change listener
        board.addBoardChangeListener(new BoardChangeListener() {
            public void boardChanged(BoardChangeEvent e) {
                fireProjectChange((TBoard) e.getSource(),
                        ProjectChangeEvent.BOARD_MODIFIED);
            }
        });
        // Add to both storage structures
        boardList.add(board);
        boardTable.put(board.getBoardName(), board);
        // If there is no initial board, set this
        if (initialBoard == null) {
            setInitialBoard(board);
        }
        // Fire the listener
        fireProjectChange(board, ProjectChangeEvent.BOARD_ADDED);
    }

    /**
     * Add all the boards of
     * <code>project</code> to the project. If any of the new board has a name
     * already used by other board of the project, it is renamed. If this
     * project was empty, the first board of
     * <code>project</code> is set as
     * <code>initialBoard</code>.
     *
     * @param project The <code>project</code> to add
     */
    public void addProject(TProject project) {
        ArrayList boardList = project.getBoardList();
        for (int i = 0; i < boardList.size(); i++) {
            addBoard((TBoard) boardList.get(i));
        }
    }

    /**
     * Removes the specified
     * <code>board</code> from the project.
     *
     * @param board The <code>board</code> to delete
     */
    public void removeBoard(TBoard board) {
        // Fire the listener
        fireProjectChange(board, ProjectChangeEvent.BOARD_REMOVED);
        // Remove from both storage structures
        boardTable.remove(board.getBoardName());
        boardList.remove(board);
        // Remove the board from all the other board component attributes
        ArrayList list = getBoardList();
        for (int i = 0; i < list.size(); i++) {
            ((TBoardModel) ((TBoard) list.get(i)).getModel())
                    .deleteBoardFromAttributes(board);
        }
    }

    /**
     * Returns the number of boards contained by the project
     *
     * @return The project number of boards
     */
    public int getBoardCount() {
        return boardList.size();
    }

    /**
     * Returns the project board with the specified
     * <code>name</code>.
     *
     * @param name The specified board <code>name</code>
     * @return The project board with the specified <code>name</code>. If the
     * project does not contain any board with that name, returns null
     */
    public TBoard getBoard(String name) {
        return (TBoard) boardTable.get(name);
    }

    /**
     * Returns the project board in the specified
     * <code>index</code>.
     *
     * @param index The specified board <code>index</code>
     * @return The project board with the specified <code>name</code>
     */
    public TBoard getBoard(int index) {
        return (TBoard) boardList.get(index);
    }

    /**
     * Returns the
     * <code>initialBoard</code>.
     *
     * @return The <code>initialBoard</code>
     */
    public TBoard getInitialBoard() {
        return initialBoard;
    }

    /**
     * Sets the
     * <code>initialBoard</code>. It
     * <code>initialBoard</code> is not contained in the project, adds it.
     *
     * @param initialBoard The <code>initialBoard</code> to set
     */
    public void setInitialBoard(TBoard initialBoard) {
        if (!boardList.contains(initialBoard)) {
            addBoard(initialBoard);
        }
        this.initialBoard = initialBoard;
        fireProjectChange(null, ProjectChangeEvent.INITIAL_BOARD_CHANGED);
    }

    public int getDefaultSynthMode() {
        return synthMode;
    }

    public void setDefaultSynthMode(int value) {
        this.synthMode = value;
    }

    public String getDefaultVoiceName() {
        return defaultVoiceName;
    }

    /**
     * Sets the voice name to use as default in the the project for the voice
     * sinthesizer
     *
     * @param voiceName The unique name of the voice
     */
    public void setDefaultVoiceName(String voiceName) {
        this.defaultVoiceName = voiceName;
    }

    /**
     * Determines if any project board has the specified
     * <code>name</code>.
     *
     * @param name The specified <code>name</code>
     * @return <i>true</i> if any project board has the *      * specified <code>name</code>
     */
    public boolean isUsedName(String name) {
        boolean used = false;
        int i = 0;
        while (i < boardList.size() && !used) {
            used = ((TBoard) boardList.get(i)).getBoardName().equals(name);
            i++;
        }
        return used;
    }

    /**
     * Determines if any project board different from
     * <code>board</code> has the specified
     * <code>name</code>.
     *
     * @param board The <code>board</code>
     * @param name The specified <code>id</code>
     * @return <i>true</i> if any project board different from
     * <code>board</code> has the specified <code>name</code>
     */
    public boolean isRepeatedName(TBoard board, String name) {
        boolean isRepeated = false;
        int i = 0;
        String nameBoard = board.getBoardName();
        while (i < boardList.size() && !isRepeated) {
            if (!((TBoard) boardList.get(i)).getBoardName().equals(nameBoard)) {
                isRepeated = ((TBoard) boardList.get(i)).getBoardName().equals(name);
            }
            i++;
        }
        return isRepeated;
    }

    /**
     * Return an
     * <code>ArrayList</code> with all project boards.
     *
     * @return An <code>ArrayList</code> with all project boards
     */
    public ArrayList getBoardList() {
        return (ArrayList) boardList.clone();
    }

    public void setBoardList(ArrayList newBoardList) {
        boardList.clear();
        for (int i = 0; i < newBoardList.size(); i++) {
            boardList.add(((TBoardContainer) newBoardList.get(i)).getBoard());
        }
    }

    // Fired when the project or any of its boards has been changed
    private void fireProjectChange(TBoard changedBoard, int change) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ProjectChangeListener.class) {
                ((ProjectChangeListener) listeners[i + 1])
                        .projectChanged(new ProjectChangeEvent(this,
                        changedBoard, change));
            }
        }
    }

    /**
     * Adds an
     * <code>ProjectChangeListener</code>.
     *
     * The
     * <code>ProjectChangeListener</code>. will receive a
     * <code>ProjectChangeEvent</code> when the project of any of its boards has
     * been changed.
     *
     * @param listener The <code>ProjectChangeListener</code> that is to be
     * notified
     */
    public void addProjectChangeListener(ProjectChangeListener listener) {
        // Add to the listeners list
        listenerList.add(ProjectChangeListener.class, listener);
    }

    /**
     * Removes a
     * <code>ProjectChangeListener</code>.
     *
     * @param listener The <code>ProjectChangeListener</code> to remove
     */
    public void removeProjectChangeListener(ProjectChangeListener listener) {
        // Remove from the listeners list
        listenerList.remove(ProjectChangeListener.class, listener);
    }

    /**
     * Generates an XML
     * <code>Element</code> that contains the project information.
     *
     * @param doc The <code>Document</code> that represents the entire XML
     * document
     * @return The XML <code>Element</code> generated
     */
    public Element XMLEncode(Document doc) {
        // Create project node
        Element projectElement = doc.createElement("project");
        // Apply project attributes
        projectElement.setAttribute("name", getName());

        Element voice = doc.createElement("voice");
        voice.setAttribute("defaultVoiceName", getDefaultVoiceName());
        voice.setAttribute("defaultSynthMode", String.valueOf(getDefaultSynthMode()));
        projectElement.appendChild(voice);

        //anyadido orientacion si es modo android
        if (TEditor.get_android_mode() && !TEditor.get_android_orientation().equals("free")) {
            projectElement.setAttribute("orientation", TEditor.get_android_orientation());
        }

        // Append initial board node
        Element boardElement = doc.createElement("initial_board");
        boardElement.appendChild(doc.createTextNode(getInitialBoard().toString()));
        projectElement.appendChild(boardElement);

        // Append board nodes
        for (int i = 0; i < boardList.size(); i++) {
            projectElement.appendChild(getBoard(i).XMLEncode(doc));
        }

        // Append project node
        return projectElement;
    }

    /**
     * Returns a
     * <code>TProject</code> object from the data contained in the XML
     * <code>Element</code>.
     *
     * @param element The XML <code>Element</code> that contains the project
     * data
     * @return The generated <code>TProject</code>
     * @throws InvalidFormatException If <code>Element</code> has an invalid
     * format
     */
    public static TProject XMLDecode(Element element)
            throws InvalidFormatException {
        return XMLDecode(element, "");
    }

    /**
     * Returns a
     * <code>TProject</code> object from the data contained in the XML
     * <code>Element</code>.
     *
     * @param element The XML <code>Element</code> that contains the project
     * data
     * @param projectName The new <code>TProject</code> project name
     * @return The generated <code>TProject</code>
     * @throws InvalidFormatException If <code>Element</code> has an invalid
     * format
     */
    public static TProject XMLDecode(Element element, String projectName)
            throws InvalidFormatException {
        String initialBoardName = null;
        String defaultVoiceName = "";
        int defaultSynthMode = TBoardConstants.GOOGLE_MODE;

        if (element.getTagName().equals("project")) {
            // Create the new project
            TProject project = new TProject();
            // Get the project name
            // TUNE Find a better way of doing projectName usage
            if (projectName != "") {
                project.setName(projectName);
            } else if (element.hasAttribute("name")) {
                project.setName(element.getAttribute("name"));
            }

            //Anyadido orientacion en android
            if (element.hasAttribute("orientation")) {
                TEditor.set_android_orientation(element.getAttribute("orientation"));
            } else {
                TEditor.set_android_orientation("free");
            }

            // Get child nodes
            NodeList childNodes = element.getChildNodes();
            // For each node in child nodes
            for (int i = 0; i < childNodes.getLength(); i++) {
                String tagName = childNodes.item(i).getNodeName();
                // If is attributes, decode attribute node
                if (tagName.equals("board")) {
                    project.addBoard(TBoard.XMLDecode((Element) childNodes.item(i)));
                } else if (tagName.equals("initial_board")) {
                    initialBoardName = childNodes.item(i).getChildNodes().item(0).getNodeValue();
                } else if (tagName.equals("voice")) {
                    defaultVoiceName = childNodes.item(i).getAttributes().getNamedItem("defaultVoiceName").getTextContent();
                    try {
						defaultSynthMode = Integer.parseInt(childNodes.item(i).getAttributes().getNamedItem("defaultSynthMode").getTextContent());
					} catch (Exception e) {						
						e.printStackTrace();
					} 
                }
            }
            // Replace the following board attribute in all the board components
            // Make a list with all the components with the following board attribute
            Vector followingBoardComponents = new Vector();
            for (int i = 0; i < project.getBoardCount(); i++) {
                TBoardModel boardModel = (TBoardModel) project.getBoard(i).getModel();
                for (int j = 0; j < boardModel.getRootCount(); j++) {
                    TComponent component = (TComponent) boardModel.getRootAt(j);
                    if (component.getAttributes().containsKey(
                            TBoardConstants.FOLLOWING_BOARD)) {
                        followingBoardComponents.add(component);
                    }
                    // Check the components child
                    for (int k = 0; k < component.getChildCount(); k++) {
                        TComponent childComponent = (TComponent) component.getChildAt(k);
                        if (childComponent.getAttributes().containsKey(TBoardConstants.FOLLOWING_BOARD)) {
                            followingBoardComponents.add(childComponent);
                        }
                    }
                }
            }
            // Replace the board id with the corresponding board
            for (int i = 0; i < followingBoardComponents.size(); i++) {
                TComponent component = (TComponent) followingBoardComponents.get(i);
                TBoard followingBoard = project.getBoard((String) component.getAttributes().get(TBoardConstants.FOLLOWING_BOARD));
                if (followingBoard != null) {
                    TBoardConstants.setFollowingBoard(component.getAttributes(), followingBoard.getBoardName());
                } else {
                    new InvalidFormatException();
                }
            }
            // Set the initial board with its initialBoardName
            project.setInitialBoard(project.getBoard(initialBoardName));

            //Set the default voice name
            project.setDefaultVoiceName(defaultVoiceName);
            project.setDefaultSynthMode(defaultSynthMode);

            // Return the project
            return project;
        }
        throw new InvalidFormatException();
    }
}