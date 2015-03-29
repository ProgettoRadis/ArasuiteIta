/*
 * File: TIGTableModelOneColumn.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Patricia M. Jaray
 * 
 * Date: Mar 31, 2008
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

package tico.imageGallery.tasks;

import javax.swing.table.AbstractTableModel;
import javax.swing.event.*;

import tico.configuration.TLanguage;

import java.util.Vector;
import java.util.LinkedList;

public class TIGTableModelOneColumn extends AbstractTableModel{
	
        final String[] columnNames = {TLanguage.getString("TIGTableModel.FIRST_COLUMN")};
               
        private Vector data = new Vector();
        
        Class[] types = new Class [] {java.lang.String.class};
            
        public TIGTableModelOneColumn(Vector keyWords){
        	
        	for (int i = 0; i < keyWords.size(); i++)
        		{	
        			String element = new String();
        			element = (String)keyWords.elementAt(i);	
        			data.addElement(element);
        		}	 
        }
        
        /*
         * Returns the number of columns of the table
         */
        public int getColumnCount() {
            return columnNames.length;
        }
        
        /*
         * Returns the number of rows of the table
         */
        public int getRowCount() {
            return data.size();
        }

        /*
         * Returns the name of the column indicated
         */
        @Override
		public String getColumnName(int col) {
            return columnNames[col];
        }

        /*
         * Returns the value of the cell indicated
         */
        public Object getValueAt(int row) {            
        	return data.elementAt(row);
        }
        
        /*
         * Returns the value of the cell indicated
         */
        public Object getValueAt(int row,int col) {            
        	return data.elementAt(row);
        }
        
        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        @Override
		public Class getColumnClass(int c) {
        	return types [c];
        }

        /*
         * Returns true if the cell is editable
         */
        @Override
		public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears on screen.
            return false;
        }

        /*
         * Sets the value of the indicated cell to value
         */             
        public void setValueAt(Object value, int row) {
        		
        		if (row < getRowCount())
        		{
            		//data.setElementAt(value, row);
        			
        			deleteKeyWord(row);
        			
        			addElement((String)value);
        		
            		//Create a TableModelEvent
            		TableModelEvent evento;
            		evento = new TableModelEvent (this, row, row, 0, TableModelEvent.UPDATE);

            		// and tell the listeners
            		tellListeners (evento);
        		}
        }
        
        /*
         * Return all the data
         */
        public Vector returnData(){        		
        		return data; 
        }
        
        /*
         * Adds an element to the table but in the rigth place
         */
        public void addElement(String element){
        		if (!isElement(element)){
        			        		
        			//Let's order the data
        			int i = 0;
        			boolean inserted = false;
        			while ((i < data.size()) && (!inserted))
        			{
        				if (order((String)getValueAt(i)).compareTo(order(element)) > 0)
        				{
        					data.insertElementAt(element, i);
        					inserted = true;
        				}
        				else
        					i++;
        			}
        			if (!inserted)        			
        				data.addElement(element);
        			
        			//Create a TableModelEvent
        			TableModelEvent evento;
        			evento = new TableModelEvent (this, i,i,TableModelEvent.ALL_COLUMNS,
        					TableModelEvent.INSERT);

        			// and tell the listeners
        			tellListeners (evento);
        		}
        }
        
        /*
         * Returns true if the element is in the table
         */
        public boolean isElement(String word){
        		boolean is = false;
        		int i = 0;
        		while ((i < data.size()) && (!is) ){
        			if (order(word).equals(order((String)getValueAt(i)))){
        				is = true;
        			}
        			else i++;
        		}        		
        		return is;
        }
        
        /*
         * Returns the index of word in the table
         */
        public int whereIsElement(String word){
    		boolean is = false;
    		int i = 0;
    		while ((i < data.size()) && (!is) ){
    			if (order(word).equals(order((String)getValueAt(i)))){
    				is = true;
    			}
    			else i++;
    		} 
    		if (is)
    			return i;
    		else 
    			return 0;
    }
        
        /**
         * Deletes the row indicated
         */
        public void deleteKeyWord (int row)
        {	
        		        		
            // Delete the row
           data.remove(row);
       
            // Create a TableModelEvent
            TableModelEvent evento = new TableModelEvent (this, row, row, 
                TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
            
            // and tell the listeners
            tellListeners (evento);
        }
        /**
         * Deletes the object indicated
         */
        
        public void deleteKeyWord2 (String object)
        {	
        		        		
            // Delete the row
           data.remove(object);
           int row=0;
           for (int i=0; i< data.size(); i++){
        	   if (data.get(i)== object){
        		   row=i;
        	   }
           }
       
            // Create a TableModelEvent
            TableModelEvent evento = new TableModelEvent (this, row, row, 
                TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
            
            // and tell the listeners
            tellListeners (evento);
        }   
        
        
        
        /** Adds a listener to the list that is notified each time a change
         * to the data model occurs.
         *
         * @param	l		the TableModelListener
         *
         */
        @Override
		public void addTableModelListener(TableModelListener l) {
            
            listeners.add (l);
        }
        
        /**
         * Tells the listeners the event
         */
        private void tellListeners (TableModelEvent evento)
        {
            int i;
           
            for (i=0; i<listeners.size(); i++)
                ((TableModelListener)listeners.get(i)).tableChanged(evento);
        }
                
        /** 
         * Listeners list
         */
        private LinkedList listeners = new LinkedList();
        
        public void updateTable(int row)
        {
        	// Create a TableModelEvent
            TableModelEvent evento = new TableModelEvent (this, row, row, 
                TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
            
            // and tell the listeners
            tellListeners (evento);
        }
        
        private static String order(String word){
        	String result = word.replace(' ', '_').replace(',', '-').replace('á', 'a').replace('é', 'e').replace('í', 'i').replace('ó', 'o').replace('ú', 'u').
    		replace('Á', 'A').replace('É', 'E').replace('Í', 'I').replace('Ó', 'O').replace('Ú', 'U').toLowerCase();
    		return result;
    	}
        
}
