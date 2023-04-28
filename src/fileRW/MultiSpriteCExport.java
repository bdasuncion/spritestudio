package fileRW;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import infoObjects.MultiSpriteAnimation;
import infoObjects.SpriteFrame;
import infoObjects.SpriteFrameSet;
import tools.ImageTools;

public class MultiSpriteCExport {
	
	private static final String DO_HFLIP = "DO_HFLIP";
	private static final String NO_HFLIP = "NO_HFLIP";
	
	private static final String DO_VFLIP = "DO_VFLIP";
	private static final String NO_VFLIP = "NO_VFLIP";
	
	private static final String NO_COMPRESSION = "NO_COMPRESSION";
	private static final String LZSS_COMPRESSION = "LZSS_COMPRESSION";
	
	private static final String SMALL_A = "SMALL_A";
	private static final String SMALL_B = "SMALL_B";
	private static final String MEDIUM = "MEDIUM";
	private static final String LARGE = "LARGE";
	
	private static final String SQUARE = "SQUARE";
	private static final String WIDE = "WIDE";
	private static final String TALL = "TALL";
	
    public MultiSpriteCExport() {
		// TODO Auto-generated constructor stub
	}
    
    public void write(File outputFile, String baseName, MultiSpriteAnimation multiSpriteAnimation, boolean compressFiles) {
    	Collection<String> cBaseImageFiles = multiSpriteAnimation.getSourceFiles();
    	
    	Map<String, IndexColorModel> palleteMap = new HashMap<String, IndexColorModel>();
    	
    	Vector<IndexColorModel> allPalletes = new Vector<IndexColorModel>();
    	
    	for(String baseImageFile : cBaseImageFiles) {
    		File sourceFile = new File(outputFile.getParent() + "\\" + baseImageFile);
            
            SpriteStudioFileReader ssfr = new SpriteStudioFileReader(sourceFile);
            BufferedImage[] images = ImageTools.createMultiImages(ssfr.getWidth(), ssfr.getHeight(), ssfr.getNumOfFrames(), 
           		 ssfr.getPalette(), ssfr.getData());
            String cSpriteBaseName = baseImageFile.substring(0, baseImageFile.lastIndexOf("."));
            String cFileName = "sprite_" + cSpriteBaseName + ".thumb.c";
            
            File cImageFile = new File(outputFile.getParent() + "\\" + cFileName);
            //System.out.println(cImageFile.getName());
            if (compressFiles) {
            	ExportToC.expToCGBA1DLZSSFrame(cImageFile, cSpriteBaseName, images, ssfr.getPalette());
            } else {
            	ExportToC.exportToCGBA1DPerFrame(cImageFile, cSpriteBaseName, images, ssfr.getPalette());
            }
            palleteMap.put(cSpriteBaseName + "_pal", ssfr.getPalette());
            allPalletes.add(ssfr.getPalette());
            File hImageFile  = new File(outputFile.getParent() + "\\" + "sprite_" + cSpriteBaseName + ".h");
            ExportToC.expToHFileFrame(hImageFile, cSpriteBaseName, images);
    	}
    	
    	List<IndexColorModel> uniquePalleteArray = new ArrayList<IndexColorModel>();
 
    	collectUniquePallette(allPalletes, uniquePalleteArray);
    	
    	//System.out.print("UNIQUE PALLETE COUNT:" + uniquePalleteArray.size() + "\n");
    	Map<String, Integer> indexPalletteMap = new HashMap<String, Integer>();

    	mapUniquePalletteToIndex(palleteMap, uniquePalleteArray, indexPalletteMap);
    	try {
    		FileWriter cFile = new FileWriter(outputFile);
	    	writeSpriteSet(cFile, baseName, multiSpriteAnimation, indexPalletteMap, compressFiles);
	    	
	      	cFile.flush();
	    	cFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} 
    }
    
    private void mapUniquePalletteToIndex(Map<String, IndexColorModel> palleteMap,
    		List<IndexColorModel> uniquePalleteArray, Map<String, Integer> indexPalletteMap) {
    	for (String palletteName :palleteMap.keySet()) {
    		IndexColorModel pallette = palleteMap.get(palletteName);
    		
    		for (int idx = 0; idx < uniquePalleteArray.size(); idx++) {
    			if (!isDifferentPalletes(pallette, uniquePalleteArray.get(idx))) {
    				indexPalletteMap.put(palletteName, idx);
    				break;
    			}
    		}
    	}
    	
    }
    private void collectUniquePallette (Vector<IndexColorModel> allPalletes,
    		List<IndexColorModel> uniquePalleteArray) {
    	for (IndexColorModel pallette : allPalletes) {
    		if (uniquePalleteArray.size() == 0) {
    			uniquePalleteArray.add(pallette);
    		} else {
    			boolean addPallette = true;
	    		for (IndexColorModel uniquePallete : uniquePalleteArray) {
	    			boolean hasDifference = isDifferentPalletes(uniquePallete, pallette);
	    			
	    			if (!hasDifference) {
	    				addPallette = false;
	    				break;
	    			}
	    		}
	    		
	    		if (addPallette) {
	    			uniquePalleteArray.add(pallette);
	    		}
    		}
    	}
    }
    
    private boolean isDifferentPalletes(IndexColorModel firstPal, IndexColorModel secondPal) {
    	int[] rgb = new int[firstPal.getMapSize()];
		firstPal.getRGBs(rgb);
		
		int[] checkRgb = new int[secondPal.getMapSize()];
		secondPal.getRGBs(checkRgb);
		
    	for (int idx = 0; idx < rgb.length; ++idx) {
			if (rgb[idx] != checkRgb[idx]) {
				return true;
			}
		}
    	
    	return false;
    }
    
