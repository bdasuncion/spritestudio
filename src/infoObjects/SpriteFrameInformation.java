package infoObjects;

import java.awt.image.BufferedImage;

public class SpriteFrameInformation {
	private BufferedImage frameImage;
	private int drawPriority = 0;
	private int xOffset = 0;
	private int yOffset = 0;
	private boolean isEditableImage = false;
	
	public SpriteFrameInformation
	(	BufferedImage img, 
		int drawPriority,
		int x,
		int y)
	{
		frameImage = img;
		this.drawPriority = drawPriority;
		xOffset = x;
		yOffset = y;
	}
	public int getDrawPriority() {
		return drawPriority;
	}
	public void setDrawPriority(int drawPriority) {
		this.drawPriority = drawPriority;
	}
	public BufferedImage getFrameImage() {
		return frameImage;
	}
	public void setFrameImage(BufferedImage frameImage) {
		this.frameImage = frameImage;
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
	public boolean isEditableImage() {
		return isEditableImage;
	}
	public void setEditableImage(boolean isEditableImage) {
		this.isEditableImage = isEditableImage;
	}
}
