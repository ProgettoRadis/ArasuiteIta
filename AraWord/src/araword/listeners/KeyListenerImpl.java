//A key listener implementation. Has the control over all word processing, including special key characters
//(whitespace, backspace, tab, new line...) and the actions associated with them, quite complex in some cases.

package araword.listeners;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import araword.G;
import araword.classes.AWElement;
import araword.configuration.TLanguage;
import araword.gui.MenuFunctions;
import araword.utils.TextUtils;

public class KeyListenerImpl implements KeyListener {

	private void processSeparator(KeyEvent e) {
		
        try {
            final JTextField jt = (JTextField)G.activeComponent;
            int posCaret = jt.getCaretPosition();
            int textFieldLength = jt.getText().length();

            if (posCaret==0) {
                // At the beginning of the textfield.
                // We must create a separator and insert it BEFORE.
                AWElement newAuxElem = null;
                if (e.getKeyCode()==KeyEvent.VK_SPACE) newAuxElem = AWElement.createSeparatorElement(G.activeElementPosition,G.separatorSpace);
                else if (e.getKeyCode()==KeyEvent.VK_ENTER) newAuxElem = AWElement.createSeparatorElement(G.activeElementPosition,"\n");
                else if (e.getKeyCode()==KeyEvent.VK_TAB) newAuxElem = AWElement.createSeparatorElement(G.activeElementPosition,"\t");
                else System.out.println("Something wrong on processSeparator");
                G.elementList.add(G.activeElementPosition,newAuxElem);
                // Shifts element on there one position to the right.
                for (int count=G.activeElementPosition+1;count<G.elementList.size();count++) {
                    // Increment position of element.
                    AWElement tmp = G.elementList.get(count);
                    tmp.setPosition(tmp.getPosition()+1);
                }
                TextUtils.regenerateTextZone();
                // Request focus, and then set caret at the beginning.
                G.activeElement.getTextField().requestFocusInWindow();
                G.activeElement.getTextField().setCaretPosition(0);
            }
            else if (posCaret==textFieldLength) {
                // At the end of the textfield.
//            	if (!G.activeElement.isSearchImageOnBD()) { // For new feature: could substitute "niño" by "José Antonio"
//					int tmpCaret = jt.getCaretPosition();
//					// Add the character.
//					StringBuilder strB = new StringBuilder(jt.getText());
//					strB.insert(tmpCaret,' ');
//					jt.setText(strB.toString());
//					
//					int tmpPos = G.activeElement.getPosition();
//	                
//					G.activeElement.regeneratePictogram();
//					G.elementList.get(tmpPos).getTextField().requestFocus();
//					G.elementList.get(tmpPos).getTextField().setCaretPosition(tmpCaret+1);
//					
//					G.activeElement = G.elementList.get(tmpPos);
//	                G.activeComponent = G.elementList.get(tmpPos).getTextField();
//	                G.activeElementPosition = tmpPos;
//				}
//            	else {
	                // We must create a separator and a new pictogram.
	                AWElement newAuxElem = null;
	                if (e.getKeyCode()==KeyEvent.VK_SPACE) newAuxElem = AWElement.createSeparatorElement(G.activeElementPosition+1,G.separatorSpace);
	                else if (e.getKeyCode()==KeyEvent.VK_ENTER) newAuxElem = AWElement.createSeparatorElement(G.activeElementPosition+1,"\n");
	                else if (e.getKeyCode()==KeyEvent.VK_TAB) newAuxElem = AWElement.createSeparatorElement(G.activeElementPosition+1,"\t");
	                else System.out.println("Something wrong on processSeparator");
	                G.elementList.add(G.activeElementPosition+1,newAuxElem); // Shifts element on there one position to the right.
	                // Create new element and add it to listElement.
	                AWElement newElem = AWElement.createPictogramElement(G.activeElementPosition+2,"");
	                G.elementList.add(G.activeElementPosition+2,newElem);
	                for (int count=G.activeElementPosition+3;count<G.elementList.size();count++) {
	                    // Increment position of element, by two (separator + pictogram)
	                    AWElement tmp = G.elementList.get(count);
	                    tmp.setPosition(tmp.getPosition()+2);
	                }
	                TextUtils.regenerateTextZone();
	                // Request focus, and then set caret at the beginning.
	                newElem.getTextField().requestFocusInWindow();
	                newElem.getTextField().setCaretPosition(0);
//            	}
            }
            else {
				// A word is split in two: perro(1) --> pe (1) + SEPARATOR (2) + rro (3)
				// We don't search for new compound words if we split a old compound word. Will be too complicated, caret and focus problems, and slow.
				// Anyway, user can use F4 to join and split compound words if necessary (not very often).
				// Exactly the same issue occurs on processBackspace, when joining old compound words, we don't search for new compound words.
				if (!G.activeElement.isCompound()) {
					if ((!G.activeElement.isSearchImageOnBD()) && (e.getKeyCode()==KeyEvent.VK_SPACE)) { // For new feature: could substitute "niño" by "José Antonio"
						int tmpCaret = jt.getCaretPosition();
						// Add the character.
						StringBuilder strB = new StringBuilder(jt.getText());
						strB.insert(tmpCaret,' ');
						jt.setText(strB.toString());
						
						int tmpPos = G.activeElement.getPosition();
	                    
						G.activeElement.regeneratePictogram();
						G.elementList.get(tmpPos).getTextField().requestFocus();
						G.elementList.get(tmpPos).getTextField().setCaretPosition(tmpCaret+1);
						
						G.activeElement = G.elementList.get(tmpPos);
	                    G.activeComponent = G.elementList.get(tmpPos).getTextField();
	                    G.activeElementPosition = tmpPos;
					}
					else {		
						if (G.activeElement.isCompound()) TextUtils.splitWord();
						else TextUtils.checkCompoundWord();
						
						String rightPart = jt.getText().substring(posCaret);
						String leftPart = jt.getText(0,posCaret);
						
						// New feature: Remember last pictogram used for certain word.
						Integer res = (Integer)G.lastPictogramWordAssociation.get(leftPart);
						if (res!=null) G.activeElement.setNumPictogram(res);
						else G.activeElement.setNumPictogram(0); // Resets num pictogram...
						
						G.activeElement.getTextField().setText(leftPart);
						G.activeElement.regeneratePictogram();
						// Create new separator and add it to listElement.
						AWElement newAuxElem = null;
						if (e.getKeyCode()==KeyEvent.VK_SPACE) newAuxElem = AWElement.createSeparatorElement(G.activeElementPosition+1,G.separatorSpace);
						else if (e.getKeyCode()==KeyEvent.VK_ENTER) newAuxElem = AWElement.createSeparatorElement(G.activeElementPosition+1,"\n");
						else if (e.getKeyCode()==KeyEvent.VK_TAB) newAuxElem = AWElement.createSeparatorElement(G.activeElementPosition+1,"\t");
						else System.out.println("Something wrong on processSeparator");
						G.elementList.add(G.activeElementPosition+1,newAuxElem); // Shifts element on there one position to the right.
						// Create new pictogram and add it to listElement.
						AWElement newElem = AWElement.createPictogramElement(G.activeElementPosition+2,rightPart);
						G.elementList.add(G.activeElementPosition+2,newElem);
						for (int count=G.activeElementPosition+3;count<G.elementList.size();count++) {
							// Increment position of element, by two (separator + pictogram)
							AWElement tmp = G.elementList.get(count);
							tmp.setPosition(tmp.getPosition()+2);
						}
						boolean b1 = TextUtils.checkCompoundWord(); // Checks "first word", as it is activeElement.
						// Now, we go to next element, and check again. --> bad case: "balón de fútbolbalón de fútbol", split on TWO compounds...
						// Remember that checkCompoundWord() changes correctly the activeElement.
						if (G.activeElementPosition+2<G.elementList.size()) {
							// There is "second word" (could have been joined with first one, in that case checking is redundant but difficult to avoid)
							G.activeElement = G.elementList.get(G.activeElementPosition+2);
							G.activeComponent = G.activeElement.getTextField();
							G.activeElementPosition += 2;
							G.activeComponent.requestFocusInWindow();
							// New feature: Remember last pictogram used for certain word.
							res = (Integer)G.lastPictogramWordAssociation.get(rightPart);
							if (res!=null) G.activeElement.setNumPictogram(res);
							else G.activeElement.setNumPictogram(0); // Resets num pictogram...
							
							G.activeElement.getTextField().setCaretPosition(0);
							boolean b2 = TextUtils.checkCompoundWord(); // Checks "second word", as it is activeElement.
							if (!(b1 || b2)) {
								TextUtils.regenerateTextZone(); // Two simple words, usual case, we must regenerate text zone (new elements added...)
								G.activeComponent.requestFocusInWindow();
								G.activeElement.getTextField().setCaretPosition(0);
							}
						}
					}
				}
				else { // Compound word!!
					if ((!G.activeElement.isSearchImageOnBD()) && (e.getKeyCode()==KeyEvent.VK_SPACE)) { // For new feature: could substitute "niño" by "José Antonio"
						int tmpCaret = jt.getCaretPosition();
						// Add the character.
						StringBuilder strB = new StringBuilder(jt.getText());
						strB.insert(tmpCaret,' ');
						jt.setText(strB.toString());
						
						int tmpPos = G.activeElement.getPosition();
	                    
						G.activeElement.regeneratePictogram();
						G.elementList.get(tmpPos).getTextField().requestFocus();
						G.elementList.get(tmpPos).getTextField().setCaretPosition(tmpCaret+1);
						
						G.activeElement = G.elementList.get(tmpPos);
	                    G.activeComponent = G.elementList.get(tmpPos).getTextField();
	                    G.activeElementPosition = tmpPos;
					}
					else {
						String rightPart = jt.getText().substring(posCaret);
						String leftPart = jt.getText(0,posCaret);
						// Delete old compound word.
						G.elementList.remove(G.activeElementPosition);
						StringTokenizer tokens = new StringTokenizer(leftPart);
						int numElementsLeft = 0;
						while(tokens.hasMoreTokens()) {
							String str = tokens.nextToken();
							// Adds the element.
							G.elementList.add(G.activeElementPosition+numElementsLeft,AWElement.createPictogramElement(G.activeElementPosition+numElementsLeft,str));
							numElementsLeft++;
							if (tokens.hasMoreTokens()) { // More simple-word pictograms to create...
								// Create new whitespace and add it to listElement.
								G.elementList.add(G.activeElementPosition+numElementsLeft,AWElement.createSeparatorElement(G.activeElementPosition+numElementsLeft,G.separatorSpace));
							}
							else { // Last word, insert separator according to event.
								AWElement newAuxElem = null;
								if (e.getKeyCode()==KeyEvent.VK_SPACE) newAuxElem = AWElement.createSeparatorElement(G.activeElementPosition+numElementsLeft,G.separatorSpace);
								else if (e.getKeyCode()==KeyEvent.VK_ENTER) newAuxElem = AWElement.createSeparatorElement(G.activeElementPosition+numElementsLeft,"\n");
								else if (e.getKeyCode()==KeyEvent.VK_TAB) newAuxElem = AWElement.createSeparatorElement(G.activeElementPosition+numElementsLeft,"\t");
								else System.out.println("Something wrong on processSeparator");
								G.elementList.add(G.activeElementPosition+numElementsLeft,newAuxElem);
							}
							numElementsLeft++;
						}
						tokens = new StringTokenizer(rightPart);
						int numElementsRight = 0;
						while(tokens.hasMoreTokens()) {
							String str = tokens.nextToken();
							// Adds the element.
							G.elementList.add(G.activeElementPosition+numElementsLeft+numElementsRight,AWElement.createPictogramElement(G.activeElementPosition+numElementsLeft+numElementsRight,str));
							numElementsRight++;
							if (tokens.hasMoreTokens()) { // More simple-word pictograms to create...
								// Create new whitespace and add it to listElement.
								G.elementList.add(G.activeElementPosition+numElementsLeft+numElementsRight,AWElement.createSeparatorElement(G.activeElementPosition+numElementsLeft+numElementsRight,G.separatorSpace));
								numElementsRight++;
							}
							else { // Last word, nothing to insert.
							}
						}
						for (int count=G.activeElementPosition+numElementsLeft+numElementsRight;count<G.elementList.size();count++) {
							// Increment position of element, by (numElementsLeft+numElementsRight+2)
							AWElement tmp = G.elementList.get(count);
							tmp.setPosition(tmp.getPosition()+numElementsLeft+numElementsRight-1);
						}
						TextUtils.regenerateTextZone();
						// Request focus, and then set caret at the beginning.
						G.elementList.get(G.activeElementPosition+numElementsLeft).getTextField().requestFocusInWindow();
						G.elementList.get(G.activeElementPosition+numElementsLeft).getTextField().setCaretPosition(0);
					}
				}
            }
            e.consume();
        }
        catch (Exception exc) {System.out.println(exc);}
	}

