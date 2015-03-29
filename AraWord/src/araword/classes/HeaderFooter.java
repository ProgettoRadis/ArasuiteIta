package araword.classes;



import araword.G;
import araword.configuration.TLanguage;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

//*************** CREATE PDF ******************************
	/** Inner class to add a header and a footer. */
  public class HeaderFooter extends PdfPageEventHelper {
      /** Alternating phrase for the header. */
      Phrase[] header = new Phrase[2];
      /** Current page number (will be reset for every chapter). */
      int pagenumber=1;

      /**
       * Initialize one of the headers.
       * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
       *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
       */
      @Override
	public void onOpenDocument(PdfWriter writer, Document document) {
          header[0] = new Phrase("Movie history");
          pagenumber = 1;
          System.out.println("ON OPEN PDF");
      }

      

      /**
       * Increase the page number.
       * @see com.itextpdf.text.pdf.PdfPageEventHelper#onStartPage(
       *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
       */
      @Override
	public void onStartPage(PdfWriter writer, Document document) {
          pagenumber++;
          System.out.println("ON Start Page PDF");
 Rectangle rect = writer.getBoxSize("art");
          
          /* header     
          ColumnText.showTextAligned(writer.getDirectContent(),                		
              		com.itextpdf.text.Element.ALIGN_RIGHT, header[0],
                      rect.getRight(), rect.getTop(), 0);
           */  
 		  Font font = new Font();
 		  font.setSize(8);
          if (G.licensePDF) {
	          ColumnText.showTextAligned(writer.getDirectContent(),
	          		com.itextpdf.text.Element.ALIGN_CENTER, new Phrase(String.format(TLanguage.getString("EXPORT_PDF_LICENCIA")),font),
	                  (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);
	          ColumnText.showTextAligned(writer.getDirectContent(),
		          		com.itextpdf.text.Element.ALIGN_CENTER, new Phrase(String.format(TLanguage.getString("EXPORT_PDF_LICENCIA2")),font),
		                  (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 9, 0);
          }
          
          
      }

      /**
       * Adds the header and the footer.
       * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
       *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
       */
      @Override
	public void onEndPage(PdfWriter writer, Document document) {
    	  System.out.println("ON END Page PDF");
         
      }
  }

  
