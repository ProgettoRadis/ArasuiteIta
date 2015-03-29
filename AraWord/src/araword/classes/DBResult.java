package araword.classes;

import javax.swing.ImageIcon;
import javax.swing.border.Border;

// A rather dull class to return ImageIcon and color of border associated to element.
public class DBResult {
	private ImageIcon image;
	private Border border;
	public ImageIcon getImage() {
		return image;
	}
	public void setImage(ImageIcon image) {
		this.image = image;
	}
	public Border getBorder() {
		return border;
	}
	public void setBorder(Border border) {
		this.border = border;
	}
}
