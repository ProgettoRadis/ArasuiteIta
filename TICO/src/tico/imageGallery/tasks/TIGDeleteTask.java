/*
 * File: TIGDeleteTask.java
 * 		This file is part of Tico, an application to create and	perform
 * 		interactive communication boards to be used by people with
 * 		severe motor disabilities.
 * 
 * Authors: Patricia M. Jaray
 * 
 * Date: Feb 20, 2008
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import tico.editor.TEditor;
import tico.imageGallery.dataBase.TIGDataBase;

public class TIGDeleteTask {
    private int lengthOfTask;
    private int current = 0;
    private String statMessage;
    private TEditor myEditor;
    private TIGDataBase myDataBase;
    private boolean stop = false;
    private boolean cancel = false;
    private boolean running = false;
    
    private Vector myImages;

    public TIGDeleteTask () {
        //compute length of task ...
        //in a real program, this would figure out
        //the number of bytes to read or whatever
        lengthOfTask = 1000;
    }

    //called from ProgressBarDemo to start the task
    public void go(TEditor editor, TIGDataBase dataBase, Vector images) {
        current = 0;
        running = true;
        this.myEditor = editor;
        this.myDataBase = dataBase;
        this.myImages = images;
 
        final SwingWorker worker = new SwingWorker() {
            @Override
			public Object construct() {
                return new ActualTask(myEditor, myDataBase, myImages);
            }
        };
    }

    //called from ProgressBarDemo to find out how much work needs to be done
    public int getLengthOfTask() {
        return lengthOfTask;
    }
    
    public void setLengthOfTask(int num) {
        lengthOfTask = num;
    }

    //called from ProgressBarDemo to find out how much has been done
    public int getCurrent() {
        return current;
    }

    public void stop() {
    	stop = true;
    	running = false;
    }
    
    public void cancel() {
    	cancel = true;
    	running = false;
    }
    
    public boolean isRunning(){
    	return running;
    }

    //called from ProgressBarDemo to find out if the task has completed
    public boolean done() {
        if (current >= lengthOfTask)
            return true;
        else
            return false;
    }

    public String getMessage() {
        return statMessage;
    }

    //the actual long running task, this runs in a SwingWorker thread
    public class ActualTask {
    	
        public ActualTask (TEditor editor, TIGDataBase dataBase, Vector imagenes) {
        	
        	File directory = null;
        	File[] directoryFiles;
        	lengthOfTask = imagenes.size();
        	
        	TIGDataBase.activateTransactions();
        	
        	for (int i=0; ((i < imagenes.size()) && !stop && !cancel); i++){
        		current = i;
				Vector data1 = new Vector(2);
				data1 = (Vector) imagenes.elementAt(i);
				String pathImage = (String)data1.elementAt(0);
				
										
				int key = TIGDataBase.imageKeySearch(pathImage);
				TIGDataBase.deleteImageDB(key);
				
				//Delete from the directory the image and its thumbnail
				File image = new File("images" + File.separator + pathImage.substring(0,1).toUpperCase() + File.separator + pathImage);
				image.delete();
				
				File imageTh = new File("images" + File.separator + pathImage.substring(0,1).toUpperCase() + File.separator + pathImage.substring(0,pathImage.lastIndexOf('.')) + "_th.jpg");
				imageTh.delete();
				
				directory = new File("images" + File.separator + pathImage.substring(0,1).toUpperCase());
				directoryFiles = directory.listFiles();
				if (directoryFiles.length==0){
					directory.delete();
				}
			}
        	
        	TIGDataBase.executeQueries();
        	
        	current = lengthOfTask;    
        }
    	
    }
}