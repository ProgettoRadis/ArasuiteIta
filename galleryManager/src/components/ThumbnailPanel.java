package components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class ThumbnailPanel extends JPanel implements PropertyChangeListener {
    private JFileChooser jfc;
    private Image img;

    public ThumbnailPanel(JFileChooser jfc) {
        this.jfc = jfc;
        Dimension sz = new Dimension(200, 200);
        setPreferredSize(sz);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        try {
            File file = jfc.getSelectedFile();
            updateImage(file);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void updateImage(File file) throws IOException {
        if (file == null) {
            return;
        }

        if (!file.getName().endsWith("png") && !file.getName().endsWith("bmp") && !file.getName().endsWith("jpg")
                && !file.getName().endsWith("gif")) {
            return;
        }

        img = ImageIO.read(file);
        repaint();
    }

    public void paintComponent(Graphics g) {
        // fill the background
        g.setColor(Color.gray);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (img != null) {
            // calculate the scaling factor
            int w = img.getWidth(null);
            int h = img.getHeight(null);
            int side = Math.max(w, h);
            double scale = 200.0 / (double) side;
            w = (int) (scale * (double) w);
            h = (int) (scale * (double) h);

            // draw the image
            g.drawImage(img, 0, 0, w, h, null);

            // draw the image dimensions
            String dim = w + " x " + h;
            g.setColor(Color.black);
            g.drawString(dim, 31, 196);
            g.setColor(Color.white);
            g.drawString(dim, 30, 195);

        } else {

            // print a message
            g.setColor(Color.black);
            g.drawString("Not an image", 30, 100);
        }
    }
}