/*
 * File: TInterpreterRectangle.java
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

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

public class TInterpreterRectangle extends JButton{
	
	boolean transparentBackgroundRectangle;
	Color gradientColorRectangle;
	
	public TInterpreterRectangle (){
		super();		
	}

	public TInterpreterRectangle setAttributes(Color borderColor, Rectangle r, float borderSize, Color backgroundColor, Color gradientColor, boolean transparentBackground){
		this.setBounds(r);
		this.setBackground(backgroundColor);
		transparentBackgroundRectangle = transparentBackground;
		gradientColorRectangle = gradientColor;
		setBorder(null);
		if (borderColor!=null)
			this.setBorder(new LineBorder(borderColor, (int)borderSize));
		this.setEnabled(false);
		return this;
	}
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		// Draw the gradient
		if (!transparentBackgroundRectangle && gradientColorRectangle != null) {
			setOpaque(false);
			g2.setPaint(new GradientPaint(0, 0, getBackground(), getWidth(),
					getHeight(), gradientColorRectangle, true));
			g2.fillRect(0, 0, getWidth(), getHeight());
		}
		super.paint(g);
	}
}
