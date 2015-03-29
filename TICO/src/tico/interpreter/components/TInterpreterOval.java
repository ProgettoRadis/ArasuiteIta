/*
 * File: TInterpreterOval.java
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

public class TInterpreterOval extends JButton {

	Color borderColorOval;
	int borderSizeOval;
	Color backgroundColorOval;
	Color gradientColorOval;
	int widthOval;
	int heightOval;
	boolean transparentBackgroundOval;

  public TInterpreterOval(Color borderColor, float borderSize, Rectangle bounds, Color backgroundColor, Color gradientColor, boolean transparentBackground) {
    super();
    borderColorOval = borderColor;
    borderSizeOval = (int)borderSize;
    backgroundColorOval = backgroundColor;
    gradientColorOval = gradientColor;
    transparentBackgroundOval = transparentBackground;
    widthOval=bounds.width;
    heightOval=bounds.height;
    this.setLocation(bounds.x, bounds.y);
    this.setSize(bounds.width, bounds.height);
    this.setEnabled(false);
    setContentAreaFilled(false);
    }
  
  public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		Dimension dimension = getSize();
				
		if (!transparentBackgroundOval) {
			g.setColor(backgroundColorOval);
			if (gradientColorOval != null) {
				setOpaque(false);
				g2.setPaint(new GradientPaint(0, 0, backgroundColorOval,
						getWidth(), getHeight(), gradientColorOval, true));
			}
			g.fillOval(borderSizeOval - 1, borderSizeOval - 1,
					dimension.width - 2*borderSizeOval,
					dimension.height - 2*borderSizeOval);
		}
		
		setBorder(null);
		setOpaque(false);
		super.paint(g);
		
		if (borderColorOval != null) {
			g.setColor(borderColorOval);
			g2.setStroke(new BasicStroke(borderSizeOval));
			g.drawOval(borderSizeOval - 1, borderSizeOval - 1,
					dimension.width - 2*borderSizeOval,
					dimension.height - 2*borderSizeOval);
		}else{
			if (gradientColorOval!=null){
				setOpaque(false);
				g2.setPaint(new GradientPaint(0, 0, backgroundColorOval,
						getWidth(), getHeight(), gradientColorOval, true));
				g.fillOval(0, 0, dimension.width, dimension.height);
				
			}else{
				g.setColor(backgroundColorOval);
				g.fillOval(0, 0, dimension.width, dimension.height);
			}		
		}
		
	}
  
  }
