package infoObjects;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.LookupOp;
import java.awt.image.Raster;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.util.Hashtable;

import tools.ImageTools;

public class SpriteFrame {
	private BufferedImage editableImage;
	private int xOffset;
	private int yOffset;
	private boolean flipVertical;
	private boolean flipHorizontal;
	private String srcFileName;
	private String compressedFrameName;
	private int copyImageWidth = 1, copyImageHeight = 1;
	private int stampImgWidth = 1, stampImgHeight = 1;
	int copied[];
	boolean isDrawMode = true;
	BufferedImage copiedImage;
	
	public SpriteFrame() {
		setImage(null);

		xOffset = 0;
		yOffset = 0;
		flipVertical = false;
		flipHorizontal = false;
		srcFileName = "";
		setCompressedFrameName("");
	}
	
	public SpriteFrame(BufferedImage image) {
		setImage(image);

		xOffset = 0;
		yOffset = 0;
		//drawPriority = 0;
		flipVertical = false;
		flipHorizontal = false;
		srcFileName = "";
		setCompressedFrameName("");
	}
	
	public SpriteFrame(SpriteFrame spriteFrame) {
		setImage(spriteFrame.getImage());
		setCompressedFrameName(spriteFrame.getCompressedFrameName());
        setSrcFileName(spriteFrame.getSrcFileName());
		xOffset = 0;
		yOffset = 0;
		//drawPriority = 0;
		flipVertical = false;
		flipHorizontal = false;
	}
	
	public BufferedImage getImage() {
		return editableImage;
	}

	public void setImage(BufferedImage img) {
		if (img != null) {
			this.editableImage = ImageTools.copyImage(img);
		}
	}
	
	public void updatePalette(IndexColorModel cm) {
		editableImage = ImageTools.copyImage(editableImage, cm);
	}
	
	public void updatePalette(int idxChanged, int RPal, int GPal, int BPal) {
		IndexColorModel cm = (IndexColorModel) editableImage.getColorModel();
		int numColors = cm.getMapSize();
		int pixelSize = cm.getPixelSize();
		byte red[] = new byte[numColors],
			green[] = new byte[numColors],
			blue[] = new byte[numColors];
		cm.getReds(red);
		cm.getGreens(green);
		cm.getBlues(blue);

		red[idxChanged] = (byte)RPal;
		green[idxChanged] = (byte)GPal;
		blue[idxChanged] = (byte)BPal;
		
		cm = new IndexColorModel(pixelSize,numColors,red, green, blue);
		updatePalette(cm);
	}
	
	public void updateAlpha(int aPal) {
		IndexColorModel cm = (IndexColorModel) editableImage.getColorModel();
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
		
		/*if (alpha[0] == 255) {
			alpha[0] = (byte)aPal;
		}*/
		
		for (int i = 1; i < numColors; ++i) {
			alpha[i] = (byte)aPal;
		}
		
		cm = new IndexColorModel(pixelSize,numColors,red, green, blue, alpha);
		updatePalette(cm);
	}
	
	public void updateImage(Raster r)
	{
		editableImage.setData(r);
	}
	
	public void updateImage(int[] ARGB)
	{
		editableImage.setRGB(0, 0, 
				editableImage.getWidth(), 
				editableImage.getHeight(), 
				ARGB, 0, editableImage.getWidth());
	}
	
	public IndexColorModel getImageColorModel()
	{
		return (IndexColorModel)editableImage.getColorModel();
	}
	
	public int getXOffset() {
		return xOffset;
	}

	public void setXOffset(int offset) {
		xOffset = offset;
	}

	public int getYOffset() {
		return yOffset;
	}

	public void setYOffset(int offset) {
		yOffset = offset;
	}

	/*public int getDrawPriority() {
		return drawPriority;
	}

	public void setDrawPriority(int drawPriority) {
		this.drawPriority = drawPriority;
	}*/
	
	public int getCopyBlockWidth() {
		return copyImageWidth;
	}
	
	public int getCopyBlockHeight() {
		return copyImageHeight;
	}

	public void setCopyImageWidth(int width) {
		this.copyImageWidth = width;
		copiedImage = null;
	}
	
	public void setCopyImageHeight(int height) {
		this.copyImageHeight = height;
		copiedImage = null;
	}
	
