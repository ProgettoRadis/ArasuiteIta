package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import configuration.TConfiguration;

public class ImageManager {
    
    /**
     * It adds a new image to the path
     * @param path
     */
    public static String add(String sourcePath){
        try {
            //Configuration
            //String rootPath = ConfigManager.getInstance().getProperty(ConfigManager.KEY_ROOT_PATH);
            //String imgFolder = ConfigManager.getInstance().getProperty(ConfigManager.KEY_IMAGE_FOLDER);
        
            //Copying
            File sourceImage = new File(sourcePath);
            File destImage = new File(TConfiguration.getImagesPath());
            
            if(!sourceImage.exists()){
                return null;
            }else{
                
                if(!destImage.exists()){
                    destImage.mkdirs();
                }
                
                //If file exists we modify the name
                String destName = destImage + File.separator + sourceImage.getName();
                File tempFile = new File(destName);
                if(tempFile.exists()){
                    int cont = 0;
                    destName = destImage + File.separator + sourceImage.getName().substring(0, sourceImage.getName().lastIndexOf(".")) + "_" + cont + sourceImage.getName().substring(sourceImage.getName().lastIndexOf("."));
                    File newFileName = new File(destName);
                    while(newFileName.exists()){
                        cont++;
                        destName = destImage + File.separator + sourceImage.getName().substring(0, sourceImage.getName().lastIndexOf(".")) + "_" + cont + sourceImage.getName().substring(sourceImage.getName().lastIndexOf("."));
                        newFileName = new File(destName);
                    }
                }
                
                FileInputStream in = new FileInputStream(sourceImage);
                FileOutputStream out = new FileOutputStream(destName);
                                
                byte b[] = new byte[(int) sourceImage.length()];
                while(in.read(b) != -1){
                    out.write(b);
                }
            
                in.close();
                out.close();
                
                return destName;
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * It removes an image from the path
     * @param path
     */
    public static boolean delete(String name){
        File f = new File(TConfiguration.getImagesPath() + File.separator + name);
        if (!f.exists()) {
            return true;
        } else {
            f.delete();
            return true;
        }
    }
}
