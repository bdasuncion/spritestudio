package fileRW;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FileFilterBase extends FileFilter{
	private String extension;
	private String description;
	
	public FileFilterBase(String ext, String description) {
		setExtension(ext);
		setDescription(description);
	}
	
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
	     }
	     
	     String fileName = f.getName();
	     int i = fileName.lastIndexOf('.');
	     
	    String ext = null; 
        if (i > 0 &&  i < fileName.length() - 1) {
            ext = fileName.substring(i+1).toLowerCase();
        }
        else 
        	return false;
        if(ext.matches(getExtension()) == true)
        	return true;

        return false;
	}

	@Override
	public String getDescription() {
		return description;
	}

	private String getExtension() {
		return extension;
	}

	private void setExtension(String extension) {
		this.extension = extension;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}