    private void processBackSpace(KeyEvent e) {
        try {
        	if (!G.activeElement.isSearchImageOnBD()) return;
            final JTextField jt = (JTextField)G.activeComponent;
            if (jt.getCaretPosition()==0) { // Few options...
                if (G.activeElementPosition>0) {
                    G.previousElement = G.elementList.get(G.activeElementPosition-1);
                    if (G.previousElement.getType()==0) { // Pictogram, shouldn't be possible, only for debug.
                        System.out.println("Error on processBackSpace");
                    }
                    else { // It's a separator.
                        if (G.activeElementPosition>1) {
                            G.previous2Element = G.elementList.get(G.activeElementPosition-2);
                            if (G.previous2Element.getType()==0) { // Pictogram, join two textfields and delete separator.
                               if (!G.previous2Element.isCompound() && !G.activeElement.isCompound()) { // Easiest case.
                                   int previous2ElementLength = G.previous2Element.getTextField().getText().length();
                                   String str = G.previous2Element.getTextField().getText()+jt.getText();
                                   // Remove changes indices, shifting elements on the right one position left. Be careful!!
                                   G.elementList.remove(G.activeElementPosition-2);
                                   G.elementList.remove(G.activeElementPosition-2);
                                   G.activeElement = G.elementList.get(G.activeElementPosition-2);
                                    G.activeComponent = G.activeElement.getTextField();
                                    G.activeElementPosition -= 2;
                                   for (int count=G.activeElementPosition;count<G.elementList.size();count++) {
                                       // Decrement position counter, by two.
                                       AWElement tmp = G.elementList.get(count);
                                       tmp.setPosition(tmp.getPosition()-2);
                                   }
                                   G.activeElement.getTextField().setText(str);
	                                // New feature: Remember last pictogram used for certain word.
	           						Integer res = (Integer)G.lastPictogramWordAssociation.get(str);
            						if (res!=null) G.activeElement.setNumPictogram(res);
            						else G.activeElement.setNumPictogram(0); // Resets num pictogram...
                                  G.activeElement.getTextField().requestFocusInWindow();
                                  // Caret to the end of old text.
                                  G.activeElement.getTextField().setCaretPosition(previous2ElementLength);
                                  if (!TextUtils.checkCompoundWord()) {
                                     TextUtils.regenerateTextZone();
                                     G.activeElement.getTextField().requestFocusInWindow();
                                     // Caret to the end of old text.
                                     G.activeElement.getTextField().setCaretPosition(previous2ElementLength);
                                  }                     
                               }
                               else { // Split compounds. Don't check for new compounds (difficult and usually nonsense).
                                    String rightPart = G.activeElement.getTextField().getText();
                                     String leftPart = G.previous2Element.getTextField().getText();
                                     // Delete old compound or simple word, and separator in between.
                                     G.elementList.remove(G.activeElementPosition-2);
                                     G.elementList.remove(G.activeElementPosition-2);
                                     G.elementList.remove(G.activeElementPosition-2);
                                     G.activeElementPosition-=2; // For easier copy & paste.
                                     StringTokenizer tokensLeft = new StringTokenizer(leftPart);
                                     StringTokenizer tokensRight = new StringTokenizer(rightPart);
                                     int numElementsLeft = 0;
                                     int countCaret = 0;
                                     while(tokensLeft.hasMoreTokens()) {
                                        String str = tokensLeft.nextToken();
                                        if (tokensLeft.hasMoreTokens()) {
                                           // Adds the element and the whitespace.
                                           G.elementList.add(G.activeElementPosition+numElementsLeft,AWElement.createPictogramElement(G.activeElementPosition+numElementsLeft,str));
                                           numElementsLeft++;
                                           G.elementList.add(G.activeElementPosition+numElementsLeft,AWElement.createSeparatorElement(G.activeElementPosition+numElementsLeft,G.separatorSpace));
                                           numElementsLeft++;
                                        }
                                        else { // Last word, insert element joining last word of tokensLeft and first word of tokensRight.
                                           countCaret = str.length();
                                           G.elementList.add(G.activeElementPosition+numElementsLeft,AWElement.createPictogramElement(G.activeElementPosition+numElementsLeft,str+tokensRight.nextToken()));
                                           numElementsLeft++;
                                        }
                                     }
                                     int numElementsRight = 0;
                                     while(tokensRight.hasMoreTokens()) {
                                        // Have already consumed one token, now separator and element.
                                        G.elementList.add(G.activeElementPosition+numElementsLeft+numElementsRight,AWElement.createSeparatorElement(G.activeElementPosition+numElementsLeft+numElementsRight,G.separatorSpace));
                                        numElementsRight++;
                                        String str = tokensRight.nextToken();
                                        // Adds the element.
                                        G.elementList.add(G.activeElementPosition+numElementsLeft+numElementsRight,AWElement.createPictogramElement(G.activeElementPosition+numElementsLeft+numElementsRight,str));
                                        numElementsRight++;
                                     }
                                     for (int count=G.activeElementPosition+numElementsLeft+numElementsRight;count<G.elementList.size();count++) {
                                         // Increment position of element, by (numElementsLeft+numElementsRight-3)
                                         AWElement tmp = G.elementList.get(count);
                                         tmp.setPosition(tmp.getPosition()+numElementsLeft+numElementsRight-3);
                                     }
                                    TextUtils.regenerateTextZone();
                                     // Request focus, and then set caret at the beginning.
                                     G.elementList.get(G.activeElementPosition+numElementsLeft-1).getTextField().requestFocusInWindow();
                                     G.elementList.get(G.activeElementPosition+numElementsLeft-1).getTextField().setCaretPosition(countCaret);
                               }
                            }
                            else { // More than one separator in between, we delete the last one and nothing more.
                                for (int count=G.activeElementPosition;count<G.elementList.size();count++) {
                                    // Decrement position counter.
                                    AWElement tmp = G.elementList.get(count);
                                    tmp.setPosition(tmp.getPosition()-1);
                                }
                                G.elementList.remove(G.activeElementPosition-1); // Separator
                                TextUtils.regenerateTextZone();
                                G.activeElement.getTextField().requestFocusInWindow();
                                G.activeElement.getTextField().setCaretPosition(0);
                            }
                        }
                        else {
                            // G.activeElement is second element and previous one is a separator (must be!!)
                            // We remove that separator.
                            for (int count=G.activeElementPosition;count<G.elementList.size();count++) {
                                // Decrement position counter.
                                AWElement tmp = G.elementList.get(count);
                                tmp.setPosition(tmp.getPosition()-1);
                            }
                            G.elementList.remove(G.activeElementPosition-1); // Separator
                            TextUtils.regenerateTextZone();
                            G.activeElement.getTextField().requestFocusInWindow();
                            G.activeElement.getTextField().setCaretPosition(0);
                        }
                    }
                }
                else {
                    // Nothing to do, it's first element and caret 0 (the beginning of text). Ignore it.
                }
            }
            else { // Just delete one character and regenerate element.
                int posCaret = jt.getCaretPosition();
                String rightPart = jt.getText().substring(posCaret);
                String leftPart = jt.getText(0,posCaret-1);
                int activeElementCaret = G.activeElement.getTextField().getCaretPosition();
                G.activeElement.getTextField().setText(leftPart+rightPart);
//                G.activeElement.getTextField().setCaretPosition(activeElementCaret-1);
                
                if (G.activeElement.isSearchImageOnBD()) {
					// New feature: Remember last pictogram used for certain word.
					Integer res = (Integer)G.lastPictogramWordAssociation.get(leftPart+rightPart);
					if (res!=null) G.activeElement.setNumPictogram(res);
					else G.activeElement.setNumPictogram(0); // Resets num pictogram...
					
					if (G.activeElement.isCompound()) TextUtils.splitWord();
					else TextUtils.checkCompoundWord();
				}
				else G.activeElement.regeneratePictogram();
				jt.requestFocus();
				jt.setCaretPosition(activeElementCaret-1);
            }
            e.consume();
        }
        catch (Exception exc) {System.out.println(exc);}
    }

