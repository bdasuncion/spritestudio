package fileRW;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class TileFileFilter extends FileFilter {

	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
	     }
	     
	     String fileName = f.getName();
	     int i = fileName.lastIndexOf('.');
	     
	    String ext = null; 
        if (i > 0 &&  i < fileName.length() - 1) {
            ext = fileName.substring(i+1).toLowerCase();
        } else {
        	return false;
        }
        
        if(ext.matches("tile") == true) {
        	return true;
        }

        return false;
	}
	
	public String getDescription()  {
		return "Tile(*.tile)";
	}

}
