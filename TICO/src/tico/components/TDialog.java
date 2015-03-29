/*
 * File: TDialog.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Pablo Muñoz
 * 
 * Date: May 18, 2006
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

package tico.components;

import java.awt.Frame;

import javax.swing.JDialog;

/**
 * An implementation of a dialog with the format parameters for Tico
 * applications.
 *
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TDialog extends JDialog {
    /**
     * Creates a non-modal <code>TDialog</code> without a title and without a
     * specified <code>Frame</code> owner. A shared, hidden frame will be set
     * as the owner of the dialog.
     */
    public TDialog() {
        this((Frame)null, false);
    }

    /**
     * Creates a non-modal <code>TDialog</code> without a title with the specified
     * <code>Frame</code> as its owner. If owner  is null, a shared, hidden frame
     * will be set as the owner of the dialog.
     * 
     * @param owner The <code>Frame</code> from which the dialog is displayed
     */
    public TDialog(Frame owner) {
    	super(owner, false);
    }

    /**
     * Creates a non-modal <code>TDialog</code> with the specified title and with
     * the specified owner frame. If owner  is null, a shared, hidden frame will
     * be set as the owner of the dialog.
     * 
     * @param owner The <code>Frame</code> from which the dialog is displayed
     * @param modal Set to <i>true</i> for a modal dialog, <i>false</i> for one
     * that allows others windows to be active at the same time
     */
    public TDialog(Frame owner, boolean modal) {
    	super(owner, null, modal);
    }

    /**
     * Creates a modal or non-modal <code>TDialog</code> with the specified title
     * and the specified owner <code>Frame</code>. If owner  is null, a shared,
     * hidden frame will be set as the owner of this dialog. All constructors
     * defer to this one.
     * 
     * @param owner The <code>Frame</code> from which the dialog is displayed
     * @param title The <code>String</code> to display in the dialog's title bar
     * @param modal Set to <i>true</i> for a modal dialog, <i>false</i> for one
     * that allows others windows to be active at the same time
     */
    public TDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);     
    }
}