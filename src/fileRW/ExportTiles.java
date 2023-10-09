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
pixel size: 	 			4 bytes
number of tiles in length:	4 bytes
number of tiles in width:	4 bytes
number of 8x8 tiles:		4 bytes

Palette			
size:					4 bytes per element
number of elements:		16(pixel size: 4 bits) or 256 entries(pixel size: 8 bits)
Image data
size:					1 byte per element
number of elements:		(8*8*number of tile)/2(pixel size: 4 bits) or 8*8*number of tile(pixel size: 8 bits)	
*/
public class ExportTiles
{
	//export image to 8x8 tiles
	public static void exportToTiles(File f, BufferedImage image, IndexColorModel cm)
	{
		File fileOut = new File(f.getParent() + "\\" + appendExtensionTile(f.getName()));
		FileOutputStream fileOutStream = null;
		BufferedOutputStream fileOutBuffer;;
		
		try {
			fileOutStream = new FileOutputStream(fileOut);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		fileOutBuffer = new BufferedOutputStream(fileOutStream);
		
		int pixelSize = cm.getPixelSize();
		int widthInTiles = (image.getWidth()/8);
		int heightInTiles = (image.getHeight()/8);
		int numberOfTiles = widthInTiles*heightInTiles;
		
		writeHeader(fileOutBuffer, 
				pixelSize,widthInTiles,
				heightInTiles, numberOfTiles);
		writePalette(fileOutBuffer,cm);
		
		if(pixelSize == 4)
			writeTiles4bit(image.getRaster().getDataBuffer(),fileOutBuffer, image.getWidth(), image.getHeight());
		else
			writeTiles8bit(image.getRaster().getDataBuffer(),fileOutBuffer, image.getWidth(), image.getHeight());
		
		try {
			fileOutBuffer.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			fileOutBuffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void writeHeader(BufferedOutputStream fileOutBuffer, 
			int pixelSize, int widthInTiles, 
			int heightInTiles, int numberOfTiles)
	{
		try {
			fileOutBuffer.write((byte)pixelSize&0xff);
			fileOutBuffer.write((byte)((pixelSize&0xff00)>>8));
			fileOutBuffer.write((byte)((pixelSize&0xff0000)>>16));
			fileOutBuffer.write((byte)((pixelSize&0xff000000)>>24));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			fileOutBuffer.write((byte)widthInTiles&0xff);
			fileOutBuffer.write((byte)((widthInTiles&0xff00)>>8));
			fileOutBuffer.write((byte)((widthInTiles&0xff0000)>>16));
			fileOutBuffer.write((byte)((widthInTiles&0xff000000)>>24));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fileOutBuffer.write((byte)heightInTiles&0xff);
			fileOutBuffer.write((byte)((heightInTiles&0xff00)>>8));
			fileOutBuffer.write((byte)((heightInTiles&0xff0000)>>16));
			fileOutBuffer.write((byte)((heightInTiles&0xff000000)>>24));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			fileOutBuffer.write((byte)numberOfTiles&0xff);
			fileOutBuffer.write((byte)((numberOfTiles&0xff00)>>8));
			fileOutBuffer.write((byte)((numberOfTiles&0xff0000)>>16));
			fileOutBuffer.write((byte)((numberOfTiles&0xff000000)>>24));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void writePalette(BufferedOutputStream fileOutBuffer, IndexColorModel paletteData)
	{
		for(int i = 0; i<paletteData.getMapSize(); i++)
		{
			try {
				fileOutBuffer.write((byte)paletteData.getRed(i)&0xff);
				fileOutBuffer.write((byte)paletteData.getGreen(i)&0xff);
				fileOutBuffer.write((byte)paletteData.getBlue(i)&0xff);
				fileOutBuffer.write((byte)paletteData.getAlpha(i)&0xff);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void writeTiles4bit(DataBuffer imageData, BufferedOutputStream fileOutBuffer, int width, int height)
	{
		int offset = 0;
		int i = 0, x = 0;
	
		for(int k = 0; k<height/8; k+=1)
		{
			offset = (width*4)*k;
			for(int j = 0; j<width/8; j++)
			{
				for(i = offset, x = 0; x<8; i+=width/2, x++)
				{
					try {
						fileOutBuffer.write(imageData.getElem(i)&0xff);
						fileOutBuffer.write(imageData.getElem(i+1)&0xff);
						fileOutBuffer.write(imageData.getElem(i+2)&0xff);
						fileOutBuffer.write(imageData.getElem(i+3)&0xff);					
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				offset += 4;
			}
		}
	}
	
	private static void writeTiles8bit(DataBuffer imageData, BufferedOutputStream fileOutBuffer, int width, int height)
	{
		int offset = 0;
		int i = 0, x = 0;
	
		for(int k = 0; k<height/8; k+=1)
		{
			offset = (width*8)*k;
			for(int j = 0; j<width/8; j++)
			{
				for(i = offset, x = 0; x<8; i+=width, x++)
				{
					try {
						fileOutBuffer.write(imageData.getElem(i)&0xff);
						fileOutBuffer.write(imageData.getElem(i+1)&0xff);
						fileOutBuffer.write(imageData.getElem(i+2)&0xff);
						fileOutBuffer.write(imageData.getElem(i+3)&0xff);
						fileOutBuffer.write(imageData.getElem(i+4)&0xff);
						fileOutBuffer.write(imageData.getElem(i+5)&0xff);
						fileOutBuffer.write(imageData.getElem(i+6)&0xff);
						fileOutBuffer.write(imageData.getElem(i+7)&0xff);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				offset += 8;
			}
		}
	}
	
	private static String appendExtensionTile(String fileName)
	{
		if(fileName.length()<=5)
			return fileName + ".tile";
	   String ext = fileName.substring(fileName.length() - 5).toLowerCase();
       
       if(ext.matches(".tile") == true)
       	return fileName;

       return fileName + ".tile";
	}
}