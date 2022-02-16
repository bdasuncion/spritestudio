package fileRW;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;

import tools.ImageTools;

public class ConvertFileToTiles
{
    public static void convertFile(File f)
    {
	SpriteStudioFileReader fileRead = new SpriteStudioFileReader(f);
	int nFrames = fileRead.getNumOfFrames();
	BufferedImage[] images = new BufferedImage[nFrames];
	
	byte[] allData = fileRead.getData();
	int width,height, pixelSize, sizeInBytes;
	IndexColorModel pal = fileRead.getPalette();
	width = fileRead.getWidth();
	height = fileRead.getHeight();
	pixelSize = fileRead.getPixelSize();
	sizeInBytes = width*height*pixelSize/8;
	
	for(int i = 0; i<nFrames; i++)
	{
	    byte[] imageData = new byte[sizeInBytes];
	    for(int j = 0; j<sizeInBytes; j++)
	    {
		imageData[j] = allData[j + i*sizeInBytes];
	    }
	    images[i] = ImageTools.createBufferedImage(width, height, pal, imageData);
	}
	
	File[] files = new File[nFrames];
	
	String fileName = removeExtensionKnight(f.getName());
	if(nFrames == 1)
	{
	    files[0] = new File(f.getParent() + "\\" + fileName);
	    ExportTiles.exportToTiles(files[0], images[0], pal);
	}
	else
	{
	    for(int i = 0; i<nFrames; i++)
	    {
		files[i] = new File(f.getParent() + "\\" + fileName + i);
		ExportTiles.exportToTiles(files[i], images[i], pal);
	    }
	}

    }
    
    private static String removeExtensionKnight(String fileName)
    {
	String[] names = fileName.split(".k");
	return names[0];
    }

}
/*
 * 
	private JMenuItem convertToTiles; 
 	
 	convertToTiles = new JMenuItem("Convert To Tiles");
	convertToTiles.addActionListener(new convertToTiles());
		
	class convertToTiles implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			JFileChooser openFileDialog;
			openFileDialog = new JFileChooser();
			openFileDialog.setAcceptAllFileFilterUsed(false);
			openFileDialog.addChoosableFileFilter(new BraveKnightFileFilter());
			openFileDialog.setMultiSelectionEnabled(true);
			openFileDialog.showOpenDialog(MainFrame);
			
			if(openFileDialog.getSelectedFiles().length == 0)
			    return;
			File[] files = openFileDialog.getSelectedFiles();
			
			for(int i = 0; i<openFileDialog.getSelectedFiles().length; i++)
			{
			    ConvertFileToTiles.convertFile(files[i]);
			}
		}
	}
 * 
 * */
