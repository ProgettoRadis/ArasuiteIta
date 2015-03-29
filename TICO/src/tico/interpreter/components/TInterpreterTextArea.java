/*
 * File: TInterpreterTextArea.java
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
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;


public class TInterpreterTextArea extends JLabel{
	
	boolean transparentBackgroundTextArea;
	Color gradientColorTextArea;
	
	public TInterpreterTextArea (){
		super();
	}
	
	public TInterpreterTextArea setAttributes(String id,Color borderColor, float borderSize, Color backgroundColor, Color gradientColor,  Rectangle bounds, String texto, Font f, Color textColor, int ha, int va, boolean backgroundTransparent){
		
		transparentBackgroundTextArea = backgroundTransparent;
		gradientColorTextArea = gradientColor;
		
		setFont(f);
		
		if (backgroundColor==null){
			setBackground(new Color(0,0,0,0));
		}
		else{
			setOpaque(true);
			setBackground(backgroundColor);
		}
		setBorder(null);
		if (borderColor!=null)
			setBorder(new LineBorder(borderColor, (int)borderSize));
		
		setForeground(textColor);

		setName(id);
		setBounds(bounds);
		
		// Apply text align properties
		String htmlBegin = "<html><body>", text="", htmlEnd = "</p></body></html>";
		
		setHorizontalAlignment(ha);
		setVerticalAlignment(va);
		
		switch (ha) {
		case SwingConstants.CENTER:
			htmlBegin = htmlBegin + "<p align='center'>";
			htmlEnd = "</p>";
			break;
		case SwingConstants.RIGHT:
			htmlBegin = htmlBegin + "<p align='right'>";
			htmlEnd = "&nbsp;</p>";
		}
		
		if (texto!=null){
			text = texto;
		}
		
		this.setText(htmlBegin + text + htmlEnd);		

		return this;
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		// Draw the gradient
		if (!transparentBackgroundTextArea && gradientColorTextArea != null ) { //&& isOpaque()
			setOpaque(false);
			g2.setPaint(new GradientPaint(0, 0, getBackground(), getWidth(),
					getHeight(), gradientColorTextArea, true));
			g2.fillRect(0, 0, getWidth(), getHeight());
		}

		super.paint(g);
	}
	
}
