package multiSprite.Canvas;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import componentInterface.MultiSpriteFrameInterface;
import componentInterface.UpdaterInterface;
import infoObjects.SpriteFrame;
import infoObjects.SpriteFrameSet;

public class MultiSpriteCanvas extends JPanel implements MultiSpriteFrameInterface {
	
    private SpriteFrameSet spriteFrames;
    private int scale;
    private int baseX, baseY;
    private UpdaterInterface updaterInterface;
    private Point dragStart;
    
     public MultiSpriteCanvas() {
    	 super();
    	 
    	 //this.enableEvents(AWTEvent.MOUSE_EVENT_MASK|AWTEvent.MOUSE_WHEEL_EVENT_MASK);
    	 this.enableEvents(AWTEvent.MOUSE_WHEEL_EVENT_MASK);
    	 this.setVisible(true);
    	 scale = 4;
    	 //baseX = 32;
    	 //baseY = 16;
    	 baseX = 16;
    	 baseY = 16;
    	 
    	 ShiftImage controls = new ShiftImage();
 		this.addMouseListener(controls);
 		this.addMouseMotionListener(controls);
     }
    
    public void paintComponent(Graphics g) {
    	
    	if (spriteFrames == null) {
    		return;
    	}
 		super.paintComponent(g);
 		Graphics2D g2D =(Graphics2D)g;
 		BufferedImage disp;
 		
 		g2D.scale(scale, scale);
		
		g2D.translate(baseX/scale, baseY/scale);
		
 		Vector<SpriteFrame> spriteSet = spriteFrames.getSpriteFrames();
 		if (spriteSet.size() <= 0) {	
 			return;
 		}
 		
 		for (int i = 0; i < spriteSet.size(); ++i) {
 			SpriteFrame spriteFrame = spriteSet.get(i);
 			disp = g2D.getDeviceConfiguration().
 		 			createCompatibleImage(spriteFrame.getImage().getHeight(),
 		 				spriteFrame.getImage().getWidth());
 			
 			int flipYOffset = 0, flipXOffset = 0, flipVertical = 1, flipHorizontal = 1;
 			
 			if (spriteFrame.isFlippedVertical()) {
 				flipYOffset= spriteFrame.getImage().getHeight();
 				flipVertical = -1;
 			}
 			
 			if (spriteFrame.isFlippedHorizontal()) {
 				flipXOffset = spriteFrame.getImage().getWidth();
 				flipHorizontal = -1;
 			}
 			
 			
 			disp = spriteFrame.getImage();
 			//g2D.drawImage(disp, baseX*scale + (spriteFrame.getXOffset()*scale) + flipXOffset, 
 			//		baseY*scale + (spriteFrame.getYOffset()*scale) + flipYOffset, 
 			//		flipHorizontal*spriteFrame.getImage().getWidth()*scale,
 		 	//		flipVertical*spriteFrame.getImage().getHeight()*scale, this);
 			
 			g2D.drawImage(disp, (spriteFrame.getXOffset()) + flipXOffset, 
 					(spriteFrame.getYOffset()) + flipYOffset, 
 					flipHorizontal*spriteFrame.getImage().getWidth(),
 		 			flipVertical*spriteFrame.getImage().getHeight(), this);

 		}
 		
 		g2D.dispose();
 	}

    @Override
	public SpriteFrameSet getSpriteFrames() {
		return spriteFrames;
	}

    @Override
	public void setSpriteFrames(SpriteFrameSet spriteFrames) {
		this.spriteFrames = spriteFrames;
		if (updaterInterface != null) {
			updaterInterface.onUpdate();
		}
	}

	@Override
	public void updateView() {
		this.repaint();
	}
	
	public void setUpdater(UpdaterInterface uinterface) {
		updaterInterface = uinterface;
	}
	
	public void setScale(int s) {
		scale = s;
		repaint();
	}
	
	public void processMouseWheelEvent(MouseWheelEvent e) {
		if(e.getWheelRotation()>0 && scale > 1){
			scale--;
		}
		else if((e.getWheelRotation() < 0)){
			scale++;
		}
		this.setBounds(0, 0, this.getWidth(), this.getHeight());
		this.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		this.revalidate();
		this.repaint();
	}
	
	protected class ShiftImage extends MouseInputAdapter {
		int xTileSetPostion;
		int yTileSetPostion;
		int snapToWidth = 16;
		int snapToHeight = 16;
		public void mousePressed(MouseEvent e) {
			if(e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON1) {
				dragStart = e.getPoint();
			}
		}
		
		@Override
    	public void mouseDragged(MouseEvent e) {
			int dragX = e.getX();
			int dragY = e.getY();
			
			if (SwingUtilities.isLeftMouseButton(e)) {
				Point dragEnd = e.getPoint();
                baseX += (dragEnd.x - dragStart.x);
                baseY += (dragEnd.y - dragStart.y);
                dragStart = dragEnd;
                repaint();
			}
			
			repaint();
        }
	}
}
