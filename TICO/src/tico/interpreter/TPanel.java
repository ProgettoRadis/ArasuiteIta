/*
 * File: TPanel.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Antonio Rodríguez
 * 
 * Date: Aug 22, 2006
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

package tico.interpreter;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import javax.swing.JPanel;

/**
 * 
 * @author Antonio Rodríguez
 *
 */
public class TPanel extends JPanel implements Printable{
	
	public TPanel(){
		super();
	}
		public int print(Graphics g,PageFormat pf,int pi) 
	      throws PrinterException {
	      if( pi >= 1 ) {
	        return( Printable.NO_SUCH_PAGE);
	        }
	      Graphics2D g2 = (Graphics2D)g;
	      g2.translate( pf.getImageableX(),pf.getImageableY() );
	      pf.setOrientation(PageFormat.LANDSCAPE);
	      Font f = Font.getFont("Courier");
	     
	      g2.setFont(f);
	      // En este caso, se envía directamente el objeto Graphics2D a 
	      // la impresora
	      paint(g2);
	      return(Printable.PAGE_EXISTS);
	      }
}
	

	

