/*
 * File: TGenerateRules.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Beatriz Mateo
 * 
 * Date: Oct 2, 2007
 * 
 * Company: Universidad de Zaragoza, CPS, DIIS, GIGA
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

package tico.rules;

import jess.*;

import java.awt.Color;
import java.io.File;
import java.io.StringWriter;
import java.util.Map;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.jgraph.graph.CellView;

import tico.board.TBoard;
import tico.board.TBoardConstants;
import tico.board.TBoardModel;
import tico.board.TProject;
import tico.board.components.TCell;
import tico.board.components.TComponent;
import tico.board.components.TGridCell;
import tico.configuration.TLanguage;
import tico.interpreter.TInterpreter;
import tico.interpreter.TInterpreterConstants;
import tico.rules.TShowResults;
import tico.rules.TComparationMin;
import tico.rules.TComparationMax;
import tico.rules.TExist;
import tico.rules.TType;
import tico.rules.TLightness;
import tico.rules.database.TLoadRule;

/**
 * The <code>TGenerateRules</code> class creates the rules for validation, getting
 * the features of the element to validate.
 * 
 * Calls the Jess engine.
 * 
 * @author Beatriz Mateo
 * @version 0.1 Oct 2, 2007
 */

public class TGenerateRules{
	
	private Rete engine;
	StringWriter sw;
	
