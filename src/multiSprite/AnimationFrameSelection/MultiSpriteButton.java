package multiSprite.AnimationFrameSelection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import componentInterface.ButtonCommandsInterface;
import componentInterface.MultiSpriteFrameInterface;
import infoObjects.SpriteFrameSet;
import multiSprite.Canvas.MultiSpriteCanvas;

public class MultiSpriteButton extends JButton implements ActionListener{
	//SpriteFrameSet spriteFrameSet;
	MultiSpriteDisplay buttonDisplay;
	MultiSpriteFrameInterface multiSpriteInterface;
	ButtonCommandsInterface buttonCommandsInterface;
	
    public MultiSpriteButton(MultiSpriteFrameInterface msi,
    		ButtonCommandsInterface bci, SpriteFrameSet sfs) {
		super();
		multiSpriteInterface = msi;
		buttonCommandsInterface = bci;
		buttonDisplay = new MultiSpriteDisplay(sfs);
		this.add(buttonDisplay);
		this.addActionListener(this);
		multiSpriteInterface.setSpriteFrames(getSpriteFrameSet());
	}
    
    public SpriteFrameSet getSpriteFrameSet() {
    	return buttonDisplay.getSpriteFrames();
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		multiSpriteInterface.setSpriteFrames(getSpriteFrameSet());
		multiSpriteInterface.updateView();
		buttonCommandsInterface.onButtonPressed(this.getActionCommand());
	}
	
	public void setSpriteFrames(SpriteFrameSet sfs) {
		buttonDisplay.setSpriteFrames(sfs);
		multiSpriteInterface.setSpriteFrames(getSpriteFrameSet());
	}
}