    private void processLeftArrow(){
        JTextField jt = (JTextField)G.activeComponent;
        if ((jt.getCaretPosition()==0) && (G.activeElementPosition>1)) {
            // Only if G.activeElement >2, we search where we do have to go (previous textfield)
            int tmpPos = G.activeElementPosition-2;
            while (true) {
                if (G.elementList.get(tmpPos).getType()==0) { // Found pictogram!!
                    JTextField textFieldPreviousElement = G.elementList.get(tmpPos).getTextField();
                    textFieldPreviousElement.requestFocusInWindow();
                    textFieldPreviousElement.setCaretPosition(textFieldPreviousElement.getText().length());
                    break;
                }
                tmpPos--;
                if (tmpPos<0){
                    break;
                }
            }
        }
    }

    private void processRightArrow() {
        JTextField jt = (JTextField)G.activeComponent;
        if ((jt.getCaretPosition()==jt.getText().length()) && (G.activeElementPosition<(G.elementList.size()-2))) {
            // Only if G.activeElement <size()-2, we search where we do have to go (next textfield)
            int tmpPos = G.activeElementPosition+2;
            while (true) {
                if (G.elementList.get(tmpPos).getType()==0) { // Found pictogram!!
                    JTextField textFieldNextElement = G.elementList.get(tmpPos).getTextField();
                    textFieldNextElement.requestFocusInWindow();
                    textFieldNextElement.setCaretPosition(0);
                    break;
                }
                tmpPos++;
                if (tmpPos>=G.elementList.size()){
                    break;
                }
            }
        }
    }

