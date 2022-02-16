package componentInterface;

import java.util.Vector;

import javax.swing.JButton;

import infoObjects.SpriteFrame;

public interface FramePanelInterface {
	public void setSpriteFrame(SpriteFrame s, JButton b);
	public Vector<SpriteFrame> getAllSpriteFrames();
}
