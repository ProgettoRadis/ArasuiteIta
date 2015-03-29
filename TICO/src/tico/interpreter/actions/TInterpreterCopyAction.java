    /*
 * File: TInterpreterReadAction.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * * Authors: Rodrigo Perez Fulloni
 * 
 * Date:	Aug-2012
 * 
 * Company: Fundacion Teleton Uruguay, Dpto. Ingenieria
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
package tico.interpreter.actions;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import tico.configuration.TLanguage;
import tico.interpreter.TInterpreter;
import tico.interpreter.components.TInterpreterAccumulatedCell;
import tico.interpreter.threads.TInterpreterMp3Sound;
import tico.interpreter.threads.TInterpreterWavSound;

public class TInterpreterCopyAction extends TInterpreterAbstractAction {

    TInterpreterWavSound nAudio = null;
    TInterpreterMp3Sound nMp3Audio = null;

    public TInterpreterCopyAction(TInterpreter interpreter) {
        super(interpreter, TLanguage.getString("TInterpreterCopy.NAME"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        interpreter = getInterpreter();
        String aCopiar = "";

        for (Object cell : TInterpreter.accumulatedCellsList) { //leo las acumuladas
            TInterpreterAccumulatedCell c = (TInterpreterAccumulatedCell) cell;
            if(c.getVoiceText()!=null){
                aCopiar += " " + c.getVoiceText();
            }else{
                aCopiar += " " + c.getText();
            }
        }

        if (aCopiar.equals("")) { //no habia ninguna acumulada => leo las textareas
            for (Component c : TInterpreter.interpretArea.getComponents()) {
                if (c instanceof JLabel) {
                    aCopiar += " " + ((JLabel) c).getText();
                }
            }
        }

        if (!aCopiar.equals("")) { //si encontré algo, lo copio al portapapeles
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection ss = new StringSelection(aCopiar.replaceAll("\\<.*?>", ""));
            cb.setContents(ss, ss);
        }


    }
}
