package tools;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;


public class ImageTools
{
	public static final int SIZE_4BITS_PERPIXEL = 4;
	public static final int SIZE_8BITS_PERPIXEL = 8;
	public ImageTools()
	{
		
	}
	
	public static BufferedImage createBufferedImage(int width, int height, IndexColorModel cm, byte[] imageData)
	{
		int type;
		if(cm.getMapSize() == 16)
			type = BufferedImage.TYPE_BYTE_BINARY;
		else
			type = BufferedImage.TYPE_BYTE_INDEXED;
		BufferedImage image = new BufferedImage(width,height,type, cm);
		
		DataBuffer data = image.getData().getDataBuffer();
		
		for(int i = 0; i<imageData.length; i++ )
		{
			data.setElem(i, (byte)imageData[i]);
		}
		
		int pixelSize = cm.getPixelSize();
		
		WritableRaster wRaster = Raster.createPackedRaster(data, width, height, pixelSize, new Point(0,0));
		
		image.setData(wRaster);
		return image;
	}
	
	public static BufferedImage createEmptyImage(int width, int height, IndexColorModel cm)
	{
		int type;
		if(cm.getMapSize() == 16)
			type = BufferedImage.TYPE_BYTE_BINARY;
		else
			type = BufferedImage.TYPE_BYTE_INDEXED;
		BufferedImage image = new BufferedImage(width,height,type, cm);
		
		int pixelSize = cm.getPixelSize();
		
		DataBuffer data = image.getData().getDataBuffer();
		
		for(int i = 0; i<width*height*pixelSize/8; i++ )
		{
			data.setElem(i, (byte)0);
		}
		
		WritableRaster wRaster = Raster.createPackedRaster(data, width, height, pixelSize, new Point(0,0));
		
		image.setData(wRaster);
		return image;
	}
	
	public static BufferedImage createEmptyImage(int width, int height, int pixelSize)
	{
		int numberOfColours = (int) java.lang.Math.pow(2, pixelSize);
		
		byte[] R = new byte[numberOfColours];
		byte[] G = new byte[numberOfColours];
		byte[] B = new byte[numberOfColours];
		
		R[0] = G[0] = B[0] = (byte)(255);
		
		IndexColorModel imagePalette = new IndexColorModel(pixelSize,numberOfColours,R,G,B);
		
		int type;
		if(pixelSize == 4) {
			type = BufferedImage.TYPE_BYTE_BINARY;
		} else {
			type = BufferedImage.TYPE_BYTE_INDEXED;
		}
		 
		return new BufferedImage(width,height,type, imagePalette);
	}
	
