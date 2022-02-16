package multiSprite.Frame;

import javax.swing.JInternalFrame;

import infoObjects.MultiSpriteAnimation;
import multiSprite.Animation.MultiSpriteAnimationPanel;

public class MultiSpriteAnimationFrame extends JInternalFrame {
	MultiSpriteAnimationPanel animationPanel;
    public MultiSpriteAnimationFrame() {
    	animationPanel = new MultiSpriteAnimationPanel();
    	this.add(animationPanel);
    	this.setVisible(true);
    	this.setResizable(true);
    	this.pack();
    }
    
    public void setMultiSpriteAnimation(MultiSpriteAnimation msa) {
    	animationPanel.setAnimation(msa.getAnimation());
    }
}
