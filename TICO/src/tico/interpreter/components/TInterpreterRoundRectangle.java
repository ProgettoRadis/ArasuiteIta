/*
 * File: TInterpreterRoundRectangle.java
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
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JButton;

public class TInterpreterRoundRectangle extends JButton {

	Color borderColorRoundRectangle;
	Color backgroundColorRoundRectangle;
	Color gradientColorRoundRectangle;
	int borderSizeRoundRectangle;
	int widthRoundRectangle;
	int heightRoundRectangle;
	boolean transparentBackgroundRoundRectangle;
	
  public TInterpreterRoundRectangle(Color borderColor, Color backgroundColor, Color gradientColor, float borderSize, Rectangle bounds, boolean transparentBackground) {
	  super();
	  borderColorRoundRectangle = borderColor;
	  backgroundColorRoundRectangle = backgroundColor;
	  gradientColorRoundRectangle = gradientColor;
	  borderSizeRoundRectangle = (int)borderSize;
	  widthRoundRectangle = bounds.width;
	  heightRoundRectangle = bounds.height;
	  transparentBackgroundRoundRectangle = transparentBackground;
	  
	  this.setLocation(bounds.x, bounds.y);
	  this.setSize(bounds.width, bounds.height);
	  this.setEnabled(false);
	  setContentAreaFilled(false);
	}
  
  public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		Dimension dimension = getSize();
		
		int roundRectArc = getArcSize(
				dimension.width - borderSizeRoundRectangle,
				dimension.height - borderSizeRoundRectangle);
		
		if (!transparentBackgroundRoundRectangle) {
			g.setColor(backgroundColorRoundRectangle);
			if (gradientColorRoundRectangle != null) {
				setOpaque(false);
				g2.setPaint(new GradientPaint(0, 0, backgroundColorRoundRectangle,
						getWidth(), getHeight(), gradientColorRoundRectangle, true));
			}
			g.fillRoundRect(borderSizeRoundRectangle - 1, borderSizeRoundRectangle - 1,
					dimension.width - 2*borderSizeRoundRectangle,
					dimension.height - 2*borderSizeRoundRectangle,
					roundRectArc, roundRectArc);
		}
		setBorder(null);
		setOpaque(false);
		super.paint(g);
		
		if (borderColorRoundRectangle != null) {
			g.setColor(borderColorRoundRectangle);
			g2.setStroke(new BasicStroke(borderSizeRoundRectangle));
			g.drawRoundRect(borderSizeRoundRectangle - 1, borderSizeRoundRectangle - 1,
					dimension.width - 2*borderSizeRoundRectangle,
					dimension.height - 2*borderSizeRoundRectangle,
					roundRectArc, roundRectArc);
		}else{
			if (gradientColorRoundRectangle!=null){
				setOpaque(false);
				g2.setPaint(new GradientPaint(0, 0, backgroundColorRoundRectangle,
						getWidth(), getHeight(), gradientColorRoundRectangle, true));
				g.fillRoundRect(0, 0,
						dimension.width,
						dimension.height,
						roundRectArc, roundRectArc);
			}
			else{
				g.setColor(backgroundColorRoundRectangle);
				g.fillRoundRect(0, 0,
						dimension.width,
						dimension.height,
						roundRectArc, roundRectArc);
			}			
		}
	}
  
  public static int getArcSize(int width, int height) {
		int arcSize;
		// The arc width of a activity rectangle is 1/5th of the larger
		// of the two of the dimensions passed in, but at most 1/2
		// of the smaller of the two. 1/5 because it looks nice and 1/2
		// so the arc can complete in the given dimension
		if (width <= height) {
			arcSize = height / 5;
			if (arcSize > (width / 2)) {
				arcSize = width / 2;
			}
		} else {
			arcSize = width / 5;
			if (arcSize > (height / 2)) {
				arcSize = height / 2;
			}
		}
		return arcSize;
	}
}