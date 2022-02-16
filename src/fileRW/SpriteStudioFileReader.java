package fileRW;

import java.awt.image.IndexColorModel;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;


public class SpriteStudioFileReader
{
	private byte[] data;
	private int frames;
	private int width;
	private int height;
	private int pixelSize;
	private int paletteDepth;
	private byte R[];
	private byte G[];
	private byte B[];
	private byte[] imageDataBytes;
	private IndexColorModel imagePalette; 
	private int i;
	private FileInputStream fileIn;
	private BufferedInputStream fileInBuffer;
	private DataInputStream fileDataIn;
	
	public SpriteStudioFileReader(File path)
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
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			fileDataIn.readFully(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.readHeader();
		this.readPalette();
		this.readImageData();
		
		try {
			fileIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readHeader()
	{
		frames = (int)(data[0]|data[1]>>8|data[2]>>16|data[3]>>24);
		width = (int)(data[4]|data[5]>>8|data[6]>>16|data[7]>>24);
		height = (int)(data[8]|data[9]>>8|data[10]>>16|data[11]>>24);
		pixelSize = (int)(data[12]|data[13]>>8|data[14]>>16|data[15]>>24);
		paletteDepth = (int)(data[16]|data[17]>>8|data[18]>>16|data[19]>>24);
	}
	
	private void readPalette()
	{
		int numberOfColours = (int)java.lang.Math.pow(2,pixelSize);
		R = new byte[numberOfColours];
		G = new byte[numberOfColours];
		B = new byte[numberOfColours];
		
		int idx;
		for(i = 20,idx = 0; idx<numberOfColours; i+=4,idx++)
		{
			R[idx] = data[i];
			G[idx] = data[i+1];
			B[idx] = data[i+2];
		}
		
		imagePalette = new IndexColorModel(pixelSize,numberOfColours,R,G,B);
		
	}
	
	private void readImageData()
	{
		imageDataBytes = new byte[width*height*pixelSize*frames/8];
		
		int idx;
		for(idx = 0; idx<width*height*pixelSize*frames/8; i++, idx++)
		{
			imageDataBytes[idx] = data[i];
		}
	}
	
	public IndexColorModel getPalette()
	{
		return imagePalette;
	}
	
	public int getPixelSize()
	{
		return pixelSize;
	}
	
	public int getPaletteDepth()
	{
		return paletteDepth;
	}
	
	public byte[] getData()
	{
		return imageDataBytes;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public int getNumOfFrames()
	{
		return frames;
	}

}