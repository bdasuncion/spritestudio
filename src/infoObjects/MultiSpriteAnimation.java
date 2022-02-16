package infoObjects;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

public class MultiSpriteAnimation {
    private Vector<SpriteFrameSet> animation;
    //private HashMap<String, String> files;
    private Vector<String> files;

    public MultiSpriteAnimation() {
    	animation = new Vector<SpriteFrameSet>();
    	//files = new HashMap<String, String>();
    	files = new Vector<String>();
    }
	public Vector<SpriteFrameSet> getAnimation() {
		return animation;
	}

	public void setAnimation(Vector<SpriteFrameSet> animation) {
		this.animation = animation;
	}
	
	public SpriteFrameSet createSpriteFrameSet() {
		SpriteFrameSet spriteFrameSet = new SpriteFrameSet();
		animation.add(spriteFrameSet);
		return spriteFrameSet;
	}
	
	public void deleteSpriteFrameSet(int idx) {
		if (animation.size() <= idx) {
			return;
		}
		animation.remove(idx);
	}
	
	public void addSourceFile(String sourceFile) {
		//files.put(sourceFile, sourceFile);
		for (String file: files) {
			if (file.contentEquals(sourceFile)) {
				return;
			}
		}
		files.add(sourceFile);
	}
	
	public Collection<String> getSourceFiles() {
		return files;
	}
    
}
