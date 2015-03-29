/*
 * File: TIGThumbnails.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Patricia M. Jaray, Carolina Palacio (2010)
 * 
 * Date: Feb 20, 2008
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

package tico.imageGallery.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Vector;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import tico.components.TButton;
import tico.components.resources.TFileUtils;
import tico.components.resources.TResourceManager;
import tico.configuration.TLanguage;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import database.DB;

/*
 * This class displays the panel that contains the four images that are part of the
 * result of a search and has buttons so that its possible to see all the images selected.
 * It also displays information about the images.
 */ 
public class TIGThumbnails extends JPanel implements KeyListener, MouseListener {
	
	//Index point to the image selected between the four displayed
	protected int index = 0;
	
	//Number of images displayed on the panel
	private final int IMAGES_DISPLAYED = 4;

	//Number of images in the result vector
	protected int total;
		
	protected String nImagesString;
	
	protected ImageIcon imageSelected;
	
	private TButton leftButton;
	
	private TButton rightButton;
	
	//Indicates if the selection of images is enabled
	private Boolean enabledSelection;
	
	//Contains the path of the image selected
	private String path;
	
	private Vector result;
	
	//Those Strings contain the path of the four displayed images
	//private String pathImage;
	private String pathImage[] = new String [IMAGES_DISPLAYED];
	
	//Those Strings contain the name of the four displayed images
	private String imageName;
		
	JLabel nImagesTextField;
	
	JLabel thumbnail = new JLabel();
	JLabel thumbnails1 = new JLabel();
	JLabel thumbnails2 = new JLabel();
	JLabel thumbnails3 = new JLabel();
	JLabel thumbnails4 = new JLabel();
	
	TIGThumbImage thum = new TIGThumbImage();
	TIGThumbImage thum1 = new TIGThumbImage();
	TIGThumbImage thum2 = new TIGThumbImage();
	TIGThumbImage thum3 = new TIGThumbImage();
	TIGThumbImage thum4 = new TIGThumbImage();
	
	
	TIGThumbImage[] thumbImages = new TIGThumbImage[IMAGES_DISPLAYED];
	//Vector de thumbnails
	JLabel[] thumbnails = new JLabel[IMAGES_DISPLAYED];
	//Vector de booleanos
	Boolean[] containsImage = new Boolean[IMAGES_DISPLAYED];
	
	GridBagConstraints gridBag;
	
	boolean select;
		
	public TIGThumbnails(boolean select){
		
		this.select = select;
		
		addMouseListener(this);
		addKeyListener(this); 
	
	}
	
	/*
	 * Creates the thumbnail panel and all its components
	 */
	public JPanel createThumbnailsPanel(Vector result, Boolean enabled){
		enabledSelection = enabled;
		this.result = result;
		total = result.size();
		String thumbName;
		String pathSrc;
		String pathTh;
		
		gridBag = new GridBagConstraints();
		setLayout(new GridBagLayout());
		
		//Create the arrows to navigate on the list of images
		//Left Button
		leftButton = new TButton(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		leftButton.setIcon(TResourceManager.getImageIcon("move-left-16.png"));
		leftButton.setMargin(new Insets(2, 2, 2, 2));
		leftButton.setToolTipText(TLanguage.getString("TIGThumbnailsDialog.PREVIOUS"));
		leftButton.addMouseListener(new java.awt.event.MouseAdapter(){
			public void mouseClicked(java.awt.event.MouseEvent e){
				index=(index-IMAGES_DISPLAYED)%total;
				if (index<0){
					index = index + total;
				}
				updateThumbnailsPanel(index);
			}
		});
		//Right Button
		rightButton = new TButton(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		rightButton.setIcon(TResourceManager.getImageIcon("move-right-16.png"));
		rightButton.setMargin(new Insets(2, 2, 2, 2));
		rightButton.setToolTipText(TLanguage.getString("TIGThumbnailsDialog.NEXT"));
		rightButton.addMouseListener(new java.awt.event.MouseAdapter(){
			public void mouseClicked(java.awt.event.MouseEvent e){
				index = (index+IMAGES_DISPLAYED)%total;
				updateThumbnailsPanel(index);
			}
		});
		//Create the text that will show how many images are, and what name are being displayed
		if (result.size()==1){
			nImagesString = TLanguage.getString("TIGThumbnailsDialog.ONE_IMAGE_FOUND");
		}else{
			nImagesString = result.size() + " " + TLanguage.getString("TIGThumbnailsDialog.IMAGES_FOUND");
		}
		nImagesTextField = new JLabel(nImagesString);
		
		//Configure the background of the panel
		this.setOpaque(true);
		this.setBackground(Color.WHITE);
		
		this.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
				Color.WHITE, new Color(165, 163, 151))));
		
