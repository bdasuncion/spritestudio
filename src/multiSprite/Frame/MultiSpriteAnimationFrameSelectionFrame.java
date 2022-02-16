package multiSprite.Frame;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;

import componentInterface.MultiSpriteFrameInterface;
import componentInterface.MultispriteFrameSetInterface;
import infoObjects.MultiSpriteAnimation;
import multiSprite.AnimationFrameSelection.MultiSpriteFrameSelectionControl;
import multiSprite.AnimationFrameSelection.MultiSpritePanel;

public class MultiSpriteAnimationFrameSelectionFrame extends JInternalFrame {
	MultiSpritePanel multiSpritePanel;
	MultiSpriteFrameSelectionControl multiSpriteFrameSelectionControl;
	JScrollPane frameView;
    public MultiSpriteAnimationFrameSelectionFrame(MultiSpriteFrameInterface msi,
    		MultiSpriteAnimation msa) {
    	super("", true, true);
    	this.setVisible(true);
		this.show();
		
    	multiSpritePanel = new MultiSpritePanel(msi, msa);
    
    	frameView = new JScrollPane(multiSpritePanel);
    	
    	multiSpriteFrameSelectionControl = new MultiSpriteFrameSelectionControl(multiSpritePanel);
    	this.add(multiSpriteFrameSelectionControl, BorderLayout.WEST);
    	//this.add(multiSpritePanel, BorderLayout.EAST);
    	this.add(frameView, BorderLayout.CENTER);
    	this.pack();
    }
    
    public MultispriteFrameSetInterface getMultispriteFrameSetInterface() {
    	return multiSpritePanel;
    }
}
