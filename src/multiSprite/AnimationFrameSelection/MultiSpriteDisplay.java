package multiSprite.AnimationFrameSelection;

import java.awt.Dimension;

import infoObjects.SpriteFrameSet;
import multiSprite.Canvas.MultiSpriteCanvas;

public class MultiSpriteDisplay extends MultiSpriteCanvas {
    public MultiSpriteDisplay(SpriteFrameSet spriteFrameSet) {
    	super();
    	setSpriteFrames(spriteFrameSet);
    	//setMinimumSize(new Dimension(400, 400));
    	//setSize(200, 200);
    	
    	setPreferredSize(new Dimension(50, 50));
    	setMinimumSize(new Dimension(50, 50));
    	setMaximumSize(new Dimension(50, 50));
    	
    	setScale(1);
    }
}
