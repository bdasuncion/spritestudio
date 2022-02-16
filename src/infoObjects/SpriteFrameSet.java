package infoObjects;

import java.util.Vector;

public class SpriteFrameSet {
    private Vector<SpriteFrame> spriteFrames;
    private int displayForNumberOfFrames = 5;
    private int currentIdx = 0;
    private int baseFrameIdx = 0;
    
    public SpriteFrameSet() {
    	spriteFrames = new Vector<SpriteFrame>();
    }
    
	public Vector<SpriteFrame> getSpriteFrames() {
		return spriteFrames;
	}
	
	public void setSpriteFrames(Vector<SpriteFrame> spriteFrames) {
		this.spriteFrames = spriteFrames;
	}
	
	public int getDisplayForNumberOfFrames() {
		return displayForNumberOfFrames;
	}
	
	public void setDisplayForNumberOfFrames(int displayForNUmberOfFrames) {
		this.displayForNumberOfFrames = displayForNUmberOfFrames;
	}
	
	public boolean increasePriority() {
		if (currentIdx <= 0) {
			return false;
		}
		SpriteFrame temp = spriteFrames.elementAt(currentIdx - 1);
		spriteFrames.setElementAt(spriteFrames.elementAt(currentIdx), currentIdx - 1);
		spriteFrames.setElementAt(temp, currentIdx);
		--currentIdx;
		return true;
	}
	
	public boolean decreasePriority() {
		if ((currentIdx + 1) >= (spriteFrames.size())) {
			return false;
		}
		SpriteFrame temp = spriteFrames.elementAt(currentIdx + 1);
		spriteFrames.setElementAt(spriteFrames.elementAt(currentIdx), currentIdx + 1);
		spriteFrames.setElementAt(temp, currentIdx);
		++currentIdx;
		return true;
	}
	
	public void addLayer(SpriteFrame layer) {
		spriteFrames.add(layer);
	}
	
	public void removeLayer() {
		if (currentIdx < 0 || currentIdx >= spriteFrames.size()) {
			return;
		}
		spriteFrames.remove(currentIdx);
		
		if (currentIdx >= spriteFrames.size()) {
			currentIdx = spriteFrames.size() - 1;
		}
		
		if (baseFrameIdx >= spriteFrames.size()) {
			baseFrameIdx = spriteFrames.size() - 1;
		}
	} 

	public void setCurrentIdx(int currentIdx) {
		this.currentIdx = currentIdx;
	}
	
	public SpriteFrame getCurrentSelected() {
		if (currentIdx < spriteFrames.size()) {
			return spriteFrames.get(currentIdx);	
		}
		return null;
	}

	public int getBaseFrameIdx() {
		return baseFrameIdx;
	}

	public void setBaseFrameIdx(int baseFrameIdx) {
		this.baseFrameIdx = baseFrameIdx;
	}
}
