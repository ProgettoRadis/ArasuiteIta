package araword.db;

import javax.swing.ImageIcon;
import javax.swing.border.Border;

// A rather dull class to return ImageIcon and color of border associated to element.
public class DBResult {
	private ImageIcon image;
	private Border border;
	private String fileImage;
	private String infinitive;
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
	public String getFileImage() {
		return fileImage;
	}
	public void setFileImage(String fileImage) {
		this.fileImage = fileImage;
	}
	public String getInfinitive() {
		return infinitive;
	}
	public void setInfinitive(String infinitive) {
		this.infinitive = infinitive;
	}
}
