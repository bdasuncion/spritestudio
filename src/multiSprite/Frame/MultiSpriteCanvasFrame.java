package multiSprite.Frame;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import componentInterface.CanvasInterface;
import componentInterface.ImportFramesInterface;
import componentInterface.MultiSpriteFrameInterface;
import componentInterface.MultispriteFrameSetInterface;
import componentInterface.SpriteFrameDetailsInterface;
import infoObjects.SpriteFrame;
import multiSprite.Canvas.MultiSpriteButtonPanel;
import multiSprite.Canvas.MultiSpriteCanvas;

public class MultiSpriteCanvasFrame extends JInternalFrame implements SpriteFrameDetailsInterface{
	
	private MultiSpriteButtonPanel canvasButtonPanel;
	private MultiSpriteCanvas canvas;
	public MultiSpriteCanvasFrame(ImportFramesInterface fsi) {
		super("", true, true);
		
		this.setLayout(new BorderLayout());
		this.setVisible(true);
		this.show();
		
		canvas = new MultiSpriteCanvas();
		canvasButtonPanel = new MultiSpriteButtonPanel(canvas, fsi, this);
		
		canvas.setUpdater(canvasButtonPanel);
		
		this.add(canvas, BorderLayout.CENTER);
		this.add(canvasButtonPanel, BorderLayout.SOUTH);
		
		this.pack();
	}
	
	public MultiSpriteFrameInterface getMultiSpriteFrameInterface() {
		return canvas;
	}
	
	public void setMultispriteFrameSetInterface(MultispriteFrameSetInterface msfsi) {
		canvasButtonPanel.setMultispriteFrameSetInterface(msfsi);
	}
	
    public CanvasInterface getCanvasInterface() {
    	return canvasButtonPanel.getCanvasInterface();
    }

	@Override
	public void setSpriteFrameDetails(SpriteFrame spriteFrame) {
		if (spriteFrame != null) {
			setTitle(spriteFrame.getImage().getWidth() + "x" + spriteFrame.getImage().getHeight() +
					" X: " + spriteFrame.getXOffset() + " Y: " + spriteFrame.getYOffset() +
					" VFlip: " + spriteFrame.isFlippedVertical() + " HFlip:" + spriteFrame.isFlippedHorizontal());
		}
	}
}
