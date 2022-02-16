package spriteMaker.Frames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

import componentInterface.CanvasInterface;
import componentInterface.FramePanelInterface;
import infoObjects.SpriteFrame;

public class SpriteFrameButton extends JButton implements ActionListener{

	//CanvasInterface canvas;
	FramePanelInterface framePanel;
	SpriteFrame spriteFrame;
	SpriteFrameCanvas display;
	
	SpriteFrameButton(FramePanelInterface fpi, 
			SpriteFrame sf) {
		super();
		framePanel = fpi;
		addActionListener(this);
		setBackground(Color.lightGray);
		//setSize(sf.getImage().getWidth() + 20, sf.getImage().getHeight() + 20);
		setSpriteFrame(sf);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		framePanel.setSpriteFrame(spriteFrame, this);
	}
	
	public void deselect() {
		setBackground(Color.lightGray);
	}
	
	public SpriteFrameCanvas getDisplay() {
		return display;
	}
	
	public void resizeFrame(int width, int height) {
		display.getFrame().resizeImage(width, height);
		display.setSize(width, height);
		display.setPreferredSize(new Dimension(width, height));
		framePanel.setSpriteFrame(spriteFrame, this);
		this.repaint();
	}
	
	public void setSpriteFrame(SpriteFrame sf) {
		spriteFrame = sf;
		if (display != null) {
			remove(display);
		}
		display = new SpriteFrameCanvas(spriteFrame);
		add(display);
		this.repaint();
	}

}
