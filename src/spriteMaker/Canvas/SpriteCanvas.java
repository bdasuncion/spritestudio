package spriteMaker.Canvas;

import java.awt.*;
import javax.imageio.*;
import javax.swing.JPanel;

import componentInterface.CanvasButtonPanelInterface;
import componentInterface.CanvasInterface;
import componentInterface.ResizeInterface;
import componentInterface.SelectRGBInterface;
import componentInterface.SpriteLayerPanelInterface;
import infoObjects.SpriteFrame;
//import infoObjects.SpriteFrameInformation;
import spriteMaker.Frames.SpriteFrameCanvas;
import tools.ImageTools;

import java.awt.image.IndexColorModel;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import java.util.Vector;

public class SpriteCanvas extends JPanel implements CanvasInterface,
	SelectRGBInterface
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1754145642341359037L;
	private BufferedImage bgImage;
	private BufferedImage bgCover;
	private String title;
	private int scale = 1;
	private int rgbVal = 0;
	private double startX,startY;
	private int displayX,displayY;
	private boolean button1 = false;
	private boolean imageBGTrans = false;
	private int tileWidth = 8, tileHeight = 8;
//	Vector<SpriteFrameInformation> allImagesAtCanvasSameIdx;
	private SpriteLayerPanelInterface spriteLayerPanelInterface;
	private boolean displayAll = false;
	private boolean gridLineVisible = false;
	private CanvasButtonPanelInterface canvasButtonPanelInterface;
	private SpriteFrame spriteFrame;
	ResizeInterface resizeInterface;
	int copyWidth = 1, copyHeight = 1, copyRGB[];
	BufferedImage copiedImage;
	
	public SpriteCanvas(int width, int height, int pixelSize, ResizeInterface ri) {
		super();
		this.resizeInterface = ri;
		this.setSize(width,height);
		this.enableEvents(AWTEvent.MOUSE_EVENT_MASK|AWTEvent.MOUSE_WHEEL_EVENT_MASK|AWTEvent.MOUSE_MOTION_EVENT_MASK|AWTEvent.KEY_EVENT_MASK);
		
		copyRGB = new int[width*height];
		
		bgImage = ImageTools.createMottledTile(
				width, height, tileWidth, tileHeight);
		bgCover = ImageTools.createEmptyImage(width, height, pixelSize);
		this.setVisible(true);
		title = String.valueOf(width) + "x" + String.valueOf(height) + 
		"x" + String.valueOf(pixelSize);
		
		setPreferredSize(new Dimension(width*scale, height*scale));
//		allImagesAtCanvasSameIdx = new Vector<SpriteFrameInformation>();
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    cursorImg, new Point(0, 0), "blank cursor");
		
		setCursor(blankCursor);
	}
	
	public void setSpriteLayerPanelInterface(SpriteLayerPanelInterface slpInterface){
		spriteLayerPanelInterface = slpInterface;
	}
	
	public void setTileWidth(int w){
		tileWidth = w;
	}
	
	public void setTileHeight(int h){
		tileHeight = h;
	}
	
	public boolean isImageBGTransparent(){
		return imageBGTrans;
	}
	
	public void setImageBGTransparent(boolean trans){
		imageBGTrans = trans;
	}
	
	public void setImageBGtransparent(){
		bgCover = ImageTools.makeImageBGTransparent(bgCover);
		spriteFrame.setImageBGTransparent(true);
		imageBGTrans = true;
		this.repaint();
	}
	
	public void setImageBGOpaque(){
		bgCover = ImageTools.makeImageBGOpaque(bgCover);
		spriteFrame.setImageBGTransparent(false);
		imageBGTrans = false;
		this.repaint();
	}
	
	public void setGridLinesVisible(boolean visible){
		gridLineVisible = visible;
		this.repaint();
	}
	
	public void shiftImageUp(){
		ImageTools.imageShiftUp(spriteFrame.getImage());
		this.repaint();
	}
	
	public void shiftImageDown(){
		ImageTools.imageShiftDown(spriteFrame.getImage());
		this.repaint();
	}
	
	public void shiftImageLeft(){
		ImageTools.imageShiftLeft(spriteFrame.getImage());
		this.repaint();
	}
	
	public void shiftImageRight(){
		ImageTools.imageShiftRight(spriteFrame.getImage());
		this.repaint();
	}
	
	public void flipImageVertical(){
		ImageTools.imageFlipVertical(spriteFrame.getImage());
		this.repaint();
	}
	
	public void flipImageHorizontal(){
		ImageTools.imageFlipHorizontal(spriteFrame.getImage());
		this.repaint();
	}
	
	public void setMottledTileBG(){
		setBGImage();
		this.repaint();
	}
	
	public void setXOffset(String cmd){
		
		if (cmd == CanvasButtonPanel.INCREASE_TRANSPARENCY) {
		    spriteFrame.updateAlpha(128);
		} else if (cmd == CanvasButtonPanel.DECREASE_TRANSPARENCY) {
			spriteFrame.updateAlpha(255);
		}
		this.repaint();
	}
	
	public void setYOffset(String cmd){
		
	}
	
	public void tileResize(int newWidth, int newHeight){
		tileWidth = newWidth;
		tileHeight = newHeight;
		setMottledTileBG();
	}	
	
	public void setMottledPixelBG(){
		tileWidth = 1;
		tileHeight = 1;
		setBGImage();
		this.repaint();
	}
	
	public String getTitle(){
		return title;
	}
	
	public IndexColorModel getImageColorModel(){	
		return spriteFrame.getImageColorModel();
	}
	
	public BufferedImage getImage(){
		return spriteFrame.getImage();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D =(Graphics2D)g;
		BufferedImage disp;
		
		if (spriteFrame == null) {
			return;
		}
		
		g2D.scale(scale, scale);
		//System.out.print("DISPLAY SPRITE FRAME ON CANVAS");
		disp = g2D.getDeviceConfiguration().
			createCompatibleImage(spriteFrame.getImage().getHeight(),
				spriteFrame.getImage().getWidth());
		
		disp = bgImage;
		g2D.drawImage(disp, 0, 0, 
				bgImage.getWidth(),bgImage.getHeight(), this);

		disp = bgCover;
		g2D.drawImage(disp, 0, 0, 
				bgCover.getWidth(),bgCover.getHeight(), this);
		
		disp = spriteFrame.getImage();
		g2D.drawImage(disp, 0, 0, 
			spriteFrame.getImage().getWidth(),
			spriteFrame.getImage().getHeight(), this);
		
		if (spriteFrame.isDrawMode()) {
			drawBrush(g2D, displayX, displayY, rgbVal);
		} else {
			drawCopiedBlock(g2D, disp);
		}
		
		if (gridLineVisible) {
		    drawGrid(g2D, disp);
		}
		
		g2D.dispose();
	}
	
	private void drawCopiedBlock(Graphics2D g2D, BufferedImage display) {
		
		int width = spriteFrame.getCopyBlockWidth(), height = spriteFrame.getCopyBlockHeight();
		if (copiedImage != null) {
			display = copiedImage;
			g2D.drawImage(display, displayX, displayY, 
					copiedImage.getWidth(),
					copiedImage.getHeight(), this);
			width = copiedImage.getWidth();
			height = copiedImage.getHeight();
		}
		
		g2D.setStroke(new BasicStroke(0.15f));
		g2D.drawLine(displayX, displayY, displayX + width, displayY);
		g2D.drawLine(displayX, displayY, displayX, displayY + height);
		g2D.drawLine(displayX + width, displayY, displayX + width, displayY + height);
		g2D.drawLine(displayX, displayY + height, displayX + width, displayY + height);
	}
	
	private void drawBrush(Graphics2D g2D, int displayX, int displayY, int rgbVal){
		if (displayX < spriteFrame.getImage().getWidth() && 
				displayY < spriteFrame.getImage().getHeight()) {
			g2D.setColor(new Color(rgbVal));
			g2D.fillRect(displayX, displayY, spriteFrame.getCopyBlockWidth(), spriteFrame.getCopyBlockHeight());
		}		
	}
	
	private void drawGrid(Graphics2D g2D, BufferedImage display) {
		g2D.setStroke(new BasicStroke(0.2f));
		g2D.setColor(Color.WHITE);
		
		for (int i = 0; i <= display.getWidth()/tileWidth; ++i) {
			g2D.drawLine(tileWidth*i, 0, tileWidth*i, display.getHeight());
		}
		
		for (int i = 0; i <= display.getHeight()/tileHeight; ++i) {
			g2D.drawLine(0, tileHeight*i, display.getWidth(), tileHeight*i);
		}
	}
	
	private int convertMouseX(MouseEvent e) {
		return (e.getX()/scale);
	}
	
	private int convertMouseY(MouseEvent e) {
		return (e.getY()/scale);
	}
	
	public void processMouseEvent(MouseEvent e){
		
		if(spriteFrame == null) {
			return;
		}
		
		if(e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON3) {
			rgbVal = spriteFrame.getImage().getRGB(convertMouseX(e), convertMouseY(e));
			button1 = false;
			copiedImage = spriteFrame.copyAt(convertMouseX(e), convertMouseY(e), 0, 0, copyRGB);
			
		} else if(e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON1) {
			if(e.getX()/scale < spriteFrame.getImage().getWidth() && 
					e.getY()/scale < spriteFrame.getImage().getHeight()) {
				spriteFrame.drawAt(convertMouseX(e), convertMouseY(e), rgbVal);
			}
			startX = convertMouseX(e);
			startY = convertMouseY(e);
			button1 = true;
		}
		repaint();
	}
	
	public void processMouseWheelEvent(MouseWheelEvent e) {
		if(e.getWheelRotation()>0 && scale > 1){
			scale--;
		}
		else if((e.getWheelRotation() < 0)){
			scale++;
		}
		displayX = convertMouseX(e);
		displayY = convertMouseY(e);
		this.setBounds(0, 0, this.getWidth(), this.getHeight());
		//this.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		this.setPreferredSize(new Dimension(spriteFrame.getImage().getWidth()*scale, 
				spriteFrame.getImage().getHeight()*scale));
		this.revalidate();
		this.repaint();
	}
	
	public void processMouseMotionEvent(MouseEvent e) {
		if (MouseEvent.MOUSE_MOVED == e.getID()) {
			displayX = convertMouseX(e);
			displayY = convertMouseY(e);
			this.repaint();
		}
		
		if(e.getID() == MouseEvent.MOUSE_DRAGGED && button1 == true){
			int x = convertMouseX(e);
			int y = convertMouseY(e);
			drawLine(startX, startY, x, y);
			startX = x;
			startY = y;
			displayX = x;
			displayY = y;
			this.repaint();
		}
	}
	
	public void processKeyEvent(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_UP){
			scale++;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN && scale > 1){
			scale--;
		}
		this.setBounds(0, 0, this.getWidth(), this.getHeight());
		this.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		this.revalidate();		
		this.repaint();
		
	}
	
	private void drawLine(double startX, double startY, double endX, double endY){
		double xDist = startX - endX;
		double yDist = startY - endY;
		double numPixelSet = Math.max(Math.abs(xDist), Math.abs(yDist));
		double xOffsetDist = xDist/numPixelSet;
		double yOffsetDist = yDist/numPixelSet;
		double xOffset = 0, yOffset = 0;
		
		for(int i = 0; i<=numPixelSet; i++){
			int pointX = (int)(startX + xOffset);
			int pointY = (int)(startY + yOffset);
			if(pointX < spriteFrame.getImage().getWidth() && pointX >= 0 &&
				pointY < spriteFrame.getImage().getHeight() && pointY >= 0) {
				spriteFrame.drawAt(pointX, pointY, rgbVal);
			}
			xOffset -= xOffsetDist;
			yOffset -= yOffsetDist;
		}
	}

	public void setDisplayAll(boolean displayAll) {
		this.displayAll = displayAll;
		this.setBGImage();
		this.repaint();
	}
	
	private void setBGImage() {
		
		int maxX = spriteFrame.getImage().getWidth();
		int maxY = spriteFrame.getImage().getHeight();
		
		bgCover = ImageTools.createEmptyImage(maxX, maxY, spriteFrame.getImage().getColorModel().getPixelSize());
		bgImage = ImageTools.createMottledTile(
				maxX, maxY, tileWidth, tileHeight);
		if(imageBGTrans == true){
			bgCover = 
				ImageTools.makeImageBGTransparent(bgCover);
		}else{
			bgCover = 
				ImageTools.makeImageBGOpaque(bgCover);
		}
	}

	@Override
	public void setCanvasButtonPanelInterface(CanvasButtonPanelInterface cbpInterface) {
		canvasButtonPanelInterface = cbpInterface;
	}
	
	@Override
	public CanvasButtonPanelInterface getCanvasButtonPanelInterface(){
		return canvasButtonPanelInterface;
	}

	@Override
	public void setSpriteFrame(SpriteFrame sp) {
		spriteFrame = sp;
		bgImage = ImageTools.createMottledTile(
			spriteFrame.getImage().getWidth(), 
			spriteFrame.getImage().getHeight(), tileWidth, tileHeight);
		bgCover = ImageTools.createEmptyImage(spriteFrame.getImage().getWidth(), spriteFrame.getImage().getHeight(), 
			spriteFrame.getImage().getColorModel().getPixelSize());
		resizeInterface.onResize(spriteFrame.getImage().getWidth(), spriteFrame.getImage().getHeight());
		repaint();
	}
	
	public SpriteFrame getSpriteFrame() {
		return spriteFrame;
	}

	@Override
	public void setRGB(int rgb){
		if(imageBGTrans == true)
			this.setImageBGtransparent();
		else
			this.setImageBGOpaque();
		rgbVal = rgb;
		this.repaint();
	}
}