	public static BufferedImage makeImageBGTransparent(BufferedImage image) {
		IndexColorModel cm = (IndexColorModel)image.getColorModel();
		byte R[], G[], B[], A[];
		
		R = new byte[cm.getMapSize()];
		G = new byte[cm.getMapSize()];
		B = new byte[cm.getMapSize()];
		A = new byte[cm.getMapSize()];
		
		for(int i = 0; i<cm.getMapSize(); i++) {
			R[i] = (byte)cm.getRed(i);
			G[i] = (byte)cm.getGreen(i);
			B[i] = (byte)cm.getBlue(i);
			A[i] = (byte)255;
		}
		
		A[0] = (byte)0;
		
		int type;
		if(cm.getMapSize() == 16)
			type = BufferedImage.TYPE_BYTE_BINARY;
		else
			type = BufferedImage.TYPE_BYTE_INDEXED;
		
		IndexColorModel newcm = new IndexColorModel(cm.getPixelSize(), cm.getMapSize(), R, G, B, A);
		
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), type, newcm);
		
		newImage.setData(image.getData());
		
		return newImage;
	}
	
	public static BufferedImage makeImageBGOpaque(BufferedImage image)
	{
		IndexColorModel cm = (IndexColorModel)image.getColorModel();
		byte R[], G[], B[], A[];
		
		R = new byte[cm.getMapSize()];
		G = new byte[cm.getMapSize()];
		B = new byte[cm.getMapSize()];
		A = new byte[cm.getMapSize()];
		
		for(int i = 0; i<cm.getMapSize(); i++)
		{
			R[i] = (byte)cm.getRed(i);
			G[i] = (byte)cm.getGreen(i);
			B[i] = (byte)cm.getBlue(i);
			A[i] = (byte)255;
		}
		
		int type;
		if(cm.getMapSize() == 16)
			type = BufferedImage.TYPE_BYTE_BINARY;
		else
			type = BufferedImage.TYPE_BYTE_INDEXED;
		
		IndexColorModel newcm = new IndexColorModel(cm.getPixelSize(), cm.getMapSize(), R, G, B, A);
		
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), type, newcm);
		
		newImage.setData(image.getData());
		
		return newImage;
	}
	
	public static WritableRaster imageShiftRight(BufferedImage image)
	{
		int width,height, rgbValCurrent = 0, rgbValNext = 0;
		width = image.getWidth();
		height = image.getHeight();
		
		for(int i = 0;i<height;i++)
		{
			int idx = 1;
			rgbValCurrent = image.getRGB(0, i);
			for(int j = 0; j<width; j++, idx++)
			{
				if(j == width - 1)
					idx = 0;
				rgbValNext = image.getRGB(idx, i);
				image.setRGB(idx, i, rgbValCurrent);
				rgbValCurrent = rgbValNext; 
			}	
		}
		return image.getRaster();
	}
	
	public static WritableRaster imageShiftLeft(BufferedImage image)
	{
		int width,height, rgbValCurrent = 0, rgbValNext = 0;
		width = image.getWidth();
		height = image.getHeight();
		
		for(int i = 0;i<height;i++)
		{
			int idx = width - 2;
			rgbValCurrent = image.getRGB(width - 1, i);
			for(int j = width - 1; j>=0; j--, idx--)
			{
				if(j == 0)
					idx = width - 1;
				rgbValNext = image.getRGB(idx, i);
				image.setRGB(idx, i, rgbValCurrent);
				rgbValCurrent = rgbValNext; 
			}	
		}
		return image.getRaster();
	}
	
	public static WritableRaster imageShiftUp(BufferedImage image)
	{
		int width,height, rgbValCurrent = 0, rgbValNext = 0;
		width = image.getWidth();
		height = image.getHeight();
		
		for(int i = 0;i<width;i++)
		{
			int idx = height - 2;
			rgbValCurrent = image.getRGB(i, height - 1);
			for(int j = height - 1; j>=0; j--, idx--)
			{
				if(j == 0)
					idx = height - 1;
				rgbValNext = image.getRGB(i, idx);
				image.setRGB(i, idx, rgbValCurrent);
				rgbValCurrent = rgbValNext; 
			}	
		}
		return image.getRaster();
	}
	
	public static WritableRaster imageShiftDown(BufferedImage image)
	{
		int width,height, rgbValCurrent = 0, rgbValNext = 0;
		width = image.getWidth();
		height = image.getHeight();
		
		for(int i = 0;i<width;i++)
		{
			int idx = 1;
			rgbValCurrent = image.getRGB(i, 0);
			for(int j = 0; j<height; j++, idx++)
			{
				if(j == height - 1)
					idx = 0;
				rgbValNext = image.getRGB(i, idx);
				image.setRGB(i, idx, rgbValCurrent);
				rgbValCurrent = rgbValNext; 
			}	
		}
		return image.getRaster();
	}
	
	public static WritableRaster imageFlipHorizontal(BufferedImage image) {
		int width,height, rgbVal = 0;
		width = image.getWidth();
		height = image.getHeight();
		
		for(int i = 0;i<height;i++)
		{
			for(int j = 0; j<width/2; j++)
			{
				rgbVal = image.getRGB((width - 1) - j, i);
				image.setRGB((width - 1) - j, i, image.getRGB(j, i));
				image.setRGB(j, i, rgbVal);
			}	
		}
		return image.getRaster();
	}
	
	public static WritableRaster imageFlipVertical(BufferedImage image)
	{
		int width,height, rgbVal = 0;
		width = image.getWidth();
		height = image.getHeight();
		
		for(int i = 0;i<width;i++)
		{
			for(int j = 0; j<height/2; j++)
			{
				rgbVal = image.getRGB(i, (height - 1) - j);
				image.setRGB(i, (height - 1) - j, image.getRGB(i, j));
				image.setRGB(i, j, rgbVal);
			}	
		}
		return image.getRaster();
	}
	
	public static BufferedImage createMottledTile(int imageWidth, int imageHeight, int tileWidth, int tileHeight)
	{
		byte R[], G[], B[];
		
		R = new byte[16];
		G = new byte[16];
		B = new byte[16];
		
		for(int i = 0; i<16; i++)
		{
		    R[i] = (byte) 255;
		    G[i] = (byte) 255;
		    B[i] = (byte) 255;
		}
		
		 R[1] = (byte) 128;
		 G[1] = (byte) 128;
		 B[1] = (byte) 128;
		IndexColorModel cm = new IndexColorModel(4, 16, R,G,B);
		
		if(imageWidth<tileWidth)
			tileWidth = imageWidth;
		
		if(imageHeight<tileHeight)
			tileHeight = imageHeight;
		
		BufferedImage mottledImage = new BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_BYTE_BINARY, cm);
		BufferedImage tileMottle = new BufferedImage(tileWidth,tileHeight,BufferedImage.TYPE_BYTE_BINARY, cm);
		
		int RGBMottle = cm.getRGB(1);
		
		for(int i = 0; i<tileWidth; i++)
		{
		    for(int j = 0; j<tileHeight; j++)
		    {
		    	tileMottle.setRGB(i, j, RGBMottle);
		    }
		}

		int ARGB[] = new int[tileWidth*tileHeight];
		tileMottle.getRGB(0, 0, tileWidth, tileHeight, ARGB, 0, tileWidth);
		boolean hPreviousW = false, vPreviousW = false; 
		
		for(int j = 0; j<imageHeight; j+=tileHeight)
		{
		    if(vPreviousW == true)
		    {
		    	hPreviousW = true;
		    	vPreviousW = false;
		    }
		    else
		    {
		    	hPreviousW = false;
		    	vPreviousW = true;
		    }
		    
		    for(int i = 0;i<imageWidth;i+=tileWidth)
		    {
		    	if(hPreviousW == true)
		    	{
		    		int widthOver = 0, HeightOver = 0;
		    		if(i + tileWidth>imageWidth)
		    			widthOver = i + tileWidth - imageWidth;
		    		if(j + tileHeight>imageHeight)
		    			HeightOver = j + tileHeight - imageHeight;
		    		mottledImage.setRGB( i, j, tileWidth - widthOver, tileHeight - HeightOver, ARGB, 0, tileWidth);
		    		hPreviousW = false;
		    	}
		    	else
		    		hPreviousW = true;
		    }
		}	
		
		return mottledImage;
	}
	
	public static BufferedImage createMottledPixels(int w, int h)
	{
		byte R[], G[], B[];
		
		R = new byte[16];
		G = new byte[16];
		B = new byte[16];
		
		for(int i = 0; i<16; i++)
		{
		    R[i] = (byte) 255;
		    G[i] = (byte) 255;
		    B[i] = (byte) 255;
		}
		
		 R[1] = (byte) 128;
		 G[1] = (byte) 128;
		 B[1] = (byte) 128;
		IndexColorModel cm = new IndexColorModel(4, 16, R,G,B);
			
		BufferedImage mottledImage = new BufferedImage(w,h,BufferedImage.TYPE_BYTE_BINARY, cm);
		
		int RGBMottle = cm.getRGB(1);
		
		boolean hPreviousW = false, vPreviousW = false; 
		
		for(int j = 0; j<h; j++)
		{
		    if(vPreviousW == true)
		    {
			hPreviousW = true;
			vPreviousW = false;
		    }
		    else
		    {
			hPreviousW = false;
			vPreviousW = true;
		    }
		    
		    for(int i = 0;i<w;i++)
		    {
			if(hPreviousW == true)
			{
			    mottledImage.setRGB( i, j, RGBMottle);
			    hPreviousW = false;
			}
			else
			    hPreviousW = true;
		
		    }
		}	
		
		return mottledImage;
	}
	
	public static BufferedImage[] createMultiImages(int w, int h, int frames,IndexColorModel cm, byte[] data)
	{
		BufferedImage[] multiImage = new BufferedImage[frames];
		int pixelSize = cm.getPixelSize();
		int sizeInBytes = w*h*pixelSize/8;
		
		for(int i = 0;i<frames;i++)
		{
			byte[] imageData = new byte[sizeInBytes];
			for(int i2 = i*sizeInBytes, i3 = 0; i2<(i+1)*sizeInBytes;i2++, i3++)
			{
				imageData[i3] = data[i2];
			}
			multiImage[i] = ImageTools.createBufferedImage(w, h, cm, imageData);	
		}
		return multiImage;
	}
	
	public static BufferedImage copyImage(BufferedImage img) {
		BufferedImage cpyImg = new BufferedImage(img.getWidth(), img.getHeight(),
				img.getType(), 
				(IndexColorModel) copyColorModel(
				(IndexColorModel) img.getColorModel()));
		cpyImg.setData(img.getRaster());
		
		return cpyImg;
	}
	
	public static BufferedImage copyImage(BufferedImage img, IndexColorModel cm) {
		BufferedImage cpyImg = new BufferedImage(img.getWidth(), img.getHeight(),
				img.getType(), (IndexColorModel) copyColorModel(cm));
		cpyImg.setData(img.getRaster());
		
		return cpyImg;
	}
	
	public static IndexColorModel copyColorModel(IndexColorModel cm) {
		int numColors = cm.getMapSize();
		int pixelSize = cm.getPixelSize();
		byte red[] = new byte[numColors],
			green[] = new byte[numColors],
			blue[] = new byte[numColors],
			alpha[] = new byte[numColors];
		cm.getReds(red);
		cm.getGreens(green);
		cm.getBlues(blue);
		cm.getAlphas(alpha);
		
		return new IndexColorModel(pixelSize,numColors,red, green, blue, alpha);
	}
}