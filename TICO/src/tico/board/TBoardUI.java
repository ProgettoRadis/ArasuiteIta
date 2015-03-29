/*
 * File: TBoardUI.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: Jul 5, 2006
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.Map;

import javax.swing.ImageIcon;

import org.jgraph.plaf.basic.BasicGraphUI;

import tico.editor.TEditor;

/**
 * The basic L&F for a <code>board</code> data structure.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TBoardUI extends BasicGraphUI {
	// Get TBoardUI board
	protected TBoard getBoard() {
		return (TBoard)graph;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jgraph.plaf.basic.BasicGraphUI#paintBackground(java.awt.Graphics)
	 */
	protected void paintBackground(Graphics g) {
		Map map = ((TBoardModel)getBoard().getModel()).getAttributes();

		Dimension size = TBoardConstants.getSize(map);	
		int width = size.width;
		int height = size.height;

		double s = graph.getScale();
		Graphics2D g2 = (Graphics2D)g;
		AffineTransform tmp = g2.getTransform();
		g2.scale(s, s);

		// Draw board border
		g.setColor(TBoard.DEFAULT_BORDER_COLOR);
		g.drawRect(-1, -1, width + 1, height + 1);

		// Draw background color
		Color backgroundColor = TBoardConstants.getBackground(map);
		Color gradientColor = TBoardConstants.getGradientColor(map);

		if (backgroundColor != null) {
			g.setColor(backgroundColor);
			g.fillRect(0, 0, width, height);

			if (gradientColor != null /*&& !TEditor.get_android_mode()*/) {
				g2.setPaint(new GradientPaint(0, 0, backgroundColor, width,
						height, gradientColor, true));
				g2.fillRect(0, 0, width, height);
			}
		}

		// Draw background image
		ImageIcon image = (ImageIcon)TBoardConstants.getIcon(map);

		if (image != null) {
			int imageWidth = image.getIconWidth();
			int imageHeight = image.getIconHeight();

			switch (TBoardConstants.getImageResizeStyle(map)) {
			case TBoardConstants.IMAGE_FIT:
				image = new ImageIcon(image.getImage().getScaledInstance(width,
						height, Image.SCALE_SMOOTH));
				break;
			case TBoardConstants.IMAGE_SCALE:
				image = new ImageIcon(image.getImage().getScaledInstance(width,
						-1, Image.SCALE_SMOOTH));
				if (image.getIconHeight() > height)
					image = new ImageIcon(image.getImage().getScaledInstance(
							-1, height, Image.SCALE_SMOOTH));
				break;
			case TBoardConstants.IMAGE_CENTER:
				if (imageHeight > height)
					image = new ImageIcon(image.getImage().getScaledInstance(
							-1, height, Image.SCALE_SMOOTH));
				if (imageWidth > width)
					image = new ImageIcon(image.getImage().getScaledInstance(
							width, -1, Image.SCALE_SMOOTH));
				break;
			}

			imageWidth = image.getIconWidth();
			imageHeight = image.getIconHeight();

			image.paintIcon(getBoard(), g, (width - imageWidth) / 2,
					(height - imageHeight) / 2);
		}

		g2.setTransform(tmp);

		super.paintBackground(g);
	}
}