    private void processSuprKey(final KeyEvent e) {
       // Quite identical to processBackspace
    	if (!G.activeElement.isSearchImageOnBD()) return;
        final JTextField jt = (JTextField)G.activeComponent;
        if (jt.getCaretPosition()==jt.getText().length()) {
            if (G.activeElementPosition<(G.elementList.size()-1)) {
                G.nextElement = G.elementList.get(G.activeElementPosition+1);
                if (G.nextElement.getType()==0) { // Pictogram, should never happen.
                    System.out.println("Problem with processSuprKey");
                }
                else {
                    G.next2Element = G.elementList.get(G.activeElementPosition+2);
                    if (G.next2Element.getType()==0) { // Pictogram, join two textfields and delete separator.
                       if (!G.next2Element.isCompound() && !G.activeElement.isCompound()) { // Easiest case.
                           int activeElementLength = G.activeElement.getTextField().getText().length();
                           String str = jt.getText()+G.next2Element.getTextField().getText();
                           // Remove changes indices, shifting elements on the right one position left. Be careful!!
                           G.elementList.remove(G.activeElementPosition+1); // Separador
                           G.elementList.remove(G.activeElementPosition+1);
                           for (int count=G.activeElementPosition+1;count<G.elementList.size();count++) {
                               // Decrement position counter, by two.
                               AWElement tmp = G.elementList.get(count);
                               tmp.setPosition(tmp.getPosition()-2);
                           }
                           G.activeElement.getTextField().setText(str);
	                        // New feature: Remember last pictogram used for certain word.
	   						Integer res = (Integer)G.lastPictogramWordAssociation.get(str);
    						if (res!=null) G.activeElement.setNumPictogram(res);
    						else G.activeElement.setNumPictogram(0); // Resets num pictogram...
                          G.activeElement.getTextField().requestFocusInWindow();
                          // Caret to the end of old text.
                          G.activeElement.getTextField().setCaretPosition(activeElementLength);
                          if (!TextUtils.checkCompoundWord()) {
                             TextUtils.regenerateTextZone();
                             G.activeElement.getTextField().requestFocusInWindow();
                             // Caret to the end of old text.
                             G.activeElement.getTextField().setCaretPosition(activeElementLength);
                          }
                      }
                      else { // Split compounds.
                           String rightPart = G.next2Element.getTextField().getText();
                            String leftPart = G.activeElement.getTextField().getText();
                            // Delete old compound or simple word, and separator in between.
                            G.elementList.remove(G.activeElementPosition);
                            G.elementList.remove(G.activeElementPosition);
                            G.elementList.remove(G.activeElementPosition);
                            StringTokenizer tokensLeft = new StringTokenizer(leftPart);
                            StringTokenizer tokensRight = new StringTokenizer(rightPart);
                            int numElementsLeft = 0;
                            int countCaret = 0;
                            while(tokensLeft.hasMoreTokens()) {
                               String str = tokensLeft.nextToken();
                               if (tokensLeft.hasMoreTokens()) {
                                  // Adds the element and the whitespace.
                                  G.elementList.add(G.activeElementPosition+numElementsLeft,AWElement.createPictogramElement(G.activeElementPosition+numElementsLeft,str));
                                  numElementsLeft++;
                                  G.elementList.add(G.activeElementPosition+numElementsLeft,AWElement.createSeparatorElement(G.activeElementPosition+numElementsLeft,G.separatorSpace));
                                  numElementsLeft++;
                               }
                               else { // Last word, insert element joining last word of tokensLeft and first word of tokensRight.
                                  countCaret = str.length();
                                  G.elementList.add(G.activeElementPosition+numElementsLeft,AWElement.createPictogramElement(G.activeElementPosition+numElementsLeft,str+tokensRight.nextToken()));
                                  numElementsLeft++;
                               }
                            }
                            int numElementsRight = 0;
                            while(tokensRight.hasMoreTokens()) {
                               // Have already consumed one token, now separator and element.
                               G.elementList.add(G.activeElementPosition+numElementsLeft+numElementsRight,AWElement.createSeparatorElement(G.activeElementPosition+numElementsLeft+numElementsRight,G.separatorSpace));
                               numElementsRight++;
                               String str = tokensRight.nextToken();
                               // Adds the element.
                               G.elementList.add(G.activeElementPosition+numElementsLeft+numElementsRight,AWElement.createPictogramElement(G.activeElementPosition+numElementsLeft+numElementsRight,str));
                               numElementsRight++;
                            }
                            for (int count=G.activeElementPosition+numElementsLeft+numElementsRight;count<G.elementList.size();count++) {
                                // Increment position of element, by (numElementsLeft+numElementsRight-3)
                                AWElement tmp = G.elementList.get(count);
                                tmp.setPosition(tmp.getPosition()+numElementsLeft+numElementsRight-3);
                            }
                           TextUtils.regenerateTextZone();
                            // Request focus, and then set caret at the beginning.
                            G.elementList.get(G.activeElementPosition+numElementsLeft-1).getTextField().requestFocusInWindow();
                            G.elementList.get(G.activeElementPosition+numElementsLeft-1).getTextField().setCaretPosition(countCaret);
                      }
                   }
                    else {
                        // More than one separator in between, we delete the last one and nothing more.
                        for (int count=G.activeElementPosition+2;count<G.elementList.size();count++) {
                            // Decrement position counter.
                            AWElement tmp = G.elementList.get(count);
                            tmp.setPosition(tmp.getPosition()-1);
                        }
                        G.elementList.remove(G.activeElementPosition+1); // Separador
                        TextUtils.regenerateTextZone();
                        G.activeElement.getTextField().requestFocusInWindow();
                        G.activeElement.getTextField().setCaretPosition(G.activeElement.getTextField().getText().length()); // Al final del text.
                    }
                }
            }
            else { // Nothing, Supr on caret last at last element.
            }
        }
        else {
            // Just delete one character and regenerate element.
            try {
                int posCaret = jt.getCaretPosition();
                String rightPart = jt.getText().substring(posCaret+1);
                String leftPart = jt.getText(0,posCaret);
                int activeElementCaret = G.activeElement.getTextField().getCaretPosition();
                G.activeElement.getTextField().setText(leftPart+rightPart);
                G.activeElement.getTextField().setCaretPosition(activeElementCaret);
                
                if (G.activeElement.isSearchImageOnBD()) {
					// New feature: Remember last pictogram used for certain word.
					Integer res = (Integer)G.lastPictogramWordAssociation.get(leftPart+rightPart);
					if (res!=null) G.activeElement.setNumPictogram(res);
					else G.activeElement.setNumPictogram(0); // Resets num pictogram...
					
					if (G.activeElement.isCompound()) TextUtils.splitWord();
					else TextUtils.checkCompoundWord();
				}
				else G.activeElement.regeneratePictogram();
				jt.requestFocus();
				jt.setCaretPosition(activeElementCaret);
            }
            catch (Exception exc) { System.out.println(exc); }
        }
        e.consume();
    }

