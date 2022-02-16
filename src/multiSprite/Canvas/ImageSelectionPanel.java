package multiSprite.Canvas;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import infoObjects.SpriteFrame;
import spriteMaker.Frames.SpriteFrameCanvas;

public class ImageSelectionPanel extends JPanel {
	
	SpriteFrame spriteFrame;
	SpriteFrameCanvas display;
	
    public ImageSelectionPanel() {
    	super();
    	
    	spriteFrame = new SpriteFrame();
    	display = new SpriteFrameCanvas(spriteFrame);
    	
    	JRadioButton pigButton = new JRadioButton();
    	
    	pigButton.add(display);
    	
    	ButtonGroup group = new ButtonGroup();
    }
}
