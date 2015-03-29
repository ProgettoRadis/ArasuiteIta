// A collection of useful methods to create documents, regenerate the text zone, search on BD, compound/split words and so on.
// May be further split on different modules; it's OK now.
package araword.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.text.DefaultStyledDocument;

import araword.G;
import araword.classes.AWElement;
import araword.db.DBManagement;
import araword.db.DBResult;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class TextUtils {

    public static void newDocument() {
        try {
	        G.elementList.clear();
	        G.textZone.setText("");
	        G.textZone.setStyledDocument(new DefaultStyledDocument());
	        
	        AWElement elem = AWElement.createPictogramElement(0,"AraWord");
	        G.elementList.add(0,elem);
	        
	        
//	        elem = AWElement.createSeparatorElement(1,"  ");
//	        G.elementList.add(1,elem);
//	        elem = AWElement.createPictogramElement(2,"uno");
//	        G.elementList.add(2,elem);
//	        elem = AWElement.createSeparatorElement(3,"  ");
//	        G.elementList.add(3,elem);
//	        elem = AWElement.createPictogramElement(4,"dos");
//	        G.elementList.add(4,elem);
//	        elem = AWElement.createSeparatorElement(5,"  ");
//	        G.elementList.add(5,elem);
//	        elem = AWElement.createPictogramElement(6,"tres");
//	        G.elementList.add(6,elem);
//	        elem = AWElement.createSeparatorElement(7,"  ");
//	        G.elementList.add(7,elem);
//	        elem = AWElement.createPictogramElement(8,"cuatro");
//	        G.elementList.add(8,elem);
//	        elem = AWElement.createSeparatorElement(9,"  ");
//	        G.elementList.add(9,elem);
//	        elem = AWElement.createPictogramElement(10,"cinco");
//	        G.elementList.add(10,elem);
//	        elem = AWElement.createSeparatorElement(11,"  ");
//	        G.elementList.add(11,elem);
//	        elem = AWElement.createPictogramElement(12,"seis");
//	        G.elementList.add(12,elem);
//	        elem = AWElement.createSeparatorElement(13,"  ");
//	        G.elementList.add(13,elem);
//	        elem = AWElement.createPictogramElement(14,"siete");
//	        G.elementList.add(14,elem);
//	        elem = AWElement.createSeparatorElement(15,"  ");
//	        G.elementList.add(15,elem);
//	        elem = AWElement.createPictogramElement(16,"ocho");
//	        G.elementList.add(16,elem);
//	        elem = AWElement.createSeparatorElement(17,"  ");
//	        G.elementList.add(17,elem);
//	        elem = AWElement.createPictogramElement(18,"nueve");
//	        G.elementList.add(18,elem);
//	        elem = AWElement.createSeparatorElement(19,"  ");
//	        G.elementList.add(19,elem);
//	        elem = AWElement.createPictogramElement(20,"diez");
//	        G.elementList.add(20,elem);

	        TextUtils.regenerateTextZone();

	        G.elementList.get(0).getTextField().requestFocusInWindow();
	        G.elementList.get(0).getTextField().setCaretPosition(0);
	        
	        G.activeDocumentFileName = "";
	        
        } catch (Exception exc) {System.out.println(exc);}
    }
    
    // Simple implementation of deep copy.
    public static Object copy(Object orig) {
        Object obj = null;
        try {
            // Write the object out to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(orig);
            out.flush();
            out.close();

            // Make an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream(
                new ByteArrayInputStream(bos.toByteArray()));
            obj = in.readObject();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return obj;
    }
    
    public static void regenerateTextZone() {
        G.textZone.setText("");
        G.textZone.setStyledDocument(new DefaultStyledDocument()); // A bug REALLY difficult to trace... :D
        for (AWElement elem: G.elementList) {
            if (elem.getType()==0) // Pictogram
                G.textZone.insertComponent(elem);
            else try { // Separator
                G.textZone.getStyledDocument().insertString(G.textZone.getStyledDocument().getLength(), elem.getSeparator(), null);
            } catch (Exception exc) {System.out.println(exc);}
        }
    }
    
    //para regenerar la previsualizacion en PDF
   
    public static void regeneratePDFPrevZone(javax.swing.JTextPane prevZone, int page) {
    	//previsualization ration in relation to A4 size
    	
    	
    	int numPage=1;
    	
    	double ratio=2.164;
    	prevZone.setMargin(new java.awt.Insets((int)(10/ratio), (int)(10/ratio), (int)(10/ratio), (int)(10/ratio)));
    	prevZone.setText("");    	
    	prevZone.setStyledDocument(new DefaultStyledDocument()); // A bug REALLY difficult to trace... :D
    	//paper sizes
    	int numPixelsHor = 270;
		int numPixelsVer = 370;	
		double pictoRatio=1.5;
		int numPixelsHorConsumed = 0;
		int numPixelsVerConsumed = G.imagesSize;
		int pxPerTab = (int)((450 / 8)/2.164); // Very rough estimation.
		int pxPerSpace = (int)(10/2.164);
		int picLine=0;
		
		boolean justReadNewLine = false;
		// If two or more \n are present in the text, the first one consumes elem.getHeight() pixels, but the second,
		// third... consume only about 15 pixels.
		int pxPerNewLine = (int)(15/2.164);   
		
    	int numElements=G.elementList.size();
        for (int i=0; i<numElements; i++) {
        	
	 		AWElement elem=G.elementList.get(i);
            if (elem.getType()==0) { // Pictogram 
            	justReadNewLine = false;
            	numPixelsHorConsumed += G.imagesSize;
            	picLine++;
				if ((numPixelsHorConsumed>numPixelsHor) || (picLine>G.numPictosLinePDF)) {
					if (numPage==page)
						try {
							prevZone.getStyledDocument().insertString(prevZone.getStyledDocument().getLength(), "\n", null);
						} catch (Exception exc) {System.out.println(exc);}
					numPixelsVerConsumed += G.imagesSize*pictoRatio;
					numPixelsHorConsumed = G.imagesSize;
					picLine=1;
				}
				if (numPixelsVerConsumed<numPixelsVer) {
	            	//create new element with different size
	            	AWElement newElem = null;
	            	newElem = AWElement.createPDFPrevPictogramElement(i,elem.getTextField().getText(),elem.getNumPictogram(),elem.isVisibleImage(),elem.isVisibleBorder(),elem.isSearchImageOnBD(),elem.getWordSearchBD(), true);
	            	if (numPage==page) prevZone.insertComponent(newElem);
				} else { //new page					
					picLine=0;
					numPixelsHorConsumed = 0;
					numPixelsVerConsumed = G.imagesSize;
					numPage+=1;
					i--;
					
				
				}
            }
            else try { // Separator
            	if (elem.getSeparator().equals("\n")) {
            		if (justReadNewLine) numPixelsVerConsumed += pxPerNewLine;
            			else numPixelsVerConsumed += G.imagesSize*pictoRatio;
	            	justReadNewLine = true;
					numPixelsHorConsumed = 0;
					picLine=0;
					if (numPage==page) prevZone.getStyledDocument().insertString(prevZone.getStyledDocument().getLength(), elem.getSeparator(), null);
					if (numPixelsVerConsumed>numPixelsVer) { //new page
						
					}
				}
            	else if (elem.getSeparator().equals("\t")) {
            		numPixelsHorConsumed += pxPerTab;
            		if (numPage==page) prevZone.getStyledDocument().insertString(prevZone.getStyledDocument().getLength(), elem.getSeparator(), null);
                	if (numPixelsHorConsumed>numPixelsHor) {
						numPixelsVerConsumed += G.imagesSize*pictoRatio;
						numPixelsHorConsumed = pxPerTab;
					} 
                	if (numPixelsVerConsumed>numPixelsVer) { //new page
                	}
                }
            	else if (elem.getSeparator().equals(G.separatorSpace)) {
            		numPixelsHorConsumed+=pxPerSpace;
            		if (numPage==page) prevZone.getStyledDocument().insertString(prevZone.getStyledDocument().getLength(), elem.getSeparator(), null);
                	if (numPixelsHorConsumed>numPixelsHor) {
                		numPixelsVerConsumed += G.imagesSize*pictoRatio;
						numPixelsHorConsumed = pxPerSpace;
						
                	}
                	if (numPixelsVerConsumed>numPixelsVer) { // new page
                		
                	}
            	}
            	
            	//if (elem.getSeparator()!=" ")
            	//	prevZone.getStyledDocument().insertString(prevZone.getStyledDocument().getLength(), elem.getSeparator(), null);
            } catch (Exception exc) {System.out.println(exc);}
        } // end of for elements
        //height consumed
        //System.out.println("WEIDTH CONSUMED="+numPixelsHorConsumed);
        //System.out.println("HEIGHT CONSUMED="+numPixelsVerConsumed);
        
        
    }
    
    public static void regenerateDocument() {
    	// Like regenerateTextZone, but also search again on BD for images. Useful for changing images' size, language...
    	   	
	 	
	 	int numElements=G.elementList.size();
	 	
	 	    	
	 	for (int i=0; i<numElements; i++) {
	 		AWElement elem=G.elementList.get(i);
            if (elem.getType()==0) {
            	//substitute picto with new size  
            	G.elementList.remove(i);
            	AWElement newElem = null;
            	newElem = AWElement.createPictogramElement(i,elem.getTextField().getText(),elem.getNumPictogram(),elem.isVisibleImage(),elem.isVisibleBorder(),elem.isSearchImageOnBD(),elem.getWordSearchBD());
        	 	G.elementList.add(i,newElem);        	 	
            	
            } else {
            	//separator
            	
            }
        } 	
	 	
        TextUtils.regenerateTextZone();
    }
    
    public static String getText() {
    	String text="";
    	
    	int numElements=G.elementList.size();	 	
	    	
	 	for (int i=0; i<numElements; i++) {
	 		AWElement elem=G.elementList.get(i);
            if (elem.getType()==0) {
            	text=text+elem.getTextField().getText();  	 	
            	
            } else {
            	//separator
            	text=text+" ";            	
            }
	 	}
    	
    	System.out.println(text);
    	return text;
    }
    
    

    public static boolean checkCompoundWord () {
        // Checks compound words. Deletes elements and adjust focus and caret, according to active element.
       // Returns true if a compound word was found (and regenerateTextZone invoked accordingly). Useful for speedup.
        try {
           int tmpCaret = G.activeElement.getTextField().getCaretPosition();
            for (int lengthWord = G.maxLengthCompoundWords; lengthWord >= 2; lengthWord--) {
                for (int offsetWord = 0; offsetWord < lengthWord; offsetWord++) {
                    String str = obtainWord(lengthWord,offsetWord);
                    if (str==null) continue; // Frequent case. Near to beginning/end, wrong type or quantity of separators, etc.
                    // We have found a possible word, let's see if it exists on the DB.
                    DBResult dbr = DBManagement.searchOnDB(str,0);
                    if (dbr.getImage()==G.notFound) continue; // Frequent case. Image simply doesn't exist. ("de dientes perro cualquiera").
                    // Found compound word, we must delete elements, adjust focus and caret.
                    int maxPosition = G.activeElementPosition+2*offsetWord;
                    int minPosition = maxPosition-2*(lengthWord-1);
                    int countCaret = 0;
                    boolean endCountCaret = false;
                    for (int cont= minPosition; cont<= maxPosition; cont++) {
                        // Delete all elements. Get new caret.
                        if (cont!=G.activeElementPosition) {
                        	if (!endCountCaret) {
                        		if (G.elementList.get(minPosition).getType()==0)
                        			countCaret+=G.elementList.get(minPosition).getTextField().getText().length();
                        		else
                        			countCaret+=1; // Whitespace
                        	}
                        }
                        else {
                        	endCountCaret = true;
                        	countCaret+=tmpCaret;
                        }
                        G.elementList.remove(minPosition);
                    }
                    G.elementList.add(minPosition,AWElement.createPictogramElement(minPosition,str));
                    // Elements deleted, so decrease counter
                    for (int count=minPosition+1;count<G.elementList.size();count++) {
                        // Decrement position counter, by (maxPosition-minPosition+1). Beware of the not-deleted element!!
                        AWElement tmp = G.elementList.get(count);
                        tmp.setPosition(tmp.getPosition()-(maxPosition-minPosition));
                    }
                    G.elementList.get(minPosition).getTextField().setText(str);
                    G.elementList.get(minPosition).regeneratePictogram();
                    TextUtils.regenerateTextZone();
                    // Adjust focus, caret, compound.
                    G.elementList.get(minPosition).getTextField().requestFocusInWindow();
                    G.elementList.get(minPosition).getTextField().setCaretPosition(countCaret);
                    G.elementList.get(minPosition).setCompound(true);

                    G.activeElement = G.elementList.get(minPosition);
                    G.activeComponent = G.elementList.get(minPosition).getTextField();
                    G.activeElementPosition = minPosition;
                    return true;
                }
            }
            // Compound word not found.
            G.activeElement.regeneratePictogram();
            G.activeElement.getTextField().requestFocusInWindow();
            G.activeElement.getTextField().setCaretPosition(tmpCaret);
            return false;
        }
        catch (Exception exc) {System.out.println(exc);}
        return false; // Compound word not found.
    }

    private static String obtainWord(int lengthWord, int offsetWord) {
        if ((G.activeElementPosition+2*offsetWord)>G.elementList.size()-1) return null; // Too near of end of text.
        if ((G.activeElementPosition+2*offsetWord-2*(lengthWord-1))<0) return null; // Too near of beginning of text.
        // Enough elements, let's see if they are of the right type and quantity.
        for (int cont=0; cont<lengthWord; cont++) {
            if (G.elementList.get(G.activeElementPosition+2*offsetWord-2*cont).getType()!=0) return null;
            if (G.elementList.get(G.activeElementPosition+2*offsetWord-2*cont).isCompound()) return null; // Compound words can't be part of other compound words.
        }
        // Correct type of pictogram elements, let's see if separators are all of the correct kind (only whitespace, no tab or enter allowed).
        for (int cont=0; cont<lengthWord-1; cont++) {
            if (G.elementList.get(G.activeElementPosition+2*offsetWord-1-2*cont).getType()==1) {
                // Separator, should always happen, just for debugging.
                if (!(G.elementList.get(G.activeElementPosition+2*offsetWord-1-2*cont).getSeparator().equals(G.separatorSpace))) return null;
                // Found separator not whitespace.
            }
        }
        // They are correct!! Return the word.
        // The right-most part of the word...
        // StringBuilder is more efficient than String, and should be used in a monothreaded environment like this one.
        StringBuilder strAux = new StringBuilder(G.elementList.get(G.activeElementPosition+2*offsetWord).getTextField().getText());
//        String strAux = G.elementList.get(G.activeElementPosition+2*offsetWord).getTextField().getText();
        for (int cont=1; cont<lengthWord; cont++) {
        	//... and we add words right-to-left.
        	strAux.insert(0," ");
        	strAux.insert(0,G.elementList.get(G.activeElementPosition+2*offsetWord-2*cont).getTextField().getText());
        	// More efficient stringbuilder...
//        	strAux = G.elementList.get(G.activeElementPosition+2*offsetWord-2*cont).getTextField().getText() + " " + strAux;
        }
        return strAux.toString();
//        return strAux;
    }

    public static void splitWord () {
        // Split compound words. Creates elements and adjust focus and caret, according to active element.
        try {
            if (!G.activeElement.isCompound()) return;
             int tmpCaret = G.activeElement.getTextField().getCaretPosition();
             String compoundWord = G.activeElement.getTextField().getText();
             StringTokenizer tokens = new StringTokenizer(compoundWord);
             // Delete old compound word.
             G.elementList.remove(G.activeElementPosition);
             int numElements = 0;
             int numElementsUntilCaret = 0;
             boolean endCountCaret = false;
             while(tokens.hasMoreTokens()) {
                String str = tokens.nextToken();
                // Adds the element.
                G.elementList.add(G.activeElementPosition+numElements,AWElement.createPictogramElement(G.activeElementPosition+numElements,str));
                numElements++;
                if (tmpCaret-str.length()-1<=0) endCountCaret = true;
                if (!endCountCaret) {
                    numElementsUntilCaret++;
                    tmpCaret = tmpCaret - str.length() - 1; // 1 from whitespace.
                }
                if (tokens.hasMoreTokens()) {
                   // Create new whitespace and add it to listElement.
                   G.elementList.add(G.activeElementPosition+numElements,AWElement.createSeparatorElement(G.activeElementPosition+numElements,G.separatorSpace));
                   numElements++;
                   if (!endCountCaret) numElementsUntilCaret++;
                }
             }
             for (int count=G.activeElementPosition+numElements;count<G.elementList.size();count++) {
                 // Increment position of element, by (numElementsLeft+numElementsRight+2)
                 AWElement tmp = G.elementList.get(count);
                 tmp.setPosition(tmp.getPosition()+numElements-1);
             }
            TextUtils.regenerateTextZone();
             // Request focus, and then set caret at the beginning.
             G.elementList.get(G.activeElementPosition+numElementsUntilCaret).getTextField().requestFocusInWindow();
             G.elementList.get(G.activeElementPosition+numElementsUntilCaret).getTextField().setCaretPosition(tmpCaret);
             G.activeElement = G.elementList.get(G.activeElementPosition+numElementsUntilCaret);
             G.activeComponent = G.elementList.get(G.activeElementPosition+numElementsUntilCaret).getTextField();
             G.activeElementPosition = G.activeElementPosition+numElementsUntilCaret;
        }
        catch (Exception exc) {System.out.println(exc);}
    }

    public static void printModel() {
        // Print model (debug)
        for (AWElement elem2: G.elementList) {
            if (elem2.getType()==0) // pictograma normal
                System.out.println("Soy el elemento "+elem2.getPosition()+" y soy de tipo pictograma y mi campo de texto tiene esto: " + elem2.getTextField().getText() + " y mi valor de compuesto es: " + elem2.isCompound());
            else {
                if (elem2.getSeparator().equals("\n"))
                    System.out.println("Soy el elemento "+elem2.getPosition()+" y soy de tipo nueva línea");
                else if(elem2.getSeparator().equals("\t"))
                    System.out.println("Soy el elemento "+elem2.getPosition()+" y soy de tipo tabulador");
                else if(elem2.getSeparator().equals(G.separatorSpace))
                    System.out.println("Soy el elemento "+elem2.getPosition()+" y soy de tipo espacio");
                else
                    System.out.println("Soy el elemento "+elem2.getPosition()+ "y conmigo ha pasado algo muy raro");
            }
        }
        System.out.println("Selection state: "+G.selectionState);
    }
    
    public static void updateUndo() {
    	// Add list to undoList and checks undo levels.
    	G.undoList.add(0,TextUtils.ElementList2String(G.elementList));
    	if (G.undoList.size()>G.maxUndoLevel) {
    		G.undoList.remove(G.maxUndoLevel); // At most, one more of allowed, the last one, index = G.maxUndoLevels
    	}
    }
    
    public static ArrayList<AWElement> String2ElementList(String str) {
    	// Parses a string (i.e. "hola esto es una prueba" into different elements and returns them as an ElementList.
    	// Load data
        String aux = "";
        ArrayList<AWElement> eL = new ArrayList<AWElement>();
        int tempPos = 0;
        for (int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            if ((c==' ') || (c=='\t') || (c=='\n')) { // Separator
                if (!aux.equals("")) { // Create pictogram and then, separator.
                    AWElement newElem = AWElement.createPictogramElement(tempPos,aux);
                    eL.add(tempPos,newElem);
                    tempPos++;
                }
                AWElement newAuxElem = null;
                if (c==' ') newAuxElem = AWElement.createSeparatorElement(tempPos,G.separatorSpace);
                else if(c == '\t') newAuxElem = AWElement.createSeparatorElement(tempPos,"\t");
                else if(c == '\n') newAuxElem = AWElement.createSeparatorElement(tempPos,"\n");
                else System.out.println("Something wrong parsing string, position "+tempPos);
                eL.add(tempPos,newAuxElem);
                tempPos++;
                aux = "";
            }
            else { // Read a letter, append it to aux.
                aux+=c;
            }
        }
        if (!aux.equals("")) {
            // Write last word of string.
            AWElement newElem = AWElement.createPictogramElement(tempPos,aux);
            eL.add(tempPos,newElem);
        }
        return eL;
    }
    
    public static String ElementList2String (ArrayList<AWElement> eL) {
    	// Saves an ElementList in a string with its separators.
    	String str = "";
    	for (AWElement elem2: eL) {
            if (elem2.getType()==0) // pictogram
                str+=elem2.getTextField().getText();
            else {
                if (elem2.getSeparator().equals(G.separatorSpace))
                    str+=" ";
                else if (elem2.getSeparator().equals("\t"))
                	str+="\t";
                else if (elem2.getSeparator().equals("\n"))
                	str+="\n";
            }
        }
    	return str;
    }
    
    public static void concatPDFs(List<InputStream> streamOfPDFFiles, OutputStream outputStream, boolean paginate) {

        Document document = new Document();
        try {
            List<InputStream> pdfs = streamOfPDFFiles;
            List<PdfReader> readers = new ArrayList<PdfReader>();
            int totalPages = 0;
            Iterator<InputStream> iteratorPDFs = pdfs.iterator();

            // Create Readers for the pdfs.
            while (iteratorPDFs.hasNext()) {
                InputStream pdf = iteratorPDFs.next();
                    PdfReader pdfReader = new PdfReader(pdf);
                    readers.add(pdfReader);
                    totalPages += pdfReader.getNumberOfPages();
            }
            // Create a writer for the outputstream
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            	      document.open();
            	      BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            	      PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
            	      // data

            	      PdfImportedPage page;
            	      int currentPageNumber = 0;
            	      int pageOfCurrentReaderPDF = 0;
            	      Iterator<PdfReader> iteratorPDFReader = readers.iterator();

            	      // Loop through the PDF files and add to the output.
            	      while (iteratorPDFReader.hasNext()) {
            	          PdfReader pdfReader = iteratorPDFReader.next();

            	          // Create a new page in the target for each source page.
            	          while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
            	              document.newPage();
            	                  pageOfCurrentReaderPDF++;
            	                  currentPageNumber++;
            	                  page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
            	                  cb.addTemplate(page, 0, 0);

            	                  // Code for pagination.
            	                  if (paginate) {
            	                      cb.beginText();
            	                          cb.setFontAndSize(bf, 9);
            	                          cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "" + currentPageNumber + " / " + totalPages, 520, 5, 0);
            	                          cb.endText();
            	                  }
            	          }
            	          pageOfCurrentReaderPDF = 0;
            	      }
            	      outputStream.flush();
            	      document.close();
            	      outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document.isOpen())
                document.close();
            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    
    
}
