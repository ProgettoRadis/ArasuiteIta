// The elements you can put in the textZone, two types: pictogram and separator (" ","\t","\n").
// Also, their constructors with a lot of default options for each one of them.

package araword.classes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import araword.G;
import araword.db.DBManagement;
import araword.db.DBResult;

public class AWElement extends JPanel {

    private static final long serialVersionUID = 1L;
    private int position; // Position in the text zone.
    private int type; // Pictogram 0, separator 1.
    private String separator; // If is separator, what kind?? " ", "\t", "\n".
    private JLabel image; // If is pictogram.
    private String imageName; //to figure out whether it is custom image
    private AWTextField textField;
    private int numPictogram; // If a word has different pictograms to choose from, what one do we show. Default 0.
    private boolean compound; // It's a compound word (e.g "cepillo de dientes").
    private boolean visibleImage;
    private boolean visibleBorder;
    private boolean searchImageOnBD; // Useful for implementing pictogram "niño" with text "José Manuel".
    private String wordSearchBD;
    private String infinitive; //the infinitive form of a verb

    public AWElement() {
        super();
        addMouseListener(G.mouseListener);
        addMouseMotionListener(G.mouseMotionListener);
    }
    
    public static String filter(String text) {
        text = text.replace("!", "");
        text = text.replace("¡", "");
        text = text.replace("?", "");
        text = text.replace("¿", "");
        text = text.replace("(", "");
        text = text.replace(")", "");
        text = text.replace("\"", "");
        text = text.replace(",", "");
        text = text.replace(".", "");
        text = text.replace(";", "");
        text = text.replace(":", "");
        return text;
    }
    
    /********************
    public static AWElement createCustomPictogramElement(int pos, String fileName, int numPictogram,
            boolean visibleImage, boolean visibleBorder, boolean searchOnBD, String wordToSearch) {
        AWElement elem = new AWElement();

        //encontrarlo en la base de datos segun su nombre de fichero
        //para ello yo haria un DBManagement.searchFileonDB que me devuelva el result de la busqueda por nombre de fichero

        //usar esto para hacer la busqueda

         try {

                     String query = "select * from ArawordView where name=\""+fileName+"\" and word=\"euldlmdcnnqa\"";
                     ResultSet rs = DB.getInstance().query(query);
                     while (rs.next()) {
                         String name = rs.getString("name");

                         System.out.println("***** CUSTOM IMAGE "+ name);

                     }
                 }
                 catch (Exception exc) {System.out.println(exc);}


        return elem;
    }
    ******************/
    public static AWElement createPictogramElement(int pos, String text, int numPictogram,
            boolean visibleImage, boolean visibleBorder, boolean searchOnBD, String wordToSearch) {
        AWElement elem = new AWElement();
        
        
        try {

            DBResult dbr = null;
            if (searchOnBD) dbr = DBManagement.searchOnDB(filter(text),numPictogram);
            else dbr = DBManagement.searchOnDB(wordToSearch,0);

            JLabel labelImage = new JLabel("",dbr.getImage(),SwingConstants.CENTER);
            labelImage.setPreferredSize(new Dimension(G.imagesSize,G.imagesSize));
            AWTextField textField = new AWTextField(text);

            elem = new AWElement();
           
            elem.setInfinitive(dbr.getInfinitive());
            elem.setPosition(pos);
            elem.setTextField(textField);
            elem.setImage(labelImage);
            elem.imageName=dbr.getFileImage();
            elem.setNumPictogram(numPictogram);
//            elem.setOpaque(false);
            elem.setBackground(Color.WHITE); // No gray background...
            elem.setFocusable(true);
            elem.setLayout(new BorderLayout(0,1));
            if (G.textBelowPictogram) {
                elem.add(labelImage, BorderLayout.NORTH);
                elem.add(textField, BorderLayout.SOUTH);
            }
            else {
                elem.add(textField, BorderLayout.NORTH);
                elem.add(labelImage, BorderLayout.SOUTH);
            }
            elem.setBorder(dbr.getBorder());
            elem.setMaximumSize(elem.getPreferredSize()); // With this we get text "left-aligned".
            elem.setType(0); // Pictogram.
            elem.setCompound(false);
            elem.setVisibleImage(visibleImage);
            elem.setVisibleBorder(visibleBorder);
            elem.setSearchImageOnBD(searchOnBD);
            if (!searchOnBD) elem.setWordSearchBD(wordToSearch);
        }
        catch (Exception exc) { System.out.println(exc);}
        return elem;
    }
    
