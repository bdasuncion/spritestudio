package multiSprite.Animation;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.Timer;

import infoObjects.SpriteFrame;
import infoObjects.SpriteFrameSet;

public class MultiSpriteAnimationPanel extends JPanel implements ActionListener {
    private Vector<SpriteFrameSet> animation;
    private int scale;
    private int framesPassed;
    private int currentFrame;
    private int baseX;
    private int baseY;
    private Timer animationUpdate;
    private int sixtyFramesPerSecond = 1000/60;
    private SpriteFrameSet forDisplay;
    
    public MultiSpriteAnimationPanel() {
    	scale = 4;
    	framesPassed = 0;
    	currentFrame = 0;
    	baseX = 16;
    	baseY = 16;
    	animationUpdate = new Timer(sixtyFramesPerSecond, this);
    	animationUpdate.start();
    	this.enableEvents(AWTEvent.MOUSE_EVENT_MASK|AWTEvent.MOUSE_WHEEL_EVENT_MASK);
    }
    
    public void paintComponent(Graphics g) {
    	
    	if (forDisplay == null || forDisplay.getSpriteFrames().size() < 1) {
    		return;
    	}

    	super.paintComponent(g);
 		Graphics2D g2D =(Graphics2D)g;
 		BufferedImage disp;
 		
 		Vector<SpriteFrame> spriteSet = forDisplay.getSpriteFrames();
 		
 		for (int i = 0; i < spriteSet.size(); ++i) {
 			SpriteFrame spriteFrame = spriteSet.get(i);
 			disp = g2D.getDeviceConfiguration().
 		 			createCompatibleImage(spriteFrame.getImage().getHeight(),
 		 				spriteFrame.getImage().getWidth());
 			
 			int flipYOffset = 0, flipXOffset = 0, flipVertical = 1, flipHorizontal = 1;
 			
 			if (spriteFrame.isFlippedVertical()) {
 				flipYOffset= spriteFrame.getImage().getHeight()*scale;
 				flipVertical = -1;
 			}
 			
 			if (spriteFrame.isFlippedHorizontal()) {
 				flipXOffset = spriteFrame.getImage().getWidth()*scale;
 				flipHorizontal = -1;
 			}
 			
 			
 			disp = spriteFrame.getImage();
 			g2D.drawImage(disp, baseX*scale + (spriteFrame.getXOffset()*scale) + flipXOffset, 
 					baseY*scale + (spriteFrame.getYOffset()*scale) + flipYOffset, 
 					flipHorizontal*spriteFrame.getImage().getWidth()*scale,
 		 			flipVertical*spriteFrame.getImage().getHeight()*scale, this);

 		}
 		
 		g2D.dispose();
 	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (getAnimation() == null || getAnimation().size() < 1) {
    		return;
    	}
		
		if (currentFrame >= getAnimation().size()) {
 			currentFrame = 0;
 		}
		
		forDisplay = getAnimation().get(currentFrame);
		
		int displayForNumberOfFrames = getAnimation().get(currentFrame).getDisplayForNumberOfFrames();
		
		++framesPassed;
		if (framesPassed >= displayForNumberOfFrames) {
			++currentFrame;
			framesPassed = 0;
		}
		
		if (forDisplay.getSpriteFrames().size() < 1) {
			return;
		}
		
		this.repaint();
	}

	private Vector<SpriteFrameSet> getAnimation() {
		return animation;
	}

	public void setAnimation(Vector<SpriteFrameSet> animation) {
		this.animation = animation;
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
}