	String boardName;
	public Vector<TAttribute> attributeList = new Vector<TAttribute>();
	private final static String newline = "\n";
	// The Jess file
	private static String RULES_DIRECTORY = "src" + File.separator + "tico" 
	+ File.separator + "rules" + File.separator + "TRules.clp";
	
	
	// Creates a Jess engine
	public TGenerateRules() {
	    engine = new Rete();
	}
	
	
	// Generate the rules
	private void generateRules(String element, String name) {
		sw = new StringWriter();
		engine.addOutputRouter("out", sw);
		try {
			engine.batch(RULES_DIRECTORY);
			String ret = createPredefinedRules(element, name);
			if (ret == null) {
				engine.run();
				engine.clear();
				getResults(element);
			}
			else
				JOptionPane.showMessageDialog(null,
					TLanguage.getString("TGenerateRules.ERROR_MESSAGE") + " " + ret, 
					TLanguage.getString("ERROR"),
					JOptionPane.ERROR_MESSAGE);
			
		} catch (JessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	// Create the rules
	private String createPredefinedRules(String element, String name) {
		Vector<TRule> ruleList = new Vector<TRule>();
			
		TLoadRule rules = new TLoadRule();
		ruleList = rules.loadRulesByType(element, attributeList);
		
		for (int i=0; i<ruleList.size(); i++) {
			TRule rule = ruleList.get(i);
			try {		
				// Generate rule
				if (rule.getFunctionXML().compareTo("comparationMin")==0) {
					TComparationMin comparationMin = new TComparationMin(rule.getNameXML(), name, rule.getAttribute().getValue(), rule.getParameter().getValue(), rule.getMessageXML());
					Funcall f = new Funcall("definstance", engine);	//type and engine
					f.add(new Value("comparationMin", RU.ATOM));	//class and object type
					f.add(new Value(comparationMin));		 		//the object
					f.execute(engine.getGlobalContext());	  	 	//execution
				} else if (rule.getFunctionXML().compareTo("comparationMax")==0) {
					TComparationMax comparationMax = new TComparationMax(rule.getNameXML(), name, rule.getAttribute().getValue(), rule.getParameter().getValue(), rule.getMessageXML());
					Funcall f = new Funcall("definstance", engine);	
					f.add(new Value("comparationMax", RU.ATOM));
					f.add(new Value(comparationMax));		 		
					f.execute(engine.getGlobalContext());	  	 	
				} else if (rule.getFunctionXML().compareTo("exist")==0) {
					TExist exist = new TExist(rule.getNameXML(), name, rule.getAttribute().getValue(), rule.getParameter().getValue(), rule.getMessageXML());
					Funcall f = new Funcall("definstance", engine);	
					f.add(new Value("exist", RU.ATOM));				
					f.add(new Value(exist));						
					f.execute(engine.getGlobalContext());	
				} else if (rule.getFunctionXML().compareTo("type")==0){ 
					TType type = new TType(rule.getNameXML(), name, rule.getAttribute().getValue(), rule.getParameter().getValue(), rule.getMessageXML());
					Funcall f = new Funcall("definstance", engine);
					f.add(new Value("type", RU.ATOM));				
					f.add(new Value(type));						
					f.execute(engine.getGlobalContext());	
				} else { 
					TLightness lightness = new TLightness(rule.getNameXML(), name, rule.getAttribute().getValue(), rule.getAttribute2().getValue(), rule.getParameter().getValue(), rule.getMessageXML());
					Funcall f = new Funcall("definstance", engine);
					f.add(new Value("lightness", RU.ATOM));				
					f.add(new Value(lightness));						
					f.execute(engine.getGlobalContext());	
				}
			} catch (Exception e) {
				return rule.getName();
			}
		}
		return null;
	}
	
	
	// Get the attributes of a project
	public void getProjectAttributes(TProject project) throws JessException {
		// Project attributes
		String projectName;
		int projectNumBoards;
		
		attributeList.clear();
		// get all project attributes
		projectName=project.getName();
		projectNumBoards=project.getBoardCount();
		attributeList.add(new TAttribute(TLanguage.getString("Rules."
				+ "PROJECTNUMBOARDS"),"projectNumBoards", (Object) projectNumBoards));

		// Call the rule engine
		generateRules("project", projectName);
		// Validate all boards of the project saved in boardList
		for (int i=0; i<projectNumBoards; i++) {
			getBoardAttributes(project.getBoard(i));
		}
	}
	
	
	// gets the board attributes
	public void getBoardAttributes(TBoard board) throws JessException {
		// Board attributes
		int boardHeight;
		int boardWidth;
		Color boardColor;
		Boolean boardNoImage;
		Boolean boardSound;
		int boardLightness;
		
		// Get all board attributes
		Map boardMap = ((TBoardModel)board.getModel()).getAttributes();		 

		boardName=board.getBoardName();
		boardHeight=TBoardConstants.getSize(boardMap).height;
		attributeList.add(new TAttribute(TLanguage.getString("Rules."
				+ "BOARDHEIGHT"), "boardHeight", (Object) boardHeight));
		
		boardWidth=TBoardConstants.getSize(boardMap).width;
		attributeList.add(new TAttribute(TLanguage.getString("Rules."
				+ "BOARDWIDTH"), "boardWidth", (Object) boardWidth));
		
		boardColor=TBoardConstants.getBackground(boardMap);
		TColorTransformation color = new TColorTransformation();
		boardLightness = color.rgbToLab(boardColor.getRed(), boardColor.getGreen(), 
				boardColor.getBlue());
		attributeList.add(new TAttribute(TLanguage.getString("Rules."
				+ "BOARDLIGHTNESS"), "boardLightness", (Object) boardLightness));

		if (TBoardConstants.getIcon(boardMap)==null)
			boardNoImage=true;
		else boardNoImage=false;
		attributeList.add(new TAttribute(TLanguage.getString("Rules."
				+ "BOARDNOIMAGE"), "boardNoImage", (Object) boardNoImage));

		if(TBoardConstants.getSoundFile(boardMap)==null)
			boardSound=false;
		else boardSound=true;
		attributeList.add(new TAttribute(TLanguage.getString("Rules."
				+ "BOARDSOUND"), "boardSound", (Object) boardSound));
		
		// Call the rule engine
		generateRules("board", boardName);
		// Validate all the cells of the board
		getCellsAttributes(board);
	}
	
	
	// get cells' board attributes
	public void getCellsAttributes(TBoard board) throws JessException {
		// Cell attributes
		String cellid;
		int cellHeight;
		int cellWidth;
		Color cellBackgroundColor;
		int cellLightness;
		int cellBorderWidth;
		Color cellBorderColor;
		int cellBorderLightness;
		Boolean cellText;
		int cellTextSize;
		Boolean cellTextFont;
		Boolean cellTextStyle;
		Color cellTextColor;
		int cellTextLightness;
		Boolean cellTextType;
		Boolean cellSound;
		Boolean cellNoImage;
		int cellImageHeight;
		int cellImageWidth;
		Boolean cellNoAlternativeIcon;
		int cellIconHeight;
		int cellIconWidth;
		int cellRow;
		int cellColumn;
		Boolean cellSendText;
		int cellSendTextTimer;
		
		// Get all cell attributes
		CellView[] views = board.getGraphLayoutCache().getCellViews();//.getAllViews();
		for (int i = 0; i < views.length; i++) {
			// Select only cell or grid cells
			if ((views[i].getCell() instanceof TCell) || (views[i].getCell() instanceof TGridCell)) {
			Map map = views[i].getAllAttributes();

			cellid = ((TComponent)views[i].getCell()).getId();

			cellHeight = (int)TBoardConstants.getBounds(map).getHeight();
			attributeList.add(new TAttribute(TLanguage.getString("Rules."
					+ "CELLHEIGHT"), "cellHeight", (Object) cellHeight));
			cellWidth = (int)TBoardConstants.getBounds(map).getWidth();
			attributeList.add(new TAttribute(TLanguage.getString("Rules."
					+ "CELLWIDTH"), "cellWidth", (Object) cellWidth));
			cellBackgroundColor = TBoardConstants.getBackground(map);
			TColorTransformation color = new TColorTransformation();
			try {
				cellLightness = color.rgbToLab(cellBackgroundColor.getRed(), cellBackgroundColor.getGreen(), 
					cellBackgroundColor.getBlue());
			} catch (Exception e) {
				cellLightness = 0;
			}
			attributeList.add(new TAttribute(TLanguage.getString("Rules."
					+ "CELLLIGHTNESS"), "cellLightness", (Object) cellLightness));

			cellBorderColor = TBoardConstants.getBorderColor(map);
			try {
				cellBorderLightness = color.rgbToLab(cellBorderColor.getRed(), cellBorderColor.getGreen(), 
					cellBorderColor.getBlue());
			} catch (Exception e) {
				cellBorderLightness = 0;
			}
			attributeList.add(new TAttribute(TLanguage.getString("Rules."
					+ "CELLBORDERLIGHTNESS"), "cellBorderLightness", (Object) cellBorderLightness));

			if (cellBorderColor != null)
				cellBorderWidth = Math.max(1, Math.round(TBoardConstants.getLineWidth(map)));
			else cellBorderWidth = 0;
			attributeList.add(new TAttribute(TLanguage.getString("Rules."
					+ "CELLBORDERWIDTH"), "cellBorderWidth", (Object) cellBorderWidth));

			String cellTextString = TBoardConstants.getText(map);
			if ((cellTextString=="") || (cellTextString==null)){
				cellText=false;
				cellTextSize=(Integer)0;
				cellTextType=true;
				cellTextFont=true;
				cellTextStyle=true;
				cellTextLightness=(Integer)(-1);
			} else {
				cellText=true;
				cellTextSize=TBoardConstants.getFont(map).getSize();
				String font=TBoardConstants.getFont(map).getFontName();
				if (font.compareTo("Arial")!=0 || font.compareTo("Verdana")!=0)
					cellTextFont=false;
				else cellTextFont=true; // font is Arial or Verdana
				int style=TBoardConstants.getFont(map).getStyle();
				if (style!=0)
					cellTextStyle=false;
				else cellTextStyle=true; // style is PLAIN
				
				String cellTextUpperString=cellTextString.toUpperCase();
				// 0 if equal, < 0 if cellTextString is upper than upper case
				// > 0 if cellTextString is lower than upper case
				if (cellTextString.compareTo(cellTextUpperString)>0) // capital letters are lower than small letters
					cellTextType=false; // lower case
				else cellTextType=true; // upper case
				
				cellTextColor=TBoardConstants.getForeground(map);
				try {
					cellTextLightness = color.rgbToLab(cellTextColor.getRed(), cellTextColor.getGreen(), 
						cellTextColor.getBlue());
				} catch (Exception e) {
					cellTextLightness = 0;
				}
			}
			attributeList.add(new TAttribute(TLanguage.getString("Rules."
					+ "CELLTEXT"), "cellText", (Object) cellText));
			attributeList.add(new TAttribute(TLanguage.getString("Rules."
					+ "CELLTEXTSIZE"), "cellTextSize", (Object) cellTextSize));
			attributeList.add(new TAttribute(TLanguage.getString("Rules."
					+ "CELLTEXTTYPE"), "cellTextType", (Object) cellTextType));
			attributeList.add(new TAttribute(TLanguage.getString("Rules."
					+ "CELLTEXTFONT"), "cellTextFont", (Object) cellTextFont));
			attributeList.add(new TAttribute(TLanguage.getString("Rules."
					+ "CELLTEXTSTYLE"), "cellTextStyle", (Object) cellTextStyle));
			attributeList.add(new TAttribute(TLanguage.getString("Rules."
					+ "CELLTEXTLIGHTNESS"), "cellTextLightness", (Object) cellTextLightness));
			try {
				if (TBoardConstants.getSoundFile(map) == null)
					cellSound=false;
				else cellSound=true;
			} catch (Exception e){
				cellSound=false;
			}
			attributeList.add(new TAttribute(TLanguage.getString("Rules."
					+ "CELLSOUND"), "cellSound", (Object) cellSound));

			if ((ImageIcon)TBoardConstants.getIcon(map)!=null) {
				cellNoImage = false;
				cellImageHeight = TBoardConstants.getIcon(map).getIconHeight();
				cellImageWidth = TBoardConstants.getIcon(map).getIconWidth();
			} else {
				cellNoImage = true;
				cellImageHeight = 0;
				cellImageWidth = 0;
			}
			attributeList.add(new TAttribute(TLanguage.getString("Rules."
					+ "CELLNOIMAGE"), "cellNoImage", (Object) cellNoImage));
			attributeList.add(new TAttribute(TLanguage.getString("Rules."
					+ "CELLIMAGEHEIGHT"), "cellImageHeight", (Object) cellImageHeight));
			attributeList.add(new TAttribute(TLanguage.getString("Rules."
					+ "CELLIMAGEWIDTH"), "cellImageWidth", (Object) cellImageWidth));
			
			cellRow = TBoardConstants.getRow(map);
			attributeList.add(new TAttribute(TLanguage.getString("Rules."
					+ "CELLROW"), "cellRow", (Object) cellRow));

			cellColumn = TBoardConstants.getColumn(map);
			attributeList.add(new TAttribute(TLanguage.getString("Rules."
					+ "CELLCOLUMN"), "cellColumn", (Object) cellColumn));
			
			if (TBoardConstants.getAlternativeIcon(map)!=null) {
				cellNoAlternativeIcon = false;
				cellIconHeight = TBoardConstants.getAlternativeIcon(map).getIconHeight();
				cellIconWidth = TBoardConstants.getAlternativeIcon(map).getIconWidth();
			} else {
				cellNoAlternativeIcon = true;
				cellIconHeight = 0;
				cellIconWidth = 0;
			}
			attributeList.add(new TAttribute(TLanguage.getString("Rules."
					+ "CELLNOALTERNATIVEICON"), "cellNoAlternativeIcon", (Object) cellNoAlternativeIcon));
			attributeList.add(new TAttribute(TLanguage.getString("Rules."
					+ "CELLICONHEIGHT"), "cellIconHeight", (Object) cellIconHeight));
			attributeList.add(new TAttribute(TLanguage.getString("Rules."
					+ "CELLICONWIDTH"), "cellIconWidth", (Object) cellIconWidth));
			
			if (TBoardConstants.getSendText(map) != null && TBoardConstants.getSendText(map).compareTo("")!=0) {
				cellSendText = true;
				cellSendTextTimer = TBoardConstants.getSendTextTimer(map);
			}
			else {
				cellSendText = false;
				cellSendTextTimer = 0;
			}
			
			// Call the rule engine
			generateRules("cell", cellid);
			}
		}	
	}
	
	public void getInterpreterAttributes(TInterpreter interpreter) throws JessException {
		// Interpreter attributes
		int intDelay;
		Boolean intMouseBar;
		int intAccumulated;
		
		intAccumulated = TInterpreterConstants.accumulatedCells;
		attributeList.add(new TAttribute(TLanguage.getString("Rules."
					+ "INTACCUMULATED"), "intAccumulated", (Object) intAccumulated));
		
		intDelay = TInterpreterConstants.interpreterDelay;
		attributeList.add(new TAttribute(TLanguage.getString("Rules."
				+ "INTDELAY"), "intDelay", (Object) intDelay));
		
		if (TInterpreter.returnMouseMode().equals(TInterpreterConstants.AUTOMATIC_SCANNING_MODE))
			intMouseBar = true;
		else intMouseBar = false;

		attributeList.add(new TAttribute(TLanguage.getString("Rules."
				+ "INTMOUSEBAR"), "intMouseBar", (Object) intMouseBar));
		
		generateRules("interpreter", interpreter.getProject().getName());
	}
	
	
	// Gets the Jess engine results and adds them to the rest of results
	private void getResults(String element) {
		TShowResults res = new TShowResults();
		if (element.compareTo("board")==0)
			res.addToValidationResults(TLanguage.getString("TBoardRules.ANALYZE")+ " " + boardName
					+ newline + "___________________________________________");
		if (sw.toString().compareTo("")!=0) {
			String[] strVector = sw.toString().split(";");
			for (int i=0; i<strVector.length; i++)
				res.addToValidationResults(strVector[i]);
		} else res.addToValidationResults("");
	}
}