		//Create every panel that will contain an image
		
			if (result.size()<=IMAGES_DISPLAYED){
				leftButton.setEnabled(false);
				rightButton.setEnabled(false);
			}
			for (int i=0; i<IMAGES_DISPLAYED; i++){
				thumbImages[i]= new TIGThumbImage();
			}
			for (int i=0; i<IMAGES_DISPLAYED; i++){
				if (i<result.size()){
					Vector data = new Vector(2);
					data = (Vector)result.elementAt(i%IMAGES_DISPLAYED);
				
					pathImage[i] = (String)data.elementAt(0);
					//thumbName = pathImage[i].substring(0, pathImage[i].lastIndexOf('.')) + "_th.jpg";
					try{
					
    					pathSrc = DB.getInstance().getImagesPath() + File.separator + pathImage[i];
    		        	 
    		        	pathTh = DB.getInstance().getImagesPath() + File.separator + pathImage[i];
    		        	
    		        	createThumbnail(pathSrc, pathTh);
					
					}catch(Exception e){
                        e.printStackTrace();
                    }
		        	
					thumbnails[i] = thumbImages[i].createImageLabel(pathImage[i]);
					imageName = (String)data.elementAt(1);
					thumbnails[i].setToolTipText(imageName);
					thumbnails[i].setPreferredSize(new Dimension(130,130));

					if (enabledSelection){
						thumbnails[i].addMouseListener(new selectedImageListener(i));
					}
					
					containsImage[i] = true;

				}else{ //No result
					thumbnails[i] = thumbImages[i].createImageLabel(null);
					thumbnails[i].setPreferredSize(new Dimension(125,125));
					thumbnails[i].setText("");
					thumbnails[i].setToolTipText(null);
					thumbnails[i].setPreferredSize(new Dimension(130,130));
					thumbImages[i].updateBorder(Color.WHITE);
					
					containsImage[i] = false;
				}
				
			}
			gridBag.fill = GridBagConstraints.BOTH;
			gridBag.insets = new Insets(10, 10, 10, 10);
			gridBag.gridy = 0;
			for (int i=0; i<IMAGES_DISPLAYED; i++){
				gridBag.gridx = i;
				add(thumbnails[i], gridBag);
			}		
		//}
		gridBag.fill = GridBagConstraints.BOTH;
		gridBag.insets = new Insets(10, 30, 10, 30);
		gridBag.gridx = 0;
		gridBag.gridy = 1;
		add(leftButton, gridBag);
		
		gridBag.fill = GridBagConstraints.CENTER;
		gridBag.insets = new Insets(10, 30, 10, 30);
		gridBag.gridwidth = 2;
		gridBag.gridx = 1;
		gridBag.gridy = 1;
		add(nImagesTextField, gridBag);
		
		gridBag.fill = GridBagConstraints.BOTH;
		gridBag.insets = new Insets(10, 30, 10, 30);
		gridBag.gridx = 3;
		gridBag.gridy = 1;
		add(rightButton, gridBag);
		
