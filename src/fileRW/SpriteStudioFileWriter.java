package fileRW;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/* 
  File Structure
  Header
  number of frames: 	4 bytes
  width: 				4 bytes
  height:				4 bytes
  pixel size:			4 bytes(4 or 8 bit)
  palette depth:		4 bytes(16 or 32 bit)
  
  Palette			
  size:					4 bytes per element
  number of elements:	16(pixel size: 4 bits) or 256 entries(pixel size: 8 bits)
  
  Image data
  size:					1 byte per element
  number of elements:	(width*height)/2(pixel size: 4 bits) or width*height(image depth: 8 bits)	
*/
public class SpriteStudioFileWriter
{
	private int frames;
	private int width;
	private int height;
	private int pixelSize;
	private int paletteDepth;
	private FileOutputStream fileOutNoBuff;
	private BufferedOutputStream fileOut;
	private DataBuffer imageData;
	private IndexColorModel paletteData;
	
	private static final String EXT = ".knight";
	
	public SpriteStudioFileWriter(File f, BufferedImage[] buffer, IndexColorModel cm, int depth)
	{
		File fileR = new File(f.getParent() + "\\" + this.appendExtension(f.getName()));
		try {
			fileOutNoBuff = new FileOutputStream(fileR);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		fileOut = new BufferedOutputStream(fileOutNoBuff);
		frames = buffer.length;
		width = buffer[0].getWidth();
		height = buffer[0].getHeight();
		
		paletteData = cm;
		pixelSize = cm.getPixelSize();
		paletteDepth = depth;
		
		this.createHeader();
		this.createPalette();
		for(int i = 0; i<buffer.length;i++)
		{
			imageData = buffer[i].getRaster().getDataBuffer();
			this.createImageData();
		}
		
		try {
			fileOut.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void createHeader()
	{
		try {
			fileOut.write((byte)frames&0xff);
			fileOut.write((byte)((frames&0xff00)>>8));
			fileOut.write((byte)((frames&0xff0000)>>16));
			fileOut.write((byte)((frames&0xff000000)>>24));
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		try {
			fileOut.write((byte)width&0xff);
			fileOut.write((byte)((width&0xff00)>>8));
			fileOut.write((byte)((width&0xff0000)>>16));
			fileOut.write((byte)((width&0xff000000)>>24));
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		try {
			fileOut.write((byte)height&0xff);
			fileOut.write((byte)((height&0xff00)>>8));
			fileOut.write((byte)((height&0xff0000)>>16));
			fileOut.write((byte)((height&0xff000000)>>24));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			fileOut.write((byte)pixelSize&0xff);
			fileOut.write((byte)((pixelSize&0xff00)>>8));
			fileOut.write((byte)((pixelSize&0xff0000)>>16));
			fileOut.write((byte)((pixelSize&0xff000000)>>24));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			fileOut.write((byte)paletteDepth&0xff);
			fileOut.write((byte)((paletteDepth&0xff00)>>8));
			fileOut.write((byte)((paletteDepth&0xff0000)>>16));
			fileOut.write((byte)((paletteDepth&0xff000000)>>24));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void createPalette()
	{
		for(int i = 0; i<paletteData.getMapSize(); i++)
		{
			try {
				fileOut.write((byte)paletteData.getRed(i)&0xff);
				fileOut.write((byte)paletteData.getGreen(i)&0xff);
				fileOut.write((byte)paletteData.getBlue(i)&0xff);
				fileOut.write((byte)paletteData.getAlpha(i)&0xff);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void createImageData()
	{
		for(int i = 0; i<imageData.getSize(); i++)
		{
			try {
				fileOut.write((byte)imageData.getElem(i)&0xff);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String appendExtension(String fileName) {
		if(fileName.length() <= EXT.length())
			return fileName + EXT;

		String ext = fileName.substring(fileName.length() - EXT.length()).toLowerCase();
          
		if(ext.matches(EXT) == true)
			return fileName;

		return fileName + EXT;
	}

}