    public static AWElement createPDFPrevPictogramElement(int pos, String text, int numPictogram,
            boolean visibleImage, boolean visibleBorder, boolean searchOnBD, String wordToSearch, boolean prev) {
         AWElement elem = new AWElement();
         try {

            DBResult dbr = null;
            if (searchOnBD) dbr = DBManagement.searchOnDB(filter(text),numPictogram);
            else dbr = DBManagement.searchOnDB(wordToSearch,0);

             JLabel labelImage = new JLabel("",dbr.getImage(),SwingConstants.CENTER);
             labelImage.setPreferredSize(new Dimension(G.imagesSize,G.imagesSize));
             AWTextField textField = new AWTextField(text);
             Font PDFFont;
             if (prev) 	 PDFFont= new Font(G.defaultFont.getName(),G.defaultFont.getStyle(),G.defaultFont.getSize()*2/G.numPictosLinePDF);
             else	PDFFont= new Font(G.defaultFont.getName(),G.defaultFont.getStyle(),G.defaultFont.getSize()*4/G.numPictosLinePDF); 
             textField.setFont(PDFFont);
             elem = new AWElement();
             elem.setPosition(pos);
             elem.setTextField(textField);
             elem.setImage(labelImage);
             elem.setNumPictogram(numPictogram);
//             elem.setOpaque(false);
             elem.setBackground(Color.WHITE); // No gray background...
             elem.setFocusable(true);
             elem.setLayout(new BorderLayout(0,1));
             if (G.textBelowPictogram) {
                elem.add(labelImage, BorderLayout.NORTH);
                elem.add(textField, BorderLayout.SOUTH);
             }
             else {
                elem.add(textField, BorderLayout.NORTH);
                elem.add(labelImage, BorderLayout.SOUTH);
             }
             if (!prev) {
                 javax.swing.border.Border PDFBorder = dbr.getBorder();
                 elem.setBorder(PDFBorder);
             }
             elem.setMaximumSize(elem.getPreferredSize()); // With this we get text "left-aligned".
             elem.setType(0); // Pictogram.
             elem.setCompound(false);
             elem.setVisibleImage(visibleImage);
             elem.setVisibleBorder(visibleBorder);
             elem.setSearchImageOnBD(searchOnBD);
             if (!searchOnBD) elem.setWordSearchBD(wordToSearch);
         }
         catch (Exception exc) { System.out.println(exc);}
         return elem;
    }
    
    public static AWElement createPictogramElement(int pos, String text) {
        // Useful less-arguments method.

        return createPictogramElement(pos,text,0,true,true,true,"");
    }
    
    public static AWElement createSeparatorElement(int pos, String separator) {

        AWElement elem = new AWElement();
        elem.setPosition(pos);
        elem.setSeparator(separator);
        elem.setType(1); // Separator.
        elem.setCompound(false);
        elem.setVisibleImage(false);
        elem.setVisibleBorder(false); 
        elem.setSearchImageOnBD(false);
        return elem;
    }
    
    public void regeneratePictogram () {
        // Regenerates a pictogram, including pictogram, textfield and size if needed.

        try {
            DBResult dbr = null;
            if (this.isSearchImageOnBD()) {
                dbr = DBManagement.searchOnDB(filter(this.getTextField().getText()),this.getNumPictogram());
            }
            else {
                dbr = DBManagement.searchOnDB(filter(this.getWordSearchBD()),this.getNumPictogram());
            }
            //is it a verb?
            this.setInfinitive(dbr.getInfinitive());

            if (this.isVisibleImage()) {
                this.getImage().setIcon(dbr.getImage());
                this.imageName=dbr.getFileImage();
            }
            else {
                this.getImage().setIcon(G.notFound);
            }

            if (this.isVisibleBorder()) {
                this.setBorder(dbr.getBorder());
            }
            else {
                this.setBorder(G.borders.get("NO_BORDER"));
            }

            this.setMaximumSize(this.getPreferredSize());
        }
        catch (Exception exc) {
            System.out.println(exc);
        }
    }
    
    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the separator
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * @param separator the separator to set
     */
    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getInfinitive() {
        return infinitive;
    }

    /**
     * @param infinitive the infinitive to set
     */
    public void setInfinitive(String infinitive) {
        this.infinitive = infinitive;
    }

    /**
     * @return the image
     */
    public JLabel getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(JLabel image) {
        this.image = image;
    }

    /**
     * @return the textField
     */
    public AWTextField getTextField() {
        return textField;
    }

    /**
     * @param textField the textField to set
     */
    public void setTextField(AWTextField textField) {
        this.textField = textField;
    }

    /**
     * @return the numPictogram
     */
    public int getNumPictogram() {
        return numPictogram;
    }

    /**
     * @param numPictogram the numPictogram to set
     */
    public void setNumPictogram(int numPictogram) {

        this.numPictogram = numPictogram;
    }

    /**
     * @return the numPictogram
     */
    public String getImageName() {
        return imageName;
    }


    /**
     * @return the compound
     */
    public boolean isCompound() {
        return compound;
    }

    /**
     * @param compound the compound to set
     */
    public void setCompound(boolean compound) {
        this.compound = compound;
    }

    public boolean isVisibleImage() {
        return visibleImage;
    }

    public void setVisibleImage(boolean visibleImage) {
        this.visibleImage = visibleImage;
    }

    public boolean isVisibleBorder() {
        return visibleBorder;
    }

    public void setVisibleBorder(boolean visibleBorder) {
        this.visibleBorder = visibleBorder;
    }

    public void setSearchImageOnBD(boolean searchImageOnBD) {
        this.searchImageOnBD = searchImageOnBD;
    }

    public boolean isSearchImageOnBD() {
        return searchImageOnBD;
    }

    public String getWordSearchBD() {
        return wordSearchBD;
    }

    public void setWordSearchBD(String wordSearchBD) {
        this.wordSearchBD = wordSearchBD;
    }

}
