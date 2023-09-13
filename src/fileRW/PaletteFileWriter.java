package fileRW;

import java.awt.image.IndexColorModel;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
/* 
	File Structure
	Header
	pixel size:			4 bytes(4 or 8 bit)
	palette depth:		4 bytes(16 or 32 bit)

	Palette			
	size:				4 bytes per element
	number of elements:	16(pixel size: 4 bits) or 256 entries(pixel size: 8 bits)
*/

public class PaletteFileWriter
{
	private int pixelSize;
	private int paletteDepth;
	private FileOutputStream fileOutNoBuff;
	private BufferedOutputStream fileOut;
	private IndexColorModel paletteData;
	
	public PaletteFileWriter(File f, IndexColorModel cm, int palDepth) {
		File file =  new File(f.getParent() + "\\" + appendExtensionPal(f.getName()));
		
		try {
			fileOutNoBuff = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		fileOut = new BufferedOutputStream(fileOutNoBuff);
		
		paletteData = cm;
		pixelSize = cm.getPixelSize();
		paletteDepth = palDepth;
		
		this.createHeader();
		this.createPalette();
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
	
	private void createHeader() {	
		try {
			fileOut.write((byte)pixelSize&0xff);
			fileOut.write((byte)(pixelSize&0x00ff)>>8);
			fileOut.write((byte)(pixelSize&0x0000ff)>>16);
			fileOut.write((byte)(pixelSize&0x000000ff)>>24);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			fileOut.write((byte)paletteDepth&0xff);
			fileOut.write((byte)(paletteDepth&0x00ff)>>8);
			fileOut.write((byte)(paletteDepth&0x0000ff)>>16);
			fileOut.write((byte)(paletteDepth&0x000000ff)>>24);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void createPalette() {
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
	
	private String appendExtensionPal(String fileName) {
		if(fileName.length()<=4)
			return fileName + ".pal";
	   String ext = fileName.substring(fileName.length() - 4).toLowerCase();
       
       if(ext.matches(".pal") == true)
       	return fileName;

       return fileName + ".pal";
	}
}