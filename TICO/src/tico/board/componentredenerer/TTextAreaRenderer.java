/*
 * File: TTextAreaRenderer.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Aug 6, 2006
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

package tico.board.componentredenerer;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import org.jgraph.graph.CellView;

import tico.board.TBoardConstants;
import tico.board.components.TComponent;
import tico.editor.TEditor;

/**
 * <code>Swing</code> which implements the <code>TLineRenderer</code> visualization.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TTextAreaRenderer extends TComponentRenderer {

	transient protected String id = "";

	transient protected int borderWidth;

	transient protected int verticalAlignment, horizontalAlignment;

	transient protected int height, width;

	/* (non-Javadoc)
	 * @see org.jgraph.graph.VertexRenderer#installAttributes(org.jgraph.graph.CellView)
	 */
	protected void installAttributes(CellView view) {
		// Get the text area attributes
		Map map = view.getAllAttributes();

		// Get the vairables needed in the paint function
		id = ((TComponent)view.getCell()).getId();

		height = (int)TBoardConstants.getBounds(map).getHeight();
		width = (int)TBoardConstants.getBounds(map).getWidth();

		// Needed to apply JLabel properties correctly with the html marks
		setText("");

		// Apply all the component properties
		// Apply border
		Color borderColor = TBoardConstants.getBorderColor(map);
		if (borderColor != null) {
			borderWidth = Math.max(1, Math.round(TBoardConstants
					.getLineWidth(map)));
			setBorder(BorderFactory.createLineBorder(borderColor, borderWidth));
		} else {
			borderWidth = 0;
			setBorder(null);
		}

		// Apply background and gradient
		Color backgroundColor = TBoardConstants.getBackground(map);
		setBackground(backgroundColor);
		setOpaque((backgroundColor != null));

		//if(!TEditor.get_android_mode())
			setGradientColor(TBoardConstants.getGradientColor(map));

		// Apply text font properties
		Font font = TBoardConstants.getFont(map);
		if (font == null)
			font = TBoardConstants.DEFAULTFONT;
		//setFont(TBoardConstants.getFont(map));
		//visualizamos la fuente de android si estamos en modo android
		if(TEditor.get_android_mode()) font=new Font("Droid Sans",font.getStyle(),font.getSize());
		setFont(font);
		
		Color foregroundColor = TBoardConstants.getForeground(map);
		setForeground((foregroundColor != null) ? foregroundColor
				: TBoardConstants.DEFAULT_FOREGROUND);
		
		// Apply text align properties
		String htmlBegin = "<html><body>", text, htmlEnd = "</p></body></html>";

		verticalAlignment = TBoardConstants.getVerticalAlignment(map);
		horizontalAlignment = TBoardConstants.getHorizontalAlignment(map);
		// TUNE Correct vertical alignment html problems		
		setHorizontalAlignment(horizontalAlignment);
		setVerticalAlignment(verticalAlignment);		

		switch (TBoardConstants.getHorizontalAlignment(map)) {
		case SwingConstants.CENTER:
			htmlBegin = htmlBegin + "<p align='center'>";
			htmlEnd = "</p>";
			break;
		case SwingConstants.RIGHT:
			htmlBegin = htmlBegin + "<p align='right'>";
			htmlEnd = "&nbsp;</p>";
		}

		// Apply text
		text = TBoardConstants.getText(map);
		if (text == null)
			text = "";
		setText(htmlBegin + text + htmlEnd);
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		// Variables for id placement
		int topIdPosition, leftIdPosition;

		// Draw the gradient
		if (getGradientColor() != null && isOpaque()) {
			setOpaque(false);
			g2.setPaint(new GradientPaint(0, 0, getBackground(), getWidth(),
					getHeight(), getGradientColor(), true));
			g2.fillRect(0, 0, getWidth(), getHeight());
		}

		super.paint(g);

		if (id != null) {
			// Draw the id in the opposite position of the text
			if (verticalAlignment != SwingConstants.BOTTOM)
				topIdPosition = height - (6 + borderWidth);
			else
				topIdPosition = 14 + borderWidth;

			if (horizontalAlignment != SwingConstants.LEFT)
				leftIdPosition = 6 + borderWidth;
			else
				leftIdPosition = width - (6 + borderWidth)
						- g.getFontMetrics(ID_FONT).stringWidth(id);

			g2.setFont(ID_FONT);
			if (getBackground() != Color.BLACK)
				g2.setColor(Color.BLACK);
			else
				g2.setColor(Color.WHITE);
			g2.drawString(id, leftIdPosition, topIdPosition);
		}
	}
}
