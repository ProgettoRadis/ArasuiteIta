/*
 * File: TLabelRenderer.java
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
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.CellView;

import tico.board.TBoardConstants;
import tico.board.TBoardModel;
import tico.editor.TEditor;

/**
 * <code>Swing</code> which implements the <code>TLabelRenderer</code> visualization.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TLabelRenderer extends TComponentRenderer {
	private final static int HORIZONTAL_MARGIN = 10;
	/* (non-Javadoc)
	 * @see org.jgraph.graph.VertexRenderer#installAttributes(org.jgraph.graph.CellView)
	 */
	//Anyadido
//	int textWidth=0;
	
	protected void installAttributes(CellView view) {
		// Get the oval attributes
		Map map = view.getAllAttributes();
		//setSize(30,30);
		
		
		// Apply all the component properties
		/*movemos el apply border*/
		
		// Apply border
		int borderWidth = 0;
		Color borderColor = TBoardConstants.getBorderColor(map);
		if (borderColor != null) {
			borderWidth = Math.max(1, Math.round(TBoardConstants
					.getLineWidth(map)));
			setBorder(BorderFactory.createLineBorder(borderColor, borderWidth));
		} else setBorder(null);
		

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
		//visualizamos la fuente de android si estamos en modo android
		if(TEditor.get_android_mode()){
			font=new Font("Droid Sans",font.getStyle(),font.getSize());
			
		
		}
		setFont(font);
		
		Color foregroundColor = TBoardConstants.getForeground(map);
		setForeground((foregroundColor != null) ? foregroundColor
				: TBoardConstants.DEFAULT_FOREGROUND);

		// Apply text align properties
		setVerticalAlignment(SwingConstants.CENTER);
		setHorizontalAlignment(SwingConstants.CENTER);

		// Apply text properties
		String text = TBoardConstants.getText(map);
		if (text == null)
			text = "";
		setText(text);
		
		
		/*cambio de tamanyo al texto*/
		//System.out.println("cambio tamanyo");
		if(!TEditor.get_android_mode()){
			TBoardConstants.setBounds(map,
				TBoardModel.computeNewBounds(TBoardConstants.getBounds(map),
						TBoardConstants.getText(map),
						TBoardConstants.getFont(map), Math.max(1,
								Math.round(TBoardConstants
										.getLineWidth(map)))));
		}else{//modo android
			font=new Font("Droid Sans",font.getStyle(),font.getSize());
			TBoardConstants.setBounds(map,
					TBoardModel.computeNewBounds(TBoardConstants.getBounds(map),
							TBoardConstants.getText(map),
							font, Math.max(1,
									Math.round(TBoardConstants
											.getLineWidth(map)))));
		}
		
		
		// Check if the text fits in the cell with the font
		/*modificado, ya que no es celda*/
		/*
		int cellTextSpace = (int)TBoardConstants.getBounds(map).getWidth()-
							2*borderWidth-2*HORIZONTAL_MARGIN;
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
		*/
		/*fin modificacion*/
		

	}

	/* (non-Javadoc)
	 * @see java.awt.Component#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		// Draw the gradient
		if (getGradientColor() != null && isOpaque()) {
			setOpaque(false);
			g2.setPaint(new GradientPaint(0, 0, getBackground(), getWidth(),
					getHeight(), getGradientColor(), true));
			g2.fillRect(0, 0,getWidth(), getHeight());
		}

		super.paint(g);
	}
}
