package components;

import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class PictogramTableCellRenderer extends DefaultTableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		setBorder(noFocusBorder);

		JLabel label;
		if (value != null) {
			String fileName = value.toString().substring(
					value.toString().lastIndexOf(File.separator) + 1);
			label = new JLabel();
			try {
				BufferedImage img = ImageIO.read(new File(value.toString()));
				ImageIcon icon = new ImageIcon(
						img.getScaledInstance(70, 70, 70));
				label = new JLabel(fileName, icon, JLabel.CENTER);
				label.setVerticalTextPosition(JLabel.BOTTOM);
				label.setHorizontalTextPosition(JLabel.CENTER);
				if (hasFocus) {
					label.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,
							Color.red));
				}
			} catch (MalformedURLException mfe) {
				mfe.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			label = new JLabel();
			label.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,
					Color.white));
		}
		return label;
	}

}
