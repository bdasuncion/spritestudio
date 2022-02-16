package spriteMaker.Frames;

import java.awt.*;
import java.awt.image.IndexColorModel;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import javax.swing.JPanel;

import infoObjects.SpriteFrame;

public class SpriteFrameCanvas extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4409795423224082893L;
	private SpriteFrame spriteFrame;

	public SpriteFrameCanvas(BufferedImage image)
	{	
		super();
		this.setSize(image.getWidth(), image.getHeight());
		this.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		spriteFrame = new SpriteFrame(image);
	}
	
	public SpriteFrameCanvas(SpriteFrame frame) {
		super();
		spriteFrame = frame;
		this.setSize(spriteFrame.getImage().getWidth(), spriteFrame.getImage().getHeight());
		this.setPreferredSize(new Dimension(spriteFrame.getImage().getWidth(),
				spriteFrame.getImage().getHeight()));
	}
	
	public void updatePalette(IndexColorModel cm) {
		spriteFrame.updatePalette(cm);
		this.repaint();
	}
	
	public void updateImage(Raster r)
	{
		spriteFrame.updateImage(r);
		this.repaint();
	}
	
	public SpriteFrame getFrame() {
		return spriteFrame;
	}
		
	public void paintComponent(Graphics g) {
		BufferedImage editableImage = spriteFrame.getImage();
		super.paintComponent(g);
		Graphics2D g2D =(Graphics2D)g;
		BufferedImage disp;
		disp = g2D.getDeviceConfiguration().
				createCompatibleImage(editableImage.getHeight(),
				editableImage.getWidth());
		disp = editableImage;
		g2D.drawImage(disp, 0, 0, 
				editableImage.getWidth(),editableImage.getHeight(), this);
		g2D.dispose();
	}
} 