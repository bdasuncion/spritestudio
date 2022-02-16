package fileRW;

import java.awt.image.IndexColorModel;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;


public class PaletteFileReader
{
	private byte[] data;
	private int pixelSize;
	private int paletteDepth;
	private byte R[];
	private byte G[];
	private byte B[];
	
	private IndexColorModel imagePalette; 

	private FileInputStream fileIn;
	private BufferedInputStream fileInBuffer;
	private DataInputStream fileDataIn;
	
	public PaletteFileReader(File path)
	{
		try {
			fileIn = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		fileInBuffer = new BufferedInputStream(fileIn);
		fileDataIn = new DataInputStream(fileInBuffer);
	
		try {
			data = new byte[fileInBuffer.available()];
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			fileDataIn.readFully(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.readHeader();
		this.readPalette();
		
		try {
			fileIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readHeader()
	{
		pixelSize = (int)(data[0]|data[1]>>8|data[2]>>16|data[3]>>24);
		paletteDepth = (int)(data[4]|data[5]>>8|data[6]>>16|data[7]>>24);
	}
	
	private void readPalette()
	{
		int numberOfColours = (int)java.lang.Math.pow(2,pixelSize);
		R = new byte[numberOfColours];
		G = new byte[numberOfColours];
		B = new byte[numberOfColours];
		
		int idx,i;
		for(i = 8,idx = 0; idx<numberOfColours; i+=4,idx++)
		{
			R[idx] = data[i];
			G[idx] = data[i+1];
			B[idx] = data[i+2];
		}
		
		imagePalette = new IndexColorModel(pixelSize,numberOfColours,R,G,B);
		
	}
	
	public IndexColorModel getPalette()
	{
		return imagePalette;
	}
	
	//public int getPaletteDepth()
	//{
	//	return paletteDepth;
	//}
}