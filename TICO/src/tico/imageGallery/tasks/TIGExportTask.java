/*
 * File: TIGExportTask.java
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import tico.editor.TEditor;
import tico.imageGallery.dataBase.TIGDataBase;

public class TIGExportTask {
    private int lengthOfTask;
    private int current = 0;
    private String statMessage;
    private TEditor myEditor;
    private TIGDataBase myDataBase;
    private String myDirectoryPath;
    private boolean stop = false;
    private boolean cancel = false;
    private boolean running = false;
    
    private Vector myImages;

    public TIGExportTask () {
        //compute length of task ...
        //in a real program, this would figure out
        //the number of bytes to read or whatever
        lengthOfTask = 1000;
    }

    //called from ProgressBarDemo to start the task
    public void go(TEditor editor,TIGDataBase dataBase, String directoryPath, Vector images) {
        current = 0;
        running = true;
        this.myEditor = editor;
        this.myDataBase = dataBase;
        this.myDirectoryPath = directoryPath;
        this.myImages = images;
 
        final SwingWorker worker = new SwingWorker() {
            @Override
			public Object construct() {
                return new ActualTask(myEditor, myDataBase, myDirectoryPath, myImages);
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
    	
        public ActualTask (TEditor editor,TIGDataBase dataBase, String directoryPath, Vector images) {
                      	
    		int i;
           	lengthOfTask = images.size();
 
			// Create the data base node
			Element dataBaseXML = new Element("dataBase");

			for (i = 0; ((i < images.size()) && !stop && !cancel); i++){
    			
    			Vector imagen = new Vector(2);
    			imagen = (Vector) images.elementAt(i);
				String element = (String)imagen.elementAt(0);
    			current = i;
    			
    			String pathSrc = System.getProperty("user.dir")+File.separator+"images"+File.separator + 
    				element.substring(0,1).toUpperCase() + File.separator + element;
    				
    			String name = pathSrc.substring(pathSrc.lastIndexOf(File.separator) + 1,pathSrc.length());
    			
    			String pathDst = directoryPath + name;
				try {
					// Create channel on the source
					FileChannel srcChannel = new FileInputStream(pathSrc).getChannel();
					// Create channel on the destination
					FileChannel dstChannel = new FileOutputStream(pathDst).getChannel();
					// Copy file contents from source to destination
					dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
	    
					// Close the channels
					srcChannel.close();
					dstChannel.close();
				} catch (IOException exc) {
					System.out.println(exc.getMessage());
					System.out.println(exc.toString());    		
				}
        
				Vector<String> keyWords = new Vector<String>();
				keyWords = TIGDataBase.asociatedConceptSearch(element);
				
				// Create image node
				Element image = new Element("image");
				image.setAttribute("name", name);
				if (keyWords.size()!=0){
					for (int k=0; k<keyWords.size(); k++){
						Element category = new Element("category");
						category.setText(keyWords.get(k).trim());
						image.addContent(category);
					}
				}
				dataBaseXML.addContent(image);
    		}
    		
    		Document doc = new Document(dataBaseXML);
					
    		try{
				XMLOutputter out = new XMLOutputter();
				FileOutputStream f = new FileOutputStream(directoryPath+"images.xml");
				out.output(doc,f);
				f.flush();
				f.close();
				}catch(Exception e){
					e.printStackTrace();
				}
    		current = lengthOfTask;
        }
    	
    }
}