		return this;
	}
	
	/*
	 * Update the thumbnails panel after modifying an image in order to delete the previous selection
	 */
	public JPanel updateThumbnailsPanel(){
		path = null;
		return updateThumbnailsPanel(result,index);
	}
	
	public JPanel updateThumbnailsPanel(int first){
		return updateThumbnailsPanel(result,first);
	}
	
	/*
	 * Update the thumbnails panel because of three reasons. First, because the left 
	 * or right arrows have been clicked. Second, a search has been made and third, 
	 * an image has been deleted.
	 */
	public JPanel updateThumbnailsPanel(Vector result, int first){
		this.result = result;
		total = result.size();
		String thumbName;
		String pathSrc;
		String pathTh;
		
		//Update every panel that will contain an image
		
		if (result.size()==0){ //No search results
			for (int i=0; i<IMAGES_DISPLAYED; i++){
				thumbnails[i] = thumbImages[i].createImageLabel(null);
				thumbnails[i].setPreferredSize(new Dimension(125,125));
				thumbnails[i].setText("");
				thumbnails[i].setToolTipText(null);
				thumbnails[i].setPreferredSize(new Dimension(130,130));
				thumbImages[i].updateBorder(Color.WHITE);
				
				containsImage[i] = false;
			}
			leftButton.setEnabled(false);
			rightButton.setEnabled(false);
			
			nImagesString = result.size() + " " + TLanguage.getString("TIGThumbnailsDialog.IMAGES_FOUND");
			nImagesTextField.setText(nImagesString);
			
		}else{ //Results > 0
			if (result.size()==1){
				nImagesString = TLanguage.getString("TIGThumbnailsDialog.ONE_IMAGE_FOUND");
			}else{
				nImagesString = result.size() + " " + TLanguage.getString("TIGThumbnailsDialog.IMAGES_FOUND");
			}
			nImagesTextField.setText(nImagesString);
			
			if (result.size()<=IMAGES_DISPLAYED){ //Results <= IMAGES_DISPLAYED
				for (int i=0; i<IMAGES_DISPLAYED; i++){
					if (i<result.size()){
						Vector data = new Vector(2);
						data = (Vector)result.elementAt(i%IMAGES_DISPLAYED);
					
						pathImage[i] = (String)data.elementAt(0);
						thumbName = pathImage[i];
						try{
    						pathSrc = DB.getInstance().getImagesPath() + File.separator + pathImage[i];
    		        	 
    						pathTh = DB.getInstance().getImagesPath() + File.separator + thumbName;
		        	
    						createThumbnail(pathSrc, pathTh);
						}catch(Exception e){
						    e.printStackTrace();
						}
    						
						thumbnails[i] = thumbImages[i].createImageLabel(thumbName);
						imageName = (String)data.elementAt(1);
						thumbnails[i].setToolTipText(imageName);
						thumbnails[i].setPreferredSize(new Dimension(130,130));
						thumbImages[i].updateBorder(Color.GREEN);
						
						if (enabledSelection){
							thumbnails[i].addMouseListener(new selectedImageListener(i));
						}						

						containsImage[i] = true;

					}else{ //No result
						thumbnails[i] = thumbImages[i].createImageLabel(null);
						thumbnails[i].setPreferredSize(new Dimension(125,125));
						thumbnails[i].setText("");
						thumbnails[i].setToolTipText(null);
						thumbnails[i].setPreferredSize(new Dimension(130,130));
						thumbImages[i].updateBorder(Color.WHITE);
						
						containsImage[i] = false;
					}
				}
				leftButton.setEnabled(false);
				rightButton.setEnabled(false);
				
			}else{  //Results > IMAGES_DISPLAYED
				int i;
				int indexThumb = 0;
				int restartIndex = 0;
				for (i=first; i<(IMAGES_DISPLAYED+first); i++){
					if (i<result.size()){
						Vector data = new Vector(2);
						data = (Vector)result.elementAt(i);
					
						pathImage[indexThumb] = (String)data.elementAt(0);
						thumbName = pathImage[indexThumb];
						
						try{
    						pathSrc = DB.getInstance().getImagesPath() + File.separator + pathImage[indexThumb];
    		        	 
    						pathTh = DB.getInstance().getImagesPath() + File.separator + thumbName;
    		        			        	
    			        	createThumbnail(pathSrc, pathTh);
						}catch(Exception e){
						    e.printStackTrace();
						}
						
						thumbnails[indexThumb] = thumbImages[indexThumb].createImageLabel(thumbName);
						imageName = (String)data.elementAt(1);
						thumbnails[indexThumb].setToolTipText(imageName);
						thumbnails[indexThumb].setPreferredSize(new Dimension(130,130));
						thumbImages[indexThumb].updateBorder(Color.GREEN);
						
						if (enabledSelection){
							thumbnails[indexThumb].addMouseListener(new selectedImageListener(indexThumb));
						}						

						containsImage[indexThumb] = true;
						
					}else{
						Vector data = new Vector(2);
						data = (Vector)result.elementAt(restartIndex);
						
						pathImage[indexThumb] = (String)data.elementAt(0);
						thumbName = pathImage[indexThumb].substring(0, pathImage[indexThumb].lastIndexOf('.')) + "_th.jpg";
						
						pathSrc = System.getProperty("user.dir") + File.separator + "images" + File.separator +
	        	 		pathImage[indexThumb].substring(0,1).toUpperCase() +	File.separator + pathImage[indexThumb];
		        	 
						pathTh = System.getProperty("user.dir") + File.separator + "images" + File.separator +
		        	 	pathImage[indexThumb].substring(0,1).toUpperCase() +	File.separator + thumbName;
		        	
						createThumbnail(pathSrc,pathTh);
						
						thumbnails[indexThumb] = thumbImages[indexThumb].createImageLabel(thumbName);
						imageName = (String)data.elementAt(1);
						thumbnails[indexThumb].setToolTipText(imageName);
						thumbnails[indexThumb].setPreferredSize(new Dimension(130,130));
						thumbImages[indexThumb].updateBorder(Color.GREEN);
						
						if (enabledSelection){
							thumbnails[indexThumb].addMouseListener(new selectedImageListener(indexThumb));
						}						
						
						containsImage[indexThumb] = true;
						restartIndex++;
					}
					indexThumb++;
				}
				leftButton.setEnabled(true);
				rightButton.setEnabled(true);
			}
			
		}

		return this;
	}
	
	/*
	 * Delete the red border of the previous selected image 
	 * and paint a red border around the actual selected image. Update the
	 * information about the actual selected image.
	 */
	public void selectThumbnail(int imageClicked){
		
		//Saves the path of the selected image
		path = pathImage[imageClicked];
		
		//Update border of images
		if (containsImage[imageClicked]){
			for (int i=0; i<IMAGES_DISPLAYED; i++){
				if (i==imageClicked){
					thumbImages[i].updateBorder(Color.RED);
					imageSelected = thumbImages[i].returnIcon();
				}else{
					if (containsImage[i]){
						thumbImages[i].updateBorder(Color.GREEN);
					}else{
						thumbImages[i].updateBorder(Color.WHITE);
					}
				}				
			}
		}
	}
	
	/*
	 * Returns the ImageIcon of the selected image
	 */
	public ImageIcon imageSelected(){
		if (path != null){
			TIGThumbImage image = new TIGThumbImage();
			image.createImageLabel(path);
			return image.returnIcon();
		}
		else return null;
	}
	
	/*
	 * Returns the path of the selected image
	 */
	public String pathImageSelected(){
		return path;
	}
	/*
	 * Returns the text of the indicated thumbnail
	 */
	public String imageClickedText(int imageClicked){
		switch (imageClicked)
		{		
			case 1: return thum1.returnText();
			case 2: return thum2.returnText();
			case 3: return thum3.returnText();
			case 4: return thum4.returnText();
			default: return "";
		}
	}
	
	public JPanel deleteImage(){
		int i=0;
		boolean deleted = false;
		String pathImage;
		Vector<String> image = new Vector<String>(2);
		if (path!=null){
			while (i<result.size() && !deleted){
				image = (Vector<String>) result.elementAt(i);
				pathImage = (String)image.elementAt(0);
				if (pathImage.equals(path)){
					result.removeElementAt(i);
					deleted = true;
				}
				i++;
			}
			path = null;
		}
		updateThumbnailsPanel(result, index);
		return this;
	}
	
	public int getVectorIndex(char letter){
		int i = 0;
		boolean founded = false;
		while ((i < result.size()) && (!founded)){
			Vector data = new Vector(2);
			data = (Vector) result.elementAt(i);
			String name = ((String)data.elementAt(1)).toLowerCase();
			if (letter == 'ñ'){
				String aux = replace(name);
				if ((aux.charAt(0) == 'n') && (aux.length() > 1))
					if ((aux.charAt(1) == 'y'))
						founded = true;
					else i++;
				else i++;
			}else
				if (replace(name).charAt(0) >= letter) 
					founded = true;
				else i++;
		}
		if (!founded)
			return 0;

		if ((i >= result.size()) || (((result.size()-4) <= i)&& (i < result.size()))){
			return (result.size()-4);
		}			
		return i;
	}	
	
	public boolean isFocusable(){
		return true;
	}

	public void mousePressed(java.awt.event.MouseEvent e) {}
	public void mouseReleased(java.awt.event.MouseEvent e) {}
	public void mouseClicked(java.awt.event.MouseEvent e) {}
	public void mouseEntered(java.awt.event. MouseEvent e) {
		this.requestFocus();
	}

	public void mouseExited(java.awt.event.MouseEvent e) {}

	public void keyReleased(java.awt.event.KeyEvent e){
		char letter = e.getKeyChar();
		if ((('a' <= letter) && (letter <= 'z')) || (('0' <= letter) && (letter <= '9')) || (letter == 'ñ')){
			int i = getVectorIndex(letter);
			updateThumbnailsPanel(i);
		}
		if (e.getKeyCode() == KeyEvent.	VK_RIGHT ){
			index = (index+IMAGES_DISPLAYED)%total;
			updateThumbnailsPanel(index);
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT){
			index=(index-IMAGES_DISPLAYED)%total;
			if (index<0){
				index = index + total;
			}
			updateThumbnailsPanel(index);
		}
	}

	public void keyTyped(java.awt.event.KeyEvent e) {}

	public void keyPressed(java.awt.event.KeyEvent e) {} 
	
	private static String replace(String word){
		CharSequence seq_nz = "nz";
		CharSequence seq_ñ = "ñ";
		CharSequence seq_NZ = "NZ";
		CharSequence seq_Ñ = "Ñ";
		String result = word.replace(' ', '_').replace(',', '-').replace('á', 'a').replace('é', 'e').replace('í', 'i').replace('ó', 'o').replace('ú', 'u').
		replace('Á', 'A').replace('É', 'E').replace('Í', 'I').replace('Ó', 'O').replace('Ú', 'U').replace(seq_ñ,seq_nz).replace(seq_Ñ,seq_NZ).toLowerCase();
		return result;
	}
	
	private void createThumbnail(String pathImage, String pathThumbnail){
	    	final int PREVIEW_WIDTH = 125;
	    	final int PREVIEW_HEIGHT = 125;	
	    	
	    	 File srcImage = new File(pathImage);
        	 ImageIcon imageIcon = null;
        	 if (srcImage != null) {			        		 
        		 // Test if need to be loaded with JAI libraries (different
					 // format than JPG, PNG and GIF)
					 if (TFileUtils.isJAIRequired(srcImage)) {
						 // Load it with JAI class
						 RenderedOp src_aux = JAI.create("fileload", srcImage.getAbsolutePath());
						 BufferedImage bufferedImage = src_aux.getAsBufferedImage();
						 imageIcon = new ImageIcon(bufferedImage);				
					 } else {
						 // Create it as usual
						 imageIcon = new ImageIcon(srcImage.getAbsolutePath());
					 }
        	 }
        	 File thum = new File (pathThumbnail);
        	 if (!thum.exists()){
		    	try{
					int thumbWidth = PREVIEW_WIDTH;
					int thumbHeight = PREVIEW_HEIGHT;
					double thumbRatio = (double)thumbWidth / (double)thumbHeight;
					int imageWidth = imageIcon.getIconWidth();
					int imageHeight = imageIcon.getIconHeight();
					double imageRatio = (double)imageWidth / (double)imageHeight;
					if (thumbRatio < imageRatio) {
						thumbHeight = (int)(thumbWidth / imageRatio);
					} else {
						thumbWidth = (int)(thumbHeight * imageRatio);
					}
					BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_RGB);
					Graphics2D graphics2D = thumbImage.createGraphics();
					graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
					graphics2D.drawImage(imageIcon.getImage(), 0, 0, thumbWidth, thumbHeight, null);
					BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(pathThumbnail));
					JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
					JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
					int quality = 100;
					quality = Math.max(0, Math.min(quality, 100));
					param.setQuality((float)quality / 100.0f, false);
					encoder.setJPEGEncodeParam(param);
					encoder.encode(thumbImage);
					out.close(); 
					} catch (Exception ex) {
						System.out.println("Error creating THUMBNAIL");
						System.out.println(ex.toString());   
					}
        	 }
	    }
	
	public int indexImageSelected(){
		int i = 0;
		boolean found = false;
		String pathImage;
		Vector<String> image = new Vector<String>(2);
		if (path!=null){
			while (i<result.size() && !found){
				image = (Vector<String>) result.elementAt(i);
				pathImage = (String)image.elementAt(0);
				if (pathImage.equals(path)){
					found = true;
				}else{
					i++;
				}
			}
		}
		if (!found) i=0;
		return i;
	}
	
	class selectedImageListener implements MouseListener{
		int selected;
		public selectedImageListener(int imageSelected){
			selected = imageSelected;
		}
		public void mouseClicked(MouseEvent arg0) {
			selectThumbnail(selected);
		}
		public void mouseEntered(MouseEvent arg0) {
		}
		public void mouseExited(MouseEvent arg0) {			
		}
		public void mousePressed(MouseEvent arg0) {
		}
		public void mouseReleased(MouseEvent arg0) {
		}
		
	}
}
