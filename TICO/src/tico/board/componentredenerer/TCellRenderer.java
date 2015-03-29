/*
 * File: TCellRenderer.java
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
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import org.jgraph.graph.CellView;

import tico.board.TBoardConstants;
import tico.board.components.TComponent;
import tico.editor.TEditor;
//import tico.editor.TEditor;

/**
 * <code>Swing</code> which implements the <code>TCell</code> visualization.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TCellRenderer extends TComponentRenderer {

	private final static int VERTICAL_ICON_MARGIN = 15;
	
	private final static int HORIZONTAL_ICON_MARGIN = 10;
	// Default distance between the icon and the text
	private final static int GAP_ICON_TEXT = 5;

	transient protected String id;

	transient protected int borderWidth;

	/* (non-Javadoc)
	 * @see org.jgraph.graph.VertexRenderer#installAttributes(org.jgraph.graph.CellView)
	 */
	protected void installAttributes(CellView view) {
		if(!TEditor.get_android_mode()){
			installAttributesNormal(view);
		}else{
			installAttributesAndroid(view);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		// Draw the gradient
		if (getGradientColor() != null && isOpaque()) {
			setOpaque(false);
			g2.setPaint(new GradientPaint(0, 0, getBackground(), getWidth(),
					getHeight(), getGradientColor(), true));
			g2.fillRect(0, 0, getWidth(), getHeight());
		}

		super.paint(g);
		
		if (id != null) {
			// Draw id in the top right corner
			g2.setFont(ID_FONT);
			if (getBackground() != Color.BLACK)
				g2.setColor(Color.BLACK);
			else
				g2.setColor(Color.WHITE);
			g2.drawString(id, 6 + borderWidth, 14 + borderWidth);
		}
	}
	
	
	
	protected void installAttributesNormal(CellView view) {
		
		// Get the cell attributes
		Map map = view.getAllAttributes();

		// Get the variables needed in the paint function
		id = ((TComponent)view.getCell()).getId();

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

		setGradientColor(TBoardConstants.getGradientColor(map));

		// Apply text font properties
		Font font = TBoardConstants.getFont(map);
		if (font == null)
			font = TBoardConstants.DEFAULTFONT;
		setFont(TBoardConstants.getFont(map));

		Color foregroundColor = TBoardConstants.getForeground(map);
		setForeground((foregroundColor != null) ? foregroundColor
				: TBoardConstants.DEFAULT_FOREGROUND);

		// Apply text align properties
		setVerticalAlignment(SwingConstants.CENTER);
		setHorizontalAlignment(SwingConstants.CENTER);
		setVerticalTextPosition(TBoardConstants.getVerticalTextPosition(map));
		setHorizontalTextPosition(SwingConstants.CENTER);

		// Apply text properties
		String text = TBoardConstants.getText(map);
		if (text == null)
			text = "";
		setText(text);
		
		// Check if the text fits in the cell with the font
		int cellTextSpace = (int)TBoardConstants.getBounds(map).getWidth()-
							2*borderWidth-2*HORIZONTAL_ICON_MARGIN;
		JButton j = new JButton();
		j.setSize(cellTextSpace, cellTextSpace);
		FontMetrics fm = j.getFontMetrics(font);
		int textWidth = fm.stringWidth(text);
		if (textWidth > cellTextSpace){
			int fontSize = font.getSize();
			int fontStyle = font.getStyle();
			String fontName = font.getFontName();
			while ((textWidth > cellTextSpace) && fontSize>0){
				fontSize--;
				font = new Font(fontName, fontStyle, fontSize);
				fm = j.getFontMetrics(font);
				textWidth = fm.stringWidth(text);
			}
			if (fontSize!=0){
				setFont(font);
				setText(text);
			}
		}
		
		// Save the icon to be painted in the paint function
		ImageIcon icon = (ImageIcon)TBoardConstants.getIcon(map);

		if (icon != null) {
			int textPosition = TBoardConstants.getVerticalTextPosition(map);
			
			int cellWidth = (int)TBoardConstants.getBounds(map).getWidth();
			int cellHeight = (int)TBoardConstants.getBounds(map).getHeight();
			
			int maxImageWidth;
			int maxImageHeight;
			
			// The image will be centered and will fit all the cell		
			
			maxImageWidth = cellWidth - 2 * (borderWidth + HORIZONTAL_ICON_MARGIN);
			maxImageHeight = cellHeight - 2 * (borderWidth + VERTICAL_ICON_MARGIN);
			
			if ((textPosition != SwingConstants.CENTER) && !text.equals("")) {
				// The image will be set with a margin
				setIconTextGap(GAP_ICON_TEXT);

				maxImageWidth = cellWidth - 2
						* (HORIZONTAL_ICON_MARGIN + borderWidth) - GAP_ICON_TEXT;
				maxImageHeight = cellHeight - 2
						* (VERTICAL_ICON_MARGIN + borderWidth) - GAP_ICON_TEXT -
						getFont().getSize();
			}

			if (icon.getIconHeight() > maxImageHeight)
				icon = new ImageIcon(icon.getImage().getScaledInstance(-1,
						maxImageHeight, Image.SCALE_SMOOTH));
			if (icon.getIconWidth() > maxImageWidth)
				icon = new ImageIcon(icon.getImage().getScaledInstance(
						maxImageWidth, -1, Image.SCALE_SMOOTH));
			
		}

		setIcon(icon);
	}
	
protected void installAttributesAndroid(CellView view) {
		
		// Get the cell attributes
		Map map = view.getAllAttributes();

		// Get the variables needed in the paint function
		id = ((TComponent)view.getCell()).getId();

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

		setGradientColor(TBoardConstants.getGradientColor(map));

		// Apply text font properties

		
		
		Font font = TBoardConstants.getFont(map);
		if (font == null)
			font = TBoardConstants.DEFAULTFONT;
		//setFont(TBoardConstants.getFont(map));
		
		//visualizamos la fuente de android
		font=new Font("Droid Sans",font.getStyle(),font.getSize());
		
		setFont(font);
		

		Color foregroundColor = TBoardConstants.getForeground(map);
		setForeground((foregroundColor != null) ? foregroundColor
				: TBoardConstants.DEFAULT_FOREGROUND);

		// Apply text align properties
int textPosition = TBoardConstants.getVerticalTextPosition(map);
if(textPosition==SwingConstants.TOP)
	setVerticalAlignment(SwingConstants.BOTTOM);
else setVerticalAlignment(SwingConstants.TOP);//		setVerticalAlignment(SwingConstants.CENTER);
		setHorizontalAlignment(SwingConstants.CENTER);
		setVerticalTextPosition(TBoardConstants.getVerticalTextPosition(map));
		
		setHorizontalTextPosition(SwingConstants.CENTER);

		// Apply text properties
		String text = TBoardConstants.getText(map);
		if (text == null)
			text = "";
		setText(text);

		
		// Check if the text fits in the cell with the font
		int cellTextSpace = (int)(((float)TBoardConstants.getBounds(map).getWidth()-
							2*borderWidth)*0.9);
		JButton j = new JButton();
		j.setSize(cellTextSpace, cellTextSpace);
		FontMetrics fm = j.getFontMetrics(font);
		int textWidth = fm.stringWidth(text);
		if (textWidth > cellTextSpace){
			int fontSize = font.getSize();
			int fontStyle = font.getStyle();
			String fontName = font.getFontName();
			while ((textWidth > cellTextSpace) && fontSize>0){
				fontSize--;
				font = new Font(fontName, fontStyle, fontSize);
				fm = j.getFontMetrics(font);
				textWidth = fm.stringWidth(text);
			}
			if (fontSize!=0){
				setFont(font);
				setText(text);
			}
		}
		int cellWidth = (int)TBoardConstants.getBounds(map).getWidth();
		int cellHeight = (int)TBoardConstants.getBounds(map).getHeight();
		
		/*if(textPosition==SwingConstants.BOTTOM){
			setIconTextGap(cellHeight-2*borderWidth -fm.getHeight());
		}*/
		
		// Save the icon to be painted in the paint function
		ImageIcon icon = (ImageIcon)TBoardConstants.getIcon(map);

		if (icon != null) {
//			int textPosition = TBoardConstants.getVerticalTextPosition(map);
			
			
			
			int maxImageWidth;
			int maxImageHeight;
			
			// The image will be centered and will fit all the cell		
			
			maxImageWidth = cellWidth - 2 * (borderWidth);
			maxImageHeight = cellHeight - 2 * (borderWidth);
			
//			if ((textPosition != SwingConstants.CENTER) && !text.equals("")) {
				// The image will be set with a margin
				//GAP_ICON_TEXT);

//				maxImageWidth = cellWidth - 2
//						* (HORIZONTAL_ICON_MARGIN + borderWidth) - GAP_ICON_TEXT;
//				maxImageHeight = cellHeight - 2
//						* (VERTICAL_ICON_MARGIN + borderWidth) - GAP_ICON_TEXT -
//						getFont().getSize();
//			}

//			if (icon.getIconHeight() > maxImageHeight)
				icon = new ImageIcon(icon.getImage().getScaledInstance(-1,
						maxImageHeight, Image.SCALE_SMOOTH));
			if (icon.getIconWidth() > maxImageWidth)
				icon = new ImageIcon(icon.getImage().getScaledInstance(
						maxImageWidth, -1, Image.SCALE_SMOOTH));
			
			
			//posicionamos el texto en el margen superior o inferior
			//if(textPosition==SwingConstants.BOTTOM){
				setIconTextGap(cellHeight-2*borderWidth -fm.getHeight()-icon.getIconHeight());
			//}
			
			
		}else{
			if(textPosition==SwingConstants.BOTTOM){
				setVerticalAlignment(SwingConstants.BOTTOM);
			}
		}

		setIcon(icon);
	}
	
	
	
}