    private void writeSpriteSet(FileWriter fileOut, String baseName, MultiSpriteAnimation multiSpriteAnimation,
    		Map<String, Integer> indexPalletteMap, boolean compressFiles) throws IOException {
    	int count = 0;
    	Vector<String> spriteSetNames = new Vector<String>();
    	for (SpriteFrameSet spriteFrameSet : multiSpriteAnimation.getAnimation()) {
    		spriteSetNames.add(writeSpriteLayerSet(fileOut, baseName, count, 
    				spriteFrameSet, indexPalletteMap, compressFiles));
    		++count;
    	}
    	
    	int idx = 0;
    	String setName = baseName + "_layerSet";
    	fileOut.write("\nconst SpriteLayerSet " + setName + "[]" + " = {\n");
    	for (SpriteFrameSet spriteFrameSet : multiSpriteAnimation.getAnimation()) {
    		fileOut.write("    {" + spriteSetNames.get(idx) + "," + spriteFrameSet.getDisplayForNumberOfFrames() +
    				 "," + spriteFrameSet.getSpriteFrames().size() + "},\n");
    		++idx;
        }
    	fileOut.write("};\n");
    	
    	fileOut.write("\nconst SpriteSet " + baseName + " = {" + 
    	    setName + "," + multiSpriteAnimation.getAnimation().size() + "};\n");
    }
    
    private String writeSpriteLayerSet(FileWriter fileOut, String baseName, int count, 
    		SpriteFrameSet spriteFrameSet, Map<String, Integer> indexPalletteMap, boolean compressFiles) throws IOException {
    	
    	Vector<SpriteFrame> spriteFrames = spriteFrameSet.getSpriteFrames();
    	int spriteOffset = 0;
    	int paletteOffset = 0;
    	
    	String frameName = baseName + "_layerSet" + count;
    	
    	fileOut.write("const SpriteLayer " + frameName + "[]" + " = {\n");
    	for(int idx = spriteFrames.size() - 1; idx >= 0; --idx) {
    		spriteOffset = writeSpriteLayer(fileOut, spriteFrames.get(idx), spriteOffset, indexPalletteMap, compressFiles);
    		++paletteOffset;
    		if (idx > 0) {
    			fileOut.write(",\n");
    		}
    	}
    	fileOut.write("};\n");
    	return frameName;
    }
    
    private int writeSpriteLayer(FileWriter fileOut, SpriteFrame spriteFrame, int spriteOffset, 
    		Map<String, Integer> indexPalletteMap, boolean compressFiles) throws IOException {
    	
    	fileOut.write("    {");
    	
    	fileOut.write(spriteFrame.getCompressedFrameName() + ",");
    	//
    	String palleteName = spriteFrame.getCompressedFrameName();
    	palleteName = palleteName.substring(0, palleteName.lastIndexOf("_")) + "_pal";
    	//fileOut.write(palleteName + "_pal" + ",");
    	//
    	fileOut.write(spriteFrame.getXOffset() + ",");
    	fileOut.write(spriteFrame.getYOffset() + ",");
    	
    	int width = spriteFrame.getImage().getWidth();
    	int height = spriteFrame.getImage().getHeight();
    	
    	//System.out.println(width + "x" + height);
    	int shorterSide = 0;
    	int longerSide = 0;
    	if (height > width) {
    		fileOut.write(TALL + ",");
    		shorterSide = width;
    	    longerSide = height;
    	} else if (width > height) {
    		fileOut.write(WIDE + ",");
    		shorterSide = height;
    		longerSide = width;
    	} else {
    		fileOut.write(SQUARE + ",");
    		shorterSide = height;
    		longerSide = width;
    	}
    	writeSize(fileOut, shorterSide, longerSide);
    	
    	fileOut.write((spriteFrame.isFlippedHorizontal() ? DO_HFLIP : NO_HFLIP) + ",");
    	fileOut.write((spriteFrame.isFlippedVertical() ? DO_VFLIP : NO_VFLIP) + ",");
    	
    	fileOut.write((compressFiles ? LZSS_COMPRESSION : NO_COMPRESSION) + ",");
    	
    	fileOut.write(spriteOffset + ",");
    	//System.out.print("PALETTE NAMEXX:" + palleteName + "\n");
    	//System.out.print("PALETTE IDX:" + indexPalletteMap.get(palleteName)  + "\n");
    	fileOut.write((indexPalletteMap.get(palleteName) != null ? indexPalletteMap.get(palleteName) : 0) + "}");
    	return (width * height)/64;
    }
    
    private void writeSize(FileWriter fileOut, int shorterSide, int longerSide) throws IOException {
    	if (shorterSide == longerSide) {
    		if (shorterSide == 8)
    			fileOut.write(SMALL_A + ",");
    		else if(shorterSide == 16) 
    			fileOut.write(SMALL_B + ",");
    		else if (shorterSide == 32) 
    			fileOut.write(MEDIUM + ",");
    		else
    			fileOut.write(LARGE + ",");
    	} else {
    		if (shorterSide == 8 && longerSide == 16)
    			fileOut.write(SMALL_A + ",");
    		else if (shorterSide == 8 && longerSide == 32)
    			fileOut.write(SMALL_B + ",");
    		else if (shorterSide == 16 && longerSide == 32)
    			fileOut.write(MEDIUM + ",");
    		else 
    			fileOut.write(LARGE + ",");
    	}
    }
}
