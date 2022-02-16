package infoObjects;

import java.awt.image.BufferedImage;
import java.util.Vector;

import tools.ImageTools;

public class SpriteAnimationSet {
	private Vector<SpriteFrame> spriteFrames;

	public SpriteAnimationSet() {
	    spriteFrames = new Vector<SpriteFrame>();
	} 
	
	public Vector<SpriteFrame> getSpriteFrames() {
		return spriteFrames;
	}

	public void setSpriteFrames(Vector<SpriteFrame> spriteFrames) {
		this.spriteFrames = spriteFrames;
	}
	
	public SpriteFrame get(int idx) {
		if (idx >= spriteFrames.size()) {
			return null;
		}
		
		return spriteFrames.get(idx);
	}
	
	public void add(BufferedImage img) {
		SpriteFrame spriteFrame = new SpriteFrame(img);
		spriteFrames.add(spriteFrame);
	}
	
	public void add(int width, int height, int pixelSize) {
	    BufferedImage img = ImageTools.createEmptyImage(width, height, pixelSize);
	    add(img);
	}
	
	public void removeAt(int idx) {
		if (idx >= 0 && idx < spriteFrames.size() && spriteFrames.size() > 1) {
			spriteFrames.remove(idx);	
		}
	}
	
	public int getSize() {
		return spriteFrames.size();
	}
	
	public void exchangeWithPreviousFrame(int fromIdx) {
		if (fromIdx == 0) {
			return;
		}
		
		SpriteFrame from = spriteFrames.get(fromIdx);
		int toIdx = fromIdx - 1;
		SpriteFrame to = spriteFrames.get(toIdx);
		spriteFrames.set(toIdx, from);
		spriteFrames.set(fromIdx, to);
	}
	
    public void exchangeWithNextFrame(int fromIdx) {
    	if (fromIdx == getSize() - 1) {
			return;
		}
		
		SpriteFrame from = spriteFrames.get(fromIdx);
		int toIdx = fromIdx + 1;
		SpriteFrame to = spriteFrames.get(toIdx);
		spriteFrames.set(toIdx, from);
		spriteFrames.set(fromIdx, to);
	}
    
    public void exchangeFrames(int fromIdx, int toIdx) {
    	if (fromIdx >= spriteFrames.size() || 
    		toIdx >= spriteFrames.size() ||
    		fromIdx < 0 || toIdx < 0) {
    		return;
    	}
    	
    	SpriteFrame from = spriteFrames.get(fromIdx);
    	SpriteFrame to = spriteFrames.get(toIdx);
		spriteFrames.set(toIdx, from);
		spriteFrames.set(fromIdx, to);
    }
}
