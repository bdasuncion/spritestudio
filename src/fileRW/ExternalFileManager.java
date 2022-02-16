package fileRW;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class ExternalFileManager {
	
	public File getFileFromFileDialog(boolean isOpenFile, Component parent, FileFilter fileFilter) {
		return getFileFromFileDialog(null, isOpenFile, parent, fileFilter);
	}
	
	public File getFileFromFileDialog(File dir, boolean isOpenFile, 
			Component parent, FileFilter fileFilter) {
		JFileChooser fileDialog;
		fileDialog = new JFileChooser();
		fileDialog.setAcceptAllFileFilterUsed(false);
		fileDialog.addChoosableFileFilter(fileFilter);
		
		if (dir != null) {
			fileDialog.setCurrentDirectory(dir);
		}
		
		if (!isOpenFile) {
			fileDialog.showSaveDialog(parent);	
		} else {
			fileDialog.showOpenDialog(parent);
		}
		

		return fileDialog.getSelectedFile();
	}
}
