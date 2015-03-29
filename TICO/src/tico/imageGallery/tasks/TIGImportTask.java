/*
 * File: TIGImportTask.java
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
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import tico.configuration.TLanguage;
import tico.editor.TEditor;
import tico.imageGallery.dataBase.TIGDataBase;

public class TIGImportTask {
	private int lengthOfTask;
    private int current = 0;
    private String statMessage;
    private TEditor myEditor;
    private TIGDataBase myDataBase;
    private String myDirectoryPath;
    private String myImagesBehaviour;
    private String errorImages = "";
    private boolean stop = false;
    private boolean cancel = false;
    private boolean running = false;
    
    public TIGImportTask () {
        //compute length of task ...
        //in a real program, this would figure out
        //the number of bytes to read or whatever
        lengthOfTask = 1000;
    }

    //called from ProgressBarDemo to start the task
    public void go(TEditor editor,TIGDataBase dataBase, String directoryPath, String imagesBehaviour) {
        current = 0;
        running = true;
        this.myEditor = editor;
        this.myDataBase = dataBase;
        this.myDirectoryPath = directoryPath;
        this.myImagesBehaviour = imagesBehaviour;

        final SwingWorker worker = new SwingWorker() {
            public Object construct() {
                return new ActualTask(myEditor, myDataBase, myDirectoryPath, myImagesBehaviour);
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
    
    public String getErrorImages(){
    	return errorImages;
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
    
    private boolean exists(String list[], String name){
		boolean exists = false;
		int i = 0;
		while ((i<list.length) && !exists){
    		exists = name.equals(list[i]);
    		i++;
    	}
		return exists;
	}

    //the actual long running task, this runs in a SwingWorker thread
    public class ActualTask {	
    	
        public ActualTask (TEditor editor, TIGDataBase dataBase, String directoryPath, String myImagesBehaviour) {
                  
    		//Copies the images from the source directory to the directory images
    		//and renames them so that there are not characters like ' ' or '´'
        	
    		File myDirectory = new File(directoryPath);
    		String[] list = myDirectory.list();
    		
    		File fileXML = new File(directoryPath+"images.xml");
    		SAXBuilder builder = new SAXBuilder(false);      
	        try {
				Document docXML = builder.build(fileXML);
				Element root = docXML.getRootElement();
				List images = root.getChildren("image");
				Iterator j = images.iterator();
				int i=0;
				
				TIGDataBase.activateTransactions();
				
				while (j.hasNext() && !stop && !cancel){
					 current = i;
					 i++;
					 Element image = (Element)j.next();
			         String name = image.getAttributeValue("name");
			         List categories = image.getChildren("category");
			         Iterator k = categories.iterator();
			        
			         if (exists(list, name)){

			        	 String pathSrc = directoryPath.concat(name);
			        	 
			        	 String pathDst = System.getProperty("user.dir") + File.separator + "images" + File.separator +
			        	 	name.substring(0,1).toUpperCase() +	File.separator;
			        	 			        	 
			        	 String folder = System.getProperty("user.dir") + File.separator + "images" + File.separator +
			        	 	name.substring(0,1).toUpperCase();
			        	 
			        	 if (myImagesBehaviour.equals(TLanguage.getString("TIGImportDBDialog.REPLACE_IMAGES"))){
			        		 Vector<Vector<String>> aux = TIGDataBase.imageSearchByName(name.substring(0, name.lastIndexOf('.')));
		    			     if (aux.size()!=0){
		    			    	 int idImage = TIGDataBase.imageKeySearchName(name.substring(0, name.lastIndexOf('.')));
		    			    	 TIGDataBase.deleteAsociatedOfImage(idImage);
		    			     }
		    			     pathDst = pathDst.concat(name);
			        	 }
		    			 
			        	 //Rename if image exists on data base
			        	 if (myImagesBehaviour.equals(TLanguage.getString("TIGImportDBDialog.ADD_IMAGES"))){
			        	    Vector aux = new Vector();
		    				aux = TIGDataBase.imageSearchByName(name.substring(0, name.lastIndexOf('.')));
		    				int fileCount = 0;
		    				if (aux.size()!=0){
			    				while (aux.size()!=0){
			    					fileCount++;
			    					aux = TIGDataBase.imageSearchByName(name.substring(0, name.lastIndexOf('.'))+"_"+fileCount);			    					
			    				}
			    				pathDst = pathDst + name.substring(0, name.lastIndexOf('.')) + '_' + fileCount + 
	    							name.substring(name.lastIndexOf('.'),name.length());
			    				
			    				name = name.substring(0, name.lastIndexOf('.')) + '_' + fileCount + 
    								name.substring(name.lastIndexOf('.'),name.length());
		    				}else{
		    					pathDst = pathDst.concat(name);
		    				}
			        	 }
		    			
		    			 String pathThumbnail = (pathDst.substring(0,pathDst.lastIndexOf("."))).concat("_th.jpg");
			        	 
		    			//If the directory not exists, create!
		    			File newDirectoryFolder = new File(folder);
		    			if (!newDirectoryFolder.exists()){
		    				newDirectoryFolder.mkdirs(); 
		    			}
		    			 //Copy image
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
						
						//Insert in database
						TIGDataBase.insertImageDB(name.substring(0,name.lastIndexOf('.')), name);
						int idImage = TIGDataBase.imageKeySearchName(name.substring(0, name.lastIndexOf('.')));
						
						//Insert Categories and associate to image
						while (k.hasNext()){
				        	 Element category = (Element)k.next();
				        	 int idCategory = TIGDataBase.insertConceptDB(category.getValue());
				        	 TIGDataBase.insertAsociatedDB(idCategory, idImage);
				         }
			         }
			         else {
			        	 errorImages = errorImages+System.getProperty("line.separator")+name;
			         }
				}
				TIGDataBase.executeQueries();
    		
				current = lengthOfTask;
    		
	        } catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
}