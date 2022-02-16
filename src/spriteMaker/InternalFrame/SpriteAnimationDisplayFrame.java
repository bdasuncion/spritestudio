package spriteMaker.InternalFrame;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import infoObjects.SpriteAnimationSet;
import spriteMaker.Animation.SpriteAnimationPanel;
import spriteMaker.Animation.TileViewOption;

public class SpriteAnimationDisplayFrame extends JInternalFrame {
	private SpriteAnimationPanel spriteanimation;
	private TileViewOption tileViewOption;
	private JScrollPane animationScrollView;
	
    public SpriteAnimationDisplayFrame(SpriteAnimationSet animation) {
    	super("Preview", true, true);
    	spriteanimation = new SpriteAnimationPanel(animation);
		tileViewOption = new TileViewOption(spriteanimation);
		//spriteframes.setAnimationInterface(spriteanimation);
		
		animationScrollView = new JScrollPane(spriteanimation);
		animationScrollView.setWheelScrollingEnabled(false);

		JPanel animationPanel = new JPanel();
		animationPanel.setLayout(new BorderLayout());
		animationPanel.add(animationScrollView, BorderLayout.CENTER);
		animationPanel.add(tileViewOption, BorderLayout.SOUTH);

		add(animationPanel);
		setSize(100, 100);
		setVisible(true);
    }
    
    public SpriteAnimationPanel getSpriteAnimationPanel() {
    	return spriteanimation;
    }
}