    public void keyTyped(KeyEvent e) {
//        System.out.println("keyTyped fired");
        // Normal typewriting: 'a', 'P', and so on.
        char car = e.getKeyChar();
        int tmp = car;
//        System.out.println("Caracter: "+tmp);
        if ((tmp==24)||(tmp==3)||(tmp==22)||(tmp==26)||(tmp==25)) {
        	if (tmp==24) {
        		// Cut action (weird character Ctrl+{X,C,V}.
        		MenuFunctions.editCut();
        	}
        	else if (tmp==3) { // Copy action.
        		MenuFunctions.editCopy();
        	}
        	else if (tmp==22) { // Paste action.
        		TextUtils.updateUndo();
        		int pastePos = ((AWElement)(e.getComponent().getParent())).getPosition()+1;
        		MenuFunctions.editPaste(pastePos);
        	}
        	else if (tmp==26) { // Undo action.
        		MenuFunctions.editUndo();
        	}
        	else if (tmp==25) { // Redo action.
        		MenuFunctions.editRedo();
        	}
        }
        else if ((tmp<33) || (tmp==127)) {}
        // Ignores backspace, enter, tab, escape, whitespace, Supr, and other non-printable problematic chars.
        // also ignores separators
        else {
        	G.wereDrag = false;
    		G.selectionState = 0;
			try {
//				System.out.println("keyTyped handled");            	
				// Normal character handling.
//				TextUtils.updateUndo(); // Slows too much program.
				JTextField jt = (JTextField)G.activeComponent;
				int tmpCaret = jt.getCaretPosition();
				// Add the character.
				StringBuilder strB = new StringBuilder(jt.getText());
				strB.insert(tmpCaret,car);
				jt.setText(strB.toString());
//				jt.setCaretPosition(tmpCaret+1);
				
				if (G.activeElement.isSearchImageOnBD()) {
					
					// New feature: Remember last pictogram used for certain word.
					Integer res = (Integer)G.lastPictogramWordAssociation.get(araword.classes.AWElement.filter(jt.getText()));
					if (res!=null) G.activeElement.setNumPictogram(res);
					else {
						
						G.activeElement.setNumPictogram(0); // Resets num pictogram...
					}
					
					if (G.activeElement.isCompound()) TextUtils.splitWord();
					else TextUtils.checkCompoundWord();
				}
				else G.activeElement.regeneratePictogram();
				jt.requestFocus();
				jt.setCaretPosition(tmpCaret+1);
			}
			catch (Exception exc) {System.out.println(exc);}
        }
        e.consume();
//        TextUtils.printModel();
    }

