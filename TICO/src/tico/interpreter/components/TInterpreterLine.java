/*
 * File: TInterpreterLine.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Isabel Gonz�lez y Carolina Palacio
 * 
 * Date: Nov, 2009
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

package tico.interpreter.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JButton;

import tico.board.TBoardConstants;

public class TInterpreterLine extends JButton {

	Color borderColorLine;
	float sizeLine;
	int startCornerLine;
	int widthLine;
	int heightLine;

  public TInterpreterLine(Color borderColor, float borderSize, Rectangle bounds, int startCorner) {
    super();
    borderColorLine = borderColor;
    sizeLine = borderSize;
    widthLine = bounds.width;
    heightLine = bounds.height;
    startCornerLine=startCorner;
	this.setLocation(bounds.x, bounds.y);
	this.setSize(bounds.width, bounds.height);
	this.setEnabled(false);
    setContentAreaFilled(false);
    }
  
  protected void paintComponent(Graphics g) {	  
	  Graphics2D graphics2 = (Graphics2D)g;
	  graphics2.setColor(borderColorLine);
	  graphics2.setStroke(new BasicStroke(sizeLine));
	  
	  if (startCornerLine==TBoardConstants.TOP_LEFT_CORNER){
		  g.drawLine(0, 0, widthLine, heightLine);
	  }else if (startCornerLine==TBoardConstants.TOP_RIGHT_CORNER){
		  g.drawLine(widthLine, 0, 0, heightLine);
	  }else if (startCornerLine==TBoardConstants.BOTTOM_LEFT_CORNER){
		  g.drawLine(0, heightLine, widthLine, 0);
	  }else if (startCornerLine==TBoardConstants.BOTTOM_RIGHT_CORNER){
		  g.drawLine(widthLine, heightLine, 0, 0);
	  }
	  super.paintComponent(g);
    }

  protected void paintBorder(Graphics g) {
	  Graphics2D graphics2 = (Graphics2D)g;
	  graphics2.setColor(borderColorLine);
	  graphics2.setStroke(new BasicStroke(sizeLine));
	
	  if (startCornerLine==TBoardConstants.TOP_LEFT_CORNER){
		  g.drawLine(0, 0, widthLine, heightLine);
	  }else if (startCornerLine==TBoardConstants.TOP_RIGHT_CORNER){
		  g.drawLine(widthLine, 0, 0, heightLine);
	  }else if (startCornerLine==TBoardConstants.BOTTOM_LEFT_CORNER){
		  g.drawLine(0, heightLine, widthLine, 0);
	  }else if (startCornerLine==TBoardConstants.BOTTOM_RIGHT_CORNER){
		  g.drawLine(widthLine, heightLine, 0, 0);
	  }
   }
 }