	public void copyAt(int x, int y) {
		stampImgWidth = copyImageWidth;
		if (x + copyImageWidth > editableImage.getWidth()) {
			stampImgWidth = editableImage.getWidth() - x;
		}
		stampImgHeight = copyImageHeight;
		if (y + copyImageHeight > editableImage.getHeight()) {
			stampImgHeight = editableImage.getHeight() - y;
		}
		copied = new int[editableImage.getWidth()*editableImage.getHeight()];
		editableImage.getRGB(x, y, stampImgWidth, stampImgHeight, copied, 0, stampImgWidth);
		copiedImage = ImageTools.createEmptyImage(stampImgWidth, stampImgHeight, (IndexColorModel) editableImage.getColorModel());
		copiedImage.setRGB(0, 0, stampImgWidth, stampImgHeight, copied, 0, stampImgWidth);
	}
	
	public void copyModeDrawAt(int x, int y) {
		Graphics2D g = editableImage.createGraphics();
		g.drawImage(copiedImage, x, y, copiedImage.getWidth(), copiedImage.getHeight(), null);
		//editableImage.setRGB(x, y, stampImgWidth, stampImgHeight, copied, 0, stampImgWidth);
	}
	
	public void drawAt(int x, int y, int rgbVal) {
		if (isDrawMode) {
			drawModeDrawAt(x,y,rgbVal);
		} else if (copiedImage != null){
			copyModeDrawAt(x, y);
		}
		
	}
	
	private void drawModeDrawAt(int x, int y, int rgbVal) {
		//if (copyImageWidth == 1) {
		//	editableImage.setRGB(x, y, rgbVal);
		//} else {
		Graphics2D g = editableImage.createGraphics();
		g.setColor(new Color(rgbVal));
		//g.fillOval(x - radius/2, y - radius/2, radius, radius);
		g.fillRect(x, y, copyImageWidth, copyImageHeight);
		//}
	}
	
	public void setImageBGTransparent(boolean isTransparent) {
		if (isTransparent) {
			editableImage = ImageTools.makeImageBGTransparent(editableImage);	
		} else {
			editableImage = ImageTools.makeImageBGOpaque(editableImage);
		}
	}
	
	public void resizeImage(int newWidth, int newHeight) {
		/*if (width < editableImage.getWidth() || 
				height < editableImage.getHeight()) {
			return;
		}*/
		
		
		int currentWidth = editableImage.getWidth();
		int currentHeight = editableImage.getHeight();
		int[] rgbArray = new int[currentWidth*currentHeight];
		rgbArray = editableImage.getRGB(0, 0, currentWidth, currentHeight, 
				rgbArray, 0, currentWidth);
		
		BufferedImage resizedImage = new BufferedImage(newWidth, 
				newHeight,
				editableImage.getType(), 
				(IndexColorModel) ImageTools.copyColorModel(
				(IndexColorModel) editableImage.getColorModel()));
		
		//resizedImage.setRGB(0, 0, currentWidth, currentHeight, 
		//		rgbArray, 0, currentWidth);
		int copyWidth = newWidth > currentWidth ? currentWidth : newWidth;
		int copyHeight = newHeight > currentHeight ? currentHeight : newHeight;
		
		resizedImage.setRGB(0, 0, copyWidth, copyHeight, 
			rgbArray, 0, currentWidth);

		
		editableImage = resizedImage;
	}
	
	public void flipImageVertical(boolean flip) {
		flipVertical = flip;
	}
	
	public void flipImageHorizontal(boolean flip) {
		flipHorizontal = flip;
	}
	
	public boolean isFlippedVertical() {
		return flipVertical;
	}
	
	public boolean isFlippedHorizontal() {
		return flipHorizontal;
	}

	public String getSrcFileName() {
		return srcFileName;
	}

	public void setSrcFileName(String srcFileName) {
		this.srcFileName = srcFileName;
	}

	public String getCompressedFrameName() {
		return compressedFrameName;
	}

	public void setCompressedFrameName(String compressedFrameName) {
		this.compressedFrameName = compressedFrameName;
	}

	public boolean isDrawMode() {
		return isDrawMode;
	}

	public void setDrawMode(boolean isDrawMode) {
		this.isDrawMode = isDrawMode;
		copiedImage = null;
	}

	public BufferedImage getCopiedImage() {
		return copiedImage;
	}
	
}