    public void keyPressed(KeyEvent e) {
        // Process space, backspace, arrow keys, enter, Supr and another non-letter keys.
//        System.out.println("keyPressed fired");
        if ((e.getKeyCode()==KeyEvent.VK_SPACE) ||
            (e.getKeyCode()==KeyEvent.VK_BACK_SPACE) ||
            (e.getKeyCode()==KeyEvent.VK_LEFT) ||
            (e.getKeyCode()==KeyEvent.VK_RIGHT) ||
            (e.getKeyCode()==KeyEvent.VK_ENTER) ||
            (e.getKeyCode()==KeyEvent.VK_DELETE) ||
            (e.getKeyCode()==KeyEvent.VK_TAB) ) {
//            System.out.println("keyPressed handled");
            try {
                if ((e.getKeyCode()==KeyEvent.VK_SPACE) ||
                    (e.getKeyCode()==KeyEvent.VK_ENTER) ||
                    (e.getKeyCode()==KeyEvent.VK_TAB)) {
                	TextUtils.updateUndo();
                	processSeparator(e);
                }
                else if (e.getKeyCode()==KeyEvent.VK_BACK_SPACE) {
                	TextUtils.updateUndo();
                	if (G.selectionState==2) {
                		// Delete all elements of selection.
                		// Don't allow cut of the whole text (unable to paste it again somewhere,
            			// quite problematic).
            			boolean fullSelection = true;
            			for (int i=0;i<G.indexSelectionFrom;i++) {
            				if (G.elementList.get(i).getType()==0) {
            					fullSelection = false;
            					break;
            				}
            			}
            			if (fullSelection) {
            				for (int i=G.elementList.size()-1;i>G.indexSelectionTo;i--) {
            					if (G.elementList.get(i).getType()==0) {
            						fullSelection = false;
            						break;
            					}
            				}
            			}
            			if (fullSelection) {
            	            JOptionPane.showMessageDialog(null,TLanguage.getString("UNABLE_DELETE_ALL_TEXT"),
            	            		TLanguage.getString("WARNING"),JOptionPane.INFORMATION_MESSAGE);
            			}
            			else {
            				for (int i=G.indexSelectionFrom; i<=G.indexSelectionTo; i++) {
            					G.elementList.get(i).setBackground(Color.WHITE);
            				}
            				TextUtils.updateUndo();
            				G.selectionState = 0;
            				if (G.indexSelectionTo==G.elementList.size()-1) { // Until last element.
            					for (int i=G.indexSelectionFrom; i<=G.indexSelectionTo; i++) { // Remove from main list.
            						G.elementList.remove(G.indexSelectionFrom);
            					}
            				}
            				else {
            					for (int i=G.indexSelectionFrom; i<=G.indexSelectionTo+1; i++) { // Remove from main list, also next separator.
            						G.elementList.remove(G.indexSelectionFrom);
            					}
            				}
            				for (int count=G.indexSelectionFrom;count<G.elementList.size();count++) { // Adjust positions.
                                AWElement tmpE = G.elementList.get(count);
                                tmpE.setPosition(count);
                            }
            				TextUtils.regenerateTextZone();
            				if (!(G.activeElement.getTextField().requestFocusInWindow())) {
            					// If we delete "active element", transfer focus to first element visible in the textZone.
            					Component[] myComps = G.textZone.getComponents();
            					Component realComp = ((Container)myComps[0]).getComponent(0);
            					((AWElement)realComp).getTextField().requestFocusInWindow();
            				}
            			}
                		e.consume();
                	}
                	else processBackSpace(e);
                }
                else if (e.getKeyCode()==KeyEvent.VK_LEFT) {
                    processLeftArrow();
                }
                else if (e.getKeyCode()==KeyEvent.VK_RIGHT) {
                    processRightArrow();
                }
                else if (e.getKeyCode()==KeyEvent.VK_DELETE) {
                	TextUtils.updateUndo();
                	if (G.selectionState==2) {
                		// Delete all elements of selection.
                		// Don't allow cut of the whole text (unable to paste it again somewhere,
            			// quite problematic).
            			boolean fullSelection = true;
            			for (int i=0;i<G.indexSelectionFrom;i++) {
            				if (G.elementList.get(i).getType()==0) {
            					fullSelection = false;
            					break;
            				}
            			}
            			if (fullSelection) {
            				for (int i=G.elementList.size()-1;i>G.indexSelectionTo;i--) {
            					if (G.elementList.get(i).getType()==0) {
            						fullSelection = false;
            						break;
            					}
            				}
            			}
            			if (fullSelection) {
            	            JOptionPane.showMessageDialog(null,TLanguage.getString("UNABLE_DELETE_ALL_TEXT"),
            	            		TLanguage.getString("WARNING"),JOptionPane.INFORMATION_MESSAGE);
            			}
            			else {
            				for (int i=G.indexSelectionFrom; i<=G.indexSelectionTo; i++) {
            					G.elementList.get(i).setBackground(Color.WHITE);
            				}
            				TextUtils.updateUndo();
            				G.selectionState = 0;
            				if (G.indexSelectionTo==G.elementList.size()-1) { // Until last element.
            					for (int i=G.indexSelectionFrom; i<=G.indexSelectionTo; i++) { // Remove from main list.
            						G.elementList.remove(G.indexSelectionFrom);
            					}
            				}
            				else {
            					for (int i=G.indexSelectionFrom; i<=G.indexSelectionTo+1; i++) { // Remove from main list, also next separator.
            						G.elementList.remove(G.indexSelectionFrom);
            					}
            				}
            				for (int count=G.indexSelectionFrom;count<G.elementList.size();count++) { // Adjust positions.
                                AWElement tmpE = G.elementList.get(count);
                                tmpE.setPosition(count);
                            }
            				TextUtils.regenerateTextZone();
            				if (!(G.activeElement.getTextField().requestFocusInWindow())) {
            					// If we delete "active element", transfer focus to first element visible in the textZone.
            					Component[] myComps = G.textZone.getComponents();
            					Component realComp = ((Container)myComps[0]).getComponent(0);
            					((AWElement)realComp).getTextField().requestFocusInWindow();
            				}
            			}
                		e.consume();
                	}
                	else processSuprKey(e);
                }
                G.wereDrag = false;
        		G.selectionState = 0;
//                TextUtils.printModel();
            }
            catch (Exception exc) { System.out.println(exc);}
        }
    }

    public void keyReleased(KeyEvent e) {
    	
    }
}
