package fileRW;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;


public class ExportToC
{
	private static String convert[] = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};

	public static void exportToCGBA1D(File f, BufferedImage[] buffer, IndexColorModel cm)
	{
		FileWriter cFile = null;
		File outFile = new File(f.getParent() + "\\" + appendExtensionC(f.getName()));
		try {
			cFile = new FileWriter(outFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		createPalette16BitGBA(f,cm, cFile);
		
		int width = buffer[0].getWidth();
		int height = buffer[0].getHeight();
		try {
			cFile.write("const unsigned int " + getFileName(f) + "_image[" 
					+ (width*height*buffer.length/(8/cm.getPixelSize()))/4 + "] = \n{\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i<buffer.length;i++)
		{
			boolean isLast = true;
			if(i<buffer.length - 1)
				isLast = false;
			if(cm.getPixelSize() == 4)
				U324BPP1DGBA(buffer[i].getRaster().getDataBuffer(), cFile, buffer[i].getWidth(), buffer[i].getHeight(), isLast);
			
			if(i < buffer.length - 1)
				try {
					cFile.write("\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		try {
			cFile.write("};\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			cFile.flush();
			cFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void exportToCGBA1DPerFrame(File f, String baseName, BufferedImage[] buffer, IndexColorModel cm)
	{
		FileWriter cFile = null;
		File outFile = new File(f.getParent() + "\\" + appendExtensionC(f.getName()));
		try {
			cFile = new FileWriter(outFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//createPalette16BitGBA(f,cm, cFile);
		createPalette16BitGBA(f, baseName, cm, cFile);
		
		int width = buffer[0].getWidth();
		int height = buffer[0].getHeight();
		
		for(int i = 0; i<buffer.length;i++) {
			try {
				cFile.write("const unsigned int " + baseName + "_image" + i + "["
						+ ((width*height)/(8/cm.getPixelSize()))/4 + "] = \n{\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		
			boolean isLast = true;
			if(i<buffer.length - 1)
				isLast = false;
			if(cm.getPixelSize() == 4)
				U324BPP1DGBA(buffer[i].getRaster().getDataBuffer(), cFile, buffer[i].getWidth(), buffer[i].getHeight(), isLast);
			
			if(i < buffer.length - 1) {
				try {
					cFile.write("\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			try {
				cFile.write("};\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		try {
			cFile.flush();
			cFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void exportToCGBA2D(File f, BufferedImage[] buffer, IndexColorModel cm)
	{
		FileWriter cFile = null;
		File outFile = new File(f.getParent() + "\\" + appendExtensionC(f.getName()));
		try {
			cFile = new FileWriter(outFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		createPalette16BitGBA(f,cm, cFile);
		
		int width = buffer[0].getWidth();
		int height = buffer[0].getHeight();
		try {
			cFile.write("const unsigned int " + getFileName(f) + "_image[" 
					+ (width*height*buffer.length/(8/cm.getPixelSize()))/4 + "] = \n{\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i<buffer.length;i++)
		{
//			boolean isLast = true;
//			if(i<buffer.length - 1)
//				isLast = false;
			if(cm.getPixelSize() == 4)
				U324BPP2DGBA(buffer[i].getRaster().getDataBuffer(), cFile);
			
			if(i < buffer.length - 1)
				try {
					cFile.write("\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		try {
			cFile.write("};\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			cFile.flush();
			cFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void exportToCGBA1DLZSS(File f, BufferedImage[] buffer, IndexColorModel cm)
	{
		Vector<Integer> convertedSprite = new Vector<Integer>();
		FileWriter cFile = null;
		File outFile = new File(f.getParent() + "\\" + appendExtensionC(f.getName()));
		try {
			cFile = new FileWriter(outFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		createPalette16BitGBA(f,cm, cFile);
	
		for(int i = 0; i<buffer.length;i++)
		{
			if(cm.getPixelSize() == 4)
				convertTo8x8(buffer[i].getRaster().getDataBuffer(), 
					buffer[i].getWidth(), buffer[i].getHeight(), 
					convertedSprite);
		}
		
		Vector<Integer> compressedSprite = new Vector<Integer>();

		compressLZSS(convertedSprite, compressedSprite);

		try {
			cFile.write("const unsigned int " + getFileName(f) + "_image[" 
					+ compressedSprite.size()/4 + "] = \n{\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		writeToCFileLZSS(compressedSprite, cFile);
		
		try {
			cFile.write("};\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			cFile.flush();
			cFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void expToCGBA1DLZSSFrame(File f, String baseName, BufferedImage[] buffer, IndexColorModel cm)
	{
		FileWriter cFile = null;
		File outFile = new File(f.getParent() + "\\" + appendExtensionC(f.getName()));
		try {
			cFile = new FileWriter(outFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		createPalette16BitGBA(f, baseName, cm, cFile);
		
		for(int i = 0; i<buffer.length;i++) {
			Vector<Integer> convertedSprite = new Vector<Integer>();
			if(cm.getPixelSize() == 4)
				convertTo8x8(buffer[i].getRaster().getDataBuffer(), 
					buffer[i].getWidth(), buffer[i].getHeight(), 
					convertedSprite);
			Vector<Integer> sprite16bit = new Vector<Integer>();
			Vector<Integer> compressedSprite = new Vector<Integer>();
			Vector<Integer> sizeData = new Vector<Integer>();
			Vector<Integer> compressedConverted = new Vector<Integer>();
			//if(i == buffer.length - 1)
			{
				//compressLZSS(convertedSprite, compressedSprite);
				convertTo16bit(convertedSprite, sprite16bit);
				compressLZSS(sprite16bit, compressedSprite, sizeData);
				convertTo8bit(compressedSprite, sizeData, compressedConverted);
			
				System.out.println(baseName);
				try {
					//cFile.write("const unsigned int " + getFileName(f) + "_image" + i 
					cFile.write("const unsigned int " + baseName + "_image" + i 
							+ "[" + compressedConverted.size()/4 + "] = \n{\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//writeToCFileLZSS(compressedSprite, cFile);
				writeToCFileLZSS(compressedConverted, cFile);
				
				try {
					cFile.write("};\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
				
		try {
			cFile.flush();
			cFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void expToHFileFrame(File f, String baseName, BufferedImage[] buffer) {
		FileWriter cFile = null;
		File outFile = new File(f.getParent() + "\\" + f.getName());
		try {
			cFile = new FileWriter(outFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			cFile.write("extern const unsigned short " + baseName + "_pal[];\n");
			for (int i = 0; i < buffer.length; ++i) {
				cFile.write("extern const unsigned int " + baseName + "_image" + i + "[];\n");
			}
			cFile.flush();
			cFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
/*	public static void exportToCGBA1DLZSSHW(File f, BufferedImage[] buffer, IndexColorModel cm)
	{
		Vector<Integer> convertedSprite = new Vector<Integer>();
		FileWriter cFile = null;
		File outFile = new File(f.getParent() + "\\" + appendExtensionC(f.getName()));
		try {
			cFile = new FileWriter(outFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		createPalette16BitGBA(f,cm, cFile);
		
		for(int i = 0; i<buffer.length;i++)
		{
			if(cm.getPixelSize() == 4)
				convertTo8x8(buffer[i].getRaster().getDataBuffer(), 
					buffer[i].getWidth(), buffer[i].getHeight(), 
					convertedSprite);
		}
		
		Vector<Integer> compressedSprite = new Vector<Integer>();

		compressLZSSHW(convertedSprite, compressedSprite);
	
		try {
			cFile.write("const unsigned int " + getFileName(f) + "_image[" 
					+ compressedSprite.size()/4 + "] = \n{\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		writeToCFileLZSS(compressedSprite, cFile);

		try {
			cFile.write("};\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			cFile.flush();
			cFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void expToCGBA1DLZSSHWFrame(File f, BufferedImage[] buffer, IndexColorModel cm)
	{
		FileWriter cFile = null;
		File outFile = new File(f.getParent() + "\\" + appendExtensionC(f.getName()));
		try {
			cFile = new FileWriter(outFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		createPalette16BitGBA(f,cm, cFile);
		
		for(int i = 0; i<buffer.length;i++)
		{
			Vector<Integer> convertedSprite = new Vector<Integer>();
			if(cm.getPixelSize() == 4)
				convertTo8x8(buffer[i].getRaster().getDataBuffer(), 
					buffer[i].getWidth(), buffer[i].getHeight(), 
					convertedSprite);
			Vector<Integer> compressedSprite = new Vector<Integer>();

			compressLZSSHW(convertedSprite, compressedSprite);
			
			try {
				cFile.write("const unsigned int " + getFileName(f) + "_image" + i 
						+ "[" + compressedSprite.size()/4 + "] = \n{\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			writeToCFileLZSS(compressedSprite, cFile);
			
			try {
				cFile.write("};\n");
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
				
		try {
			cFile.flush();
			cFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
	private static void createPalette16BitGBA(File f,IndexColorModel cm, FileWriter cFile) {
		createPalette16BitGBA(f, f.getName(), cm, cFile);
	}
	
	private static void createPalette16BitGBA(File f, String baseName, IndexColorModel cm, FileWriter cFile)
	{
		try {
			//cFile.write("const unsigned short " + getFileName(f) + "_pal[16] = \n{\n");
			cFile.write("const unsigned short " + baseName + "_pal[16] = \n{\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i<cm.getMapSize(); i++) {
			int RVal16 = (cm.getRed(i)/8);
			int GVal16 = (cm.getGreen(i)/8)<<5;
			int BVal16 = (cm.getBlue(i)/8)<<10;
			int RGBVal16 = RVal16 | GVal16 | BVal16;
			if((i)%8 == 0)
				try {
					cFile.write("\t");
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			try {
				cFile.write("0x");
				cFile.write(convertByteToHexa2((RGBVal16>>8)&0xff));
				cFile.write(convertByteToHexa2(RGBVal16&0xff));
				cFile.write(",");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if((i+1)%8 == 0)
				try {
					cFile.write("\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		try {
			cFile.write("};\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//Export in a continuous array
	private static void U324BPP2DGBA(DataBuffer imageData, FileWriter cFile)
	{
		for(int i = 0; i<imageData.getSize(); i+=4)
		{
			if(i%32 == 0 && i>0)
				try {
					cFile.write("\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			if((i)%32 == 0)
				try {
					cFile.write("\t");
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			try {
				cFile.write("0x");
				cFile.write(convertByteToHexa(imageData.getElem(i)&0xff));
				cFile.write(convertByteToHexa(imageData.getElem(i+1)&0xff));
				cFile.write(convertByteToHexa(imageData.getElem(i+2)&0xff));
				cFile.write(convertByteToHexa(imageData.getElem(i+3)&0xff));
				cFile.write(",");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//Export to 8x8 tiles, 4 bits per pixel
	private static void U324BPP1DGBA(DataBuffer imageData, FileWriter cFile, int width, int height, boolean isLast) {
		int offset = 0;
		int i = 0, x = 0;
	
		for(int k = 0; k<height/8; k+=1) {
			offset = (width*4)*k;
			for(int j = 0; j<width/8; j++) {
				try {
					cFile.write("\t");
				} catch (IOException e) {
					e.printStackTrace();
				}
				for(i = offset, x = 0; x<8; i+=width/2, x++) {
					String hexa = convertByteToHexa((imageData.getElem(i+3)&0xff));
					hexa += convertByteToHexa((imageData.getElem(i+2)&0xff));
					hexa += convertByteToHexa((imageData.getElem(i+1)&0xff));
					hexa += convertByteToHexa((imageData.getElem(i)&0xff));
					try {
						cFile.write("0x");
						cFile.write(hexa);
						if(i < (width*height/2) - 4 || isLast == false) {
							cFile.write(",");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					cFile.write("\n");
				} 	catch (IOException e) {
					e.printStackTrace();
				}
				offset += 4;
			}
		}
	}
	
	//Export to 8x8 tiles, 4 bits per pixel
	//for GBA export only
	private static void convertTo8x8 (
		DataBuffer imageData, 
		int width, 
		int height, 
		Vector<Integer> convertedImage
	) {
		int offset = 0;
		int i = 0, x = 0;
	
		for(int k = 0; k<height/8; k+=1)
		{
			offset = (width*4)*k;
			for(int j = 0; j<width/8; j++)
			{
				for(i = offset, x = 0; x<8; i+=width/2, x++)
				{
					convertedImage.add(new Integer(imageData.getElem(i)&0xff));
					convertedImage.add(new Integer(imageData.getElem(i+1)&0xff));
					convertedImage.add(new Integer(imageData.getElem(i+2)&0xff));
					convertedImage.add(new Integer(imageData.getElem(i+3)&0xff));
				}
				offset += 4;
			}
		}
	}
	
	private static void convertTo16bit (
		Vector<Integer> originalImage, 
		Vector<Integer> convertedImage ) {
		for(int i = 0; i<originalImage.size(); i+=2) {
			Integer combine = 0;
			combine = originalImage.get(i) | originalImage.get(i + 1)<<8;
			convertedImage.add(combine);
		}
	}
	
	private static void convertTo8bit (
		Vector<Integer> originalData,
		Vector<Integer> sizeData,
		Vector<Integer> convertedData ) {
		for(int i = 0; i<originalData.size(); i++) {
			Integer size = sizeData.get(i);
			if(size == 1) {
				convertedData.add(originalData.get(i));
			} else {
				Integer first8 = originalData.get(i)&0xFF;
				Integer last8 = (originalData.get(i)>>8)&0xFF;
				convertedData.add(first8);
				convertedData.add(last8);
			}
		}
		
		while(convertedData.size()%4 != 0) {
			convertedData.add(new Integer(0));
		}
	}
	
	private static final int MIN_LENGTH_MATCH = 3;
	private static final int MAX_LENGTH_MATCH = 18;
	private static final int LOOKBACK_LENGTH = 4095;
//	8-bit aligned compression
	private static void compressLZSS(Vector<Integer> uncompressedData, Vector<Integer> compressedData) {
		int blockCode = 0, windowStart = 0;
		int blockNumber = 0, insertBlockCode = 0;
		int dataHeader = (uncompressedData.size() << 8)|(1<<4);
		compressedData.add(new Integer((dataHeader>>24)&0xFF));
		compressedData.add(new Integer((dataHeader>>16)&0xFF));
		compressedData.add(new Integer((dataHeader>>8)&0xFF));
		compressedData.add(new Integer((dataHeader)&0xFF));
		
		insertBlockCode = compressedData.size();

		for(int i = 0; i < uncompressedData.size(); i++) {
			if(i<3) {
				compressedData.add(uncompressedData.get(i));
				blockCode |= (0 << (7 - blockNumber));
				blockNumber++;
			} else {
				if(i <= LOOKBACK_LENGTH)
					windowStart = 0;
				else
					windowStart = i - LOOKBACK_LENGTH;
				
				int j = windowStart;
				for(j = windowStart; j<i; j++) {
					int byteVal = uncompressedData.get(i).intValue();
					int findMatch = uncompressedData.get(j).intValue();
					
					if(byteVal != findMatch)
						continue;

					int lengthMatch = 0;
					int windowLook = 0;
					boolean breakLoop = false;
					
					for(int lookForward = i; 
						lookForward < uncompressedData.size() && 
						lookForward < i + MAX_LENGTH_MATCH; 
						lookForward++, windowLook++)
					{	
						byteVal = uncompressedData.get(lookForward).intValue();
						findMatch = uncompressedData.get(j + windowLook).intValue();

						if(byteVal == findMatch)
							lengthMatch++;
						
						if((byteVal != findMatch || 
							j  + windowLook == i - 1 || 
							lookForward == uncompressedData.size() - 1) && 
							lengthMatch >= MIN_LENGTH_MATCH)
						{
							lengthMatch -= MIN_LENGTH_MATCH;
							int length = (lengthMatch) | (((i - j - 1)&0xF00)>>8)<<4;
							int displaceLSB = 0;
							displaceLSB = ((i - j - 1)&0xF0)>>4;
							displaceLSB |= ((i - j - 1)&0xF)<<4;

							compressedData.add(new Integer((length)&0xFF));
							compressedData.add(new Integer(displaceLSB&0xFF));

							if(byteVal != findMatch)
								i = lookForward - 1;
							else
								i = lookForward;
							
							blockCode |= (1 << (7 - blockNumber));
							blockNumber++;
							breakLoop = true;
							break;
						}
						else if(byteVal != findMatch && lengthMatch < MIN_LENGTH_MATCH) {
							break;
						}
						if (lengthMatch == MAX_LENGTH_MATCH) {
							lengthMatch -= MIN_LENGTH_MATCH;

							int length = (lengthMatch) | (((i - j - 1)&0xF00)>>8)<<4;
							int displaceLSB = 0;
							displaceLSB = ((i - j - 1)&0xF0)>>4;
							displaceLSB |= ((i - j - 1)&0xF)<<4;

							compressedData.add(new Integer((length)&0xFF));
							compressedData.add(new Integer(displaceLSB&0xFF));
							
							i = lookForward;
							blockCode |= (1 << (7 - blockNumber));
							blockNumber++;
							breakLoop = true;
							break;
						}
						if(blockNumber>7 || lookForward + 1 == uncompressedData.size()) {
							int block = 0;
							block = ((blockCode)&0xF0)>>4;
							block |= ((blockCode)&0xF)<<4;
							compressedData.add(insertBlockCode,block);
							insertBlockCode = compressedData.size();
							blockCode = 0;
							blockNumber = 0;
						}				
					}
					if(breakLoop == true)
						break;
				}

				if(j == i) {
					compressedData.add(uncompressedData.get(i));
					blockCode |= (0 << (7 - blockNumber));
					blockNumber++;					
				}
				if(blockNumber>7 || i + 1 == uncompressedData.size()) {
					int block = 0;
					block = ((blockCode)&0xF0)>>4;
					block |= ((blockCode)&0xF)<<4;
					compressedData.add(insertBlockCode,block);
					insertBlockCode = compressedData.size();
					blockCode = 0;
					blockNumber = 0;
				}	
			}
		}
		
		while(compressedData.size()%4 != 0) {
			compressedData.add(new Integer(0));
		}
	}
	//16-bit aligned compression
	private static void compressLZSS(Vector<Integer> uncompressedData, Vector<Integer> compressedData,
			Vector<Integer> sizeData) {
		int blockCode = 0, windowStart = 0;
		int blockNumber = 0, insertBlockCode = 0;
		int dataHeader = (uncompressedData.size() << 8)|(1<<4);
		compressedData.add(new Integer((dataHeader>>24)&0xFF));
		compressedData.add(new Integer((dataHeader>>16)&0xFF));
		compressedData.add(new Integer((dataHeader>>8)&0xFF));
		compressedData.add(new Integer((dataHeader)&0xFF));
		
		sizeData.add(new Integer(1));
		sizeData.add(new Integer(1));
		sizeData.add(new Integer(1));
		sizeData.add(new Integer(1));
		
		insertBlockCode = compressedData.size();

		for(int i = 0; i < uncompressedData.size(); i++) {
			if(i<3) {
				compressedData.add(uncompressedData.get(i));
				sizeData.add(new Integer(2));
				blockCode |= (0 << blockNumber);
				blockNumber++;
			} else {
				if(i <= LOOKBACK_LENGTH)
					windowStart = 0;
				else
					windowStart = i - LOOKBACK_LENGTH;
				
				int j = windowStart;
				for(j = windowStart; j<i; j++) {
					int byteVal = uncompressedData.get(i).intValue();
					int findMatch = uncompressedData.get(j).intValue();
					
					if(byteVal != findMatch)
						continue;

					int lengthMatch = 0;
					int windowLook = 0;
					boolean breakLoop = false;
					
					for(int lookForward = i; 
						lookForward < uncompressedData.size() && 
						lookForward < i + MAX_LENGTH_MATCH; 
						lookForward++, windowLook++) {	
						byteVal = uncompressedData.get(lookForward).intValue();
						findMatch = uncompressedData.get(j + windowLook).intValue();

						if(byteVal == findMatch)
							lengthMatch++;
						
						if((byteVal != findMatch || 
							j  + windowLook == i - 1 || 
							lookForward == uncompressedData.size() - 1) && 
							lengthMatch >= MIN_LENGTH_MATCH) {
							lengthMatch -= MIN_LENGTH_MATCH;
							
							//int length = (lengthMatch) | (((i - j - 1)&0xF00)>>8)<<4;
							//int displaceLSB = 0;
							//displaceLSB = ((i - j - 1)&0xF0)>>4;
							//displaceLSB |= ((i - j - 1)&0xF)<<4;
							
							int length = (lengthMatch)<<4 | ((i - j - 1)&0xF);
							int displaceLSB = 0;
							displaceLSB = ((i - j - 1)&0xF00)>>8 | ((i - j - 1)&0xF0);

							compressedData.add(new Integer((length)&0xFF));
							compressedData.add(new Integer(displaceLSB&0xFF));
							sizeData.add(new Integer(1));
							sizeData.add(new Integer(1));

							if(byteVal != findMatch)
								i = lookForward - 1;
							else
								i = lookForward;
							
							blockCode |= (1 << blockNumber);
							blockNumber++;
							breakLoop = true;
							break;
						}
						else if(byteVal != findMatch && lengthMatch<3) {
							break;
						}
						if(lengthMatch == MAX_LENGTH_MATCH) {
							lengthMatch -= MIN_LENGTH_MATCH;

							//int length = (lengthMatch) | (((i - j - 1)&0xF00)>>8)<<4;
							//int displaceLSB = 0;
							//displaceLSB = ((i - j - 1)&0xF0)>>4;
							//displaceLSB |= ((i - j - 1)&0xF)<<4;
							
							int length = (lengthMatch)<<4 | ((i - j - 1)&0xF);
							int displaceLSB = 0;
							displaceLSB = ((i - j - 1)&0xFF0)>>8 | ((i - j - 1)&0xF0);

							compressedData.add(new Integer((length)&0xFF));
							compressedData.add(new Integer(displaceLSB&0xFF));
							sizeData.add(new Integer(1));
							sizeData.add(new Integer(1));

							i = lookForward;
							blockCode |= (1 << blockNumber);
							blockNumber++;
							breakLoop = true;
							break;
						}
					}
					if(breakLoop == true)
						break;
				}

				if(j == i) {
					compressedData.add(uncompressedData.get(i));
					sizeData.add(new Integer(2));
					blockCode |= (0 << blockNumber);
					blockNumber++;					
				}
				if(blockNumber>15 || i + 1 == uncompressedData.size()) {
					int block = 0;
					int blockA = 0, blockB = 0;
					blockA = ((blockCode)&0xF0)>>4;
					blockA |= ((blockCode)&0xF)<<4;
					blockB = ((blockCode>>8)&0xF0)>>4;
					blockB |= ((blockCode>>8)&0xF)<<4;
					block = blockA | blockB<<8;
					compressedData.add(insertBlockCode,block);
					sizeData.add(insertBlockCode,new Integer(2));
					insertBlockCode = compressedData.size();
					blockCode = 0;
					blockNumber = 0;
				}	
			}
		}
	}
	
	private static void writeToCFileLZSS ( Vector<Integer> compressedData, FileWriter cFile) {	
		try {
			cFile.write("\t");
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < compressedData.size(); i+=4) {
			String hexa;
			if(i != 0) {
				hexa = convertByteToHexa((compressedData.elementAt(i+3)&0xff));
				hexa += convertByteToHexa((compressedData.elementAt(i+2)&0xff));
				hexa += convertByteToHexa((compressedData.elementAt(i+1)&0xff));
				hexa += convertByteToHexa((compressedData.elementAt(i)&0xff));
			} else {
				hexa = convertByteToHexa2((compressedData.elementAt(i)&0xff));
				hexa += convertByteToHexa2((compressedData.elementAt(i+1)&0xff));
				hexa += convertByteToHexa2((compressedData.elementAt(i+2)&0xff));
				hexa += convertByteToHexa2((compressedData.elementAt(i+3)&0xff));				
			}

			try {
				cFile.write("0x");
				cFile.write(hexa);
				cFile.write(",");
				if(i%32 == 0)
					cFile.write("\n\t");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static String getFileName(File f) {
		String fileName = f.getName();
        return fileName.substring(0,fileName.length() - 2);
	}
	
	private static String appendExtensionC(String fileName) {
		if(fileName.length()<=2)
			return fileName + ".c";
	   String ext = fileName.substring(fileName.length() - 2).toLowerCase();
       
       if(ext.matches(".c") == true)
       	return fileName;

       return fileName + ".c";
	}
	
	private static String convertByteToHexa(int byteVal) {
		String hexaVal = convert[byteVal&0xf];
		hexaVal += convert[(byteVal>>4)&0xf];
		return hexaVal;
	}
	
	private static String convertByteToHexa2(int byteVal)
	{
		String hexaVal = convert[(byteVal>>4)&0xf];
		hexaVal += convert[byteVal&0xf];
		return hexaVal;
	}
}