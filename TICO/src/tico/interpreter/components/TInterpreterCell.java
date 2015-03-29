/*
 * File: TInterpreterCell.java
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
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import tico.interpreter.TInterpreterConstants;

public class TInterpreterCell extends JButton{
	
	private final static int VERTICAL_ICON_MARGIN = 15;
	
	private final static int HORIZONTAL_ICON_MARGIN = 10;
	// Default distance between the icon and the text
	private final static int GAP_ICON_TEXT = 5;
	
	public Font font;
	public Color textColor;
	public int verticalTextPosition;
	public ImageIcon icon;
	public float borderSize;
	public Color borderColor;
	public Color backgroundColor;
	public Color gradientColor;
	public boolean transparentBackground;
	public boolean transparentBorder;
	public boolean accumulated;
	public String textAreaToSend = null;
	public int timeSending;
	public String textToSend;

	public int altBorderSize;
	public Color altBorderColor = null;
	private ImageIcon alternativeIcon = null;

	public String boardToGo = null;
	public String soundPath = null;
	public String alternativeSoundPath = null; //anyadido sonido alternativo
	public String videoPath = null;
	public String videoURL = null;
	public int xVideo = -1;
	public int yVideo = -1;
	public String command = null;
	
	public Point center = new Point();
        private int synthMode = 0;
    private String voiceName = null;
    private String voiceText = null;
		
	public TInterpreterCell (){
		super();
	}

	public float getBorderSize() {
		return borderSize;
	}

	public void setBorderSize(int borderSize) {
		this.borderSize = borderSize;
	}
	
	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}
	
	public Point getCenter() {
		return center;
	}
        
    public int getSynthMode(){
        return synthMode;
    }
    
    public void setSynthMode(int mode){
        synthMode = mode;
    }

    public String getVoiceName() {
        return voiceName;
    }

    public void setVoiceName(String voiceName) {
        this.voiceName = voiceName;
    }

    public String getVoiceText() {
        return voiceText;
    }

    public void setVoiceText(String voiceText) {
        this.voiceText = voiceText;
    }
        
        

	public void setCenter(Point center) {
		this.center = center;
	}

	public String getCommand(){
		return command;
	}
	
	public boolean isTransparentBorder() {
		return transparentBorder;
	}
	
	public boolean isTransparentBackground() {
		return transparentBackground;
	}
	
	public void setAccumulated(boolean accumulated) {
		this.accumulated = accumulated;
	}
	
	public boolean isAccumulated() {
		return accumulated;
	}
	
	public String getTextAreaToSend() {
		return textAreaToSend;
	}
	
	public void setTextAreaToSend(String textArea) {
		this.textAreaToSend = textArea;
	}
	
	public int getTimeSending() {
		return timeSending;
	}

	public void setTimeSending(int timeSending) {
		this.timeSending = timeSending;
	}

	public String getTextToSend() {
		return textToSend;
	}

	public void setTextToSend(String textToSend) {
		this.textToSend = textToSend;
	}
	
	public int getAlternativeBorderSize() {
		return altBorderSize;
	}
	
	public void setAlternativeBorderSize(int borderSize) {
		this.altBorderSize = borderSize;
	}
	
	public Color getAlternativeBorderColor() {
		return altBorderColor;
	}
	
	public void setAlternativeBorderColor(Color borderColor) {
		this.altBorderColor = borderColor;
	}
	
	public ImageIcon getAlternativeIcon() {
		return alternativeIcon;
	}
	
	public void setAlternativeIcon(ImageIcon icon) {
		this.alternativeIcon = icon;
	}
	
	public ImageIcon getDefaultIcon() {
		return icon;
	}
	
	public void setDefaultIcon(ImageIcon icon) {
		this.icon = icon;
	}

	public String getBoardToGo() {
		return boardToGo;
	}

	public void setBoardToGo(String boardToGo) {
		this.boardToGo = boardToGo;
	}
	
	public String getSoundPath() {
		return soundPath;
	}
	
	
	//anyadido sonido alterantivo
	public String getAlternativeSoundPath() {
		return alternativeSoundPath;
	}
	public void setAlternativeSoundPath(String alternativeSoundPath){
		this.alternativeSoundPath=alternativeSoundPath;
	}
	
	public String getVideoPath() {
		return videoPath;
	}
	
	public String getVideoURL() {
		return videoURL;
	}
	
	public int getXVideo() {
		return xVideo;
	}

	public void setXVideo(int xVideo) {
		this.xVideo = xVideo;
	}
	
	public int getYVideo() {
		return yVideo;
	}

	public void setYVideo(int yVideo) {
		this.yVideo = yVideo;
	}

	
	public TInterpreterCell setAttributes(String id, Rectangle bounds, String text, Font font, Color textColor, 
			int verticalTextPosition, ImageIcon icon, float borderSize, Color borderColor, Color backgroundColor, 
			Color gradientColor, boolean transBackground, boolean transBorder, ImageIcon alternativeIcon){
		
		this.setName(id);
		this.setBounds(bounds);
		this.setText(text);
		this.setFont(font);
		this.setForeground(textColor);
		
		// Apply text align properties
		this.setVerticalAlignment(SwingConstants.CENTER);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setVerticalTextPosition(verticalTextPosition);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
		
		// Check if the text fits in the cell with the font
		int cellTextSpace = (int) (bounds.getWidth()-
							2*borderSize-2*HORIZONTAL_ICON_MARGIN);
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

		if (icon != null) {			
			
			int maxImageWidth;
			int maxImageHeight;
			
			// The image will be centered and will fit all the cell
			maxImageWidth = (int) (bounds.width - 2 * (borderSize + HORIZONTAL_ICON_MARGIN));
			maxImageHeight = (int) (bounds.height - 2 * (borderSize + VERTICAL_ICON_MARGIN));
			
			if ((verticalTextPosition != SwingConstants.CENTER) && !text.equals("")) {
				
				// The image will be set with a margin
				this.setIconTextGap(GAP_ICON_TEXT);

				maxImageWidth = bounds.width - 2
						* (HORIZONTAL_ICON_MARGIN + (int) borderSize) - GAP_ICON_TEXT;
				maxImageHeight = bounds.height - 2
						* (VERTICAL_ICON_MARGIN + (int) borderSize) - GAP_ICON_TEXT -
						font.getSize();
			}

			if (icon.getIconHeight() > maxImageHeight)
				icon = new ImageIcon(icon.getImage().getScaledInstance(-1,
						maxImageHeight, Image.SCALE_SMOOTH));
			if (icon.getIconWidth() > maxImageWidth)
				icon = new ImageIcon(icon.getImage().getScaledInstance(
						maxImageWidth, -1, Image.SCALE_SMOOTH));
			
			this.setIcon(icon);
				
		}	
		
		this.icon = icon;		
		this.borderSize = borderSize;
		this.borderColor = borderColor;
		this.backgroundColor = backgroundColor;
		this.gradientColor = gradientColor;
		this.transparentBackground = transBackground;
		this.transparentBorder = transBorder;
		
		if (transparentBackground){
			this.setFocusPainted(false);
			this.setContentAreaFilled(false);
		}
		else{
			this.setOpaque(true);
			this.setBackground(backgroundColor);
		}

		if (transparentBorder){
			this.setBorderPainted(false);
		}
		else{
			this.setBorder(new LineBorder(borderColor, (int)borderSize));
		}
		
		if (alternativeIcon != null) {
			int textPosition = verticalTextPosition;
			
			int maxAImageWidth;
			int maxAImageHeight;
			
			
			// The image will be centered and will fit all the cell
			maxAImageWidth = bounds.width - 2 * ((int) borderSize + HORIZONTAL_ICON_MARGIN);
			maxAImageHeight = bounds.height - 2 * ((int) borderSize + VERTICAL_ICON_MARGIN);
			
			if ((textPosition != SwingConstants.CENTER) && !text.equals("")) {
				// The image will be set with a margin
				this.setIconTextGap(GAP_ICON_TEXT);

				maxAImageWidth = bounds.width - 2
						* (HORIZONTAL_ICON_MARGIN + (int) borderSize) - GAP_ICON_TEXT;
				maxAImageHeight = bounds.height - 2
						* (VERTICAL_ICON_MARGIN + (int) borderSize) - GAP_ICON_TEXT -
						font.getSize();
			}

			if (alternativeIcon.getIconHeight() > maxAImageHeight)
				alternativeIcon = new ImageIcon(alternativeIcon.getImage().getScaledInstance(-1,
						maxAImageHeight, Image.SCALE_SMOOTH));
			if (alternativeIcon.getIconWidth() > maxAImageWidth)
				alternativeIcon = new ImageIcon(alternativeIcon.getImage().getScaledInstance(
						maxAImageWidth, -1, Image.SCALE_SMOOTH));
			
			this.alternativeIcon = alternativeIcon;
		}
		
		return this;
	}
	
	public TInterpreterCell setActionsAttributes(String soundPath, String videoPath, String videoURL, String command, String voiceName, String voiceText, int synthMode){
		this.soundPath = soundPath;
		this.videoPath = videoPath;
		this.videoURL = videoURL;
		this.command = command;
                this.voiceName = voiceName;
                this.voiceText = voiceText;
                this.synthMode = synthMode;
		return this;
	}
	
	public TInterpreterCell setAttributes2(String id, Rectangle bounds, String texto, Font f, Color textColor, ImageIcon icon){
		
		this.textColor = textColor;
		this.font=f;
		
		this.borderSize = TInterpreterConstants.BORDER_SIZE;
		this.borderColor = TInterpreterConstants.BORDER_COLOR;
		
		this.setName(id);
		this.setForeground(textColor);
		this.setText(texto);
		this.setBounds(bounds);
		this.setBackground(TInterpreterConstants.BACKGROUND_COLOR);
		this.setBorder(new LineBorder(TInterpreterConstants.BORDER_COLOR, TInterpreterConstants.BORDER_SIZE));
		this.setFont(f);
		
		// Apply text align properties
		this.setVerticalAlignment(SwingConstants.CENTER);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setVerticalTextPosition(SwingConstants.BOTTOM);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
		
		// icon
		if (icon != null) {			
			
			int maxImageWidth;
			int maxImageHeight;
				
			// The image will be set with a margin
			this.setIconTextGap(GAP_ICON_TEXT);

			maxImageWidth = bounds.width - 2
						* (HORIZONTAL_ICON_MARGIN + (int) 1) - GAP_ICON_TEXT;
			maxImageHeight = bounds.height - 2
						* (VERTICAL_ICON_MARGIN + (int) 1) - GAP_ICON_TEXT -
						f.getSize();
			

			if (icon.getIconHeight() > maxImageHeight)
				icon = new ImageIcon(icon.getImage().getScaledInstance(-1,
						maxImageHeight, Image.SCALE_SMOOTH));
			if (icon.getIconWidth() > maxImageWidth)
				icon = new ImageIcon(icon.getImage().getScaledInstance(
						maxImageWidth, -1, Image.SCALE_SMOOTH));
			
			
			this.setIcon(icon);
			
		}
		
		this.icon = icon;
		
		return this;
	}
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		// Draw the gradient
		if (!transparentBackground && gradientColor != null) {
			setOpaque(false);
			g2.setPaint(new GradientPaint(0, 0, getBackground(), getWidth(),
					getHeight(), gradientColor, true));
			g2.fillRect(0, 0, getWidth(), getHeight());
		}

		super.paint(g);
	}
}
